package io.fritz2.utils

import io.fritz2.binding.Patch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

@ExperimentalStdlibApi
object Myer {

    fun <T> diff(oldList: List<T>, newList: List<T>, isSame: (a: T, b: T) -> Boolean): Flow<Patch<T>> {
        val trace = shortestEdit(oldList, newList, isSame)
        return flow<Patch<T>> {
            backtrack<T>(trace, oldList, newList)
        }
    }

    private suspend fun <T> FlowCollector<Patch<T>>.backtrack(
        trace: List<CircularArray>,
        oldList: List<T>,
        newList: List<T>
    ) {
        var x = oldList.size
        var y = newList.size

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
//                console.run { log("d=$d, k=$k | x: $prevX -> $x, | y: $prevY -> $y") }

                if (prevX < x) {
                    val element = oldList[prevX]
//                    console.log("delete $element @ $prevX ")
                    emit(Patch.Delete(prevX, 1))
                } else if (prevY < y) {
                    val element = newList[prevY]
//                    console.log("insert $element before $x")
                    emit(Patch.Insert(element, x))
                }
            }

            x = prevX
            y = prevY
        }

    }

    private fun <T> shortestEdit(
        oldList: List<T>,
        newList: List<T>,
        isSame: (a: T, b: T) -> Boolean
    ): List<CircularArray> {
        val max = oldList.size + newList.size

        //init array
        val v = CircularArray(max)
        v.set(1, 0)

        return buildList<CircularArray> {
            outerLoop@ for (d in 0..max) {
                add(v.copyOf())
                for (k in -d..d step 2) {
                    //console.log("evaluating d=$d, k=$k")

                    //walk right or down?
                    var x = if ((k == -d) || (k != d && v.get(k - 1) < v.get(k + 1))) {
                        //console.log("move down (insert)")
                        v.get(k + 1)
                    } else {
                        //console.log("move right (delete)")
                        v.get(k - 1) + 1
                    }

                    var y = x - k

                    //console.log("moved to ($x,$y)")

                    //walk diagonal is possible as far as possible
                    while (x < oldList.size && y < newList.size && isSame(oldList[x], newList[y])) {
                        x += 1
                        y += 1
                    }

                    //console.log("    -> k = $k, best x = $x")

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
