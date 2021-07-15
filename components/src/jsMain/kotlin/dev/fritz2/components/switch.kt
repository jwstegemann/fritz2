package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.components.switch.SwitchComponent
import dev.fritz2.dom.html.Label
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BasicParams

/**
 * This component generates a switch.
 * So this component supports the use case to select or deselect an option. As alternative there is also the
 * [checkbox].
 *
 * You can set different kind of properties like the label or different styling aspects like the colors of the
 * background, the label or the checked state. Further more there are configuration functions for accessing the checked
 * state of this box or totally disable it.
 *
 * Basic usage
 * ```
 * val cheeseStore = storeOf(false)
 * switch(value=cheeseStore) {
 *      label("with extra cheese") // set the label
 * }
 * ```
 *
 * @see SwitchComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param value a boolean store to handle the state and its changes automatically
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [SwitchComponent]
 */
fun RenderContext.switch(
    styling: BasicParams.() -> Unit = {},
    value: Store<Boolean>? = null,
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "switchComponent",
    build: SwitchComponent.() -> Unit = {}
): Label = SwitchComponent(value).apply(build).render(this, styling, baseClass, id, prefix)
