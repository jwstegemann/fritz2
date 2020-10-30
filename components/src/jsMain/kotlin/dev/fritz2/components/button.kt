package dev.fritz2.components

import dev.fritz2.dom.Listener
import dev.fritz2.dom.WithEvents
import dev.fritz2.dom.html.Button
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.renderAll
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.StyleClass.Companion.plus
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.ColorProperty
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.plus
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.Colors
import dev.fritz2.styling.theme.PushButtonSizes
import dev.fritz2.styling.theme.PushButtonVariants
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.events.MouseEvent

open class PushButtonComponent {
    companion object {
        val staticCss = staticStyle(
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
    }

    private val iconSize = "1.15em"
    private val marginToText = "0.35rem"
    private val marginToBorder = "-0.2rem"

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

    var events: (WithEvents<HTMLButtonElement>.() -> Unit)? = null

    fun events(value: WithEvents<HTMLButtonElement>.() -> Unit) {
        events = value
    }

    private fun buildColor(value: ColorProperty): Style<BasicParams> = { css("--main-color: $value;") }

    var color: Style<BasicParams> = buildColor(theme().colors.primary)

    fun color(value: Colors.() -> ColorProperty) {
        color = buildColor(theme().colors.value())
    }

    var variant: PushButtonVariants.() -> Style<BasicParams> = { theme().button.variants.solid }

    fun variant(value: PushButtonVariants.() -> Style<BasicParams>) {
        variant = value
    }

    var size: PushButtonSizes.() -> Style<BasicParams> = { theme().button.sizes.normal }

    fun size(value: PushButtonSizes.() -> Style<BasicParams>) {
        size = value
    }

    var label: (HtmlElements.(hide: Boolean) -> Unit)? = null

    fun text(value: String) {
        label = { hide -> span(if (hide) hidden.name else null) { +value } }
    }

    fun text(value: Flow<String>) {
        label = { hide -> span(if (hide) hidden.name else null) { value.bind() } }
    }

    var loadingText: (HtmlElements.() -> Unit)? = null

    fun loadingText(value: String) {
        loadingText = { span { +value } }
    }

    fun loadingText(value: Flow<String>) {
        loadingText = { span { value.bind() } }
    }

    var loading: Flow<Boolean>? = null

    fun loading(value: Flow<Boolean>) {
        loading = value
    }

    var icon: ((HtmlElements, Style<BasicParams>) -> Unit)? = null

    fun icon(
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = IconComponent.prefix,
        build: IconComponent.() -> Unit = {}
    ) {
        icon = { context, iconStyle ->
            context.icon(styling + iconStyle, baseClass, id, prefix, build)
        }
    }

    var isIconRight: Boolean = false
    fun iconRight() {
        isIconRight = true
    }

    fun renderIcon(renderContext: Button, iconStyle: Style<BasicParams>, spinnerStyle: Style<BasicParams>) {
        if (loading == null) {
            icon?.invoke(renderContext, iconStyle)
        } else {
            val x = loading?.renderAll { running ->
                if (running) {
                    spinner(spinnerStyle) {}
                } else {
                    icon?.invoke(this, iconStyle)
                }
            }
            renderContext.apply { x?.bind(true) }
        }
    }

    fun renderLabel(renderContext: Button) {
        if (loading == null || icon != null) {
            label?.invoke(renderContext, false)
        } else {
            val x = loading?.renderAll { running ->
                if (running) {
                    spinner({
                        if (loadingText == null) {
                            css("position: absolute;")
                            centerSpinnerStyle()
                        } else leftSpinnerStyle()
                    }) {}
                    if (loadingText != null) {
                        loadingText!!.invoke(this)
                    } else {
                        label?.invoke(this, true)
                    }
                } else {
                    label?.invoke(this, false)
                }
            }
            renderContext.apply { x?.bind() }
        }
    }
}

fun HtmlElements.pushButton(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "push-button",
    build: PushButtonComponent.() -> Unit = {}
) {
    val component = PushButtonComponent().apply(build)

    (::button.styled(styling, baseClass + PushButtonComponent.staticCss, id, prefix) {
        component.color()
        component.variant.invoke(theme().button.variants)()
        component.size.invoke(theme().button.sizes)()
    }) {
        if (component.label == null) {
            component.renderIcon(this, component.centerIconStyle, component.centerSpinnerStyle)
        } else {
            if (component.icon != null && !component.isIconRight) {
                component.renderIcon(this, component.leftIconStyle, component.leftSpinnerStyle)
            }
            component.renderLabel(this)
            if (component.icon != null && component.isIconRight) {
                component.renderIcon(this, component.rightIconStyle, component.rightSpinnerStyle)
            }
        }

        component.events?.invoke(this)
    }
}


fun HtmlElements.clickButton(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "push-button",
    build: PushButtonComponent.() -> Unit = {}
): Listener<MouseEvent, HTMLButtonElement> {
    var clickEvents: Listener<MouseEvent, HTMLButtonElement>? = null
    pushButton(styling, baseClass, id, prefix) {
        build()
        events {
            clickEvents = clicks
        }
    }
    return clickEvents!!
}


/*
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

*/