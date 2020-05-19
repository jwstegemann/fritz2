package io.fritz2.webcomponents

import io.fritz2.dom.Tag
import io.fritz2.dom.html.html
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
            html {
                p(id = "paragraph-in-web-component") {
                    text("I am a WebComponent")
                }
            }
    }

    @Test
    fun testWebComponent() = runTest {
        initDocument()

        registerWebComponent("my-component", MyComponent::class)

        delay(500)

        val body = document.getElementById(targetId).unsafeCast<HTMLBodyElement>()

        body.appendChild(document.createElement("my-component", ElementCreationOptions("my-component")))

        delay(1000)

        //val content = document.getElementById("paragraph-in-web-component").unsafeCast<HTMLParagraphElement>()
        val content = document.getElementsByTagName("p").get(0)

        assertEquals("I am a WebComponent", content?.textContent?.trim())
    }

}