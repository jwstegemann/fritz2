package dev.fritz2.headless.foundation

import dev.fritz2.dom.Tag
import dev.fritz2.dom.afterMount
import dev.fritz2.dom.asElementList
import dev.fritz2.dom.beforeUnmount
import dev.fritz2.dom.html.Keys
import dev.fritz2.dom.html.shortcutOf

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.awaitAnimationFrame
import kotlinx.coroutines.flow.filter
import org.w3c.dom.HTMLElement
import kotlin.math.max

/*
 * The implementation of the focus management (especially the "trap") is heavily inspired and based upon the
 * fantastic [headless-ui project](https://github.com/tailwindlabs/headlessui)
 */

data class FocusOptions(
    /** Focus the first non-disabled element */
    val first: Boolean = false,

    /** Focus the previous non-disabled element */
    val previous: Boolean = false,

    /** Focus the next non-disabled element */
    val next: Boolean = false,

    /** Focus the last non-disabled element */
    val last: Boolean = false,

    /** Wrap tab around */
    val wrapAround: Boolean = false,

    /** Prevent scrolling the focusable elements into view */
    val noScroll: Boolean = false
)

enum class FocusResult {
    /** Something went wrong while trying to focus. */
    Error,

    /** When `Focus.WrapAround` is enabled, going from position `N` to `N+1` where `N` is the last index in the array, then we overflow. */
    Overflow,

    /** Focus was successful. */
    Success,

    /** When `Focus.WrapAround` is enabled, going from position `N` to `N-1` where `N` is the first index in the array, then we underflow. */
    Underflow,

    NoDirection
}

/**
 * Credit: https://stackoverflow.com/a/30753870
 */
internal val focusableSelector = """
              a[href]:not([tabindex='-1']),
              area[href]:not([tabindex='-1']),
              input:not([disabled]):not([tabindex='-1']),
              select:not([disabled]):not([tabindex='-1']),
              textarea:not([disabled]):not([tabindex='-1']),
              button:not([disabled]):not([tabindex='-1']),
              iframe:not([tabindex='-1']),
              [tabindex]:not([tabindex='-1']),
              [contentEditable=true]:not([tabindex='-1'])
        """.trimIndent()

fun getFocusableElements(container: HTMLElement? = document.body) =
    container?.querySelectorAll(focusableSelector)?.asElementList() ?: emptyList()

fun isElementWithinFocusableElements(element: HTMLElement, container: HTMLElement? = document.body) =
    getFocusableElements(container).toSet().contains(element)

fun focusIn(container: HTMLElement, focusOptions: FocusOptions): FocusResult {
    val elements = getFocusableElements(container)
    val active = document.activeElement as HTMLElement

    val direction = if (focusOptions.next || focusOptions.first) Direction.Next
    else if (focusOptions.previous || focusOptions.last) Direction.Previous
    else return FocusResult.NoDirection

    val startIndex = if (focusOptions.next) max(0, elements.indexOf(active)) + 1
    else if (focusOptions.previous) max(0, elements.indexOf(active)) - 1
    else if (focusOptions.first) 0
    else if (focusOptions.last) elements.size - 1
    else return FocusResult.Error

    // TODO: Check if this exist in Kotlin.js already -> seems that not!
    //  There is no overloaded ``focus`` method with a parameter on ``HTMLElement``!
    //val focusOptions = focus.noScroll ? { preventScroll: true } : {}

    var offset = 0
    val total = elements.size
    var next: HTMLElement?
    do {
        if (offset >= total || offset + total <= 0) return FocusResult.Error

        var nextIdx = startIndex + offset
        if (focusOptions.wrapAround) {
            nextIdx = (nextIdx + total) % total
        } else {
            if (nextIdx < 0) return FocusResult.Underflow
            if (nextIdx >= total) return FocusResult.Overflow
        }

        next = elements[nextIdx]

        // Try the focus the next element, might not work if it is "hidden" to the user.
        next.focus()

        // Try the next one in line
        offset += direction.value

    } while (next != document.activeElement)

    // This is a little weird, but let me try and explain: There are a few scenario's
    // in chrome for example where a focused `<a>` tag does not get the default focus
    // styles and sometimes they do. This highly depends on whether you started by
    // clicking or by using your keyboard. When you programmatically add focus `anchor.focus()`
    // then the active element (document.activeElement) is this anchor, which is expected.
    // However in that case the default focus styles are not applied *unless* you
    // also add this tabindex.
    next?.let {
        if (!it.hasAttribute("tabindex")) it.setAttribute("tabindex", "0")
    }

    return FocusResult.Success
}

const val INITIAL_FOCUS_DATA_ATTR = "data-fritz2-initialFocus"

fun setInitialFocus(tag: HTMLElement) {
    tag.setAttribute(INITIAL_FOCUS_DATA_ATTR, "")
}

fun Tag<HTMLElement>.setInitialFocus() {
    attr(INITIAL_FOCUS_DATA_ATTR, "")
}

fun Tag<HTMLElement>.trapFocus(restoreFocus: Boolean = true, setInitialFocus: Boolean = true) {
    // restore focus
    if (restoreFocus) {
        beforeUnmount(document.activeElement) { _, element ->
            (element as HTMLElement).focus()
        }
    }

    // handle initial focus
    if (setInitialFocus) {
        afterMount { _, _ ->
            val active = document.activeElement as HTMLElement
            if (!isElementWithinFocusableElements(active, domNode)) {
                val initialFocus = domNode.querySelector("[$INITIAL_FOCUS_DATA_ATTR]")
                if (initialFocus != null) {
                    if (active != initialFocus) {
                        (initialFocus as HTMLElement).focus()
                    }
                } else {
                    if (focusIn(domNode, FocusOptions(first = true)) == FocusResult.Error) {
                        console.warn("There are no focusable elements inside the focus-trap!")
                    }
                }
            }
        }
    }

    // handle tab key
    keydowns.events.filter { setOf(Keys.Tab, Keys.Shift + Keys.Tab).contains(shortcutOf(it)) } handledBy { event ->
        event.preventDefault()
        focusIn(
            domNode,
            if (shortcutOf(event).shift) FocusOptions(previous = true, wrapAround = true)
            else FocusOptions(next = true, wrapAround = true)
        ).also {
            if (it != FocusResult.Success) {
                console.warn("Focus-trap was not successful!", it)
            }
        }
    }
}

/**
 * Sets focus on the [Tag] it is called on.
 * Continues to try setting the focus until it was successful or the maximum number of retries is reached.
 * Use this function for example to set the focus on an element that just becomes visible.
 * @param maxRetries maximum retries
 */
suspend fun Tag<HTMLElement>.setFocus(maxRetries: Int = 10) {
    for (i in 0 until maxRetries) {
        window.awaitAnimationFrame()
        domNode.focus()
        if (document.activeElement == domNode) break
    }
}

