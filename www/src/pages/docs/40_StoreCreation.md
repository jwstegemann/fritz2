---
title: Store Creation
description: "Learn how to create Stores"
layout: layouts/docs.njk
permalink: /docs/createstores/
eleventyNavigation:
    key: createstores
    parent: documentation
    title: Store Creation
    order: 40
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

### Custom Store

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
This makes them perfect for fritz2's use case: 
It uses flows for the "output" of a store by its `data`-property and for 
[all HTML5-events](https://www.fritz2.dev/api/core/dev.fritz2.core/-with-events/index.html).

They do not consume any memory or CPU load, until they get rendered: The mount-points consumes them and pull new values.

In Kotlin, there is another communication model called `Channel` which is the _hot_ counterpart of the `Flow`.
fritz2 only uses `Channel`s internally to feed the flows, so you should not encounter them while using fritz2.

To get more information about `Flow`s, `Channel`s, and their API,
have a look at the [official documentation](https://kotlinlang.org/docs/flow.html).


### Custom Handler in Depth

As you have already learned from the [overview](#custom-handler) it often makes sense to write custom
handlers for a dedicated task.

There are three different variants of handler factories available, which differ in the amount of parameters available
in the handle expression:

| Factory                        | Parameters in handle-expression | Use case                                                                                                                                                                                                                               |
|--------------------------------|---------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Store<T>.handle<Unit>`        | `value: T`                      | New state can only be generated from the old one or some external source. There is no information from the event source available. Typical use cases are resetting to initial state or clearing the store.                             |
| `Store<T>.handle<A>`           | `value: T`, `action: A`         | New state can be based upon information passed from the event source. Typical use cases are updating some part of the overall value or adding or dropping a list item. The default handler `update` uses this, where its `A` is a ``T. |
| `Store<T>.handleAndEmit<A, E>` | `value: T`, `action: A`         | New state can be based upon information passed from the event source. Typical use cases are updating some part of the overall value or adding or dropping a list item. As bonus one can `emit` some value `E`.                         |

We will look at the first two of them here; the [emitting-handler](#emittinghandler---observer-pattern-for-handlers) 
is rather an advanced topic and covered in a dedicated section there.

Have a look at our [nested model](/examples/masterdetail) example too.

#### Example Use Case

Let's imagine some application that manages a list of persons.
The application should allow to...
- ... add a new person
- ... add some interest to a specific person
- ... clear the whole list

First we need a store for a `List<Person>`, where we can create the needed handlers and some simple UI code, that will
render all persons:

```kotlin
val storedPersons = object : RootStore<List<Person>>(emptyList()) {
    // here we will place our custom handlers
}

render {
    section {
        h1 { +"Persons:" }
        ul {
            storedPersons.data.renderEach { person ->
                dl {
                    dt { +"Id:" }
                    dd { +person.id.toString() }
                    dt { +"Name:" }
                    dd { +person.name }
                    dt { +"Interests:" }
                    dd { +person.interests.joinToString() }
                }
            }
        }
    }
}
```

#### Implement some Handler with Action

Now we can implement the first requirement: Add a new person.
In order to do so, we will need a new `Person`-object to be passed into the handler. Inside the handle-code we can then
add this person to the existing list, if the object is not already present in the list to avoid duplicates.

Thus, we need the typical `handle`-factory, which accepts a so-called *action* parameter. This could be an arbitrary 
type `A`. The existing default handler `update` uses the `T` as action, as it simply substitutes the old state by the
passed new object of the same type. But an action can be any type and is not limited to anything.

In our case the type `T` of the store is `List<Person>`, but as action it is sufficient to have a single 
`Person`-object. 

```kotlin
val storedPersons = object : RootStore<List<Person>>(emptyList()) {

    val addPerson: Handler<Person> = handle { persons, newPerson ->
        //                 ^^^^^^^            ^^^^^^^  ^^^^^^^^^
        //                 defines the        the      the new
        //                 type of parameter  "old"    person
        //                 to pass            value    to add
        if (persons.any { it.id == newPerson.id }) persons else persons + newPerson
        //  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^               ^^^^^^^^^^^^^^^^^^^
        //  use the "old" value and the action                  add the action to the
        //  to check for duplicates                             old value to create the new value
    }
}
```
The above handle-code shows some typical pattern: We create the new store's value by analyzing first the "old" value
with some information from the action and then decide whether the old state could remain or there must be some update
also using some information from the action.

Let's "simulate" some UI, that uses this handler:

```kotlin
val storedPersons = object : RootStore<List<Person>>(emptyList()) {
    val addPerson: Handler<Person> = handle { persons, newPerson ->
        if (persons.any { it.id == newPerson.id }) persons else persons + newPerson
    }
}

render {
// (rendering of person omitted) 

    section {
        button {
            +"Add Fritz"
            clicks.map {
                // we define some static person; in real life this might be created by some user input
                Person(1, "Fritz", setOf(Interest.Programming, Interest.History))
            } handledBy storedPersons.addPerson
            // in order to call `addPerson`, we need a `Flow<A>` as first parameter for `handledBy`
            // So the flow defines the action `A`, that the handle-code will receive,
            // in this case some `Person`-object
        }
    }
}
```

#### Use Meta-Information in Action

In order to implement the second requirement, which is to add some interest to some specific person, we should
recap the shape of our model: `Person` is an *entity*, so it has some stable identifier. To determine the person in
the list, which some new interest should be added to this person's interests list, we simply can rely on the
`Person.id`-property.

So our action consists of two parts:
- the new interest: meta-information to help to identify a person
- the id of the specific person: the real information payload, that will be added to person's interests list

For simple cases like this, a `Pair` is a sufficient choice to group both information. But of course you could also
create some (data) class or any other kotlin feature that fits.

```kotlin
val storedPersons = object : RootStore<List<Person>>(emptyList()) {
    val addPerson: Handler<Person> = // ...

    val addInterest: Handler<Pair<Int, Interest>> = handle { persons, (idForUpdate, newInterest) ->
        //                   ^^^^^^^^^^^^^^^^^^^                       ^^^^^^^^^^^^^^^^^^^^^^^^
        //                   combine information                       destructure information
        //                   with meta-information                     and meta-information
        //                   as action parameter                       for expressive naming
        persons.map { person ->
            if (person.id == idForUpdate) person.copy(interests = person.interests + newInterest) else person
            //               ^^^^^^^^^^^                                             ^^^^^^^^^^^
            //               use meta-information to determine the specific          use the information portion
            //               person that must get an update                          from action to update the person
        }
    }
}

render {
    // (rendering of person omitted) 

    section {
        button {
            +"Make Fritz write Documentation"
            clicks.map {
                // create the Pair of meta-information and information
                1 to Interest.WritingDocumentation
            } handledBy storedPersons.addInterest
        }
    }
}
```

#### Create Handler without external Information

The last task is quite easy: It should be possible, to clear the complete list of users.

We can set an empty list as new store's value without any further information. Thus, the handler we must create does not
need any action parameter at all.

fritz2 offers a special variant for cases like this, where the action type is `Unit`.

```kotlin
val storedPersons = object : RootStore<List<Person>>(emptyList()) {
    val addPerson: Handler<Person> = // ...
    val addInterest: Handler<Pair<Int, Interest>> = // ...

    val clear: Handler<Unit> = handle { emptyList() }
}

render {
    // (rendering of person omitted) 

    section {
        button {
            +"Clear Persons"
            clicks.map { } handledBy storedPersons.clear
            //     ^^^^^^^^
            //     use empty mapping to create `Flow<Unit>`!
        }
    }
}
```

Don't be fooled: Inside the handle-code the "old" value would be available; we simply do not need it for our 
implementation. Just to make this more explicit, look at this:
```kotlin
val clear: Handler<Unit> = handle { persons ->
    console.log("Dropped list of ${persons.size} persons...")    
    emptyList() 
}
```

There are other use-cases where the access to the old state is definitely needed, and you always have access there.

#### Complete Example

Just to show you the final result en block:
```kotlin
val storedPersons = object : RootStore<List<Person>>(emptyList()) {

    val addPerson: Handler<Person> = handle { persons, newPerson ->
        if (persons.any { it.id == newPerson.id }) persons else persons + newPerson
    }

    val addInterest: Handler<Pair<Int, Interest>> = handle { persons, (idForUpdate, newInterest) ->
        persons.map { person ->
            if (person.id == idForUpdate) person.copy(interests = person.interests + newInterest) else person
        }
    }

    val clear: Handler<Unit> = handle { emptyList() }
}

render {
    section {
        h1 { +"Persons:" }
        ul {
            storedPersons.data.renderEach { person ->
                dl {
                    dt { +"Id:" }
                    dd { +person.id.toString() }
                    dt { +"Name:" }
                    dd { +person.name }
                    dt { +"Interests:" }
                    dd { +person.interests.joinToString() }
                }
            }
        }
    }
    section {
        button {
            +"Add Fritz"
            clicks.map {
                Person(1, "Fritz", setOf(Interest.Programming, Interest.History))
            } handledBy storedPersons.addPerson
        }
        button {
            +"Make Fritz write Documentation"
            clicks.map {
                1 to Interest.WritingDocumentation
            } handledBy storedPersons.addInterest
        }
        button {
            +"Clear Persons"
            clicks.map { } handledBy storedPersons.clear
        }
    }
}
```

### Calling a Handler directly

If you need to purposefully fire an action somewhere inside a `RenderContext` or a `Store` you can directly invoke
a `Handler`:
```kotlin
//call handler with data
someStore.someHandler(someValue)

//call handler without data
someStore.someHandler()
```

### Ad Hoc Handler - Handler without a Store

It is possible in fritz2 to create some handler without any store.

Common use cases are the combination of multiple handler calls based upon the same event or to simply create a working
placeholder before the store is ready.

The syntax is simple: Just pass an expression of type `(A) -> Unit` to the `handledBy`-function, where `A` is the 
inner type of the flow.

Look at this example:
```kotlin
section {
    button {
        +"Remove Fritz"
        clicks.map { 1 } handledBy { id ->
            // `removePerson` handler is not ready yet; but UI should be checked to work
            console.log("Would delete person with id=$id")
        }
    }
}
```

### Extend None Custom (Local) Stores 

Sometimes creating a [custom store](#custom-stores) is kinda overkill or there are certain situations, where it is
simply more feasible to rely on [store-mapping](/docs/storemapping/) which implies there is no custom store available.

In those cases - often in some small local code area - it is totally ok, to customize a standard store created by 
`storeOf`-factory.

```kotlin
val storedPerson = storeOf(Person(1, "fritz2", emptySet()))

// define handler on some store object
val addInterest = storedPerson.handle<Interest> { person, interest ->
    person.copy(interests = person.interests + interest)
}

render {
    button {
        +"Make fritz write documentation"
        clicks.map { Interest.WritingDocumentation } handledBy addInterest
    }
}
```

As the `handler`-factory functions are *extension* functions, it is possible to extend a "closed" store-object.

### Connecting Stores to Each Other

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

Have a look at our [nested model](/examples/nestedmodel) example too.

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
in `personListStore`. All dependent components will be updated accordingly.

To see a complete example visit our
[validation example](/examples/validation) which uses connected
stores and validate a `Person` before adding it to a list of `Person`s.

### React to Changes inside the Store itself

If you need a handler's code to be executed whenever the model is changed,
you have to use the `drop(1)` function on a `Flow` to skip the `initialData`:

```kotlin
val store = object : RootStore<String>("initial") {
    init {
        data.drop(1) handledBy {
            console.log("model changed to: $it")
        }
    }
}
```

By using the ad-hoc `handledBy` function here your store gets not updated after new data arrives.
