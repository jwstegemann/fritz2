---
title: Store Mapping
description: "Learn how Mapping on Stores will enable elegant state management"
layout: layouts/docs.njk
permalink: /docs/storemapping/
eleventyNavigation:
    key: storemapping
    parent: documentation
    title: Store Mapping
    order: 50
---

## Overview

fritz2 uses stores to manage the application state by holding some data model. But quite often those data does 
not fit naturally to the needed UI-fragment. This is often due to the normalized form, where redundancy is avoided
as much as possible. Also, HTML can only render `Strings` in the end, but the `String` representation of some data 
types might differ from case to case, and we do not want to store those all explicitly.

In order to support clean data management but also a good match between data and UI-shape, fritz2's store concept
offers a powerful concept: Store *mapping*.

Like the known `map`-function from collections, where some source type `T` gets transformed to some other type `R`
inside an expression, we can also *map* a store in order to change its source type to some other, better fitting type.
There is one big difference between the classical `map`-function and the store's mapping functions: A store needs
not only a function from `T -> R` (getter) but also from `R -> T` (setter) as a store manages changes!

### Lenses

There is a universal concept in computer science for such a functionality called *lens*. You might have a look at
the [excellent documentation of lenses](https://arrow-kt.io/docs/optics/lens/) from the 
[arrow-project](https://arrow-kt.io).

fritz2 also offers the method `lensOf()` for a short-and-sweet-experience, which accepts a getter- and 
a setter-expression:
```kotlin
val nameLens: Lens<Person, String> = lensOf({ it.name }, { person, value -> person.copy(name = value) })
```

The lens can then be used to access the `name`-property of a `Person` or to create a new person with changed name:
```kotlin
val fritz2 = Person(1, "fritz2")
val nameOfFritz2: String = nameLens.get(person) // nameOfFritz2 = "fritz2"
val hugo: Person = nameLens.set(fritz2, "hugo") // hugo = Person(1, "hugo")
```
As you can see, there is no *magic*; just plain old function calling.

Let us take a step back and explore, how this concept of lenses can be used to map one store to another.

### Mapping a Store

Imagine a use case, where we want to render the interests of a person like a kind of tags as comma seperated values.
We would also like to change them by typing them as CSV.

In order to further process interests of a person, it makes more sense to store them a `List<Interests>` though. 
So that should be the canonical state representation in our application.
```kotlin
val interestsStore: Store<List<Interest>> = storeOf(emptyList())
```

It does not fit to the requirements of the specific UI-fragment though!

We can define a `Lens` that does the mapping between the list and the `String` based CSV representation:
```kotlin
val interestLens: Lens<List<Interest>, String> = lensOf(
    List<Interest>::joinToString, // getter
    { it.split(",").map { Interest.valueOf(it.trim()) } } // setter
)
```

Armed with this lens, we can finally map the whole interest-store and use the resulting store for the UI:
```kotlin
val interestsStore: Store<List<Interest>> = storeOf(emptyList())

val interestLens: Lens<List<Interest>, String> = lensOf(
    List<Interest>::joinToString,
    { it.split(",").map { Interest.valueOf(it.trim()) } }
)

val csvInterests: Store<String> = interestsStore.map(interestLens)
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^                  ^^^^^^^^^^^^^^^^^
//  We create a new store with the               we use the `map` function to "map" the store
//  desired type (2nd of the `Lens`)             and provide the lens, that `map` uses to process the mapping

render {
    h1 { +"Choose Interests from" }
    p { +Interest.values().joinToString() }
    input {
        label { +"Interests:" }
        // connect the input reactively to the mapped CSV representation store
        value(csvInterests.data) 
        changes.values() handledBy csvInterests.update
    }
    h1 { +"Chosen Interests:" }
    csvInterests.data.renderText()
    
    // Just to demonstrate that the original store is always in sync with the mapped one:
    interestsStore.data handledBy { interests ->
        console.log(interests)
    }
}
```
As you can see, the mapped store fits perfectly to the desired (yet a little artificial) requirements for the UI:
There is no mapping inside the UI, nor are there any custom handler or data-flows in the store.

To be fair, the heavy work is done by the manual creation of the lens though.

fritz2 offers some more tools to make lens generation easier, especially for the use case of destructuring complex
model types.

## Essentials

### Lenses in Depth

Most of the time, your model for a view will not be of just a simple data-type but a complex entity, like a
person having a name, multiple addresses, an email, a date of birth, etc.

In those cases, you will most likely need `Store`s for the single properties of your main entity, and - later on - for
the properties of the child-entity like the street in an address in our example from above.

fritz2 uses a mechanism called `Lens` to describe the relationship between an entity and its child-entities and
properties.

A `Lens` is basically a way to describe the relation between an outer and inner entity in a structure.
It focuses on the inner entity from the viewpoint of the outer entity, which is how it got its name.
Lenses are especially useful when using immutable data-types like fritz2 does.
A `Lens` needs to handle the following:

* Getting the value of the inner entity from a given instance of the outer entity
* Creating a new instance of the outer entity (immutable!) as a copy of a given one with a different value only for the 
inner entity

In fritz2, a `Lens` is defined by the following interface:
```kotlin
interface Lens<P,T> {
    val id: String
    fun get(parent: P): T
    fun set(parent: P, value: T): P
}
```

You can easily use this interface by just implementing `get()` and `set()`.
fritz2 also offers the method `lensOf()` for a short-and-sweet-experience:

```kotlin
val nameLens = lensOf("name", { it.name }, { person, value -> person.copy(name = value) })
```

No magic there. The first parameter sets an id for the `Lens`. When using `Lens`es with `Store`s,
the `id` will be used to generate a valid HTML `id` representing the path through your model.
This can be used to identify your elements semantically (for validation or automated ui-tests for example).

If you have deep nested structures or a lot of them, you may want to automate this behavior.
fritz2 offers an annotation `@Lenses` you can add to your data-classes in the `commonMain` source-set of
your multiplatform project:
```kotlin
@Lenses
data class Person(val name: String, val value: String) {
    companion object // needs to be declared! The generated lens-factories are created within.
}
```
Using an annotation-processor, fritz2 builds factory methods for each public constructor property within the
companion object of the data class from these annotations which contains all the `Lens`es you need.
They are named exactly like the entities and properties, so it's easy to use:

```kotlin
val nameLens = Person.name()
```

You can see it in action at our [nestedmodel-example](/examples/nestedmodel).

Keep in mind that your annotated classes have to be in your `commonMain` source-set
otherwise the automatic generation of the lenses will not work!

Have a look at the [validation-example](/examples/validation)
to see how to set it up.

This will also help you define a multiplatform project for sharing your model and validation code between
the browser and backend.

### Destructuring complex Models with mapped `Store`s and `Lense`s

Having a `Lens` available which points to some specific property makes it very easy to get a `Store` for that
property from an original `Store` of the parent entity:

```kotlin
// given the following nested data classes...
@Lenses
data class Name(val firstname: String, val lastname: String) {
    companion object
}

@Lenses 
data class Person(val name: Name, description: String) {
    companion object
}

// ... you can create a root-store...
val personStore = storeOf(Person(Name("first name", "last name"), "more text"))
// ... and a derived store using the automatic generated lens-factory `Person.name()`
val nameStore = personStore.map(Person.name())
```

Now you can use your `nameStore` exactly like any other `Store` to set up _two-way data binding_, call `map(...)`
again to access the properties of `Name`. If a `Store` contains a `List`, you can of course iterate over it by
using `renderEach()`. It's fully recursive from here on down to the deepest nested parts of your model.

[Rememeber](/docs/createstores/#extend-none-custom-(local)-stores) that you can also add `Handler`s to your `Store`s 
by simply calling the `handle` method:

```kotlin
val booleanChildStore = parentStore.map(someLens)
val switch = booleanChildStore.handle { model: Boolean -> 
    !model
}

render {
    button {
        +"switch state"
        clicks handledBy switch
    }
}
```

To keep your code well-structured, it is recommended to implement complex logic at your `Store` or inherit it by 
using interfaces. However, the code above is a decent solution for small (convenience-)handlers.

### Calling `map` on a `Store` with nullable Content

To call `map` on a nullable `Store` only makes sense, when you have checked, that its state is not null:

```kotlin
@Lenses
data class Person(val name: String)

//...

val applicationStore = storeOf<Person>(null)

//...

applicationStore.data.render { person ->
    if (person != null) { // if person is null you would get NullPointerExceptions reading or updating its Stores
        val nameStore = customerStore.map(Person.name())
        input {
            value(nameStore.data)
            changes.values() handledBy nameStore.update
        }
    }
    else {
        p { + "no customer selected" }
    }
}
```

### Handling nullable States in `Store`s

If you have a `Store` with a nullable state, you can use `mapNull` to derive a non-nullable `Store` from it,
that transparently translates a `null`-value from its parent `Store` to the given default-value and vice versa.

In the following case, when you enter some text in the input and remove it again,
you will have a state of `null` in your `nameStore`:

```kotlin
val nameStore = storeOf<String?>(null)

render {
    input {
        nameStore.mapNull("").also { formStore ->
            value(formStore.data)
            changes.values() handledBy formStore.update
        }
    }
}
```

In real world, you will often come across nullable attributes of complex entities. Then you can call `mapNull`
directly on the `Store` you create to use with your form elements:

```kotlin
@Lenses
data class Person(val name: String?)

//...

val personStore = storeOf(Person(null))

//...

val nameStore = personStore.map(Person.name()).mapNull("")
```

### Formatting Values

In html you can only use `String`s in your attributes like in the `value` attribute of `input {}`. To use other data
types in your model you have to specify how to represent a specific value as `String` (e.g. Number, Currency, Date).
When you work with `input {}` you also need parse the entered text back to your data type.
For all Kotlin basic types there is a convenience function `asString()` which generates a `Lens` from this type to 
`String` and vice versa. Therefore, it calls internally the `T.toString()` and `String.toT()` functions.

```kotlin
@Lenses
data class Person(val age: Int)

val ageLens: Lens<Person, Int> = Person.age() // cannot be used in tag attributes
val ageLensAsString: Lens<Person, String> = Person.age().asString() // now it is usable
```

fritz2 also provides a special `lensOf()` function for creating a `Lens<P, String>` for special types that are not 
basic:

```kotlin
fun <P> lensOf(format: (P) -> String, parse: (String) -> P): Lens<P, String>
```

If you use other types like `kotlinx.datetime.LocalDate` in your data classes, you have to specify a special lens for it.
This lens then converts the value to a `String` and vice versa.
```kotlin
import kotlinx.datetime.*

@Lenses
data class Person(val birthday: LocalDate)

object Formats {
    val date: Lens<LocalDate, String> = lensOf(LocalDate::toString, String::toLocalDate)
}
```

Now you can use the `Formats.date` lens for deriving appropriate stores:

```kotlin
val personStore = storeOf<Person>(Person(LocalDate(1990, 1, 1)))

val birthday: Store<String> = personStore.map(Person.birthday() + Formats.date)
// or when interim store is needed
val birthday: Store<String> = personStore.map(Person.birthday()).map(Formats.date)
```

Take a look at our complete [validation example](/examples/validation) to get an impression on that topic.

### Summary of Store-Mapping-Factories

| Factory                                                         | Use case                                                                                                                                                       |
|-----------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Store<P>.map(lens: Lens<P, T>): Store<T>`                      | Most generic map-function. Maps any `Store` given a `Lens`. Use for model destructuring with automatic generated lenses for example.                           |
| `Store<P?>.map(lens: Lens<P & Any, T>): Store<T>`               | Maps any *nullable* `Store` given a `Lens` to a `Store` of a definitely none nullable `T`. Use in `render*`-content expressions combined with some null check. |
| `Store<List<T>>.mapByElement(element: T, idProvider): Store<T>` | Maps a `Store` of some `List<T>` to one element of that list. Works for entities, as a stable Id is needed.                                                    |
| `Store<List<T>>.mapByIndex(index: Int): Store<T>`               | Maps a `Store` of some `List<T>` to one element of that list using the index.                                                                                  |
| `Store<Map<K, V>>.mapByKey(key: K): Store<V>`                   | Maps a `Store` of some `Map<T>` to one element of that map using the key                                                                                       |
| `Store<T?>.mapNull(default: T): Store<T>`                       | Maps a `Store` of some nullable `T` to a `Store` of a definitely none nullable `T` using some default value in case of `null` in source-store.                 |
| `MapRouter.mapByKey(key: String): Store<String>`                | Maps a `MapRouter` to a `Store`. See [chapter about routers](/docs/routing/#maprouter) for more information.                                                   |

### Summary Lens-Factories

| Factory                                                                   | Use case                                                                                        |
|---------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| `lensOf(id: String, getter: (P) -> T, setter: (P, T) -> P): Lens<P, T>`   | Most generic lens (used by `lenses-annotation-processor`. Fits for complex model destructuring. |
| `lensOf(parse: (String) -> P, format: (P) -> String): Lens<P, String>`    | Formatting lens: Use for mapping into `String`s.                                                |
| `lensForElement(element: T, idProvider: IdProvider<T, I>): Lens<List, T>` | Select one element from a list of entities, therefore some stable Id is needed.                 |
| `lensForElement(index: Int): Lens<List, T>`                               | Select one element from a list by index. Useful for value objects                               |
| `lensForElement(key: K): Lens<Map<K, V>, V>`                              | Select one element from a map by some key.                                                      |

## Advanced Topics

### Reactive Rendering of Lists of Entities with automatically Mapped Element Store

There is a special convenience method for the [reactive rendering](/docs/render/#reactive-rendering) of list of 
entities, that can only be explained with the already explained knowledge about `Store`s and `Lens`es.

On a store of `List<T>` an extension method called `renderEach` ia defined *directly* on the
store. It is mandatory to pass an `idProvider`, so this is targeted to entity-types.

Inside the content-parameter expression of `renderEach`, instead of some `T` a whole `Store<T>` gets injected. So
under the hood there is some store-mapping taking place, where for each element of the original list, a mapped store
handling that element from the original store is created:

```kotlin
val storedPersons: Store<List<Person>> = storeOf(listOf(Person(1, "fritz2", emptySet())))

// needed for mapping an already mapped store to destructure the model further 
val nameLens: Lens<Person, String> = lensOf("name", Person::name) { person, name -> person.copy(name = name) }

render {
    div {
        storedPersons.renderEach(Person::id) { storedPerson ->
        //            ^^^^^^^^^^^^^^^^^^^^^    ^^^^^^^^^^^^
        //            call directly on the     get a full `Store<Person>`
        //            store                    for each element
            
            val storedName = storedPerson.map(nameLens) // create this store to map it further
            
            // provide some input element in order to modify one property of that person
            input {
                value(storedName.data)
                changes.values() handledBy storedName.update
            }
        }
    }
}
```

Typical use cases are tables with editable cells for example.

With all the knowledge about reactive rendering of entities and lenses, we can demonstrate, what boilerplate code
can be omitted, using the `Flow<List<T>>.renderEach`-function:
```kotlin
val storedPersons: Store<List<Person>> = storeOf(listOf(Person(1, "fritz2", emptySet())))

val nameLens: Lens<Person, String> = lensOf("name", Person::name) { person, name -> person.copy(name = name) }

render {
    div {
        storedPersons.data.renderEach(Person::id) { person -> // we only get some `T` ...
            
            // ... thus we must create the mapped store manually:
            val storedPerson = storedPersons.mapByElement(person, Person::id)
            
            // the same as above
            val storedName = storedPerson.map(nameLens)
            input {
                value(storedName.data)
                changes.values() handledBy storedName.update
            }
        }
    }
}
```

You might recognize that the parameters of `renderEach` and `mapByElement` are identically. That's why it is possible
to encapsulate the store mapping directly into the former presented convenience function.
