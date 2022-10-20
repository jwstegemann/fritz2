package dev.fritz2.headlessdemo

import dev.fritz2.core.RenderContext
import dev.fritz2.headless.components.toasts

private enum class ToastLocation {
    TopLeft, TopCenter, TopRight, BottomLeft, BottomCenter, BottomRight
}

fun RenderContext.toastDemo() {
    var toastCount = 0

    toasts {
        div(
            """absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-2/3
                | flex flex-col gap-6
                | bg-white rounded p-4
            """.trimMargin()
        ) {
            p {
                +"Press any of the buttons below to spawn a respective toast:"
            }
            div("grid grid-cols-1 md:grid-cols-3 gap-4") {
                ToastLocation.values().forEach { location ->
                    button(
                        """flex justify-center items-center px-4 py-2.5
                        | rounded shadow-sm
                        | border border-transparent
                        | text-sm font-sans text-white
                        | bg-primary-400 hover:bg-primary-900
                        | focus:outline-none focus:ring-4 focus:ring-primary-600""".trimMargin()
                    ) {
                        +location.name

                        clicks handledBy {
                            toast(
                                """flex flex-row flex-shrink-0 gap-2 justify-center
                                | w-max px-4 py-2.5
                                | rounded shadow-sm
                                | border border-transparent
                                | text-sm font-sans
                                | bg-primary-200
                            """.trimIndent(),
                                location = location,
                                duration = 5000L,
                            ) {
                                +"Toast #${toastCount++}"

                                toastCloseButton { close ->
                                    icon(
                                        classes = "w-4 h-4 text-primary-900",
                                        content = HeroIcons.x
                                    )
                                    clicks handledBy close
                                }
                            }
                        }
                    }
                }
            }

            div("relative w-full h-72 flex justify-center items-center border-4 border-dashed rounded-lg") {
                div("text-gray-300") {
                    +"Viewport"
                }

                toastLocation(
                    "absolute top-5 left-5 flex flex-col gap-2 items-start",
                    location = ToastLocation.TopLeft
                )

                toastLocation(
                    "absolute top-5 left-1/2 -translate-x-1/2 flex flex-col gap-2 items-center",
                    location = ToastLocation.TopCenter
                )

                toastLocation(
                    "absolute top-5 right-5 flex flex-col gap-2 items-end",
                    location = ToastLocation.TopRight
                )

                toastLocation(
                    "absolute bottom-5 left-5 flex flex-col-reverse gap-2 items-start",
                    location = ToastLocation.BottomLeft
                )

                toastLocation(
                    "absolute bottom-5 left-1/2 -translate-x-1/2 flex flex-col-reverse gap-2 items-center",
                    location = ToastLocation.BottomCenter
                )

                toastLocation(
                    "absolute bottom-5 right-5 flex flex-col-reverse gap-2 items-end",
                    location = ToastLocation.BottomRight
                )
            }
        }
    }
}