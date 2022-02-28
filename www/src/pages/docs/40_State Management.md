---
layout: layouts/docsWithContentNav.njk
title: State Management
permalink: /docs/state/
eleventyNavigation:
    key: state
    parent: documentation
    title: State Management
    order: 40
---
## Flows

fritz2 heavily depends on flows, introduced by [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines).

A `Flow` is a time discrete stream of values.

Like a collection, you can use `Flow`s to represent multiple values, but unlike other collections like `List`s, for example,
the values are retrieved one by one. fritz2 relies on `Flow`s to represent values that change over time and lets you react to them (your data-model for example) .

A `Flow` is built from a source which creates the values. This source could be your model or the events raised by an element,
for example. On the other end of the `Flow`, a simple function called for each element collects the values one by one.
Between those two ends, various actions can be taken on the data (formatting strings, filtering the values, combining values, etc).

The great thing about `Flow`s is that they are _cold_, which means that nothing is calculated before the result is needed.
This makes them perfect for fritz2's use case.

In Kotlin, there is another communication model called `Channel` which is the _hot_ counterpart of the `Flow`.
fritz2 only uses `Channel`s internally to feed the flows, so you should not encounter them while using fritz2.

To get more information about `Flow`s, `Channel`s, and their API,
have a look at the [official documentation](https://kotlinlang.org/docs/reference/coroutines/flow.html).

Now you have seen how fritz2 handles events and the state of your app.

## Rendering

A mount-point in fritz2 is an anchor of a `Flow` somewhere in a structure like the DOM-tree. Afterwards, each value
appearing on the mounted `Flow` will be put into the structure at exactly that position replacing the former value.

Most of the time you will use mount-points in the browser's DOM, allowing you to mount `Tag`s to some point in the
html-structure you are building using for example the `someFlow.render {}` function.

Inside the `RenderContext` opened by `someFlow.render {}`, a new mount-point is created as a `<div>`-tag and
added to the current parent-element. Whenever a new value appears on the `Flow`, the new content is rendered
and replaces the old elements.

In this `RenderContext`, any number of root elements can be created (also none).
```kotlin
someIntFlow.render {
    if(it % 2 == 0) p { +"is even" }
    // nothing is rendered if odd        
}
// or multiple elements
someStringFlow.render { name ->
    h5 { +"Your name is:" }
    div { +name }
    hr {}
}
```
The latter example will result in the following DOM structure:
```html
<div class="mount-point" data-mount-point> <!-- created by `render` function -->
  <h5>Your name is:</h5>
  <div>Chris</div>
  <hr/>
</div>
```
The CSS-class `mount-point` consists only of a `display: contents;` directive so that the element will not appear
in the visual rendering of the page.

Whenever the mount-point is definitely the only sub-element of its parent element, you can omit the dedicated
`<div>`-mount-point-tag by setting the `into` parameter to the parent element. In this case the rendering engine
uses the existing parent node as reference for the mount-point:
```kotlin
render {
    dl { // `this` is <dl>-tag within this scope
        flowOf("fritz2" to "Awesome web frontend framework").render(into = this) { (title, def) ->
            //                                                      ^^^^^^^^^^^
            //                        define parent node as anchor for mounting    
            dt { +title }
            dd { +def }
        }
    }
}
```
This will result in the following DOM structure:
```html
<dl data-mount-point> <!-- No more dedicated <div> needed! Data attribute gives hint that tag is a mount-point -->
  <dt>fritz2</dt>
  <dd>Awesome web frontend framework</dd>
</dl>
```

## Render Lists

In fritz2 you can render out every type of data in a `Flow` including lists:

```kotlin
val listFlow = flowOf(listOf("a", "b", "c"))

listFlow.render { list ->
    list.forEach {
        span { +it }
    }
}
```

But keep in mind that this means re-rendering *all* `span`s in this example when the list changes, regardless of how
many items you actually changed. This might be what you want for small `List`s,
for `List`s that rarely change, or for `List`s with a small representation in HTML (like just text per item), etc.
However, for `List`s that change more often and/or result in complex HTML-trees per item, this does not perform well.

For those cases, fritz2 offers the method `renderEach {}` which creates a `RenderContext` and mounts its result to the DOM.
`renderEach {}` works like `render {}`, but it creates a specialized mount-point in order to
identify elements for re-rendering. This mount-point compares the last version of your list with the
new one on every change and applies the minimum necessary patches to the DOM.

```kotlin
val listFlow = flowOf(listOf("a", "b", "c"))

listFlow.renderEach {
    span { +it }
}
```

## Store
In fritz2, `Store`s are used to handle your app's state.
Let's assume the state of your app is a simple `String`.
Creating a `Store` to manage that state is quite easy:

```kotlin
val s = storeOf("initial value")
```

Every `Store` offers a `Flow` named `data` which can be bound as part of your html:

```kotlin
render {
    p {
        s.data.renderText()
    }
}
```

By calling `s.data.renderText()` a mount-point is created and collects your model values.
This means a DOM-element is created (in this example it's a `span` with the text as text-node) and
bound to your `data` so that it will change whenever your `Store`'s state updates. This is called _precise data binding_.

You can of course use every intermediate action like `map`,`filter`, etc., on the `data`-flow as on every other `Flow`:

```kotlin
render {
    p {
        s.data.map { "you have entered ${it.length} characters so far." }.renderText()
    }
}
```

To combine data from two or more stores you can use the `combine` method:
```kotlin
val firstName = object : RootStore<String>("Foo") {}
val lastName = object : RootStore<String>("Bar") {}

render {
    p {
        firstName.data.combine(lastName.data) { firstName, lastName ->
            "Your full name is: $firstName $lastName"
        }.renderText()
    }
}
```
Of course, you can also use a `RootStore<T>` with a complex model which contains all data that you need in one place.

You can also bind a `Flow` to an attribute:
```kotlin
input {
    value(store.data)
}
```
In this case, only the attribute value will change when the model in your store changes.

## Using Handlers

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
[all HTML5-events](https://www.fritz2.dev/api/core/dev.fritz2.core/-with-events/index.html).
There are some more [convenience functions](https://www.fritz2.dev/api/core/dev.fritz2.core/-listener/index.html) to 
help you to extract data from an event or control event-processing.

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

## History in Stores

Sometimes you may want to keep the history of values in your `Store`, so you can navigate back in time to build an 
undo-function or maybe just for debugging...

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

## Complex Data-Models

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

Have a look at the [validation-example](https://examples.fritz2.dev/validation/build/distributions/index.html)
to see how to set it up.

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
you can of course iterate over it by using `renderEach {}`.
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

## Formatting Values

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

## Improve Rendering

```kotlin
object SeqStore : RootStore<List<String>>(listOf("one", "two", "three")) {
    var count = 0

    val addItem = handle { list ->
        count++
        list + "Yet another item: No. $count"
    }
    val deleteItem = handle<String> { list, current ->
        list.minus(current)
    }
}

fun main() {
    render {
        section {
            ul {
                SeqStore.data.renderEach { s ->
                    li {
                        button("btn", id = "delete-btn") {
                            +s
                            clicks.map { console.log("Deleting $s"); s } handledBy SeqStore.deleteItem
                        }
                    }
                }
            }
            button("button") {
                +"Add an item"
                clicks handledBy SeqStore.addItem
            }
        }
    }
}
```

In this example, `renderEach` uses the equals function to determine whether an item at a given index is still the same.
So for the `String` example, renderEach won't patch your DOM when "two" is replaced by another instance of String
with the same content. It will however remove the `li` representing "two" when the item at index 1 is replaced
by "another two" and a newly rendered `li` is inserted with this text content. So be aware that the lambda you pass
to `renderEach` is executed whenever the DOM representation of a new or changed item is rendered.

When dealing with more complex data models, this sometimes isn't what you want.

```kotlin
@Lenses
data class ToDo(
    val id: Int,
    val text: String,
    val completed: Boolean
) {
    companion object
}
```
When you have a `ToDo` like this in your list, which is rendered by..

```kotlin
val toDoListStore = storeOf(listOf(ToDo(1, "foo", false), ToDo(2, "bar", false)))

fun main() {
    render {
        section {
            toDoListStore.data.renderEach(ToDo::id) { toDo ->
                val toDoStore = toDoListStore.sub(toDo, ToDo::id)
                li {
                    val completed = toDoStore.sub(ToDo.completed()).data
                    // css is based on https://tailwindcss.com
                    className(completed.map { if (it) "line-through" else ""})
                    +toDo.text
                }
            }
        }
    }
}
```

you might just want the `class`-attribute to be re-rendered when the `ToDo` at a given index is still the same,
but just the value of its `completed` state changes. `renderEach` must be told how to determine, whether an element
at an index is still the same entity although one or more of its attributes (or sub-entities) have changed by
passing an `IdProvider`.

An `IdProvider` is a function mapping an entity to a unique id of arbitrary type. In this example, we just use the
`id`-attribute of the `ToDo`.

Then create a `SubStore` for a given entity conveniently by calling `sub(someEntity, properIdProvider)` on
a `Store<List<*>>`. Of course you can do this yourself by mapping the flows if you work on a `Flow<List<*>>` and
have no `Store` available or don't want to utilize fritz2's `Lens`es:

```kotlin
val completed = toDoListStore.data.map { it.find { t -> t.id == toDo.id } ?: false }
```

Regardless if you use a `SubStore` or your mapped `Flow`: be aware that in the example above nothing will happen to
your DOM, when the text of a `ToDo` changes, but it is still at the same position in the list. fritz2 determines
that still the same entity (identified by the `IdProvider`) is at the same place in the list and therefore the `li`
as a whole won't be re-rendered, but just the `class`-attribute (which is mounted to its own mountpoint depending
on you `completed`-flow). This might be exactly what you want, but it depends on your use-case.

You can easily do two-way-databinding inside `renderEach` using a `SubStore` created for a particular entity as seen
above. This of course only makes sense in combination with an `IdProvider`:

```kotlin
val toDoListStore = storeOf(listOf(ToDo(text = "foo"), ToDo(text = "bar")))

fun main() {
    render {
        section {
            toDoListStore.data.renderEach(ToDo::id) { toDo ->
                val toDoStore = toDoListStore.sub(toDo, ToDo::id)
                li {
                    input {
                        value(toDoStore.data.map { it.text })
                        changes.values() handledBy toDoStore.sub(ToDo.text).update
                    }
                }
            }
        }
    }
}
```

If you need two-way-databinding directly on a `Store`'s `data` without any intermediate operations
(filters, maps, etc.), call `renderEach(IdProvider)` directly on the `Store`. This will provide the `SubStore` as
the render-lambda's parameter for each element.

Now you know how to handle all kinds of data and structures in your `Store`s.
Next, you might want to check whether your model is valid. In fritz2 this is done with [Validation](/docs/validation).