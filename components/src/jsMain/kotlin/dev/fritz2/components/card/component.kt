package dev.fritz2.components.card

import dev.fritz2.components.card
import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.components.paper
import dev.fritz2.components.paper.PaperComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BasicParams
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


    private var header: (RenderContext.() -> Unit)? = null

    fun header(value: (RenderContext.() -> Unit)) {
        header = value
    }

    fun header(value: String) = this.header(flowOf(value))

    fun header(value: Flow<String>) {
        header = {
            span { value.asText() }
        }
    }


    private var footer: (RenderContext.() -> Unit)? = null

    fun footer(value: (RenderContext.() -> Unit)) {
        footer = value
    }

    fun footer(value: String) = this.footer(flowOf(value))

    fun footer(value: Flow<String>) {
        footer = {
            span { value.asText() }
        }
    }


    private var content: (RenderContext.() -> Unit)? = null

    fun content(value: (RenderContext.() -> Unit)) {
        content = value
    }

    fun content(value: String) = this.content(flowOf(value))

    fun content(value: Flow<String>) {
        content = {
            span { value.asText() }
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
                            header(Theme().card.header, prefix = headerStylePrefix) { it() }
                        }
                        this@CardComponent.content?.let {
                            section(Theme().card.content, prefix = contentStylePrefix) { it() }
                        }
                        this@CardComponent.footer?.let {
                            footer(Theme().card.footer, prefix = footerStylePrefix) { it() }
                        }
                    }
                }
            }
        }
    }
}