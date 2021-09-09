package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.components.radios.RadioComponent
import dev.fritz2.dom.html.Label
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams


/**
 * This component generates a *single* radio.
 * So this component supports the use case to select or deselect an option.
 * In general prefer a [checkbox] for this use case.
 * If an application has a strong focus on mobile, consider a [switch] instead!
 *
 * This component mostly exist only as base item for a [radioGroup]!
 *
 * You can set different kind of properties like the label or different styling aspects like the colors of the
 * background, the label or the checked state. Further more there are configuration functions for accessing the checked
 * state of this box or totally disable it.
 *
 * Example usage
 * ```
 * val cheeseStore = storeOf(false)
 * radio(value = cheeseStore) {
 *      label("with extra cheese") // set the label
 * }
 * ```
 *
 * @see RadioComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param value a boolean store to handle the state and its changes automatically
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [RadioComponent]
 */
fun RenderContext.radio(
    styling: BasicParams.() -> Unit = {},
    value: Store<Boolean>? = null,
    baseClass: StyleClass = StyleClass.None,
    id: String? = value?.id,
    prefix: String = "radioComponent",
    build: RadioComponent.() -> Unit = {}
): Label = RadioComponent(value).apply(build).render(this, styling, baseClass, id, prefix)

