---
layout: layouts/docsWithContentNav.njk
title: Format
permalink: /docs/format/
eleventyNavigation:
    key: formats
    parent: documentation
    title: Format
    order: 84
---

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