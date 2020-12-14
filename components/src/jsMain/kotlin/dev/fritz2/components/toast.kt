package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.components.Position.BOTTOM
import dev.fritz2.components.Position.BOTTOM_LEFT
import dev.fritz2.components.Position.BOTTOM_RIGHT
import dev.fritz2.components.Position.TOP
import dev.fritz2.components.Position.TOP_LEFT
import dev.fritz2.components.Position.TOP_RIGHT
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

typealias ToastRenderContext = RenderContext.() -> Li
typealias AddToast = () -> Unit

data class ToastListElement(val toastRenderContext: ToastRenderContext, val id: String)

object Position {

    const val BOTTOM = "bottom"
    const val BOTTOM_LEFT = "bottomLeft"
    const val BOTTOM_RIGHT = "bottomRight"
    const val TOP = "top"
    const val TOP_LEFT = "topLeft"
    const val TOP_RIGHT = "topRight"

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

// TODO  would be nice to have one main store with 6 sub-stores

    object BottomLeftStore : RootStore<List<ToastListElement>>(listOf()) {

        val add = handle<ToastListElement> { allToasts, newToast ->

            allToasts + newToast
        }
        val remove = handle<ToastListElement> { allToasts, currentToast ->
            allToasts - currentToast
        }
    }

    object BottomStore : RootStore<List<ToastListElement>>(listOf()) {
        val add = handle<ToastListElement> { allToasts, newToast ->
            allToasts + newToast
        }
        val remove = handle<ToastListElement> { allToasts, currentToast ->
            allToasts - currentToast
        }
    }

    object BottomRightStore : RootStore<List<ToastListElement>>(listOf()) {
        val add = handle<ToastListElement> { allToasts, newToast ->

            allToasts + newToast
        }
        val remove = handle<ToastListElement> { allToasts, currentToast ->
            allToasts - currentToast
        }
    }

    object TopLeftStore : RootStore<List<ToastListElement>>(listOf()) {
        val add = handle<ToastListElement> { allToasts, newToast ->

            allToasts + newToast
        }
        val remove = handle<ToastListElement> { allToasts, currentToast ->
            allToasts - currentToast
        }
    }

    object TopStore : RootStore<List<ToastListElement>>(listOf()) {
        val add = handle<ToastListElement> { allToasts, newToast ->

            allToasts + newToast
        }
        val remove = handle<ToastListElement> { allToasts, currentToast ->
            allToasts - currentToast
        }
    }


    object TopRightStore : RootStore<List<ToastListElement>>(listOf()) {
        val add = handle<ToastListElement> { allToasts, newToast ->
            println("update in TopRightStore")
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
        radius { "0.375rem" }
        css("transform-origin: 50% 50% 0px;")
        css("flex-direction: column;")
        opacity { "1" }
        css("transition: opacity 1s ease-in-out;")
    }

    val toastInner: Style<BasicParams> = {

        maxWidth { "560px" }
        minWidth { "300px" }
        margin { "0.5rem" }
        css("pointer-events: auto;")
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
            right { "2rem" }
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

    var position: String = BOTTOM
    fun position(value: String) {
        position = value
    }

    fun position(value: () -> String) {
        position = value()
    }


    var status: (ToastStatus.() -> Style<BasicParams>) = { Theme().toast.status.info }
    fun status(value: ToastStatus.() -> Style<BasicParams>) {
        status = value
    }

    var duration: Int = 2000
    fun duration(value: () -> Int) {
        duration = value()
    }

    fun duration(value: Int) {
        duration = value
    }


    var closeButton: (RenderContext.(SimpleHandler<Unit>) -> Unit)? = null
    private fun closeButton(
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = "toast-close-button",
        build: PushButtonComponent.() -> Unit = {},
    ) {
        closeButton = { closeHandle ->
            clickButton({
                Theme().toast.closeButton.close
                styling()
            }, baseClass, id, prefix) {
                variant { ghost }
                color { light }
                icon { fromTheme { close } }
                build()
            }.map { Unit } handledBy closeHandle
        }
    }

    private fun renderTitleAndDescription(renderContext: Div) {
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

    private fun handleStoreAdd(element: ToastListElement) {

        when (position) {

            BOTTOM_LEFT -> BottomLeftStore.add(element)
            BOTTOM_RIGHT -> BottomRightStore.add(element)
            BOTTOM -> BottomStore.add(element)
            TOP_LEFT -> TopLeftStore.add(element)
            TOP_RIGHT -> TopRightStore.add(element)
            TOP -> TopStore.add(element)
            else -> TopStore.add(element)

        }

    }

    fun handleStoreRemove(element: ToastListElement) {

        when (position) {
            BOTTOM_LEFT -> BottomLeftStore.remove(element)
            BOTTOM_RIGHT -> BottomRightStore.remove(element)
            BOTTOM -> BottomStore.remove(element)
            TOP_LEFT -> TopLeftStore.remove(element)
            TOP_RIGHT -> TopRightStore.remove(element)
            TOP -> TopStore.remove(element)
            else -> TopStore.remove(element)
        }
    }

    fun show() {


        closeButton()
        val listId = uniqueId()
        val clickStore = object : RootStore<String>("") {
            val delete = handle {
                val currentToastListElement = toastMap[listId]
                document.getElementById(listId)!!.setAttribute("style", "opacity: 0;")
                toastMap.remove(listId)
                delay(1010)
                handleStoreRemove(currentToastListElement!!)
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

                        closeButton?.invoke(this, clickStore.delete)
                    }
                }

            }
        }

        val listContext = ToastListElement(toast, listId)
        toastMap[listId] = listContext
        handleStoreAdd(listContext)

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

    (::ul.styled(styling, baseClass + ToastComponent.staticCss, id, prefix) {
        Theme().toast.placement.top()
    }){

        ToastComponent.TopStore.data.renderEach { it.toastRenderContext.invoke(this) }
    }
    (::ul.styled(styling, baseClass + ToastComponent.staticCss, id, prefix) {
        Theme().toast.placement.topLeft()
    }){

        ToastComponent.TopLeftStore.data.renderEach { it.toastRenderContext.invoke(this) }
    }
    (::ul.styled(styling, baseClass + ToastComponent.staticCss, id, prefix) {
        Theme().toast.placement.topRight()
    }){

        ToastComponent.TopRightStore.data.renderEach { it.toastRenderContext.invoke(this) }
    }

    (::ul.styled(styling, baseClass + ToastComponent.staticCss, id, prefix) {
        Theme().toast.placement.bottom()
    }){

        ToastComponent.BottomStore.data.renderEach { it.toastRenderContext.invoke(this) }
    }

    (::ul.styled(styling, baseClass + ToastComponent.staticCss, id, prefix) {
        Theme().toast.placement.bottomRight()
    }){

        ToastComponent.BottomRightStore.data.renderEach { it.toastRenderContext.invoke(this) }
    }

    (::ul.styled(styling, baseClass + ToastComponent.staticCss, id, prefix) {
        Theme().toast.placement.bottomLeft()
    }){

        ToastComponent.BottomLeftStore.data.renderEach { it.toastRenderContext.invoke(this) }
    }

    return store.add

}