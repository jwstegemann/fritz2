package dev.fritz2.examples.todomvc

import dev.fritz2.core.Id
import dev.fritz2.core.Lenses

@Lenses
data class ToDo(
    val id: String = Id.next(),
    val text: String,
    val completed: Boolean = false,
) {
    companion object {
        fun deserialize(source: String): ToDo {
            val split = source.split(';')
            return ToDo(split[0], split[1], split[2].toBoolean())
        }

        fun serialize(item: ToDo): String {
            return "${item.id};${item.text};${item.completed}"
        }
    }
}
