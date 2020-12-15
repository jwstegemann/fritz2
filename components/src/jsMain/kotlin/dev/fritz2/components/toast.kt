package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
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
import dev.fritz2.styling.StyleClass.Companion.plus
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.rgb
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.Theme
import dev.fritz2.styling.theme.ToastStatus
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

}

/**
 *  This class combines the _configuration_ and the core styling of a toast
 *
 *  You can configure the following aspects:
 *  - position of the toast: top | top-left | top-right | bottom | bottom-left | bottom-right
 *  - title: title of the toast
 *  - description: description of the toast
 *  - status:  success | error | warning | info(default)
 *  - duration: time in ms before dismiss the toast
 *  - hasCloseButton : if true, a button is added to the toast
 *  - icon : icon of the toast
 *  - customComponent : custom component to render instead of title and description
 *
 */

@ComponentMarker
class ToastComponent {


    object ToastStore : RootStore<List<ToastListElement>>(listOf(), id = "Toaststore") {

        val add = handle<ToastListElement> { allToasts, newToast ->
            allToasts + newToast
        }
        val remove = handle<ToastListElement> { allToasts, currentToast ->
            allToasts - currentToast
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

    var standAlone: Boolean = false
    fun standAlone(value: Boolean) {
        standAlone = value
    }

    fun standAlone(value: () -> Boolean) {
        standAlone = value()
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
    fun icon(value: () -> IconDefinition) {
        icon = value()
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

    private fun renderTitleAndDescription(renderContext: Div) {
        if (isCloseable) {
            closeButton()
        }

        if (title == null && description == null) {
            renderContext.apply {
                p { +"You have to define a title or a description or you just define a custom component !!!" }
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
            customComponent!!.invoke(this)
        }
    }


    fun show() {

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

                }){
                    (::div.styled(prefix = "toast-alert") {
                        alertStyle()
                    }){
                        when (customComponent) {
                            null -> renderTitleAndDescription(this)
                            else -> renderCustomComponent(this)
                        }
                        if (customComponent == null) closeButton?.invoke(this, clickStore.delete)
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
 *
 */
fun RenderContext.toast(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "ul-toast-container",
    build: ToastComponent.() -> Unit,
): SimpleHandler<Unit> {


    val component = ToastComponent().apply(build)
    if (component.standAlone) {
        component.show()
    }

    val store = object : RootStore<AddToast>({}) {
        val add = handle {
            component.show()

            it
        }
    }

    div {
        (::ul.styled(styling, baseClass + ToastComponent.staticCss, id, prefix) {
            Theme().toast.placement.top()
        }){

            ToastComponent.ToastStore.data.map { it.filter { it.position == top } }
                .renderEach { it.toastRenderContext.invoke(this) }
        }
        (::ul.styled(styling, baseClass + ToastComponent.staticCss, id, prefix) {
            Theme().toast.placement.topLeft()
        }){

            ToastComponent.ToastStore.data.map { it.filter { it.position == topLeft } }
                .renderEach { it.toastRenderContext.invoke(this) }
        }
        (::ul.styled(styling, baseClass + ToastComponent.staticCss, id, prefix) {
            Theme().toast.placement.topRight()
        }){

            ToastComponent.ToastStore.data.map { it.filter { it.position == topRight } }
                .renderEach { it.toastRenderContext.invoke(this) }
        }

        (::ul.styled(styling, baseClass + ToastComponent.staticCss, id, prefix) {
            Theme().toast.placement.bottom()
        }){

            ToastComponent.ToastStore.data.map { it.filter { it.position == bottom } }
                .renderEach { it.toastRenderContext.invoke(this) }
        }

        (::ul.styled(styling, baseClass + ToastComponent.staticCss, id, prefix) {
            Theme().toast.placement.bottomRight()
        }){

            ToastComponent.ToastStore.data.map { it.filter { it.position == bottomRight } }
                .renderEach { it.toastRenderContext.invoke(this) }
        }

        (::ul.styled(styling, baseClass + ToastComponent.staticCss, id, prefix) {
            Theme().toast.placement.bottomLeft()
        }){

            ToastComponent.ToastStore.data.map { it.filter { it.position == bottomLeft } }
                .renderEach { it.toastRenderContext.invoke(this) }
        }
    }



    return store.add
}