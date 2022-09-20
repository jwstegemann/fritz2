package dev.fritz2.headless.foundation

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.InitialFocus.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.awaitAnimationFrame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent
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

/**
 * Mark some [Tag] with a data-attribute [INITIAL_FOCUS_DATA_ATTR] so that the [trapFocus] function can find
 * this [Tag] and set the initial focus to it.
 *
 * @param tag The target [Tag] that should get the initial focus within a focus-trap
 */
fun setInitialFocus(tag: HTMLElement) {
    tag.setAttribute(INITIAL_FOCUS_DATA_ATTR, "")
}

/**
 * Mark some [Tag] with a data-attribute [INITIAL_FOCUS_DATA_ATTR] so that the [trapFocus] function can find
 * this [Tag] and set the initial focus to it.
 */
fun Tag<HTMLElement>.setInitialFocus() {
    attr(INITIAL_FOCUS_DATA_ATTR, "")
}

/**
 * This type is used to decide which strategy for setting an initial focus is appropriate for [trapFocus] function.
 *
 * There are three values available:
 * - [DoNotSet]
 * - [TryToSet]
 * - [InsistToSet]
 *
 * @param focus This boolean value splits the enum values into two disjoint sets: values which should set a focus
 *              after all (`true`) and those who does not at all (`false`)
 */
enum class InitialFocus(public val focus: Boolean) {

    /**
     * Do not set any focus at all
     */
    DoNotSet(false),

    /**
     * Try to set a focus, but do not print out any warning, if no focusable element could be found and therefor
     * no focus has been set.
     */
    TryToSet(true),

    /**
     * Try to set a focus and print out a warning if no focusable element could be found.
     *
     * This should be chosen for situations, where the availability of a focusable element is expected and strongly
     * related to the functionality, like for modal dialogs. The warning will help to detect missing functionality
     * on the user interface side.
     */
    InsistToSet(true)
}

/**
 * This function enables a so called focus-trap. This enforces the specific behaviour within the receiver [Tag],
 * that switching the focus is only possible on elements that are inside the receiver. No other focusable elements
 * outside the enclosing container will get the focus.
 *
 * This is often useful for components that acts as overlays like modal dialogs or menus.
 *
 * @param restoreFocus sets the focus back to the element that had the focus before the container with the trap was
 *                      entered.
 * @param setInitialFocus will automatically focus the first element of the container or that one, which has been
 *                        tagged by [setInitialFocus] function if the [InitialFocus] value has `focus=true`.
 */
fun Tag<HTMLElement>.trapFocus(restoreFocus: Boolean = true, setInitialFocus: InitialFocus = InitialFocus.TryToSet) {
    trapFocusOn(
        keydowns.filter { setOf(Keys.Tab, Keys.Shift + Keys.Tab).contains(shortcutOf(it)) },
        restoreFocus,
        setInitialFocus
    )
}

/**
 * This variant of [trapFocus] allows to reactively trap a focus based on a conditional [Flow] of [Boolean].
 *
 * @see trapFocus
 *
 * @param condition some boolean [Flow] that will enable the trap only on ``true`` values.
 * @param restoreFocus sets the focus back to the element that had the focus before the container with the trap was
 *                      entered.
 * @param setInitialFocus will automatically focus the first element of the container or that one, which has been
 *                        tagged by [setInitialFocus] function if the [InitialFocus] value has `focus=true`.
 */
fun Tag<HTMLElement>.trapFocusWhenever(
    condition: Flow<Boolean>,
    restoreFocus: Boolean = true,
    setInitialFocus: InitialFocus = InitialFocus.TryToSet
) {
    trapFocusOn(
        keydowns.combine(condition, ::Pair)
            .filter { it.second }
            .map { it.first }
            .filter { setOf(Keys.Tab, Keys.Shift + Keys.Tab).contains(shortcutOf(it)) },
        restoreFocus,
        setInitialFocus
    )
}

private fun Tag<HTMLElement>.trapFocusOn(
    tabEvents: Flow<KeyboardEvent>,
    restoreFocus: Boolean = true,
    setInitialFocus: InitialFocus = InitialFocus.TryToSet
) {
    restoreFocusOnDemand(restoreFocus)
    setInitialFocusOnDemand(setInitialFocus)

    // handle tab key
    tabEvents handledBy { event ->
        event.preventDefault()
        focusIn(
            domNode,
            if (shortcutOf(event).shift) FocusOptions(previous = true, wrapAround = true)
            else FocusOptions(next = true, wrapAround = true)
        ).also {
            if (it != FocusResult.Success && setInitialFocus == InitialFocus.InsistToSet) {
                console.warn("Focus-trap was not successful!", it)
            }
        }
    }
}

private fun Tag<HTMLElement>.restoreFocusOnDemand(restoreFocus: Boolean) {
    if (restoreFocus) {
        beforeUnmount(document.activeElement) { _, element ->
            (element as HTMLElement).focus()
        }
    }
}

private fun Tag<HTMLElement>.setInitialFocusOnDemand(setInitialFocus: InitialFocus) {
    if (setInitialFocus.focus) {
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
                        if (setInitialFocus == InitialFocus.InsistToSet) {
                            console.warn("There are no focusable elements inside the focus-trap!")
                        }
                    }
                }
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

