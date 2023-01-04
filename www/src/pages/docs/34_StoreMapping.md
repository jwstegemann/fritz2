---
title: Store Mapping
description: "Learn how Mapping on Stores will enable elegant state management"
layout: layouts/docs.njk
permalink: /docs/storemapping/
eleventyNavigation:
    key: storemapping
    parent: documentation
    title: Store Mapping
    order: 34
---

## Overview

- map(Lens)
- Store -> anderen Store, der

```
Store<A> <-> Store<B>
             ^^^^^^^
             für UI sinnvoller
             
tags = listOf("a", "b", "c")
val a = Store<List<String>>
<->
tags = "a,b,c" // CSV
val b = Store<String> // für Input

lens: lensOf("id"
  { p -> p.split(",") } // a -> b
  { p, v -> v.joinToString() } // b (ggf. unter Nutzung des alten Wertes "a") -> a
)
```

- -> p braucht man hier nicht (evtl. ohne p -> Jan muss formatOf anpassen!)

## Essentials

- Spezialfall: abgeleiteter Store hat nur Teile vom Parent-Modell (ganz oft einzelnes Attribut!) -> p wird benötigt, um
  setter zu implementieren.
- manuell
- @Lenses Annotation für Convenience Fälle
- alle anderen map Fälle
- renderEach mit IdProvider
- renderEach auf dem Store

### Handling nullable values in `Store`s

If you have a `Store` with a nullable content, you can use `orDefault` to derive a non-nullable `Store` from it,
that transparently translates a `null`-value from its parent `Store` to the given default-value and vice versa.

In the following case, when you enter some text in the input and remove it again,
you will have a value of `null` in your `nameStore`:

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

val applicationStore = storeOf(Person(null))

//...

val nameStore = applicationStore.map(Person.name()).mapNull("")
```

## Advanced Topics

???