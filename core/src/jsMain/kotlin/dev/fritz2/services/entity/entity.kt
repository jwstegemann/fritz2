package dev.fritz2.services.entity

import kotlinx.coroutines.channels.SendChannel

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
     * @param channel channel to inform about changes
     * @param entity entity to save
     * @return the entity (identified by [id]) loaded
     */
    suspend fun saveOrUpdate(channel: SendChannel<Unit>, entity: T): T

    /**
     * deletes an entity
     *
     * @param channel channel to inform about changes
     * @param entity entity to delete
     * @return the new entity after deletion
     */
    suspend fun delete(channel: SendChannel<Unit>, entity: T): T
}

/**
 * defines the interface that should be provided by all services dealing with a list of Entities.
 */
interface QueryService<T, I, Q> {

    /**
     * runs a query
     *
     * @param entities current list
     * @param query object defining the query
     * @return result of the query
     */
    suspend fun query(entities: List<T>, query: Q): List<T>

    /**
     * saves the complete list of entities
     *
     * @param entities current list
     * @return list after save
     */
    suspend fun saveAll(entities: List<T>): List<T>

    /**
     * delete on entity
     *
     * @param entities current list
     * @param id of the entity to delete
     * @return list after deletion
     */
    suspend fun delete(entities: List<T>, id: I): List<T>
}
