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
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.rgb
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.*
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

typealias ToastRenderContext = RenderContext.() -> Li
typealias AddToast = () -> Unit

data class ToastListElement(
    val toastRenderContext: ToastRenderContext,
    val id: String,
    val position: String,
    val duration: Long,
)

object Position {

    const val bottom = "bottom"
    const val bottomLeft = "bottomLeft"
    const val bottomRight = "bottomRight"
    const val top = "top"
    const val topLeft = "topLeft"
    const val topRight = "topRight"

    val positionList = listOf(bottom, bottomLeft, bottomRight, top, topLeft, topRight)
}

private const val defaultToastContainerPrefix = "ul-toast-container"
private const val defaultInnerToastPrefix = "toast-inner"

/**
 * This class combines the _configuration_ and the core styling of a toast.
 *
 * You can configure the following aspects:
 * - position of the toast: top | topLeft | topRight | bottom (default) | bottomLeft | bottomRight
 * - status:  success | error | warning | info(default)
 * - duration: time in ms before dismiss the toast - default are 5000 ms
 * - isCloseable : if true, a close button is added for closing the toast before timer is expired
 * - icon : icon of the toast - default icon is Theme().icons.circleInformation
 * - content : the toast's content (e.g. some text)
 *
 * As an alternative to the 'content { ... }` method there are also a hand full of convenience factories available
 * which you can use to display simple toasts consisting of only a title and description as well as an optional
 * close-button:
 * - [showInfoToast]
 * - [showSuccessToast]
 * - [showWarningToast]
 * - [showErrorToast]
 *
 * Example on how to set a status, position, duration and icon:
 * ```
 * showToast {
 *     status { warning }
 *     position { bottomRight }
 *     duration { 8000 }
 *     icon { arrowRight }
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
 * @param renderContext The current RenderContext under which the toast will be rendered
 */
@ComponentMarker
class ToastComponent(private val renderContext: RenderContext) {

    object ToastStore : RootStore<List<ToastListElement>>(listOf(), id = "toast-store") {

        val add = handle<ToastListElement> { allToasts, newToast ->
            allToasts + newToast
        }

        val remove = handle<ToastListElement> { allToasts, currentToast ->
            allToasts - currentToast
        }

        val removeAll = handle { _ ->
            emptyList()
        }

        val removeLast = handle { allToasts ->
            allToasts.dropLast(1)
        }
    }


    companion object {

        private val staticCss = staticStyle(
            "toastContainer",
            """
               position: fixed; 
               z-index: 5500;
               pointer-events: none;
               display: flex;
               flex-direction: column;
           
               """
        )
        val toastMap = mutableMapOf<String, ToastListElement>()


        init {
            // Rendering of the toast container hosting all toast messages.
            render(targetElement = document.body, override = false) {
                div {
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

                        (::ul.styled(staticCss, id, defaultToastContainerPrefix) {
                            placementStyle()
                        }){
                            ToastStore.data.map { toastList -> toastList.filter { toast -> toast.position == it } }
                                .renderEach { element -> element.toastRenderContext.invoke(this) }
                        }
                    }
                }
            }
        }


        // TODO: Are these really needed?:

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


    var listStyle: Style<BasicParams> = {
        display { flex }
        css("transform-origin: 50% 50% 0px;")
        css("flex-direction: column;")
        opacity { "1" }
        css("transition: opacity 1s ease-in-out;")
        overflow { auto }
    }

    val toastInner: Style<BasicParams> = {
        maxWidth { "560px" }
        minWidth { "300px" }
        height { "72px" }
        margin { "0.5rem" }
        css("pointer-events: auto;")
        radius { "0.375rem" }
    }

    val alertStyle: Style<BasicParams> = {
        display { flex }
        position { relative { } }
        overflow { hidden }
        background { rgb(56, 161, 105) }
        color { rgb(255, 255, 255) }
        textAlign { left }
        paddings {
            top { "0.75rem" }
            right { "0.75rem" }
            bottom { " 0.75rem" }
            left { " 1rem" }
        }

        css("  -webkit-box-align: start;")
        css(" align-items: start;")
        css(" box-shadow: rgba(0, 0, 0, 0.1) 0px 10px 15px -3px, rgba(0, 0, 0, 0.05) 0px 4px 6px -2px;")

    }


    var content: (RenderContext.() -> Unit)? = null
    fun content(value: RenderContext.() -> Unit) {
        content = value
    }

    var icon: IconDefinition = Theme().icons.circleInformation
    fun icon(value: Icons.() -> IconDefinition) {
        icon = Theme().icons.value()
    }

    var position: String = bottom
    fun position(value: String) {
        position = value
    }

    fun position(value: Position.() -> String) {
        position = Position.value()
    }

    var status: (ToastStatus.() -> Style<BasicParams>) = { Theme().toast.status.info }
    fun status(value: ToastStatus.() -> Style<BasicParams>) {
        status = value
    }

    var duration: Long = 5000L
    fun duration(value: () -> Long) {
        duration = value()
    }

    fun duration(value: Long) {
        duration = value
    }

    var isCloseable: Boolean = true
    fun isCloseable(value: () -> Boolean) {
        isCloseable = value()
    }

    fun isCloseable(value: Boolean) {
        isCloseable = value
    }

    var closeButton: (RenderContext.(SimpleHandler<Unit>) -> Unit)? = null
    private fun closeButton(
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = "toast-close-button",
        build: PushButtonComponent.() -> Unit = {},
    ) {
        closeButton = { closeHandle ->
            clickButton({
                Theme().toast.closeButton.close()

            }, baseClass, id, prefix) {
                variant { ghost }
                color { light }
                icon { fromTheme { close } }
                build()
            }.map { Unit } handledBy closeHandle
        }
    }


    /**
     * Makes the toast visible on the screen.
     *
     * @param styling Additional styling of the toast
     * @param baseClass Base class of the toast style
     * @param id Specific id to use for the toast
     * @param prefix Specific prefix to use for the generated CSS of the toast
     */
    fun show(
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = defaultInnerToastPrefix
    ) {
        val toastId = id?: uniqueId()

        var job: Job? = null
        val clickStore = object : RootStore<String>("") {
            val delete = handle {
                job?.cancel()
                val currentToastListElement = toastMap[toastId]
                document.getElementById(toastId)!!.setAttribute("style", "opacity: 0;")
                toastMap.remove(toastId)
                delay(1020)
                ToastStore.remove(currentToastListElement!!)
                it
            }
        }

        if (isCloseable)
            closeButton()

        val toast: ToastRenderContext = {
            (::li.styled(id = toastId) {
                listStyle()
                alignItems { center }
            }){
                (::div.styled(prefix = prefix, baseClass = baseClass) {
                    toastInner()
                    status.invoke(Theme().toast.status)()
                    content?.let { alertStyle() }
                    styling()
                }){
                    (::span.styled() {
                        margins { right { "0.75rem" } }
                        width { "1.25rem" }
                        height { "1.5rem" }
                        display { inherit }
                        flex { shrink { "0" } }
                    }){
                        icon {
                            fromTheme { icon }
                        }
                    }
                    content?.let {
                        (::div.styled {
                            css("flex: 1 1 0%;")
                        }) {
                            content!!.invoke(this)
                        }
                    }
                    closeButton?.invoke(this, clickStore.delete)
                }
            }
        }

        val listContext = ToastListElement(toast, toastId, position, duration)
        toastMap[toastId] = listContext
        ToastStore.add(listContext)

        job = GlobalScope.launch {
            delay(duration)
            ToastStore.remove(listContext)
        }

    }
}

/**
 * This factory method creates a toast and displays it _right away_.
 * Use [delayedToast] in order to display a toast delayed, e.g. when a button is pressed.
 *
 * A toast usually consists of a title and a description but you are free to specify any content you prefer via the
 * `content { ... }` method. In most cases it should be sufficient to use on of the convenience factories
 * [showInfoToast], [showSuccessToast], [showWarningToast] or [showErrorToast], though. They use a unified layout
 * consisting of a title and a description which you can simply pass as a parameter.
 *
 * Example of a custom content:
 * ```
 * showToast {
 *     content {
 *         p { +"My toast content"}
 *     }
 * }
 * ```
 *
 * For a detailed overview about the possible properties of the component object itself and more use cases,
 * have a look at [ToastComponent].
 *
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
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
    val component = ToastComponent(this).apply(build)
    component.show(styling, baseClass, id, prefix)
}

/**
 * Convenience factory that creates an info-toast with a given [title] and [description].
 *
 * Additional adjustments can be made via the standard `build`-lambda. Note that any custom _content_ will be ignored,
 * however!
 *
 * @param title Text to be used for the title of the toast
 * @param description Text to be used for the description (secondary text) of the toast.
 * @param build Optional additional build arguments
 */
fun RenderContext.showInfoToast(
    title: String,
    description: String,
    build: ToastComponent.() -> Unit = {},
) = showToastWithTitleAndDescription(title, description, status = { info }, build)

/**
 * Convenience factory that creates a success-toast with a given [title] and [description].
 *
 * Additional adjustments can be made via the standard `build`-lambda. Note that any custom _content_ will be ignored,
 * however!
 *
 * @param title Text to be used for the title of the toast
 * @param description Text to be used for the description (secondary text) of the toast.
 * @param build Optional additional build arguments
 */
fun RenderContext.showSuccessToast(
    title: String,
    description: String,
    build: ToastComponent.() -> Unit = {},
) = showToastWithTitleAndDescription(title, description, status = { success }, build)

/**
 * Convenience factory that creates a warning-toast with a given [title] and [description].
 *
 * Additional adjustments can be made via the standard `build`-lambda. Note that any custom _content_ will be ignored,
 * however!
 *
 * @param title Text to be used for the title of the toast
 * @param description Text to be used for the description (secondary text) of the toast.
 * @param build Optional additional build arguments
 */
fun RenderContext.showWarningToast(
    title: String,
    description: String,
    build: ToastComponent.() -> Unit = {},
) = showToastWithTitleAndDescription(title, description, status = { warning }, build)

/**
 * Convenience factory that creates an error-toast with a given [title] and [description].
 *
 * Additional adjustments can be made via the standard `build`-lambda. Note that any custom _content_ will be ignored,
 * however!
 *
 * @param title Text to be used for the title of the toast
 * @param description Text to be used for the description (secondary text) of the toast.
 * @param build Optional additional build arguments
 */
fun RenderContext.showErrorToast(
    title: String,
    description: String,
    build: ToastComponent.() -> Unit = {},
) = showToastWithTitleAndDescription(title, description, status = { error }, build)

private fun RenderContext.showToastWithTitleAndDescription(
    title: String,
    description: String,
    status: ToastStatus.() -> Style<BasicParams>,
    build: ToastComponent.() -> Unit,
) {
    showToast {
        // Apply default settings
        this.status = status
        position { bottomRight }
        isCloseable(true)

        // Apply additional build functions. Content will be ignored as it gets overwritten below.
        build()

        content {
            div {
                (::div.styled(prefix = "toast-title") {
                    fontWeight { "700" }
                    lineHeight { "1.5rem" }
                    margins {
                        right { "0.5rem" }
                    }

                }){ +title }

                (::div.styled(prefix = "toast-description") {
                    display { block }
                    lineHeight { "1.5rem" }

                }){ +description }
            }
        }
    }
}

/**
 * This factory method creates a toast that will be shown when the returned handler is triggered, eg. on a button press.
 *
 * You can bind this toast to a Flow where every element of this Flow will then create a toast or
 * you may combine the toast directly with some other component that has a listener which fits to our handler, like for
 * example a [clickButton].
 *
 * Example of binding a toast message to a Flow:
 * ```
 * val myFlow = flowOf(listOf(1 to "one", 2 to "two", 3 to "three"))
 *
 * myFlow.render {
 *     toast {
 *         status { warning }
 *         position { bottomRight }
 *         title { it.first.toString() }
 *         description { it.second }
 *         duration { 8000 }
 *     }
 * }
 * ```
 *
 * Example of binding a toast message to a button event:
 * ```
 * clickButton {
 *    variant { outline }
 *    text("ADD TOAST")
 * } handledBy showToastDelayed {
 *
 *    position { topLeft }
 *    title("Title")
 *    description("Description")
 * }
 * ```
 *
 * For a detailed overview about the possible properties of the component object itself and more use cases,
 * have a look at [ToastComponent].
 *
 *
 * @param styling a lambda expression for declaring the styling of the toast using fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the toast element
 * @param id the ID of the toast element
 * @param prefix the prefix for the generated CSS class of the toast element resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself
 */
fun RenderContext.delayedToast(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = defaultToastContainerPrefix,
    build: ToastComponent.() -> Unit
): SimpleHandler<Unit> {

    val component = ToastComponent(this).apply(build)

    val pendingToastStore = object : RootStore<AddToast>({}) {
        val show = handle {
            component.show(styling, baseClass, id, prefix)
            it
        }
    }
    return pendingToastStore.show
}