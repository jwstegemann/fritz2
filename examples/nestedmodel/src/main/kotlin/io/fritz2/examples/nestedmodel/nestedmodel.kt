package io.fritz2.examples.nestedmodel

import io.fritz2.binding.*
import io.fritz2.dom.Tag
import io.fritz2.dom.html.HtmlElements
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.dom.states
import io.fritz2.dom.values
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLDivElement

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val personStore = RootStore(Person(uniqueId()))

    val name = personStore.sub(Lenses.Person.name)
    val birthday = personStore.sub(Lenses.Person.birthday)
    val address = personStore.sub(Lenses.Person.address)
    val street = address.sub(Lenses.Address.street)
    val number = address.sub(Lenses.Address.number)
    val postalCode = address.sub(Lenses.Address.postalCode)
    val city = address.sub(Lenses.Address.city)
    val activities = personStore.sub(Lenses.Person.activities)

    val listStore = object : RootStore<List<Person>>(emptyList()) {
        val add: Handler<Person> = handle { list, person ->
            list + person
        }
    }

    // helper method for creating form-groups from SubStores
    fun <X, Y> HtmlElements.formGroup(label: String,
                                      subStore: SubStore<X, Y, String>,
                                      inputType: String = "text",
                                      extraClass: String = "") {
        div("form-group $extraClass") {
            label {
                text(label)
                `for` = const(subStore.id)
            }
            input("form-control", id = subStore.id) {
                placeholder = const(label)
                value = subStore.data
                type = const(inputType)

                subStore.update <= changes.values()
            }
        }
    }

    fun HtmlElements.activityCheckbox(activity: SubStore<Person, List<Activity>, Activity>): Tag<HTMLDivElement> {
        val name = activity.sub(Lenses.Activity.name)
        val like = activity.sub(Lenses.Activity.like)

        return html {
            div("form-check form-check-inline") {
                input("form-check-input", id = activity.id) {
                    type = const("checkbox")
                    checked = like.data

                    like.update <= changes.states()
                }
                label("form-check-label") {
                    `for` = const(activity.id)

                    name.data.bind()
                }
            }
        }
    }

    html {
        div {
            h4 { text("Person") }
            formGroup("Name", name)
            formGroup("Birthday", birthday, "date")
            div("form-row") {
                formGroup("Street", street, extraClass = "col-md-6")
                formGroup("House Number", number, extraClass = "col-md-6")
            }
            div("form-row") {
                formGroup("Postal Code", postalCode, extraClass = "col-md-6")
                formGroup("City", city, extraClass = "col-md-6")
            }
            div("form-row") {
                div("form-group") {
                    activities.eachStore().map { activity ->
                        activityCheckbox(activity)
                    }.bind()
                }
            }
            div("form-group my-4") {
                button("btn btn-primary") {
                    text("Add")
                    listStore.add <= clicks.events.flatMapMerge { personStore.data }
                }

                button("btn btn-secondary mx-2") {
                    text("Show data")
                    attr("data-toggle", "collapse")
                    attr("data-target", "#showData")
                }
                div("collapse", id = "showData") {
                    div("card card-body") {
                        pre {
                            code {
                                personStore.data.map { JSON.stringify(it, space = 2) }.bind()
                            }
                        }
                    }
                }
            }

            hr("my-4") { }

            table("table") {
                thead("thead-dark") {
                    th { text("Name") }
                    th { text("Birthday") }
                    th { text("Address") }
                    th { text("Activities") }
                }
                tbody {
                    listStore.data.each().map { person ->
                        val address = "${person.address.street} ${person.address.number}, " +
                                "${person.address.postalCode} ${person.address.city}"
                        val activities = person.activities.filter { it.like }.map { it.name }.joinToString()

                        html {
                            tr {
                                td { text(person.name) }
                                td { text(person.birthday) }
                                td { text(address) }
                                td { text(activities) }
                            }
                        }
                    }.bind()
                }
            }
        }
    }.mount("target")

}