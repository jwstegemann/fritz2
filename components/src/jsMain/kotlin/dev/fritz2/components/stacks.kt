package dev.fritz2.components

import dev.fritz2.components.stacks.LineUpComponent
import dev.fritz2.components.stacks.StackUpComponent
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.FlexParams


/**
 * This _layout_ component enables the *vertical* stacking of child components.
 *
 * The component itself does not do anything special besides offering a context for child components, that will be
 * rendered vertically from top to bottom. You can configure the _spacing_ between the items and invert the order the
 * child items are rendered.
 *
 * ```
 * stackUp {
 *      spacing { large } // define a margin between two items
 *      items { // open a sub context for adding arbitrary HTML elements or other components!
 *          p { +"Some text paragraph" }
 *          p { +"Another text section }
 *          // go on for arbitrary other HTML elements!
 *      }
 * }
 * ```
 *
 * Pay *attention* tp *always* set the child items within the ``items`` expression and not directly within the
 * StackComponent context. You won't get an error, but the child items are rendered falsy if applied wrong!
 *
 * @see StackComponent
 * @see StackUpComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [StackComponent]
 * @return a [Div] element in order to use this component as top level element of an UI part. This way it can be
 *         directly integrated into one of the _render_ functions!
 */
fun RenderContext.stackUp(
    styling: FlexParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "stack-up",
    build: StackUpComponent.() -> Unit = {}
): Div = StackUpComponent().apply(build).render(this, styling, baseClass, id, prefix)


/**
 * This _layout_ component enables the *horizontal* stacking of child components.
 *
 * The component itself does not do anything special besides offering a context for child components, that will be
 * rendered horizontally from left to right. You can configure the _spacing_ between the items and invert the order the
 * child items are rendered.
 *
 * ```
 * lineUp {
 *      spacing { small } // define a margin between two items
 *      items { // open a sub context for adding arbitrary HTML elements or other components!
 *          clickButton { text("Add") } handledBy creationHandler
 *          clickButton { text("Edit") } handledBy creationHandler
 *          clickButton { text("Delete") } handledBy deleteHandler
 *          // go on for arbitrary other HTML elements!
 *      }
 * }
 * ```
 *
 * Pay *attention* tp *always* set the child items within the ``items`` expression and not directly within the
 * StackComponent context. You won't get an error, but the child items are rendered falsy if applied wrong!
 *
 * @see StackComponent
 * @see LineUpComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [StackComponent]
 * @return a [Div] element in order to use this component as top level element of an UI part. This way it can be
 *         directly integrated into one of the _render_ functions!
 */
fun RenderContext.lineUp(
    styling: FlexParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "line-up",
    build: LineUpComponent.() -> Unit = {}
): Div = LineUpComponent().apply(build).render(this, styling, baseClass, id, prefix)
