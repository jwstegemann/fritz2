package dev.fritz2.services.rest

import dev.fritz2.lenses.IdProvider
import dev.fritz2.remote.Request
import dev.fritz2.remote.getBody
import dev.fritz2.remote.remote
import dev.fritz2.services.entity.EntityService
import dev.fritz2.services.entity.QueryService
import dev.fritz2.services.serialization.Serializer
import kotlinx.coroutines.channels.SendChannel
import org.w3c.fetch.Response


/**
 * Definition of a concrete resource
 *
 * @param url base-url of the service
 * @param id function to provide an id for a given entity
 * @param serializer used to (de-)serialize the entity/response
 * @param emptyEntity an instance of the entity defining an empty [Store] (after reset, etc.)
 * @param contentType to be used by the service's requests
 * @param remote base [Request] to be used by all subsequent requests. Use it to configure authentification, etc.
 * @param serializeId convert the entities [id] into a [String]
 */
data class RestResource<T, I>(
    val url: String,
    inline val id: IdProvider<T, I>,
    val serializer: Serializer<T, String>,
    inline val emptyEntity: T,
    val contentType: String = "application/json; charset=utf-8",
    val remote: Request = remote(url),
    inline val serializeId: (I) -> String = { it.toString() }
)


/**
 * provides crud-functions to deal with a single entity
 *
 * @param resource definition of the Rest-resource
 */
open class RestEntityService<T, I>(
    val resource: RestResource<T, I>
) : EntityService<T, I> {

    /**
     * loads an entity by a get request to [resource].url/(id)
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
     * sends a a post-(for save) or put-(for update) request to [resource].url/(id) with the serialized entity in it's body.
     * The emptyEntity of [resource] is used to determine if it should saved or updated
     *
     * @param channel channel to inform about changes
     * @param entity entity to save
     * @return the saved entity
     */
    override suspend fun saveOrUpdate(channel: SendChannel<Unit>, entity: T): T =
        resource.remote.contentType(resource.contentType)
            .body(resource.serializer.write(entity)).run {
                if (resource.id(entity) == resource.id(resource.emptyEntity)) {
                    resource.serializer.read(
                        accept(resource.contentType).post().getBody()
                    )
                } else {
                    put(resource.serializeId(resource.id(entity)))
                    entity
                }
            }.also {
                channel.offer(Unit)
            }

    /**
     * deletes an entity by a delete-request to [resource].url/(id)
     *
     * @param channel channel to inform about changes
     * @param entity entity to delete
     * @return the emptyEntity defined at [resource]
     */
    override suspend fun delete(channel: SendChannel<Unit>, entity: T): T {
        resource.remote.delete(resource.serializeId(resource.id(entity)))
        channel.offer(Unit)
        return resource.emptyEntity
    }
}


/**
 * provides services to deal with queries to a defined Rest-resource
 *
 * @param resource definition of the Rest-resource
 * @param buildQuery function to build a [Request] for a given object defining the query
 */
open class RestQueryService<T, I, Q>(
    val resource: RestResource<T, I>,
    inline val buildQuery: suspend Request.(Q) -> Response = { accept(resource.contentType).get() }
) : QueryService<T, I, Q> {


    /**
     * queries the resource by sending the request build by [buildQuery] using [query]
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
     * saves all entities in the current list (sending a put-request to the base-url of the [resource])
     *
     * @param entities current list
     * @return list after saving
     */
    override suspend fun saveAll(entities: List<T>): List<T> =
        resource.serializer.readList(
            resource.remote
                .contentType(resource.contentType).body(resource.serializer.writeList(entities)).put()
                .getBody()
        )

    /**
     * deletes one entity from the list
     *
     * @param entities list before deletion
     * @param id identifies the entity to delete
     * @return list after deletion
     */
    override suspend fun delete(entities: List<T>, id: I): List<T> {
        resource.remote.delete(resource.serializeId(id))
        return entities.filterNot { resource.id(it) == id }
    }

}
