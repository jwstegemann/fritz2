package dev.fritz2.components.buttons

import dev.fritz2.components.Icon
import dev.fritz2.components.Spinner
import dev.fritz2.components.hidden
import dev.fritz2.dom.html.Button
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.renderAll
import dev.fritz2.styling.params.*
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.flow.Flow


internal object ButtonFoundation {
    val css = staticStyle(
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

    fun color(value: ColorProperty): Style<BasicStyleParams> = {
        css("--main-color: $value;")
    }

    private const val iconSize = "1.15em"
    private const val marginToText = "0.35rem"
    private const val marginToBorder = "-0.2rem"

    val centerIconStyle: Style<BasicStyleParams> = {
        width { "1.5em" }
        height { "1.5em" }
    }

    val centerSpinnerStyle: Style<BasicStyleParams> = {
        width { iconSize }
        height { iconSize }
    }

    val leftSpinnerStyle: Style<BasicStyleParams> = {
        width { "1.0em" }
        height { "1.0em" }
        margins {
            left { marginToBorder }
            right { marginToText }
        }
    }

    val leftIconStyle: Style<BasicStyleParams> = {
        width { iconSize }
        height { iconSize }
        margins {
            left { marginToBorder }
            right { marginToText }
        }
    }

    val rightIconStyle: Style<BasicStyleParams> = {
        width { iconSize }
        height { iconSize }
        margins {
            right { marginToBorder }
            left { marginToText }
        }
    }
}


/*
fun HtmlElements.Button(
    text: String,
    styles: Style<BasicStyleParams> = {},
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

val hidden = staticStyle(
    "hidden", """
    visibility: hidden;
"""
)

fun HtmlElements.Button(
    text: String,
    loading: Flow<Boolean>,
    styles: Style<BasicStyleParams> = {},
    loadingText: String? = null,
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicStyleParams> = { solid },
    size: ButtonSizes.() -> Style<BasicStyleParams> = { normal }
): Flow<Unit> {
    lateinit var buttonClicks: Flow<Unit>
    Button(styles, color, variant, size) {
        buttonClicks = clicks.map { Unit }
        }.bind()
    }
    return buttonClicks
}



 fun HtmlElements.Button(
    text: String,
    icon: IconDefinition,
    loading: Flow<Boolean>,
    styles: Style<BasicStyleParams> = {},
    loadingText: String? = null,
    iconRight: Boolean = false,
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicStyleParams> = { solid },
    size: ButtonSizes.() -> Style<BasicStyleParams> = { normal }
): Flow<Unit> {
    lateinit var buttonClicks: Flow<Unit>
    Button(styles, color, variant, size) {
        buttonClicks = clicks.map { Unit }
        loading.renderAll { running ->
            if (!iconRight) {
                if (running) {
                    Spinner({
                        buttonLeftIconStyle()
                    })
                } else Icon(icon, buttonLeftIconStyle)
            }
            span { +(if (running && loadingText != null) loadingText else text) }
            if (iconRight) {
                if (running) {
                    Spinner({
                        buttonRightIconStyle()
                    })
                } else Icon(icon, buttonRightIconStyle)
            }
        }.bind()
    }
    return buttonClicks
}
*/

fun HtmlElements.Button(
    styles: Style<BasicStyleParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicStyleParams> = { solid },
    size: ButtonSizes.() -> Style<BasicStyleParams> = { normal },
    init: Button.() -> Any
): Button {
    return button(
        "${ButtonFoundation.css} ${
            use(
                ButtonSizes.size() +
                        ButtonFoundation.color(color) +
                        ButtonVariants.variant() +
                        styles,
                "button"
            )
        }"
    ) {
        init()
    }
}

fun HtmlElements.ClickButton(
    styles: Style<BasicStyleParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicStyleParams> = { solid },
    size: ButtonSizes.() -> Style<BasicStyleParams> = { normal },
    init: Button.() -> Any
): Flow<Unit> {
    lateinit var buttonClicks: Flow<Unit>
    Button(styles, color, variant, size) {
        buttonClicks = clicks.map {}
        init()
    }
    return buttonClicks
}


internal fun Button.label(text: String) {
    span { +text }
}

internal fun Button.label(text: Flow<String>) {
    span { text.bind() }
}

internal fun Button.label(text: String, loading: Flow<Boolean>, loadingText: String?) {
    loading.renderAll { running ->
        //render spinner
        if (running) {
            Spinner({
                if (loadingText == null) {
                    css("position: absolute;")
                    ButtonFoundation.centerSpinnerStyle()
                } else ButtonFoundation.leftSpinnerStyle()
            })
            span(if (loadingText == null) hidden else "") { +(loadingText ?: text) }
        } else {
            span { +text }
        }
    }.bind()
}

internal fun Button.iconCenter(iconDefinition: IconDefinition) {
    Icon(iconDefinition) {
        ButtonFoundation.centerIconStyle()
    }
}

internal fun Button.iconCenter(iconDefinition: IconDefinition, loading: Flow<Boolean>) {
    loading.renderAll { running ->
        if (running) Spinner({ ButtonFoundation.centerSpinnerStyle() })
        else Icon(iconDefinition) { ButtonFoundation.centerIconStyle() }
    }.bind()
}

fun HtmlElements.ClickButton(
    text: String,
    styles: Style<BasicStyleParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicStyleParams> = { solid },
    size: ButtonSizes.() -> Style<BasicStyleParams> = { normal },
): Flow<Unit> {
    return ClickButton(styles, color, variant, size) {
        label(text)
    }
}

fun HtmlElements.ClickButton(
    text: String,
    loading: Flow<Boolean>,
    loadingText: String? = null,
    styles: Style<BasicStyleParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicStyleParams> = { solid },
    size: ButtonSizes.() -> Style<BasicStyleParams> = { normal },
): Flow<Unit> {
    return ClickButton(styles, color, variant, size) {
        label(text, loading, loadingText)
    }
}

fun HtmlElements.ClickButton(
    iconDefinition: IconDefinition,
    styles: Style<BasicStyleParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicStyleParams> = { solid },
    size: ButtonSizes.() -> Style<BasicStyleParams> = { normal },
): Flow<Unit> {
    return ClickButton(styles, color, variant, size) {
        iconCenter(iconDefinition)
    }
}

fun HtmlElements.ClickButton(
    iconDefinition: IconDefinition,
    loading: Flow<Boolean>,
    styles: Style<BasicStyleParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicStyleParams> = { solid },
    size: ButtonSizes.() -> Style<BasicStyleParams> = { normal },
): Flow<Unit> {
    return ClickButton(styles, color, variant, size) {
        iconCenter(iconDefinition, loading)
    }
}
