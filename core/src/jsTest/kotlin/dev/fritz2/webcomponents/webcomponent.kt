package dev.fritz2.webcomponents

import dev.fritz2.dom.Tag
import dev.fritz2.dom.WithDomNode
import dev.fritz2.dom.html.TagContext
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.browser.document
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.w3c.dom.*
import kotlin.test.Test
import kotlin.test.assertEquals

//FIXME: use RenderContext instead!
inline fun <E : Element> createTag(content: TagContext.() -> Tag<E>): Tag<E> =
    content(object : TagContext {
        override val job = Job()
        override fun <E : Element, T : WithDomNode<E>> register(element: T, content: (T) -> Unit): T {
            content(element)
            return element
        }
    })

class WebComponentTests {

    class MyComponent : WebComponent<HTMLParagraphElement>() {
        override fun init(element: HTMLElement, shadowRoot: ShadowRoot) =
            createTag {
                p(id = "paragraph-in-web-component") {
                    +"I am a WebComponent"
                }
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