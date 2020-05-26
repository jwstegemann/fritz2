package io.fritz2.examples.routing

import io.fritz2.binding.const
import io.fritz2.binding.handledBy
import io.fritz2.dom.html.render
import io.fritz2.dom.mount
import io.fritz2.dom.selectedText
import io.fritz2.dom.states
import io.fritz2.routing.router
import io.fritz2.routing.select
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map

object Pages {
    const val home = "Home"
    const val show = "Show"
    const val change = "Change"
}

object Roles {
    const val anonymous = "anonymous"
    const val user = "user"
    const val admin = "admin"
}

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val router = router(mapOf("page" to Pages.home))

    render {
        div {
            nav("navbar navbar-expand-lg navbar-light bg-light") {
                a("navbar-brand") {
                    text("Routing")
                    href = const("/")
                }
                button("navbar-toggler") {
                    attr("data-toggle", "collapse")
                    attr("data-target", "#navbarContent")

                    span("navbar-toggler-icon") {}
                }

                div("collapse navbar-collapse", id = "navbarContent") {
                    ul("navbar-nav mr-auto") {
                        li("btn nav-item") {
                            a("nav-link") {
                                text(Pages.home)

                                clicks.map { mapOf("page" to Pages.home) } handledBy router.navTo
                            }
                        }
                        li("btn nav-item") {
                            a("nav-link") {
                                text(Pages.show)

                                clicks.map {
                                    mapOf(
                                        "page" to Pages.show,
                                        "extra" to "extra text"
                                    )
                                } handledBy router.navTo
                            }
                        }
                        li("btn nav-item") {
                            a("nav-link") {
                                text(Pages.change)

                                clicks.map {
                                    mapOf(
                                        "page" to Pages.change,
                                        "debug" to false,
                                        "role" to Roles.anonymous
                                    )
                                } handledBy router.navTo
                            }
                        }
                    }
                }
            }
            div("card") {
                router.select("page") { (page, params) ->
                    when (page) {
                        Pages.home -> render {
                            div("card-body") {
                                h5("card-title") {
                                    text(Pages.home)
                                }
                                p("card-text") {
                                    text(
                                        """
                                        |Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
                                        |sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat,
                                        |sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. 
                                        |Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.""".trimMargin()
                                    )
                                }
                            }
                        }
                        Pages.show -> render {
                            div("card-body") {
                                h5("card-title") {
                                    text(Pages.show)
                                }
                                div("form-group") {
                                    label {
                                        text("Extra parameter")
                                    }
                                    div("form-control") {
                                        text(params["extra"].toString())
                                        attr("readonly", "true")
                                    }
                                }
                            }
                        }
                        Pages.change -> render {
                            div("card-body") {
                                h5("card-title") {
                                    text(Pages.change)
                                }
                                div("form-group") {
                                    label {
                                        text("Debug")
                                    }
                                    div("form-check") {
                                        input("form-check-input", id = "debug") {
                                            type = const("checkbox")
                                            checked = const(params["debug"].unsafeCast<Boolean>())

                                            changes.states().map { checked ->
                                                params.plus("debug" to checked)
                                            } handledBy router.navTo
                                        }
                                        label("form-check-label", `for` = "debug") {
                                            text("enable debug flag")
                                        }
                                    }
                                }
                                div("form-group") {
                                    label {
                                        text("Role")
                                    }
                                    select("form-control") {
                                        option {
                                            text(Roles.anonymous)
                                            selected = const(params["role"] == Roles.anonymous)
                                        }
                                        option {
                                            text(Roles.user)
                                            selected = const(params["role"] == Roles.user)
                                        }
                                        option {
                                            text(Roles.admin)
                                            selected = const(params["role"] == Roles.admin)
                                        }

                                        changes.selectedText().map { text ->
                                            params.plus("role" to text)
                                        } handledBy router.navTo
                                    }
                                }
                            }
                        }
                        else -> render {
                            div("card-body") {
                                h5("card-title") {
                                    text("Page not found")
                                }
                                p("card-text") {
                                    text("""¯\_(ツ)_/¯""")
                                }
                            }
                        }
                    }
                }.bind()
            }
        }
    }.mount("target")
}