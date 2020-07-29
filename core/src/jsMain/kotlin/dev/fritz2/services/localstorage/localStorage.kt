package dev.fritz2.services.localstorage

import dev.fritz2.lenses.IdProvider
import dev.fritz2.services.entity.EntityService
import dev.fritz2.services.entity.QueryService
import dev.fritz2.services.serialization.Serializer
import kotlinx.coroutines.channels.SendChannel
import org.w3c.dom.get
import kotlin.browser.localStorage
import kotlin.browser.window

data class LocalResource<T, I>(
    val prefix: String,
    inline val id: IdProvider<T, I>,
    val serializer: Serializer<T, String>,
    inline val emptyEntity: T,
    inline val serializeId: (I) -> String = { it.toString() }
)


open class LocalStorageEntityService<T, I>(
    val resource: LocalResource<T, I>
) : EntityService<T, I> {

    override suspend fun load(entity: T, id: I): T =
        window.localStorage["${resource.prefix}.${resource.serializeId(id)}"]?.let(resource.serializer::read) ?: entity

    override suspend fun saveOrUpdate(channel: SendChannel<Unit>, entity: T): T {
        window.localStorage.setItem(
            "${resource.prefix}.${resource.serializeId(resource.id(entity))}",
            resource.serializer.write(entity)
        )
        return entity
    }

    override suspend fun delete(channel: SendChannel<Unit>, entity: T): T {
        window.localStorage.removeItem("${resource.prefix}.${resource.serializeId(resource.id(entity))}")
        return resource.emptyEntity
    }
}

open class LocalStorageQueryService<T, I, Q>(
    val resource: LocalResource<T, I>,
    inline val runQuery: (List<T>, Q) -> List<T> = { entities, _ -> entities }

) : QueryService<T, I, Q> {

    @ExperimentalStdlibApi
    override suspend fun query(entities: List<T>, query: Q): List<T> = runQuery(buildList<T> {
        for (index in 0 until localStorage.length) {
            val key = localStorage.key(index)
            if (key != null && key.startsWith(resource.prefix)) {
                add(resource.serializer.read(localStorage[key]!!))
            }
        }
    }, query)

    override suspend fun saveAll(entities: List<T>): List<T> {
        entities.forEach { entity ->
            window.localStorage.setItem(
                "${resource.prefix}.${resource.serializeId(resource.id(entity))}",
                resource.serializer.write(entity)
            )
        }
        return entities
    }

    override suspend fun delete(entities: List<T>, id: I): List<T> {
        window.localStorage.removeItem("${resource.prefix}.${resource.serializeId(id)}")
        return entities.filterNot { resource.id(it) == id }
    }
}