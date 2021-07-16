package dev.fritz2.components.foundations

import dev.fritz2.dom.HtmlTagMarker
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BoxParams

/**
 * Marker interface that *every* component should implement, so that the central render method appears in a unified way
 * throughout this framework.
 *
 * The render method has to be implemented in order to do the actual rendering of one component.
 * This reduces the boilerplate code within the corresponding factory function(s):
 * ```
 * open class MyComponent: Component {
 *      override fun render(...) {
 *          // some content rendering
 *      }
 * }
 *
 * RenderContext.myComponent(
 *      // most params omitted
 *      build: MyComponent.() -> Unit = {}
 * ) {
 *      MyComponent().apply(build).render(this, /* params */)
 *      //                         ^^^^^^
 *      //                         just start the rendering by one additional call!
 * }
 * ```
 *
 * Often a component needs *additional* parameters that are passed into the factory functions (remember that those
 * should be located starting after the ``styling`` parameter in first position and before the ``baseClass`` parameter)
 * Typical use cases are [Store]s or list of items, as for [RenderContext.checkboxGroup] for example.
 * Those additional parameters should be passed via constructor injection into the component class:
 * ```
 * open class MyComponent(protected val items: List<String>, protected val store: Store<String>?): Component {
 *      override fun render(...) {
 *          // some content rendering with access to the ``items`` and the ``store``
 *      }
 * }
 *
 * RenderContext.myComponent(
 *      styling: BasicParams.() -> Unit,
 *      items: List<String>,          // two additional parameters
 *      value: Store<String>? = null, // after ``styling`` and before ``baseClass``!
 *      baseClass: StyleClass,
 *      id: String?,
 *      prefix: String
 *      build: MyComponent.() -> Unit = {}
 * ) {
 *      MyComponent(items, store) // inject additional parameters
 *          .apply(build)
 *          .render(this, styling, baseClass, id, prefix) // pass context + regular parameters
 * }
 * ```
 */
@HtmlTagMarker
interface Component<T> {

    /**
     * Central method that should do the actual rendering of a component.
     *
     * Consider to declare your implementation as ``open`` in order to allow the customization of a component.
     *
     * @param context the receiver to render the component into
     * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
     * @param baseClass optional CSS class that should be applied to the element
     * @param id the ID of the element
     * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
     */
    fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ): T
}