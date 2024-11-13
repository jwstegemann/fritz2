package dev.fritz2.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

/**
 * This extension method takes a boolean [Flow] that controls the forwarding of the initial value:
 * If it is `true` the value will be passed further on the result flow, if it is `false` a `null` will appear instead.
 *
 * This is especially useful for DOM node attributes, that should only appear if a certain condition is true.
 *
 * Take the `aria-controls` attribute as example. This should only be set, if there is an area active / visible
 * to control. Within a dynamic component - like some disclosure based one - the latter is only shown, if a state-flow
 * is `true`:
 * ```kotlin
 * // `open`: Flow<Boolean>
 * button.attr("aria-controls", "panelId".whenever(open))
 * //                                     ^^^^^^^^^^^^^^
 * //                                     if open == true -> result flow provides "panelId" String
 * //                                     if open == false -> result flow provides `null` -> whole attribute is removed
 * ```
 *
 *  @param condition the boolean flow that decides whether to forward [T] or `null`
 */
fun <T> T.whenever(condition: Flow<Boolean>): Flow<T?> = condition.map { if (it) this else null }

/**
 * This extension method takes a boolean [Flow] that controls the forwarding of an initial flow:
 * If it is `true` the current value will be passed further on the result flow, if it is `false` a `null` will appear
 * instead.
 *
 * @see whenever
 */
fun <T> Flow<T>.whenever(condition: Flow<Boolean>): Flow<T?> =
    condition.flatMapLatest { cond -> this.map { value -> if (cond) value else null } }