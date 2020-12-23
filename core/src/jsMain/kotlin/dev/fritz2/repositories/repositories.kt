package dev.fritz2.repositories

import dev.fritz2.lenses.IdProvider
import dev.fritz2.resource.ResourceSerializer


/**
 * defines the interface that is used in the repositories
 *
 * @param idProvider function to provide an id for a given entity
 * @param serializer used to (de-)serialize the entity/response
 * @param serializeId convert the entities [idProvider] into a [String], default calling [toString]
 */
data class Resource<T, I>(
    val idProvider: IdProvider<T, I>,
    val serializer: ResourceSerializer<T>,
    val serializeId: (I) -> String = { it.toString() }
)

/**
 * Gets thrown when load can't find the resource
 * @param id Id of the resource to look for
 * @param throwable source exception if occurs
 */
class ResourceNotFoundException(id: String, throwable: Throwable? = null): Exception("resource with id ($id) not exists", throwable)

/**
 * defines the interface that should be provided by all repositories dealing with a single Entity.
 */
interface EntityRepository<T, I> {

    /**
     * loads an entity
     *
     * @param id of the entity to load
     * @return the entity (identified by [id]) loaded
     * @throws ResourceNotFoundException when resource not found
     */
    suspend fun load(id: I): T

    /**
     * adds or updates an entity
     *
     * @param entity entity to add or to save
     * @return the new entity after add or update
     */
    suspend fun addOrUpdate(entity: T): T

    /**
     * deletes an entity
     *
     * @param entity entity to delete
     * @return a new entity after deletion
     */
    suspend fun delete(entity: T)
}

/**
 * defines the interface that should be provided by all repositories dealing with a list of Entities.
 */
interface QueryRepository<T, I, Q> {

    /**
     * runs a query
     *
     * @param query object defining the query
     * @return result of the query
     */
    suspend fun query(query: Q): List<T>

    /**
     * updates all entities in the list
     *
     * @param entities entity list
     * @param entitiesToUpdate entities to update in entity list
     * @return list after update
     */
    suspend fun updateMany(entities: List<T>, entitiesToUpdate: List<T>): List<T>

    /**
     * adds or updates an entity in the list
     *
     * @param entities entity list
     * @param entity entity to save
     * @return list after add or update
     */
    suspend fun addOrUpdate(entities: List<T>, entity: T): List<T>

    /**
     * delete one entity
     *
     * @param entities entity list
     * @param id of entity to delete
     * @return list after deletion
     */
    suspend fun delete(entities: List<T>, id: I): List<T>

    /**
     * delete multiple entities
     *
     * @param entities entity list
     * @param ids of entities to delete
     * @return list after deletion
     */
    suspend fun delete(entities: List<T>, ids: List<I>): List<T>
}
