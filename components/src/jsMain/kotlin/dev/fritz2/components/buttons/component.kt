package dev.fritz2.components.buttons

import dev.fritz2.components.foundations.*
import dev.fritz2.components.icon
import dev.fritz2.components.spinner
import dev.fritz2.dom.html.A
import dev.fritz2.dom.html.Button
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.button
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.*
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.HTMLButtonElement

/**
 * This class combines the _configuration_ and the core rendering of a button.
 *
 * The rendering functions are used by the component factory functions [pushButton] and [clickButton], so they are
 * not meant to be called directly unless you plan to implement your own button.
 * If not, just use those functions for stetting up a button!
 *
 * Much more important are the _configuration_ functions. You can configure the following aspects:
 *  - the type (``colorScheme``) - default is ``primary``
 *  - the label text
 *  - the icon including its position (left or right)
 *  - a state called ``loading`` for visualizing a longer background task
 *  - an additional label during the loading state
 *  - some predefined styling variants
 *  - link events of the button like ``clicks`` with external handlers
 *
 * This can be done within a functional expression that is the last parameter of the two button functions, called
 * ``build``. It offers an initialized instance of this [PushButtonComponent] class as receiver, so every mutating
 * method can be called for configuring the desired state for rendering the button.
 *
 * The following example shows the usage:
 * ```
 * pushButton {
 *     icon { fromTheme { check } } // set up an icon
 *     iconPlacement { right } // place the icon on the right side (``left`` is the default)
 *     loading(someStore.loading) // pass in some [Flow<Boolean>] that shows a spinner if ``true`` is passed
 *     loadingText("saving") // show an _alternate_ label, if store sends ``true``
 *     text("save") // define the default label
 *     disabled(true) // disable the button; could also be a ``Flow<Boolean>`` for dynamic disabling
 *     events { // open inner context with all DOM-element events
 *         clicks handledBy someStore.update // react to click event
 *     }
 *     element {
 *         // exposes the underlying HTML button element for direct access. Use with caution!
 *     }
 * }
 * ```
 */
open class PushButtonComponent :
    Component<Unit>,
    EventProperties<HTMLButtonElement> by EventMixin(),
    ElementProperties<Button> by ElementMixin(),
    FormProperties by FormMixin() {
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
                white-space: nowrap;
                vertical-align: middle;
                outline: none;
                text-overflow: ellipsis;
                
                &:disabled {
                    opacity: 0.4;
                    cursor: not-allowed;
                    boxShadow: none;
                }
            """
        )

        internal val hidden = staticStyle(
            "hidden",
            "visibility: hidden;"
        )

        val iconPlacementContext = IconPlacementContext()
    }

    private val iconSize = "1.15em"
    private val marginToText = "0.35rem"
    private val marginToBorder = "-0.2rem"

    val centerIconStyle: Style<BasicParams> = {
        width { "1.5em" }
        height { "1.5em" }
    }

    val centerSpinnerStyle: Style<BasicParams> = {
        width { this@PushButtonComponent.iconSize }
        height { this@PushButtonComponent.iconSize }
    }

    val leftSpinnerStyle: Style<BasicParams> = {
        width { "1.0em" }
        height { "1.0em" }
        margins {
            left { this@PushButtonComponent.marginToBorder }
            right { this@PushButtonComponent.marginToText }
        }
    }

    val rightSpinnerStyle: Style<BasicParams> = {
        width { "1.0em" }
        height { "1.0em" }
        margins {
            left { this@PushButtonComponent.marginToText }
            right { this@PushButtonComponent.marginToBorder }
        }
    }

    val leftIconStyle: Style<BasicParams> = {
        width { this@PushButtonComponent.iconSize }
        height { this@PushButtonComponent.iconSize }
        margins {
            left { this@PushButtonComponent.marginToBorder }
            right { this@PushButtonComponent.marginToText }
        }
    }

    val rightIconStyle: Style<BasicParams> = {
        width { this@PushButtonComponent.iconSize }
        height { this@PushButtonComponent.iconSize }
        margins {
            right { this@PushButtonComponent.marginToBorder }
            left { this@PushButtonComponent.marginToText }
        }
    }

    enum class ButtonVariant {
        OUTLINE, SOLID, GHOST, LINK
    }

    object VariantContext {
        val outline = ButtonVariant.OUTLINE
        val solid = ButtonVariant.SOLID
        val ghost = ButtonVariant.GHOST
        val link = ButtonVariant.LINK
    }

    val type = ComponentProperty<PushButtonTypes.() -> ColorScheme> { Theme().button.types.primary }
    val variant = ComponentProperty<VariantContext.() -> ButtonVariant> { solid }
    val size = ComponentProperty<FormSizesStyles.() -> Style<BasicParams>> { Theme().button.sizes.normal }

    private var text: (RenderContext.(hide: Boolean) -> Unit)? = null

    fun text(value: String) {
        text = { hide ->
            span {
                +value
                if (hide) className(hidden.name)
            }
        }
    }

    fun text(value: Flow<String>) {
        text = { hide ->
            span {
                value.asText()
                if (hide) className(hidden.name)
            }
        }
    }

    private var loadingText: (RenderContext.() -> Unit)? = null

    fun loadingText(value: String) {
        loadingText = { span { +value } }
    }

    fun loadingText(value: Flow<String>) {
        loadingText = { span { value.asText() } }
    }

    private var loading: Flow<Boolean>? = null

    fun loading(value: Flow<Boolean>) {
        loading = value
    }

    val icon = ComponentProperty<(Icons.() -> IconDefinition)?>(null)

    enum class IconPlacement {
        Right,
        Left
    }

    class IconPlacementContext {
        val right: IconPlacement = IconPlacement.Right
        val left: IconPlacement = IconPlacement.Left
    }

    val iconPlacement = ComponentProperty<IconPlacementContext.() -> IconPlacement> { IconPlacement.Left }

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            button({
                when (this@PushButtonComponent.variant.value(VariantContext)) {
                    ButtonVariant.SOLID ->
                        Theme().button.variants.solid(this, this@PushButtonComponent.type.value(Theme().button.types))
                    ButtonVariant.LINK ->
                        Theme().button.variants.link(this, this@PushButtonComponent.type.value(Theme().button.types))
                    ButtonVariant.OUTLINE ->
                        Theme().button.variants.outline(this, this@PushButtonComponent.type.value(Theme().button.types))
                    ButtonVariant.GHOST ->
                        Theme().button.variants.ghost(this, this@PushButtonComponent.type.value(Theme().button.types))
                }
                this@PushButtonComponent.size.value.invoke(Theme().button.sizes)()
            }, styling, baseClass + staticCss, id, prefix) {
                disabled(this@PushButtonComponent.disabled.values)
                if (this@PushButtonComponent.text == null) {
                    this@PushButtonComponent.renderIcon(
                        this,
                        this@PushButtonComponent.centerIconStyle,
                        this@PushButtonComponent.centerSpinnerStyle
                    )
                } else {
                    if (this@PushButtonComponent.icon.value != null && this@PushButtonComponent.iconPlacement.value(
                            iconPlacementContext
                        ) == IconPlacement.Left
                    ) {
                        this@PushButtonComponent.renderIcon(
                            this,
                            this@PushButtonComponent.leftIconStyle,
                            this@PushButtonComponent.leftSpinnerStyle
                        )
                    }
                    this@PushButtonComponent.renderText(this)
                    if (this@PushButtonComponent.icon.value != null && this@PushButtonComponent.iconPlacement.value(
                            iconPlacementContext
                        ) == IconPlacement.Right
                    ) {
                        this@PushButtonComponent.renderIcon(
                            this,
                            this@PushButtonComponent.rightIconStyle,
                            this@PushButtonComponent.rightSpinnerStyle
                        )
                    }
                }
                this@PushButtonComponent.events.value.invoke(this)
                this@PushButtonComponent.element.value.invoke(this)
            }
        }
    }

    private fun renderIcon(renderContext: Button, iconStyle: Style<BasicParams>, spinnerStyle: Style<BasicParams>) {
        if (loading == null) {
            renderContext.apply {
                icon(iconStyle) {
                    def(this@PushButtonComponent.icon.value?.invoke(Theme().icons))
                }
            }
        } else {
            renderContext.apply {
                this@PushButtonComponent.loading?.render { running ->
                    if (running) {
                        spinner(spinnerStyle) {}
                    } else {
                        icon(iconStyle) {
                            def(this@PushButtonComponent.icon.value?.invoke(Theme().icons))
                        }
                    }
                }
            }
        }
    }

    private fun renderText(renderContext: Button) {
        if (loading == null || icon.value != null) {
            text?.invoke(renderContext, false)
        } else {
            renderContext.apply {
                this@PushButtonComponent.loading?.render { running ->
                    if (running) {
                        spinner({
                            if (this@PushButtonComponent.loadingText == null) {
                                css("position: absolute;")
                                this@PushButtonComponent.centerSpinnerStyle()
                            } else this@PushButtonComponent.leftSpinnerStyle()
                        }) {}
                        if (this@PushButtonComponent.loadingText != null) {
                            this@PushButtonComponent.loadingText!!.invoke(this)
                        } else {
                            this@PushButtonComponent.text?.invoke(this, true)
                        }
                    } else {
                        this@PushButtonComponent.text?.invoke(this, false)
                    }
                }
            }
        }
    }
}

/**
 * This class extends the [PushButtonComponent] and adds link specific properties to it.
 *
 *
 * Example:
 * ```
 * linkButton {
 *     text("fritz2") // define the default label
 *     href("//www.fritz2.dev") // specifies the URL of the page the link goes to
 *     target("_blank") // specifies where to open the linked document
 *
 *     icon { fromTheme { check } } // set up an icon
 *     iconPlacement { right } // place the icon on the right side (``left`` is the default)
 *     loading(someStore.loading) // pass in some [Flow<Boolean>] that shows a spinner if ``true`` is passed
 *     loadingText("saving") // show an _alternate_ label, if store sends ``true``
 *     disabled(true) // disable the button; could also be a ``Flow<Boolean>`` for dynamic disabling
 *     element {
 *         // exposes the underlying HTML button element for direct access. Use with caution!
 *     }
 * }
 * ```
 *
 * @see PushButtonComponent
 */
open class LinkButtonComponent : PushButtonComponent() {

    private var href: A.() -> Unit = {}
    fun href(value: String) {
        href = {
            href(value)
        }
    }

    fun href(value: Flow<String>) {
        href = {
            href(value)
        }
    }

    private var target: A.() -> Unit = {}
    fun target(value: String) {
        target = {
            target(value)
        }
    }

    fun target(value: Flow<String>) {
        target = {
            target(value)
        }
    }

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            a {
                this@LinkButtonComponent.href(this)
                this@LinkButtonComponent.target(this)
                super.render(this, styling, baseClass, id, prefix)
            }
        }
    }
}