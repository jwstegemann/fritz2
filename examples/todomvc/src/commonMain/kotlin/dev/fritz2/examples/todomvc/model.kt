package dev.fritz2.examples.todomvc

import dev.fritz2.core.Id
import dev.fritz2.core.IdProvider
import dev.fritz2.core.Lenses
import dev.fritz2.repository.Resource

@Lenses
data class ToDo(
    val id: String = Id.next(),
    val text: String,
    val completed: Boolean = false
) {
    companion object
}

object ToDoResource : Resource<ToDo, String> {
    override val idProvider: IdProvider<ToDo, String> = ToDo::id

    override fun deserialize(source: String): ToDo {
        val split = source.split(';')
        return ToDo(split[0], split[1], split[2].toBoolean())
    }

    override fun deserializeList(source: String): List<ToDo> {
        val split = source.split("|")
        return split.map { deserialize(it) }
    }

    override fun serialize(item: ToDo): String {
        return "${item.id};${item.text};${item.completed}"
    }

    override fun serializeList(items: List<ToDo>): String {
        return items.joinToString("|") { serialize(it) }
    }
}