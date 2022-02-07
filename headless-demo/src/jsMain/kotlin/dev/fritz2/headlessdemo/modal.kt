package dev.fritz2.headlessdemo

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.*
import dev.fritz2.headless.components.modal

fun RenderContext.modalDemo() {

    val toggle = storeOf(false)

    button(
        """w-48 inline-flex justify-center rounded-md border border-transparent 
            | shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 
            | focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:col-start-2 
            | sm:text-sm""".trimMargin()
    ) {
        +"Open"
        clicks.map { !toggle.current } handledBy toggle.update
    }
    modal {
        openClose(toggle)
        modalPanel("w-1/3 fixed z-10 inset-0 overflow-y-auto") {
            div("flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0") {
                modalOverlay("fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity") {
                    transition(
                        "ease-out duration-300",
                        "opacity-0",
                        "opacity-100",
                        "ease-in duration-200",
                        "opacity-100",
                        "opacity-0"
                    )
                }
                /* <!-- This element is to trick the browser into centering the modal contents. --> */
                span("hidden sm:inline-block sm:align-middle sm:h-screen") {
                    attr("aria-hidden", "true")
                    +" "
                }
                div(
                    """inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden 
                        | shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg 
                        | sm:w-full sm:p-6""".trimMargin()
                ) {
                    transition(
                        "ease-out duration-300",
                        "opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95",
                        "opacity-100 translate-y-0 sm:scale-100",
                        "ease-in duration-200",
                        "opacity-100 translate-y-0 sm:scale-100",
                        "opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
                    )
                    div("sm:flex sm:items-start") {
                        div(
                            """mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full 
                                | bg-green-100 sm:mx-0 sm:h-10 sm:w-10""".trimMargin()
                        ) {
                            svg("h-6 w-6 text-green-600") {
                                xmlns("http://www.w3.org/2000/svg")
                                fill("none")
                                viewBox("0 0 24 24")
                                attr("stroke", "currentColor")
                                attr("aria-hidden", "true")
                                path {
                                    attr("stroke-linecap", "round")
                                    attr("stroke-linejoin", "round")
                                    attr("stroke-width", "2")
                                    d("M5 13l4 4L19 7")
                                }
                            }
                        }
                        div("mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left") {
                            modalTitle("text-lg leading-6 font-medium text-gray-900", tag = RenderContext::h3) {
                                +"Payment successful"
                            }
                            div("mt-2") {
                                modalDescription("text-sm text-gray-500") {
                                    +"""Lorem ipsum, dolor sit amet consectetur adipisicing elit. Eius aliquam
                                        | laudantium explicabo pariatur iste dolorem animi vitae error totam.
                                        |  At sapiente aliquam accusamus facere veritatis.""".trimMargin()
                                }
                            }
                        }
                    }
                    div("mt-5 sm:mt-4 sm:flex sm:flex-row-reverse") {
                        button(
                            """w-full inline-flex justify-center rounded-md border border-transparent shadow-sm 
                                | px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 
                                | focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 
                                | sm:ml-3 sm:w-auto sm:text-sm""".trimMargin()
                        ) {
                            type("button")
                            +"Back to Dashboard"
                            clicks handledBy close
                        }
                        button(
                            """mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm 
                                | px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 
                                | focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 
                                | sm:mt-0 sm:w-auto sm:text-sm""".trimMargin()
                        ) {
                            type("button")
                            +"Cancel"
                            clicks handledBy close
                        }
                    }
                }
            }
        }
    }
}
