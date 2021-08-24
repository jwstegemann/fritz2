package dev.fritz2.components.paper

import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.div
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.Theme

/**
 * This class combines the configuration and rendering of a PaperComponent.
 *
 * A PaperComponent is simply an elevated card-like box with a solid background and corner radii / paddings that vary
 * depending on the specified [size] of the component.
 *
 * There are different [type]s of paper:
 * - `normal` ([Types.NORMAL]): Paper sheet appears card-like with a box-shadow.
 * - `outline` ([Types.OUTLINE]): Paper sheet does not appear elevated but with an outline instead.
 * - `ghost` ([Types.GHOST]): Paper sheet neither appears elevated nor outlined.
 *
 * The content is specified via the [content] property. In addition to the atual content an optional styling parameter
 * can be specified as well.
 *
 *
 * Usage example:
 * ```
 * paper {
 *     size { normal }
 *     type { normal }
 *     content {
 *         span { +"This is paper." }
 *     }
 * }
 *
 * paper {
 *     content({
 *         // Custom styled content
 *     }) {
 *         span { +"This is paper." }
 *     }
 * }
 * ```
 */
open class PaperComponent : Component<Unit> {

    enum class Sizes {
        SMALL, NORMAL, LARGE
    }

    object SizesContext {
        val small = Sizes.SMALL
        val normal = Sizes.NORMAL
        val large = Sizes.LARGE
    }

    val size = ComponentProperty<SizesContext.() -> Sizes> { normal }


    enum class Types {
        NORMAL, OUTLINE, GHOST
    }

    object TypesContext {
        val normal = Types.NORMAL
        val outline = Types.OUTLINE
        val ghost = Types.GHOST
    }

    val type = ComponentProperty<TypesContext.() -> Types> { normal }


    data class PaperContent(
        val styling: Style<BoxParams>,
        val value: RenderContext.() -> Unit
    )

    protected var content: PaperContent? = null

    fun content(
        styling: Style<BoxParams> = {},
        value: RenderContext.() -> Unit
    ) {
        content = PaperContent(styling, value)
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
                when (this@PaperComponent.size.value(SizesContext)) {
                    Sizes.SMALL -> Theme().paper.sizes.small
                    Sizes.NORMAL -> Theme().paper.sizes.normal
                    Sizes.LARGE -> Theme().paper.sizes.large
                }.invoke()

                when(this@PaperComponent.type.value(TypesContext)) {
                    Types.NORMAL -> Theme().paper.types.normal
                    Types.OUTLINE -> Theme().paper.types.outline
                    Types.GHOST -> Theme().paper.types.ghost
                }.invoke()

                Theme().paper.background()

                styling()
            }, baseClass, id, prefix) {
                this@PaperComponent.content?.let {
                    div(it.styling) { it.value(this) }
                }
            }
        }
    }
}