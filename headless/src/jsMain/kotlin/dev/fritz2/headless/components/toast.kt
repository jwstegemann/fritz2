package dev.fritz2.headless.components

import dev.fritz2.binding.RootStore
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.ScopeContext
import dev.fritz2.headless.foundation.TagFactory
import dev.fritz2.identification.Id
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLElement

enum class ToastPosition {
    TopLeft, TopRight, TopCenter, BottomLeft, BottomRight, BottomCenter;

    companion object {
        val bottomPositions = listOf(BottomLeft, BottomRight, BottomCenter)
    }
}

class ToastFragment {
    var id: String = Id.next()
    var position: ToastPosition = ToastPosition.BottomRight
    var duration: Long = 5000L
    var isClosable: Boolean = false
    var content: RenderContext.() -> Unit = {}
}

object ToastStore : RootStore<List<ToastFragment>>(emptyList()) {

    val add = handle<ToastFragment> { toasts, toast -> toasts + toast }
    val remove = handle<String> { toasts, id -> toasts.filterNot { it.id == id } }

    fun onlyWithPosition(position: ToastPosition) = data
        .map { toasts -> toasts.filter { it.position == position } }
        .map {
            // New toasts should always be stacked on existing ones, which naturally depends on the
            // placement. Due to this, the list has to be reversed if the toast is rendered at the
            // bottom:
            if (position in ToastPosition.bottomPositions) it.asReversed() else it
        }
}

class HeadlessToasts(renderContext: RenderContext) : RenderContext by renderContext {

    /*
    private val toastContainerContext =
        ManagedComponent.managedRenderContext("headless-toasts-${Id.next()}", Job(), Scope())
     */

    var toastRendering: (RenderContext.(ToastFragment) -> Tag<HTMLElement>)? = null


    fun <T : Tag<HTMLElement>> headlessToastContainer(
        position: ToastPosition,
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<T>
    ) = tag(this, classes, id, scope) {
        toastRendering?.let { rendering ->
            ToastStore
                .onlyWithPosition(position)
                .renderEach { toast -> rendering(toast) }
        }
    }


    inner class HeadlessToast<T : Tag<HTMLElement>>(tag: T, private val toastId: String) : RenderContext by tag {

        fun <TC : Tag<HTMLElement>> headlessToastCloseButton(
            classes: String? = null,
            id: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<TC>,
            content: TC.() -> Unit
        ) = tag(this, classes, id, scope) {
            content()
            clicks.map { toastId } handledBy ToastStore.remove
        }
    }

    fun <T : Tag<HTMLElement>> headlessToast(
        toastId: String,
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<T>,
        initialize: HeadlessToast<T>.() -> Unit
    ) = tag(this, classes, id, scope) {
        HeadlessToast(this, toastId).run {
            initialize()
        }
    }
}

fun <C : RenderContext> RenderContext.headlessToasts(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<C>,
    initialize: HeadlessToasts.() -> Unit
) = tag(this, classes, id, scope) {
    HeadlessToasts(this).run {
        initialize()
    }
}


fun showToast(initialize: ToastFragment.() -> Unit) {
    val toastFragment = ToastFragment().apply(initialize)

    ToastStore.add(toastFragment)

    (MainScope() + Job()).launch {
        delay(toastFragment.duration)
        ToastStore.remove(toastFragment.id)
    }
}

fun handleAsToast(initialize: ToastFragment.() -> Unit) = object : RootStore<Unit>(Unit) {
    val showToast = handle<Unit> { _, _ -> showToast(initialize) }
}.showToast
