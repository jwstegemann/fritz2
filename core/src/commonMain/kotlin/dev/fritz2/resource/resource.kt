package dev.fritz2.resource

import dev.fritz2.lenses.IdProvider

/**
 * defines the interface that is used in the repositories
 *
 * @param idProvider function to provide an id for a given entity
 * @param serializer used to (de-)serialize the entity/response
 * @param emptyEntity an instance of the entity defining an empty state (e.g. after deletion)
 * @param serializeId convert the entities [idProvider] into a [String], default calling [toString]
 */
data class Resource<T, I>(
    val idProvider: IdProvider<T, I>,
    val serializer: ResourceSerializer<T>,
    val emptyEntity: T,
    val serializeId: (I) -> String = { it.toString() }
)

/**
 * defines how to serialize and deserialize an object to/from [String]
 */
interface ResourceSerializer<T> {
    fun write(item: T): String
    fun read(source: String): T
    fun writeList(items: List<T>): String = throw NotImplementedError()
    fun readList(source: String): List<T> = throw NotImplementedError()
}