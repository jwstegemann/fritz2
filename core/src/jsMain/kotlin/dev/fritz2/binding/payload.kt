package dev.fritz2.binding

import dev.fritz2.dom.HtmlTagMarker

interface WithPayload {
    val payload: Payload
}

value class Payload(private val entries: HashMap<Key<*>, Any> = hashMapOf()) {

    constructor(parent: Payload) : this(HashMap(parent.entries))

    open class Key<T: Any>(val name: String)

    internal operator fun <T: Any> set(key: Key<T>, value: T) { entries[key] = value }

    operator fun <T: Any> get(key: Key<T>): T? = entries[key]?.unsafeCast<T>()

//    operator fun <T: Any> invoke(key: Key<T>): T? = entries[key]?.unsafeCast<T>()

    val keys: Set<Key<*>> get() = entries.keys

    val size: Int get() = entries.size

    fun <T: Any> contains(key: Key<T>) = entries.contains(key)

    fun <T: Any> remove(key: Key<T>) { entries.remove(key) }
}

@HtmlTagMarker
class PayloadContext(parent: Payload) {
    val payload = Payload(parent)
    fun <T: Any> set(key: Payload.Key<T>, value: T) {
        payload[key] = value
    }
}

inline fun <reified T: Any> keyOf(name: String? = null): Payload.Key<T> =
    Payload.Key(name ?: T::class.simpleName ?: "unknown")