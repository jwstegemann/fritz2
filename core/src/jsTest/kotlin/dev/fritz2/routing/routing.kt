package dev.fritz2.routing

import dev.fritz2.dom.html.render
import dev.fritz2.identification.uniqueId
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals


class RoutingTests {

    @Test
    fun  testStringRouter() = runTest {
        initDocument()

        window.location.hash = ""

        val defaultRoute = ""

        val router = router(defaultRoute)
        val testRange = 0..2
        val testId = uniqueId()
        val buttons = testRange.map { "btn-${uniqueId()}" to "page$it" }

        render {
            div(id = testId) {
                router.data.asText()
                ul {
                    buttons.forEach { (id, page) ->
                        li {
                            button(id = id) {
                                clicks.map { page } handledBy router.navTo
                            }
                        }
                    }
                }
            }
        }

        delay(200)

        val element = document.getElementById(testId).unsafeCast<HTMLDivElement>()
        assertEquals(defaultRoute, element.textContent)
        assertEquals(defaultRoute, router.current)

        for ((id, page) in buttons) {
            document.getElementById(id).unsafeCast<HTMLButtonElement>().click()
            delay(100)
            assertEquals(page, element.textContent)
            assertEquals(page, router.current)
        }
    }

    @Test
    fun testMapRouter() = runTest {
        initDocument()

        window.location.hash = ""

        val pageKey = "page"
        val btnKey = "btn"
        val defaultRoute = mapOf(pageKey to "start", btnKey to "")

        val router = router(defaultRoute)

        val testRange = 0..2
        val pageId = "page-${uniqueId()}"
        val btnId = "btn-${uniqueId()}"
        val buttons = testRange.map { "btn-${uniqueId()}" to "page-$it" }

        render {
            div {
                div(id = pageId) {
                    router.select(pageKey, "").asText()
                }
                div(id = btnId) {
                    router.select(btnKey).map { it.first ?: "" }.asText()
                }
                ul {
                    buttons.forEach { (id, page) ->
                        li {
                            button(id = id) {
                                +page
                                clicks.map { mapOf(pageKey to page, btnKey to id) } handledBy router.navTo
                            }
                        }
                    }
                }
            }
        }

        delay(250)

        val pageElement = document.getElementById(pageId) as HTMLDivElement
        val btnElement = document.getElementById(btnId) as HTMLDivElement

        assertEquals(defaultRoute[pageKey], pageElement.textContent, "initial page does not match")
        assertEquals(defaultRoute[btnKey], btnElement.textContent, "initial btn does not match")

        for ((id, page) in buttons) {
            document.getElementById(id).unsafeCast<HTMLButtonElement>().click()
            delay(250)
            assertEquals(page, pageElement.textContent)
            assertEquals(id, btnElement.textContent)
        }
    }

    @Test
    fun testMapRouterFailing() = runTest {
        initDocument()

        window.location.hash = ""

        val router = router(mapOf("test" to "123"))

        val divId = "div-${uniqueId()}"

        render {
            div(id = divId) {
                router.select("fail", "error").asText()
            }
        }

        delay(250)

        val divElement = document.getElementById(divId) as HTMLDivElement

        assertEquals("error", divElement.textContent, "expected default value not occur")
    }
}