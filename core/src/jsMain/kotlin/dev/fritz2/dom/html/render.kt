package dev.fritz2.dom.html

import dev.fritz2.dom.MultipleRootElementsException
import dev.fritz2.dom.Tag
import dev.fritz2.dom.WithDomNode
import dev.fritz2.dom.removeChildren
import kotlinx.browser.document
import kotlinx.coroutines.Job
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

/**
 * Occurs when the targeted html element is not present in document.
 *
 * @param targetId id which used for mounting
 */
class MountTargetNotFoundException(targetId: String) :
    Exception("html document contains no element with id: $targetId")

/**
 * Creates a render context for [Tag]s and
 * mounts it to a constant element in the static html file
 * which id matches the [targetId].
 *
 * @param targetId id of the element to mount to
 * @param override if true all child elements are removed before rendering
 * @param content [RenderContext] for rendering the data to the DOM
 * @throws MountTargetNotFoundException if target element with [targetId] not found
 */
fun render(
    targetId: String,
    override: Boolean = true,
    content: RenderContext.() -> Unit
) {
    document.getElementById(targetId)?.let { parentElement ->
        if (parentElement is HTMLElement) {
            render(parentElement, override, content)
        } else MountTargetNotFoundException(targetId)
    } ?: throw MountTargetNotFoundException(targetId)
}

/**
 * Creates a render context for [Tag]s and mounts it to a [targetElement].
 *
 * @param targetElement [HTMLElement] to mount to
 * @param override if true all child elements are removed before rendering
 * @param content [RenderContext] for rendering the data to the DOM
 * @throws MountTargetNotFoundException if [targetElement] not found
 */
fun render(
    targetElement: HTMLElement?,
    override: Boolean = true,
    content: RenderContext.() -> Unit
) {
    targetElement?.let {
        if (override) it.removeChildren()
        content(RenderContext(it.tagName, it.id, job = Job(), domNode = it))
    } ?: throw MountTargetNotFoundException("")
}

/**
 * Creates a [Tag] by providing you a tag context.
 *
 * @param content [HtmlElements] for creating [Tag]s
 * @throws MultipleRootElementsException if more then one root [Tag] is defined
 */
fun <E : Element> tag(
    content: HtmlElements.() -> Tag<E>
): Tag<E> = content(object : HtmlElements {
    override val job = Job()

    var alreadyRegistered: Boolean = false

    override fun <E : Element, T : WithDomNode<E>> register(element: T, content: (T) -> Unit): T {
        if (alreadyRegistered) {
            throw MultipleRootElementsException("You can have only one root-tag per html-context!")
        } else {
            content(element)
            alreadyRegistered = true
            return element
        }
    }
})

/**
 * Mounts a [Tag] to a constant element in the static html file.
 *
 * @param targetId id of the element to mount to
 * @param override if true all child elements are removed before rendering
 * @receiver the [Tag] to mount to element with [targetId]
 * @throws MountTargetNotFoundException if target element with [targetId] not found
 */
fun <E : Element> Tag<E>.mount(
    targetId: String,
    override: Boolean = true
) {
    document.getElementById(targetId)?.let { parent ->
        if(parent is HTMLElement) {
            this@mount.mount(parent, override)
        } else throw MountTargetNotFoundException(targetId)
    } ?: throw MountTargetNotFoundException(targetId)
}

/**
 * Mounts a [Tag] to a constant element in the static html file.
 *
 * @param targetElement [HTMLElement] to mount to
 * @param override if true all child elements are removed before rendering
 * @receiver the [Tag] to mount to [targetElement]
 * @throws MountTargetNotFoundException if [targetElement] not found
 */
fun <E : Element> Tag<E>.mount(
    targetElement: HTMLElement?,
    override: Boolean = true
) {
    targetElement?.let {
        if(override) it.removeChildren()
        it.appendChild(this.domNode)
    } ?: throw MountTargetNotFoundException("")
}