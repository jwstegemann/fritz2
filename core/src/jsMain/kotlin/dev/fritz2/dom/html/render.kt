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
 * @param message exception message
 */
class MountTargetNotFoundException(message: String) : Exception(message)

/**
 * Creates a [RenderContext] for [Tag]s and
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
        } else MountTargetNotFoundException("element with id=$targetId is not an HTMLElement")
    } ?: throw MountTargetNotFoundException("html document contains no element with id=$targetId")
}

/**
 * Creates a [RenderContext] for [Tag]s and mounts it to a [targetElement].
 *
 * @param targetElement [HTMLElement] to mount to, default is *document.body*
 * @param override if true all child elements are removed before rendering
 * @param content [RenderContext] for rendering the data to the DOM
 * @throws MountTargetNotFoundException if [targetElement] not found
 */
fun render(
    targetElement: HTMLElement? = document.body,
    override: Boolean = true,
    content: RenderContext.() -> Unit
) {
    targetElement?.let {
        if (override) it.removeChildren()
        content(RenderContext(it.tagName, it.id, job = Job(), domNode = it))
    } ?: throw MountTargetNotFoundException("targetElement should not be null")
}