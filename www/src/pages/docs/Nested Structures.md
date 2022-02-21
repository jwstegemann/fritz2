---
layout: layouts/docsWithContentNav.njk
title: Nested Structures
permalink: /docs/nested-structures/
eleventyNavigation:
    key: nested-structures
    parent: documentation
    title: Nested Structures
    order: 80
---

Most of the time, your model for a view will not be of just a simple data-type but a complex entity, like a 
person having a name, multiple addresses, an email, a date of birth, etc.

In those cases, you will most likely need `Store`s for the single properties of your main entity, and - later on - for 
the properties of the sub-entity like the street in an address in our example from above.

fritz2 uses a mechanism called `Lens` to describe the relationship between an entity and its sub-entities and properties. 

## Lenses

A `Lens` is basically a way to describe the relation between an outer and inner entity in a structure.
It focuses on the inner entity from the viewpoint of the outer entity, which is how it got its name.
Lenses are especially useful when using immutable data-types like fritz2 does.
A `Lens` needs to handle the following:

* Getting the value of the inner entity from a given instance of the outer entity
* Creating a new instance of the outer entity (immutable!) as a copy of a given one with a different value only for the inner entity

In fritz2, a `Lens` is defined by the following interface:
```kotlin
interface Lens<P,T> {
    val id: String
    fun get(parent: P): T
    fun set(parent: P, value: T): P
}
```

You can easily use this interface by just implementing `get()` and `set()`.
fritz2 also offers the method `lens()` for a short-and-sweet-experience:

```kotlin
val nameLens = lens("name", { it.name }, { person, value -> person.copy(name = value) })
```

No magic there. The first parameter sets an id for the `Lens`. When using `Lens`es with `SubStore`s, the `id` will be used to generate a valid html-id representing the path through your model.
This can be used to identify your elements semantically (for automated ui-tests for example).

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

You can see it in action at our [nestedmodel-example](https://examples.fritz2.dev/nestedmodel/build/distributions/index.html).

Keep in mind that your annotated classes have to be in your `commonMain` source-set
otherwise the automatic generation of the lenses will not work!

Have a look at the [validation-example](https://examples.fritz2.dev/validation/build/distributions/index.html) to see how to set it up.

This will also help you define a multiplatform project for sharing your model and validation code between
the browser and backend.  

Having a `Lens` available which points to some specific property makes it very easy to get a `SubStore` for that 
property from a `Store` of the parent entity:

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
// ... and a sub-store using the automatic generated lens-factory `Person.name()`
val nameStore = personStore.sub(Person.name())
```

Now you can use your `nameStore` exactly like any other `Store` to set up _two-way-databinding_, call `sub(...)` 
again to access the properties of `Name`. If a `SubStore` contains a `List`, 
you can of course iterate over it by using `renderEach {}` like described in [Lists as a Model](ListsinaModel.html). 
It's fully recursive from here on down to the deepest nested parts of your model.

You can also add `Handler`s to your `SubStore`s by simply calling the `handle`-method:

```kotlin
val booleanSubStore = parentStore.sub(someLens)
val switch = booleanSubStore.handle { model: Boolean -> 
    !model
}

render {
    button {
        +"switch state"
        clicks handledBy switch
    }
}
````

To keep your code well-structured, it is recommended to implement complex logic at your `RootStore` or inherit it by using interfaces. 
However, the code above is a decent solution for small (convenience-)handlers.

Your real world data model will most certainly contain lists of elements.
Learn how to handle them effectively by reading about [Lists as a Model](ListsinaModel.html)

# Formatting Values

In html you can only use `Strings` in your attributes like in the `value` attribute of `input {}`. To use other data
types in your model you have to specify how to represent a specific value as `String` (e.g. Number, Currency, Date).
When you work with `input {}` you also need parse the entered text back to your data type.
For all Kotlin basic types there is a convenience function `asString()` which generates a `Lens` from this type to `String`
and vice versa. Therefore, it calls internally the `T.toString()` and `String.toT()` functions.

```kotlin
val ageLens: Lens<Person, Int> = Person.age() // cannot used in Tag attributes
val ageLensAsString: Lens<Person, String> = Person.age().asString() // now it is useable
```

fritz2 also provides a special function `format()` for creating a `Lens<P, String>` for special types that are not basic:

```kotlin
fun <P> format(parse: (String) -> P, format: (P) -> String): Lens<P, String>
```

The following [validation example](https://examples.fritz2.dev/validation/build/distributions/index.html) demonstrates its usage:
```kotlin
import dev.fritz2.lens.format

object Formats {
    private val dateFormat: DateFormat = DateFormat("yyyy-MM-dd")
    val date: Lens<Date, String> = format(
        parse = { dateFormat.parseDate(it) },
        format = { dateFormat.format(it) }
    )
}
```

When you have created a special `Lens` for your own data type like `Formats.date`, you can then use it to create a new `SubStore`:
concatenate your Lenses before using them in the `sub()` method e.g. `sub(Person.birthday() + Fromat.dateLens)` or
call the method `sub()` on the `SubStore` of your custom type `P` with your formatting `Lens` e.g `sub(Format.dateLens)`.

Here is the code from the [validation example](https://examples.fritz2.dev/validation/build/distributions/index.html)
which uses the special `Lens` in the `Formats` object specified above for the `com.soywiz.klock.Date` type:
```kotlin
import com.soywiz.klock.Date
...

val personStore = object : RootStore<Person>(Person(createUUID()))
...
val birthday = personStore.sub(Person.birthday() + Format.date)
// or
val birthday = personStore.sub(Person.birthday()).sub(Format.date)
...
input("form-control", id = birthday.id) {
    value = birthday.data
    type = const("date")

    changes.values() handledBy birthday.update
}
```
The resulting `SubStore` is a `Store<String>`.

You can of course reuse your custom formatting `Lens` for every `SubStore` of the same type (in this case `com.soywiz.klock.Date`).