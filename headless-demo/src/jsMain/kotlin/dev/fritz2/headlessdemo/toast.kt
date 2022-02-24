package dev.fritz2.headlessdemo

import dev.fritz2.core.*
import dev.fritz2.headless.components.ToastPosition
import dev.fritz2.headless.components.handleAsToast
import dev.fritz2.headless.components.toasts
import kotlinx.browser.document
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import org.w3c.dom.Node


private class GlobalRenderContext : RenderContext {
    override val job: Job = Job()
    override val scope: Scope = Scope()

    override fun <N : Node, W : WithDomNode<N>> register(element: W, content: (W) -> Unit): W {
        document.body?.let {
            content(element)
            it.appendChild(element.domNode)
        }
        return element
    }
}


object Toasts {
    private const val containerBaseClasses = "fixed flex flex-col pointer-events-none z-50"

    fun render() {
        GlobalRenderContext().run {
            toasts(RenderContext::div) {
                rendering { toastFragment ->
                    toast(
                        classes = "relative m-2 py-2 pl-2 pr-8 bg-white text-gray-600 drop-shadow-lg rounded-lg",
                        tag = RenderContext::li
                    ) {
                        toastFragment.content(this)

                        if (toastFragment.isClosable) {
                            toastCloseButton(
                                toastId = toastFragment.id,
                                classes = "absolute top-2 right-2 pointer-events-auto text-gray-600",
                                tag = RenderContext::button
                            ) {
                                svg("h-4 w-4") {
                                    xmlns("http://www.w3.org/2000/svg")
                                    viewBox("0 0 20 20")
                                    fill("currentColor")
                                    attr("aria-hidden", "true")
                                    path {
                                        attr("fill-rule", "evenodd")
                                        d("M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z")
                                        attr("clip-rule", "evenodd")
                                    }
                                }
                            }
                        }
                    }
                }

                container(
                    ToastPosition.TopLeft,
                    classes(containerBaseClasses, "top-0 left-0 items-start"),
                    tag = RenderContext::ul
                )
                container(
                    ToastPosition.TopRight,
                    classes(containerBaseClasses, "top-0 right-0 items-end"),
                    tag = RenderContext::ul
                )
                container(
                    ToastPosition.TopCenter,
                    classes(containerBaseClasses, "top-0 left-0 right-0 mx-auto items-center"),
                    tag = RenderContext::ul
                )
                container(
                    ToastPosition.BottomLeft,
                    classes(containerBaseClasses, "bottom-0 left-0 items-start"),
                    tag = RenderContext::ul
                )
                container(
                    ToastPosition.BottomRight,
                    classes(containerBaseClasses, "bottom-0 right-0 items-end"),
                    tag = RenderContext::ul
                )
                container(
                    ToastPosition.BottomCenter,
                    classes(containerBaseClasses, "bottom-0 left-0 right-0 mx-auto items-center"),
                    tag = RenderContext::ul
                )
            }
        }
    }
}


fun RenderContext.toastDemo() = div {
    Toasts.render()

    button(
        """w-48 inline-flex justify-center rounded-md border border-transparent 
            | shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 
            | focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:col-start-2 
            | sm:text-sm""".trimMargin()
    ) {
        +"New toast"
        clicks.map { } handledBy handleAsToast {
            position = ToastPosition.TopRight
            isClosable = true
            content = {
                div("text-sm w-48") {
                    div("font-semibold") {
                        +"Hello World!"
                    }
                    div {
                        +"This is a toast."
                    }
                }
            }
        }
    }
 }
