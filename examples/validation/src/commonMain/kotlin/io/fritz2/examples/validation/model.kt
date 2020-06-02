package io.fritz2.examples.validation

import com.soywiz.klock.Date
import com.soywiz.klock.DateFormat
import com.soywiz.klock.format
import com.soywiz.klock.parseDate
import io.fritz2.format.Format
import io.fritz2.optics.Lens
import io.fritz2.optics.WithId
import io.fritz2.optics.buildLens

data class Person(
    override val id: String,
    val name: String = "",
    val birthday: Date = Date(1900, 1, 1),
    val address: Address = Address(),
    val activities: List<Activity> = listOf(
        Activity("ac1", "walking"),
        Activity("ac2", "running"),
        Activity("ac3", "meeting friends"),
        Activity("ac4", "playing computer games"),
        Activity("ac5", "programming"),
        Activity("ac6", "to go biking")
    )
) : WithId {

    companion object {
        val id: Lens<Person, String> = buildLens("id", { it.id }, { p, v ->
            p.copy(id = v)
        })

        val name: Lens<Person, String> = buildLens("name", { it.name }, { p, v -> p.copy(name = v) })

        val birthday: Lens<Person, Date> = buildLens("birthday", {
            it.birthday
        }, { p, v -> p.copy(birthday = v) })

        val address: Lens<Person, Address> =
            buildLens("address", { it.address }, { p, v -> p.copy(address = v) })

        val activities: Lens<Person,
                List<Activity>> = buildLens("activities", { it.activities }, { p, v -> p.copy(activities = v) })
    }
}

data class Address(
    val street: String = "",
    val number: String = "",
    val postalCode: String = "",
    val city: String = ""
) {

    companion object {
        val street: Lens<Address, String> = buildLens("street", {
            it.street
        }, { p, v -> p.copy(street = v) })

        val number: Lens<Address, String> = buildLens("number", {
            it.number
        }, { p, v -> p.copy(number = v) })

        val postalCode: Lens<Address, String> = buildLens("postalCode", {
            it.postalCode
        }, { p, v -> p.copy(postalCode = v) })

        val city: Lens<Address, String> = buildLens("city", { it.city }, { p, v -> p.copy(city = v) })
    }
}

data class Activity(
    override val id: String,
    val name: String,
    val like: Boolean = false
) : WithId {

    companion object {
        val id: Lens<Activity, String> = buildLens("id", { it.id }, { p, v ->
            p.copy(id = v)
        })

        val name: Lens<Activity, String> = buildLens("name", { it.name }, { p, v -> p.copy(name = v) })

        val like: Lens<Activity, Boolean> = buildLens("like", {
            it.like
        }, { p, v -> p.copy(like = v) })
    }

}

object Format {
    val date = object : Format<Date> {
        private val formatter: DateFormat = DateFormat("yyyy-MM-dd")
        override fun parse(value: String): Date = formatter.parseDate(value)
        override fun format(value: Date): String = formatter.format(value)
    }
}