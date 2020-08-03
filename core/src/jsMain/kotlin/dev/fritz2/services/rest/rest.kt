package dev.fritz2.services.rest

import dev.fritz2.lenses.IdProvider
import dev.fritz2.remote.Request
import dev.fritz2.remote.getBody
import dev.fritz2.remote.remote
import dev.fritz2.serialization.Serializer
import dev.fritz2.services.entity.EntityService
import dev.fritz2.services.entity.QueryService
import org.w3c.fetch.Response


/**
 * defines of a concrete [RestResource]
 *
 * @param url base-url of the REST-API
 * @param idProvider function to provide an id for a given entity
 * @param serializer used to (de-)serialize the entity/response
 * @param emptyEntity an instance of the entity defining an empty state (e.g. after deletion)
 * @param contentType to be used by the REST-API
 * @param remote base [Request] to be used by all subsequent requests. Use it to configure authentication, etc.
 * @param serializeId convert the entities [idProvider] into a [String]
 */
data class RestResource<T, I>(
    val url: String,
    inline val idProvider: IdProvider<T, I>,
    val serializer: Serializer<T, String>,
    inline val emptyEntity: T,
    val contentType: String = "application/json; charset=utf-8",
    val remote: Request = remote(url),
    inline val serializeId: (I) -> String = { it.toString() }
)


/**
 * provides crud-functions for REST-API to a defined [RestResource]
 *
 * @param resource definition of the [RestResource]
 */
open class RestEntityService<T, I>(
    val resource: RestResource<T, I>
) : EntityService<T, I> {

    /**
     * loads an entity by a get request to [resource].url/{id}
     *
     * @param entity current entity (before load)
     * @param id of the entity to load
     * @return the entity (identified by [id]) loaded
     */
    override suspend fun load(entity: T, id: I): T =
        resource.serializer.read(
            resource.remote.accept(resource.contentType).get(resource.serializeId(id))
                .getBody()
        )

    /**
     * sends a post-(for save) or a put-(for update) request to [resource].url/{id} with the serialized entity in it's body.
     * The emptyEntity of [resource] is used to determine if it should saved or updated
     *
     * @param entity entity to save
     * @return the saved entity
     */
    override suspend fun saveOrUpdate(entity: T): T =
        resource.remote.contentType(resource.contentType)
            .body(resource.serializer.write(entity)).run {
                if (resource.idProvider(entity) == resource.idProvider(resource.emptyEntity)) {
                    resource.serializer.read(
                        accept(resource.contentType).post().getBody()
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
        resource.remote.delete(resource.serializeId(resource.idProvider(entity)))
        return resource.emptyEntity
    }
}


/**
 * provides services to deal with queries for REST-API to a defined [RestResource]
 *
 * @param resource definition of the [RestResource]
 * @param buildQuery function to build a [Request] for a given object defining the query
 */
open class RestQueryService<T, I, Q>(
    val resource: RestResource<T, I>,
    inline val buildQuery: suspend Request.(Q) -> Response = { accept(resource.contentType).get() }
) : QueryService<T, I, Q> {

    /**
     * queries the resource by sending the request which is build by [buildQuery] using the [query]
     *
     * @param entities current list of entities
     * @param query object defining the query
     * @return result of the query
     */
    override suspend fun query(entities: List<T>, query: Q): List<T> =
        resource.serializer.readList(
            resource.remote
                .buildQuery(query)
                .getBody()
        )

    /**
     * updates all entities in the current list (sending a post-request to the base-url of the [resource])
     *
     * @param entities current list
     * @return list after update
     */
    override suspend fun updateAll(entities: List<T>): List<T> =
        resource.serializer.readList(
            resource.remote
                .contentType(resource.contentType).body(resource.serializer.writeList(entities)).post()
                .getBody()
        )

    private suspend fun deleteById(id: I) {
        resource.remote.delete(resource.serializeId(id))
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
