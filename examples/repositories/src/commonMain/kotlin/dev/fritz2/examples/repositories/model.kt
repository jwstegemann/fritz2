package dev.fritz2.examples.repositories

import dev.fritz2.core.Id
import dev.fritz2.core.IdProvider
import dev.fritz2.core.Lenses
import dev.fritz2.repository.Resource
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Lenses
@Serializable
data class Person(
    val _id: String = Id.next(),
    val name: String = "",
    val age: Int = 0,
    val salary: Int = 0,
    val saved: Boolean = false
) {
    companion object
}

object PersonResource : Resource<Person, String> {
    override val idProvider: IdProvider<Person, String> = Person::_id
    override fun deserialize(source: String): Person = Json.decodeFromString(Person.serializer(), source)
    override fun serialize(item: Person): String = Json.encodeToString(Person.serializer(), item)
    override fun deserializeList(source: String): List<Person> = Json.decodeFromString(ListSerializer(Person.serializer()), source)
    override fun serializeList(items: List<Person>): String = Json.encodeToString(ListSerializer(Person.serializer()), items)
}