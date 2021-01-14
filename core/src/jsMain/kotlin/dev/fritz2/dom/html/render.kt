package dev.fritz2.dom.html

import dev.fritz2.dom.Tag
import dev.fritz2.dom.removeChildren
import kotlinx.browser.document
import kotlinx.coroutines.Job
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
 * Creates a render context for [Tag]s and mounts it to a [parentElement].
 *
 * @param parentElement [HTMLElement] to mount to
 * @param override if true all child elements are removed before rendering
 * @param content [RenderContext] for rendering the data to the DOM
 */
fun render(
    parentElement: HTMLElement?,
    override: Boolean = true,
    content: RenderContext.() -> Unit
) {
    parentElement?.let {
        if (override) it.removeChildren()
        content(RenderContext(it.tagName, it.id, job = Job(), domNode = it))
    } ?: throw MountTargetNotFoundException("")
}