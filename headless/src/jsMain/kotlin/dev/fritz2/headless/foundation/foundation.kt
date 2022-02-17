package dev.fritz2.headless.foundation

import dev.fritz2.core.ScopeContext
import dev.fritz2.dom.RenderContext

typealias TagFactory<C> = (RenderContext, String?, String?, ScopeContext.() -> Unit, C.() -> Unit) -> C

fun <T> List<T>.rotateNext(element: T): T? = indexOf(element).let {
    when (it) {
        -1 -> null
        size - 1 -> this[0]
        else -> this[it + 1]
    }
}

fun <T> List<T>.rotatePrevious(element: T): T? = indexOf(element).let {
    when (it) {
        -1 -> null
        0 -> this.last()
        else -> this[it - 1]
    }
}

enum class Direction(val value: Int) {
    Previous(-1), Next(1)
}

enum class Orientation {
    Horizontal, Vertical
}
