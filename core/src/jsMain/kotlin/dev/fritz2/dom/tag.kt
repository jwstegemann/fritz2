package dev.fritz2.dom

import dev.fritz2.binding.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.MultipleRootElementsException
import dev.fritz2.dom.html.HtmlElements
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
import org.w3c.dom.Node
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
 * @property id the DOM-id of the element to be created
 * @property baseClass a static base value for the class-attribute.
 * All dynamic values for this attribute will be concatenated to this base-value.
 * @property job used for launching coroutines in
 * @property domNode the [Element]-instance that is wrapped by this [Tag]
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
) : WithDomNode<E>, WithComment<E>, WithEvents<E>(), HtmlElements {

    /**
     * Creates the content of the [Tag] and appends it as a child to the wrapped [Element].
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
     * Renders the data of a [Flow] as [Tag]s to the DOM.
     *
     * @receiver [Flow] containing the data
     * @param content [HtmlElements] for rendering the data to the DOM
     */
    fun <V> Flow<V>.render(content: HtmlElements.(V) -> Unit) {
        val newJob = Job(job)
        mountDomNodeList(job, domNode, this.map { data ->
            newJob.cancelChildren()
            dev.fritz2.dom.html.render(newJob) {
                content(data)
            }
        })
    }

    /**
     * Renders the data of a [Flow] as [Tag]s to the DOM.
     * It should only create one root [Tag] like a [Div] otherwise a
     * [MultipleRootElementsException] will be thrown.
     *
     * @receiver [Flow] containing the data
     * @param preserveOrder use a placeholder to keep the rendered [Tag]s in order with static [Tag]s at
     * the same level (default true)
     * @param content [HtmlElements] for rendering the data to the DOM
     */
    fun <V> Flow<V>.renderElement(
        preserveOrder: Boolean = true,
        content: HtmlElements.(V) -> RenderContext
    ) {
        val newJob = Job(job)

        val upstream = this.map { data ->
            newJob.cancelChildren()
            dev.fritz2.dom.html.renderElement(newJob) {
                content(data)
            }
        }

        if (preserveOrder) mountDomNode(job, domNode, upstream)
        else mountDomNodeUnordered(job, domNode, upstream)
    }

    /**
     * Accumulates a [Pair] and a [List] to a new [Pair] of [List]s
     *
     * @param accumulator [Pair] of two [List]s
     * @param newValue new [List] to accumulate
     */
    private fun <T> accumulate(
        accumulator: Pair<List<T>, List<T>>,
        newValue: List<T>
    ): Pair<List<T>, List<T>> = Pair(accumulator.second, newValue)


    /**
     * Renders each element of a [List].
     * Internally the [Patch]es are determined using Myer's diff-algorithm.
     * This allows the detection of moves. Keep in mind, that no [Patch] is derived,
     * when an element stays the same, but changes it's internal values.
     *
     * @param idProvider function to identify a unique entity in the list
     * @param content [HtmlElements] for rendering the data to the DOM
     */
    fun <V, I> Flow<List<V>>.renderEach(
        idProvider: IdProvider<V, I>,
        content: HtmlElements.(V) -> RenderContext
    ) {
        val jobs = mutableMapOf<Node, Job>()
        mountDomNodePatch(job, domNode,
            this.scan(Pair(emptyList(), emptyList()), ::accumulate).map { (old, new) ->
                Myer.diff(old, new, idProvider).map { patch ->
                    patch.map(job) { value, newJob ->
                        dev.fritz2.dom.html.renderElement(newJob) {
                            content(value)
                        }.also {
                            jobs[it.domNode] = newJob
                        }
                    }
                }
            }) { node ->
            val job = jobs.remove(node)
            if (job != null) job.cancelChildren()
            else console.error("could not cancel renderEach-jobs!")
        }
    }


    /**
     * Renders each element of a [List].
     * Internally the [Patch]es are determined using instance comparison.
     * This allows the detection of moves. Keep in mind, that no [Patch] is derived,
     * when an element stays the same, but changes it's internal values.
     *
     * @param content [HtmlElements] for rendering the data to the DOM
     */
    fun <V> Flow<List<V>>.renderEach(
        content: HtmlElements.(V) -> RenderContext
    ) {
        val jobs = mutableMapOf<Node, Job>()
        mountDomNodePatch(job, domNode,
            this.scan(Pair(emptyList(), emptyList()), ::accumulate).map { (old, new) ->
                Myer.diff(old, new).map { patch ->
                    patch.map(job) { value, newJob ->
                        dev.fritz2.dom.html.renderElement(newJob) {
                            content(value)
                        }.also {
                            jobs[it.domNode] = newJob
                        }
                    }
                }
            }) { node ->
            val job = jobs.remove(node)
            if (job != null) job.cancelChildren()
            else console.error("could not cancel renderEach-jobs!")
        }
    }

    /**
     * Renders each element of a [Store]s [List] content.
     * Internally the [Patch]es are determined using Myer's diff-algorithm.
     * This allows the detection of moves. Keep in mind, that no [Patch] is derived,
     * when an element stays the same, but changes it's internal values.
     *
     * @param idProvider function to identify a unique entity in the list
     * @param content [HtmlElements] for rendering the data to the DOM
     */
    fun <V, I> RootStore<List<V>>.renderEach(
        idProvider: IdProvider<V, I>,
        content: HtmlElements.(SubStore<List<V>, List<V>, V>) -> RenderContext
    ) {
        val jobs = mutableMapOf<Node, Job>()

        mountDomNodePatch(job, domNode,
            this.data.scan(Pair(emptyList(), emptyList()), ::accumulate).map { (old, new) ->
                Myer.diff(old, new, idProvider).map { patch ->
                    patch.map(job) { value, newJob ->
                        dev.fritz2.dom.html.renderElement(newJob) {
                            content(sub(elementLens(value, idProvider)))
                        }.also {
                            jobs[it.domNode] = newJob
                        }
                    }
                }
            }) { node ->
            val job = jobs.remove(node)
            if (job != null) job.cancelChildren()
            else console.error("could not cancel renderEach-jobs!")
        }
    }

    /**
     * Renders each element of a [Store]s list content.
     * Internally the [Patch]es are determined using the position of an item in the list.
     * Moves cannot be detected that way and replacing an item at a certain position will be treated as a change of the item.
     *
     * @param content [HtmlElements] for rendering the data to the DOM given a [Store] of the list's item-type
     */
    fun <V> RootStore<List<V>>.renderEach(
        content: HtmlElements.(SubStore<List<V>, List<V>, V>) -> RenderContext
    ) {
        val jobs = mutableMapOf<Node, Job>()
        mountDomNodePatch(job, domNode,
            this.data.map { it.withIndex().toList() }.eachIndex().map { patch ->
                listOf(patch.map(job) { (i, _), newJob ->
                    dev.fritz2.dom.html.renderElement(newJob) {
                        content(sub(i))
                    }.also {
                        jobs[it.domNode] = newJob
                    }
                })
            }) { node ->
            val job = jobs.remove(node)
            if (job != null) job.cancelChildren()
            else console.error("could not cancel renderEach-jobs!")
        }
    }

    /**
     * Renders each element of a [Store]s list content.
     * Internally the [Patch]es are determined using Myer's diff-algorithm.
     * This allows the detection of moves. Keep in mind, that no [Patch] is derived,
     * when an element stays the same, but changes it's internal values.
     *
     * @param idProvider function to identify a unique entity in the list
     * @param content [HtmlElements] for rendering the data to the DOM given a [Store] of the list's item-type
     */
    fun <R, P, V, I> SubStore<R, P, List<V>>.renderEach(
        idProvider: IdProvider<V, I>,
        content: HtmlElements.(SubStore<R, List<V>, V>) -> RenderContext
    ) {
        val jobs = mutableMapOf<Node, Job>()
        mountDomNodePatch(job, domNode,
            this.data.scan(Pair(emptyList(), emptyList()), ::accumulate).map { (old, new) ->
                Myer.diff(old, new, idProvider).map { patch ->
                    patch.map(job) { value, newJob ->
                        dev.fritz2.dom.html.renderElement(newJob) {
                            content(sub(elementLens(value, idProvider)))
                        }.also {
                            jobs[it.domNode] = newJob
                        }
                    }
                }
            }) { node ->
            val job = jobs.remove(node)
            if (job != null) job.cancelChildren()
            else console.error("could not cancel renderEach-jobs!")
        }
    }

    /**
     * Renders each element of a [Store]s list content.
     * Internally the [Patch]es are determined using the position of an item in the list.
     * Moves cannot be detected that way and replacing an item at a certain position will be treated as a change of the item.
     *
     * @param content [HtmlElements] for rendering the data to the DOM given a [Store] of the list's item-type
     */
    fun <R, P, V> SubStore<R, P, List<V>>.renderEach(
        content: HtmlElements.(SubStore<R, List<V>, V>) -> RenderContext
    ) {
        val jobs = mutableMapOf<Node, Job>()
        mountDomNodePatch(job, domNode,
            this.data.map { it.withIndex().toList() }.eachIndex().map { patch ->
                listOf(patch.map(job) { (i, _), newJob ->
                    dev.fritz2.dom.html.renderElement(newJob) {
                        content(sub(i))
                    }.also {
                        jobs[it.domNode] = newJob
                    }
                })
            }) { node ->
            val job = jobs.remove(node)
            if (job != null) job.cancelChildren()
            else console.error("could not cancel renderEach-jobs!")
        }
    }

    /**
     * Creates a [Flow] of [Patch]es representing the changes between the current list in a [Flow] and it's predecessor.
     *
     * @receiver [Flow] of lists to create [Patch]es for
     * @return [Flow] of patches
     */
    fun <V> Flow<List<V>>.eachIndex(): Flow<Patch<V>> =
        this.scan(Pair(emptyList(), emptyList()), ::accumulate).flatMapConcat { (old, new) ->
            val oldSize = old.size
            val newSize = new.size
            when {
                oldSize < newSize -> flowOf<Patch<V>>(
                    Patch.InsertMany(
                        new.subList(oldSize, newSize).reversed(),
                        oldSize
                    )
                )
                oldSize > newSize -> flowOf<Patch<V>>(Patch.Delete(newSize, (oldSize - newSize)))
                else -> emptyFlow()
            }
        }

    /**
     * Connects [Event]s to a [Handler].
     *
     * @receiver [Listener] which contains the [Event]
     * @param handler that will handle the fired [Event]
     */
    infix fun <E : Event, X : Element> Listener<E, X>.handledBy(handler: Handler<Unit>) =
        handler.collect(this.events.map { }, job)

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
        mountSingle(job, value) { v, _ -> attr(name, v) }
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
        mountSingle(job, value.map { it.toString() }) { v, _ -> attr(name, v) }
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
        mountSingle(job, value) { v, _ -> attr(name, v, trueValue) }
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
        mountSingle(job, values) { v, _ -> attr(name, v, separator) }
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
        mountSingle(job, values) { v, _ -> attr(name, v, separator) }
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
