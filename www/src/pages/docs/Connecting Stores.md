---
layout: layouts/docsWithContentNav.njk
title: Connecting Stores
permalink: /docs/connecting-stores/
eleventyNavigation:
    key: connecting-stores
    parent: documentation
    title: Connecting Stores
    order: 72
---
## Connecting stores to each other

Most real-world applications contain multiple stores which need to be linked to properly react to model changes.

To make your stores interconnect, fritz2 allows calling `Handlers` of others stores directly with or
without a parameter. 
```kotlin
object SaveStore : RootStore<String>("") {
    val save = handle<String> { _, data -> data }
}

object InputStore : RootStore<String>("") { 
    val input = handle<String> { _, input ->
        SaveStore.save(input) // call other store`s handler
        input // do not forget to return the "next" store value!
    }
}
```

In cases where you don't know which `Handler` of another store will handle the exposed data, you can use 
the `EmittingHandler`, a type of handler that doesn't just take data
as an argument but also emits data on a new `Flow` for other handlers to receive.

Create such a handler by calling the `handleAndEmit<T>()` function instead of the usual `handle()` function and add the 
offered data type to the type brackets: 

```kotlin
val personStore = object : RootStore<Person>(Person(...)) {
    val save = handleAndEmit<Person> { person ->
        emit(person) // emits current person
        Person(...) // return a new empty person (set as new store value)
    }
}
```
The `EmittingHandler` named `save` emits the saved `Person` on its `Flow`. 
Another store can be setup to handle this `Person` by connecting the handlers: 
```kotlin
val personStore = ... //see above

val personListStore = object : RootStore<List<Person>>(emptyList<Person>()) {
    val add = handle<Person> { list, person ->
       console.log("add new person: $person")
       list + person
    }

    init {
       // don't forget to connect the handlers
       personStore.save handledBy personListStore.add
    }
}
```
After connecting these two stores via their handlers, a saved `Person` will also be added to the list
in `personListStore`. All depending components will be updated accordingly.

To see a complete example visit our 
[validation example](https://examples.fritz2.dev/validation/build/distributions/index.html) which uses connected
 stores and validate a `Person` before adding it to a list of `Person`s.

Most of the time you will work on much more complex structures in your data model, so you will need to get `Store`s for elements "hidden" deeper in your [Nested Structures](NestedStructures.html). Let's see how fritz2 can help you here.
