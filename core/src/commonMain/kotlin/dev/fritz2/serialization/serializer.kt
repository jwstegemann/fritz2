package dev.fritz2.serialization

interface Serializer<T, S> {
    fun write(item: T): S
    fun read(msg: S): T

    fun writeList(items: List<T>): S
    fun readList(msg: S): List<T>
}
