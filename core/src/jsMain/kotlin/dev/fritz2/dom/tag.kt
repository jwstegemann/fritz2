package dev.fritz2.dom

import dev.fritz2.binding.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.MultipleRootElementsException
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.lenses.IdProvider
import dev.fritz2.lenses.elementLens
import dev.fritz2.utils.Myer
import kotlinx.browser.window
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.*
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

/**
 * A marker to separate the layers of calls in the type-safe-builder pattern.
 */
@DslMarker
annotation class HtmlTagMarker

/**
 * Represents a tag in the resulting HTML.
 * Sorry for the name, but we needed to delimit it from the [Element] it is wrapping.
 *
 * @param tagName name of the tag. Used to create the corresponding [Element]
 * @param id the DOM-id of the element to be created
 * @param baseClass a static base value for the class-attribute.
 * All dynamic values for this attribute will be concatenated to this base-value.
 * @param job used for launching coroutines in
 * @param domNode the [Element]-instance that is wrapped by this [Tag]
 * (you should never have to pass this by yourself, just let it be created by the default)
 */
@HtmlTagMarker
open class Tag<out E : Element>(
    tagName: String,
    val id: String? = null,
    val baseClass: String? = null,
    override val job: Job,
    override val domNode: E = window.document.createElement(tagName).also { element ->
        if (id != null) element.id = id
        if (baseClass != null) element.className = baseClass
    }.unsafeCast<E>()
) : WithDomNode<E>, WithComment<E>, WithEvents<E>(), RenderContext {

    /**
     * Creates the content of the [Tag] and appends it as a child to the wrapped [Element]
     *
     * @param element the parent element of the new content
     * @param content lambda building the content (following the type-safe-builder pattern)
     */
    override fun <E : Element, W : WithDomNode<E>> register(element: W, content: (W) -> Unit): W {
        content(element)
        domNode.appendChild(element.domNode)
        return element
    }

    /**
     * Creates a new [RenderContext] for transferring the data inside the [Flow] to [Tag]s which are
     * visible in the browser.
     *
     * @receiver [Flow] containing the data
     * @param renderContext new [RenderContext] for rendering the data to the DOM
     */
    inline fun <V> Flow<V>.render(crossinline renderContext: RenderContext.(V) -> Unit) {
        mountDomNodeList(job, domNode) { childJob ->
            this.map { data ->
                childJob.cancelChildren()
                dev.fritz2.dom.html.render(childJob) {
                    renderContext(data)
                }
            }
        }
    }

    /**
     * Creates a new [RenderContext] for transferring the data inside the [Flow] to [Tag]s which are
     * visible in the browser. It should only contain one root [Tag] like a [Div] otherwise a
     * [MultipleRootElementsException] will be thrown.
     *
     * @receiver [Flow] containing the data
     * @param preserveOrder decides to keep the order of the rendered [Tag]s when they are a the same level as
     * static [Tag]s. (default true)
     * @param renderContext new [RenderContext] for rendering the data to the DOM
     */
    inline fun <V> Flow<V>.renderElement(
        preserveOrder: Boolean = true,
        crossinline renderContext: RenderContext.(V) -> Tag<HTMLElement>
    ) {
        if (preserveOrder) {
            mountDomNode(job, domNode) { childJob ->
                this.map { data ->
                    childJob.cancelChildren()
                    dev.fritz2.dom.html.renderElement(job) {
                        renderContext(data)
                    }
                }
            }
        } else {
            mountDomNodeUnordered(job, domNode) { childJob ->
                this.map { data ->
                    childJob.cancelChildren()
                    dev.fritz2.dom.html.renderElement(job) {
                        renderContext(data)
                    }
                }
            }
        }
    }

    /**
     * Accumulates a [Pair] and a [List] to a new [Pair] of [List]s
     *
     * @param accumulator [Pair] of two [List]s
     * @param newValue new [List] to accumulate
     */
    suspend inline fun <T> accumulate(
        accumulator: Pair<List<T>, List<T>>,
        newValue: List<T>
    ): Pair<List<T>, List<T>> = Pair(accumulator.second, newValue)


    /**
     * Creates a new [RenderContext] for a [Flow] of [List].
     * Internally the [Patch]es are determined using Myer's diff-algorithm.
     * Elements with the same id, provided by the [idProvider], are considered the same element.
     * This allows the detection of moves. Keep in mind, that no [Patch] is derived,
     * when an element stays the same, but changes it's internal values.
     *
     * @param [idProvider] to identify the element in the list (i.e. when it's content changes over time)
     * @param renderContext new [RenderContext] for rendering the data to the DOM
     */
    inline fun <V, I> Flow<List<V>>.renderEach(
        noinline idProvider: IdProvider<V, I>,
        crossinline renderContext: RenderContext.(V) -> Tag<HTMLElement>
    ) {
        mountDomNodePatch(job, domNode) { childJob ->
            childJob.cancelChildren()
            this.scan(Pair(emptyList(), emptyList()), ::accumulate).flatMapConcat { (old, new) ->
                Myer.diff(old, new, idProvider)
            }.map {
                it.map { value ->
                    renderContext(value)
                }
            }
        }
    }


    /**
     * Creates a new [RenderContext] for a [Flow] of [List].
     * Internally the [Patch]es are determined using Myer's diff-algorithm.
     * This allows the detection of moves. Keep in mind, that no [Patch] is derived,
     * when an element stays the same, but changes it's internal values.
     *
     * @param renderContext new [RenderContext] for rendering the data to the DOM
     */
    inline fun <V> Flow<List<V>>.renderEach(
        crossinline renderContext: RenderContext.(V) -> Tag<HTMLElement>
    ) {
        mountDomNodePatch(job, domNode) { childJob ->
            childJob.cancelChildren()
            this.scan(Pair(emptyList(), emptyList()), ::accumulate).flatMapConcat { (old, new) ->
                Myer.diff(old, new)
            }.map {
                it.map { value ->
                    renderContext(value)
                }
            }
        }
    }

    //TODO: comment
    /**
     * Creates a new [RenderContext] for a [Flow] of [List].
     * (do not use this, if you want to manipulate the list itself (add or move elements, filter, etc.).
     */
    inline fun <V, I> RootStore<List<V>>.renderEach(
        noinline idProvider: IdProvider<V, I>,
        crossinline renderContext: RenderContext.(SubStore<List<V>, List<V>, V>) -> Tag<HTMLElement>
    ) {
        mountDomNodePatch(job, domNode) { childJob ->
            childJob.cancelChildren()
            this.data.scan(Pair(emptyList(), emptyList()), ::accumulate).flatMapConcat { (old, new) ->
                Myer.diff(old, new, idProvider)
            }.map {
                it.map { value ->
                    renderContext(sub(elementLens(value, idProvider)))
                }
            }
        }
    }

    //TODO: comment
    /**
     * creates a [Seq] of [SubStore]s, one for each element of the [List] without [IdProvider]
     * using the index in the list (do not use this, if you want to manipulate the list itself (add or move elements, filter, etc.).
     */
    inline fun <V> RootStore<List<V>>.renderEach(
        crossinline renderContext: RenderContext.(Store<V>) -> Tag<HTMLElement>
    ) {
        mountDomNodePatch(job, domNode) { childJob ->
            childJob.cancelChildren()
            this.data.map { it.withIndex().toList() }.eachIndex().map {
                it.map { (i, _) -> renderContext(sub(i)) }
            }
        }
    }

    //TODO: comment
    inline fun <R, P, V, I> SubStore<R, P, List<V>>.renderEach(
        noinline idProvider: IdProvider<V, I>,
        crossinline renderContext: RenderContext.(SubStore<R, List<V>, V>) -> Tag<HTMLElement>
    ) {
        mountDomNodePatch(job, domNode) { childJob ->
            childJob.cancelChildren()
            this.data.scan(Pair(emptyList(), emptyList()), ::accumulate).flatMapConcat { (old, new) ->
                Myer.diff(old, new, idProvider)
            }.map {
                it.map { value ->
                    renderContext(sub(elementLens(value, idProvider)))
                }
            }
        }
    }

    //TODO: comment
    inline fun <R, P, V> SubStore<R, P, List<V>>.renderEach(
        crossinline renderContext: RenderContext.(Store<V>) -> Tag<HTMLElement>
    ) {
        mountDomNodePatch(job, domNode) { childJob ->
            childJob.cancelChildren()
            this.data.map { it.withIndex().toList() }.eachIndex().map {
                it.map { (i, _) -> renderContext(sub(i)) }
            }
        }
    }

    //TODO: comment
    fun <V> Flow<List<V>>.eachIndex(): Flow<Patch<V>> =
        this.scan(Pair(emptyList(), emptyList()), ::accumulate).flatMapConcat { (old, new) ->
            val oldSize = old.size
            val newSize = new.size
            if (oldSize < newSize) flowOf<Patch<V>>(Patch.InsertMany(new.subList(oldSize, newSize).reversed(), oldSize))
            else if (oldSize > newSize) flowOf<Patch<V>>(Patch.Delete(newSize, (oldSize - newSize)))
            else emptyFlow()
        }

    /**
     * Connects [Event]s to a [Handler].
     *
     * @receiver [Listener] which contains the [Event]
     * @param handler that will handle the fired [Event]
     */
    infix fun <E : Event, X : Element> Listener<E, X>.handledBy(handler: Handler<Unit>) =
        handler.collect(this.events.map { Unit })

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
     * Sets an attribute.
     *
     * @param name to use
     * @param value to use
     */
    fun attr(name: String, value: Flow<String>) {
        mountSingle(job, { childJob ->
            childJob.cancelChildren()
            value
        }) { v, _ -> attr(name, v) }
    }

    /**
     * Sets an attribute.
     *
     * @param name to use
     * @param value to use
     */
    fun <T> attr(name: String, value: T) {
        domNode.setAttribute(name, value.toString())
    }

    /**
     * Sets an attribute.
     *
     * @param name to use
     * @param value to use
     */
    fun <T> attr(name: String, value: Flow<T>) {
        mountSingle(job, { childJob ->
            childJob.cancelChildren()
            value.map { it.toString() }
        }) { v, _ -> attr(name, v) }
    }

    /**
     * Sets an attribute when [value] is true other removes it.
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
     * Sets an attribute when [value] is true other removes it.
     *
     * @param name to use
     * @param value for decision
     * @param trueValue value to use if attribute is set (default "")
     */
    fun attr(name: String, value: Flow<Boolean>, trueValue: String = "") {
        mountSingle(job, { childJob ->
            childJob.cancelChildren()
            value
        }) { v, _ -> attr(name, v, trueValue) }
    }

    /**
     * Sets an attribute from a [List] of [String]s.
     * Therefore it concatenates the [String]s to the final value [String].
     *
     * @param name to use
     * @param values for concatenation
     * @param separator [String] for separation
     */
    fun attr(name: String, values: List<String>, separator: String = " ") {
        domNode.setAttribute(name, values.joinToString(separator))
    }

    /**
     * Sets an attribute from a [List] of [String]s.
     * Therefore it concatenates the [String]s to the final value [String].
     *
     * @param name to use
     * @param values for concatenation
     * @param separator [String] for separation
     */
    fun attr(name: String, values: Flow<List<String>>, separator: String = " ") {
        mountSingle(job, { childJob ->
            childJob.cancelChildren()
            values
        }) { v, _ -> attr(name, v, separator) }
    }

    /**
     * Sets an attribute from a [Map] of [String]s and [Boolean]s.
     * The key inside the [Map] getting only set when the corresponding value
     * is true. Otherwise they get removed from the resulting [String].
     *
     * @param name to use
     * @param values to use
     * @param separator [String] for separation
     */
    fun attr(name: String, values: Map<String, Boolean>, separator: String = " ") {
        domNode.setAttribute(name, values.filter { it.value }.keys.joinToString(separator))
    }

    /**
     * Sets an attribute from a [Map] of [String]s and [Boolean]s.
     * The key inside the [Map] getting only set when the corresponding value
     * is true. Otherwise they get removed from the resulting [String].
     *
     * @param name to use
     * @param values to use
     * @param separator [String] for separation
     */
    fun attr(name: String, values: Flow<Map<String, Boolean>>, separator: String = " ") {
        mountSingle(job, { childJob ->
            childJob.cancelChildren()
            values
        }) { v, _ -> attr(name, v, separator) }
    }

    /**
     * Sets the *class* attribute.
     *
     * @param value [Flow] with [String]
     */
    fun className(value: Flow<String>) {
        attr("class", baseClass?.let { value.map { "$baseClass $it" } } ?: value)
    }

    /**
     * Sets the *class* attribute from a [List] of [String]s.
     *
     * @param values [Flow] with [List] of [String]s
     */
    fun classList(values: Flow<List<String>>) {
        attr("class", (if (baseClass != null) values.map { it + baseClass } else values))
    }


    /**
     * Sets the *class* attribute from a [Map] of [String] to [Boolean].
     * If the value of the [Map]-entry is true, the key will be used inside the resulting [String].
     *
     * @param values [Flow] of [Map] with key to set and corresponding values to decide
     */
    fun classMap(values: Flow<Map<String, Boolean>>) {
        attr("class", if (baseClass != null) values.map { it + (baseClass to true) } else values)
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
     * Sets the *style* attribute from a [List] of [String]s.
     *
     * @param values [List] of [String]s
     */
    fun inlineStyle(values: List<String>) {
        attr("style", values, separator = "; ")
    }

    /**
     * Sets the *style* attribute from a [List] of [String]s.
     *
     * @param values [Flow] with [List] of [String]s
     */
    fun inlineStyle(values: Flow<List<String>>) {
        attr("style", values, separator = "; ")
    }

    /**
     * Sets the *style* attribute from a [Map] of [String] to [Boolean].
     * If the value of the [Map]-entry is true, the key will be used inside the resulting [String].
     *
     * @param values [Map] with key to set and corresponding values to decide
     */
    fun inlineStyle(values: Map<String, Boolean>) {
        attr("style", values, separator = "; ")
    }

    /**
     * Sets the *style* attribute from a [Map] of [String] to [Boolean].
     * If the value of the [Map]-entry is true, the key will be used inside the resulting [String].
     *
     * @param values [Flow] of [Map] with key to set and corresponding values to decide
     */
    fun inlineStyle(values: Flow<Map<String, Boolean>>) {
        attr("style", values, separator = "; ")
    }
}
