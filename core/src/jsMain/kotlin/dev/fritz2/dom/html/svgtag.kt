@file:Suppress("unused")

package dev.fritz2.dom.html

import dev.fritz2.dom.Tag
import kotlinx.browser.document
import kotlinx.coroutines.Job
import org.w3c.dom.Element
import org.w3c.dom.svg.SVGElement

const val SVG_XMLNS = "http://www.w3.org/2000/svg"

/**
 * Implementation of [HtmlTag] to represent the JavaScript
 * [SVGElement](https://developer.mozilla.org/en-US/docs/Web/API/SVGElement) to Kotlin
 */
class SvgTag<out E : Element>(tagName: String, id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    HtmlTag<SVGElement>(tagName, id, baseClass, job, scope) {

    override val domNode =
        document.createElementNS(SVG_XMLNS, tagName).unsafeCast<SVGElement>().apply {
            if (!baseClass.isNullOrBlank()) setAttributeNS(null, "class", baseClass)
            if (!id.isNullOrBlank()) setAttributeNS(null, "id", id)
        }

    /**
     * Sets the given [xml] string to the *innerHTML* of the [SVGElement].
     *
     * @param xml svg xml content
     */
    fun content(xml: String) {
        domNode.innerHTML = xml
    }

    /**
     * sets XML-namespace of a [Tag]
     *
     * @param value namespace to set
     */
    fun xmlns(value: String) = attr("xmlns", value)
}