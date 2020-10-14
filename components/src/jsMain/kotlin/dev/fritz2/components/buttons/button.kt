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

    fun color(value: ColorProperty): Style<BasicParams> = {
        css("--main-color: $value;")
    }

    private const val iconSize = "1.15em"
    private const val marginToText = "0.35rem"
    private const val marginToBorder = "-0.2rem"

    val centerIconStyle: Style<BasicParams> = {
        width { "1.5em" }
        height { "1.5em" }
    }

    val centerSpinnerStyle: Style<BasicParams> = {
        width { iconSize }
        height { iconSize }
    }

    val leftSpinnerStyle: Style<BasicParams> = {
        width { "1.0em" }
        height { "1.0em" }
        margins {
            left { marginToBorder }
            right { marginToText }
        }
    }

    val rightSpinnerStyle: Style<BasicParams> = {
        width { "1.0em" }
        height { "1.0em" }
        margins {
            left { marginToText }
            right { marginToBorder }
        }
    }

    val leftIconStyle: Style<BasicParams> = {
        width { iconSize }
        height { iconSize }
        margins {
            left { marginToBorder }
            right { marginToText }
        }
    }

    val rightIconStyle: Style<BasicParams> = {
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
    styles: Style<BasicParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicParams> = { solid },
    size: ButtonSizes.() -> Style<BasicParams> = { normal },
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
    styles: Style<BasicParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicParams> = { solid },
    size: ButtonSizes.() -> Style<BasicParams> = { normal },
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

internal fun Button.icon(iconDefinition: IconDefinition, iconStyle: Style<BasicParams>) {
    Icon(iconDefinition, iconStyle)
}

internal fun Button.icon(
    iconDefinition: IconDefinition,
    iconStyle: Style<BasicParams>,
    loading: Flow<Boolean>,
    spinnerStyle: Style<BasicParams>
) {
    loading.renderAll { running ->
        if (running) Spinner(spinnerStyle)
        else Icon(iconDefinition, iconStyle)
    }.bind(preserveOrder = true)
}


fun HtmlElements.ClickButton(
    text: String,
    styles: Style<BasicParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicParams> = { solid },
    size: ButtonSizes.() -> Style<BasicParams> = { normal },
): Flow<Unit> {
    return ClickButton(styles, color, variant, size) {
        label(text)
    }
}

fun HtmlElements.ClickButton(
    text: String,
    loading: Flow<Boolean>,
    loadingText: String? = null,
    styles: Style<BasicParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicParams> = { solid },
    size: ButtonSizes.() -> Style<BasicParams> = { normal },
): Flow<Unit> {
    return ClickButton(styles, color, variant, size) {
        label(text, loading, loadingText)
    }
}

fun HtmlElements.ClickButton(
    icon: IconDefinition,
    styles: Style<BasicParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicParams> = { solid },
    size: ButtonSizes.() -> Style<BasicParams> = { normal },
): Flow<Unit> {
    return ClickButton(styles, color, variant, size) {
        icon(icon, ButtonFoundation.centerIconStyle)
    }
}

fun HtmlElements.ClickButton(
    icon: IconDefinition,
    loading: Flow<Boolean>,
    styles: Style<BasicParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicParams> = { solid },
    size: ButtonSizes.() -> Style<BasicParams> = { normal },
): Flow<Unit> {
    return ClickButton(styles, color, variant, size) {
        icon(icon, ButtonFoundation.centerIconStyle, loading, ButtonFoundation.centerSpinnerStyle)
    }
}

fun HtmlElements.ClickButton(
    iconLeft: IconDefinition,
    text: String,
    styles: Style<BasicParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicParams> = { solid },
    size: ButtonSizes.() -> Style<BasicParams> = { normal },
): Flow<Unit> {
    return ClickButton(styles, color, variant, size) {
        icon(iconLeft, ButtonFoundation.leftIconStyle)
        label(text)
    }
}

fun HtmlElements.ClickButton(
    iconLeft: IconDefinition,
    text: String,
    loading: Flow<Boolean>,
    styles: Style<BasicParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicParams> = { solid },
    size: ButtonSizes.() -> Style<BasicParams> = { normal },
): Flow<Unit> {
    return ClickButton(styles, color, variant, size) {
        icon(iconLeft, ButtonFoundation.leftIconStyle, loading, ButtonFoundation.leftSpinnerStyle)
        label(text)
    }
}

fun HtmlElements.ClickButton(
    text: String,
    iconRight: IconDefinition,
    styles: Style<BasicParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicParams> = { solid },
    size: ButtonSizes.() -> Style<BasicParams> = { normal },
): Flow<Unit> {
    return ClickButton(styles, color, variant, size) {
        label(text)
        icon(iconRight, ButtonFoundation.rightIconStyle)
    }
}

fun HtmlElements.ClickButton(
    text: String,
    iconRight: IconDefinition,
    loading: Flow<Boolean>,
    styles: Style<BasicParams> = {},
    color: ColorProperty = theme().colors.primary,
    variant: ButtonVariants.() -> Style<BasicParams> = { solid },
    size: ButtonSizes.() -> Style<BasicParams> = { normal },
): Flow<Unit> {
    return ClickButton(styles, color, variant, size) {
        label(text)
        icon(iconRight, ButtonFoundation.rightIconStyle, loading, ButtonFoundation.rightSpinnerStyle)
    }
}


class MyButtonContext(val renderContext: HtmlElements) {
    var styles: Style<BasicParams>? = null
    var variant: Style<BasicParams> = ButtonVariants.solid
    lateinit var labelBuilder: HtmlElements.() -> Unit

    fun label(text: String): Unit {
        labelBuilder = { span { +text } }
    }

    fun radios(build: MyButtonContext.() -> Unit = {}) {
        val extendedBuild: MyButtonContext.() -> Unit = {
            build()
            //disabled()
        }

    }

    fun style(s: Style<BasicParams>) {
        styles = s
    }

    fun init(initializator: Button.() -> Unit) {}
}

fun HtmlElements.MyButton(build: MyButtonContext.() -> Unit) {
    val context = MyButtonContext(this).apply(build)

    val buttonClasses = context.styles?.let { use(it) }.orEmpty()
    button(buttonClasses) {
        context
    }
}

fun HtmlElements.MyButton(text: String, build: MyButtonContext.() -> Unit = {}) {
}

fun HtmlElements.MyButton(icon: Int, build: MyButtonContext.() -> Unit = {}) {
}

fun HtmlElements.MyButton(icon: String, text: String, build: MyButtonContext.() -> Unit = {}) {
}

/*
fun HtmlElements.foo() {
    MyButton {
        style {
            margins {}
        }
        label("hugo")
        init {
            clicks handledBy
        }
    }

    MyButton("ok") {
        variant = ButtonVariants.ghost
    }

    Box() { /* content */ }

    Box MyContext .{
        style {

        }

        init {

        }
    }


}

 */
