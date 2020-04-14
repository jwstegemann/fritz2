package io.fritz2.examples.routing

import io.fritz2.binding.const
import io.fritz2.dom.*
import io.fritz2.dom.html.html
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

    html {
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

                div("collapse navbar-collapse", id="navbarContent") {
                    ul("navbar-nav mr-auto") {
                        li("btn nav-item") {
                            a("nav-link") {
                                text(Pages.home)

                                router.navTo <= clicks.map { mapOf("page" to Pages.home) }
                            }
                        }
                        li("btn nav-item") {
                            a("nav-link") {
                                text(Pages.show)

                                router.navTo <= clicks.map { mapOf("page" to Pages.show, "extra" to "extra text") }
                            }
                        }
                        li("btn nav-item") {
                            a("nav-link") {
                                text(Pages.change)

                                router.navTo <= clicks.map { mapOf("page" to Pages.change, "debug" to false, "role" to Roles.anonymous) }
                            }
                        }
                    }
                }
            }
            div("card") {
                router.select("page") { (page, params) ->
                    when (page) {
                        Pages.home -> html {
                            div("card-body") {
                                h5("card-title") {
                                  text(Pages.home)
                                }
                                p("card-text") {
                                    text("""
                                        |Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
                                        |sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat,
                                        |sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. 
                                        |Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.""".trimMargin())
                                }
                            }
                        }
                        Pages.show -> html {
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
                        Pages.change -> html {
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

                                            router.navTo <= changes.states().map { checked ->
                                                params.plus("debug" to checked)
                                            }
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

                                        router.navTo <= changes.selectedText().map { text ->
                                            params.plus("role" to text)
                                        }
                                    }
                                }
                            }
                        }
                        else -> html {
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