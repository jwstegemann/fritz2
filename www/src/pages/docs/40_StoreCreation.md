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
other approaches, there might be an arbitrary number of stores which you can connect to handle the overall state
together.

Before we start, let us recap some of the model types used for upcoming examples:
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

The simplest way to create a store is the `storeOf` factory:

```kotlin
render {
    // create a `Store` and pass some data as initial state into it:
    val storedInterest: Store<Interest> = storeOf(Interest.Programming)
    
    // a store can manage any complex type `T`
    val storedPerson: Store<Person> = storeOf(Person(1, "fritz2", 3, listOf(Interest.Programming)))
}
```

Once you have created the store, it can be used for...
- ... rendering the current state into your UI
- ... dealing with state changes triggered by (user) events.

This store supports the former functions by exposing the following two properties out of the box:
- `data`: This `Flow` of `T` can be used to render the current state into the UI by using any `render*`-function. 
This is described in the [reactive rendering](/docs/render/#reactive-rendering) sections of the 
[Render HTML](/docs/render) chapter.
- `update`: This is a `Handler` which manages the state changes of the store. This particular handler takes one `T` and
simply substitutes `T` for the old state of the store.

You can use this handler to conveniently implement _two-way data binding_ by using the `changes` event-flow
of an `input`-`Tag`, for example:

```kotlin
render {
    val store: Store<String> = storeOf("")

    input {
        // react to data changes and update the UI
        value(store.data)
        
        // react to UI events and update the data state
        changes.values() handledBy store.update
    }
}
```

`changes` in this example is a `Flow` of events created by listening to the `Change`-Event of the underlying 
input-element. Calling `values()` on it extracts the current value from the input.
Whenever such an event is raised, a new value appears on the `Flow` and is processed by the `update`-Handler of the
`Store` to update the model. Event-flows are available for
[all HTML5-events](https://www.fritz2.dev/api/core/dev.fritz2.core/-with-events/index.html).
There are some more [convenience functions](https://www.fritz2.dev/api/core/dev.fritz2.core/-listener/index.html) to
help you to extract data from an event or to control event-processing.

### Custom Store

In order to extend a store with custom handlers or data-flows, you can create your own store-object:
```kotlin
// use `RootStore` as base class - it implements all the functions you will rely on. 
val storedInterests = object : RootStore<List<Interest>>(emptyList(), job = Job()) {
    // fill with custom functionality
}
```

Of course, it is also possible to declare a class and create the object later:
```kotlin
class InterestsStore(initial: List<Interest> = emptyList()) : RootStore<List<Interest>>(initial, job = Job()) {
    // fill with custom functionality
}

// use it
val storedInterests = InterestsStore()
```

:::info
Don't be alarmed by the `job = Job()`-parameter inside the constructor of a `RootStore`. This is a crucial parameter
needed to integrate stores into the reactive rendering patterns of fritz2 - just accept it for now. There are dedicated
upcoming doc sections which explain how to deal with this parameter in different contexts.
:::

### Custom Handler

Our custom store already has the handler `update` which - as you already know - substitutes a new state for the 
old one. This is not sufficient when, for example, dealing with a list since it is not possible to simply add an 
element to the current list - the whole list must be substituted.

For purposes like this, you can create your own handler on a `Store` scope with one of the `handle`-factory
functions.

The `handle` function takes an `action` type-parameter, which in our case is simply the `Interest` we want to add. 
The resulting lambda function inside the `handle` factory then has the following two parameters:
- the old `state` of the store
- the `action` parameter

We can determine the new state by adding the new `Interest` to the existing list:

```kotlin
val storedInterests = object : RootStore<List<Interest>>(emptyList(), job = Job()) {

    val add: Handler<Interest> = handle { currentState: List<Interest>, action: Interest ->
        if(currentState.contains(action)) currentState else currentState + action
    }
}
```

Let's add a small UI in which the user can select one of all existing interests to add it to his personal interests:
```kotlin
div { +"All interests:" }
ul {
    Interest.values().forEach { interest ->
        li {
            +interest.toString()
            clicks.map { interest } handledBy storedInterests.add
            //     ^^^^^^^^^^^^^^^^ ^^^^^^^^^ ^^^^^^^^^^^^^^^^^^
            //     map click to a             connect the Flow with
            //     Flow of Interest           the custom handler
        }
    }
}
div { +"Selected interests:" }
ul {
    storedInterests.data.renderEach { interest ->
        li { +interest.toString() }
    }
}
```

### Custom Data-Flow Property

Besides custom handlers, a custom store is also a perfect place to hold custom `data`-flows which do things like
mapping, filtering, sorting, or other operations without immediately rendering the result to the ui.

Say you want to list the interests grouped by some criteria (which in real world applications should be part of the
model). Add a `Flow`-property to your store that does the filtering and mapping...
```kotlin
val storedInterests = object : RootStore<List<Interest>>(emptyList(), job = Job()) {
    
    val noneProgramming: Flow<List<Interest>> = data // use the standard `data`-property as base
        .filter { interest -> interest.any { it == Interest.History || it == Interest.Sports } }
        .map { it.toString() }
    
    val add = handle {...}
}
```
...and then use it to render the UI:
```kotlin
div { +"Selected none-programming interests:" }
ul {
    storedInterests.noneProgramming.renderEach { interest ->
        li { +interest }
    }
}
```

Of course, you could also do the intermediate operations in place, which might look like this:
```kotlin
div { +"Selected none programming interests:" }
ul {
    // Don't do this in the real world!
    storedInterests.data
        .filter { interest -> interest.any { it == Interest.History || it == Interest.Sports } }
        .map { it.toString() }
        .renderEach { interest ->
            li { +interest }
        }
}
```

But you don't want to do it this way. Even though this works, the intermediate operations clutter the UI declaration and 
make it harder to read. A second disadvantage is the absence of any meaningful naming - you have to understand all the 
filtering and mapping to understand what would be rendered here. A property in a store always has a (meaningful) name.

:::info
As rule of thumb strive for dedicated properties inside a custom store to bundle all the needed custom data-flows.
:::

## Essentials

### Flows

As fritz2 heavily depends on flows, introduced by [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines),
let's look a little deeper into this concept and why it is such an important building block for reactive
rendering.

A `Flow` is a time discrete stream of values.

Like a collection, you can use `Flow`s to represent multiple values, but unlike other collections like `List`s, for 
example, the values are retrieved one by one. fritz2 relies on `Flow`s to represent values that change over time and 
lets you react to them (your data-model for example) .

A `Flow` is built from a source which creates the values. This source could be your model or the events raised by an 
element, for example. On the other end of the `Flow`, a simple function called for each element collects the values one 
by one. Between those two ends, various actions can be taken on the data (formatting strings, filtering the values, 
combining values, etc).

The great thing about `Flow`s is that they are _cold_, which means that nothing is calculated before the result is needed.
This makes them perfect for fritz2's use case: 
It uses flows for the "output" of a store by its `data`-property and for 
[all HTML5-events](https://www.fritz2.dev/api/core/dev.fritz2.core/-with-events/index.html).

They do not consume any memory or CPU load until they get rendered: the mount-points consume them and pull new values.

In Kotlin, there is another communication model called `Channel` which is the _hot_ counterpart of the `Flow`.
fritz2 only uses `Channel`s internally to feed the flows, so you should not encounter them while using fritz2.

To get more information about `Flow`s, `Channel`s, and their API,
have a look at the [official documentation](https://kotlinlang.org/docs/flow.html).


### Custom Handler in Depth

As you have already learned from the [overview](#custom-handler), it often makes sense to write custom
handlers for a dedicated task.

There are three different variants of handler factories available which differ in the amount of parameters available
in the handle expression:

| Factory                        | Parameters in handle-expression | Use case                                                                                                                                                                                                                                  |
|--------------------------------|---------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Store<T>.handle<Unit>`        | `state: T`                      | New state can only be generated from the old one or some external source. There is no information from the event source available. Typical use cases are resetting to initial state or clearing the store.                                |
| `Store<T>.handle<A>`           | `state: T`, `action: A`         | New state can be based upon information passed from the event source. Typical use cases are updating some part of the overall state or adding or dropping a list item. The default handler `update` uses this, where its `A` is a `T`.    |
| `Store<T>.handleAndEmit<A, E>` | `state: T`, `action: A`         | New state can be based upon information passed from the event source. Typical use cases are updating some part of the overall state or adding or dropping a list item. This handler ist a `Flow` itself one can `emit` some value `E` on. |

We will look at the first two of them here; the [emitting-handler](#emittinghandler---observer-pattern-for-handlers) 
is a rather advanced topic and will be covered in another section.

Have a look at our [nested model](/examples/masterdetail) example as well.

#### Example Use Case

Imagine an application that manages a list of persons.
The application should allow the user to...
- ... add a new person
- ... add an interest to a person
- ... clear the list

First we need a store for a `List<Person>` where we can create the needed handlers, and some simple UI code which will
render all persons:

```kotlin
val storedPersons = object : RootStore<List<Person>>(emptyList(), job = Job()) {
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

#### Implement a Handler With Action

Now we can implement the first requirement: add a new person.
In order to do so, we will need a new `Person`-object to be passed into the handler. Inside the handle-code we can then
add this person to the existing list if the object is not already present in the list to avoid duplicates.

Thus, we need the typical `handle`-factory, which accepts an `action` parameter of any arbitrary 
type. The existing default handler `update` uses the `T` as action, as it simply substitutes the newly passed object 
for the old state of the same type. But an action can be any type and is not limited to anything.

In our case the type `T` of the store is `List<Person>`, but as action, a single 
`Person`-object will do. 

```kotlin
val storedPersons = object : RootStore<List<Person>>(emptyList(), job = Job()) {

    val addPerson: Handler<Person> = handle { persons, newPerson ->
        //                 ^^^^^^^            ^^^^^^^  ^^^^^^^^^
        //                 defines the        the      the new
        //                 type of parameter  "old"    person
        //                 to pass            state    to add
        if (persons.any { it.id == newPerson.id }) persons else persons + newPerson
        //  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^               ^^^^^^^^^^^^^^^^^^^
        //  use the "old" state and the action                  add the action to the
        //  to check for duplicates                             old state to create the new state
    }
}
```
The above handler-code shows a typical pattern: the new store's state is created by analyzing the "old" state first 
with information from the action, and then deciding to change the state or keep the old state.

Here's a UI that uses this handler:

```kotlin
val storedPersons = object : RootStore<List<Person>>(emptyList(), job = Job()) {
    val addPerson: Handler<Person> = handle { persons, newPerson ->
        if (persons.any { it.id == newPerson.id }) persons else persons + newPerson
    }
}

render {
    // Rendering of person omitted...
    
    section {
        button {
            +"Add Fritz"
            clicks.map {
                // define a static person; in real life this might be created by user input
                Person(1, "Fritz", setOf(Interest.Programming, Interest.History))
            } handledBy storedPersons.addPerson
            // In order to call `addPerson`, we need a `Flow<A>` as first parameter for `handledBy`
            // so the flow defines the action `A` which the handle-code will receive,
            // in this case a `Person`-object
        }
    }
}
```

#### Use Meta-Information in Action

In order to implement the second requirement, which is to add an interest to a person, we should
recap the shape of our model: `Person` is an entity, meaning it has a stable identifier. We can rely on the 
`Person.id`-property to determine which person will receive the newly added interest.

So our action consists of two parts:
- the id of the person to whom the interest will be added (meta-information)
- the interest to add (information)

For simple cases like this, a `Pair` is a sufficient choice to group the information. But of course you could also
create a (data) class or any other kotlin feature that fits.

```kotlin
val storedPersons = object : RootStore<List<Person>>(emptyList(), job = Job()) {
    val addPerson: Handler<Person> = {...}

    val addInterest: Handler<Pair<Int, Interest>> = handle { persons, (idForUpdate, newInterest) ->
        //                   ^^^^^^^^^^^^^^^^^^^                       ^^^^^^^^^^^^^^^^^^^^^^^^
        //                   combine meta-information                  destructure meta-information
        //                   with information                          and information
        //                   as action parameter                       for expressive naming
        persons.map { person ->
            if (person.id == idForUpdate) person.copy(interests = person.interests + newInterest) else person
            //               ^^^^^^^^^^^                                             ^^^^^^^^^^^
            //               use meta-information to determine the specific          use the information portion
            //               person that must be updated                             of action to update the person
        }
    }
}

render {
    // Rendering of person omitted...

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

#### Create Handler Without External Information

The last task is quite easy: it should be possible to clear the list of users.

We can set an empty list as the new store's state without any further information. Thus, the handler we must create does 
not need any action parameter at all.

fritz2 offers a special variant for cases like this, where the action type is `Unit`.

```kotlin
val storedPersons = object : RootStore<List<Person>>(emptyList(), job = Job()) {
    val addPerson: Handler<Person> = {...}
    val addInterest: Handler<Pair<Int, Interest>> = {...}

    val clear: Handler<Unit> = handle { emptyList() }
}

render {
    // Rendering of person omitted... 

    section {
        button {
            +"Clear Persons"
            clicks handledBy storedPersons.clear
        }
    }
}
```

Don't be fooled: Inside the handler-code, the "old" state would be available; we simply do not need it for our 
implementation. Just to make this more explicit, look at this:
```kotlin
val clear: Handler<Unit> = handle { persons ->
    console.log("Dropped list of ${persons.size} persons...")    
    emptyList() 
}
```

There are other use-cases where the access to the old state is definitely needed, and therefore you always have access.

#### Complete Example

Just to show you the final result en block:
```kotlin
val storedPersons = object : RootStore<List<Person>>(emptyList(), job = Job()) {

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
            clicks handledBy storedPersons.clear
        }
    }
}
```

### Calling a Handler Directly

If you need to purposefully fire an action somewhere inside a `RenderContext` or a `Store`, you can directly invoke
a `Handler`:
```kotlin
//call handler with data
someStore.someHandler(someAction)

//call handler without data
someStore.someHandler()
```

### Ad Hoc Handler - Handler Without a Store

It is possible in fritz2 to create handlers without any store.

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

As a typical use case, consider an event that triggers more than one handler, often from different stores:
```kotlin
// imagine one store for a `List<Person>` and one boolean store, that  
showSection.renderIf({it}) {
    section {
        button {
            +"Remove Fritz"
            clicks.map { 1 } handledBy { id ->
                // call arbitrary handlers (or any other code!)
                // this is guaranteed to be executed as a whole, even if the re-redendering is triggered first!
                storedPersons.removePerson(id)
                showSection.update(false)
            }
        }
    }
}
```


### Extend None Custom (Local) Stores 

Sometimes creating a [custom store](#custom-stores) is a bit overkill, or it's simply more feasible to rely on 
[store-mapping](/docs/storemapping/) which implies there is no custom store available.

In those cases - often in some small local code area - it is totally fine to customize a standard store created by 
`storeOf`-factory.

```kotlin
val storedPerson = storeOf(Person(1, "fritz2", emptySet()), job = Job())

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

As the `Handler`-factory functions are extension functions, it is possible to extend a closed store-object.

### Connecting Stores to Each Other

Most real-world applications contain multiple stores which need to be linked to properly react to model changes.

To make your stores interconnect, fritz2 allows calling `Handlers` of others stores directly with or
without a parameter.
```kotlin
object SaveStore : RootStore<String>("", job = Job()) {
    val save = handle<String> { _, data -> data }
}

object InputStore : RootStore<String>("", job = Job()) { 
    val input = handle<String> { _, input ->
        SaveStore.save(input) // call other store`s handler
        input // do not forget to return the new store state
    }
}
```

Another common pattern is connecting different stores inside the `init`-block of a `Store`:
```kotlin
object SaveStore : RootStore<String>("", job = Job()) {
    val save = handle<String> { _, data -> data }
}

object InputStore : RootStore<String>("", job = Job()) {
    
    init {
        data handledBy SaveStore.save
        // call any arbitrary number of connections
    }
}
```

Have a look at our [nested model](/examples/nestedmodel) example too.

### Dealing with Jobs of Stores aka Lifecycle Patterns

We already teased the mysterious `job`-parameter which every `RootStore` needs inside its constructor. Now that we
have introduced the key features of stores, it is time to learn about the *lifecycle* of stores.

This is an important aspect, as it might lead to *memory leaks* if applied the wrong way.

We won't discuss the `Job` type in depth here, as it is simply a type of the 
[Coroutine](https://kotlinlang.org/docs/coroutines-basics.html#an-explicit-job)-abstraction.
For the context of this doc, just understand that a `Job` allows fritz2 to start and also *terminate* all reactive 
actions inside its range.

Those are:
- all reactive renderings, that is all UI portions created inside some `render*()` call
- `Store`s through their `Flow`s and `Handler`s

In contrast to stores, there is no need for a `job`-parameter for the different variants of the `render`-functions, 
which you already read about in chapter [HTML Rendering](/docs/render/#reactive-rendering). There, jobs are created
by fritz2 internally.
They are managed in a completely user agnostic way, which reduces complexity (and is in fact the foundation of all 
fritz2's reactive "magic").

The situation is different for stores.

Rendering simply creates a tree of DOM-nodes with mount-points which act as anchors for the reactive parts. 
Stores conceptually do not form any similar data structure as a whole.

Application data is often unrelated to each other, and it is much easier to focus on a specific aspect
of a domain to model data. So there is no good reason to manage all those different kinds of data within one 
root-structure.
That is why fritz2's `Store`s do not force you to group all your data into one big artificial model.

This freedom comes at a price: The job-handling cannot be managed automatically by the framework.
This is however not as bad as it sounds.

There are basically two different kind of stores which you have to deal with:
- Global application stores that exist for the lifetime of an application.
- Local stores that exist only within some reactively rendered UI-portion.

Job-handling for both types is really easy!

#### Global Stores

Often applications have one or more global `Store`s which hold all kinds of data needed to drive the 
application. Imagine the current user, his authorities, user specific preferences, and possibly domain data, representing
the core content of your application - the customer of a CRM-application.

Those data could and should be stored in application wide `Store`s at a global level.

The most common use case is to define a `Store`-object that directly derives from `RootStore`, but any other construct
is also suitable.

For those cases simply create *one* new `Job` inside the constructor:
```kotlin
val storedInterests = object : RootStore<List<Interest>>(emptyList(), job = Job()) {
    //                                                                ^^^^^^^^^^^
    //                                                                only one new job-object is needed
}
```

There is nothing special to it: There is only one object created in memory for the new `Job`. It will never get stopped
by fritz2 and will therefore run forever. Don't be afraid to simply create new Jobs here without 
any management. Those global stores are intended to run for as long as the application runs. 
So there is no need to stop or cancel them, no matter whether you have one or dozens of these.


The factory function `storeOf` also needs the `job`-parameter if called globally (to be precise: outside
any `WithJob`-context):
```kotlin
val storedInterests = storeOf<List<Interest>>(emptyList(), job = Job())
```

#### Local Stores as Part of Reactive Rendering

On the other hand, there are `Store`s that handle data which is only needed inside a limited, reactively rendered
UI-portion and which should exist only while the surrounding `RenderContext` exists. Those kinds of stores
should always be properly destroyed when the surrounding `RenderContext` gets destroyed by a re-rendering action.

In order to achieve this behaviour, we can simply rely on the already managed `Job`-object of the surrounding
`RenderContext` and pass that to the local store:
```kotlin
// some global store:
val storedFruits = storeOf<List<String>>(listOf("apple", "banana", "raspberry"), job = Job())

render {
    storedFruits.data.renderEach { fruit -> // a new `RenderContext` is created here
        // some local store, tight to its reactively rendered context!
        val editStore = storeOf(false) // Look carefully: No explicit `job`-parameter needed!
        
        // further UI code...
        editStore.data.render { isEditable ->
            if(isEditable) ...
            else ...
        }
    }
}
```
Pay attention to the `storeOf`-factory function! This is an overloaded convenience variant defined onto
a `WithJob`-receiver. As the name suggests, `WithJob` holds a job, in this case the job of the `RenderContext`,
which implements `WithJob`-interface. This way, the job of the created store is automatically shared with the 
surrounding reactive context.

:::info
Always strive to use the `Job` of a surrounding `RenderContext` for locally defined `Store`s. Those jobs are managed
properly by fritz2, so the application behaves in a deterministic way and memory leaks are prevented.
:::

If you need to create a custom local `Store` without the use of the factory function, you can also grab the 
correct `Job` from the surrounding `RenderContext`:
```kotlin
render {
    storedFruits.data.renderEach { fruit -> 
        // a new `RenderContext` is created here, remember it "has" a `job` property you can reuse!
        
        val editStore = object : RootStore<Boolean>(false, job = job) {
            //                                                   ^^^
            //                                                   refer to the nearest receiving RenderContext
            //                                                   and grab its job. You could also choose more
            //                                                   precisely by refering to the labeled name:
            //                                                   `this@renderEach.job`.
        }
    }
}
```

### History in Stores

Sometimes you may want to keep the history of states in your store to navigate back in time to build an
undo-function, or maybe just for debugging...

fritz2 offers a `history` factory to do so.

```kotlin
val store = object : RootStore<String>("", job = Job()) {
    val history = history()
}
```

By default, the history is synchronized with the updates of your store, so each new state will be added to the history 
automatically.

By calling `history(synced = false)`, you can control the content of the history
and manually add new entries. Call `push(entry)` to do so.

You can access the complete history via its `data` attribute as `Flow<List<T>>`
or by using `current` attribute which returns a `List<T>`. For your convenience, `history` also offers
* a `Flow<Boolean>` called `available` representing if entries are available (e.g., to show or hide an undo button)
* a `back()` method to get the latest entry and remove it from the history
* a `clear()` method to clear the history

For a store with a minimal undo function, just write:
```kotlin
val storedData = object : RootStore<String>("", job = Job()) {
    val hist = history()

    // your handlers go here (add history.clear() here if suitable)

    val undo = handle { hist.back() }
}

render {
    div("form") {
        input {
            value(storedData.data)
            changes.values() handledBy storedData.update
        }
        storedData.hist.available.render {
            if(it) {
                button("btn") {
                    +"Undo"
                }.clicks handledBy storedData.undo
            }
        }
    }
}
```

### Track Processing State of Stores

When one of your handlers contains a long-running action (like a server-call, etc.) you might want to keep the user
informed that something is going on.

In fritz2, you can use the `tracker` factory to implement this:

```kotlin
val storedData = object : RootStore<String>("", job = Job()) {
    val tracking = tracker()

    val save = handle { model ->
        tracking.track() {
            delay(1500) // do something that takes a while
            "$model."
        }
    }
}

render {
    button("btn") {
        className(storedData.tracking.data.map {
            if(it) "spinner" else ""
        })
        +"save"
        clicks handledBy storedData.save
    }
}
```
The tracker provides you with a boolean `Flow` representing the state of the running transaction.

Keep in mind that you are responsible for handling exceptions when using unsafe operations within the tracking scope.
The tracker is safe in the way that it will catch any escaped exception, then stop the tracking, and finally rethrow the 
former.

If you want your tracking to continue instead, just handle exceptions within the tracking scope.

## Advanced Topics

### Use Store's Current Value Correctly - Bypass Complex Flow-Operations

Sometimes there is the need to access some `Store`'s current value in a safe but easy way. That is, why its API offers
a property called `current`, which allows exactly to safely get its value at that one moment of access.

Typical use cases are the mapping of `Flow`s, often event based flows, that should be mapped to some store's content
or where the content is needed for some further processing.

Consider some panel, that can be opened and closed by some button click. In order to toggle it, you must manipulate
some state holding store:
```kotlin
val toggle = storeOf(false) // `false`-> closed, `true` -> open

button {
    +"Toggle"
    clicks.map { !toggle.current } handledBy toggle.update
    //                  ^^^^^^^
    //                  get current value of the store to process it inside some flow-mapping
}

// finally render the panel depending on the state
toggle.data.renderIf({ it }) {
    div {
        // ...
    }
}
```

The key aspect is found inside the `clicks`-event handling: The event itself is not really useful. It is just needed
to get some `Flow` that listens to the click events and emit new values each time a user clicks the button. But in order
to toggle the state, we need to access the store's value, where `current` comes into play!

But to solve this without the `current`-property, we would have to apply typical
`Flow`-[combining functions](https://kotlinlang.org/docs/flow.html#composing-multiple-flows),
like `combine`, `flatMapLatest` and so on. So imagine a solution without `current`:
```kotlin
button {
    +"Toggle"
    clicks.combine(state.data) { _, value -> !value } handledBy state.update
}
```

This looks far more complicated and clutters the UI code.

::: warning
**Beware:** There are many situations, where using `current` is just false. Applying it in inappropriate situations
might lead to subtle and hard to find errors!

Never use `current` inside some `render`-function body for example!
:::

In order to use `current` in the right situations, just follow this rule of thumb:

::: info
Use `current` only in code blocks, that are executed within some **reactive** scope!
That is, only in some intermediate `Flow`-operations or inside some `Handler` code for example.
:::

You can find typical examples for using `current` inside of the fritz2 [headless-components](/headless), for example
in `Listbox`'s implementation that deals with `KeyEvents`. Similar examples can be found in other headless components.

### EmittingHandler - Observer Pattern for Handlers

In cases where you don't know which `Handler` of another store will handle the data exposed by your handler, you can use
the `EmittingHandler`, a type of handler that doesn't just take data
as an argument but also emits data on a new `Flow` for other handlers to receive.

Create such a handler by calling the `handleAndEmit<T>()` function instead of the usual `handle()` function and add the
offered data type to the type brackets:

```kotlin
val personStore = object : RootStore<Person>(Person(...), job = Job()) {
    val save = handleAndEmit<Person> { person ->
        emit(person) // emits current person
        Person(...) // return a new empty person (set as new store state)
    }
}
```
The `EmittingHandler` named `save` emits the saved `Person` on its `Flow`.
Another store can be set up to handle this `Person` by connecting the handlers:
```kotlin
val personStore = ... //see above

val personListStore = object : RootStore<List<Person>>(emptyList<Person>(), job = Job()) {
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

To see a complete example, visit our
[validation example](/examples/validation) which uses connected
stores and validates a `Person` before adding it to a list of `Person`s.

### React to Changes Inside the Store Itself - Skip Initial Data

If you need a handler's code to be executed whenever the model is changed, use the `drop(1)` function on the store's 
`data` Flow to skip the value used on store creation:

```kotlin
val store = object : RootStore<String>("initial", job = Job()) { // "initial" is on the flow unless it's dropped
    init {
        data.drop(1) handledBy {
            console.log("model changed to: $it") // start logging with the first real value change
        }
    }
}
```

### Externalize Initial Store Interactions

Large applications sometimes include a rather complex setup necessary to connect the main
application store with lots of smaller subdomain-specific stores. As described within the 
[section](/docs/createstores/#connecting-stores-to-each-other) about store connections, the `init`-block 
of a store is a good place to setup the whole interaction.

If those init expressions grow too large, it is good practice to use `private` methods to organize them in a useful 
manner. 
But whaf if they need to be moved out of the `Store`-object? The 'handledBy'-function within a `RootStore` is 
`protected` and can therefore only be used within the `RootStore` itself or any derived custom store-implementation. 
A `RootStore` as receiver - e.g. using extension functions or apply/run - is not sufficient,
If you explicitly want to use the store-job outside the `RootStore`, you have to create an extension function 
with an `WithJob` receiver and call that function within the `RootStore` wrapped with the new `runWithJob`-function.

Example:
```kotlin
object MyStore : RootStore<String>("", job = Job()){
    init {
        runWithJob{ myFunction() }
    }
}

fun WithJob.myFunction() {
    flowOf("ABC") handledBy MyStore.update
}
```
