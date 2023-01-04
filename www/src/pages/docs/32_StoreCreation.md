---
title: Store Creation
description: "Learn how to create Stores"
layout: layouts/docs.njk
permalink: /docs/createstores/
eleventyNavigation:
    key: createstores
    parent: documentation
    title: Store Creation
    order: 32
---

## Overview

State handling is probably the key aspect of any reactive web application, so fritz2 supports this with a simple, but
mighty concept: `Store`s.

A store stores all your data for running an application; no matter if it is domain data or UI state. In contrast to
other approaches, there might be an arbitrary number of stores, which you can connect to handle the overall state
together.

Before we start, let us recap some model types, that are used for some upcoming examples:
```kotlin
// example of some value type
enum class Interest {
    Programming,
    Sports,
    History,
    WritingDocumentation
}

// example of an entity
data class Person(
    val id: Int, // stable identifier
    val name: String,
    val age: Int,
    val interests: List<Interest>
)
```

### Simple Store by Factory

The simplest way to create some store is to create one with the `storeOf` factory:

```kotlin
// a store can be created anywhere in you application!
// pass some data as initial state into it
val storedInterest: Store<Interest> = storeOf(Interest.Programming)

// a store can manage any complex type `T`
val storedPerson: Store<Person> = storeOf(Person(1, "fritz2", 3, listOf(Interest.Programming)))
```

Once you have created the store, it can be used for...
- ... rendering the current state into your UI
- ... dealing with state changes triggered by (user) events.

This store supports the former functions by exposing the following two properties out of the box:
- `data`: This `Flow` of `T` can be used to render the current state into the UI by using some `render*`-functions. 
This is described in the [reactive rendering](/docs/render/#reactive-rendering) sections of the 
[Render HTML](/docs/render) chapter.
- `update`: This is a `Handler` which manages the state changes of the store. This default handler takes one `T` and
simply substitutes the old value `T` of the store with it.

You can use this handler to conveniently implement _two-way-databinding_ by using the `changes` event-flow
of an `input`-`Tag`, for example:

```kotlin
val store: Store<String> = storeOf("")

render {
    input {
        // react to data changes and update the UI
        value(store.data)
        
        // react to UI events and update the data state
        changes.values() handledBy store.update
    }
}
```

`changes` in this example is a `Flow` of events created by listening to the `Change`-Event of the underlying input-element.
Calling `values()` on it extracts the current value from the input.
Whenever such an event is raised, a new value appears on the `Flow` and is processed by the `update`-Handler of the
`Store` to update the model. Event-flows are available for
[all HTML5-events](https://www.fritz2.dev/api/core/dev.fritz2.core/-with-events/index.html).
There are some more [convenience functions](https://www.fritz2.dev/api/core/dev.fritz2.core/-listener/index.html) to
help you to extract data from an event or control event-processing.

### Custom Stores

In order to extend a store with own handlers or some specific data-flows, you can create your own store-object:
```kotlin
// use `RootStore` as base class! It implements all the functions you will rely on. 
val storedInterests = object : RootStore<List<Interest>>(emptyList()) {
    // fill with custom functionality
}
```

Of course, it is also possible to declare a class and create the object later:
```kotlin
class InterestsStore(initial: List<Interest> = emptyList()) : RootStore<List<Interest>>(initial) {
    // fill with custom functionality
}

// use it
val storedInterests = InterestsStore()
```

### Custom Handler

Our custom store already has the handler `update` as we already know, which simply substitutes the old store's value 
with a new one. This is not sufficient, when dealing with a list. It would be awkward to *add* some interest for
example.

For purposes like this, you can create your own handler inside some `Store` scope with one of the `handle`-factory
functions.

In this case we need the factory that takes first one `action`-parameter, which is simply our interest we want to add,
and as second parameter we provide an expression, that creates the new value of the store. It therefore gets two
parameters:
- the "old" value of the store
- the action parameter

We can determine the new state simply by adding the new interest to the existing list:

```kotlin
val storedInterests = object : RootStore<List<Interest>>(emptyList()) {

    val add: Handler<Interest> = handle { value, new ->
        if(state.contains(action)) state else state + action
    }
}
```

We can craft a small UI, where the user can click on one item of the list of all exsiting interests to add one to
his personal interests:
```kotlin
div { +"all interests:" }
ul {
    Interest.values().forEach { interest ->
        li {
            +interest.toString()
            clicks.map { interest } handledBy InterestsStore.add
            //     ^^^^^^^^^^^^^^^^ ^^^^^^^^^ ^^^^^^^^^^^^^^^^^^
            //     map click to a             connect the Flow with
            //     Flow of Interest           the custom handler
        }
    }
}
div { +"selected interests:" }
ul {
    storedInterests.data.renderEach { interest ->
        li { +interest.toString() }
    }
}
```

### Custom Data-Flow Property

Besides custom handlers a custom store is also a perfect place to hold custom `data`-flows, which do sorts like
mapping, filtering or other operations before the final result should get rendered.

Imagine you want to list the interests grouped by some criteria (which should in real world applications part of the
model of course). You could add a property to your store, that does the filtering and mapping:
```kotlin
val storedInterests = object : RootStore<List<Interest>>(emptyList()) {
    
    val noneProgramming: Flow<List<Interest>> = data // use the standard `data`-property as base
        .filter { interest -> interest.any { it == Interest.History || it == Interest.Sports } }
        .map { it.toString() }
    
    val add = handle // ...
}
```
And then you can use it for your UI rendering:
```kotlin
div { +"selected none programming interests:" }
ul {
    storedInterests.noneProgramming.renderEach { interest ->
        li { +interest }
    }
}
```

Of course, you could also do the intermediate operations in place, which might look like this:
```kotlin
div { +"selected none programming interests:" }
ul {
    // Don't do this in real world!
    storedInterests.data
        .filter { interest -> interest.any { it == Interest.History || it == Interest.Sports } }
        .map { it.toString() }
        .renderEach { interest ->
            li { +interest }
        }
}
```

Even though this works, the intermediate operations clutter the UI declaration and make it harder to read. A second
disadvantage is the absence of any meaningful naming; you have to understand all the filtering and mapping to 
understand, what would be rendered here. A property in a store always has a (meaningful) name.

:::info
As rule of thumb strive for dedicated properties inside a custom store, to bundle all the needed custom data-flows.
:::

## Essentials

### Flows

As fritz2 heavily depends on flows, introduced by [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines),
we want to look a little deeper into their concept and why we use them as important building block for the reactive
rendering.

A `Flow` is a time discrete stream of values.

Like a collection, you can use `Flow`s to represent multiple values, but unlike other collections like `List`s, for example,
the values are retrieved one by one. fritz2 relies on `Flow`s to represent values that change over time and lets you react
to them (your data-model for example) .

A `Flow` is built from a source which creates the values. This source could be your model or the events raised by an element,
for example. On the other end of the `Flow`, a simple function called for each element collects the values one by one.
Between those two ends, various actions can be taken on the data (formatting strings, filtering the values, combining values, etc).

The great thing about `Flow`s is that they are _cold_, which means that nothing is calculated before the result is needed.
This makes them perfect for fritz2's use case.

In Kotlin, there is another communication model called `Channel` which is the _hot_ counterpart of the `Flow`.
fritz2 only uses `Channel`s internally to feed the flows, so you should not encounter them while using fritz2.

To get more information about `Flow`s, `Channel`s, and their API,
have a look at the [official documentation](https://kotlinlang.org/docs/reference/coroutines/flow.html).

We use flows for the "output" of a store by its `data`-property and for 
[all HTML5-events](https://www.fritz2.dev/api/core/dev.fritz2.core/-with-events/index.html).

### Custom Handler in depth

### Ad Hoc Handler

### Extend None Custom (Local) Stores 

### Connecting stores to each other

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

### History in Stores

Sometimes you may want to keep the history of values in your `Store`, so you can navigate back in time to build an
undo-function or maybe just for debugging...

fritz2 offers a history service to do so.

```kotlin
val store = object : RootStore<String>("") {
    val history = history()
}
```

By default you synchronise the history with the updates of your `Store`,
so each new value will be added to the history automatically.

Calling `history(synced = false)`, you can control the content of the history by manually adding new entries. Call `push(entry)` to do so.

You can access the complete history via its `data` attribute as `Flow<List<T>>`
or by using `current` attribute which returns a `List<T>`. For your convenience `history` also offers
* a `Flow<Boolean>` called `available` representing if entries are available (e.g., to show or hide an undo button)
* a `back()` method to get the latest entry and remove it from the history
* a `clear()` method to clear the history

For a `Store` with a minimal undo function you just have to write:
```kotlin
val store = object : RootStore<String>("") {
    val history = history()

    // your handlers go here (add history.clear() here where suitable)

    val undo = handle { history.back() }
}

render {
    div("form") {
        input {
            value(store.data)
            changes.values() handledBy store.update
        }
        store.history.available.render {
            if(it) {
                button("btn") {
                    +"Undo"
                }.clicks handledBy store.undo
            }
        }
    }
}
```

### Track Processing State of Stores

When one of your `Handler`s contains long running actions (like server-calls, etc.) you might want to keep the user
informed about that something is going on.

Using fritz2 you can use the `tracker`-service to implement this:

```kotlin
val store = object : RootStore<String>("") {
    val tracking = tracker()

    val save = handle { model ->
        tracking.track("myTransaction") {
            delay(1500) // do something that takes a while
            "$model."
        }
    }
}

render {
    button("btn") {
        className(store.tracking.data.map {
            if(it) "spinner" else ""
        })
        +"save"
        clicks handledBy store.save
    }
}
```
The service provides you with a `Flow` representing the description of the currently running transaction or `null`.

Filter the `Flow` using the meta-data you chose when calling `track(meta-data)` if you want to react to only certain transactions.

Of course, you can also use the meta-data to show to the user what is currently running (in a status-bar for example).

Beware that you are responsible for handling exceptions, if you use an unsafe operation within the tracking scope.
The tracker is safe in the way, that it will catch any escaped exception, then stops the tracking, and finally it will
rethrow the former.

If you want your tracking to continue instead, just handle exceptions within the tracking scope.

## Advanced Topics

### EmittingHandler - Observer Pattern for Handlers

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
[validation example](/examples/validation) which uses connected
stores and validate a `Person` before adding it to a list of `Person`s.
