package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.components.ToastComponent.Companion.closeAllToasts
import dev.fritz2.components.ToastComponent.Companion.closeLastToast
import dev.fritz2.dom.html.Li
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.*
import dev.fritz2.styling.params.*
import dev.fritz2.styling.theme.Colors
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map


/**
 * This class combines the _configuration_ and the core styling of a toast.
 *
 * You can configure the following aspects:
 * - position of the toast: top | topLeft | topRight | bottom (default) | bottomLeft | bottomRight
 * - duration: time in ms before the toast is automatically dismissed - default is 5000 ms
 * - isCloseable : if true, a close button is added for closing the toast before the duration timer hits zero
 * - closeButtonStyle: style of the toast's close button, if displayed
 * - background: background color of the toast
 * - content : actual content of the toast, e.g. some text or an icon.
 *
 * In order to avoid having to build the toast's content manually, it is recommended to use toasts in combination with
 * alerts. This can be done by providing an [AlertComponent] as the toast's content.
 * ```
 * showToast {
 *     content {
 *          alert {
 *              title("Alert-Toast")
 *              content("This is a test-alert in a toast.")
 *          }
 *     }
 *     severity { success }
 *     variant { leftAccent }
 * }
 * ```
 *
 * Example on how to set up a toast manually:
 * ```
 * showToast {
 *     status { warning }
 *     position { bottomRight }
 *     duration { 8000 }
 *
 *     content { ... }
 * }
 * ```
 *
 * Also, there are two static helper functions for the following use cases:
 *  - [closeAllToasts] -> close all visible toasts
 *  - [closeLastToast]  -> close the last toast
 *
 * Example:
 * ```
 * clickButton {
 *      variant { outline }
 *      text("closeAll")
 * } handledBy ToastComponent.closeAllToasts()
 * ```
 */
open class ToastComponent : ManagedComponent<Unit>,
    CloseButtonProperty by CloseButtonMixin(
        "toast-close-button",
        Theme().toast.closeButton.close
    ) {

    data class ToastFragment(
        val id: String,
        val placement: String,
        val render: RenderContext.() -> Li
    )

    object Placement {

        const val bottom = "bottom"
        const val bottomLeft = "bottomLeft"
        const val bottomRight = "bottomRight"
        const val top = "top"
        const val topLeft = "topLeft"
        const val topRight = "topRight"

        val placements = listOf(bottom, bottomLeft, bottomRight, top, topLeft, topRight)

        fun appearsFromBottom(toasts: List<ToastFragment>): Boolean = toasts.isNotEmpty()
                && setOf(bottom, bottomRight, bottomLeft).contains(toasts.first().placement)
    }

    object ToastStore : RootStore<List<ToastFragment>>(listOf(), id = "toast-store") {

        val add = handle<ToastFragment> { allToasts, newToast ->
            allToasts + newToast
        }

        val remove = handle<String> { allToasts, toastId ->
            allToasts.find { toast -> toast.id == toastId }
                ?.let { allToasts.minus(it) }
                ?: allToasts
        }

        val removeAll = handle {
            emptyList()
        }

        val removeLast = handle { allToasts ->
            allToasts.dropLast(1)
        }
    }


    companion object {

        private val toastContainerStaticCss = staticStyle(
            "toastContainer",
            """
               position: fixed; 
               pointer-events: none;
               display: flex;
               flex-direction: column;
               """
        )

        private val listStyle: Style<BasicParams> = {
            display { flex }
            css("transform-origin: 50% 50% 0px;")
            css("flex-direction: column;")
            opacity { "1" }
            css("transition: opacity 1s ease-in-out;")
            overflow { auto }
        }

        private val toastStyle: Style<BasicParams> = {
            display { flex }
            css("flex-direction: row")
            position { relative { } }
            overflow { hidden }

            maxWidth { "560px" }
            minWidth { "300px" }

            margin { "0.5rem" }
            radius { "0.375rem" }

            css("pointer-events: auto;")
            css("-webkit-box-align: start;")
            css("align-items: start;")
            css("box-shadow: rgba(0, 0, 0, 0.1) 0px 10px 15px -3px, rgba(0, 0, 0, 0.05) 0px 4px 6px -2px;")
        }

        private val job = Job()
        private val globalId = "f2c-toasts-${randomId()}"
        const val defaultToastContainerPrefix = "ul-toast-container"

        init {
            // Rendering of the toast container hosting all toast messages.
            ManagedComponent.managedRenderContext(globalId, job).apply {
                Placement.placements.forEach {
                    val placementStyle = when (it) {
                        Placement.bottom -> Theme().toast.placement.bottom
                        Placement.bottomLeft -> Theme().toast.placement.bottomLeft
                        Placement.bottomRight -> Theme().toast.placement.bottomRight
                        Placement.top -> Theme().toast.placement.top
                        Placement.topLeft -> Theme().toast.placement.topLeft
                        Placement.topRight -> Theme().toast.placement.topRight
                        else -> Theme().toast.placement.bottom
                    }

                    ul({
                        placementStyle()
                        zIndex { toast }
                    }, toastContainerStaticCss, uniqueId(), defaultToastContainerPrefix) {
                        ToastStore.data
                            .map { toasts ->
                                toasts.filter { toast -> toast.placement == it }
                            }.map { toasts ->
                                /*
                                New toasts should always be stacked on existing ones, which naturally depends on the
                                basic placement:
                                - top: the new toast must be rendered *below* the existing ones
                                - bottom: the new toast must be rendered *above* the existing ones (reverse order
                                          needed)
                                 */
                                if (Placement.appearsFromBottom(toasts)
                                ) {
                                    toasts.asReversed()
                                } else {
                                    toasts
                                }
                            }
                            .renderEach { toast -> toast.render(this) }
                    }
                }
            }
        }

        fun closeAllToasts(): SimpleHandler<Unit> {
            val store = object : RootStore<String>("") {
                val closeAll = handle {
                    ToastStore.removeAll.invoke()
                    it
                }
            }
            return store.closeAll
        }

        fun closeLastToast(): SimpleHandler<Unit> {
            val store = object : RootStore<String>("") {
                val closeLatest = handle {
                    ToastStore.removeLast.invoke()
                    it
                }
            }
            return store.closeLatest
        }
    }

    val content = ComponentProperty<(RenderContext.() -> Unit)?>(null)
    val placement = ComponentProperty<Placement.() -> String> { bottomRight }
    val duration = ComponentProperty(5000L)
    val background = ComponentProperty<Colors.() -> ColorProperty> { info.main }

    /**
     * This method registers one toast at the central toast store and creates a rendering expression that will be
     * executed by the central rendering in the companion's object [ToastComponent.Companion] init block.
     *
     * @param styling lambda expression for declaring the styling of the toast using fritz2's styling DSL
     * @param baseClass optional CSS class that should be applied to the toast element
     * @param id ID of the toast element, a unique id will be generated if no ID is specified
     * @param prefix prefix for the generated CSS class of the toast element resulting in the form ``$prefix-$hash``
     */
    override fun render(
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String,
    ) {
        val localId: String = id ?: uniqueId()

        (MainScope() + job).launch {
            delay(duration.value)
            ToastStore.remove(localId)
        }

        ToastStore.add(ToastFragment(
            localId,
            placement.value(Placement)
        ) {
            li({
                listStyle()
                alignItems { center }
            }, baseClass, id, prefix) {
                div({
                    Theme().toast.base()
                    toastStyle()
                    background { color(this@ToastComponent.background.value) }
                    alignItems { center }
                    styling()
                }) {
                    this@ToastComponent.content.value?.let {
                        div({
                            css("flex: 1 1 0%;")
                        }) {
                            it.invoke(this)
                        }
                    }
                    if (this@ToastComponent.hasCloseButton.value) {
                        this@ToastComponent.closeButtonRendering.value(this).map { localId } handledBy ToastStore.remove
                    }
                }
            }
        })
    }
}

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
    prefix: String = ToastComponent.defaultToastContainerPrefix,
    build: ToastComponent.() -> Unit
) {
    ToastComponent().apply(build).render(styling, baseClass, id, prefix)
}

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
    prefix: String = ToastComponent.defaultToastContainerPrefix,
    build: ToastComponent.() -> Unit
): SimpleHandler<Unit> = asHandler {
    showToast(styling, baseClass, id, prefix, build)
}

/**
 * This factory method creates a toast with an alert as it's content and displays it _right away_.
 * Use [alertToast] in order to display a toast delayed, e.g. when a button is pressed.
 *
 * The build-lambda of this method configures the _alert_, not the _toast_. Use the [buildToast] parameter to configure
 * properties of the underlying [ToastComponent].
 *
 * Usage example:
 * ```
 * showAlertToast({
 *     duration(9000)
 *     // configure additional toast-properties here
 * }) {
 *     title("AlertToast!")
 *     content("This is an alert in a toast.")
 *     // configure additional alert-properties here
 * }
 * ```
 *
 *
 * @param styling lambda expression for declaring the styling of the toast using fritz2's styling DSL
 * @param buildToast lambda expression to configure the enclosing [ToastComponent]
 * @param baseClass optional CSS class that should be applied to the toast element
 * @param id ID of the toast element
 * @param prefix prefix for the generated CSS class of the toast element resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself.
 *
 */
fun showAlertToast(
    styling: BasicParams.() -> Unit = {},
    buildToast: ToastComponent.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = ToastComponent.defaultToastContainerPrefix,
    build: AlertComponent.() -> Unit
) {
    val alertComponent = AlertComponent()
        .apply(build)
        .apply {
            stacking { toast }
        }

    showToast {
        closeButtonStyle(Theme().toast.closeButton.close + {
            color {
                val colorScheme = alertComponent.severity.value(Theme().alert.severities).colorScheme
                when(alertComponent.variant.value(AlertComponent.VariantContext)) {
                    AlertComponent.AlertVariant.SUBTLE -> colorScheme.main
                    AlertComponent.AlertVariant.TOP_ACCENT -> colorScheme.main
                    AlertComponent.AlertVariant.LEFT_ACCENT -> colorScheme.main
                    else -> colorScheme.mainContrast
                }
            }
        })
        content {
            alertComponent.render(this, styling, baseClass, id, prefix)
        }
        buildToast()
    }
}

/**
 * This factory method creates a toast with an alert as it's content that will be shown when the returned handler is
 * triggered, eg. on a button press (similar to [toast]). The same configuration options as in [showAlertToast] are
 * provided.
 *
 * Usage example:
 * ```
 * clickButton {
 *    variant { outline }
 *    text("New alert-toast")
 * } handledBy alertToast({
 *     duration(9000)
 *     // configure additional toast-properties here
 * }) {
 *     title("AlertToast!")
 *     content("This is an alert in a toast.")
 *     // configure additional alert-properties here
 * }
 * ```
 *
 *
 * @param styling lambda expression for declaring the styling of the toast using fritz2's styling DSL
 * @param buildToast lambda expression to configure the enclosing [ToastComponent]
 * @param baseClass optional CSS class that should be applied to the toast element
 * @param id ID of the toast element
 * @param prefix prefix for the generated CSS class of the toast element resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself
 */
fun alertToast(
    styling: BasicParams.() -> Unit = {},
    buildToast: ToastComponent.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = ToastComponent.defaultToastContainerPrefix,
    build: AlertComponent.() -> Unit
): SimpleHandler<Unit> = asHandler {
    showAlertToast(styling, buildToast, baseClass, id, prefix, build)
}


private fun asHandler(action: () -> Unit): SimpleHandler<Unit> = object : RootStore<Unit>(Unit) {
    val handler = handle { action() }
}.handler
