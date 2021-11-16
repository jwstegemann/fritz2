package dev.fritz2.dom.html

import dev.fritz2.dom.Tag
import dev.fritz2.dom.WithDomNode
import kotlinx.browser.document
import kotlinx.coroutines.Job
import kotlinx.dom.clear
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.Node
import org.w3c.dom.css.CSSStyleSheet

/**
 * Occurs when the targeted html element is not present in document.
 *
 * @param message exception message
 */
class MountTargetNotFoundException(message: String) : Exception(message)

/**
 * Creates a [RenderContext] for [Tag]s and
 * mounts it to a constant element in the static html file
 * which id matches the [selector].
 *
 * @param selector [query selector](https://developer.mozilla.org/en-US/docs/Web/API/Document/querySelector)
 * of the element to mount to
 * @param override if true all child elements are removed before rendering
 * @param content [RenderContext] for rendering the data to the DOM
 * @throws MountTargetNotFoundException if target element with [selector] not found
 */
fun render(
    selector: String,
    override: Boolean = true,
    content: RenderContext.() -> Unit
) {
    document.querySelector(selector)?.let { parentElement ->
        if (parentElement is HTMLElement) {
            render(parentElement, override, content)
        } else MountTargetNotFoundException("element with id=$selector is not an HTMLElement")
    } ?: throw MountTargetNotFoundException("html document contains no element with id=$selector")
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
    //add style sheet continaing mount-point-class
    addGlobalStyle(".mount-point { display: contents; }")
    targetElement?.let {
        if (override) it.clear()
        content(object : RenderContext {
            override fun <E : Node, T : WithDomNode<E>> register(element: T, content: (T) -> Unit): T {
                content(element)
                it.appendChild(element.domNode)
                return element
            }

            override val job = Job()
            override val scope = Scope()
        })
    } ?: throw MountTargetNotFoundException("targetElement should not be null")
}

const val FRITZ2_GLOBAL_STYLESHEET_ID = "fritz2-global-styles"

internal fun getOrCreateGlobalStylesheet() = (document.getElementById(FRITZ2_GLOBAL_STYLESHEET_ID)?.let {
    (it as HTMLStyleElement).sheet
} ?: (document.createElement("style") as HTMLStyleElement).also {
    it.setAttribute("id", FRITZ2_GLOBAL_STYLESHEET_ID)
    it.appendChild(document.createTextNode(""))
    document.head!!.appendChild(it)
}.sheet!!) as CSSStyleSheet

/**
 * Adds global css-rules to a fritz2-specific stylesheet added to the document when first called
 *
 * @param css the valid css-code to insert
 */
fun addGlobalStyle(css: String) {
    getOrCreateGlobalStylesheet().insertRule(css, 0)
}

/**
 * Adds global css-rules to a fritz2-specific stylesheet added to the document when first called
 *
 * @param css the valid rules to insert
 */
fun addGlobalStyles(css: List<String>) {
    val stylesheet = getOrCreateGlobalStylesheet()
    css.forEach { stylesheet.insertRule(it, 0) }
}