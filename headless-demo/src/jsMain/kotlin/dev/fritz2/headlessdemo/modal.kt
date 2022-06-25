package dev.fritz2.headlessdemo

import dev.fritz2.core.*
import dev.fritz2.headless.components.modal
import kotlinx.coroutines.flow.map

fun RenderContext.modalDemo() {

    val toggle = storeOf(false)

    button(
        """inline-flex justify-center w-32 px-4 py-2 sm:col-start-2
        | rounded shadow-sm bg-primary-800
        | border border-transparent
        | text-sm text-white
        | hover:bg-primary-900
        | focus:outline-none focus:ring-4 focus:ring-primary-600""".trimMargin()
    ) {
        +"Open"
        clicks.map { !toggle.current } handledBy toggle.update
    }
    modal {
        openState(toggle)
        modalPanel("w-full fixed z-10 inset-0 overflow-y-auto") {
            div("flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0") {
                modalOverlay("fixed inset-0 bg-primary-300 bg-opacity-75 transition-opacity") {
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
                    """inline-block align-bottom sm:align-middle w-full sm:max-w-2xl px-6 py-10 sm:p-14 
                    | bg-white rounded-lg
                    | shadow-xl transform transition-all 
                    | text-left overflow-hidden""".trimMargin()
                ) {
                    transition(
                        "ease-out duration-300",
                        "opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95",
                        "opacity-100 translate-y-0 sm:scale-100",
                        "ease-in duration-200",
                        "opacity-100 translate-y-0 sm:scale-100",
                        "opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
                    )
                    div("mt-3 text-center sm:mt-0 sm:text-left") {
                        modalTitle("mb-5 text-gray-800 font-light text-2xl", tag = RenderContext::h1) {
                            +"Payment successful"
                        }
                        div("mt-2") {
                            modalDescription("text-sm text-primary-800") {
                                +"""Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod 
                                    |tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. 
                                    |At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd 
                                    |gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.""".trimMargin()
                            }
                        }
                    }
                    div("flex flex-col items-center sm:flex-row sm:justify-end gap-2 mt-6") {
                        button(
                            """inline-flex justify-center w-full sm:w-32 px-4 py-2 sm:col-start-2
                            | rounded shadow-sm 
                            | border border-transparent
                            | text-sm text-primary-800
                            | hover:bg-primary-400
                            | focus:outline-none focus:ring-4 focus:ring-primary-600""".trimMargin(),
                            id = "button-stay"
                        ) {
                            type("button")
                            +"Stay"
                        }
                        button(
                            """inline-flex justify-center w-full sm:w-32 px-4 py-2 sm:col-start-2
                            | rounded shadow-sm 
                            | border border-transparent
                            | text-sm text-primary-800
                            | hover:bg-primary-400
                            | focus:outline-none focus:ring-4 focus:ring-primary-600""".trimMargin(),
                            id = "button-cancel"
                        ) {
                            type("button")
                            +"Cancel"
                            clicks handledBy close
                        }
                        button(
                            """inline-flex justify-center w-full sm:w-32 px-4 py-2 sm:col-start-2
                            | rounded shadow-sm bg-primary-800
                            | border border-transparent
                            | text-sm text-white
                            | hover:bg-primary-900
                            | focus:outline-none focus:ring-4 focus:ring-primary-600""".trimMargin(),
                            id = "button-close"
                        ) {
                            type("button")
                            +"Back"
                            clicks handledBy close
                        }
                    }
                }
            }
        }
    }
}
