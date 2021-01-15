package dev.fritz2.dom.html

import dev.fritz2.dom.MultipleRootElementsException
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


class HtmlTests {

    @Test
    fun testMultipleRootElementsException() = runTest {
        initDocument()

        render {
            flowOf(Unit).catch {
                assertTrue(it is MultipleRootElementsException)
            }.renderElement {
                div { +"div1" }
                div { +"div2" }
            }
        }
        delay(250)
    }

    @Test
    fun testMountTargetNotFoundException() = runTest {
        assertFailsWith(MountTargetNotFoundException::class) {
            render("missing") {
                div { +"div" }
            }
        }
    }
}