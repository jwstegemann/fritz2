package io.fritz2.routing

import io.fritz2.binding.const
import io.fritz2.binding.each
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.test.initDocument
import io.fritz2.test.randomId
import io.fritz2.test.runTest
import io.fritz2.test.targetId
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLParagraphElement
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals


class RoutingTests {

    @Test
    fun testStringRouter() = runTest {
        initDocument()
        delay(100)

        val defaultRoute = "start"

        val router = router(defaultRoute)
        val testRange = 0..4
        val testId = randomId()
        val buttons = testRange.map { "btn$it" to "page$it" }

        html {
            div(id = testId) {
                router.routes.bind()
                ul {
                    const(buttons).each().map { (id, page) ->
                        html {
                            li {
                                button(id = id) {
                                    router.navTo <= clicks.map { page }
                                }
                            }
                        }
                    }.bind()
                }
            }
        }.mount(targetId)

        delay(500)

        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()
        //console.log("${element.textContent}\n")
        assertEquals(defaultRoute, element.textContent)

        for ((id, page) in buttons) {
            document.getElementById(id).unsafeCast<HTMLButtonElement>().click()
            delay(100)
            assertEquals(page, element.textContent)
        }
    }

    @Test
    fun testMapRouter() = runTest {
        initDocument()

        val pageKey = "page"
        val btnKey = "btn"
        val defaultRoute = mapOf(pageKey to "start", btnKey to "")

        val router = router(defaultRoute)

        val testRange = 0..4
        val pageId = randomId("page")
        val btnId = randomId("btn")
        val buttons = testRange.map { "btn-$it" to "page-$it" }

        html {
            div {
                p(id = pageId) {
                    router.select(pageKey) { it.first }.bind()
                }
                p(id = btnId) {
                    router.select(btnKey) { it.first }.bind()
                }
                ul {
                    const(buttons).each().map { (id, page) ->
                        html {
                            li {
                                button(id = id) {
                                    router.navTo <= clicks.map { mapOf(pageKey to page, btnKey to id) }
                                }
                            }
                        }
                    }.bind()
                }
            }
        }.mount(targetId)

        delay(200)

        val pageElement = document.getElementById(pageId).unsafeCast<HTMLParagraphElement>()
        val btnElement = document.getElementById(btnId).unsafeCast<HTMLParagraphElement>()

        //FIXME: not working
//        assertEquals(defaultRoute[pageKey], pageElement.textContent)
//        assertEquals(defaultRoute[btnKey], btnElement.textContent)

        for ((id, page) in buttons) {
            document.getElementById(id).unsafeCast<HTMLButtonElement>().click()
            delay(100)
            assertEquals(page, pageElement.textContent)
            assertEquals(id, btnElement.textContent)
        }
    }
}