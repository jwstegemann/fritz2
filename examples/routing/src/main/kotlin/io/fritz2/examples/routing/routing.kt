package io.fritz2.examples.routing

import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.routing.router
import io.fritz2.routing.select
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

object Pages {
    const val a = "pageA"
    const val b = "pageB"
    const val c = "pageC"
}

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val router = router(mapOf("page" to Pages.a, "test" to "=&%#+!§/%\\\$&()"))

    val myComponent = html {
        section {
            ul {
                li {
                    button {
                        +"Show ${Pages.a}"
                        router.navTo <= clicks.map { mapOf("page" to Pages.a) }
                    }
                }
                li {
                    button {
                        +"Show ${Pages.b}"
                        router.navTo <= clicks.map { mapOf("page" to Pages.b) }
                    }
                }
                li {
                    button {
                        +"Show ${Pages.c}"
                        router.navTo <= clicks.map { mapOf("page" to Pages.c) }
                    }
                }
            }
            div {
                router.select("page") { (page, _) ->
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