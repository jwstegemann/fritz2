package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.core.Window
import dev.fritz2.headless.foundation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.w3c.dom.*


private data class ToastSlice(
    val id: String,
    val containerName: String,
    val content: RenderContext.() -> Tag<HTMLElement>
)

private object ToastStore : RootStore<List<ToastSlice>>(emptyList(), job = Job()) {

    val add = handle<ToastSlice> { toasts, toast -> toasts + toast }
    val remove = handle<String> { toasts, id -> toasts.filterNot { it.id == id } }

    fun filteredByContainer(containerName: String) = data
        .map { toasts -> toasts.filter { it.containerName == containerName } }

    init {
        // Close the most recent toast if escape is pressed:
        Window.keydowns
            .map { it to current }
            .filter { (event, toasts) -> shortcutOf(event.key) == Keys.Escape && toasts.isNotEmpty() }
            .map { (_, toasts) -> toasts.last().id } handledBy remove
    }
}


/**
 * Factory function to create a [toastContainer].
 *
 * It is recommended to define some explicit z-index within the classes-parameter.
 *
 * For more information refer to the
 * [official documentation](https://www.fritz2.dev/headless/toast/#toastContainer)
 *
 * @param name the mandatory name of the container. This ultimately defines the container's identity.
 */
fun <E : HTMLElement> toastContainer(
    name: String,
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<E>>
) {
    PortalRenderContext.portal(classes, id, scope, tag) {
        addComponentStructureInfo("toast-container ($name)", this.scope, this)
        attrIfNotSet(Aria.live, "polite")
        ToastStore.filteredByContainer(name).renderEach(into = this) { fragment ->
            fragment.content(this)
        }
    }
}

/**
 * Factory function to create a [toastContainer] with a [HTMLUListElement] as default [Tag].
 *
 * It is recommended to define some explicit z-index within the classes-parameter.
 *
 * For more information refer to the
 * [official documentation](https://www.fritz2.dev/headless/toast/#toastContainer)
 *
 * @param name the mandatory name of the container. This ultimately defines the container's identity.
 */
fun toastContainer(
    name: String,
    classes: String? = null,
    id: String? = null,
    scope: ScopeContext.() -> Unit = {},
) = toastContainer(name, classes, id, scope, RenderContext::ul)


/**
 * This class provides the building blocks to implement a toast.
 *
 * Use [toast] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/toast/)
 */
class Toast<E : HTMLElement> internal constructor(tag: Tag<E>, private val toastId: String) : Tag<E> by tag {

    /**
     * Handler that dismisses the toast when invoked.
     */
    @Suppress("unused")
    val close: Handler<Unit> = storeOf(Unit).handle { ToastStore.remove(toastId) }
}

/**
 * Factory function to create a [Toast].
 *
 * API-Sketch:
 * ```kotlin
 * toastPosition() // use for each position that should be available
 * toast() { // use for each new toast
 *     toastCloseButton() { close ->
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/toast/#toast)
 *
 * @param containerName refer to the container the toast should be stored in
 * @param duration the duration a toast should be displayed on the screen
 */
fun <E : HTMLElement> toast(
    containerName: String,
    duration: Long = 5000L,
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<E>>,
    initialize: Toast<E>.() -> Unit
) {
    val toastId = Id.next()
    val toast = ToastSlice(toastId, containerName) {
        tag(this, classes, id, scope) {
            attrIfNotSet("role", Aria.Role.alert)
            addComponentStructureInfo("parent is toast ($toastId)", this.scope, this)
            Toast(this, toastId).run(initialize)
        }
    }

    MainScope().launch {
        val currentJob = currentCoroutineContext().job
        object : WithJob {
            override val job = currentJob
        }.run {
            ToastStore.add(toast)

            // Duration of 0: Keep the toast until closed manually
            if (duration > 0L) {
                delay(duration)
                ToastStore.remove(toast.id)
            }
        }
    }
}

/**
 * Factory function to create a [Toast] with an [HTMLLIElement] as default root [Tag].
 *
 * API-Sketch:
 * ```kotlin
 * toastPosition() // use for each position that should be available
 * toast() { // use for each new toast
 *     toastCloseButton() { close ->
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/toast/#toast)
 *
 * @param containerName refer to the container the toast should be stored in
 * @param duration the duration a toast should be displayed on the screen
 */
fun toast(
    containerName: String,
    duration: Long = 5000L,
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: Toast<HTMLLIElement>.() -> Unit
): Unit =
    toast(containerName, duration, classes, id, scope, RenderContext::li, initialize)
