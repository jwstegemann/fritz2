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
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.Li
import dev.fritz2.dom.html.RenderContext
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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

private const val defaultToastContainerId = "ul-toast-container"

/**
 * This class combines the _configuration_ and the core styling of a toast
 *
 * You can configure the following aspects:
 * - position of the toast: top | topLeft | topRight | bottom (default) | bottomLeft | bottomRight
 * - title: title of the toast
 * - description: description of the toast
 * - status:  success | error | warning | info(default)
 * - duration: time in ms before dismiss the toast - default are 5000 ms
 * - isCloseable : if true, a close button is added for closing the toast before timer is expired
 * - icon : icon of the toast - default icon is Theme().icons.circleInformation
 * - customComponent : custom component to render instead of title and description
 *     * with a custom component you have not the ability to use the close button !
 *
 * Also, there are 2 static helper functions for the following use cases:
 *  - fun closeAllToasts() -> close all visible toasts
 *  - fun closeLastToast()  -> close the last toast
 *
 * A use case would look like :
 *
 * ```
 * clickButton {
 *   variant { outline }
 *   text("closeAll")
 * } handledBy ToastComponent.closeAllToasts()
 * ```
 *
 * or
 *
 * ```
 * clickButton {
 *    variant { outline }
 *    text("closeLatest")
 * } handledBy ToastComponent.closeLastToast()
 * ```
 *
 * Use case showing how to configure position, duration and status:
 *
 * ```
 * showToast {
 *     status { warning }
 *     position { bottomRight }
 *     duration { 8000 }
 *
 *     title { "Title" }
 *     description { "Description" }
 * }
 * ```
 *
 * use case showing how to set a custom icon:
 *
 * ```
 * showToast {
 *     icon { arrowRight }
 *     title { "Icon"}
 *     description {"custom icon"}
 * }
 * ```
 *
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
        val staticCss = staticStyle(
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

        // Indicates whether the Div-container hosting the individual ToastListElements
        // has already been added to the DOM.
        // TODO: Should this be synchronized?
        private var containerRendered = false


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

    var title: (RenderContext.() -> Unit)? = null
    fun title(value: Flow<String>) {
        title = {
            (::div.styled(prefix = "toast-title") {
                fontWeight { "700" }
                lineHeight { "1.5rem" }
                margins {
                    right { "0.5rem" }
                }

            }){ value.asText() }
        }
    }

    fun title(value: String) {
        this.title(flowOf(value))
    }

    fun title(value: () -> String) {
        this.title(flowOf(value()))
    }


    var description: (RenderContext.() -> Unit)? = null
    fun description(value: Flow<String>) {
        description = {
            (::div.styled(prefix = "toast-description") {
                display { block }
                lineHeight { "1.5rem" }

            }){ value.asText() }
        }
    }

    fun description(value: String) {
        this.description(flowOf(value))
    }

    fun description(value: () -> String) {
        this.description(flowOf(value()))
    }

    var customComponent: (RenderContext.() -> Unit)? = null
    fun customComponent(value: RenderContext.() -> Unit) {
        customComponent = value
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

    var duration: Long = 5000
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

    private fun renderToastContainer(
        renderContext: RenderContext,
        styling: BasicParams.() -> Unit,
        baseClass: StyleClass?,
        id: String?,
        prefix: String
    ) {
        renderContext.div {
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

                (::ul.styled(styling, staticCss + baseClass, id, prefix) {
                    placementStyle()
                }){
                    ToastStore.data.map { toastList -> toastList.filter { toast -> toast.position == it } }
                        .renderEach { element -> element.toastRenderContext.invoke(this) }
                }
            }
        }
    }

    private fun renderTitleAndDescription(renderContext: Div) {
        if (isCloseable) {
            closeButton()
        }

        if (title == null || description == null) {
            renderContext.apply {
                p { +"You have to define a title and a description or you just define a custom component !" }
            }
        } else {
            renderContext.apply {
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
                (::div.styled {
                    css("flex: 1 1 0%;")
                }){
                    title?.invoke(this)
                    description?.invoke(this)
                }
            }
        }
    }

    private fun renderCustomComponent(renderContext: Div) {
        renderContext.apply {
            (::div.styled {

            }){
                customComponent!!.invoke(this)
            }

        }
    }

    fun show(styling: BasicParams.() -> Unit = {},
             baseClass: StyleClass? = null,
             id: String? = null,
             prefix: String = defaultToastContainerId) {

        // TODO: Render via 'appendToBody' once a solution is found
        if (!containerRendered) {
            renderToastContainer(renderContext, styling, baseClass, id, prefix)
            containerRendered = true
        }

        val listId = uniqueId()
        var job: Job? = null
        val clickStore = object : RootStore<String>("") {
            val delete = handle {
                job?.cancel()
                val currentToastListElement = toastMap[listId]
                document.getElementById(listId)!!.setAttribute("style", "opacity: 0;")
                toastMap.remove(listId)
                delay(1020)
                ToastStore.remove(currentToastListElement!!)
                it
            }
        }

        val toast: ToastRenderContext = {
            (::li.styled(id = listId) {
                listStyle()
                alignItems { center }
            }){
                (::div.styled(prefix = "toast-inner") {
                    toastInner()
                    status.invoke(Theme().toast.status)()
                    customComponent?.let { alertStyle() }
                }){
                    customComponent?.let { renderCustomComponent(this) }

                    if (customComponent == null) {
                        (::div.styled(prefix = "toast-alert") {
                            alertStyle()
                        }){
                            renderTitleAndDescription(this)
                            closeButton?.invoke(this, clickStore.delete)
                        }
                    }
                }
            }
        }

        val listContext = ToastListElement(toast, listId, position, duration)
        toastMap[listId] = listContext
        ToastStore.add(listContext)

        job = GlobalScope.launch {
            delay(duration)
            ToastStore.remove(listContext)
        }

    }
}

/**
 * This factory method creates a toast and displays it _right away_.
 * Use [showToastDelayed] in order to display a toast delayed, e.g. when a button is pressed.
 *
 * A toast usually consists of a title and a description. At least one of these must be given or a custom component
 * must be included instead as shown below:
 *
 * ```
 * showToast {
 *     customComponent {
 *         p { +"my custom toast message"}
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
    prefix: String = defaultToastContainerId,
    build: ToastComponent.() -> Unit,
) {
    val component = ToastComponent(this).apply(build)
    component.show(styling, baseClass, id, prefix)
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
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself.
 */
fun RenderContext.showToastDelayed(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = defaultToastContainerId,
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