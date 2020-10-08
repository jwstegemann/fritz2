package dev.fritz2.components

import dev.fritz2.dom.html.Button
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.*
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.theme

val buttonFoundations = staticStyle(
    "button",
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
    
    &:disabled {
        opacity: 0.4;
        cursor: not-allowed;
        boxShadow: none;
    }
    
    .icon:first-of-type {
        margin-left: -0.2rem
    }
"""
)

inline fun basicButtonStyles(mainColor: ColorProperty): Style<BasicStyleParams> = {
    css("--main-color: $mainColor;")
    lineHeight { smaller }
    radius { normal }
    fontWeight { semiBold }

    focus {
        boxShadow { outline }
    }
}

object ButtonVariants {
    val solid: Style<BasicStyleParams> = {
        background { color { "var(--main-color)" } }
        color { light }

        hover {
            css("filter: brightness(132%);")
        }

        active {
            css("filter: brightness(132%);")
        }
    }

    val outline: Style<BasicStyleParams> = {
        color { "var(--main-color)" }
        border {
            width { thin }
            style { solid }
            color { "var(--main-color)" }
        }

        hover {
            background { color { light } }
        }
    }

    val ghost: Style<BasicStyleParams> = {
        color { "var(--main-color)" }
    }

    val link: Style<BasicStyleParams> = {
        paddings { all { none } }
        height { auto }
        lineHeight { normal }
        color { "var(--main-color)" }
        hover {
            textDecoration { underline }
        }
        active {
            color { secondary }
        }
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

inline fun HtmlElements.Button(
    crossinline styles: Style<BasicStyleParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicStyleParams> = { solid },
    size: ButtonSizes.() -> Style<BasicStyleParams> = { normal },
    crossinline init: Button.() -> Any
): Button {
    return button(
        "$buttonFoundations ${
            use(
                basicButtonStyles(color) +
                        ButtonSizes.size() +
                        ButtonVariants.variant() +
                        styles,
                "button"
            )
        }"
    ) {
        init()
    }
}
