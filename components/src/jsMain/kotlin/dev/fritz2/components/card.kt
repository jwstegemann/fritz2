package dev.fritz2.components

import dev.fritz2.components.card.CardComponent
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Scope
import dev.fritz2.dom.html.ScopeContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams

/**
 * This factory function creates a [CardComponent].
 *
 * @param styling a lambda expression for declaring the styling of the toast using fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the toast element
 * @param id the ID of the toast element
 * @param prefix the prefix for the generated CSS class of the toast element resulting in the form ``$prefix-$hash``
 * @param scope the scope in which the component is rendered
 * @param build a lambda expression for setting up the component itself
 */
fun RenderContext.card(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "card",
    scope: ScopeContext.() -> Unit = {},
    build: CardComponent.() -> Unit,
) {
    val actualScope = ScopeContext(Scope()).apply(scope).scope
    CardComponent(actualScope).apply(build).render(this, styling, baseClass, id, prefix)
}