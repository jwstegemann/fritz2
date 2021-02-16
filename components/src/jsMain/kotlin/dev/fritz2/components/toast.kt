package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.binding.invoke
import dev.fritz2.components.Position.bottom
import dev.fritz2.components.Position.bottomLeft
import dev.fritz2.components.Position.bottomRight
import dev.fritz2.components.Position.top
import dev.fritz2.components.Position.topLeft
import dev.fritz2.components.Position.topRight
import dev.fritz2.components.ToastComponent.Companion.closeAllToasts
import dev.fritz2.components.ToastComponent.Companion.closeLastToast
import dev.fritz2.dom.html.*
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.ColorProperty
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map


private const val defaultToastContainerPrefix = "ul-toast-container"
private const val defaultInnerToastPrefix = "toast-inner"

object Position {

    const val bottom = "bottom"
    const val bottomLeft = "bottomLeft"
    const val bottomRight = "bottomRight"
    const val top = "top"
    const val topLeft = "topLeft"
    const val topRight = "topRight"

    val positionList = listOf(bottom, bottomLeft, bottomRight, top, topLeft, topRight)
}

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
 * alerts. This can be done by providing an [AlertComponent] as the toast's content or by using one of the convenience
 * methods provided, such as [alertToast] or [showAlertToast]:
 * ```
 * showAlertToast {
 *     title("Alert-Toast")
 *     content("This is a test-alert in a toast.")
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
 *
 * @param styling lambda expression for declaring the styling of the toast using fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the toast element
 * @param id ID of the toast element, a unique id will be generated if no ID is specified
 * @param prefix prefix for the generated CSS class of the toast element resulting in the form ``$prefix-$hash``
 */
@ComponentMarker
class ToastComponent(
    private val styling: BasicParams.() -> Unit = {},
    private val baseClass: StyleClass? = null,
    id: String? = null,
    private val prefix: String = defaultInnerToastPrefix
) {

    object ToastStore : RootStore<List<ToastComponent>>(listOf(), id = "toast-store") {

        val add = handle<ToastComponent> { allToasts, newToast ->
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
               z-index: 5500;
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
            css("  -webkit-box-align: start;")
            css(" align-items: start;")
            css(" box-shadow: rgba(0, 0, 0, 0.1) 0px 10px 15px -3px, rgba(0, 0, 0, 0.05) 0px 4px 6px -2px;")
        }

        private val job = Job()
        private val globalId = "f2c-modals-${randomId()}"

        init {
            // Rendering of the toast container hosting all toast messages.
            globalRenderContext(globalId, job).apply {
                Position.positionList.forEach {
                    val placementStyle = when (it) {
                        bottom -> Theme().toast.placement.bottom
                        bottomLeft -> Theme().toast.placement.bottomLeft
                        bottomRight -> Theme().toast.placement.bottomRight
                        top -> Theme().toast.placement.top
                        topLeft -> Theme().toast.placement.topLeft
                        topRight -> Theme().toast.placement.topRight
                        else -> Theme().toast.placement.bottom
                    }

                    (::ul.styled(toastContainerStaticCss, id, defaultToastContainerPrefix) {
                        placementStyle()
                    }){
                        ToastStore.data
                            .map { toasts -> toasts.filter { toast -> toast.position == it } }
                            .renderEach { toast -> renderToast(toast) }
                    }
                }
            }
        }

        private fun RenderContext.renderToast(toast: ToastComponent): Li {

            if (toast.isCloseable)
                toast.closeButton()

            (MainScope() + job).launch {
                delay(toast.duration)
                ToastStore.remove(toast.id)
            }

            return (::li.styled(toast.baseClass, toast.id, toast.prefix) {
                listStyle()
                alignItems { center }
            }){
                (::div.styled {
                    toastStyle()
                    background { color(toast.background) }
                    alignItems { center }
                    toast.styling()
                }){
                    toast.content?.let {
                        (::div.styled {
                            css("flex: 1 1 0%;")
                        }) {
                            toast.content?.invoke(this)
                        }
                    }
                    toast.closeButton?.let {
                        (::div.styled {
                            position {
                                absolute {
                                    right { small }
                                }
                            }
                        }) {
                            toast.closeButton?.invoke(this)
                        }
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


    val id: String = id ?: uniqueId()


    private var content: (RenderContext.() -> Unit)? = null

    fun content(value: RenderContext.() -> Unit) {
        content = value
    }

    private var position: String = bottomRight

    fun position(value: String) {
        position = value
    }

    fun position(value: Position.() -> String) {
        position = Position.value()
    }

    private var duration: Long = 5000L

    @Suppress("unused")
    fun duration(value: () -> Long) {
        duration = value()
    }

    @Suppress("unused")
    fun duration(value: Long) {
        duration = value
    }

    private var background: Colors.() -> ColorProperty = { info }

    fun background(value: Colors.() -> ColorProperty) {
        background = value
    }

    private var isCloseable: Boolean = true

    @Suppress("unused")
    fun isCloseable(value: () -> Boolean) {
        isCloseable = value()
    }

    @Suppress("unused")
    fun isCloseable(value: Boolean) {
        isCloseable = value
    }

    private var closeButtonStyle: Style<BasicParams> = { }

    fun closeButtonStyle(value: Style<BasicParams>) {
        closeButtonStyle = value
    }

    private var closeButton: (RenderContext.() -> Unit)? = null

    private fun closeButton(
        baseClass: StyleClass? = null,
        prefix: String = "toast-close-button",
        build: PushButtonComponent.() -> Unit = {},
    ) {
        closeButton = {
            clickButton({
                Theme().toast.closeButton.close()
                closeButtonStyle()
            }, baseClass, id, prefix) {
                variant { ghost }
                color { lightGray }
                icon { fromTheme { close } }
                build()
            }.events.map { this@ToastComponent.id } handledBy ToastStore.remove
        }
    }


    /**
     * Makes the toast visible.
     */
    fun show() = ToastStore.add(this)
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
fun RenderContext.showToast(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = defaultToastContainerPrefix,
    build: ToastComponent.() -> Unit,
) {
    ToastComponent(styling, baseClass, id, prefix).apply(build).show()
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
 *    duration { 5000L }
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
fun RenderContext.toast(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = defaultToastContainerPrefix,
    build: ToastComponent.() -> Unit
): SimpleHandler<Unit> {

    val pendingToastStore = object : RootStore<Unit>(Unit) {
        val show = handle {
            showToast(styling, baseClass, id, prefix, build)
        }
    }
    return pendingToastStore.show
}