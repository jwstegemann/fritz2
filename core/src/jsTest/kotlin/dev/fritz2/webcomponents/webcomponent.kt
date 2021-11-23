package dev.fritz2.webcomponents

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.*
import kotlin.test.Test
import kotlin.test.assertEquals

class WebComponentTests {

    object MyComponent : WebComponent<HTMLParagraphElement>() {
        override fun RenderContext.init(element: HTMLElement, shadowRoot: ShadowRoot) =
            p(id = "paragraph-in-web-component") {
                +"I am a WebComponent"
            }
    }

    @Test
    fun testWebComponent() = runTest {
        initDocument()

        registerWebComponent("my-component", MyComponent)

        delay(250)

        val body = document.body.unsafeCast<HTMLBodyElement>()

        body.appendChild(document.createElement("my-component", ElementCreationOptions("my-component")))

        delay(250)

        val content = document.getElementsByTagName("my-component")[0]?.let {
            it.shadowRoot?.getElementById("paragraph-in-web-component")
        }

        assertEquals("I am a WebComponent", content?.textContent?.trim())
    }

    object AttributeComponent : WebComponent<HTMLElement>() {
        override fun RenderContext.init(element: HTMLElement, shadowRoot: ShadowRoot): Tag<HTMLElement> {
            val store = storeOf("Initial")
            attributeChanges("test") handledBy { store.update(it) }

            return div(id = "contents") {
                store.data.asText()
            }
        }
    }

    @Test
    fun testAttributeChanges() = runTest {
        initDocument()

        delay(250)

        val body = document.body.unsafeCast<HTMLBodyElement>()
        val attrComponent = document.createElement("attr-component", ElementCreationOptions("attr-component"))
        attrComponent.setAttribute("test", "New")

        // This is only called down here in order to replicate https://github.com/jwstegemann/fritz2/issues/83.
        registerWebComponent("attr-component", AttributeComponent, "test")

        body.appendChild(attrComponent)

        delay(250)

        val content = attrComponent.shadowRoot?.getElementById("contents")

        assertEquals("New", content?.textContent?.trim())

        attrComponent.setAttribute("test", "Newer")
        delay(250)

        assertEquals("Newer", content?.textContent?.trim())
    }
}