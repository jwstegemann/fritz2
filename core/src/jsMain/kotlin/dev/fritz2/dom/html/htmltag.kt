package dev.fritz2.dom.html

import dev.fritz2.dom.HtmlTagMarker
import dev.fritz2.dom.Tag
import dev.fritz2.dom.WithDomNode
import dev.fritz2.utils.classes
import kotlinx.browser.window
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.w3c.dom.Element
import org.w3c.dom.Node

/**
 * Implementation of [Tag] to represent HTML5-tags.
 *
 * @param tagName name of the tag. Used to create the corresponding [Element]
 * @property id the DOM-id of the element to be created
 * @property baseClass a static base value for the class-attribute. All dynamic values for this attribute will be concatenated to this base-value.
 * @property job used for launching coroutines in
 * @property scope set some arbitrary scope entries into the [Tag]'s scope
 */
@HtmlTagMarker
open class HtmlTag<out E : Element>(
    private val tagName: String,
    final override val id: String? = null,
    final override val baseClass: String? = null,
    override val job: Job,
    override val scope: Scope,
) : Tag<E> {

    override val domNode: E = window.document.createElement(tagName).also { element ->
        if (id != null) element.id = id
        if (!baseClass.isNullOrBlank()) element.className = baseClass
    }.unsafeCast<E>()

    /**
     * Creates the content of the [HtmlTag] and appends it as a child to the wrapped [Element].
     *
     * @param element the parent element of the new content
     * @param content lambda building the content (following the type-safe-builder pattern)
     */
    override fun <N : Node, W : WithDomNode<N>> register(element: W, content: (W) -> Unit): W {
        content(element)
        domNode.appendChild(element.domNode)
        return element
    }

    private var className: String? = baseClass
    private var classFlow: Flow<String>? = null

    private fun updateClasses() {
        if (classFlow == null) {
            attr("class", className)
        } else if (className == null) {
            attr("class", classFlow!!)
        } else {
            attr("class", classFlow!!.map { classes(className, it) })
        }
    }

    override fun addToClasses(classesToAdd: String) {
        className = classes(className, classesToAdd)
        updateClasses()
    }

    override fun addToClasses(classesToAdd: Flow<String>) {
        classFlow = if (classFlow == null) classesToAdd else classFlow!!.combine(classesToAdd) { a, b -> classes(a, b) }
        updateClasses()
    }

    internal inner class AnnexContext : RenderContext {
        override fun <E : Node, T : WithDomNode<E>> register(element: T, content: (T) -> Unit): T {
            domNode.parentElement?.let {
                content(element)
                it.appendChild(element.domNode)
            }
            return element
        }

        override val job: Job = this@HtmlTag.job

        override val scope: Scope = this@HtmlTag.scope
    }

    /**
     * provides [RenderContext] next to this [HtmlTag] on the same DOM-level.
     */
    override val annex: RenderContext by lazy { AnnexContext() }
}
