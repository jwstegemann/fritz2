package dev.fritz2.services.localstorage

import dev.fritz2.lenses.IdProvider
import dev.fritz2.serialization.Serializer
import dev.fritz2.services.entity.EntityService
import dev.fritz2.services.entity.QueryService
import org.w3c.dom.get
import kotlin.browser.localStorage
import kotlin.browser.window

/**
 * defines a concrete [LocalResource]
 *
 * @param prefix prefix of all keys of this resource
 * @param idProvider function to provide an id for a given entity
 * @param serializer used to (de-)serialize the entity/response
 * @param emptyEntity an instance of the entity defining an empty state (e.g. after deletion)
 * @param serializeId convert the entities [idProvider] into a [String], default calling [toString]
 */
data class LocalResource<T, I>(
    val prefix: String,
    inline val idProvider: IdProvider<T, I>,
    val serializer: Serializer<T, String>,
    inline val emptyEntity: T,
    inline val serializeId: (I) -> String = { it.toString() }
)


/**
 * provides crud-functions for [localStorage] to deal with a single entity
 *
 * @param resource definition of the [LocalResource]
 */
open class LocalStorageEntityService<T, I>(
    val resource: LocalResource<T, I>
) : EntityService<T, I> {

    /**
     * loads an entity from [localStorage] using prefix and id defined in [resource]
     *
     * @param entity current entity (before load)
     * @param id of the entity to load
     * @return the entity (identified by [id])
     */
    override suspend fun load(entity: T, id: I): T =
        window.localStorage["${resource.prefix}.${resource.serializeId(id)}"]?.let(resource.serializer::read) ?: entity

    /**
     * saves the serialized entity to [localStorage] using prefix and id defined in [resource]
     *
     * @param entity entity to save
     * @return the saved entity
     */
    override suspend fun saveOrUpdate(entity: T): T {
        window.localStorage.setItem(
            "${resource.prefix}.${resource.serializeId(resource.idProvider(entity))}",
            resource.serializer.write(entity)
        )
        return entity
    }

    /**
     * deletes the entity in [localStorage]
     *
     * @param entity entity to delete
     * @return the []emptyEntity defined at [resource]
     */
    override suspend fun delete(entity: T): T {
        window.localStorage.removeItem("${resource.prefix}.${resource.serializeId(resource.idProvider(entity))}")
        return resource.emptyEntity
    }
}


/**
 * provides services to deal with queries to a specific resource (identified by common prefix) in [localStorage]
 *
 * @param resource definition of resource in [localStorage]
 * @param runQuery function to apply a given query to the collection of entities in [localStorage]
 */
open class LocalStorageQueryService<T, I, Q>(
    val resource: LocalResource<T, I>,
    inline val runQuery: (List<T>, Q) -> List<T> = { entities, _ -> entities }

) : QueryService<T, I, Q> {

    /**
     * applies a given query to the collection of entities
     *
     * @param entities current list of entities
     * @param query object defining the query
     * @return result of the query
     */
    @ExperimentalStdlibApi
    override suspend fun query(entities: List<T>, query: Q): List<T> = runQuery(buildList<T> {
        for (index in 0 until localStorage.length) {
            val key = localStorage.key(index)
            if (key != null && key.startsWith(resource.prefix)) {
                add(resource.serializer.read(localStorage[key]!!))
            }
        }
    }, query)

    /**
     * updates all given entities to [localStorage]
     *
     * @param entities entity list to save
     * @return entity list after update
     */
    override suspend fun updateAll(entities: List<T>): List<T> {
        entities.forEach { entity ->
            window.localStorage.setItem(
                "${resource.prefix}.${resource.serializeId(resource.idProvider(entity))}",
                resource.serializer.write(entity)
            )
        }
        return entities
    }

    private fun deleteById(id: I) {
        window.localStorage.removeItem("${resource.prefix}.${resource.serializeId(id)}")
    }

    /**
     * deletes one entity from [localStorage]
     *
     * @param entities list before deletion
     * @param id identifies the entity to delete
     * @return entity list after deletion
     */
    override suspend fun delete(entities: List<T>, id: I): List<T> {
        deleteById(id)
        return entities.filterNot { resource.idProvider(it) == id }
    }

    /**
     * deletes multiple entities from [localStorage]
     *
     * @param entities list before deletion
     * @param ids identifies the entities to delete
     * @return list after deletion
     */
    override suspend fun delete(entities: List<T>, ids: List<I>): List<T> {
        ids.forEach(::deleteById)
        return entities.filterNot { ids.contains(resource.idProvider(it)) }
    }
}