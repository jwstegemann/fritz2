package dev.fritz2.binding

import dev.fritz2.binding.Payload.Key
import dev.fritz2.dom.HtmlTagMarker

/**
 * Marks a class that it has [Payload] which can be transferred for adding/receiving additional information.
 */
interface WithPayload {
    val payload: Payload
}

/**
 * Contains any type of data which consists of a [Key] and a corresponding value object.
 */
value class Payload(private val entries: HashMap<Key<*>, Any> = hashMapOf()) {

    /**
     * Creates a new [Payload] instance from a given one.
     */
    constructor(parent: Payload) : this(HashMap(parent.entries))

    /**
     * Key for setting and receiving entries in the [Payload]
     */
    open class Key<T: Any>(val name: String)

    /**
     * Sets a new key-value-pair to the [Payload].
     */
    internal operator fun <T: Any> set(key: Key<T>, value: T) { entries[key] = value }

    /**
     * Receives a key-value-par from the [Payload].
     */
    operator fun <T: Any> get(key: Key<T>): T? = entries[key]?.unsafeCast<T>()

    /**
     * Returns all containing [Key]s from the [Payload].
     */
    val keys: Set<Key<*>> get() = entries.keys

    /**
     * Gives the number of stores key-value-pairs.
     */
    val size: Int get() = entries.size

    /**
     * Checks if the [Payload] contains the given key.
     */
    fun <T: Any> contains(key: Key<T>) = entries.contains(key)

    /**
     * Removes the specified key and its corresponding value from the [Payload].
     *
     * @return the previous value associated with the key, or null if the key was not present in the [Payload].
     */
    fun <T: Any> remove(key: Key<T>) = entries.remove(key)

    /**
     * Formats the [Payload] to a valid JSON string for printing or using it inside Javascript.
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
     * Returns an Iterator over the entries in the [Payload].
     */
    operator fun iterator() = entries.toMap().iterator()
}

/**
 * Context for setting new entries to a [Payload] object.
 */
@HtmlTagMarker
class PayloadContext(parent: Payload) {
    val payload = Payload(parent)

    /**
     * Sets a new key-value-pair to the [Payload].
     */
    fun <T: Any> set(key: Payload.Key<T>, value: T) {
        payload[key] = value
    }
}

/**
 * Creates a [Payload.Key] for using it in [Payload].
 */
inline fun <reified T: Any> keyOf(name: String? = null): Payload.Key<T> =
    Payload.Key(name ?: T::class.simpleName ?: "unknown")