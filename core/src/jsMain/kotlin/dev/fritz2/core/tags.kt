@file:Suppress("TooManyFunctions")

package dev.fritz2.core

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.dom.clear
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import org.w3c.dom.svg.SVGElement

/**
 * A marker to separate the layers of calls in the type-safe-builder pattern.
 */
@DslMarker
annotation class HtmlTagMarker

/**
 * Represents a tag.
 * Sorry for the name, but we needed to delimit it from the [Element] it is wrapping.
 */
interface Tag<out E : Element> : RenderContext, WithDomNode<E>, WithEvents<E> {

    /**
     * id of this [Tag]
     */
    val id: String?

    /**
     * constant css-classes of this [Tag]
     */
    val baseClass: String?

    /**
     * Sets an attribute.
     *
     * @param name to use
     * @param value to use
     */
    fun attr(name: String, value: String) {
        domNode.setAttribute(name, value)
    }

    /**
     * Sets an attribute only if its [value] is not null.
     *
     * @param name to use
     * @param value to use
     */
    fun attr(name: String, value: String?) {
        if (value != null) {
            domNode.setAttribute(name, value)
        } else {
            domNode.removeAttribute(name)
        }
    }

    /**
     * Sets an attribute.
     *
     * @param name to use
     * @param value to use
     */
    fun attr(name: String, value: Flow<String>) {
        mountSimple(job, value) { v -> attr(name, v) }
    }

    /**
     * Sets an attribute only for all none null values of the flow.
     *
     * @param name to use
     * @param value to use
     */
    fun attr(name: String, value: Flow<String?>) {
        mountSimple(job, value) { v -> attr(name, v) }
    }

    /**
     * Sets an attribute.
     *
     * @param name to use
     * @param value to use
     */
    fun <T> attr(name: String, value: T) {
        attr(name, value?.toString())
    }

    /**
     * Sets an attribute.
     *
     * @param name to use
     * @param value to use
     */
    fun <T> attr(name: String, value: Flow<T>) {
        mountSimple(job, value.map { it?.toString() }) { v -> attr(name, v) }
    }

    /**
     * Sets an attribute when [value] is true otherwise removes it.
     *
     * @param name to use
     * @param value for decision
     * @param trueValue value to use if attribute is set (default "")
     */
    fun attr(name: String, value: Boolean, trueValue: String = "") {
        if (value) {
            domNode.setAttribute(name, trueValue)
        } else {
            domNode.removeAttribute(name)
        }
    }

    /**
     * Sets an attribute when [value] is true otherwise removes it.
     *
     * @param name to use
     * @param value for decision
     * @param trueValue value to use if attribute is set (default "")
     */
    fun attr(name: String, value: Boolean?, trueValue: String = "") {
        if (value != null && value) {
            domNode.setAttribute(name, trueValue)
        } else {
            domNode.removeAttribute(name)
        }
    }

    /**
     * Sets an attribute when [value] is true otherwise removes it.
     *
     * @param name to use
     * @param value for decision
     * @param trueValue value to use if attribute is set (default "")
     */
    fun attr(name: String, value: Flow<Boolean>, trueValue: String = "") {
        mountSimple(job, value) { v -> attr(name, v, trueValue) }
    }

    /**
     * Sets an attribute when [value] is true otherwise removes it.
     *
     * @param name to use
     * @param value for decision
     * @param trueValue value to use if attribute is set (default "")
     */
    fun attr(name: String, value: Flow<Boolean?>, trueValue: String = "") {
        mountSimple(job, value) { v -> attr(name, v, trueValue) }
    }

    /**
     * adds a [String] of class names to the classes attribute of this [Tag]
     */
    fun addToClasses(classesToAdd: String)

    /**
     * adds a [Flow] of class names to the classes attribute of this [Tag]
     */
    fun addToClasses(classesToAdd: Flow<String>)

    /**
     * Sets the *class* attribute.
     *
     * @param value as [String]
     */
    fun className(value: String) {
        addToClasses(value)
    }

    /**
     * Sets the *class* attribute.
     *
     * @param value [Flow] with [String]
     */
    fun className(value: Flow<String>) {
        addToClasses(value)
    }

    /**
     * Sets the *class* attribute from a [List] of [String]s.
     *
     * @param values as [List] of [String]s
     */
    fun classList(values: List<String>) {
        addToClasses(values.joinToString(" "))
    }

    /**
     * Sets the *class* attribute from a [List] of [String]s.
     *
     * @param values [Flow] with [List] of [String]s
     */
    fun classList(values: Flow<List<String>>) {
        addToClasses(values.map { it.joinToString(" ") })
    }

    /**
     * Sets the *class* attribute from a [Map] of [String] to [Boolean].
     * If the value of the [Map]-entry is true, the key will be used inside the resulting [String].
     *
     * @param values as [Map] with key to set and corresponding values to decide
     */
    fun classMap(values: Map<String, Boolean>) {
        addToClasses(values.filter { it.value }.keys.joinToString(" "))
    }

    /**
     * Sets the *class* attribute from a [Map] of [String] to [Boolean].
     * If the value of the [Map]-entry is true, the key will be used inside the resulting [String].
     *
     * @param values [Flow] of [Map] with key to set and corresponding values to decide
     */
    fun classMap(values: Flow<Map<String, Boolean>>) {
        addToClasses(values.map { map -> map.filter { it.value }.keys.joinToString(" ") })
    }

    /**
     * Sets the *style* attribute.
     *
     * @param value [String] to set
     */
    fun inlineStyle(value: String) {
        attr("style", value)
    }

    /**
     * Sets the *style* attribute.
     *
     * @param value [Flow] with [String]
     */
    fun inlineStyle(value: Flow<String>) {
        attr("style", value)
    }

    /**
     * Sets all scope-entries as data-attributes to the element.
     */
    fun Scope.asDataAttr() {
        for ((k, v) in this) {
            attr("data-${k.name}", v.toString())
        }
    }

    /**
     * Creates an [Listener] for the given event [eventName].
     *
     * @param eventName of the [Event] to listen for
     */
    override fun <X : Event> subscribe(eventName: String, capture: Boolean, init: Event.() -> Unit): Listener<X, E> =
        Listener(domNode.subscribe(eventName, capture, init))

    /**
     * Adds text-content of a [Flow] at this position
     *
     * @param into target to render text-content to
     * @receiver text-content
     */
    fun Flow<String>.renderText(into: Tag<*>? = null) {
        val target = into?.apply(SET_MOUNT_POINT_DATA_ATTRIBUTE) ?: span(content = SET_MOUNT_POINT_DATA_ATTRIBUTE)

        mountSimple(job, this) { content ->
            target.domNode.clear()
            target.domNode.appendChild(window.document.createTextNode(content))
        }
    }

    /**
     * Adds text-content of a [Flow] at this position
     *
     * @param into target to render text-content to
     * @receiver text-content
     */
    fun <T> Flow<T>.renderText(into: Tag<*>? = null) = this.asString().renderText(into)

    /**
     * Adds static text-content at this position
     *
     * @receiver text-content
     */
    operator fun String.unaryPlus(): Node = domNode.appendChild(document.createTextNode(this))

    /**
     * Adds a comment in your HTML by using !"Comment Text".
     *
     * @receiver comment-content
     */
    operator fun String.not(): Node = domNode.appendChild(document.createComment(this))

    /**
     * Sets scope-entry for the given [key] as data-attribute to the element
     * when available.
     *
     * @param key key of scope-entry to look for in scope
     */
    fun <T : Any> Scope.asDataAttr(key: Scope.Key<T>) {
        this[key]?.let {
            attr("data-${key.name}", it.toString())
        }
    }

    /**
     * This extension method takes a boolean [Flow] that controls the forwarding of the initial value:
     * If it is `true` the value will be passed further on the result flow, if it is `false` a `null` will appear instead.
     *
     * This is especially useful for DOM node attributes, that should only appear if a certain condition is true.
     *
     * Take the `aria-controls` attribute as example. This should only be set, if there is an area active / visible
     * to control. Within a dynamic component - like some disclosure based one - the latter is only shown, if a state-flow
     * is `true`:
     * ```kotlin
     * // `open`: Flow<Boolean>
     * button.attr("aria-controls", "panelId".whenever(open))
     * //                                     ^^^^^^^^^^^^^^
     * //                                     if open == true -> result flow provides "panelId" String
     * //                                     if open == false -> result flow provides `null` -> whole attribute is removed
     * ```
     *
     *  @param condition the boolean flow that decides whether to forward [T] or `null`
     */
    fun <T> T.whenever(condition: Flow<Boolean>): Flow<T?> = condition.map { if (it) this else null }

    /**
     * This extension method takes a boolean [Flow] that controls the forwarding of an initial flow:
     * If it is `true` the current value will be passed further on the result flow, if it is `false` a `null` will appear
     * instead.
     *
     * @see whenever
     */
    fun <T> Flow<T>.whenever(condition: Flow<Boolean>): Flow<T?> =
        condition.flatMapLatest { cond -> this.map { value -> if (cond) value else null } }

    /**
     * provides [RenderContext] next to this [Tag] on the same DOM-level.
     */
    val annex: RenderContext
}

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
    @Suppress("OVERRIDE_BY_INLINE")
    final override inline fun <N : Node, W : WithDomNode<N>> register(element: W, content: (W) -> Unit): W {
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

const val SVG_XMLNS = "http://www.w3.org/2000/svg"

/**
 * Implementation of [HtmlTag] to represent the JavaScript
 * [SVGElement](https://developer.mozilla.org/en-US/docs/Web/API/SVGElement) to Kotlin
 */
class SvgTag(tagName: String, id: String? = null, baseClass: String? = null, job: Job, scope: Scope) :
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
