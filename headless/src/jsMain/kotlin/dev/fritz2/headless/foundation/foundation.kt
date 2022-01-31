package dev.fritz2.headless.foundation

import dev.fritz2.dom.HtmlTag
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.ScopeContext
import org.w3c.dom.Element

typealias TagFactory<C> = (RenderContext, String?, String?, ScopeContext.() -> Unit, C.() -> Unit) -> C

fun <T> List<T>.rotateNext(element: T): T? = indexOf(element).let {
    when (it) {
        -1 -> null
        size - 1 -> this[0]
        else -> this[it + 1]
    }
}

fun <T> List<T>.rotatePrevious(element: T): T? = indexOf(element).let {
    when (it) {
        -1 -> null
        0 -> this.last()
        else -> this[it - 1]
    }
}

enum class Direction(val value: Int) {
    Previous(-1), Next(1)
}

enum class Orientation {
    Horizontal, Vertical
}

/**
 * Sets an attribute only if it is not present yet.
 *
 * This is intended only for attributes, that have a *static* character, like an ARIA "role" for example.
 * It enables a client to overrule a default attribute set by a lower layer of a component library, if the latter
 * uses this defensive function to set its default attribute.
 *
 * @param name to use
 * @param value to use
 */
fun <N : Element> HtmlTag<N>.attrIfNotSet(name: String, value: String) {
    if (!domNode.hasAttribute(name)) attr(name, value)
}