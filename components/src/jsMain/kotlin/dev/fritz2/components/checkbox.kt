package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.components.checkboxes.CheckboxComponent
import dev.fritz2.dom.html.Label
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams

/**
 * This component generates a *single* checkbox.
 * So this component supports the use case to select or deselect an option. If an application has a strong focus on
 * mobile, consider a [switch] instead!
 *
 * Example usage
 * ```
 * val cheeseStore = storeOf(false)
 * checkbox(value = cheeseStore) {
 *      label("with extra cheese") // set the label
 * }
 * ```
 *
 * @see CheckboxComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param value a boolean store to handle the state and its changes automatically
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [CheckboxComponent]
 */
fun RenderContext.checkbox(
    styling: BasicParams.() -> Unit = {},
    value: Store<Boolean>? = null,
    baseClass: StyleClass = StyleClass.None,
    id: String? = value?.id,
    prefix: String = "checkboxComponent",
    build: CheckboxComponent.() -> Unit = {}
): Label = CheckboxComponent(value).apply(build).render(this, styling, baseClass, id, prefix)
