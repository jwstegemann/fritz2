---
layout: layouts/docsWithContentNav.njk
title: Stores
permalink: /docs/stores/
eleventyNavigation:
    key: stores
    parent: documentation
    title: Stores
    order: 60
---

## Stores

In fritz2, `Store`s are used to handle your app's state. 
They heavily depend on Kotlin's `Flow`:

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

## Manage Your State

Let's assume the state of your app is a simple `String`. Creating a `Store` to manage that state is quite easy:

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

By calling `s.data.renderText()` a [MountPoint](MountPoint.html) is created and collects your model values. 

## Mount-Point

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

## TODO: H2 for this

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
Therefore, take a look at [Nested Structures](NestedStructures.html).

You can also bind a `Flow` to an attribute:
```kotlin
input {
    value(store.data)
}
```
In this case, only the attribute value will change when the model in your store changes. 
fritz2 offers [pre-defined properties (at each Tag)](https://api.fritz2.dev/core/core/dev.fritz2.dom.html/index.html) 
for every HTML5-attribute.

But how can you change the model-data in a store? Let's have a look at [State Management](StateManagement.html).

