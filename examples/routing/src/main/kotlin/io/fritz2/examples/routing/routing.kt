package io.fritz2.examples.routing

import io.fritz2.binding.*
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map

object Pages {
    const val a = "pageA"
    const val b = "pageB"
    const val c = "pageC"
}

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val router = routing(mapOf("page" to Pages.b, "test" to "=&%#+!§/%\\\$&()"))

    val myComponent = html {
        section {
            ul {
                li {
                    a {
                        +"Show ${Pages.a}"
                        href = !""
                        router.navTo <= clicks.map { mapOf("page" to Pages.a) } //FIXME not working!?
                    }
                }
                li {
                    a {
                        +"Show ${Pages.b}"
                        href = !""
                        router.navTo <= clicks.map { mapOf("page" to Pages.b) } //FIXME not working!?
                    }
                }
                li {
                    a {
                        +"Show ${Pages.c}"
                        href = !"#page=${Pages.c}" //works
                    }
                }
            }
            div {
                router.select("page") {
                    val (page, params) = it
                    when (page) {
                        Pages.a -> html {
                            h1 {
                                +"Showing page A"
                            }
                        }
                        Pages.b -> html {
                            h1 {
                                +"Showing page B"
                            }
                        }
                        Pages.c -> html {
                            h1 {
                                +"Showing page C"
                            }
                        }
                        else -> html {
                            h1 {
                                +"Page $page not found!"
                            }
                        }
                    }
                }.bind()
            }
        }
    }
    myComponent.mount("target")
}