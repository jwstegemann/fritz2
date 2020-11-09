package dev.fritz2.dom.html

import dev.fritz2.dom.Tag
import dev.fritz2.dom.WithDomNode
import kotlinx.coroutines.Job
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

/**
 * creates a render context for [Tag]s.
 *
 * @param parentJob used when launching new coroutines
 * @param content html [Tag] elements to render
 */
fun render(
    parentJob: Job = Job(),
    content: RenderContext.() -> Unit
): List<Tag<HTMLElement>> = buildList {
    content(object : RenderContext {
        override val job = parentJob

        override fun <E : Element, T : WithDomNode<E>> register(element: T, content: (T) -> Unit): T {
            content(element)
            add(element.unsafeCast<Tag<HTMLElement>>())
            return element
        }
    })
}

/**
 * creates a render context for [Tag]s. It should only contain
 * one root [Tag] like [Div] otherwise a [MultipleRootElementsException]
 * will be thrown.
 *
 * @param parentJob
 * @param content html [Tag] elements to render
 * @throws MultipleRootElementsException if more then one root [Tag] is defined in [content]
 */
fun <E : Element> renderElement(
    parentJob: Job = Job(),
    content: RenderContext.() -> Tag<E>
): Tag<E> =
    content(object : RenderContext {
        override val job = parentJob

        var alreadyRegistered: Boolean = false

        override fun <E : Element, T : WithDomNode<E>> register(element: T, content: (T) -> Unit): T {
            if (alreadyRegistered) {
                throw MultipleRootElementsException("You can have only one root-tag per html-context!")
            } else {
                content(element)
                return element
            }
        }
    })

/**
 * occurs when more then one root [Tag] is defined in a [render] context
 *
 * @param message exception message text
 */
class MultipleRootElementsException(message: String) : RuntimeException(message)