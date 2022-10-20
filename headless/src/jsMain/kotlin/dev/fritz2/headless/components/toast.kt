package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.TagFactory
import dev.fritz2.headless.foundation.addComponentStructureInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map
import org.w3c.dom.*

data class ToastFragment<L>(
    val id: String,
    val location: L,
    val content: RenderContext.() -> Tag<HTMLElement>
)

private class ToastStore<L> : RootStore<List<ToastFragment<L>>>(emptyList()) {
    val add = handle<ToastFragment<L>> { toasts, toast -> toasts + toast }
    val remove = handle<String> { toasts, id -> toasts.filterNot { it.id == id } }

    fun withLocation(location: L) = data.map { toasts -> toasts.filter { it.location == location } }
}


class ToastsContext<C: HTMLElement, L> internal constructor(tag: Tag<C>) : Tag<C> by tag {

    private val toastStore = ToastStore<L>()


    inner class Toast<T : HTMLElement> internal constructor(tag: Tag<T>, private val toastId: String) : Tag<T> by tag {

        fun <TC : HTMLElement> toastCloseButton(
            classes: String? = null,
            id: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<TC>>,
            content: Tag<TC>.(Handler<Unit>) -> Unit
        ): Tag<TC> {
            addComponentStructureInfo("toast close-button", this.scope, this)
            return tag(this, classes, id, scope) {
                val closeHandler = storeOf(Unit).handle { toastStore.remove(toastId) }
                content(closeHandler)
            }
        }

        fun toastCloseButton(
            classes: String? = null,
            id: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLButtonElement>.(Handler<Unit>) -> Unit
        ): Tag<HTMLButtonElement> =
            toastCloseButton(classes, id, scope, RenderContext::button, content)
    }

    fun <C : HTMLElement> RenderContext.toast(
        classes: String? = null,
        id: String? = null,
        toastId: String = Id.next(),
        location: L,
        duration: Long = 5000L,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<C>>,
        initialize: Toast<C>.() -> Unit
    ) {
        val toast = ToastFragment(toastId, location) {
            tag(this, classes, id, scope) {
                addComponentStructureInfo("parent is toast", this.scope, this)
                Toast(this, toastId).run(initialize)
            }
        }

        toastStore.add(toast)

        // TODO: Kann man das so machen?
        (MainScope() + job).launch {
            delay(duration)
            toastStore.remove(toast.id)
        }
    }

    fun RenderContext.toast(
        classes: String? = null,
        id: String? = null,
        toastId: String = Id.next(),
        location: L,
        duration: Long = 5000L,
        scope: (ScopeContext.() -> Unit) = {},
        content: Toast<HTMLLIElement>.() -> Unit
    ): Unit =
        toast(classes, id, toastId, location, duration, scope, RenderContext::li, content)


    fun <T : HTMLElement> RenderContext.toastLocation(
        classes: String? = null,
        id: String? = null,
        location: L,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<T>>
    ): Tag<T> {
        addComponentStructureInfo("toasts ($location)", this.scope, this)
        return tag(this, classes, id, scope) {
            toastStore.withLocation(location).renderEach(into = this) { fragment ->
                fragment.content(this)
            }
        }
    }

    fun RenderContext.toastLocation(
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        location: L
    ): Tag<HTMLUListElement> =
        toastLocation(classes, id, location, scope, RenderContext::ul)
}

fun <C : HTMLElement, L> RenderContext.toasts(
    classes: String? = null,
    id: String? = null,
    scope: ScopeContext.() -> Unit = {},
    tag: TagFactory<Tag<C>>,
    initialize: ToastsContext<C, L>.() -> Unit
): Tag<C> =
    tag(this, classes, id, scope) {
        ToastsContext<C, L>(this).run(initialize)
    }

fun <L> RenderContext.toasts(
    classes: String? = null,
    id: String? = null,
    scope: ScopeContext.() -> Unit = {},
    initialize: ToastsContext<HTMLDivElement, L>.() -> Unit
): Tag<HTMLDivElement> =
    toasts(classes, id, scope, RenderContext::div, initialize)
