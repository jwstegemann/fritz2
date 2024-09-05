package dev.fritz2.core

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
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
        if (value != null) domNode.setAttribute(name, value)
        else domNode.removeAttribute(name)
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
        if (value) domNode.setAttribute(name, trueValue)
        else domNode.removeAttribute(name)
    }

    /**
     * Sets an attribute when [value] is true otherwise removes it.
     *
     * @param name to use
     * @param value for decision
     * @param trueValue value to use if attribute is set (default "")
     */
    fun attr(name: String, value: Boolean?, trueValue: String = "") {
        if (value != null && value) domNode.setAttribute(name, trueValue)
        else domNode.removeAttribute(name)
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
     * Adds a [String] of class names to the classes attribute of this [Tag]
     *
     * @param value as [String]
     */
    fun className(value: String)

    /**
     * Adds a [Flow] of class names to the classes attribute of this [Tag]
     *
     * @param value [Flow] with [String]
     */
    fun className(value: Flow<String>, initial: String = "")

    /**
     * Uses a [Flow] of [T] to create some class names by a [transform] lambda expression and add them to the classes
     * attribute of the [Tag].
     *
     * In order to set some classes immediately, you must provide an initial [T] which is used to create the
     * initial classes value with the [transform] lambda.
     *
     * Use this function to avoid flickering effects on reactively based styling!
     *
     * @param value a [Flow] of [T] that provides the parameter for the [transform] lambda
     * @param initial a [T] to be used as initial state in order to generate and add class names
     * immediately without waiting for the first value of the [Flow]
     * @param transform a lambda expression which finally creates class names by passing one [T]
     */
    fun <T> className(value: Flow<T>, initial: T, transform: (T) -> String) {
        className(value.map(transform), transform(initial))
    }

    /**
     * Sets the *class* attribute from a [List] of [String]s.
     *
     * @param values as [List] of [String]s
     */
    fun classList(values: List<String>) {
        className(values.joinToString(" "))
    }

    /**
     * Sets the *class* attribute from a [List] of [String]s.
     *
     * @param values [Flow] with [List] of [String]s
     */
    fun classList(values: Flow<List<String>>) {
        className(values.map { it.joinToString(" ") })
    }

    /**
     * Sets the *class* attribute from a [Map] of [String] to [Boolean].
     * If the value of the [Map]-entry is true, the key will be used inside the resulting [String].
     *
     * @param values as [Map] with key to set and corresponding values to decide
     */
    fun classMap(values: Map<String, Boolean>) {
        className(values.filter { it.value }.keys.joinToString(" "))
    }

    /**
     * Sets the *class* attribute from a [Map] of [String] to [Boolean].
     * If the value of the [Map]-entry is true, the key will be used inside the resulting [String].
     *
     * @param values [Flow] of [Map] with key to set and corresponding values to decide
     */
    fun classMap(values: Flow<Map<String, Boolean>>) {
        className(values.map { map -> map.filter { it.value }.keys.joinToString(" ") })
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

    override fun <X : Event> subscribe(eventName: String, capture: Boolean, selector: X.() -> Boolean): Listener<X, E> =
        Listener(domNode.subscribe(eventName, capture, selector))

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

    /**
     * This [MutableStateFlow] acts as a backing field for all class names. It holds arbitrary [List]s of
     * [StateFlow]s which manage each portion of class names. This way, multiple calls of any [className] variant
     * can be merged in one central place and must be mounted into the [Tag]s `class` attribute only once.
     */
    private val classesStateFlow by lazy {
        MutableStateFlow<List<StateFlow<String>>>(listOfNotNull(baseClass?.let { MutableStateFlow(it) }))
            .also { classesFlowList ->
                attr("class", classesFlowList.flatMapLatest { styleFlows ->
                    combine(styleFlows) { joinClasses(*it) }
                })
            }
    }

    /**
     * Small utility function to create the classes [String] from the current values of the [StateFlow]s.
     *
     * This function is used to create the initial class name values to be applied immediately
     * to the domnode.
     */
    private fun buildClasses() = joinClasses(*classesStateFlow.value.map { it.value }.toTypedArray())

    override fun className(value: String) {
        classesStateFlow.value += MutableStateFlow(value)
        // this ensures that the set state is applied *immediately* without `Flow`-"delay"
        attr("class", buildClasses())
    }

    override fun className(value: Flow<String>, initial: String) {
        classesStateFlow.value += value.stateIn(MainScope() + job, SharingStarted.Eagerly, initial)
        // this ensures that the set state is applied *immediately* without `Flow`-"delay".
        // in this case, the `initial` value gets applied as "promised".
        attr("class", buildClasses())
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