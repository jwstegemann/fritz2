---
title: Fundamentals
description: "Basic fritz2 concepts"
layout: layouts/docs.njk
permalink: /docs/fundamentals/
eleventyNavigation:
    key: fundamentals
    parent: documentation
    title: Fundamentals
    order: 20
---

## What Is Fritz2?

It helps to understand what *fritz2* is in the first place. What does it do? What problems does it help me solve?
Why would I want to use it?

We would like to show you the basic concepts, explain what they are used for, and show you the beauty and elegance
of the applied framework API. 

**fritz2 is a lightweight, yet fully functional framework to build reactive web apps in Kotlin.**

The main concepts of fritz2 are:
- Declarative UI Creation: This is achieved through HTML-tag-like factories that can be nested in order to closely 
resemble the DOM structure.
- State Handling: `Store`s take care of the data and offer functions to update it by (UI-) events (`handler`s). They 
also use the data to *reactively* render the affected parts of your UI.

Sounds simple, right? In fact, it is. The main principles and concepts are that simple, that's why we consider
fritz2 *lightweight*. After getting the first overview in this chapter, the following chapters will teach you more about 
convenience functions which make writing real world apps more pleasant.

You will also be able to understand the following picture which shows the above-mentioned concepts embedded into the 
overall data flow. We will refer to this later on. 

![state management in fritz2](/img/fritz2_cycle_of_life.svg =640x)

:::info
Please have a look at our working code [getting started example](/examples/gettingstarted/) - it goes nicely with this 
documentation.
:::

## Teasing Example

Let's dive into a small example to demonstrate the *fundamental* concepts and functions.

Assume an input field where a user can enter text which will be displayed in a section below the input.
An additional button will then capitalize the input:

```kotlin
import dev.fritz2.core.*

// some styling is omitted; have a look at the complete example at the end of this chapter

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
                +"Capitalize"
                clicks handledBy store.handle { it.uppercase() }
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
    <button>Capitalize</button>
</div>
```

Although this is an enhanced "hello-world"-example, it demonstrates almost all the fundamental concepts and
parts of fritz2.

Let's have a look at the result in a browser:

@/snippets/index.html#fundamentals

Now that we have a good notion of the app and how it works, let's dive into the code and discover the various building 
blocks driving this app.

:::warning
Note: The styling in this example uses [tailwindcss](https://tailwindcss.com). As fritz2 is 
completely agnostic of any CSS styling framework, you are free to use any CSS framework, or even handcrafted CSS, 
that fits your needs. 
The [running examples](/examples) are often built with [bootstrap](https://getbootstrap.com/).
:::

### Starting Point

The fritz2 framework requires very low ceremony to set up an application. Just call the global 
`render` function inside your code once to create an initial so-called `RenderContext`. Think of it as a context 
where you can place all your UI elements, which in the end are HTML `tags`.

```kotlin
import dev.fritz2.core.*

fun main() {
    // call the render function *once* to create an initial `RenderContext` you can render your UI into
    render { /* `this` is a `RenderContext` */
        // starting from within this root context, declare the whole application's UI
    }
}
```

### UI-Elements Are HTML-Tags

Inside a `RenderContext`, fritz2 offers factory functions for all HTML5 elements, like `div`, `span`, `p`, and so on.
These functions create a special `Tag` implementation which in itself is just a new `RenderContext`. This 
enables the *nesting* of factories and enables *declarative* UI definition.

The declarative calling of tag factories resembles the natural HTML representation of the DOM and thus makes the code
very pleasant to read and understand.

```kotlin
render { /* creates the initial `RenderContext` */
    
    // call a tag-factory, like a `div` tag and optionally pass some CSS classes as first parameter
    div("w-48 m-4 flex flex-col gap-2") { /* exposes the `div` as new `RenderContext` */
        
        // create another tag inside the `div`
        label {
            // create a text-node with the plus operator
            +"Input"
            
            //TODO: Ich finde die Nutzung von 'for' in einem der ersten Beispiele nicht so schön, weil es die Sonderlocke mit den ' enthält 
            
            //set tag specific attributes - they are predefined for all HTML tags
            `for`("SomeId")
        }
        // as second parameter, you can pass an ID to the tag
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

A Store stores all your application data, no matter whether domain- or UI state data. In contrast to
other approaches, there might be an arbitrary number of stores which you can connect to link the handling of all 
individual states.

For our example, we only need one store which we declare with the `storeOf` factory.

```kotlin
    // a store can be created anywhere in your application
    // pass some data as initial state to it
    val store = storeOf("Hello, fritz2!")
```

Once you have created the store, it can be used for...
- ... rendering the UI based on the current state
- ... dealing with state changes triggered by (user) events.

### Reactive Rendering

Using the state to render parts of your UI is quite easy: Every store offers a predefined `data` property - a `Flow` 
holding the current data. The framework offers an extension method on flows called `render` which
creates a *reactive* UI part. 

```kotlin
p { +"Value" }
store.data.render { content -> // the data from store
    // create a paragraph tag and a text node with the data inside it
    p { +content }
}
```

How is this reactive? Here is a simplified answer to this question: The store holds a `Flow` which literally 
resembles a real flow, as inside the flow data is transported to a drain. The drain in this case is a 
well-defined node inside the DOM of your browser which is created by the `render` call. Every time the stored data
changes, the new data will be applied to the rendering code, which then creates a new dom subtree accordingly.

So the call of render on a flow *connects* the store with a node of the DOM: the so-called "mount-point".

Now the *"magic"* can happen: Every time the data inside the store changes, the new value will appear at the target
node and change the whole subtree based upon the code you write inside the `render` functions parameter.

In case of our example above, the `p` tag will be removed from the DOM and a new tag will be rendered with the new text 
value inside.

### Dealing With State Changes

But how can the state of a store change? Often this is due to user input which creates an `event`
inside the DOM. The `tag`s offer all events as predefined properties, like the `changes` event of an `input` tag.
fritz2 uses an extension function of this event called `values`, which simply reduces the raw DOM event to a `Flow`
of `String`s. The data on the flow is the current input value, and all the meta information about the event we do not 
need here is omitted.

Once we got our flow of data in the right shape, we can connect it to the store in order to update its state.
This is done by so-called `handler`s. A handler is a method that produces the new state of the store.
In this case we want the new input value to completely replace the old one. This can be done with the predefined,  
built-in `update` handler on every store:

```kotlin
input {
    changes.values()   handledBy   store.update
//  ^^^^^^^^^^^^^^^^   ^^^^^^^^    ^^^^^^^^^^^^
//  use event with     connect to  reference to a handler which 
//  new value          handler     changes the state of the store
}
```

In order to reactively pass the current value of the store to the input element, we have to set the tag-specific 
`value` property to the data-flow of the store:

```kotlin
input {
    // the current state will be rendered into the input (at first the initial value of course)
    // also any external state change (see the Capitalize button below) will update the input itself
    value(store.data)
}
```

The connection between a store and the UI is called *data binding*. If both the UI-rendering and the UI's event of 
those rendered HTML element are connected to a store, this is called *two-way data binding*. This is the preferred way
of data binding for most form elements, for example. If only one aspect is connected to a store, we call this 
*one-way data binding*.

Let's have a look at the teaser for *custom* handlers of a store: Capitalizing the store's state with a button click. 

```kotlin
button {
    +"Capitalize"
    clicks   handledBy     store.handle { it.uppercase() }
//  ^^^^^^   ^^^^^^^^^     ^^^^^^^^^^^^^^^^^^^^^^^^^
//  use      connect to    change the state
//  event    handler       of the store
}
```

The predefined `update` handler (which simply replaces the store's content with a new value) is often not sufficient
for all use cases. So fritz2 allows the definition of custom handlers like the one above, which simply takes the old state, 
capitalizes it and sets the result as new value.

### Identifying Data

We have one last basic concept to show you. In addition to reactive state handling, fritz2 stores also support the 
*semantic* relation between data and UI sections. This is achieved using identifiers.

Every store has an `id` property which is implicitly initialized with a random value when no parameter is passed:

```kotlin
val storeWithExplicitId = storeOf("Data", id = "42")
val storeWithRandomId = storeOf("Data") // id is created by `Id.next()`
```

This *id* is useful for linking data to the semantically corresponding part of the UI. Since the store holds the
data, it makes sense to also have it keep track of its identity.

In the following example, we use the *id* to connect the `label` to the `input` field so that clicking the label text 
will focus the input - this is a well known HTML function.

```kotlin
label {
    +"Input"
    `for`(store.id)
}
input(id = store.id) {
    // ...
}
```

We will discover more use cases for this concept in later chapters. But, to tease you: this concept enables the automatic
association of validation messages to their corresponding form-elements.

## Understanding the Circle of Life

Let's have another look at the so-called *circle of life*:

![state management in fritz2](/img/fritz2_cycle_of_life.svg =640x)

After reading through the teasing example explanations, you now have an understanding about how this picture reflects the 
main concepts of fritz2 within the cycling flow of data.

The store on the left side keeps track of the application data. The DOM on the right side 
holds all HTML tags and is in charge of emitting events. By calling the `render` extension method on the store's 
`data`-flow, its data can be connected to a well-defined node (the "mount-point"). 
Every time the stored data changes, the new data will be applied to the rendering code, which then creates a new dom 
subtree accordingly.

In most cases, the user can interact with the UI and produce events from within the DOM on the right side.
The events are used to call `handler`s to update the store's data. All handlers have access to the current
store state and the value passed by the event-flow and use both to create the new store state.

The new state will then appear on the `data`-flow and finally result in a change of the UI. Et voilà, the circle of life
is complete!

That's the core of fritz2's way of building reactive web applications. No more *magic* left, just some helpful
tools to make writing apps more pleasant.

Armed with this basic knowledge, we suggest you go on reading about UI-Rendering and the chapters explaining the 
essentials of stores.

## Complete Example

This is the full source of the example above, including all styling to make it look like the screenshot.
You can just copy and paste it into the vanilla [tailwind template](https://github.com/jwstegemann/fritz2-tailwind-template)
project, replacing the `main` function and then running the app.

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
                +"Capitalize"
                clicks handledBy store.handle { it.uppercase() }
            }
        }
    }
}
```
