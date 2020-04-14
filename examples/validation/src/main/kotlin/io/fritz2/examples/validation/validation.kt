package io.fritz2.examples.validation

import io.fritz2.binding.*
import io.fritz2.dom.*
import io.fritz2.dom.html.HtmlElements
import io.fritz2.dom.html.html
import io.fritz2.utils.createUUID
import io.fritz2.validation.Validation
import io.fritz2.validation.ValidationMessage
import io.fritz2.validation.Validator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.get
import kotlin.browser.document
import kotlin.dom.addClass
import kotlin.dom.removeClass
import kotlin.js.Date

enum class Status(val inputClass: String, val messageClass: String) {
    Valid("is-valid", "valid-feedback"),
    Invalid("is-invalid", "invalid-feedback")
}

data class Message(override val id: String, val status: Status, val text: String): ValidationMessage {
    override fun failed(): Boolean = status > Status.Valid
}

@ExperimentalCoroutinesApi
@FlowPreview
object PersonValidator: Validator<Person, Message, String>() {

    override fun validate(data: Person, metadata: String): List<Message> {
        // working with mutable list here is much more easier
        val msgs = mutableListOf<Message>()
        val idStore = LensIdRoot<Person>()

        // validate name
        if(data.name.trim().isBlank())
            msgs.add(Message(idStore.sub(Person.name).id, Status.Invalid, "Please provide a name"))
        else
            msgs.add(Message(idStore.sub(Person.name).id, Status.Valid, "Good name"))

        // validate the birthday
        when {
            data.birthday == Date("1/1/1900") -> {
                msgs.add(Message(idStore.sub(Person.birthday).id, Status.Invalid, "Please provide a birthday"))
            }
            data.birthday.getFullYear() < 1900 -> {
                msgs.add(Message(idStore.sub(Person.birthday).id, Status.Invalid, "Its a bit to old"))
            }
            data.birthday.getFullYear() > Date().getFullYear() -> {
                msgs.add(Message(idStore.sub(Person.birthday).id, Status.Invalid, "Cannot be in future"))
            }
            else -> {
                val age = Date().getFullYear() - data.birthday.getFullYear()
                msgs.add(Message(idStore.sub(Person.birthday).id, Status.Valid, "Age is $age"))
            }
        }

        //TODO: add more validations

        return msgs
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val personStore = RootStore(Person(createUUID()))

    val name = personStore.sub(Person.name)
    val birthday = personStore.sub(Person.birthday)
    val address = personStore.sub(Person.address)
    val street = address.sub(Address.street)
    val number = address.sub(Address.number)
    val postalCode = address.sub(Address.postalCode)
    val city = address.sub(Address.city)
    val activities = personStore.sub(Person.activities)

    // extend with the Validation interface and provide a PersonValidator
    val listStore = object : RootStore<List<Person>>(emptyList()), Validation<Person, Message, String> {
        override val validator = PersonValidator

        val add: Handler<Person> = handle { list, person ->
            // only update the list when new person is valid
            if(validate(person, "add")) list + person else list
        }
    }

    // adding bootstrap css classes to the validated elements
    listStore.validator.msgs.onEach { msgs ->
        // clean up all input elements
        val inputs = document.getElementsByClassName("form-control")
        for (i in 0..inputs.length) {
            val input = inputs[i]
            input?.removeClass(Status.Invalid.inputClass, Status.Valid.inputClass)
        }
        // clean up all input messages
        val messages = document.getElementsByClassName("message")
        for (i in 0..messages.length) {
            val message = messages[i]
            message?.removeClass(Status.Invalid.messageClass, Status.Valid.messageClass)
            message?.textContent = ""
        }

        // add messages to input groups
        for (msg in msgs) {
            val element = document.getElementById(msg.id)
            element?.addClass(msg.status.inputClass)
            val message = document.getElementById("${msg.id}-message")
            message?.addClass(msg.status.messageClass)
            message?.textContent = msg.text
        }
    }.watch()

    // helper method for creating form-groups for text input
    fun <X, Y> HtmlElements.stringInput(
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

                subStore.update <= changes.values()
            }
            div("message", id = "${subStore.id}-message") { }
        }
    }

    // helper method for creating checkboxes for activities
    fun activityCheckbox(activity: SubStore<Person, List<Activity>, Activity>): Tag<HTMLDivElement> {
        val name = activity.sub(Activity.name)
        val like = activity.sub(Activity.like)

        return html {
            div("form-check form-check-inline") {
                input("form-check-input", id = activity.id) {
                    type = const("checkbox")
                    checked = like.data

                    like.update <= changes.states()
                }
                label("form-check-label", `for` = activity.id) {
                    name.data.bind()
                }
            }
        }
    }

    html {
        div {
            h4 { text("Person") }
            stringInput("Name", name)

            //birthday
            div("form-group") {
                label(`for` = birthday.id) {
                    text("Birthday")
                }
                input("form-control", id = birthday.id) {
                    valueAsDate = birthday.data
                    type = const("date")

                    birthday.update <= changes.valuesAsDate()
                }
                div("message", id = "${birthday.id}-message") { }
            }

            div("form-row") {
                stringInput("Street", street, extraClass = "col-md-6")
                stringInput("House Number", number, extraClass = "col-md-6")
            }
            div("form-row") {
                stringInput("Postal Code", postalCode, extraClass = "col-md-6")
                stringInput("City", city, extraClass = "col-md-6")
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
                                td { text(person.birthday?.toDateString() ?: "") }
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