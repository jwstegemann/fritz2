---
layout: layouts/docsWithContentNav.njk
title: State Management
permalink: /docs/statemgt/
eleventyNavigation:
    key: state-management
    parent: documentation
    title: State Management
    order: 70
---

## State Management

Building your `Store`, you can add `Handler`s to respond to actions and adjust your model accordingly:

```kotlin
val store = object : RootStore<String>("") {
    val append = handle<String> { model, action: String ->
        "$model$action"
    }
    
    val remove = handle<Int> { model, action ->
        model.dropLast(action)
    }

    val clear = handle { model ->
        ""
    }
}
```
Whenever a `String` is sent to the `append`-`Handler`, it updates the model by appending the text to its current model.
`remove` is a `Handler` which needs the amount of characters as `Int` to drop from the data, so the type of the action 
is not related to the type of the store itself!
`clear` is a `Handler` that doesn't need any information to do its work, so the second parameter can be omitted.

Since everything in fritz2 is reactive, most of the time you want to connect a `Flow` of actions to the `Handler`
by calling the `handledBy` function, which can be called with
[infix](https://kotlinlang.org/docs/functions.html#infix-notation) so the code reads much nicer.

But it is also possible to call a handler directly but much less needed than the first option.

```kotlin
// use this pattern in most situations 
someFlowOfString handledBy store.append

// but direct call is also possible
store.append("someValueOfString")
```

Each `Store` inherits a `Handler` called `update`, accepting the same type as the `Store` as its action.
It updates the `Store`'s value to the new value it receives.
You can use this handler to conveniently implement _two-way-databinding_ by using the `changes` event-flow
of an `input`-`Tag`, for example:

```kotlin
val store = storeOf("") // store: RootStore<String>

render {
    input {
        value(store.data)
        changes.values() handledBy store.update
    }
}
```

`changes` in this example is a `Flow` of events created by listening to the `Change`-Event of the underlying input-element. 
Calling `values()` on it extracts the current value from the input.
Whenever such an event is raised, a new value appears on the `Flow` and is processed by the `update`-Handler of the 
`Store` to update the model. Event-flows are available for 
[all HTML5-events](https://api.fritz2.dev/core/core/dev.fritz2.dom/-with-events/index.html).
There are some more [convenience functions](https://api.fritz2.dev/core/core/dev.fritz2.dom/index.html) to help you to extract data 
from an event or control event-processing.

You can map the elements of the `Flow` to a specific action-type before connecting it to the `Handler`. 
This way you can also add information from the rendering-context to the action. 
You may also use any other source for a `Flow` like recurring timer events or even external events.

If you need to purposefully fire an action at some point in your code (to init a `Store` for example) use 
```kotlin
//call handler with data
someStore.someHandler(someValue)

//call handler without data
someStore.someHandler()
```

If you need its handler's code to be executed whenever the model is changed, 
you have to use the `syncBy` function:

```kotlin
val store = object : RootStore<String>("initial") {
    val logChange = handle { model ->
        console.log("model changed to: $model")
        model
    }

    init {
        syncBy(logChange)
    }
}
```

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

## History in Stores

Sometimes you may want to keep the history of values in your `Store`, so you can navigate back in time to build an undo-function or maybe just for debugging...

fritz2 offers a history service to do so.

```kotlin
val store = object : RootStore<String>("") {
    val history = history<String>().sync(this)
}
```

This way you synchronise the history with the updates of your `Store`, so each new value will be added to the history automatically.

Without `sync()`, you have to add new entries to the history manually by calling `history.add(entry)`.

You can access the complete history via its `Flow` as a `List` of entries. For your convenience `history` also offers
* a `Flow<Boolean>` called `available` representing if entries are available (e.g., to show or hide an undo button)
* a `last()` method to access the latest entry
* a `back()` method to get the latest entry and remove it from the history
* a `reset()` method to clear the history

So for a `Store` with a minimal undo function you just have to write:

```kotlin
val store = object : RootStore<String>("") {
    val history = history<String>().sync(this)
    
    // your handlers go here (add history.reset() here where suitable)

    val undo = handle {
        history.back()
    }
}

...

render {
    div("form") {
        // insert your form here
    
        button("btn") {
            className(store.history.available.map { if (it) "" else "hidden" })
            +"Undo"
            
            clicks handledBy store.undo
        }
    }
}
```

To see a more mature undo function in action, go to our [repositories example](https://examples.fritz2.dev/repositories/build/distributions/index.html).

## Track Processing State of Stores

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

Our [repositories example](https://examples.fritz2.dev/repositories/build/distributions/index.html) uses tracking, to show you a spinning wheel when you click on the save button.

