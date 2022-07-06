package dev.fritz2.headlessdemo

import dev.fritz2.core.RenderContext
import dev.fritz2.headless.components.ToastPosition
import dev.fritz2.headless.components.toast
import dev.fritz2.headless.components.toasts

private val greetings = listOf(
    "Hello world!",
    "Hallo Welt!",
    "Saluton mondo!",
    "Bonjour le monde!",
    "Salve mundus!",
    "¡Hola Mundo!"
)

fun RenderContext.toastDemo() {
    div {
        toasts(
            "absolute top-5 left-5 flex flex-col gap-2 items-start",
            position = ToastPosition.TopLeft
        )

        toasts(
            "absolute top-5 left-1/2 -translate-x-1/2 flex flex-col gap-2 items-center",
            position = ToastPosition.TopCenter
        )

        toasts(
            "absolute top-5 right-5 flex flex-col gap-2 items-end",
            position = ToastPosition.TopRight
        )

        toasts(
            "absolute bottom-5 left-5 flex flex-col gap-2 items-start",
            position = ToastPosition.BottomLeft
        )

        toasts(
            "absolute bottom-5 left-1/2 -translate-x-1/2 flex flex-col gap-2 items-center",
            position = ToastPosition.BottomCenter
        )

        toasts(
            "absolute bottom-5 right-5 flex flex-col gap-2 items-end",
            position = ToastPosition.BottomRight
        )
    }

    div(
        """absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2
            | flex flex-col gap-6
            | bg-white rounded p-4
        """.trimMargin()
    ) {
        p {
            +"Press any of the buttons below to spawn a respective toast:"
        }
        div("grid grid-cols-1 md:grid-cols-3 gap-4 max-w-1/2 max-h-1/2") {
            ToastPosition.values().forEach { position ->
                button(
                    """flex justify-center items-center px-4 py-2.5
                        | rounded shadow-sm
                        | border border-transparent
                        | text-sm font-sans text-white
                        | bg-primary-400 hover:bg-primary-900
                        | focus:outline-none focus:ring-4 focus:ring-primary-600""".trimMargin()
                ) {
                    +position.name

                    clicks handledBy {
                        toast(
                            """flex flex-row flex-shrink-0 gap-2 justify-center
                                | w-max px-4 py-2.5
                                | rounded shadow-sm
                                | border border-transparent
                                | text-sm font-sans
                                | bg-primary-200
                            """.trimIndent(),
                            duration = 5000L,
                            position = position
                        ) {
                            +greetings.random()

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
    }
}