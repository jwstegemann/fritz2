package dev.fritz2.components

import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.StyleClass.Companion.plus
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.Property
import dev.fritz2.styling.theme.Thickness
import dev.fritz2.styling.theme.theme


class SpinnerComponent {
    companion object {
        val staticCss = staticStyle(
            "spinner",
            """ 
            display: inline-block;
            border-color: currentColor;
            border-style: solid;
            border-radius: 99999px;
            border-bottom-color: transparent;
            border-left-color: transparent;
            color: currentColor;
        """
        )
    }

    var size: Property = theme().borderWidths.normal

    fun size(value: Thickness.() -> Property) {
        size = theme().borderWidths.value()
    }
}


fun HtmlElements.spinner(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "inputField",
    build: SpinnerComponent.() -> Unit
) {
    val component = SpinnerComponent().apply(build)

    (::div.styled(styling, baseClass + SpinnerComponent.staticCss, id, prefix) {
        css("animation: loading 0.6s linear infinite;")
        border { width { component.size } }
        width { "1rem" }
        height { "1rem" }
    }) {}
}