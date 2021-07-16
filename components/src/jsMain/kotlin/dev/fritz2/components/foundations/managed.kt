package dev.fritz2.components.foundations

import dev.fritz2.dom.HtmlTagMarker
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Scope
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BoxParams
import kotlinx.browser.document
import kotlinx.coroutines.Job
import org.w3c.dom.HTMLElement
import kotlin.js.Date
import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * This interface should be implemented by all components, that should appear at a specific, centralized node within
 * the DOM tree. Think of [modal]s or [toast]s for example: They appear as overlays above all other screen elements,
 * which is realized by a central parent node in the DOM.
 *
 * The render method has to be implemented in order to do the actual rendering of one component.
 * This reduces the boilerplate code within the corresponding factory function(s):
 * ```
 * open class MyManagedComponent: ManagedComponent {
 *     override fun render(...) {
 *         // some content rendering
 *     }
 * }
 *
 * // factory don't need to be an extension of `RenderContext`!
 * myComponent(
 *      // most params omitted
 *      build: MyManagedComponent.() -> Unit = {}
 * ) {
 *      MyComponent().apply(build).render(/* params */)
 *      //                         ^^^^^^ ^^^^^^^^^^^^
 *      //                         |      no reference to parent's `RenderContext` is needed!
 *      //                         |      (in contrast to `Component<T>` interface!)
 *      //                         |
 *      //                         just start the rendering by one additional call!
 * }
 * ```
 *
 * To start the centralized rendering process, consider using the `init` block of a companion object:
 * ```
 * open class MyManagedComponent: ManagedComponent {
 *     companion object {
 *         init {
 *             ManagedComponent.managedRenderContext("someId", Job()).apply {
 *                 // do the central rendering (often surrounded by some central store, that manages the child
 *                 // components added by the factory functions)
 *             }
 *         }
 *     }
 *
 *     override fun render(...) {
 *         // some content rendering
 *     }
 * }
 * ```
 *
 * @see Component<T>
 */
@HtmlTagMarker
interface ManagedComponent<T> {

    /**
     * Central method that should do the actual rendering of a *managed* component.
     *
     * Attention: No [RenderContext] is needed here, as the implementing class *must* provide it by itself!
     * This can be created by [managedRenderContext] provided by this interface's companion object.
     *
     * Consider to declare your implementation as ``open`` in order to allow the customization of a component.
     *
     * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
     * @param baseClass optional CSS class that should be applied to the element
     * @param id the ID of the element
     * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
     */
    fun render(
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ): T

    companion object {
        /**
         * Gives a global [RenderContext] which is appended to the html body element.
         *
         * @param id for [RenderContext] DOM element
         * @param job [Job] used in this [RenderContext]
         */
        internal fun managedRenderContext(id: String, job: Job, scope: Scope): RenderContext {
            val element = document.getElementById(id)
            return if (element != null) {
                Tag("div", element.id, job = job, scope = scope, domNode = (element as HTMLElement))
            } else {
                Div(id, job = job, scope = scope).apply { document.body?.appendChild(this.domNode) }
            }.apply {
                domNode.innerHTML = ""
            }
        }
    }
}

/**
 * Creates a random id based on random number and timestamp.
 */
fun randomId(): String {
    val random = Random.nextUInt(10000U, 99999U).toString()
    val time = Date().getMilliseconds().toString()
    return "$random${time.substring(time.length - 3)}"
}