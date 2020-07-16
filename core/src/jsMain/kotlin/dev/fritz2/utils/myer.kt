package dev.fritz2.utils

import dev.fritz2.binding.Patch
import dev.fritz2.lenses.IdProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

/**
 * Implementation of Myer's diff algorithm on two [List]s creating a [Flow] of [Patch]es.
 */
@ExperimentalStdlibApi
object Myer {

    /**
     * diffs to versions of a [List] of a type implementing [WithId]
     * The definition of an id to identify the same object in both [List]s makes it possible to detect, if an object is moved from one position to another.
     * Also this method does not emit a [Patch] if values within an element change.
     *
     * @param oldList old version of the [List]
     * @param newLis new version of the [List]
     * @return a [Flow] of [Patch]es needed to transform the old list into the new one
     */
    fun <T, I> diff(oldList: List<T>, newList: List<T>, id: IdProvider<T, I>): Flow<Patch<T>> {
        val isSame = { a: T, b: T -> id(a) == id(b) }
        val trace = shortestEdit(oldList, newList, isSame)
        return flow {
            backtrack<T>(trace, oldList, newList, isSame)
        }
    }

    /**
     * diffs to versions of a [List] of a type not implementing [WithId]
     *
     * @param oldList old version of the [List]
     * @param newLis new version of the [List]
     * @return a [Flow] of [Patch]es needed to transform the old list into the new one
     */
    fun <T> diff(oldList: List<T>, newList: List<T>): Flow<Patch<T>> {
        val isSame = { a: T, b: T -> a == b }
        val trace = shortestEdit(oldList, newList, isSame)
        return flow {
            backtrack<T>(trace, oldList, newList, isSame)
        }
    }

    private suspend inline fun <T> FlowCollector<Patch<T>>.backtrack(
        trace: List<CircularArray>,
        oldList: List<T>,
        newList: List<T>,
        isSame: (a: T, b: T) -> Boolean
    ) {
        var x = oldList.size
        var y = newList.size

        var lastPatch: Patch<T>? = null
        for ((d, v) in trace.withIndex().reversed()) {
            val k = x - y

            val prevK = if (k == -d || (k != d && v.get(k - 1) < v.get(k + 1))) {
                k + 1
            } else {
                k - 1
            }

            val prevX = v.get(prevK)
            val prevY = prevX - prevK

            while (x > prevX && y > prevY) {
                x -= 1
                y -= 1
            }

            if (d > 0) {
                if (prevX < x) {
                    val element = oldList[prevX]

                    // try to combine
                    if (lastPatch != null) {
                        // combine adjacent deletes
                        if (lastPatch is Patch.Delete && lastPatch.start == prevX + 1) {
                            lastPatch = Patch.Delete(prevX, lastPatch.count + 1)
                        }
                        // combine directly following insert and delete of same element as move
                        else if (lastPatch is Patch.Insert && isSame(lastPatch.element, element)) {
                            lastPatch = Patch.Move(prevX, lastPatch.index) // - 1)
                        } else {
                            emit(lastPatch)
                            lastPatch = Patch.Delete(prevX, 1)
                        }
                    }
                    //nothing there to combine
                    else {
                        lastPatch = Patch.Delete(prevX, 1)
                    }

                } else if (prevY < y) {
                    val element = newList[prevY]
                    val index = x

                    // try to combine
                    if (lastPatch != null) {
                        // combine adjacent inserts
                        if (lastPatch is Patch.Insert && lastPatch.index == index) {
                            //turn oder of elements!
                            lastPatch = Patch.InsertMany(listOf(lastPatch.element, element), lastPatch.index)
                        } else if (lastPatch is Patch.InsertMany && lastPatch.index == index) {
                            //turn oder of elements!
                            lastPatch = Patch.InsertMany(lastPatch.elements + element, lastPatch.index)
                        }
                        // combine directly following insert and delete of same element as move
                        else if (lastPatch is Patch.Delete && lastPatch.count == 1 && isSame(
                                oldList[lastPatch.start],
                                element
                            )
                        ) {
                            lastPatch = Patch.Move(lastPatch.start, index)
                        } else {
                            emit(lastPatch)
                            lastPatch = Patch.Insert(element, x)
                        }
                    }
                    //nothing there to combine
                    else {
                        lastPatch = Patch.Insert(element, x)
                    }
                }
            }

            x = prevX
            y = prevY
        }

        if (lastPatch != null) emit(lastPatch)
    }

    private inline fun <T> shortestEdit(
        oldList: List<T>,
        newList: List<T>,
        isSame: (a: T, b: T) -> Boolean
    ): List<CircularArray> {
        val max = oldList.size + newList.size

        //init array
        val v = CircularArray(max)
        v.set(1, 0)

        return buildList {
            outerLoop@ for (d in 0..max) {
                add(v.copyOf())
                for (k in -d..d step 2) {
                    //walk right or down?
                    var x = if ((k == -d) || (k != d && v.get(k - 1) < v.get(k + 1))) {
                        v.get(k + 1)
                    } else {
                        v.get(k - 1) + 1
                    }

                    var y = x - k
                    //walk diagonal is possible as far as possible
                    while (x < oldList.size && y < newList.size && isSame(oldList[x], newList[y])) {
                        x += 1
                        y += 1
                    }
                    v.set(k, x)

                    if (x >= oldList.size && y >= newList.size) break@outerLoop
                }
            }
        }

    }
}

internal class CircularArray(private val max: Int, private val buffer: IntArray = IntArray(2 * max + 1)) {
    fun get(index: Int) = buffer[index + max]
    fun set(index: Int, value: Int) = buffer.set(index + max, value)

    fun copyOf(): CircularArray = CircularArray(max, buffer.copyOf())

    override fun toString(): String {
        return buffer.toString()
    }
}
