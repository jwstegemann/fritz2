package dev.fritz2.components

import dev.fritz2.binding.SimpleHandler
import dev.fritz2.components.toast.AlertToastComponent
import dev.fritz2.components.toast.ToastComponent
import dev.fritz2.components.toast.asHandler
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams


/**
 * This factory method creates a toast and displays it _right away_.
 * Use [toast] in order to display a toast delayed, e.g. when a button is pressed.
 *
 * A toast usually consists of a content you prefer via the `content { ... }` property. Additional properties can be
 * set as well:
 * ```
 * showToast {
 *     position { bottomRight }
 *     ...
 *     content {
 *         ...
 *     }
 * }
 * ```
 *
 * For a detailed overview about the possible properties of the component object itself and more use cases,
 * have a look at [ToastComponent].
 *
 *
 * @param styling lambda expression for declaring the styling of the toast using fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the toast element
 * @param id ID of the toast element
 * @param prefix prefix for the generated CSS class of the toast element resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself.
 *
 */
fun showToast(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "toast",
    build: ToastComponent.() -> Unit
) = ToastComponent().apply(build).render(styling, baseClass, id, prefix)

/**
 * This factory method creates a toast that will be shown when the returned handler is triggered, eg. on a button press.
 *
 * You can bind this toast to a Flow where every element of this Flow will then create a toast or
 * you may combine the toast directly with some other component that has a listener which fits to our handler, like for
 * example a [clickButton].
 *
 * Usage example:
 * ```
 * clickButton {
 *    variant { outline }
 *    text("ADD TOAST")
 * } handledBy toast {
 *    position { topLeft }
 *    duration(5000L)
 *    ...
 *    content {
 *        ...
 *    }
 * }
 * ```
 *
 * For a detailed overview of the possible properties of the component object itself and more use cases,
 * have a look at [ToastComponent].
 *
 *
 * @param styling lambda expression for declaring the styling of the toast using fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the toast element
 * @param id ID of the toast element
 * @param prefix prefix for the generated CSS class of the toast element resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself
 */
fun toast(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "toast",
    build: ToastComponent.() -> Unit
): SimpleHandler<Unit> = asHandler {
    showToast(styling, baseClass, id, prefix, build)
}

/**
 * This factory method creates a toast with an alert as its content and displays it _right away_.
 * Use [alertToast] in order to display a toast delayed, e.g. when a button is pressed.
 *
 * Usage example:
 * ```
 * showAlertToast {
 *     // configuration of the toast:
 *     duration(9000)
 *     position { bottomRight }
 *
 *     // configuration of the alert:
 *     alert {
 *         title("AlertToast!")
 *         content("This is an alert in a toast.")
 *     }
 * }
 * ```
 *
 * @param styling lambda expression for declaring the styling of the toast using fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the toast element
 * @param id ID of the toast element
 * @param prefix prefix for the generated CSS class of the toast element resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself.
 *
 */
fun showAlertToast(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "toast-alert",
    build: AlertToastComponent.() -> Unit
) = AlertToastComponent().apply(build).render(styling, baseClass, id, prefix)

/**
 * This factory method creates a toast with an alert as it's content that will be shown when the returned handler is
 * triggered, eg. on a button press (similar to [toast]). The same configuration options as in [showAlertToast] are
 * provided.
 *
 * Please note: All parameters (styling, id, prefix, etc.) are applied to the alert, not the toast.
 *
 * Usage example:
 * ```
 * clickButton {
 *    variant { outline }
 *    text("New alert-toast")
 * } handledBy alertToast {
 *     // configuration of the toast:
 *     duration(9000)
 *     position { bottomRight }
 *
 *     // configuration of the alert:
 *     alert {
 *         title("AlertToast!")
 *         content("This is an alert in a toast.")
 *     }
 * }
 * ```
 *
 * @param styling lambda expression for declaring the styling of the toast using fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the toast element
 * @param id ID of the toast element
 * @param prefix prefix for the generated CSS class of the toast element resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself
 */
fun alertToast(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "toast-alert",
    build: AlertToastComponent.() -> Unit
): SimpleHandler<Unit> = asHandler {
    showAlertToast(styling, baseClass, id, prefix, build)
}

