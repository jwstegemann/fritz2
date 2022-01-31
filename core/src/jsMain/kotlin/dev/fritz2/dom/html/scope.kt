package dev.fritz2.dom.html

import dev.fritz2.dom.HtmlTagMarker
import dev.fritz2.dom.html.Scope.Key

/**
 * Marks a class that it has [Scope] which can be transferred for adding/receiving additional information.
 *
 * The concept of the [Scope] is to provide a consistent mechanism to pass arbitrary data down the DOM tree from
 * a hierarchical higher node to "unknown" consumer nodes down the tree. This is very important for all kind of
 * dev.fritz2.headless.components (not necessarily fritz2's dev.fritz2.headless.components!), that should adapt to its context. Some higher node places some
 * information tagged by a unique key into the scope and passes this further down to its children, which themselves
 * just passes this scope further down, optionally adding or manipulating the scope for their children on their own.
 * Somewhere down the tree a node can evaluate the scope passed to him and look out for some key it want to react to.
 * If the key is present it can then apply its value or just behave in some specific way different to its default.
 * If there is no key, the node just applies its default behaviour.
 *
 * To give a practical example:
 * Imagine some button component, which normally uses the "primary" color as background. This works fine for most of
 * the time. But now imagine a buttons-bar on the bottom edge of a modal for example to provide the typical buttons
 * like "ok", "cancel", "yes", "no" or alike. This bar uses the primary color as background too, to have a high contrast
 * against the content above. The two dev.fritz2.headless.components do not work well together this way!
 * The user would have to manually apply some other color to the buttons when using them inside the bar, in order to
 * preserve a good contrast to it. To achieve this behaviour automatically, the scope comes to the rescue:
 * The buttons-bar component can define a global scope-key `buttonsBar` by using the [keyOf] function.
 * Then it can add some key-value pair to the scope like `set(buttonsBar, true)` in order
 * to signal all child nodes that they appear within the context of a buttons bar. The button component could be
 * aware of the key and implement some different behaviour concerning the color, if it detects that it is used within
 * a buttons-bar.
 *
 * The scope only changes conformal to the node hierarchy. That is the scope is empty at the top level [render] function
 * call and may be filled or changed by each child. But a change by some node is only propagated to the children of
 * that node. The children of the next siblings of the changing node are not affected and do not see those scope values!
 *
 * Example:
 * ```
 * div { // initial scope -> empty!
 *     val sizes = keyOf<String>("sizes") // normally define scope-keys globally
 *     div(scope = {
 *         set(sizes, "small") // add some key-value to the scope
 *     }) {
 *         // all children will get this scope instance
 *         p {
 *             scope.asDataAttr() // -> { "sizes": "small" }
 *         }
 *         section {
 *             when (scope[sizes]) {
 *                 "small" -> div({ fontSize { "0.8rem" } }) { +"small text" }
 *                 "normal" -> div({ fontSize { "1rem" } }) { +"normal text" }
 *                 "large" -> div({ fontSize { "1.2rem" } }) { +"large text" }
 *                 else -> div { +"no size scope available" }
 *             }
 *         }
 *         // end of children
 *     }
 *     // next sibling -> only parent scope available, which is empty!
 *     p {
 *         scope.asDataAttr() // -> {}
 *     }
 * }
 * ```
 *
 * It is intentional that the key is not tied to some component or restricted in any other way.
 * A client should strive for a key management, that is driven by the "producing" node, not the "consuming" one!
 * That means one should prefer to encode that some specific context now exist or that some value is now available,
 * instead of setting a client node tailored rule. This enables more freedom for future usages and adaptions by other
 * consuming dev.fritz2.headless.components.
 *
 * To continue the first example: A buttons-bar component should better not inject some "buttonsColor" into the scope,
 * but better just some "buttonsBar" key without any value (Unit). As a creator you just cannot anticipate all situations
 * and future usage of the buttons-bar component. It might be possible that a client wants to put something different to a
 * button into the bar, that also should react to the context. Then a key (and value) tailored to the button does not
 * make sense anymore.
 *
 * @see Scope
 * @see [dev.fritz2.dom.Tag]
 */
interface WithScope {
    val scope: Scope
}

/**
 * Contains any type of data which consists of a [Key] and a corresponding value object.
 */
value class Scope(private val entries: HashMap<Key<*>, Any> = hashMapOf()) {

    /**
     * Creates a new [Scope] instance from a given one.
     */
    constructor(parent: Scope) : this(HashMap(parent.entries))

    /**
     * Key for setting and receiving entries in the [Scope]
     */
    open class Key<T: Any>(val name: String)

    /**
     * Sets a new key-value-pair to the [Scope].
     */
    internal operator fun <T: Any> set(key: Key<T>, value: T) { entries[key] = value }

    /**
     * Receives a key-value-par from the [Scope].
     */
    operator fun <T: Any> get(key: Key<T>): T? = entries[key]?.unsafeCast<T>()

    /**
     * Returns all containing [Key]s from the [Scope].
     */
    val keys: Set<Key<*>> get() = entries.keys

    /**
     * Gives the number of stored key-value-pairs.
     */
    val size: Int get() = entries.size

    /**
     * Checks if the [Scope] contains the given key.
     */
    fun <T: Any> contains(key: Key<T>) = entries.contains(key)

    /**
     * Removes the specified key and its corresponding value from the [Scope].
     *
     * @return the previous value associated with the key, or null if the key was not present in the [Scope].
     */
    fun <T: Any> remove(key: Key<T>) = entries.remove(key)

    /**
     * Formats the [Scope] to a valid JSON string for printing or using it inside Javascript.
     *
     * Be aware that this is just a key-value formatting, where each payload data is just encoded as [String].
     * So there is no magical serialization for primitives or complex types embedded!
     *
     * Examples:
     * ```
     * // primitive types:
     * {
     *   "boolean": "true"
     *   "integer": "42"
     * }
     *
     * // complex type
     * data class User(val name: String, val active: Boolean)
     * val key: keyOf<User>("user")
     * val user = User("Chris", true)
     * // as JSON
     * {
     *   "user" : "User(name=Chris, active=true)"
     * }
     *
     * ```
     */
    override fun toString(): String = buildString {
        append("{ ")
        var count = 0
        for ((k, v) in entries) {
            if (++count > 1) append(", ")
            append("\"")
            append(k.name)
            append("\" : \"")
            append(v)
            append("\"")
        }
        append(" }")
    }

    /**
     * Returns an Iterator over the entries in the [Scope].
     */
    operator fun iterator() = entries.toMap().iterator()
}

/**
 * Context for setting new entries to a [Scope] object.
 */
@HtmlTagMarker
class ScopeContext(private var current: Scope) {
    val scope get() = current

    /**
     * Sets a new key-value-pair to the [Scope].
     */
    fun <T: Any> set(key: Key<T>, value: T) {
        current = Scope(current)
        current[key] = value
    }
}

/**
 * Creates a [Scope.Key] for using it in [Scope].
 */
inline fun <reified T: Any> keyOf(name: String? = null): Key<T> =
    Key(name ?: T::class.simpleName ?: "unknown")