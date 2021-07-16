package dev.fritz2.components.stacks

import dev.fritz2.components.flexBox
import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.components.foundations.EventMixin
import dev.fritz2.components.foundations.EventProperties
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.FlexParams
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
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) = context.flexBox({
        this@StackComponent.stackStyles()
        styling(this as BoxParams)
    }, baseClass = baseClass + staticCss, prefix = prefix, id = id) {
        this@StackComponent.items.value(this)
        this@StackComponent.events.value(this)
    }
}

/**
 * This component class just defines the core styling in order to render child items within a flexBox layout
 * vertically.
 *
 * @see StackComponent
 */
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
 * This component class just defines the core styling in order to render child items within a flexBox layout
 * horizontally.
 *
 * @see StackComponent
 */
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