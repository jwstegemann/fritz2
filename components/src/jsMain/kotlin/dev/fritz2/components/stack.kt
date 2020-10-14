package dev.fritz2.components

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.*


abstract class StackComponentContext(prefix: String) : FlexComponentContext(prefix) {
    var reverse: Boolean = false
    var spacing: ScaledValueProperty = { normal }

    fun spacing(value: ScaledValueProperty) {
        spacing = value
    }

    abstract val stackStyles: Style<FlexParams>
}

class StackUpComponentContext(prefix: String) : StackComponentContext(prefix) {
    override val stackStyles: Style<FlexParams> = {
        if (this@StackUpComponentContext.reverse) {
            direction { columnReverse }
            children(" > :not(:first-child)") {
                margins { bottom(this@StackUpComponentContext.spacing) }
            }
        } else {
            direction { column }
            children(" > :not(:first-child)") {
                margins { top(this@StackUpComponentContext.spacing) }
            }
        }
        alignItems { center }
    }
}

class LineUpComponentContext(prefix: String) : StackComponentContext(prefix) {
    override val stackStyles: Style<FlexParams> = {
        if (this@LineUpComponentContext.reverse) {
            direction { rowReverse }
            children(" > :not(:first-child)") {
                margins { right(this@LineUpComponentContext.spacing) }
            }
        } else {
            direction { row }
            children(" > :not(:first-child)") {
                margins { left(this@LineUpComponentContext.spacing) }
            }
        }
        alignItems { center }
    }
}

fun HtmlElements.f2StackUp(build: Context<StackComponentContext> = {}): Component<Div> {
    val context = StackUpComponentContext("f2StackUp").apply(build)
    return f2Flex {
        context.stackStyles()
    }
}

fun HtmlElements.f2LineUp(build: Context<StackComponentContext> = {}): Component<Div> {
    val context = LineUpComponentContext("f2LineUp").apply(build)

    return f2Flex {
        context.stackStyles()
    }
}
