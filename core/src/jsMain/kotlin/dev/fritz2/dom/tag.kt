package dev.fritz2.dom

import dev.fritz2.binding.*
import dev.fritz2.dom.html.HtmlElements
import kotlinx.browser.window
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.Element
import org.w3c.dom.events.Event

/**
 * A marker to separate the layers of calls in the type-safe-builder pattern.
 */
@DslMarker
annotation class HtmlTagMarker

/**
 * Represents a tag in the resulting HTML. Sorry for the name, but we needed to delimit it from the [Element] it is wrapping.
 *
 * @param tagName name of the tag. Used to create the corresponding [Element]
 * @param id the DOM-id of the element to be created
 * @param baseClass a static base value for the class-attribute. All dynamic values for this attribute will be concatenated to this base-value.
 * @param domNode the [Element]-instance that is wrapped by this [Tag] (you should never have to pass this by yourself, just let it be created by the default)
 */
//TODO: remove unnecessary default-arguments
@HtmlTagMarker
open class Tag<out T : Element>(
    tagName: String,
    val id: String? = null,
    val baseClass: String? = null,
    override val domNode: T = createDomElement(tagName, id, baseClass).unsafeCast<T>()
) : WithDomNode<T>, WithAttributes<T>, WithComment<T>, WithEvents<T>(), HtmlElements {

    /**
     * creates the content of the [Tag] and appends it as a child to the wrapped [Element]
     *
     * @param element the parent element of the new content
     * @param content lambda building the content (following the type-safe-builder pattern)
     */
    override fun <X : Element, T : Tag<X>> register(element: T, content: (T) -> Unit): T {
        content(element)
        domNode.appendChild(element.domNode)
        return element
    }

    /**
     * binds a [Flow] of [Tag]s at this position (creates a DomMountPoint as a placeholder and adds it to the builder)
     *
     * @param preserveOrder set this to true, if you need to guarantee the order of children by using a temporary placeholder element
     */
    fun <X : Element> Flow<Tag<X>>.bind(preserveOrder: Boolean = false): SingleMountPoint<WithDomNode<Element>> =
        if (preserveOrder) DomMountPointPreserveOrder(this, domNode)
        else DomMountPoint(this, domNode)

    /**
     * binds a [Flow] of nullable [Tag]s at this position (creates a DomMountPoint as a placeholder and adds it to the builder)
     *
     */
    fun <X : Element> Flow<Tag<X>?>.bind(): SingleMountPoint<WithDomNode<Element>?> =
        NullableDomMountPointPreserveOrder(this, domNode)

    fun <X : Element> Flow<List<Tag<X>>>.bind(preserveOrder: Boolean = false): SingleMountPoint<List<WithDomNode<Element>>> =
        if (preserveOrder) DomListMountPointPreserveOrder(this, domNode)
        else DomListMountPoint(this, domNode)

    /**
     * binds a [Seq] of [Tag]s at this position (creates a [DomMultiMountPoint] as a placeholder and adds it to the builder)
     */
    fun <X : Element> Seq<Tag<X>>.bind(): MultiMountPoint<WithDomNode<Element>> = DomMultiMountPoint(this.data, domNode)

    /**
     * convenience method to bind [Event]s to a [Handler]
     *
     * @param handler [SimpleHandler] that will handle the [Event]s
     * @receiver [Listener]
     */
    infix fun <E : Event, X : Element> Listener<E, X>.handledBy(handler: Handler<Unit>) =
        handler.collect(this.events.map { Unit })

    /**
     * Delegate to bind a [Flow] of [String]s as the dynamic part of the class-attribute
     * @see WithAttributes.bindAttr
     */
    var className: Flow<String>
        get() {
            throw NotImplementedError()
        }
        set(value) {
            (baseClass?.let { value.map { "$baseClass $it" } } ?: value).bindAttr("class")
        }

    var style: Flow<String> by AttributeDelegate

    /**
     * Delegate to bind a [Flow] of [List]s as the dynamic part of the class-attribute
     * @see WithAttributes.bindAttr
     */
    var classList: Flow<List<String>>
        get() {
            throw NotImplementedError()
        }
        set(values) {
            (if (baseClass != null) values.map { it + baseClass } else values).bindAttr("class")
        }

    /**
     * Delegate to bind a [Flow] of [Map]s as the dynamic part of the class-attribute
     * @see WithAttributes.bindAttr
     */
    var classMap: Flow<Map<String, Boolean>>
        get() {
            throw NotImplementedError()
        }
        set(values) {
            (if (baseClass != null) values.map { it + (baseClass to true) } else values).bindAttr("class")
        }
}

internal fun createDomElement(tagName: String, id: String?, baseClass: String?): Element =
    window.document.createElement(tagName).also { element ->
        id?.let {
            element.setAttribute("id", it)
        }
        baseClass?.let {
            if (it.isNotBlank()) element.setAttribute("class", it.trim())
        }
    }
