package dev.fritz2.binding

import dev.fritz2.binding.Scope.Key
import dev.fritz2.dom.HtmlTagMarker

/**
 * Marks a class that it has [Scope] which can be transferred for adding/receiving additional information.
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
     * Gives the number of stores key-value-pairs.
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