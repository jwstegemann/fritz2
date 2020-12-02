import dev.fritz2.binding.RootStore
import dev.fritz2.binding.storeOf
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.identification.uniqueId
import dev.fritz2.lenses.Lens
import dev.fritz2.lenses.buildLens
import dev.fritz2.lenses.format
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import dev.fritz2.kitchenSink.model.*
import dev.fritz2.styling.params.AlignItemsValues
import kotlinx.coroutines.flow.combine

val defaultList = listOf(
    Person(
        uniqueId(),
        1,
        "Mr. John Doe",
        LocalDate.parse("1970-01-01"),
        "john.doe@example.com",
        "13245",
        "8445645",
        Address("Street", "1", "12345", "City")
    ),
    Person(
        uniqueId(),
        2,
        "Mrs. Jane Doe",
        LocalDate.parse("2000-11-11"),
        "jane.doe@example.com",
        "856715",
        "84456 - 131 - 2345",
        address = Address("Avenue", "42", "78945", "Hometown")
    ),
    Person(
        uniqueId(),
        3,
        "Dr.  Jean Dupont",
        LocalDate.parse("1990-09-01"),
        "jean.dupint@exemple.fr",
        "986 - 4 45",
        "84456 - 131 - 2345",
        address = Address("Rue de bleu", "13", "FR - 56473", "Ville")
    ),
    Person(
        uniqueId(),
        4,
        "Sir  Juan Pérez",
        LocalDate.parse("1980-12-06"),
        "adam@ejemplo.es",
        "897 / 123",
        "+45 2345",
        address = Address("Was Dazdann", "75", "85236", "Ciudad")
    ),
    Person(
        uniqueId(),
        5,
        "Sir  Juan Pérez",
        LocalDate.parse("1980-12-01"),
        "juan.perez@ejemplo.es",
        "897 / 123",
        "+45 2345",
        address = Address("El Caminos", "75", "85236", "Ciudad")
    )
)


val _idLens = buildLens("_id", Person::_id) { p, v -> p.copy(_id = v) }
val personIdLens = buildLens("id", Person::id) { p, v -> p.copy(id = v) }
val fullNameLens = buildLens("fullName", Person::fullName) { p, v -> p.copy(fullName = v) }
val birthdayLens = buildLens("birthday", Person::birthday) { p, v -> p.copy(birthday = v) }
val emailLens = buildLens("email", Person::email) { p, v -> p.copy(email = v) }
val mobileLens = buildLens("mobile", Person::mobile) { p, v -> p.copy(mobile = v) }
val phoneLens = buildLens("phone", Person::phone) { p, v -> p.copy(phone = v) }


val personAddressLens = buildLens("address", Person::address) { p, v -> p.copy(address = v) }
val streetLens = buildLens("street", Address::street) { p, v -> p.copy(street = v) }
val houseNumberLens = buildLens("houseNumber", Address::houseNumber) { p, v -> p.copy(houseNumber = v) }
val postalCodeLens = buildLens("postalCode", Address::postalCode) { p, v -> p.copy(postalCode = v) }
val cityLens = buildLens("city", Address::city) { p, v -> p.copy(city = v) }


object TableStore : RootStore<List<Person>>(defaultList, "personData") {
    val remove = handle<Person> { list, toDelete ->
        list.filter { it != toDelete }
    }
}

object Formats {
    val intFormat: Lens<Int, String> = format(
        parse = { it.toInt() },
        format = { it.toString() }
    )

    val dateFormat: Lens<LocalDate, String> = format(
        parse = { LocalDate.parse(it) },
        format = {
            "${it.dayOfMonth.toString().padStart(2, '0')}.${
                it.monthNumber.toString().padStart(2, '0')
            }.${it.year}"
        }
    )
}


@ExperimentalCoroutinesApi
fun RenderContext.tableDemo(): Div {


    defaultList.sortedWith(compareBy { it.fullName })

    return contentFrame {
        showcaseHeader("Tables")

        val selectedStore = object : RootStore<List<Person>>(emptyList()) {

            val add = handle<Person> { list, selected ->
                if (!list.contains(selected)) {
                    list + selected
                } else {
                    list
                }
            }

            val toggle = handle<Person> { list, item ->
                console.info(item)
                if (!list.contains(item)) {
                    list + item
                } else {
                    list.filter { it != item }
                }
            }

            val clearList = handle {
                emptyList()
            }

        }

        val selectionModeStore = storeOf(TableComponent.Companion.SelectionMode.NONE)
        componentFrame {
            lineUp {
                items {
                    TableComponent.Companion.SelectionMode.values().toList().map { mode ->
                        clickButton { text(mode.toString()) }.events.map { mode } handledBy selectionModeStore.update
                    }
                }
            }
        }

        selectedStore.data.render { list ->
            paragraph { +"Aktuell sind ${list.size} Zeilen ausgewählt!" }
        }



        table<Person, Int>(rowIdProvider = Person::id) {
            caption(selectionModeStore.data.map { mode ->
                "Table with \"${mode.name}\" Selection Mode "
            })
            tableStore(TableStore)
            selectedRows(selectedStore.data)
            selectedAllRowEvents = selectedStore.update
            selectedRowEvent = selectedStore.toggle
            selectionMode(selectionModeStore.data)

            defaultThStyle {
                {
                    background { color { "#1fd257" } }
                }
            }

            columns {
                column("ID") {
                    lens { personIdLens + Formats.intFormat }
                    width { minmax { "80px" } }
                }
                column("Name") {
                    lens { fullNameLens }
                    width { minmax { "1.33fr" } }
                }
                column("Birthday") {
                    lens { birthdayLens + Formats.dateFormat }
                    width { minmax { "120px" } }
                    styling {
                        color { danger }
                    }
                    sortBy {
                        compareBy { person ->
                            person.birthday
                        }
                    }
                }
                column {
                    // lens can be omitted! It's purely optional and totally ok to have columns that hide its relation to
                    // the data from the table itself!
                    // ``header`` oder ``head``?
                    header {
                        title { "Address" }
                        styling {
                            background { color { "purple" } }
                            fontWeight { bold }
                        }
                        content { config ->
                            +config.headerName
                            icon { fromTheme { fritz2 } }
                        }
                    }
                    width { max { "2fr" } }
                    content { ctx, _, rowStore ->
                        rowStore?.let { person ->
                            val street = person.sub(personAddressLens + streetLens)
                            val houseNumber = person.sub(personAddressLens + houseNumberLens)
                            val postalCode = person.sub(personAddressLens + postalCodeLens)
                            val city = person.sub(personAddressLens + cityLens)
                            ctx.apply {
                                street.data.combine(houseNumber.data) { s, h ->
                                    "$s $h"
                                }.combine(postalCode.data) { a, p ->
                                    "$a ,$p"
                                }.combine(city.data) { a, c ->
                                    "$a $c"
                                }.asText()
                            }

                        }
                    }
                    sortBy {
                        compareBy<Person> { person ->
                            person.address.city
                        }.thenBy { person ->
                            person.address.street
                        }
                    }
                }
                column("Phone") {
                    lens { phoneLens }
                    // TODO: Ugly -> Enum must be receiver; but how?
                    sorting { TableComponent.Companion.Sorting.DISABLED }
                }
                column("Mobile") { lens { mobileLens } }
                column("E-Mail") { lens { emailLens } }
            }
            sorter = NaiveSorter()
        }
    }
}