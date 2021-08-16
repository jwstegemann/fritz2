package dev.fritz2.components.card

import dev.fritz2.components.card
import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Scope
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * This class combines the configuration and rendering of a CardComponent.
 *
 * A card consists of arbitrary content that is optionally decorated with a header and a footer.
 * Cards can be configured in different sizes and offer the following configurable properties:
 *
 * - [size]: The size of the card, ranging from small to large. Both the font-size and margins are set accordingly.
 * - [content]: The main content of the card
 * - [header]: The header area of the card
 * - [footer]: The footer area of the card
 *
 * The content-related properties (header, footer and content) don't only take layouts but also Strings and Flows of
 * Strings for your convenience.
 * Cards are created via the [card] factory function.
 *
 * Usage example:
 * ```
 * card {
 *     size { small }
 *     header("Header")
 *     content("Lorem ipsum, dolor sit amet...")
 *     footer("Footer")
 * }
 *
 * card {
 *     size { normal }
 *     header("Simple Header")
 *     content {
 *         div({
 *             background { color { gray300 } }
 *         }) {
 *             +"Custom Content"
 *         }
 *     }
 * }
 * ```
 */
open class CardComponent(private val scope: Scope) : Component<Unit> {

    companion object {
        private const val headerStylePrefix = "card-header"
        private const val footerStylePrefix = "card-footer"
        private const val contentStylePrefix = "card-content"
    }


    /**
     * This component styles itself differently if it is rendered in the scope of a [PopoverComponent].
     * @see Scope
     */
    private val scopedStyles = when {
        else -> Theme().card
    }


    enum class Sizes {
        Small, Normal, Large
    }

    object SizesContext {
        val small = Sizes.Small
        val normal = Sizes.Normal
        val large = Sizes.Large
    }

    val size = ComponentProperty<SizesContext.() -> Sizes> { normal }


    private var header: (RenderContext.() -> Unit)? = null

    fun header(value: (RenderContext.() -> Unit)) {
        header = {
            header({
               this@CardComponent.scopedStyles.header()
            }, prefix = headerStylePrefix) { value() }
        }
    }

    fun header(value: String) {
        this.header(flowOf(value))
    }

    fun header(value: Flow<String>) {
        header = {
            header({
                this@CardComponent.scopedStyles.header()
            }, prefix = headerStylePrefix) { value.asText() }
        }
    }


    private var footer: (RenderContext.() -> Unit)? = null

    fun footer(value: (RenderContext.() -> Unit)) {
        footer = {
            footer({
                this@CardComponent.scopedStyles.footer()
            }, prefix = footerStylePrefix) { value() }
        }
    }

    fun footer(value: String) {
        this.footer(flowOf(value))
    }

    fun footer(value: Flow<String>) {
        footer = {
            footer({
                this@CardComponent.scopedStyles.footer()
            }, prefix = footerStylePrefix) { value.asText() }
        }
    }


    private var content: (RenderContext.() -> Unit)? = null

    fun content(value: (RenderContext.() -> Unit)) {
        content = {
            section({
                this@CardComponent.scopedStyles.content()
            }, prefix = contentStylePrefix) { value(this) }
        }
    }

    fun content(value: String) {
        this.content(flowOf(value))
    }

    fun content(value: Flow<String>) {
        content = {
            section({
                this@CardComponent.scopedStyles.content()
            }, prefix = contentStylePrefix) { value.asText() }
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
            div({
                when (this@CardComponent.size.value(SizesContext)) {
                    Sizes.Small -> this@CardComponent.scopedStyles.sizes.small
                    Sizes.Normal -> this@CardComponent.scopedStyles.sizes.normal
                    Sizes.Large -> this@CardComponent.scopedStyles.sizes.large
                }.invoke()

                this@CardComponent.scopedStyles.background()

                styling()
            }, baseClass, id, prefix) {
                this@CardComponent.header?.invoke(this)
                this@CardComponent.content?.invoke(this)
                this@CardComponent.footer?.invoke(this)
            }
        }
    }
}