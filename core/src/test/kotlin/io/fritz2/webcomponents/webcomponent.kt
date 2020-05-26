package io.fritz2.webcomponents

import io.fritz2.dom.Tag
import io.fritz2.dom.html.render
import io.fritz2.test.initDocument
import io.fritz2.test.runTest
import io.fritz2.test.targetId
import kotlinx.coroutines.delay
import org.w3c.dom.*
import kotlin.browser.document
import kotlin.test.Test
import kotlin.test.assertEquals

class WebComponentTests {

    class MyComponent : WebComponent<HTMLParagraphElement>() {
        override fun init(element: HTMLElement, shadowRoot: ShadowRoot): Tag<HTMLParagraphElement> =
            render {
                p(id = "paragraph-in-web-component") {
                    text("I am a WebComponent")
                }
            }
    }

    @Test
    fun testWebComponent() = runTest {
        initDocument()

        registerWebComponent("my-component", MyComponent::class)

        delay(250)

        val body = document.getElementById(targetId).unsafeCast<HTMLBodyElement>()

        body.appendChild(document.createElement("my-component", ElementCreationOptions("my-component")))

        delay(250)

        val content = document.getElementsByTagName("my-component")[0]?.let {
            it.shadowRoot?.getElementById("paragraph-in-web-component")
        }

        assertEquals("I am a WebComponent", content?.textContent?.trim())
    }

}