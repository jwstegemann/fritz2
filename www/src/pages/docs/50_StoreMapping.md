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

fritz2 uses stores to manage the application state by holding a data model. But quite often the data does 
not fit into the desired UI-fragment out of the box. This is often due to the normalized form which avoids redundancy 
as much as possible. Also, HTML can only render `Strings` at the end of the day, but the correct `String` representation 
of data types differs from case to case, and we do not want to store those explicitly.

In order to support clean data management and compromise well between data and UI-shape, fritz2's stores 
offer a powerful concept: store mapping.

Remember the `map`-function from collections where a source type `T` is transformed to another type `R`
inside of an expression? We can also *map* a store in order to change its source type to a more suitable type.
However, there is one big difference between the classical `map`-function and the store's mapping functions: A store 
needs both a getter-function from `T -> R` and a setter-function from `R -> T` to manage changes.

### Lenses

If you are unfamiliar with the universal concept for the functionality called *lens* in computer science, take
a look at the [excellent documentation on lenses](https://arrow-kt.io/learn/immutable-data/lens/) from the 
[arrow-project](https://arrow-kt.io) before reading on.

A `Lens` is basically a way to describe the relation between an outer and inner entity in a structure. It focuses on the
inner entity from the viewpoint of the outer entity, which is how it got its name. Lenses are especially useful when 
using immutable data-types like fritz2 does.

A `Lens` needs to handle the following:

* Getting the value of the inner entity from a given instance of the outer entity
* Creating a new instance of the outer entity (immutable!) as a copy of a given one with a different value only for the
  inner entity

fritz2 offers the method `lensOf()` for a short-and-sweet-experience which accepts a getter- and 
a setter-expression:
```kotlin
val nameLens: Lens<Person, String> = lensOf({ it.name }, { person, value -> person.copy(name = value) })
```

This lens can be used to access the `name`-property of a `Person`, or to create a new person with changed name:
```kotlin
val fritz2 = Person(1, "fritz2")
val nameOfFritz2: String = nameLens.get(person) // nameOfFritz2 = "fritz2"
val hugo: Person = nameLens.set(fritz2, "hugo") // hugo = Person(1, "hugo")
```
As you can see, there is no magic, just plain old function calling.

Let's take a step back and explore how the concept of lenses can be used to map one store to another.

### Mapping Stores Using Lenses

Imagine a use case where the interests of a person are rendered as tags - comma seperated values. They can be changed 
by typing them as CSV.

But in order to further process these interests, it makes more sense to store them in a `List<Interest>`, so that will
be the canonical state representation in our application:
```kotlin
val interestsStore: Store<List<Interest>> = storeOf(emptyList())
```
However, this representation does not fit the requirements of the specific UI-fragment - but no worries! Simply define 
a `Lens` that does the mapping between the list and the `String` based CSV representation:
```kotlin
val interestLens: Lens<List<Interest>, String> = lensOf(
    List<Interest>::joinToString, // getter
    { it.split(",").map { Interest.valueOf(it.trim()) } } // setter
)
```

Armed with this lens, we can map the interest-store and use the resulting store in the UI:
```kotlin
val interestsStore: Store<List<Interest>> = storeOf(emptyList())

val interestLens: Lens<List<Interest>, String> = lensOf(
    List<Interest>::joinToString,
    { it.split(",").map { Interest.valueOf(it.trim()) } }
)

val csvInterests: Store<String> = interestsStore.map(interestLens)
//  ^^^^^^^^^^^^^^^^^^^^^^^^^^^                  ^^^^^^^^^^^^^^^^^
//  Create a new store with the                  Use the `map` function to map the store
//  desired type (2nd of the `Lens`)             and provide the lens for the transformations

render {
    h1 { +"Choose Interests from:" }
    p { +Interest.values().joinToString() }
    input {
        label { +"Interests:" }
        // reactively connect the input to the mapped CSV representation store
        value(csvInterests.data) 
        changes.values() handledBy csvInterests.update
    }
    h1 { +"Chosen Interests:" }
    csvInterests.data.renderText()
    
    // just to demonstrate that the original store is always in sync with the mapped one:
    interestsStore.data handledBy { interests ->
        console.log(interests)
    }
}
```
As you can see, the mapped store fits perfectly to the desired (yet a little artificial) requirements for the UI:
There is no mapping inside the UI, nor are there any custom handlers or data-flows in the store.

But to be fair, the manual creation of the lens is heavy work. So fritz2 offers more tools to make lens generation 
easier, especially for the use case of destructuring complex model types.

## Essentials

### Lenses in Depth

Most of the time, a model for a view will not be of a simple data-type but a complex entity, like a
person having a name, multiple addresses, an email, a date of birth, etc.

In those cases, you will most likely need `Store`s for the properties of your main entity, and - later on - for
the properties of the child-entity like the street in an address in our example from above.

fritz2 again uses `Lens`es to describe the relationship between an entity and its child-entities and
properties.

In fritz2, a `Lens` is defined by the following interface:
```kotlin
interface Lens<P,T> {
    val id: String
    fun get(parent: P): T
    fun set(parent: P, value: T): P
}
```

All you do to benefit is implement the functions `get()` and `set()`, or use the `lensOf()` function we discussed in
previous sections:

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

Keep in mind that your annotated classes have to be in your `commonMain` source-set,
otherwise the automatic generation of the lenses will not work!

Have a look at the [validation-example](/examples/validation) to see how to set it up.

This will also help you define a multiplatform project for sharing your model and validation code between
the browser and backend.

### Destructuring Complex Models With Mapped `Store`s and `Lense`s

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

Now you can use your `nameStore` exactly like any other `Store` to set up _two-way data binding_. Call `map(...)`
again to access the properties of `Name`. If a `Store` contains a `List`, you can of course iterate over it by
using `renderEach()`. It's fully recursive from here on down to the deepest nested parts of your model.

[Remember](/docs/createstores/#extend-none-custom-(local)-stores) that you can also add `Handler`s to your `Store`s 
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

To keep your code well-structured, it is recommended to implement complex logic in your `Store` or inherit it by 
using interfaces. However, the code above is a decent solution for small (convenience-)handlers.

### Calling `map` on a `Store` With Nullable Content

Calling `map` on a nullable `Store` only makes sense when you have checked that its state is not null:

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

### Handling Nullable States in `Store`s

If you have a `Store` with a nullable state, you can use `mapNull` to derive a non-nullable `Store` from it which 
transparently translates a `null`-value from its parent `Store` to the given default-value and vice versa.

After text is entered into and then removed from the input of the following example, `nameStore` will have a state of
`null`: 

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

In the real world, you will often come across nullable attributes of complex entities. Call `mapNull`
directly on the `Store` you create to use with your form elements:

```kotlin
@Lenses
data class Person(val name: String?)

//...

val personStore = storeOf(Person(null))

//...

val nameStore = personStore.map(Person.name()).mapNull("")
```

### Combining Lenses

A `Lens` supports the `plus`-operator with another lens in order to create a new lens which combines the two. 
The `get` and `set`-functions of the resulting lens are chained in natural order.

Imagine the following example:
```kotlin
data class Address(val street: String)
data class Person(val address: Address)

val addressOfPerson: Lens<Person, Address> = lensOf("address", Person::address) { p, v -> p.copy(address = v) }
val streetOfAddress: Lens<Address, String> = lensOf("street", Address::street) { p, v -> p.copy(street = v) }

// combine two lenses:
val streetOfPerson = address + street

// apply the combined lens to an example object:
val person = Person(Address("Lerchenweg"))
streetOfPerson.get(person) // -> "Lerchenweg"
streetOfPerson.set("Rosenstraße") // Person(address = Address("Rosenstraße"))
```

Let's see how this example would work with automatically generated lenses.

```kotlin
@Lenses
data class Address(val street: String) { companion object }

@Lenses
data class Person(val address: Address) { companion object }

val streetOfPerson = Person.address() + Address.street()
```

This works, but the syntax is quite cumbersome, especially for deeper nested models.

fritz2's automatic `@Lenses`-annotation-processor has dedicated support for deeper nested models as well and
creates extension functions for all lenses. This allows you to chain the calls fluently:

```kotlin
@Lenses
data class Address(val street: String) { companion object }

@Lenses
data class Person(val address: Address) { companion object }

val streetOfPerson = Person.address().street()
```
This fluent API looks much terser and cleaner compared to the canonical one above. Note that, under the hood, nothing
special happens - the generated code simply uses the `plus` operator the same way you would.

Combining lenses is also very useful for formatting values, as we shall see in the next section.

### Formatting Values

HTML allows only `String`s in your attributes, for example the `value` attribute of `input {}`. To use other data
types in your model, you have to specify how a type should be represented as `String` (e.g. Number, Currency, Date).
When working with `input {}`, you also need parse the user input back to your data type.
For all Kotlin basic types, there is a convenience function `asString()` which generates a `Lens` from this type to 
`String` and vice versa. It internally calls the `T.toString()` and `String.toT()` functions.

```kotlin
@Lenses
data class Person(val age: Int)

val ageLens: Lens<Person, Int> = Person.age() // cannot be used in tag attributes
val ageLensAsString: Lens<Person, String> = Person.age().asString() // now it is usable
```

Also, remember that you can use the `lensOf()` function to create lenses which we introduced in the lenses section:

```kotlin
fun <P> lensOf(format: (P) -> String, parse: (String) -> P): Lens<P, String>
```

When using other types like `kotlinx.datetime.LocalDate`, special lenses for string conversion need to be specified for them:
```kotlin
import kotlinx.datetime.*

@Lenses
data class Person(val birthday: LocalDate)

object Formats {
    val date: Lens<LocalDate, String> = lensOf(LocalDate::toString, String::toLocalDate)
}
```

Now you can use the `Formats.date` lens for deriving stores:

```kotlin
val personStore = storeOf<Person>(Person(LocalDate(1990, 1, 1)))

val birthday: Store<String> = personStore.map(Person.birthday() + Formats.date)
// or when an interim store is needed
val birthday: Store<String> = personStore.map(Person.birthday()).map(Formats.date)
```

Take a look at our complete [validation example](/examples/validation) to get an impression of that topic.

### Summary of Store-Mapping-Factories

| Factory                                                         | Use case                                                                                                                                                  |
|-----------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Store<P>.map(lens: Lens<P, T>): Store<T>`                      | Most generic map-function. Maps any `Store` given a `Lens`. Use for model destructuring with automatic generated lenses for example.                      |
| `Store<P?>.map(lens: Lens<P & Any, T>): Store<T>`               | Maps any nullable `Store` given a `Lens` to a `Store` of a definitely none nullable `T`. Use in `render*`-content expressions combined with a null check. |
| `Store<List<T>>.mapByElement(element: T, idProvider): Store<T>` | Maps a `Store` of a `List<T>` to one element of that list. Works for entities, as a stable Id is needed.                                                  |
| `Store<List<T>>.mapByIndex(index: Int): Store<T>`               | Maps a `Store` of a `List<T>` to one element of that list using the index.                                                                                |
| `Store<Map<K, V>>.mapByKey(key: K): Store<V>`                   | Maps a `Store` of a `Map<T>` to one element of that map using the key.                                                                                    |
| `Store<T?>.mapNull(default: T): Store<T>`                       | Maps a `Store` of a nullable `T` to a `Store` of a definitely none nullable `T` using a default value in case of `null` in source-store.                  |
| `MapRouter.mapByKey(key: String): Store<String>`                | Maps a `MapRouter` to a `Store`. See [chapter about routers](/docs/routing/#maprouter) for more information.                                              |

### Summary Lens-Factories

| Factory                                                                   | Use case                                                                                        |
|---------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| `lensOf(id: String, getter: (P) -> T, setter: (P, T) -> P): Lens<P, T>`   | Most generic lens (used by `lenses-annotation-processor`. Fits for complex model destructuring. |
| `lensOf(parse: (String) -> P, format: (P) -> String): Lens<P, String>`    | Formatting lens: Use for mapping into `String`s.                                                |
| `lensForElement(element: T, idProvider: IdProvider<T, I>): Lens<List, T>` | Select one element from a list of entities, therefore a stable Id is needed.                    |
| `lensForElement(index: Int): Lens<List, T>`                               | Select one element from a list by index. Useful for value objects.                              |
| `lensForElement(key: K): Lens<Map<K, V>, V>`                              | Select one element from a map by key.                                                           |

## Advanced Topics

### Reactive Rendering of Entity-Lists With Auto-Mapped Element Store

There is a special convenience method for the [reactive rendering](/docs/render/#reactive-rendering) of lists of 
entities which requires some knowledge about `Store`s and `Lens`es.

On a store of `List<T>`, an extension method called `renderEach` is defined directly on the
store. It is mandatory to pass an `idProvider`, so this is targeted to entity-types.

Inside the content-parameter expression of `renderEach`, instead of just `T`, a `Store<T>` gets injected. Some 
store-mapping takes place under the hood: For each element of the original list, a mapped store
handling that element from the original store is created.

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
            
            // provide an input element in order to modify one property of that person
            input {
                value(storedName.data)
                changes.values() handledBy storedName.update
            }
        }
    }
}
```

Tables with editable cells are a typical use case for `Store<List<T>>.renderEach`.
Check out the boilerplate code that can be omitted in comparison to using the `Flow<List<T>>.renderEach`-function:

```kotlin
val storedPersons: Store<List<Person>> = storeOf(listOf(Person(1, "fritz2", emptySet())))

val nameLens: Lens<Person, String> = lensOf("name", Person::name) { person, name -> person.copy(name = name) }

render {
    div {
        storedPersons.data.renderEach(Person::id) { person -> //  We get a `T`.. 
            
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

You might notice that the parameters of `renderEach` and `mapByElement` are identically. That's why it is possible
to encapsulate the store mapping directly into the former presented convenience function.