package dev.fritz2.components

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.StyleClass.Companion.plus
import dev.fritz2.styling.params.FlexParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.ScaledValueProperty
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.staticStyle
import org.w3c.dom.HTMLDivElement

/**
 * This base component class for stacking components offer some _configuration_ properties.
 *
 * It enables to configure the following features:
 *  - switching the default order of rendering (top -> bottom to bottom -> top for stackUps and left -> right to
 *    right -> left for lineUps)
 *  - defining the spacing between the items. For details have a look at [dev.fritz2.styling.theme.Theme.space]
 *  - adding arbitrary items like HTML elements or other components
 *
 *  You can combine both kind of stacking components to realize a simple layou for example:
 *   - ``lineUp`` for structure "menu" and "content" parts
 *   - ``stackUp`` for alignment of menu items
 *  ```
 *      <- lineUp                                  ->
 *   ^  +----------+--------------------------------+
 *   |  | Menu:    |  ** Item 2 **                  |
 *   S  | - Item1  |                                |
 *   t  | -*Item2* |  This is the content of Item 2 |
 *   a  | - Item3  |                                |
 *   c  | - Item4  |                                |
 *   k  |          |                                |
 *   U  |          |                                |
 *   p  |          |                                |
 *   |  |          |                                |
 *   v  +----------+--------------------------------+
 *  ```
 *  This could be expressed via composition in such a way:
 *  ```
 * lineUp {
 *     items {
 *         // Stack *two* items horizontally:
 *         // Menu on the left side
 *         stackUp {
 *             items {
 *                 // Heading and menu items vertical stacked
 *                 h1 {+"Menu:"}
 *                 ul {
 *                     li { +"Item1" }
 *                     li { +"Item2" }
 *                     li { +"Item3" }
 *                     li { +"Item4" }
 *                 }
 *             }
 *         }
 *         // Content on the right side
 *         box {
 *             h1 { +"Item 2" }
 *             p { +"This is the content of Item 2" }
 *         }
 *     }
 * }
 *  ```
 */
@ComponentMarker
abstract class StackComponent : Component<Div>, EventProperties<HTMLDivElement> by EventMixin() {
    companion object {
        val staticCss = staticStyle(
            "stack",
            "align-items: flex-start;"
        )
    }

    val reversed = ComponentProperty(false)
    val spacing = ComponentProperty<ScaledValueProperty> { normal }
    val items = ComponentProperty<(RenderContext.() -> Unit)> {}

    abstract val stackStyles: Style<FlexParams>

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass?,
        id: String?,
        prefix: String
    ) = context.flexBox({
        stackStyles()
        styling(this as BoxParams)
    }, baseClass = baseClass + staticCss, prefix = prefix, id = id) {
        items.value(this)
        events.value(this)
    }
}


/**
 * This component class just defines the core styling in order to render child items within a flexBox layout
 * vertically.
 *
 * @see StackComponent
 */
@ComponentMarker
class StackUpComponent : StackComponent() {
    override val stackStyles: Style<FlexParams> = {
        if (this@StackUpComponent.reversed.value) {
            direction { columnReverse }
            children(" > :not(:first-child)") {
                margins { bottom(this@StackUpComponent.spacing.value) }
            }
        } else {
            direction { column }
            children(" > :not(:first-child)") {
                margins { top(this@StackUpComponent.spacing.value) }
            }
        }
    }
}


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
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "stack-up",
    build: StackUpComponent.() -> Unit = {}
): Div = StackUpComponent().apply(build).render(this, styling, baseClass, id, prefix)


/**
 * This component class just defines the core styling in order to render child items within a flexBox layout
 * horizontally.
 *
 * @see StackComponent
 */
@ComponentMarker
class LineUpComponent : StackComponent() {
    override val stackStyles: Style<FlexParams> = {
        if (this@LineUpComponent.reversed.value) {
            direction { rowReverse }
            children(" > :not(:first-child)") {
                margins { right(this@LineUpComponent.spacing.value) }
            }
        } else {
            direction { row }
            children(" > :not(:first-child)") {
                margins { left(this@LineUpComponent.spacing.value) }
            }
        }
    }
}


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
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "line-up",
    build: LineUpComponent.() -> Unit = {}
): Div = LineUpComponent().apply(build).render(this, styling, baseClass, id, prefix)
