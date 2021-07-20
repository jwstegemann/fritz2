package dev.fritz2.components

import dev.fritz2.components.popper.PopperComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams

/**
 * Creates a popper component
 *
 * @see PopperComponent
 * @param styling a lambda expression for declaring the styling of the actual dropdown as fritz2's styling DSL
 * @param styling a lambda expression for declaring the styling of the actual dropdown as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of element
 * @param prefix the prefix for the generated CSS class resulting in the form `$prefix-$hash`
 * @param build a lambda expression for setting up the component itself
 */
fun RenderContext.popper(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String = "fc2-popper-${randomId()}",
    prefix: String = "popper",
    build: PopperComponent.() -> Unit
) = PopperComponent().apply(build).render(this, styling, baseClass, id, prefix)
