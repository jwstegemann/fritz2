package io.fritz2.examples.validation

import com.soywiz.klock.Date
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import io.fritz2.binding.*
import io.fritz2.dom.Tag
import io.fritz2.dom.html.HtmlElements
import io.fritz2.dom.html.render
import io.fritz2.dom.mount
import io.fritz2.dom.states
import io.fritz2.dom.values
import io.fritz2.optics.Lens
import io.fritz2.utils.createUUID
import io.fritz2.validation.Validation
import io.fritz2.validation.ValidationMessage
import io.fritz2.validation.Validator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.get
import kotlin.browser.document
import kotlin.dom.addClass
import kotlin.dom.removeClass

enum class Status(val inputClass: String, val messageClass: String) {
    Valid("is-valid", "valid-feedback"),
    Invalid("is-invalid", "invalid-feedback")
}

data class Message(override val id: String, val status: Status, val text: String) : ValidationMessage {
    override fun failed(): Boolean = status > Status.Valid
}

@ExperimentalCoroutinesApi
@FlowPreview
object PersonValidator : Validator<Person, Message, String>() {

    override fun validate(data: Person, metadata: String): List<Message> {
        // working with mutable list here is much more easier
        val msgs = mutableListOf<Message>()
        val idStore = ModelIdRoot<Person>()

        // validate name
        if (data.name.trim().isBlank())
            msgs.add(Message(idStore.sub(Person.name).id, Status.Invalid, "Please provide a name"))
        else
            msgs.add(Message(idStore.sub(Person.name).id, Status.Valid, "Good name"))

        // validate the birthday
        when {
            data.birthday == Date(1900, 1, 1) -> {
                msgs.add(Message(idStore.sub(Person.birthday).id, Status.Invalid, "Please provide a birthday"))
            }
            data.birthday.year < 1900 -> {
                msgs.add(Message(idStore.sub(Person.birthday).id, Status.Invalid, "Its a bit to old"))
            }
            data.birthday.year > DateTime.now().yearInt -> {
                msgs.add(Message(idStore.sub(Person.birthday).id, Status.Invalid, "Cannot be in future"))
            }
            else -> {
                val age = DateTime.now().yearInt - data.birthday.year
                msgs.add(Message(idStore.sub(Person.birthday).id, Status.Valid, "Age is $age"))
            }
        }

        // check address fields
        val addressId = idStore.sub(Person.address)
        fun checkAddressField(name: String, lens: Lens<Address, String>) {
            val value = lens.get(data.address)
            if (value.trim().isBlank())
                msgs.add(Message(addressId.sub(lens).id, Status.Invalid, "Please provide a $name"))
            else
                msgs.add(Message(addressId.sub(lens).id, Status.Valid, "Ok"))
        }
        checkAddressField("street", Address.street)
        checkAddressField("house number", Address.number)
        checkAddressField("postalcode", Address.postalCode)
        checkAddressField("city", Address.city)

        // check activities
        if (data.activities.none { it.like })
            msgs.add(Message(idStore.sub(Person.activities).id, Status.Invalid, "Please provide at least one activity"))
        else
            msgs.add(
                Message(
                    idStore.sub(Person.activities).id,
                    Status.Valid,
                    "You choose ${data.activities.count { it.like }} activities"
                )
            )

        return msgs
    }

    fun cleanUp() {
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
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val personStore = object : RootStore<Person>(Person(createUUID())), Validation<Person, Message, String> {
        override val validator = PersonValidator

        val save = handleAndEmit<Unit, Person> { person ->
            // cleanup validation
            validator.cleanUp()
            // only update the list when new person is valid
            if (validate(person, "add")) {
                offer(person)
                validator.cleanUp()
                Person(createUUID())
            } else person
        }
    }

    val name = personStore.sub(Person.name)
    val birthday = personStore.sub(Person.birthday) using Format.date
    val address = personStore.sub(Person.address)
    val street = address.sub(Address.street)
    val number = address.sub(Address.number)
    val postalCode = address.sub(Address.postalCode)
    val city = address.sub(Address.city)
    val activities = personStore.sub(Person.activities)

    // extend with the Validation interface and provide a PersonValidator
    val listStore = object : RootStore<List<Person>>(emptyList()) {
        val add: Handler<Person> = handle { list, person ->
            list + person
        }
    }

    //connect the two stores
    personStore.save handledBy listStore.add


    // adding bootstrap css classes to the validated elements
    personStore.validator.msgs.onEach { msgs ->
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

                changes.values() handledBy subStore.update
            }
            div("message", id = "${subStore.id}-message") { }
        }
    }

    // helper method for creating checkboxes for activities
    fun activityCheckbox(activity: SubStore<Person, List<Activity>, Activity>): Tag<HTMLDivElement> {
        val name = activity.sub(Activity.name)
        val like = activity.sub(Activity.like)

        return render {
            div("form-check form-check-inline") {
                input("form-check-input", id = activity.id) {
                    type = const("checkbox")
                    checked = like.data

                    changes.states() handledBy like.update
                }
                label("form-check-label", `for` = activity.id) {
                    name.data.bind()
                }
            }
        }
    }

    render {
        div {
            h4 { text("Person") }
            stringInput("Name", name)

            //birthday
            div("form-group") {
                label(`for` = birthday.id) {
                    text("Birthday")
                }
                input("form-control", id = birthday.id) {
                    value = birthday.data
                    type = const("date")

                    changes.values() handledBy birthday.update
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
            div("form-group") {
                label(`for` = activities.id) {
                    text("Activities")
                }
                div("form-control", id = activities.id) {
                    activities.eachStore().map { activity ->
                        activityCheckbox(activity)
                    }.bind()
                }
                div("message", id = "${activities.id}-message") { }
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
                        val address = "${person.address.street} ${person.address.number}, " +
                                "${person.address.postalCode} ${person.address.city}"
                        val activities = person.activities.filter { it.like }.map { it.name }.joinToString()

                        render {
                            tr {
                                td { text(person.name) }
                                td { text(person.birthday.format(DateFormat.FORMAT_DATE) ?: "") }
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