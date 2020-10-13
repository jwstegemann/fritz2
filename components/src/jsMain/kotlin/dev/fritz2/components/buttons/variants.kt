package dev.fritz2.components.buttons

import dev.fritz2.styling.params.*

object ButtonVariants {
    private val basic: Style<BasicParams> = {
        lineHeight { smaller }
        radius { normal }
        fontWeight { FontWeights.semiBold }

        focus {
            boxShadow { outline }
        }
    }


    val solid: Style<BasicParams> = {
        basic()

        background { color { "var(--main-color)" } }
        color { light }

        hover {
            css("filter: brightness(132%);")
        }

        active {
            css("filter: brightness(132%);")
        }
    }

    val outline: Style<BasicParams> = {
        basic()

        color { "var(--main-color)" }
        border {
            width { thin }
            style { BorderStyleValues.solid }
            color { "var(--main-color)" }
        }

        hover {
            background { color { light } }
        }
    }

    val ghost: Style<BasicParams> = {
        basic()

        color { "var(--main-color)" }
    }

    val link: Style<BasicParams> = {
        basic()

        paddings { all { none } }
        height { auto }
        lineHeight { normal }
        color { "var(--main-color)" }
        hover {
            textDecoration { TextDecorations.underline }
        }
        active {
            color { secondary }
        }
    }
}
