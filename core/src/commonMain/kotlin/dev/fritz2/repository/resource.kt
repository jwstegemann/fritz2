package dev.fritz2.repository

import dev.fritz2.core.IdProvider

/**
 * interface that has to be implemented in order to use repositories with a given type [T].
 * It also defines how to serialize and deserialize the given type [T] to and from [String].
 * The functions [serializeList] and [deserializeList] must be implemented when using it within a QueryRepository.
 *
 * @property idProvider function to provide an id for a given entity
 */
interface Resource<T, I> {
    val idProvider: IdProvider<T, I>
    /**
     * converts the entity [id] to a [String], default calling [toString] on it.
     *
     * @param id id from [idProvider]
     * @return id as [String]
     */
    fun serializeId(id: I): String = id.toString()

    /**
     * serialize an [item] to [String]
     * @param item item to serialize
     * @return serialized value as [String]
     */
    fun serialize(item: T): String

    /**
     * deserialize a given [String] to [T]
     * @param source deserialized [String]
     * @return deserialized value [T]
     */
    fun deserialize(source: String): T

    /**
     * serialize a [List] of [items] to [String]
     * @param items items to serialize
     * @return serialized value as [String]
     */
    fun serializeList(items: List<T>): String = throw NotImplementedError()

    /**
     * deserialize a given [String] to a [List] of [T]
     * @param source deserialized [String]
     * @return deserialized value [List] of [T]s
     */
    fun deserializeList(source: String): List<T> = throw NotImplementedError()
}