package dev.fritz2.components.paper

import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.div
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.theme.Theme

/**
 * This class combines the configuration and rendering of a PaperComponent.
 *
 * A PaperComponent is simply an elevated card-like box with a solid background and corner radii / paddings that vary
 * depending on the specified [size] of the component.
 *
 * There are different [type]s of paper:
 * - `normal` ([Types.Normal]): Paper sheet appears card-like with a box-shadow.
 * - `outline` ([Types.Outline]): Paper sheet does not appear elevated but with an outline instead.
 * - `ghost` ([Types.Ghost]): Paper sheet neither appears elevated nor outlined.
 *
 * The content is specified via the [content] property.
 *
 *
 * Example:
 * ```
 * paper {
 *     size { normal }
 *     type { normal }
 *     content {
 *         span { +"This is paper." }
 *     }
 * }
 * ```
 */
open class PaperComponent : Component<Unit> {

    enum class Sizes {
        Small, Normal, Large
    }

    object SizesContext {
        val small = Sizes.Small
        val normal = Sizes.Normal
        val large = Sizes.Large
    }

    val size = ComponentProperty<SizesContext.() -> Sizes> { normal }


    enum class Types {
        Normal, Outline, Ghost
    }

    object TypesContext {
        val normal = Types.Normal
        val outline = Types.Outline
        val ghost = Types.Ghost
    }

    val type = ComponentProperty<TypesContext.() -> Types> { normal }


    val content = ComponentProperty<RenderContext.() -> Unit> {}


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
                    Sizes.Small -> Theme().paper.sizes.small
                    Sizes.Normal -> Theme().paper.sizes.normal
                    Sizes.Large -> Theme().paper.sizes.large
                }.invoke()

                when(this@PaperComponent.type.value(TypesContext)) {
                    Types.Normal -> Theme().paper.types.normal
                    Types.Outline -> Theme().paper.types.outline
                    Types.Ghost -> Theme().paper.types.ghost
                }.invoke()

                Theme().paper.background()

                styling()
            }) {
                this@PaperComponent.content.value(this)
            }
        }
    }
}