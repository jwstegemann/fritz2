package io.fritz2.examples.nestedmodel

import io.fritz2.binding.*
import io.fritz2.dom.Tag
import io.fritz2.dom.html.HtmlElements
import io.fritz2.dom.html.render
import io.fritz2.dom.mount
import io.fritz2.dom.states
import io.fritz2.dom.values
import io.fritz2.utils.createUUID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLDivElement

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val personStore = object : RootStore<Person>(Person(createUUID())) {
        val save = handleAndEmit<Unit, Person> { p ->
            offer(p)
            p
        }
    }

    val name = personStore.sub(Lenses.Person.name)
    val birthday = personStore.sub(Lenses.Person.birthday)
    val address = personStore.sub(Lenses.Person.address)
    val street = address.sub(Lenses.Address.street)
    val number = address.sub(Lenses.Address.number)
    val postalCode = address.sub(Lenses.Address.postalCode)
    val city = address.sub(Lenses.Address.city)
    val activities = personStore.sub(Lenses.Person.activities)

    val listStore = object : RootStore<List<Person>>(emptyList()) {
        val add: SimpleHandler<Person> = handle { list, person ->
            list + person
        }
    }

    //connect the two stores
    personStore.save handledBy listStore.add

    // helper method for creating form-groups from SubStores
    fun <X, Y> HtmlElements.formGroup(
        label: String,
        subStore: SubStore<X, Y, String>,
        inputType: String = "text",
        extraClass: String = ""
    ) {
        div("form-group $extraClass") {
            label(`for` = subStore.id) {
                text(label)
            }
            input("form-control", id = subStore.id) {
                placeholder = const(label)
                value = subStore.data
                type = const(inputType)

                changes.values() handledBy subStore.update
            }
        }
    }

    // helper method for creating checkboxes for activities
    fun activityCheckbox(activity: SubStore<Person, List<Activity>, Activity>): Tag<HTMLDivElement> {
        val activityName = activity.sub(Lenses.Activity.name)
        val activityLike = activity.sub(Lenses.Activity.like)

        return render {
            div("form-check form-check-inline") {
                input("form-check-input", id = activity.id) {
                    type = const("checkbox")
                    checked = activityLike.data

                    changes.states() handledBy activityLike.update
                }
                label("form-check-label", `for` = activity.id) {
                    activityName.data.bind()
                }
            }
        }
    }

    render {
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
                    clicks handledBy personStore.save
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
                        val fullAddress = "${person.address.street} ${person.address.number}, " +
                                "${person.address.postalCode} ${person.address.city}"
                        val selectedActivities = person.activities.filter { it.like }.map { it.name }.joinToString()

                        render {
                            tr {
                                td { text(person.name) }
                                td { text(person.birthday) }
                                td { text(fullAddress) }
                                td { text(selectedActivities) }
                            }
                        }
                    }.bind()
                }
            }
        }
    }.mount("target")

}