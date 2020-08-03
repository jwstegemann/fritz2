package dev.fritz2.services.entity

/**
 * defines the interface that should be provided by all services dealing with a single Entity.
 */
interface EntityService<T, I> {

    /**
     * loads an entity
     *
     * @param entity current entity (before load)
     * @param id of the entity to load
     * @return the entity (identified by [id]) loaded
     */
    suspend fun load(entity: T, id: I): T

    /**
     * saves or updates an entity
     *
     * @param entity entity to save
     * @return the new entity after save or update
     */
    suspend fun saveOrUpdate(entity: T): T

    /**
     * deletes an entity
     *
     * @param entity entity to delete
     * @return a new entity after deletion
     */
    suspend fun delete(entity: T): T
}

/**
 * defines the interface that should be provided by all services dealing with a list of Entities.
 */
interface QueryService<T, I, Q> {

    /**
     * runs a query
     *
     * @param entities entity list
     * @param query object defining the query
     * @return result of the query
     */
    suspend fun query(entities: List<T>, query: Q): List<T>

    /**
     * updates all entities in the list
     *
     * @param entities entity list
     * @return list after update
     */
    suspend fun updateAll(entities: List<T>): List<T>

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
