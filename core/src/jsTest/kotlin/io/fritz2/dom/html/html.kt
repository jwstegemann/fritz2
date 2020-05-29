package io.fritz2.dom.html

import io.fritz2.dom.mount
import io.fritz2.test.initDocument
import io.fritz2.test.runTest
import io.fritz2.test.targetId
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertFailsWith


class HtmlTests {

    @Test
    fun testMultipleRootElementsException() = runTest {
        initDocument()

        assertFailsWith(MultipleRootElementsException::class) {
            render {
                div {
                    text("div1")
                }
                div {
                    text("div2")
                }
            }.mount(targetId)
            delay(250)
        }
    }
}