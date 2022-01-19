package dev.fritz2.headless.foundation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.w3c.dom.get

// TODO: Add all to core -> just possible after the main PRs in fritz2 are merged! Currently it overcomplicates the
//  development of headless stuff!

/**
 * This adapter class just lifts the DOM API [NodeList] type into
 * [Kotlin's collection API](https://kotlinlang.org/docs/collections-overview.html#collection-types).
 *
 * In order to improve the further processing of typical DOM native calls, it makes sense to offer an adapter type.
 *
 * To ease the usage further, there is a factory extension defined on [NodeList] named [asList].
 *
 * Example usage:
 * ```kotlin
 * domnode // some DOM node
 *     .querySelectorAll("") // DOM API's NodeList as result -> cumbersome iterating and processing!
 *     .asList() // create this type via extension factory
 *     .filter { it.hasAttribute("data-myData") } // process the result in some way...
 *     .map { ... }
 * ```
 *
 * @param base the initial [NodeList]
 */
class DomNodeList(private val base: NodeList) : AbstractList<Node>() {
    override val size: Int = base.length

    override fun get(index: Int): Node = base[index]!!
}

/**
 * This extension method creates a [DomNodeList] from a DOM API based [NodeList].
 *
 * @see DomNodeList
 */
fun NodeList.asList() = DomNodeList(this).map { it as HTMLElement }

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
fun <T> T.whenever(condition: Flow<Boolean>) = condition.map { if (it) this else null }

/**
 * This extension method takes a boolean [Flow] that controls the forwarding of an initial flow:
 * If it is `true` the current value will be passed further on the result flow, if it is `false` a `null` will appear
 * instead.
 *
 * @see whenever
 */
fun <T> Flow<T>.whenever(condition: Flow<Boolean>) =
    condition.combine(this) { cond, value -> if (cond) value else null }

