package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.TagFactory
import dev.fritz2.headless.foundation.addComponentStructureInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLLIElement
import org.w3c.dom.HTMLUListElement


enum class ToastPosition {
    TopLeft, TopCenter, TopRight, BottomLeft, BottomCenter, BottomRight;

    companion object {
        val bottomPositions = listOf(BottomLeft, BottomCenter, BottomRight)
    }
}


data class ToastFragment(
    val id: String,
    val position: ToastPosition,
    val content: RenderContext.() -> Tag<HTMLElement>
)

private object ToastStore : RootStore<List<ToastFragment>>(emptyList()) {

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


fun <C : HTMLElement> RenderContext.toasts(
    classes: String? = null,
    id: String? = null,
    position: ToastPosition,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>
): Tag<C> {
    addComponentStructureInfo("toasts (${position.name})", this.scope, this)
    return tag(this, classes, id, scope) {
        ToastStore.onlyWithPosition(position).renderEach(into = this) { fragment ->
            fragment.content(this)
        }
    }
}

fun RenderContext.toasts(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    position: ToastPosition
): Tag<HTMLUListElement> = toasts(classes, id, position, scope, RenderContext::ul)


class Toast<C : HTMLElement> internal constructor(tag: Tag<C>, private val toastId: String) : Tag<C> by tag {

    fun <CC : HTMLElement> toastCloseButton(
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CC>>,
        content: Tag<CC>.(Handler<Unit>) -> Unit
    ): Tag<CC> {
        addComponentStructureInfo("toast close-button", this.scope, this)
        return tag(this, classes, id, scope) {
            val closeHandler = storeOf(Unit).handle { ToastStore.remove(toastId) }
            content(closeHandler)
        }
    }

    fun toastCloseButton(
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLButtonElement>.(Handler<Unit>) -> Unit
    ): Tag<HTMLButtonElement> = toastCloseButton(classes, id, scope, RenderContext::button, content)
}

fun <C : HTMLElement> RenderContext.toast(
    classes: String? = null,
    id: String? = null,
    toastId: String = Id.next(),
    position: ToastPosition = ToastPosition.TopRight,
    duration: Long = 5000L,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: Toast<C>.() -> Unit
) {
    val toast = ToastFragment(toastId, position) {
        tag(this, classes, id, scope) {
            addComponentStructureInfo("parent is toast", this.scope, this)
            Toast(this, toastId).run(initialize)
        }
    }

    ToastStore.add(toast)

    (MainScope() + Job()).launch {
        delay(duration)
        ToastStore.remove(toast.id)
    }
}

fun RenderContext.toast(
    classes: String? = null,
    id: String? = null,
    toastId: String = Id.next(),
    position: ToastPosition = ToastPosition.TopRight,
    duration: Long = 5000L,
    scope: (ScopeContext.() -> Unit) = {},
    content: Toast<HTMLLIElement>.() -> Unit
) = toast(classes, id, toastId, position, duration, scope, RenderContext::li, content)
