package dev.fritz2.components

import dev.fritz2.dom.html.Button
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.renderNotNull
import dev.fritz2.styling.params.*
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.theme
import dev.fritz2.styling.whenever
import kotlinx.coroutines.flow.Flow

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
    text: String,
    crossinline styles: Style<BasicStyleParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicStyleParams> = { solid },
    size: ButtonSizes.() -> Style<BasicStyleParams> = { normal }
): Flow<Unit> {
    lateinit var buttonClicks: Flow<Unit>
    Button(styles, color, variant, size) {
        buttonClicks = clicks.map { Unit }
        +text
    }
    return buttonClicks
}

val invisible = staticStyle(
    "invisible", """
    opacity: 0;
"""
)

fun HtmlElements.Button(
    text: String,
    loading: Flow<String?>,
    styles: Style<BasicStyleParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicStyleParams> = { solid },
    size: ButtonSizes.() -> Style<BasicStyleParams> = { normal }
): Flow<Unit> {
    lateinit var buttonClicks: Flow<Unit>
    Button(styles, color, variant, size) {
        buttonClicks = clicks.map { Unit }
        loading.renderNotNull { state ->
            if (state != null)
                Spinner({
                    css("position: absolute;")
                })
            else null
        }.bind()
        span {
            className = invisible.whenever(loading) { it != null }
            +text
        }
    }
    return buttonClicks
}


val buttonLeftIconStyle: Style<BasicStyleParams> = {
    width { "1.15em" }
    height { "1.15em" }
    margins {
        left { "-0.2rem" }
        right { "0.35rem" }
    }
}

val buttonRightIconStyle: Style<BasicStyleParams> = {
    width { "1.15em" }
    height { "1.15em" }
    margins {
        right { "-0.2rem" }
        left { "0.35rem" }
    }
}

inline fun HtmlElements.Button(
    text: String,
    icon: IconDefinition,
    crossinline styles: Style<BasicStyleParams> = {},
    iconRight: Boolean = false,
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicStyleParams> = { solid },
    size: ButtonSizes.() -> Style<BasicStyleParams> = { normal }
): Flow<Unit> {
    lateinit var buttonClicks: Flow<Unit>
    Button(styles, color, variant, size) {
        buttonClicks = clicks.map { Unit }
        if (!iconRight) {
            Icon(icon, buttonLeftIconStyle)
        }
        +text
        if (iconRight) {
            Icon(icon, buttonRightIconStyle)
        }
    }
    return buttonClicks
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
                ButtonSizes.size() +
                        basicButtonStyles(color) +
                        ButtonVariants.variant() +
                        styles,
                "button"
            )
        }"
    ) {
        init()
    }
}
