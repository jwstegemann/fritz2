package dev.fritz2.components.card

import dev.fritz2.components.card
import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.components.paper
import dev.fritz2.components.paper.PaperComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.plus
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
 * An optional styling parameter can be passed to the above properties in case you specify _custom content_ via
 * `RenderContext`.
 *
 * The content-related properties (header, footer and content) don't only take layouts but also Strings and Flows of
 * Strings for your convenience.
 * Cards are created via the [card] factory function.
 *
 * Cards can have different types which are the same as in [PaperComponent]. This is due to the fact, that the
 * CardComponent uses a [PaperComponent] internally.
 * The type can be specified via the [type] property.
 *
 * Usage example:
 * ```
 * card {
 *     size { small }
 *     type { normal }
 *     paperType { normal }
 *     header("Header")
 *     content("Lorem ipsum, dolor sit amet...")
 *     footer("Footer")
 * }
 *
 * card {
 *     size { normal }
 *     header("Simple Header")
 *     content({
 *         // Optional additional styling
 *         background { color { gray300 } }
 *     }) {
 *         span {
 *             +"Custom Content"
 *         }
 *     }
 * }
 * ```
 */
open class CardComponent : Component<Unit> {

    companion object {
        private const val headerStylePrefix = "card-header"
        private const val footerStylePrefix = "card-footer"
        private const val contentStylePrefix = "card-content"
    }


    enum class Sizes {
        SMALL, NORMAL, LARGE
    }

    object SizesContext {
        val small = Sizes.SMALL
        val normal = Sizes.NORMAL
        val large = Sizes.LARGE
    }

    val size = ComponentProperty<SizesContext.() -> Sizes> { normal }


    data class CardSection(
        val styling: Style<BoxParams> = {},
        val value: RenderContext.() -> Unit
    )


    protected var header: CardSection? = null

    fun header(
        styling: Style<BoxParams> = {},
        value: (RenderContext.() -> Unit)
    ) {
        header = CardSection(styling, value)
    }

    fun header(value: String) = this.header(flowOf(value))

    fun header(value: Flow<String>) {
        header = CardSection {
            span { value.renderText() }
        }
    }


    protected var footer: CardSection? = null

    fun footer(
        styling: Style<BoxParams> = {},
        value: (RenderContext.() -> Unit)
    ) {
        footer = CardSection(styling, value)
    }

    fun footer(value: String) = this.footer(flowOf(value))

    fun footer(value: Flow<String>) {
        footer = CardSection {
            span { value.renderText() }
        }
    }


    protected var content: CardSection? = null

    fun content(
        styling: Style<BoxParams> = {},
        value: (RenderContext.() -> Unit)
    ) {
        content = CardSection(styling, value)
    }

    fun content(value: String) = this.content(flowOf(value))

    fun content(value: Flow<String>) {
        content = CardSection {
            span { value.renderText() }
        }
    }


    val type = ComponentProperty<PaperComponent.TypesContext.() -> PaperComponent.Types> { normal }


    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            // TODO: Remove unsafe cast when issue #498 is resolved!
            paper({ styling(this as BoxParams) }, baseClass, id, prefix) {
                size {
                    when(this@CardComponent.size.value(SizesContext)) {
                        Sizes.SMALL -> PaperComponent.Sizes.SMALL
                        Sizes.NORMAL -> PaperComponent.Sizes.NORMAL
                        Sizes.LARGE -> PaperComponent.Sizes.LARGE
                    }
                }
                type { this@CardComponent.type.value(PaperComponent.TypesContext) }
                content {
                    div({
                        when (this@CardComponent.size.value(SizesContext)) {
                            Sizes.SMALL -> Theme().card.sizes.small
                            Sizes.NORMAL -> Theme().card.sizes.normal
                            Sizes.LARGE -> Theme().card.sizes.large
                        }.invoke()
                    }) {
                        this@CardComponent.header?.let {
                            header(Theme().card.header + it.styling, prefix = headerStylePrefix) { it.value(this) }
                        }
                        this@CardComponent.content?.let {
                            section(Theme().card.content + it.styling, prefix = contentStylePrefix) { it.value(this) }
                        }
                        this@CardComponent.footer?.let {
                            footer(Theme().card.footer + it.styling, prefix = footerStylePrefix) { it.value(this) }
                        }
                    }
                }
            }
        }
    }
}