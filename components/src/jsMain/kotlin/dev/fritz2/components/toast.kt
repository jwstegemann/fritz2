package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.Li
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.StyleClass.Companion.plus
import dev.fritz2.styling.params.*
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.Theme
import dev.fritz2.styling.theme.ToastPlacement
import dev.fritz2.styling.theme.ToastStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

typealias ToastRenderContext = RenderContext.() -> Li
typealias AddToast = () -> Unit

data class ToastListElement(val toastRenderContext: ToastRenderContext, val id: String)


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

    object ToastStore : RootStore<List<ToastListElement>>(listOf()) {

        val add = handle<ToastListElement> { allToasts, newToast ->
            allToasts + newToast
        }

        val remove = handle<ToastListElement> { allToasts, newToast ->
            allToasts.filter { it != newToast }

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


    val listStyle: Style<BasicParams> = {
        display { flex }
        radius { "0.375rem" }
        css("transform-origin: 50% 50% 0px;")
        css("flex-direction: column;")
        opacity { "1" }
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

    var customComponent: (RenderContext.() -> Unit)? = null
    fun customComponent(value: RenderContext.() -> Unit) {
        customComponent = value
    }

    var icon: IconDefinition = Theme().icons.circleInformation
    fun icon(value: () -> IconDefinition) {
        icon = value()
    }

    var position: ToastPlacement.() -> Style<BasicParams> = { Theme().toast.placement.bottom }
    fun position(value: ToastPlacement.() -> Style<BasicParams>) {
        position = value
    }

    private val alignment = when (position) {
        Theme().toast.placement.top -> AlignItemsValues.center
        Theme().toast.placement.topLeft -> AlignItemsValues.flexStart
        Theme().toast.placement.topRight -> AlignItemsValues.flexEnd
        Theme().toast.placement.bottom -> AlignItemsValues.center
        Theme().toast.placement.bottomLeft -> AlignItemsValues.flexStart
        Theme().toast.placement.bottomRight -> AlignItemsValues.flexEnd
        else -> AlignItemsValues.center
    }

    var status: (ToastStatus.() -> Style<BasicParams>) = { Theme().toast.status.info }
    fun status(value: ToastStatus.() -> Style<BasicParams>) {
        status = value
    }

    var hasCloseButton: Boolean = true
    fun hasCloseButton(value: Boolean) {
        hasCloseButton = value
    }

    var closeButton: (RenderContext.(SimpleHandler<Unit>) -> Unit)? = null
    fun closeButton(
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
        hasCloseButton = false
        renderContext.apply {
            customComponent!!.invoke(this)
        }
    }


    fun show() {
       
        closeButton()
        val listId = uniqueId()
        val clickStore = object : RootStore<String>("") {
            val delete = handle {
                val currentToastListElement = toastMap[listId]
                toastMap.remove(listId)
                ToastStore.remove(currentToastListElement!!)
                it
            }
        }

        val toast: ToastRenderContext = {

            (::li.styled(id = listId) {
                listStyle()
                alignItems { alignment }
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
        ToastStore.add(listContext)

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

    val store = object : RootStore<AddToast>({}) {
        val add = handle {
            component.show()
            it
        }
    }

    (::ul.styled(styling, baseClass + ToastComponent.staticCss, id, prefix) {
        component.status.let { Theme().toast.status }
        component.position.invoke(Theme().toast.placement)()
    }){
        ToastComponent.ToastStore.data.renderEach { it.toastRenderContext.invoke(this) }

    }
    return store.add

}