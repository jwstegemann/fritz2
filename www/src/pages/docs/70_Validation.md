---
title: Validation
description: "Learn how to validate the data-models of fritz2 web-app"
layout: layouts/docs.njk
permalink: /docs/validation/
eleventyNavigation:
    key: validation
    parent: documentation
    title: Validation
    order: 70
---

When accepting user-input, it is a nice idea to validate the data before processing it any further.

To do validation in fritz2, you first have to create a `Validation` object. 
To do so you can use the global `validation` function which has the following type parameters:
* the type of data to validate
* a type for metadata you want to forward from `Handler`s to the validation (`Unit` by default if you don't need this)
* a type describing the validation-results (like a message, etc.), implementing the minimal `ValidationMessage` interface

It is recommended to put your validation code inside the companion object of your data-class in the `commonMain` 
source-set of your multiplatform-project. Code in `commonMain` can be used in `jsMain` (frontend) and `jvmMain` (backend). 

Inside the `validation` function you have access to the `Inspector` of your data model. It gives you the paths to the 
data by calling the `map()` method which, like in store-mapping, requires a lens as parameter. The mapped result is also 
an `Inspector` and has two attributes `data` and `path` which you can use during the validation of the specific member 
of the model.

To add a validation-message to the list of messages, use the `add` function.

```kotlin
data class Message(override val path: String, val severity: Severity, val text: String): ValidationMessage {
    override val isError: Boolean = severity > Severity.Warning
}

@Lenses
data class Person(
    val name: String,
    val age: Int
) {
    companion object {
        val validation: Validation<Person, Unit, Message> = validation<Person, Message> { inspector ->
            val name = inspector.map(Person.name())
            if(name.data.trim().isBlank()) {
                add(Message(name.path, Severity.Error, "Please provide a name"))
            }

            val age = inspector.map(Person.age())
            if(age.data < 1) {
                add(Message(age.path, Severity.Error, "Please correct the age"))
            } else if(age.data > 100) {
                add(Message(age.path, Severity.Warning, "Is the person really older then 100 years‽"))
            }
        }
    }
}

enum class Severity {
    Info,
    Warning,
    Error
}
```
You can structure and implement your validation-rules with everything Kotlin offers. 

Now you can use the `Validation` object in your `jsMain` code:

```kotlin
val store: ValidatingStore<Person, Unit, Message> = storeOf(Person("Chris", 42), Person.validation)
val msgs: Flow<List<Message>> = store.messages
```
By default, a `ValidatingStore` automatically validates its data after changes occur to update the list of messages.
You can access these validation-messages with `store.messages`. It's `Flow<List<M>>` where `M` is your 
`ValidationMessage`-type. Handle the `Flow` of messages like any other `Flow` of a `List`, for example by rendering it 
to HTML:

```kotlin
// create some messages with shady data
store.update(Person("", 101))

render {
    ul {
        store.messages.renderEach {
            li(baseClass = it.severity.name.toLowerCase()) {
                +it.text
            }
        }
    }
}
```

If you want to start the validation process in a specific handler you can do so by implementing the `ValidatingStore` 
by yourself:

```kotlin
object PersonStore: ValidatingStore<Person, Unit, Message>(Person("", 0), Person.validation) {
    val save = handle {
        if(validate(it).valid) {
            // send request to server...
            Person("", 0)
        } else it
    }
    val reset = handle {
        resetMessages() // empties the list of messages
        Person("", 0)
    }
}
```
By calling the `resetMessages()` function you can manually reset the list of messages if needed.

Have a look at a more complete example [here](/examples/validation).
