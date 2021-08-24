package dev.fritz2.components

import dev.fritz2.components.paper.PaperComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams

/**
 * This factory function creates a [PaperComponent].
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
 * ```
 *
 * Have a look at [PaperComponent] for the full explanation.
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself.
 */
fun RenderContext.paper(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "paper",
    build: PaperComponent.() -> Unit,
) = PaperComponent().apply(build).render(this, styling, baseClass, id, prefix)