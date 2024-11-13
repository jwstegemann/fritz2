package dev.fritz2.headless.foundation

import dev.fritz2.core.Tag
import dev.fritz2.core.render
import dev.fritz2.headless.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLDivElement
import kotlin.test.Test
import kotlin.test.assertEquals

class AriaReferenceHookTests {

    @Test
    fun invokeAriaReferenceHookWithStaticIdWillAsEffectSetDefinedAttributeAndId() = runTest {
        val sut = AriaReferenceHook<Tag<HTMLDivElement>>(Aria.labelledby)

        sut("myId")

        render {
            div {
                hook(sut)
            }
        }
        delay(100)

        assertEquals("myId", document.querySelector("[${Aria.labelledby}]")?.getAttribute(Aria.labelledby))
    }

    @Test
    fun invokeAriaReferenceHookWithoutIdWillAsEffectSetDefinedAttributeAndGeneratedId() = runTest {
        val sut = AriaReferenceHook<Tag<HTMLDivElement>>(Aria.labelledby)

        val expected = sut()

        render {
            div {
                hook(sut)
            }
        }
        delay(100)

        assertEquals(expected, document.querySelector("[${Aria.labelledby}]")?.getAttribute(Aria.labelledby))
    }
}