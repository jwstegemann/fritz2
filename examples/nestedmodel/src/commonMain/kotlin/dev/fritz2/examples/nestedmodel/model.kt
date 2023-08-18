package dev.fritz2.examples.nestedmodel

import dev.fritz2.core.Lenses

@Lenses
data class Person(
    val name: String = "",
    val birthday: String = "",
    val address: Address = Address(),
    val activities: List<Activity> = listOf(
        Activity("walking"),
        Activity("running"),
        Activity("meeting friends"),
        Activity("playing computer games"),
        Activity("programming"),
        Activity("to go biking"),
    ),
) {
    companion object
}

@Lenses
data class Address(
    val street: String = "",
    val number: String = "",
    val postalCode: String = "",
    val city: String = "",
) {
    companion object
}

@Lenses
data class Activity(
    val name: String,
    val like: Boolean = false,
) {
    companion object
}
