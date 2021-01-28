package dev.fritz2.webcomponents

import dev.fritz2.dom.html.TagContext
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.*
import kotlin.test.Test
import kotlin.test.assertEquals

class WebComponentTests {

    class MyComponent : WebComponent<HTMLParagraphElement>() {
        override fun TagContext.init(element: HTMLElement, shadowRoot: ShadowRoot) =
            p(id = "paragraph-in-web-component") {
                +"I am a WebComponent"
            }
    }

    @Test
    fun testWebComponent() = runTest {
        initDocument()

        registerWebComponent("my-component", MyComponent::class)

        delay(250)

        val body = document.body.unsafeCast<HTMLBodyElement>()

        body.appendChild(document.createElement("my-component", ElementCreationOptions("my-component")))

        delay(250)

        val content = document.getElementsByTagName("my-component")[0]?.let {
            it.shadowRoot?.getElementById("paragraph-in-web-component")
        }

        assertEquals("I am a WebComponent", content?.textContent?.trim())
    }
}