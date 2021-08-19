package dev.fritz2.components

import dev.fritz2.components.foundations.randomId
import dev.fritz2.components.popup.PopupComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams

/**
 * Creates a popup component
 *
 * @see PopupComponent
 * @param styling a lambda expression for declaring the styling of the actual dropdown as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of element
 * @param prefix the prefix for the generated CSS class resulting in the form `$prefix-$hash`
 * @param build a lambda expression for setting up the component itself
 */
fun RenderContext.popup(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String = "fc2-popup-${randomId()}",
    prefix: String = "popup",
    build: PopupComponent.() -> Unit
) = PopupComponent().apply(build).render(this, styling, baseClass, id, prefix)
