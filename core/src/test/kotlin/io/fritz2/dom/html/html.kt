package io.fritz2.dom.html

import io.fritz2.dom.mount
import io.fritz2.test.initDocument
import io.fritz2.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
@FlowPreview
class HtmlTests {

    @Test
    fun testMultipleRootElementsException() = runTest {
        initDocument()

        assertFailsWith(MultipleRootElementsException::class) {
            html {
                div {
                    +"div1"
                }
                div {
                    +"div2"
                }
            }.mount("target")
            delay(250)
        }
    }
}