package dev.fritz2.binding

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.lenses.IdProvider
import dev.fritz2.utils.Myer
import kotlinx.coroutines.flow.*
import org.w3c.dom.Element
import dev.fritz2.dom.html.render as globalRender

/**
 * A [Patch] describes the changes made to a [Seq]
 */
sealed class Patch<out T> {
    /**
     * A [Patch] saying, that a new element has been inserted
     *
     * @param element the new element that has been inserted
     * @param index the element has been inserted at this index
     */
    data class Insert<T>(val element: T, val index: Int) : Patch<T>() {
        /**
         * maps the new element
         *
         * @param mapping defines, how to map the value of the patch
         */
        override fun <R> map(mapping: (T) -> R): Patch<R> = Insert(mapping(element), index)
    }

    /**
     * A [Patch] saying, that a several element have been inserted
     *
     * @param elements the new elements that have been inserted
     * @param index the elements have been inserted at this index
     */
    data class InsertMany<T>(val elements: List<T>, val index: Int) : Patch<T>() {
        /**
         * maps each of the new elements
         *
         * @param mapping defines, how to map the values of the patch
         */
        override fun <R> map(mapping: (T) -> R): Patch<R> = InsertMany(elements.map(mapping), index)
    }
    /**
     * A [Patch] saying, that one or more elements have been deleted
     *
     * @param start the index of the first element, that has been deleted
     * @param count the number of elements, that have to be deleted
     */
    data class Delete<T>(val start: Int, val count: Int = 1) : Patch<T>() {
        /**
         * nothing to be mapped here...
         */
        override fun <R> map(mapping: (T) -> R): Patch<R> = this.unsafeCast<Patch<R>>()
    }

    /**
     * A [Patch] saying, that an element has been moved from one position to another.
     * This is only used on [Seq] with a corresponding [IdProvider].
     *
     * @param from old index of the element
     * @param to new index of the element
     */
    data class Move<T>(val from: Int, val to: Int) : Patch<T>() {
        /**
         * nothing to be mapped here...
         */
        override fun <R> map(mapping: (T) -> R): Patch<R> = this.unsafeCast<Patch<R>>()
    }

    /**
     * a convenience-method, to map the values encapsulated in a [Patch]
     *
     * @param mapping defines, how to map the values of the patch
     */
    abstract fun <R> map(mapping: (T) -> R): Patch<R>
}

/**
 * Defines a sequence of values
 *
 * @param data the upstream-[Flow] defining the current state of the [Seq] by [Patch]es
 */
inline class Seq<T>(val data: Flow<Patch<T>>) {

    /**
     * convenience-method to easily map each value in the [Seq]
     */
    fun <X> map(mapper: (T) -> X): Seq<X> {
        return Seq(data.map {
            it.map(mapper)
        })
    }

    /**
     * convenience-method to easily map each value in the [Seq] to a [Tag]
     */
    fun <X : Element> render(mapper: HtmlElements.(T) -> Tag<X>): Seq<Tag<X>> {
        return Seq(data.map { patch ->
            patch.map {
                globalRender {
                    mapper(it)
                }
            }
        })
    }
}

private suspend inline fun <T> accumulate(accumulator: Pair<List<T>, List<T>>, newValue: List<T>) =
    Pair(accumulator.second, newValue)


/**
 * Creates a [Seq] from a [Flow] of a [List].
 * Call it for example on the data-[Flow] of your (Sub-)Store.
 * The [Patch]es are determined using Myer's diff-algorithm.
 * Elements with the same id, provided by the [idProvider], are considered the same element.
 * This allows the detection of moves. Keep in mind, that no [Patch] is derived,
 * when an element stays the same, but changes it's internal values.
 *
 * @param [idProvider] to identify the element in the list (i.e. when it's content changes over time)
 */
fun <T, I> Flow<List<T>>.each(idProvider: IdProvider<T, I>): Seq<T> =
    Seq(this.scan(Pair(emptyList<T>(), emptyList<T>()), ::accumulate).flatMapConcat { (old, new) ->
        console.log("#### diffing $old vs. $new \n")

        Myer.diff(old, new, idProvider)
    })

/**
 * Creates a [Seq] from a [Flow] of a [List]
 * Call it for example on the data-[Flow] of your (Sub-)Store.
 * The [Patch]es are determined using Myer's diff-algorithm.
 */
fun <T> Flow<List<T>>.each(): Seq<T> =
    Seq(this.scan(Pair(emptyList<T>(), emptyList<T>()), ::accumulate).flatMapConcat { (old, new) ->
        Myer.diff(old, new)
    })

/**
 * Creates a [Seq] from a [Flow] of a [List]
 * The [Patch]es are determined by the position of elements, so elements are just added or removed it the end.
 * This is only usefull in connection with [Store]s in your rendering.
 */
internal fun <T> Flow<List<T>>.eachIndex(): Seq<T> =
    Seq(this.scan(Pair(emptyList<T>(), emptyList<T>()), ::accumulate).flatMapConcat { (old, new) ->
        val oldSize = old.size
        val newSize = new.size
        if (oldSize < newSize) flowOf<Patch<T>>(Patch.InsertMany(new.subList(oldSize, newSize).reversed(), oldSize))
        else if (oldSize > newSize) flowOf<Patch<T>>(Patch.Delete(newSize, (oldSize - newSize)))
        else emptyFlow<Patch<T>>()
    })


