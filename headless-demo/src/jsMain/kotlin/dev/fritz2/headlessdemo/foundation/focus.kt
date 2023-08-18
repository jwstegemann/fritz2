package dev.fritz2.headlessdemo.foundation

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.setInitialFocus
import dev.fritz2.headless.foundation.trapFocusInMountpoint
import dev.fritz2.headless.foundation.trapFocusWhenever
import dev.fritz2.headlessdemo.example
import dev.fritz2.headlessdemo.result
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement

fun RenderContext.btn(id: String? = null, init: Tag<HTMLButtonElement>.() -> Unit) = button(
    """inline-flex justify-center w-72 px-4 py-2 sm:col-start-2
    | rounded shadow-sm bg-primary-800 disabled:bg-primary-600
    | border border-transparent
    | text-sm text-white
    | hover:bg-primary-900
    | focus:outline-none focus:ring-4 focus:ring-primary-600
    """.trimMargin(),
    id = id ?: Id.next(),
) {
    init()
}

fun RenderContext.key(text: String) = code("px-1 py-0.5 rounded-md bg-gray-300") {
    +text
}

fun RenderContext.chooseInitialFocus(storedInitialFocus: Store<Int>, testId: String) {
    h2 { +"Select Item that should get the initial Focus:" }
    storedInitialFocus.data.render { selected ->
        (1..3).forEach { index ->
            label(id = "$testId-radio-$index") {
                +"Tab-Item-$index"
                input("ml-1") {
                    type("radio")
                    name("$testId-initialFocus")
                    if (selected == index) checked(true)
                    changes.map { index } handledBy storedInitialFocus.update
                }
            }
        }
    }
}

fun RenderContext.testTrapFocus() {
    h1("mb-8 tracking-tight font-bold text-gray-900 text-4xl") {
        span("block sm:inline") { +"Focus" }
        span("block text-primary-800 sm:inline") { +" Test-Drive" }
    }

    example("Focus-Trap inside Mount-Point") {
        val testId = "trapMountPoint"
        val toggle = storeOf(false)
        val storedInitialFocus = storeOf(1)

        div("flex flex-col gap-2") {
            chooseInitialFocus(storedInitialFocus, testId)
            btn("$testId-open") {
                +"Open Focus-Trap"
                clicks.map { true } handledBy toggle.update
            }
        }

        toggle.data.combine(storedInitialFocus.data, ::Pair).render { (opened, initialFocus) ->
            if (opened) {
                div(
                    """z-10 max-w-sm lg:max-w-3xl my-4 p-7 flex flex-col gap-4
                    | bg-white overflow-hidden rounded-lg shadow-lg ring-1 ring-black ring-opacity-5
                    | focus:outline-none
                    """.trimMargin(),
                ) {
                    transition(
                        "transition ease-out duration-200",
                        "opacity-0 translate-y-1",
                        "opacity-100 translate-y-0",
                        "transition ease-in duration-150",
                        "opacity-100 translate-y-0",
                        "opacity-0 translate-y-1",
                    )
                    trapFocusInMountpoint()
                    h1("text-left text-gray-800 font-medium text-lg") {
                        +"Mount-Point Focus-Trap Container"
                    }
                    (1..3).forEach {
                        div(
                            """flex items-center p-2 m-1 
                            | transition duration-150 ease-in-out rounded-lg 
                            | hover:bg-primary-200 
                            | focus:outline-none focus:ring-4 focus:ring-primary-600
                            """.trimMargin(),
                            id = "$testId-Tab-Item-$it",
                        ) {
                            attr("tabindex", "0")
                            +"Tab-Item-$it"
                            if (initialFocus == it) setInitialFocus()
                        }
                    }
                    toggle.data.render {
                        if (it) {
                            btn("$testId-close") {
                                +"Close Focus-Trap"
                                clicks.map { false } handledBy toggle.update
                            }
                        }
                    }
                }
            }
        }

        result {
            span {
                +"""The focus-traps must return the focus to the initial focused element, after closing the 
                    |reactively rendered container.
                """.trimMargin()
            }
            ul("list-disc list-inside") {
                li {
                    +"Choose one item to be preselected."
                }
                li {
                    +"Click button "
                    key("Open Focus-Trap")
                }
                li {
                    +"The previously selected item must have the focus now."
                }
                li {
                    +"Use TAB and SHIFT+TAB to navigate between the buttons - you must stay inside the container"
                }
                li {
                    +"Noc close the trap container by clicking on the "
                    key("Close Focus-Trap")
                    +" button."
                }
                li {
                    +"The initially clicked button "
                    key("Open Focus-Trap")
                    +" must have got back the focus."
                }
            }
        }
    }

    example("Focus-Trap activated on some conditional flow") {
        val testId = "trapWhenever"
        val toggle = storeOf(false)
        val storedInitialFocus = storeOf(1)

        div("flex flex-col gap-2") {
            chooseInitialFocus(storedInitialFocus, testId)
            btn("$testId-open") {
                +"Activate Focus-Trap"
                clicks.map { true } handledBy toggle.update
            }
        }

        div(
            """z-10 max-w-sm lg:max-w-3xl my-4 p-7 flex flex-col gap-4
                    | overflow-hidden rounded-lg shadow-lg ring-1 ring-black ring-opacity-5
                    | focus:outline-none
            """.trimMargin(),
        ) {
            className(
                toggle.data.map {
                    if (it) "bg-white" else ""
                },
            )
            transition(
                "transition ease-out duration-200",
                "opacity-0 translate-y-1",
                "opacity-100 translate-y-0",
                "transition ease-in duration-150",
                "opacity-100 translate-y-0",
                "opacity-0 translate-y-1",
            )
            trapFocusWhenever(toggle.data)
            h1("text-left text-gray-800 font-medium text-lg") {
                toggle.data.map { if (it) "Focus-Trap activated" else "Currently no Focus-Trap" }
                    .renderText(into = this)
            }
            storedInitialFocus.data.render { initialFocus ->
                (1..3).forEach { index ->
                    div(
                        """flex items-center p-2 m-1 
                            | transition duration-150 ease-in-out rounded-lg 
                            | hover:bg-primary-200 
                            | focus:outline-none focus:ring-4 focus:ring-primary-600
                        """.trimMargin(),
                        id = "$testId-Tab-Item-$index",
                    ) {
                        attr("tabindex", "0")
                        +"Tab-Item-$index"
                        if (initialFocus == index) setInitialFocus()
                    }
                }
            }
            toggle.data.render {
                if (it) {
                    btn("$testId-close") {
                        +"Close Focus-Trap"
                        clicks.map { false } handledBy toggle.update
                    }
                }
            }
        }

        result {
            span {
                +"""The focus-traps must return the focus to the initial focused element, deactivating the trap by  
                    |changing the 
                """.trimMargin()
                code { +"Flow<Boolean>" }
                +" to "
                code { +"false" }
                +"."
            }
            ul("list-disc list-inside") {
                li {
                    +"Choose one item to be preselected."
                }
                li {
                    +"Click button "
                    key("Activate Focus-Trap")
                }
                li {
                    +"The previously selected item must have the focus now."
                }
                li {
                    +"Use TAB and SHIFT+TAB to navigate between the buttons - you must stay inside the container"
                }
                li {
                    +"Noc close the trap container by clicking on the "
                    key("Close Focus-Trap")
                    +" button."
                }
                li {
                    +"The initially clicked button "
                    key("Activate Focus-Trap")
                    +" must have got back the focus."
                }
                li {
                    +"Try to navigate again with TAB. Now you should be able to reach this result box."
                }
            }
        }
    }

    example("Nested Traps - Mountpoint with Conditional inside") {
        val toggle = storeOf(false)
        val disabled = storeOf(false)

        div("flex flex-col gap-2") {
            btn("Open-Below") {
                +"Open Focus-Trap Container Below"
                disabled(disabled.data)
                clicks.map { true } handledBy toggle.update
            }

            toggle.data.render { opened ->
                if (opened) {
                    div(
                        """z-10 max-w-sm lg:max-w-3xl my-4 p-7 flex flex-col gap-4
                | bg-white overflow-hidden rounded-lg shadow-lg ring-1 ring-black ring-opacity-5
                | focus:outline-none
                        """.trimMargin(),
                    ) {
                        transition(
                            "transition ease-out duration-200",
                            "opacity-0 translate-y-1",
                            "opacity-100 translate-y-0",
                            "transition ease-in duration-150",
                            "opacity-100 translate-y-0",
                            "opacity-0 translate-y-1",
                        )
                        trapFocusInMountpoint()
                        disabled.update(true)

                        val innerToggle = storeOf(false)
                        div("flex flex-col gap-2") {
                            h1("text-left text-gray-800 font-medium text-lg") {
                                +"Mount-Point Focus-Trap Container"
                            }

                            btn("InnerToggleActivate") {
                                +"Activate inner focus trap"
                                clicks.map { true } handledBy innerToggle.update
                            }

                            div("flex flex-col gap-2") {
                                className(
                                    innerToggle.data.map {
                                        if (it) "border rounded-md shadow-lg ring-1 ring-black ring-opacity-5 p-2" else ""
                                    },
                                )
                                trapFocusWhenever(innerToggle.data)

                                (1..3).forEach {
                                    div(
                                        """flex items-center p-2 m-1 
                                    | transition duration-150 ease-in-out rounded-lg 
                                    | hover:bg-primary-200 
                                    | focus:outline-none focus:ring-4 focus:ring-primary-600
                                        """.trimMargin(),
                                        id = "Tab-Item-$it",
                                    ) {
                                        attr("tabindex", "0")
                                        +"Tab-Item-$it"
                                    }
                                }

                                innerToggle.data.render {
                                    if (it) {
                                        btn("InnerToggleDeactivate") {
                                            +"Deactivate inner focus trap"
                                            clicks.map { false } handledBy innerToggle.update
                                        }
                                    }
                                }
                            }
                        }

                        btn("CloseOuterTrap") {
                            +"Close Focus-Trap Container"
                            clicks.map { false } handledBy {
                                toggle.update(it)
                                disabled.update(it)
                            }
                        }
                    }
                }
            }

            btn("Open-Above") {
                +"Open Focus-Trap Container Above"
                disabled(disabled.data)
                clicks.map { true } handledBy toggle.update
            }
        }

        result {
            span {
                +"""
                | The focus-traps must return the focus to the initial focused element, after closing / deactivating.
                """.trimMargin()
            }
            ul("list-disc list-inside") {
                li {
                    +"Focus one of the two buttons and click them."
                }
                li {
                    +"The first focus-trap is inside the new rendered container ("
                    code { +"trapFocusInMountpoint" }
                    +")"
                }
                li {
                    +"Use TAB and SHIFT+TAB to navigate between the buttons - you must stay inside the container"
                }
                li {
                    +"Activate the inner focus-trap by clicking the first button inside the trap container. "
                    +"This inner trap is based upon a conditional flow by using the "
                    code { +"trapFocusWhenever" }
                    +" function."
                }
                li {
                    +"The focus must now bet set on the first button below the activation button."
                }
                li {
                    +"Use TAB and SHIFT+TAB to navigate throw the inner trap. You must stay inside the inner trap. "
                    +"The "
                    key("Close Focus-Trap Container")
                    +" button must be unreachable as well as the "
                    key("Activate inner Focus-Trap")
                    +" one."
                }
                li {
                    +"Now close the inner trap by clicking on the "
                    key("Deactivate inner focus trap")
                    +" button."
                }
                li {
                    +"The "
                    key("Activate inner focus trap")
                    +" button must have got the focus back."
                }
                li {
                    +"Noc close the trap container by clicking on the "
                    key("Close focus trap container")
                    +" button."
                }
                li {
                    +"The initially clicked outer button must have got back the focus."
                }
            }
        }
    }
}
