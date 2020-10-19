package dev.fritz2.components

import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.FlexParams
import dev.fritz2.styling.params.ScaledValueProperty
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.staticStyle


abstract class StackComponent(prefix: String) {
    companion object {
        val staticCss = staticStyle(
            "stack",
            "align-items: center;"
        )
    }

    var reverse: Boolean = false

    fun reverse(value: () -> Boolean) {
        reverse = value()
    }

    var spacing: ScaledValueProperty = { normal }

    fun spacing(value: ScaledValueProperty) {
        spacing = value
    }

    var children: (HtmlElements.() -> Unit)? = null

    abstract val stackStyles: Style<FlexParams>
}


class StackUpComponent : StackComponent("stack-up") {
    override val stackStyles: Style<FlexParams> = {
        if (this@StackUpComponent.reverse) {
            direction { columnReverse }
            children(" > :not(:first-child)") {
                margins { bottom(this@StackUpComponent.spacing) }
            }
        } else {
            direction { column }
            children(" > :not(:first-child)") {
                margins { top(this@StackUpComponent.spacing) }
            }
        }
    }
}

fun HtmlElements.stackUp(
    styling: FlexParams.() -> Unit = {},
    baseClass: String? = null,
    id: String? = null,
    prefix: String = "stack-up",
    build: StackUpComponent.() -> Unit = {}
) {
    val component = StackUpComponent().apply(build)

    flexBox({
        component.stackStyles()
        styling()
    }, baseClass = StackComponent.staticCss, prefix = prefix) {
        component.children?.let { it() }
    }
}



class LineUpComponent : StackComponent("line-up") {
    override val stackStyles: Style<FlexParams> = {
        if (this@LineUpComponent.reverse) {
            direction { rowReverse }
            children(" > :not(:first-child)") {
                margins { right(this@LineUpComponent.spacing) }
            }
        } else {
            direction { row }
            children(" > :not(:first-child)") {
                margins { left(this@LineUpComponent.spacing) }
            }
        }
    }
}

fun HtmlElements.lineUp(
    styling: FlexParams.() -> Unit = {},
    baseClass: String? = null,
    id: String? = null,
    prefix: String = "line-up",
    build: LineUpComponent.() -> Unit = {}
) {
    val component = LineUpComponent().apply(build)

    flexBox({
        component.stackStyles()
        styling()
    }, baseClass = StackComponent.staticCss, prefix = prefix) {
        component.children?.let { it() }
    }
}
