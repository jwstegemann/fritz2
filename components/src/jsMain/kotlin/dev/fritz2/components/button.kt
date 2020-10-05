package dev.fritz2.components

import dev.fritz2.dom.html.Button
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.BasicStyleParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.use
import dev.fritz2.styling.staticStyle

val basicButtonStyleClass = staticStyle(
    "btn",
    """
    appearance: none;
    display: inline-flex;
    align-items : center;
    justify-content: center;
    transition: all 250ms;
    user-select: none;
    position: relative;
    white-space: nowrap;
    vertical-align: middle;
    outline: none;
    
"""
)

val basicButtonStyles: Style<BasicStyleParams> = {
    lineHeight { smaller }
    radius { normal }
    fontWeight { semiBold }
}

object ButtonVariants {
    val solid: Style<BasicStyleParams> = {
        background { color { primary } }
        color { light }
    }
}

object ButtonSizes {
    val normal: Style<BasicStyleParams> = {
        height { "2.5rem" } //TODO: smallSizes in Theme
        minWidth { "2.5rem" }
        fontSize { normal }
        paddings {
            horizontal { normal }
        }
    }
}

inline fun HtmlElements.btn(
    styles: Style<BasicStyleParams> = {},
    variant: ButtonVariants.() -> Style<BasicStyleParams> = { solid },
    size: ButtonSizes.() -> Style<BasicStyleParams> = { normal },
    crossinline init: Button.() -> Any
): Button {
    return button(
        "${basicButtonStyleClass} ${
            use(
                basicButtonStyles,
                ButtonVariants.variant(),
                ButtonSizes.size(),
                styles,
                "button"
            )
        }"
    ) {
        init()
    }
}
