package dev.fritz2.examples.masterdetail

import dev.fritz2.core.Id
import dev.fritz2.core.Lenses
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Lenses
@Serializable
data class Person(
    val id: String = Id.next(),
    val name: String = "",
    val age: Int = 0,
    val salary: Int = 0,
    val saved: Boolean = false,
) {
    companion object {
        fun deserialize(source: String): Person = Json.decodeFromString(serializer(), source)
        fun serialize(item: Person): String = Json.encodeToString(serializer(), item)
    }
}
