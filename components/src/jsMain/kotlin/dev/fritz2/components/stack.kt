package dev.fritz2.components

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.FlexParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.plus
import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.theme


inline fun HtmlElements.StackUp(
    crossinline styles: Style<FlexParams> = {},
    spacing: Property = theme().space.normal,
    reverse: Boolean = false,
    crossinline init: Div.() -> Unit
): Div {
    val stackStyles: Style<FlexParams> = {
        if (reverse) {
            direction { columnReverse }
            children(" > :not(:first-child)") {
                margins { bottom { spacing } }
            }
        } else {
            direction { column }
            children(" > :not(:first-child)") {
                margins { top { spacing } }
            }
        }
        alignItems { center }
    }

    return Flex(stackStyles + styles, init)
}

inline fun HtmlElements.LineUp(
    crossinline styles: Style<FlexParams> = {},
    spacing: Property = theme().space.normal,
    reverse: Boolean = false,
    crossinline init: Div.() -> Unit
): Div {
    val stackStyles: Style<FlexParams> = {
        if (reverse) {
            direction { rowReverse }
            children(" > :not(:first-child)") {
                margins { right { spacing } }
            }
        } else {
            direction { row }
            children(" > :not(:first-child)") {
                margins { left { spacing } }
            }
        }
        alignItems { center }
    }

    return Flex(stackStyles + styles, init)
}

