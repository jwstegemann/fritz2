package dev.fritz2.dom.html

import dev.fritz2.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith


class HtmlTests {

//    @Test
//    fun testMultipleRootElementsException() = runTest {
//        initDocument()
//
//        render {
//            flowOf(Unit).catch {
//                assertTrue(it is MultipleRootElementsException)
//            }.renderElement {
//                div { +"div1" }
//                div { +"div2" }
//            }
//        }
//        delay(250)
//    }

    @Test
    fun testMountTargetNotFoundException() = runTest {
        assertFailsWith(MountTargetNotFoundException::class) {
            render("missing") {
                div { +"div" }
            }
        }
    }
}