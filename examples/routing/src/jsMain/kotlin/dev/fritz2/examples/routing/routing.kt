package dev.fritz2.examples.routing

import dev.fritz2.core.*
import dev.fritz2.routing.routerOf
import kotlinx.browser.window
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

fun main() {

    val router = routerOf(mapOf("page" to Pages.home))

    render("#target") {
        div {
            nav("navbar navbar-expand-lg navbar-light bg-light") {
                a("navbar-brand") {
                    +"Routing"
                    href(with (window.location) { origin + pathname })
                }
                button("navbar-toggler") {
                    attr("data-toggle", "collapse")
                    attr("data-target", "#navbarContent")

                    span("navbar-toggler-icon") {}
                }

                div("collapse navbar-collapse", id = "navbarContent") {
                    ul("navbar-nav mr-auto") {
                        li("btn nav-item") {
                            className(router.data.map {
                                if (it.containsValue(Pages.home)) "active" else ""
                            })

                            a("nav-link") {
                                +Pages.home

                                clicks.map {
                                    mapOf(
                                            "page" to Pages.home
                                    )
                                } handledBy router.navTo
                            }
                        }
                        li("btn nav-item") {
                            className(router.data.map {
                                if (it.containsValue(Pages.show)) "active" else ""
                            })

                            a("nav-link") {
                                +Pages.show

                                clicks.map {
                                    mapOf(
                                            "page" to Pages.show,
                                            "extra" to "extra text"
                                    )
                                } handledBy router.navTo
                            }
                        }
                        li("btn nav-item") {
                            className(router.data.map {
                                if (it.containsValue(Pages.change)) "active" else ""
                            })

                            a("nav-link") {
                                +Pages.change

                                clicks.map {
                                    mapOf(
                                            "page" to Pages.change,
                                            "debug" to false.toString(),
                                            "role" to Roles.anonymous
                                    )
                                } handledBy router.navTo
                            }
                        }
                    }
                }
            }
            div("card") {
                router.select("page").render { (value, route) ->
                    when (value) {
                        Pages.home ->
                            div("card-body") {
                                h5("card-title") {
                                    +Pages.home
                                }
                                p("card-text") {
                                    +"""|Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
                                        |sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat,
                                        |sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. 
                                        |Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.""".trimMargin()
                                }
                            }
                        Pages.show ->
                            div("card-body") {
                                h5("card-title") {
                                    +Pages.show
                                }
                                div("form-group") {
                                    label {
                                        +"Extra parameter"
                                    }
                                    div("form-control") {
                                        +(route["extra"] ?: "")
                                        attr("readonly", "true")
                                    }
                                }
                            }
                        Pages.change ->
                            div("card-body") {
                                h5("card-title") {
                                    +Pages.change
                                }
                                div("form-group") {
                                    label {
                                        +"Debug"
                                    }
                                    div("form-check") {
                                        input("form-check-input", id = "debug") {
                                            type("checkbox")
                                            checked(route["debug"]?.toBoolean() ?: false)

                                            changes.states().map { checked ->
                                                route + ("debug" to checked.toString())
                                            } handledBy router.navTo
                                        }
                                        label("form-check-label") {
                                            `for`("debug")
                                            +"enable debug flag"
                                        }
                                    }
                                }
                                div("form-group") {
                                    label {
                                        +"Role"
                                    }
                                    select("form-control") {
                                        option {
                                            +Roles.anonymous
                                            selected(route["role"] == Roles.anonymous)
                                        }
                                        option {
                                            +Roles.user
                                            selected(route["role"] == Roles.user)
                                        }
                                        option {
                                            +Roles.admin
                                            selected(route["role"] == Roles.admin)
                                        }

                                        changes.selectedText().map { text ->
                                            route + ("role" to text)
                                        } handledBy router.navTo
                                    }
                                }
                            }
                        else ->
                            div("card-body") {
                                h5("card-title") {
                                    +"Page not found"
                                }
                                p("card-text") {
                                    +"""¯\_(ツ)_/¯"""
                                }
                            }
                    }
                }
            }
        }
    }
}