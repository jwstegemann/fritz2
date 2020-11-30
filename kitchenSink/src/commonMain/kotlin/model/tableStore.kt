package dev.fritz2.kitchenSink.model
import dev.fritz2.lenses.Lenses
import kotlinx.datetime.LocalDate

@Lenses
data class Person (
    val _id: String = "",
    val id: Int = 0,
    val fullName: String = "",
    val birthday:LocalDate = LocalDate(1900, 1, 1),
    val email: String = "",
    val mobile: String = "",
    val phone: String = "",
    val address: Address = Address()
)


@Lenses
data class Address(
    val street: String = "",
    val houseNumber: String = "",
    val postalCode: String = "",
    val city: String = "",
)