---
title: Fundamentals
description: "Learn about the basic concepts of fritz2"
layout: layouts/docs.njk
permalink: /docs/fundamentals/
eleventyNavigation:
    key: fundamentals
    parent: documentation
    title: Fundamentals
    order: 20
---

## What is fritz2?

It helps to understand what *fritz2* is in the first place. What does it do? What problems does it help me solve?
Why would I want to use it?

We would like to show you the basic concepts, explain what they are used for and to show you the beauty and elegance
of the applied framework API. 

**fritz2 is a lightweight, yet fully functional framework to build reactive web apps in Kotlin.**

The main concepts of fritz2 are:
- Declarative UI Creation: This is achieved through HTML tag alike factories that can be nested in order to resemble
the DOM structure very closely.
- State Handling: `Store`s take care of the data and offer functions to update the data by UI events (`handler`s) 
and to use the data to `render` some UI portions *reactively*.

Looks quite simple you think? In fact it is! The main principles and concepts are that simple, that's why we consider
fritz2 rather *lightweight*. After getting the first overview in this chapter, you will just learn more about 
convenience functions that makes writing real world apps more pleasant in the following chapters.

You will be also able to understand the following picture, that shows the above concepts embedded into the overall
data flow. We will refer to this later on! 

![state management in fritz2](/img/fritz2_cycle_of_life.svg =640x)

:::info
Please have a look at our running [getting started example](/examples/gettingstarted/) too. It covers nearly the same
topics and offers almost the same functionalities.
:::

## Teasing Example

Let's dive into a small example to demonstrate the *fundamental* concepts and functions.

Assume some input field, where a user can enter some text. The text should then be displayed in some section below.
As small bonus, we consider a button, which will clear the input:

```kotlin
import dev.fritz2.core.*

// some styling is omitted; have a look at the very complete example at the end of this chapter!

fun main() {
    val store = storeOf("Hello, fritz2!")

    render {
        div("w-48 m-4 flex flex-col gap-2") {
            label {
                +"Input"
                `for`(store.id)
            }
            input(id = store.id) {
                placeholder("Add some input")
                value(store.data)
                changes.values() handledBy store.update
            }
            p { +"Value" }
            store.data.render { content ->
                p{ +content }
            }
            button {
                +"Clear"
                clicks handledBy store.handle { "" }
            }
        }
    }
}
```
This is the rendered structure of the main part below the `body` tag:

```html
<div class="w-48 m-4 flex flex-col gap-2">
    <label for="dp9C88">Input</label>
    <input id="dp9C88" placeholder="Add some input" value="Hello, fritz2!">
    <p>Value</p>
    <div class="mount-point" data-mount-point="">
        <p>Hello, fritz2!</p>
    </div>
    <button>Clear</button>
</div>
```

Although this is rather some enhanced "hello-world"-example, it demonstrates almost all the fundamental concepts and
parts of fritz2!

Let's have a look at the result in a browser:

![running example in browser](/img/fundamentals_animation.gif)

Now we have a good notion of the app and how it works, let's dive into the code and discover the various building 
blocks, that drives this app.

:::warning
Just as short remark: We use [tailwindcss](https://tailwindcss.com) for the styling in this example. As fritz2 is 
totally agnostic of any CSS styling framework, you are free to use any other CSS framework or even handcrafted CSS, 
if that fit your needs best! 
The [running examples](/examples) are often built with [bootstrap](https://getbootstrap.com/) for example.
:::

### Starting point

The fritz2 framework requires very low ceremony to set up an application. Just call once inside your code the global 
`render` function, to create an initial so called `RenderContext`. Think of it as a context, where you can place all
your UI elements, that is HTML `tags` in the end.

```kotlin
import dev.fritz2.core.*

fun main() {
    // call the render function *once* to create an initial `RenderContext` you can render your UI into!
    render { /* `this` is a `RenderContext` */
        // starting from within this root context, declare the whole application's UI!
    }
}
```

### UI-Elements aka HTML-Tags

Inside a `RenderContext` fritz2 offers factory functions for all HTML elements, like `div`, `span`, `p` and so on.
Those functions always create some special `Tag` implementation, which itself is just a new `RenderContext`. This 
enables of course the *nesting* of factories and leads to a *declarative* way of defining your UI.

The declarative calling of tag factories resembles the natural HTML representation of the DOM and thus makes the code
very pleasant to read and understand.

```kotlin
render { /* creates the initial `RenderContext` */
    
    // call some tag-factory; here a `div` tag.
    // you can pass in some CSS classes as first parameter!
    div("w-48 m-4 flex flex-col gap-2") { /* exposes the `div` as new `RenderContext` */
        
        // create another tag inside the `div`
        label {
            // create a text-node with the plus operator
            +"Input"
            // set some tag specific attribute; those are predefined for all HTML tags!
            `for`("SomeId")
        }
        // as second parameter you can pass an ID into the tag
        input(id = "SomeId") {
            // set another tag specific attribute
            placeholder("Add some input")
        }            
    }
}
```

### State Handling

State handling is probably the key aspect of any reactive web application, so fritz2 supports this with a simple, but
mighty concept: `Store`s.

A store stores all your data for running an application; no matter if it is domain data or UI state. In contrast to
other approaches, there might be an arbitrary number of stores, which you can connect to handle the overall state
together.

For our example we only need one store, which we can declare with the `storeOf` factory.

```kotlin
    // a store can be created anywhere in you application!
    // pass some data as initial state into it
    val store = storeOf("Hello, fritz2!")
```

Once you have created the store, it can be used for...
- ... rendering the current state into your UI
- ... dealing with state changes triggered by (user) events.

### Reactive Rendering

Using the state to render portions of your UI is quite easy: Every store offers a predefined `data` property, which
is simply a `Flow` holding the current data. The framework offers an extension method on flows called `render`, that
creates some *reactive* UI portion. 

```kotlin
p { +"Value" }
store.data.render { content -> // the data from store
    // create some paragraph tag and inside it a text node with the data
    p{ +content }
}
```

How is this reactive? Here is a simplified answer to this question: The store holds some `Flow` which literally 
resembles some real flow, as inside the flow some data is transported to a drain. The drain in this case is a 
well-defined node inside the DOM of your browser, which is created by the `render` call. 
So the call of render onto some flow *connects* the store with some node of the DOM. 

Now the *"magic"* can happen: Every time the data inside the store changes, the new value will appear at the target
node and change the whole subtree based upon the code you write inside the `render` functions parameter.

In thís case the `p` tag will be removed from the DOM and a new tag will be rendered with the new text value inside.

### Dealing with State Changes

But how can the state of a store change? Often this is due to some user input, which technically creates some `events`
inside the DOM. the `tag`s offers all events as predefined properties, like the `changes` event of an `input` tag.
We use the extension function onto this event called `values`, which simply reduces the raw DOM event to a `Flow`
of `String`s, where the data is the current input value and cuts off all the meta information, we do not need here.

Once we got our flow of data in the right shape, we can connect it to the store in order to update its state.
This is done by so called `handler`s. A handler is just some method, that produces the new state of the store.
In this case we want the new input value to completely replace the old one. This can be done by a predefined handler
every store has: `update`:

```kotlin
input {
    changes.values() handledBy   store.update
//  ^^^^^^^^^^^^^^^^ ^^^^^^^^    ^^^^^^^^^^^^
//  use event with   connect to  reference to some handler,
//  new value        handler     that changes the state of the store
}
```

As you might remember we have to initialize every store with some state value. In order to pass this value reactively
into the input element, we have to set the tag specific `value` property with the data-flow of the store:

```kotlin
input {
    // the initial state will be rendered into the input
    // also any external state change (see the clear button below)
    // will update the input itself!
    value(store.data)
}
```

The connection between a store and the UI is called *data-binding*. If both, the UI rendering and the UI's event of 
the same HTML element, are connected to a store, this is called *two way data-binding*. This is the preferred way
of data-binding for most form elements for example.

As contrary, if only one aspect is connected to a store, we call this *one way data-binding*.

Let's have a look to the teaser for *custom* handlers of a store: Clearing the store's state by one button click: 

```kotlin
button {
    +"Clear"
    clicks handledBy store.handle { "" }
//  ^^^^^^ ^^^^^^^^^       ^^^^^^^^^^^^^
//  use    connect to      change the state
//  event  handler         of the store
}
```

Often the predefined `update` handler (which simply replaces the store's content with a new value) is not sufficient
for all use cases. So fritz2 allows to define custom handlers, like the one above, which simply sets an empty `String`
as new value.

### Identifying Data

As last aspect of the base concepts we would like to show you, that the store concept supports besides the reactive
state handling also the *semantic* relation between its data and UI sections. This is done by the simple concept of
identifiers.

Every store has an `id` property, which is implicitly initialized with some random value, if no parameter is passed:

```kotlin
val storeWithExplicitId = storeOf("Data", id = "42")
val storeWithRandomId = storeOf("Data") // id is created by `Id.next()`
```

This Id is useful for linking portions of the UI which is semantically connected by the data. As the store holds the
data, it makes sense, that it also keeps track of its identity.

Here we use the Id to connect the `label` to the `ìnput` field, so that clicking on the label text will focus the input;
this is a well known HTML function.

```kotlin
label {
    +"Input"
    `for`(store.id)
}
input(id = store.id) {
    // ...
}
```

In further chapters we will discover more use cases for this. Just to tease you, this concept enables the automatic
association of validation messages to their corresponding form-elements.

## Understanding the Circle of Life

Let's have another look at the so called *circle of life* again:

![state management in fritz2](/img/fritz2_cycle_of_life.svg =640x)

After reading through the teasing example explanations, you should be able to understand, how the picture reflects the 
main concepts of fritz2 within the cycling flow of data.

On the left side there is the store, that keeps track of the application data. On the other side there is the DOM, 
which holds all the HTML tags and keeps track on the emitting of events. The store's data gets connected to some
well defined node by calling the `render` extension method on the `data`-flow of the store. Every time the stored data
changes, the new data will be applied to the rendering code, which then creates a new dom sub-tree accordingly.

From within the DOM on the right side, the user (in almost all cases!) can interact with the UI and produces events,
which then will be used to update the store's data through so called `handler`s. The latter has access to the current
state and the new value and can then use both to create the new state.

The new state will then appear as data of the flow and lead to a change to the UI. Voilà, you have a circle of 
(data)-life.

That's the core of fritz2's way of building reactive web applications! No more *magic* left; just some more helpful
tools to make writing apps more pleasant.

Armed with this basic knowledge, you should keep on reading about the UI-Rendering and the chapters explaining the 
essentials of stores.

## Complete Example

This is the full source of the example above, including all styling to make it look as in the screenshot.
You can just copy and paste it into the vanilla [tailwind template](https://github.com/jwstegemann/fritz2-tailwind-template)
project, replacing the `main` function and the run the app.

```kotlin
import dev.fritz2.core.*

fun main() {
    val store = storeOf("Hello, fritz2!")

    render {
        div("w-48 m-4 flex flex-col gap-2") {
            label {
                +"Input"
                `for`(store.id)
            }
            input("p-2 border border-1 border-gray-300 rounded-sm", id = store.id) {
                placeholder("Add some input")
                value(store.data)
                changes.values() handledBy store.update
            }
            p { +"Value" }
            store.data.render { content ->
                p("p-2 bg-gray-100 border border-1 border-gray-300 rounded-sm") {
                    +content
                }
            }
            button("p-2 bg-blue-400 text-white border border-1 border-gray-300 rounded-md") {
                +"Clear"
                clicks handledBy store.handle { "" }
            }
        }
    }
}
```