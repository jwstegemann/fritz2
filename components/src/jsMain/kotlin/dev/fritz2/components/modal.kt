package dev.fritz2.components

import dev.fritz2.binding.SimpleHandler
import dev.fritz2.components.modal.ModalComponent
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams


/**
 * This component provides some modal dialog or messagebox. Basically it just offers a ``div`` that is rendered on a
 * higher [z-index](https://developer.mozilla.org/en-US/docs/Web/CSS/z-index) than the rest of the application.
 * It uses the reserved segments provided by the [dev.fritz2.styling.theme.ZIndices.modal] function that are
 * initialized by [dev.fritz2.styling.theme.Theme.zIndices].
 * That way the top modal will always have the highest ``z-index`` and therefore be on top of the screen.
 *
 * The content and structure within the modal are completely free to model.
 *
 * As this factory function also returns a [SimpleHandler<Unit>], it is easy to combine it directly with some other
 * component that offers some sort of Flow, like a [clickButton].
 *
 * Basic usage
 * ```
 * clickButton {
 *     text("Open")
 * } handledBy modal {
 *     content { // provide arbitrary content
 *         p { +"Hello world from a modal!" }
 *     }
 * }
 * ```
 *
 * @see ModalComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [ModalComponent]
 *              be aware that a [SimpleHandler<Unit>] is injected in order to apply it to some closing flow inside!
 */
fun modal(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "modal",
    build: ModalComponent.() -> Unit
): SimpleHandler<Unit> = ModalComponent().apply(build).render(styling, baseClass, id, prefix)
