package dev.fritz2.repositories.localstorage

import dev.fritz2.repositories.EntityRepository
import dev.fritz2.repositories.QueryRepository
import dev.fritz2.repositories.Resource
import org.w3c.dom.get
import kotlin.browser.localStorage
import kotlin.browser.window

/**
 * provides crud-functions for [localStorage] to deal with a single entity
 *
 * @param resource definition of the [Resource] to use
 * @param prefix prefix used for prepending to the keys
 */
fun <T, I> localStorageEntity(resource: Resource<T, I>, prefix: String): EntityRepository<T, I> =
    LocalStorageEntity(resource, prefix)

/**
 * provides crud-functions for [localStorage] to deal with a single entity
 *
 * @param resource definition of the [Resource] to use
 * @param prefix prefix used for prepending to the keys
 */
class LocalStorageEntity<T, I>(private val resource: Resource<T, I>, private val prefix: String) :
    EntityRepository<T, I> {

    /**
     * loads an entity from [localStorage] using prefix and id defined in [resource]
     *
     * @param entity current entity (before load)
     * @param id of the entity to load
     * @return the entity (identified by [id])
     */
    override suspend fun load(entity: T, id: I): T =
        window.localStorage["${prefix}.${resource.serializeId(id)}"]?.let(resource.serializer::read)
            ?: entity

    /**
     * saves the serialized entity to [localStorage] using prefix and id defined in [resource]
     *
     * @param entity entity to save
     * @return the saved entity
     */
    override suspend fun saveOrUpdate(entity: T): T {
        window.localStorage.setItem(
            "${prefix}.${resource.serializeId(resource.idProvider(entity))}",
            resource.serializer.write(entity)
        )
        return entity
    }

    /**
     * deletes the entity in [localStorage]
     *
     * @param entity entity to delete
     * @return the emptyEntity defined at [resource]
     */
    override suspend fun delete(entity: T): T {
        window.localStorage.removeItem("${prefix}.${resource.serializeId(resource.idProvider(entity))}")
        return resource.emptyEntity
    }

}

/**
 * provides functions to deal with queries to a specific [Resource] in [localStorage]
 *
 * @param resource definition of resource in [localStorage]
 * @param prefix prefix used for prepending to the keys
 * @param runQuery function to apply a given query to the collection of entities in [localStorage]
 */
fun <T, I, Q> localStorageQuery(
    resource: Resource<T, I>,
    prefix: String,
    runQuery: (List<T>, Q) -> List<T> = { entities, _ -> entities }
): LocalStorageQuery<T, I, Q> = LocalStorageQuery(resource, prefix, runQuery)

/**
 * provides functions to deal with queries to a specific [Resource] in [localStorage]
 *
 * @param resource definition of resource in [localStorage]
 * @param prefix prefix used for prepending to the keys
 * @param runQuery function to apply a given query to the collection of entities in [localStorage]
 */
class LocalStorageQuery<T, I, Q>(
    private val resource: Resource<T, I>,
    private val prefix: String,
    private inline val runQuery: (List<T>, Q) -> List<T> = { entities, _ -> entities }
) : QueryRepository<T, I, Q> {

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
            if (key != null && key.startsWith(prefix)) {
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
                "${prefix}.${resource.serializeId(resource.idProvider(entity))}",
                resource.serializer.write(entity)
            )
        }
        return entities
    }

    /**
     * adds or updates a given entity to [localStorage]
     *
     * @param entities entity list
     * @param entity entity to add or update
     * @return list after add or update
     */
    override suspend fun addOrUpdate(entities: List<T>, entity: T): List<T> {
        window.localStorage.setItem(
            "${prefix}.${resource.serializeId(resource.idProvider(entity))}",
            resource.serializer.write(entity)
        )
        var inList = false
        val updatedList = entities.map {
            if(resource.idProvider(it) == resource.idProvider(entity)) {
                inList = true
                entity
            } else it
        }
        return if(inList) updatedList else entities + entity
    }

    private fun deleteById(id: I) {
        window.localStorage.removeItem("${prefix}.${resource.serializeId(id)}")
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