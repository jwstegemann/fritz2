package dev.fritz2.components.foundations

import dev.fritz2.binding.RootStore

/**
 * This store can be used for components with an *internal* store that has to deal with a [List] of some type T.
 *
 * Use the [toggle] method to add or remove an selected item from the current selection:
 * ```
 * val selection = MultiSelectionStore<String>()
 * lineUp {
 *     items {
 *         listOf("One", "Two", "Three", "Four", "Five").forEach { value ->
 *             box({
 *                 background { color { info } }
 *             }) {
 *                 +value
 *                 clicks.events.map { value } handledBy selection.toggle
 *                 //                  ^^^^^                       ^^^^^^
 *                 //                  pass current value to the ``toggle`` handler!
 *             }
 *         }
 *     }
 * }
 * div {
 *     +"Selection:"
 *     ul {
 *         selection.data.renderEach { value ->
 *             li { +value }
 *         }
 *     }
 * }
 * ```
 *
 * RFC: Never ever expose the internal store directly to the client side! Only accept values or [Flow]s and return
 * those in order to exchange data with the client!
 */
open class MultiSelectionStore<T> : RootStore<List<T>>(emptyList()) {
    val toggle = this.handleAndEmit<T, List<T>> { selectedRows, new ->
        val newSelection = if (selectedRows.contains(new))
            selectedRows - new
        else
            selectedRows + new
        emit(newSelection)
        newSelection
    }
}

/**
 * This store can be used for components with an *internal* store that has to deal with a single element *selection*
 * from a collection of predefined values (like for a [selectField] or [radioGroup] component)
 *
 * It is based upon the *index* of an item from the list represented by the [Int] type.
 *
 * RFC: Never ever expose the internal store directly to the client side! Only accept values or [Flow]s and return
 * those in order to exchange data with the client!
 */
open class SingleSelectionStore : RootStore<Int?>(null) {
    val toggle = this.handleAndEmit<Int, Int> { _, new ->
        emit(new)
        new
    }
}