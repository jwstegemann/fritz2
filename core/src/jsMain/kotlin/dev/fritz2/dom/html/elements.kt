@file:Suppress("unused")

package dev.fritz2.dom.html

import dev.fritz2.dom.HtmlTag
import kotlinx.browser.document
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Element
import org.w3c.dom.svg.SVGElement

const val SVG_XMLNS = "http://www.w3.org/2000/svg"

/**
 * Exposes the JavaScript [SVGElement](https://developer.mozilla.org/en-US/docs/Web/API/SVGElement) to Kotlin
 */
class SvgTag<out E : Element>(private val tagName: String, id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
    HtmlTag<SVGElement>("", id, baseClass, job, scope) {

    override fun createDomNode(): SVGElement =
        document.createElementNS(SVG_XMLNS, tagName).unsafeCast<SVGElement>().apply {
            if (baseClass != null) setAttributeNS(null, "class", baseClass)
        }

    /**
     * Sets the given [xml] string to the *innerHTML* of the [SVGElement].
     *
     * @param xml svg xml content
     */
    fun content(xml: String) {
        domNode.innerHTML = xml
    }

    fun viewBox(value: String) = attr("viewBox", value)
    fun viewBox(value: Flow<String>) = attr("viewBox", value)

    fun fill(value: String) = attr("fill", value)
    fun fill(value: Flow<String>) = attr("fill", value)
}
