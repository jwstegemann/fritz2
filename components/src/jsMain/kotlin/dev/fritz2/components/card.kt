package dev.fritz2.components

import dev.fritz2.components.card.CardComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams

/**
 * This factory function creates a [CardComponent].
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
 * ```
 *
 * Have a look at [CardComponent] for the full explanation and additional examples.
 *
 * @param styling a lambda expression for declaring the styling of the underlying PaperComponent using fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the underlying PaperComponent
 * @param id the ID of the underlying PaperComponent
 * @param prefix the prefix for the generated CSS class of the underlying PaperComponent resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the underlying PaperComponent
 */
fun RenderContext.card(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "card",
    build: CardComponent.() -> Unit,
) = CardComponent().apply(build).render(this, styling, baseClass, id, prefix)