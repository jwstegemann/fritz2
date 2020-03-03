package io.fritz2.routing

import io.fritz2.binding.each
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.test.initDocument
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.js.Promise
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@FlowPreview
class RoutingTests {

    @BeforeTest
    fun setUp() {
        initDocument()
    }

    @Test
    fun testStringRouter(): Promise<Boolean> {

        val startPage = "start"

        val router = router(startPage)
        val testRange = 0..4
        val testId = "testId"
        val buttons = testRange.map { "btn$it" to "page$it" }

        html {
            div(testId) {
                +router.routes
                ul {
                    flowOf(buttons).each().map { (id, page) ->
                        html {
                            li {
                                button(id) {
                                    router.navTo <= clicks.map { page }
                                }
                            }
                        }
                    }.bind()
                }
            }
        }.mount("target")

        return GlobalScope.promise {
            delay(100)

            val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()
            assertEquals(startPage, element.textContent)

            for ((id, page) in buttons) {
                document.getElementById(id).unsafeCast<HTMLButtonElement>().click()
                delay(100)
                assertEquals(page, element.textContent)
            }

            true
        }

    }
}