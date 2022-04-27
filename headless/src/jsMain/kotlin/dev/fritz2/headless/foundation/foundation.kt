package dev.fritz2.headless.foundation

import dev.fritz2.core.RenderContext
import dev.fritz2.core.Scope
import dev.fritz2.core.ScopeContext
import dev.fritz2.core.Tag

/**
 * Alias in order to reduce boilerplate code for the awkward signature of a [Tag]-factory of [RenderContext].
 *
 * In order to provide some instance, just refer to one of the existing factories in [RenderContext]:
 * ```kotlin
 * val div: TagFactory<HTMLDivElement> = RenderContext::div
 * ```
 */
typealias TagFactory<C> = (RenderContext, String?, String?, ScopeContext.() -> Unit, C.() -> Unit) -> C

/**
 * Implements a forward cycling through a [List] instance.
 *
 * @param element some element of the list, for that the next one should be found.
 * @return the next element or the first, if called on last. `null` if [element] is not present in the list.
 */
fun <T> List<T>.rotateNext(element: T): T? = indexOf(element).let {
    when (it) {
        -1 -> null
        size - 1 -> this[0]
        else -> this[it + 1]
    }
}

/**
 * Implements a backwards cycling through a [List] instance.
 *
 * @param element some element of the list, for that the previous one should be found.
 * @return the previous element or the last, if called on first. `null` if [element] is not present in the list.
 */
fun <T> List<T>.rotatePrevious(element: T): T? = indexOf(element).let {
    when (it) {
        -1 -> null
        0 -> this.last()
        else -> this[it - 1]
    }
}

/**
 * Expressive model for any Direction with usable value for index manipulation:
 * - [Previous] provides `-1` for adding onto an index
 * - [Next] provides `1` for adding onto an index
 */
enum class Direction(val value: Int) {
    Previous(-1), Next(1)
}

/**
 * Expressive model for the orientation of some (UI) element.
 */
enum class Orientation {
    Horizontal, Vertical
}

val HEADLESS_DEBUG = Scope.keyOf<Boolean>("fritz2HeadlessDebug")

fun addComponentDebugInfo(comment: String, scope: Scope, context: RenderContext) =
    scope[HEADLESS_DEBUG]?.let { if (it) with(context as Tag<*>) { !comment } }