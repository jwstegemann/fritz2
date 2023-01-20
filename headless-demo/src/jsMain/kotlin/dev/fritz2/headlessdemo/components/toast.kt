package dev.fritz2.headlessdemo.components

import dev.fritz2.core.RenderContext
import dev.fritz2.core.Tag
import dev.fritz2.headless.components.toast
import dev.fritz2.headless.components.toastContainer
import org.w3c.dom.HTMLLIElement

private const val toastContainerDefault = "toast-container-default"
private const val containerImportant = "toast-container-important"

fun RenderContext.toastDemo() {
    div {
        toastContainer(
            toastContainerDefault,
            "absolute top-5 right-5 z-10 flex flex-col gap-2 items-start",
            id = toastContainerDefault
        )

        toastContainer(
            containerImportant,
            "absolute top-5 left-1/2 -translate-x-1/2 z-10 flex flex-col gap-2 items-center",
            id = containerImportant
        )
    }

    div(
        """absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2
            | flex flex-col gap-6
            | bg-white rounded p-4
        """.trimMargin()
    ) {
        p {
            +"Press a button below to create a new toast:"
        }
        div("grid grid-cols-1 md:grid-cols-2 gap-4 max-w-1/2 max-h-1/2") {
            button(
                """flex justify-center items-center px-4 py-2.5
                    | rounded shadow-sm
                    | border border-transparent
                    | text-sm font-sans text-white
                    | bg-primary-400 hover:bg-primary-900
                    | focus:outline-none focus:ring-4 focus:ring-primary-600""".trimMargin(),
                id = "btn-toast-default"
            ) {
                +"Default"

                clicks handledBy {
                    showToast(toastContainerDefault) {
                        +"Regular toast"
                        className("bg-primary-200")
                    }
                }
            }

            button(
                """flex justify-center items-center px-4 py-2.5
                    | rounded shadow-sm
                    | border border-transparent
                    | text-sm font-sans text-white
                    | bg-primary-400 hover:bg-primary-900
                    | focus:outline-none focus:ring-4 focus:ring-primary-600""".trimMargin(),
                id = "btn-toast-important"
            ) {
                +"Important"

                clicks handledBy {
                    showToast(containerImportant) {
                        className("bg-error-100")

                        div("flex flex-row items-center gap-4 text-error-500 mr-2") {
                            icon("w-6 h-6", content = HeroIcons.exclamation_circle)
                            div {
                                div("font-semibold") { +"Important" }
                                div { +"This toast is rendered in another container" }
                            }
                        }
                    }
                }
            }
        }
    }
}


private var toastCount = 0
private fun nextToastId() = "toast-${toastCount++}"

private fun showToast(container: String, initialize: Tag<HTMLLIElement>.() -> Unit) {
    toast(
        container,
        duration = 6000L,
        """flex flex-row flex-shrink-0 gap-2 justify-center
            | w-max px-4 py-2.5
            | rounded shadow-sm
            | border border-transparent
            | text-sm font-sans
        """.trimMargin(),
        nextToastId()
    ) {
        initialize()

        // FIXME: Werden Toast schnell hintereinander geöffnet, werden einige von ihnen bim
        //  Schließen zwar aus dem ToastStore entfernt, bleiben jedoch als DOM-Leiche übrig.
        //  Dies könnte an der Kombination aus renderEach und transition liegen; evtl. kann
        //  ein Element nicht aus dem DOM entfernt werden, solange es animiert wird?
        //  Vgl.: https://github.com/jwstegemann/fritz2/issues/714
        /*transition(
            enter = "transition-all duration-200 ease-in-out",
            enterStart = "opacity-0",
            enterEnd = "opacity-100",
            leave = "transition-all duration-200 ease-in-out",
            leaveStart = "opacity-100",
            leaveEnd = "opacity-0"
        )*/

        button {
            icon(
                classes = "w-4 h-4 text-primary-900",
                content = HeroIcons.x
            )
            clicks handledBy close
        }
    }
}