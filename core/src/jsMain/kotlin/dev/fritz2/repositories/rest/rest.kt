package dev.fritz2.repositories.rest

import dev.fritz2.remote.Request
import dev.fritz2.remote.getBody
import dev.fritz2.remote.http
import dev.fritz2.repositories.EntityRepository
import dev.fritz2.repositories.QueryRepository
import dev.fritz2.repositories.Resource
import org.w3c.fetch.Response


/**
 * provides crud-functions for REST-API to a defined [Resource]
 *
 * @param resource definition of the [Resource]
 * @param url base-url of the REST-API
 * @param contentType to be used by the REST-API
 */
fun <T, I> restEntity(
    resource: Resource<T, I>,
    url: String,
    contentType: String = "application/json; charset=utf-8"
): EntityRepository<T, I> =
    RestEntity(resource, contentType, http(url))

/**
 * provides crud-functions for REST-API to a defined [Resource]
 *
 * @param resource definition of the [Resource]
 * @param remote base [Request] to be used by all subsequent requests. Use it to configure authentication, etc.
 * @param contentType to be used by the REST-API
 */
fun <T, I> restEntity(
    resource: Resource<T, I>,
    remote: Request,
    contentType: String = "application/json; charset=utf-8"
): EntityRepository<T, I> =
    RestEntity(resource, contentType, remote)

/**
 * provides crud-functions for REST-API to a defined [Resource]
 *
 * @param resource definition of the [Resource]
 * @param url base-url of the REST-API
 * @param contentType to be used by the REST-API
 * @param remote base [Request] to be used by all subsequent requests. Use it to configure authentication, etc.
 */
class RestEntity<T, I>(
    private val resource: Resource<T, I>,
    val contentType: String,
    val remote: Request
) : EntityRepository<T, I> {

    /**
     * loads an entity by a get request to [resource].url/{id}
     *
     * @param entity current entity (before load)
     * @param id of the entity to load
     * @return the entity (identified by [id]) loaded
     */
    override suspend fun load(entity: T, id: I): T =
        resource.serializer.read(
            remote.accept(contentType).get(resource.serializeId(id))
                .getBody()
        )

    /**
     * sends a post-(for add) or a put-(for update) request to [resource].url/{id} with the serialized entity in it's body.
     * The [Resource.emptyEntity] of [resource] is used to determine if it should add or updated
     *
     * @param entity entity to save
     * @return the added or saved entity
     */
    override suspend fun addOrUpdate(entity: T): T =
        remote.contentType(contentType)
            .body(resource.serializer.write(entity)).run {
                if (resource.idProvider(entity) == resource.idProvider(resource.emptyEntity)) {
                    resource.serializer.read(
                        accept(contentType).post().getBody()
                    )
                } else {
                    put(resource.serializeId(resource.idProvider(entity)))
                    entity
                }
            }

    /**
     * deletes an entity by a delete-request to [resource].url/{id}
     *
     * @param entity entity to delete
     * @return the emptyEntity defined at [resource]
     */
    override suspend fun delete(entity: T): T {
        remote.delete(resource.serializeId(resource.idProvider(entity)))
        return resource.emptyEntity
    }
}


/**
 * provides services to deal with queries for REST-API to a defined [Resource]
 *
 * @param resource definition of the [Resource]
 * @param url base-url of the REST-API
 * @param contentType to be used by the REST-API
 * @param buildQuery function to build a [Request] for a given object defining the query
 */
fun <T, I, Q> restQuery(
    resource: Resource<T, I>,
    url: String,
    contentType: String = "application/json; charset=utf-8",
    buildQuery: suspend Request.(Q) -> Response = { accept(contentType).get() }
): QueryRepository<T, I, Q> = RestQuery(resource, contentType, http(url), buildQuery)

/**
 * provides services to deal with queries for REST-API to a defined [Resource]
 *
 * @param resource definition of the [Resource]
 * @param remote base [Request] to be used by all subsequent requests. Use it to configure authentication, etc.
 * @param contentType to be used by the REST-API
 * @param buildQuery function to build a [Request] for a given object defining the query
 */
fun <T, I, Q> restQuery(
    resource: Resource<T, I>,
    remote: Request,
    contentType: String = "application/json; charset=utf-8",
    buildQuery: suspend Request.(Q) -> Response = { accept(contentType).get() }
): QueryRepository<T, I, Q> = RestQuery(resource, contentType, remote, buildQuery)

/**
 * provides services to deal with queries for REST-API to a defined [Resource]
 *
 * @param resource definition of the [Resource]
 * @param url base-url of the REST-API
 * @param contentType to be used by the REST-API
 * @param remote base [Request] to be used by all subsequent requests. Use it to configure authentication, etc.
 * @param buildQuery function to build a [Request] for a given object defining the query
 */
class RestQuery<T, I, Q>(
    private val resource: Resource<T, I>,
    val contentType: String,
    private val remote: Request,
    private inline val buildQuery: suspend Request.(Q) -> Response
) : QueryRepository<T, I, Q> {

    /**
     * queries the resource by sending the request which is build by [buildQuery] using the [query]
     *
     * @param entities current list of entities
     * @param query object defining the query
     * @return result of the query
     */
    override suspend fun query(entities: List<T>, query: Q): List<T> =
        resource.serializer.readList(
            remote
                .buildQuery(query)
                .getBody()
        )

    /**
     * updates given entities in the [entities] list
     * (sending a put-request to the base-url of the [resource] for every updated entity)
     *
     * @param entities current list
     * @param entitiesToUpdate entities which getting updated
     * @return list after update
     */
    override suspend fun updateMany(entities: List<T>, entitiesToUpdate: List<T>): List<T> {
        val request = remote.contentType(contentType)
        val updated: Map<I, T> = (entities + entitiesToUpdate).groupBy { resource.idProvider(it) }
            .filterValues { it.size > 1 }.mapValues { (id, entities) ->
                val entity = entities.last()
                request.body(resource.serializer.write(entity))
                    .put(resource.serializeId(id))
                entity
            }
        return entities.map { updated[resource.idProvider(it)] ?: it }
    }

    /**
     * sends a post-(for add) or a put-(for update) request to [resource].url/{id}
     * with the serialized entity in it's body. The emptyEntity of [resource] is used to
     * determine if it should saved or updated
     *
     * @param entities entity list
     * @param entity entity to add or update
     * @return list after add or update
     */
    override suspend fun addOrUpdate(entities: List<T>, entity: T): List<T> =
        remote.contentType(contentType)
            .body(resource.serializer.write(entity)).run {
                if (resource.idProvider(entity) == resource.idProvider(resource.emptyEntity)) {
                    entities + resource.serializer.read(
                        accept(contentType).post().getBody()
                    )
                } else {
                    put(resource.serializeId(resource.idProvider(entity)))
                    entities.map { if(resource.idProvider(it) == resource.idProvider(entity)) entity else it }
                }
            }

    private suspend fun deleteById(id: I) {
        remote.delete(resource.serializeId(id))
    }

    /**
     * deletes one entity from the list
     *
     * @param entities list before deletion
     * @param id identifies the entity to delete
     * @return list after deletion
     */
    override suspend fun delete(entities: List<T>, id: I): List<T> {
        deleteById(id)
        return entities.filterNot { resource.idProvider(it) == id }
    }

    /**
     * deletes multiple entities from the list
     *
     * @param entities list before deletion
     * @param ids identifies the entities to delete
     * @return list after deletion
     */
    override suspend fun delete(entities: List<T>, ids: List<I>): List<T> {
        ids.forEach {
            deleteById(it)
        }
        return entities.filterNot { ids.contains(resource.idProvider(it)) }
    }
}
