package io.fritz2.examples.nestedmodel

import io.fritz2.optics.Lenses
import io.fritz2.optics.WithId

@Lenses
data class Person(
    override val id: String,
    val name: String = "",
    val birthday: String = "",
    val address: Address = Address(),
    val activities: List<Activity> = listOf(
        Activity("ac1", "walking"),
        Activity("ac2", "running"),
        Activity("ac3", "meeting friends"),
        Activity("ac4", "playing computer games"),
        Activity("ac5", "programming"),
        Activity("ac6", "to go biking")
    )
) : WithId

@Lenses
data class Address(
    val street: String = "",
    val number: String = "",
    val postalCode: String = "",
    val city: String = ""
)

@Lenses
data class Activity(
    override val id: String,
    val name: String,
    val like: Boolean = false
) : WithId