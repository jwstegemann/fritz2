---
title: Render HTML
description: "Learn how to render HTML with fritz2"
layout: layouts/docs.njk
permalink: /docs/render/
eleventyNavigation:
    key: render
    parent: documentation
    title: Render HTML
    order: 30
---

## Overview

### Create a basic UI

fritz2 offers a rich DSL to create the HTML for your application. You just have to call the global `render` function
inside to create an initial `RenderContext` in which you can then call the HTML-Tag functions provided by fritz2
like `div`. All of those factories has to be nested by intention, so this results in a *declarative* way of creating
UIs.

```kotlin
fun main() {
    render { // offers created root `RenderContext`; start your UI code from within her
        
        // create some HTML tag; `div` produces a `Tag`, that is also a `RenderContext`.
        // This enables the nested calling and therefore the declarative approach!
        div(id = "header") {
            
        }
        div(id = "container") {
            h1 { +"Hello World!" }
            //   ^^^^^^^^^^^^^^^
            //   create some text node inside a tag
        }
        div(id = "footer") {

        }
    }
}
```

This code results in:

```html
<body id="target">
    <div id="header"></div>
    <div id="container">
        <h1>Hello World!</h1>
    </div>
    <div id="footer"></div>
</body>
```

If you compare the result with the code, you will immediately recognize that the DOM structure is reflected by the 
declaring code. This leads to easy to read UI definitions and is some core feature of fritz2.

### Make your UI reactive

fritz2 supports reactive UIs as one of its core features, so let us enhance this example with some dynamic content.

First of all we need a so called `Store` for holding the dynamic data of our application. Such stores are the heart
of every fritz2 application; they provide the current value in a reactive way and handle all the data changes.

To bind the store's value reactively to the DOM, use some `render*`-functions on the `data`-property of a
store, which offers a `Flow` of the store's value `T`. The function creates a so called *mount-point*, that manages the
automatic update of the DOM on every change of the store's data. The *mount-point* uses a dedicated tag created in the 
DOM as reference to the node, where the deletion and recreation of the defined UI-fragment happens.

To react to (user) events like the click onto a button, a store provides so called `handler`s, which creates the new
value of the store. The default handler `update` just takes the new value and substitutes the old state with it.

:::info
The reacting to events is not part of this chapter, but explained in short just to make the example understandable!
:::

```kotlin
fun main() {
    // define a store to hold the dynamic data: In this case a `String`
    val storedName = storeOf("World")

    render {
        div(id = "header") {
        }
        div(id = "container") {
            storedName.data.render { name -> // current value is provided
                //          ^^^^^^
                //          create a "mount-point" to bind the store's data to a node in the DOM
                //          every time the data changes, this inner UI-subtree gets deleted and newly created
                h1 {
                    +"Hello, "
                    +name // use the provided data here
                    +"!"
                }
            }
        }
        div(id = "footer") {
            button {
                +"Greet fritz2"
            }.clicks.map { "fritz2" } handledBy storedName.update
            // ^^^^^^^^^^^^^^^^^^^^^^                      ^^^^^^
            // use the event to send a new                 use store's default handler
            // value to the store                          to replace the old by the new value
            // This mechanism will be explained in the chapter about store creation!
        }
    }
}
```

The DOM structure now looks as follows:
```html
<body id="target">
<div id="header"></div>
<div id="container">
    <div class="mount-point" data-mount-point="">
        <h1>Hello, World!</h1>
    </div>
</div>
<div id="footer">
    <button>Greet fritz2</button>
</div>
</body>
```

When you click the button, the whole `h1`-subtree will be removed and changed to `<h1>Hello, fritz2!</h1>`

### Style and Enrich Your UI with Attributes

As last teasing aspects we want to demonstrate, how fritz2 supports styling an UI or setting attributes of a tag.

The tag-factories accept static CSS-classes as a `String` as first parameter, as this is such a common use case.
(This is why we used the named parameter for the ids so far)
fritz2 is totally agnostic of any CSS-framework or even handcrafted CSS. Use whatever fits best!

As being reactive is such an important aspect of fritz2, styling and attributes can be set based upon the store's state. 
The button becomes reactively disabled, if it gets clicked once, because the click changes the state of the store. 
This leads to a change which is evaluated inside the `disabled`-function, that sets the related property of 
the `<button>`-tag. So not only DOM-fragments can be bound to state changes, but also attributes and styling.

These last building blocks make fritz2 a fully reactive web framework!

```kotlin
fun main() {
    val storedName = storeOf("World")

    render {
        div("w-48 m-4 flex flex-col gap-2") {
            // ^^^^^^^^^^^^^^^^^^^^^^^^^^^
            // set (static) CSS classnames as first parameter of a tag factory
            div(id = "header") {
            }
            div(id = "container") {
                h1 {
                    +"Hello, "
                    storedName.data.render { name ->
                        span(if (name == "fritz2") "font-bold" else "") {
                            // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                            // set classname depending on the store's content
                            +name
                        }
                        +"!"
                    }
                }
            }
            div(id = "footer") {
                button("p-2 text-white border border-1 border-gray-300 rounded-md") {
                    +"Greet fritz2"
                    className(storedName.data.map { if (it == "fritz2") "bg-gray-500" else "bg-blue-400" })
                    //        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                    //        set CSS classes reactively
                    disabled(storedName.data.map { it == "fritz2" })
                    //       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                    //       set disabled attribute reactively
                }.clicks.map { "fritz2" } handledBy storedName.update
            }
        }
    }
}
```

The final DOM structure now looks like this:
```html
<body id="target">
<div class="w-48 m-4 flex flex-col gap-2">
    <div id="header"></div>
    <div>
        <h1>Hello,
            <div class="mount-point" data-mount-point="">
                <span>World</span>!
            </div>
        </h1>
    </div>
    <div>
        <button class="p-2 bg-blue-400 text-white border border-1 border-gray-300 rounded-md">
            Greet fritz2
        </button>
    </div>
</div>
</body>
```

Clicking the button will change the button section to this:
```html
<button disabled class="p-2 bg-gray-500 text-white border border-1 border-gray-300 rounded-md">
    Greet fritz2
</button>
```
Pay attention to the changed CSS-classes and the added `disabled` attribute! 

(tailwindcss users might recognize that the better approach in this case would be the usage of `disabled:` prefix;
this would make the `className` call obsolete and shorten the code - accept this solution for demonstration purposes
though!)

## Essentials

Before we dive into the essential topics, let us introduce some model types, that are used for some upcoming examples:
```kotlin
// example of an entity
data class Person(
    val id: Int, // stable identifier
    val name: String,
    val age: Int
)

// example of some value type
enum class Interest {
    Programming,
    Sports,
    History,
    WritingDocumentation
}
```

### Reactive Rendering

As you already know all [state handling](/docs/fundamentals/#state-handling) is done with `Store`s in fritz2.

Based upon the `data`-property, which provides a `Flow` of the store's generic data type, there exist a variety of
`render*`-functions, that can be used to create *reactive* UIs:

| Render-Function            | Additional parameters | Description                                                                                                                    | Default Tag |    
|----------------------------|-----------------------|--------------------------------------------------------------------------------------------------------------------------------|-------------|
| `Flow<T>.render`           | -                     | creates a mount-point providing the whole store's data value `T` inside `content` expression                                   | `div`       |
| `Flow<String>.renderText`  | -                     | creates a mount-point creating a text-node                                                                                     | `span`      |
| `Flow<List<T>>.renderEach` | -                     | creates a mount-point optimizing changes by `T.equals`. Provides a `T` inside the `content` expression. Use for value objects  | `div`       |
| `Flow<List<T>>.renderEach` | idProvider            | creates a mount-point optimizing changes by `idProvider`. Provides a `T` inside the `content` expression. Use for entities     | `div`       |

There is one more `renderEach` variant, which is defined as extension directly upon a `Store` and not on a `Flow`.
This special variant and its application are described in
[chapter about store mapping](/docs/storemapping/#reactive-rendering-of-lists-of-entities-with-automatically-mapped-element-store).

| Render-Function             | Additional parameters | Description                                                                                                                       | Default Tag |    
|-----------------------------|-----------------------|-----------------------------------------------------------------------------------------------------------------------------------|-------------|
| `Store<List<T>>.renderEach` | idProvider            | creates a mount-point optimizing changes by `idProvider`. Provides a `Store<T>` inside the `content` expression. Use for entities | `div`       |

#### Reactive Rendering of some `T`

In order to render the whole store's data type, there is the `render`-function. As last and only required parameter,
it needs a functional expression with a `Tag` as receiver (remember that a `Tag` *is* a `RenderContext`), providing the
data as parameter and returning `Unit`. Inside this `content` parameter, you then have access to the current data
and can use all HTML tag factories to create the desired UI-fragment.

```kotlin
// define some Person and "store" it
val storedPerson = storeOf(Person(1, "Fritz", 42))

render {
    storedPerson.data.render { person -> // the current store's value gets injected
        dl {
            dt { +"Id" }
            // use the data type to render its contents by accessing its properties
            // as the DOM consists only of strings, we must take care of the needed type conversion from `Int` to `String`
            dd { +person.id.toString() }
            dt { +"Name" }
            dd { +person.name }
            dt { +"Age" }
            dd { +person.age.toString() }
        }
    }
}
```

As result the following DOM-fragment is rendered:
```html
<div class="mount-point" data-mount-point="">
    <dl>
        <dt>Id</dt>
        <dd>1</dd>
        <dt>Name</dt>
        <dd>Fritz</dd>
        <dt>Age</dt>
        <dd>42</dd>
    </dl>
</div>
```

The `render`-function creates a *mount-point*, that reactively combines some store's data with 
some node in the DOM-tree. The created mount-point now takes care of 
reacting to new values and keep the UI-fragment up to date.

Remember that this is the upper part of fritz2's [circle of life](/docs/fundamentals/#understanding-the-circle-of-life)!

It is important to know the following facts about `render` and mount-points:
- as default some special `<div>` tag is rendered, which is marked with the pure informational `data-mount-point`
attribute and with a special CSS class `mount-point`, that simply sets the display mode to `contents` in order to
exclude this artificial tag from the visible UI.
- on every change to the store's data, the **whole subtree** beneath that `<div>` tag is dropped and rebuilt with the
new data's content.

:::info
As a direct consequence of the last fact, you should strive to keep the reactive UI-fragments as minimal as you can,
as re-rendering a subtree is work for the browser. 

We call this concept of minimizing the dynamic UI-parts 
[precise rendering](#minimize-dom-structure-changes-within-reactive-updates-aka-precise-rendering).

As rule of thumb memoize this: The smaller the changing portions are, the faster the result will be!
:::

Take a look at our [basic example](/examples/gettingstarted/) too, which demonstrates the `render` function.

#### Reactive Rendering of Text-Nodes

As creating a reactive UI rendering texts dynamically is a common use case, 
for which fritz2 offers a *dedicated* variant render function called `renderText`
on data flows of type `String`:

```kotlin
val storedText = storeOf("fritz2")

render {
    div {
        // Attention: We need a *Tag* here, not just a `RenderContext`! 
        storedText.data.renderText()
    }
}
```

As result the following DOM-fragment is rendered:
```html
<div>
    <span>fritz2</span>
</div>
```

There is also an extension function `asString`, which converts a `Flow<T>` to a `Flow<String>` by calling the 
`toString` method internally:

```kotlin
val storedCount = storeOf<Int>(0)

render {
    div {
        storedCount.data.asString().renderText()
    }
}
```

The `renderText` function is useful in situations, where a longer text has some smaller dynamic parts. 
As only the smaller parts will change, the other parts must not be part of the mount-point:

```kotlin
val storedText = storeOf("fritz2")

render {
    p {
        +"There is some excellent Kotlin based framework named "
        storedText.data.renderText() // only dynamic part is here; the other text-nodes are static!
        +", that empowers one to easily create reactive SPAs in pure Kotlin."
    }
}
```

Imagine a `render` based solution on the contrary:
```kotlin
val storedText = storeOf("fritz2")

render {
    p {
        storedText.data.render { frameworkName ->
            +"There is some excellent Kotlin based framework named "
            +frameworkName
            +", that empowers one to easily create reactive SPAs in pure Kotlin."
        }
    }
}
```
Of course the former could have also be written with `String`-templating, instead of separate text-nodes too.

The important advantages of the dedicated `renderText` solution compared to the general `render` based solution are:
- the solution with `renderText` fits better to the *declarative* UI approach, as it better reflects the node structure
and thus is better to read.
- the mount-point encompasses a much smaller DOM-subtree, so it is more efficient.

#### Reactive Rendering of Lists of Value Objects

As `List<T>` as value of a store is a common use case, fritz2 offers a special `renderEach` function for this too:

```kotlin
// define some store with type of `List<T>`
val storedInterests = storeOf(Interest.values().toList())

render {
    ul {
        // for every value of store's interest list, the provided `content` expression is executed
        storedInterests.data.renderEach { interest -> // the current applied value of the data flow
            // just declare the UI for one item
            li {
                +interest.toString()
            }
        }
    }
}
```

As result the following DOM-fragment is rendered:
```html
<div class="mount-point" data-mount-point="">
    <li>Programming</li>
    <li>Sports</li>
    <li>History</li>
    <li>WritingDocumentation</li>
</div>
```

It is important to spot the main difference to the former render-functions: The store's data type is an (ordered)
collection type, so its value consists of an arbitrary amount of elements of the same type. It appears to be
an inherent property of same types, that their UI-representation are also of the same type. `renderEach` supports
this property in a way, that its `content` parameter only describes the UI-fragment for *one* item of the list.
The UI-container, which holds those items is therefore *not* part of the reactive expression and thus the
mount-point.

Another important aspect to handle the reactive rendering of `List`s in a specialized way are *performance*
optimizations.

In order to gain some understanding for this technical aspect, consider the above example realized with the standard
`render`-function:

```kotlin
val storedInterests = storeOf(Interest.values().toList())

render {
    ul {
        storedInterests.data.render { interests -> // name suggests we get a `List<T>` here of course!
            // "manually" create <li> tags for each item
            interests.forEach {
                li {
                    +it.toString()
                }
            }
        }
    }
}
```

You cannot spot the difference by looking at the rendered DOM structure, which remains the same for both solutions.
But looking at the code, we can conclude, that the second approach will definitely delete the whole subtree of the 
mount-point and re-create the whole `<li>`-tags even if only *one* element of the list changes.

`renderEach` instead creates a specialized mount-point in order to identify elements for re-rendering.
This mount-point compares the last version of your list with the new one on every change and applies the minimum
necessary patches to the DOM. Assuming again that only one item changes, the untouched items may remain inside the DOM
and only the changed item's DOM subtree needs to be deleted and recreated. You can imagine, that this is a game changer
concerning performance!

This `renderEach` implementation does the patch-determination applying the standard `equals`-method of the list's 
type `T`. This is the reason, why is targeted to *value* objects: They are implicitly defined by their equality!

Have a look at its usage in our [master detail](/examples/masterdetail/) example.

#### Reactive Rendering of Lists of Entities

As `List<T>` as value of a store is a common use case, where `T` is an *entity*, thus it has some stable *identity*
lasting all changes, fritz2 offers a special application of the `renderEach`-function for this use case.
When dealing with entities you can pass an additional parameter called `idProvider`, which is an expression that takes
one item `T` and determines its stable identity. Armed with this information, the internal patch-determination is then
simply based upon the id and not upon the `equals`-method anymore.

```kotlin
// define some person-entities and "store" them
val persons = listOf(
    Person(1, "Fritz", 42),
    Person(2, "Caesar", 66),
    Person(3, "Cleopatra", 50)
)

val storedPersons = storeOf(persons)

render {
    ul {
        storedPersons.data.renderEach(Person::id) { person ->
            //                        ^^^^^^^^^^
            //                        provide a function to determine the stable identity of one `T`
            li {
                dl {
                    dt { +"Id" }
                    dd { +person.id.toString() }
                    dt { +"Name" }
                    dd { +person.name }
                    dt { +"Age" }
                    dd { +person.age.toString() }
                }
            }
        }
    }
}
```

As the identity is stable, the following properties holds for the rendered items:
- an element is only rendered *once*, so only adding or deleting items to the store will lead to changes in the UI,
as those new elements will be added or inserted or the deleted elements will disappear from the DOM.
- any change to an existing item will *not* change the UI anymore. 

The latter is an important aspect to consider before the use of `renderEach` for entities: 
If you still need to reflect those changes, it might be a better choice to rely on the default `renderEach` application 
relying on equality. But if only a small set of properties of an element could possibly change and performance is an
important aspect, rely on this application of `renderEach` and add additional mount-points inside the elements subtrees. 
You will learn about those in 
[chapter about store mapping](/docs/storemapping/#reactive-rendering-of-lists-of-entities-with-automatically-mapped-element-store).

Have a look at its application in our [todomvc](/examples/todomvc/) example.

### Apply Styling to Your UI: Reactive or Static

The `class` attribute of a `Tag` for working with CSS style-classes is somewhat special. You can set the static values
of each `Tag` for `class` and `id` by using the optional parameters of its factory function:

```kotlin
render {
    div("some-static-css-class") {
        button(id = "someId")
    }
}
```

Use this one-liner to add styling and meaning to your elements by using semantic CSS class-names. Also, it keeps your
code clean when using CSS frameworks like Bootstrap, Tailwind etc.

To reactively change the styling of a rendered element, you can add dynamic classes by assigning a `Flow` of strings to
the `className`-attribute (like with any other attribute).

```kotlin
render {
    val enabled = storeOf(true)

    div("common-css-class") {
        className(enabled.data.map {
            if (it) "enabled-css-class"
            else "disabled-css-class"
        })
        +"Some important content"
    }
}
```

### Apply Attributes to Your UI: Reactive or Static

To create rich HTML interfaces, styling alone is not sufficient. You will need to use a variety of attributes.
In fritz2 there are several easy ways to achieve this, depending on your use case.

You can set all HTML attributes inside the `Tag`'s content by calling a function of the according name. Every standard
HTML attribute has two functions. One sets a static value every time the element is re-rendered, the second collects
dynamic data coming from a `Flow`. When coming from a `Flow`, the attribute's value will be updated in the
DOM whenever a new value appears on the `Flow`
without having to re-render the whole element:

```kotlin
val flowOfInts = ... // i.e. get it from some store

render {
    input {
        placeholder("some text")
        maxLength(flowOfInts)
        disabled(true)
    }
}
```

If you want to set a `Boolean` value, you can set an optional parameter `trueValue` which will be set as the
attribute-value if your data is `true`:

```kotlin
val isLow = myStore.data.map { i -> i <= 0 }

render {
    button {
        +"My button"
        attr("data-low", isLow, trueValue = "true")
        // isLow == true  -> <button data-low="true">My button</button>
        // isLow == false -> <button>My button</button>
    }
}
```

This is sometimes needed for CSS-selection or animations.

::: info
The `className` function you have already encountered is also just a way to set a common attribute on a tag - although
it works internally a bit different and offers more convenience variants due to its overall importance.
:::

To set a value for a custom (data-) attribute, use the `attr()`-function. It works for static and dynamic (from
a `Flow`) values:

```kotlin
render {
    div {
        attr("data-something", "someValue")
        attr("data-something", flowOf("someValue"))
    }
}
```

Sometimes it is important for an attribute to only appear if some condition is `true`, for example some
[ARIA](https://developer.mozilla.org/en-US/docs/Web/Accessibility/ARIA) properties like
[aria-controls](https://developer.mozilla.org/en-US/docs/Web/Accessibility/ARIA/Attributes/aria-controls) should
preferably appear only if the dependent element exist. The `attr` functions for `Flows` behave in such a way, that they
only set an attribute if the value is *not* `null`. This behaviour could be used to achieve the desired effect:

```kotlin
val isOpened = storeOf(true)

render {
    button {
        +"Toggle"
        clicks handledBy isOpened.handle { !it }

        attr("aria-controls", isOpened.data.map { if (it) "disclosure" else null })
        //                                                                  ^^^^
        //      make whole attribute disappear if disclosure-div is not rendered
    }

    isOpened.data.render {
        if (it) {
            div(id = "disclosure") {
                +"I am open!"
            }
        }
    }
}
```

### Minimize DOM Structure Changes within Reactive Updates aka Precise Rendering

In order to improve the performance and memory-footprint, you should always try to keep the reactive parts of your
UI as small as possible. This can be achieved by putting the `render*`-functions as close to the dynamic subtree 
as possible.

Let's recap the first reactive rendering example:
```kotlin
val storedPerson = storeOf(Person(1, "Fritz", 42))

render {
    storedPerson.data.render { person ->
        dl {
            dt { +"Id" }
            dd { +person.id.toString() }
            dt { +"Name" }
            dd { +person.name }
            dt { +"Age" }
            dd { +person.age.toString() }
        }
    }
}
```
This code will recreate the whole definition list, if the `storedPerson` changes - even in only one of its properties.

Now let's try to analyse, how this object might change by looking at each property:
- `id`: this must be stable by definition; so it will never change.
- `name`: it is very unlikely that their names might change.
- `age`: this is in fact the only regularly changing property. Each passing year it must be increased.

So in fact we only need to react to changes of one portion of the `Person` model: the `age`-property.

:::info
The following example uses some concepts, that have not yet been explained.

The function of [handlers]((/docs/createstores/#custom-handler-in-depth)) and custom
[data-flows](/docs/createstores/#custom-data-flow-property) are explained in the upcoming chapter
[Store Creation](/docs/createstores). We just need to tease these functions here, in order to show a working example.
Please just accept the description without a deeper understanding of those techniques. It will be sufficient, to
grasp the overall concept.

The concept of *precise rendering* fits best here - so please excuse this decent lookahead!
:::

This enables us to put the reactive rendering code much closer to the corresponding UI-elements. As result the whole
dynamic subtree will become much smaller, which will improve performance and reduce memory-usage:
```kotlin
// define a small helper type to hold all static parts of a `Person`
data class StaticPerson(val id: Int, val name: String)

val storedPerson = object : RootStore<Person>(Person(1, "Fritz", 42)) {

    // collect all static properties of the person into the helper class object
    val staticPart: Flow<StaticPerson> = data.map { StaticPerson(it.id, it.name) }.distinctUntilChanged()
    //                                                                             ^^^^^^^^^^^^^^^^^^^^^^
    // Emit only a new value on the flow, if the new value really differs from the old one, that is `equals` fails.

    // create specific data flow for the `age`-property
    val age: Flow<Int> = data.map { it.age }

    // For now accept that this element will change the store's state.
    // The concept of handlers is described in the next chapter "Creating Stores"
    val increaseAge = handle { state -> state.copy(age = state.age + 1) }
}

render {
    dl {
        storedPerson.staticPart.render { person ->
            //       ^^^^^^^^^^
            //       the upcoming `render`-function will only react to changes to this data part.
            //       as this data won't change, this will be rendered only once!
            dt { +"Id" }
            dd { +person.id.toString() }
            dt { +"Name" }
            dd { +person.name }
            dt { +"Artificial random value to show that this UI part will not react to age changes!" }
            dd { +Id.next() } // This changes on re-rendering! It won't change in this app though.
        }
        dt { +"Age" }
        dd {
            storedPerson.age.renderText()
            //           ^^^
            //           Same here: the render function will only react to changes of the `age`-portion
            //           of some `Person`-object. As this might change quite often, this part of the
            //           UI will also change in the same manner.
        }
    }
    button {
        +"Increase Age"
        clicks handledBy storedPerson.increaseAge
    }
}
```
In the above example the rather static aspects are exposed as separate data-flow `staticPart`, which will be configured
by adding `distinctUntilChanged`. The latter will filter out all values, that are equal to their predecessors. This
way every change to the store's value, exposed by its `data`-flow, will only appear on this flow, if some relevant
properties have changed.

As the example only enables to change the `age`-property, which is *not* part of the static-parts, the `staticPart`
flow will not emit a new value, so the mount-point will not re-render ist subtree.

On the other hand, the `age`-property is exposed by some special data-flow `storedPerson.age`. This one will emit a
new value on every change to the main model. The latter is realized by the `<button>`-tag below, which will trigger
some [handler in the store](/docs/createstores/#custom-handler-in-depth) that increases the `Person.age`-property.

As this value is quite atomic, we can place the mount-point as deep into the static UI-part as possible. In this case
directly as text-node inside the `<dd>`-tag. This is quite *precise*, that's why we call this concept 
*precise rendering*.

You can verify the two different behaviours of rendering in the example, by clicking the [[Increase Age]]-button.
After each click, a new `Person`-object is created by the handler. The "Age" data will show the increased value, but
the "Artificial random value" remains the same, which proves, that the first mount-point does not get some update.

### Minimize DOM Structure of Mount-Points

Let's recap the first reactive rendering example:
```kotlin
val storedPerson = storeOf(Person(1, "Fritz", 42))

render {
    storedPerson.data.render { person ->
        dl {
            dt { +"Id" }
            dd { +person.id.toString() }
            dt { +"Name" }
            dd { +person.name }
            dt { +"Age" }
            dd { +person.age.toString() }
        }
    }
}
```

As result the following DOM-fragment is rendered:
```html
<div class="mount-point" data-mount-point="">
    <dl>
        <dt>Id</dt>
        <dd>1</dd>
        <dt>Name</dt>
        <dd>Fritz</dd>
        <dt>Age</dt>
        <dd>42</dd>
    </dl>
</div>
```

As there is the preliminary `<dl>` tag, grouping all further UI below itself as children, the *artificially* created
`<div>`-tag as mount-point reference to the DOM is just overhead! We can improve ths DOM structure by telling the 
`render`-function to *use* some existing tag as mount-point reference instead of creating a dedicated one:

```kotlin
dl {
    // `this` is the <dl>-Tag in this scope!
    // pass the existing tag *into* `render` for using it as the mount-point reference 
    storedPerson.data.render(into = this) { person -> 
        dt { +"Id" }
        dd { +person.id.toString() }
        // ...
    }
}
```

As result the following, reduced DOM-fragment is rendered:
```html
<dl data-mount-point="">
    <dt>Id</dt>
    <dd>1</dd>
    <dt>Name</dt>
    <dd>Fritz</dd>
    <dt>Age</dt>
    <dd>42</dd>
</dl>
```

:::warning
As you know, the mount-point has the *full* control over the DOM-subtree below its reference tag and will drop it
completely on every change of the data.

So be cautious to **never ever** put other tags around this `render`-expression! This will be deleted sooner or 
later by the mount-point.

```kotlin
dl {
    div { +"Do not put any elements between a referenced tag and its related `render` function!" }    
    storedPerson.data.render(into = this) { person -> 
        dt { +"Id" }
        dd { +person.id.toString() }
        // ...
    }
    div { +"Even this might appear on initial rendering, it will at least be dropped after first change!" }
}
```
:::

In fact all `render*`-variants offer the `into` parameter, so this works for `renderText` or `renderEach` too.

### Structure UI

It's very easy to create a lightweight reusable component with fritz2. Basically all you have to do is write a function
with `RenderContext` as its receiver type:

```kotlin
fun RenderContext.myComponent() {
    p {
        +"This is the smallest valid stateless component"
    }
}

render {
    myComponent()
}
```

Of course, you can also use a subtype of `RenderContext` like a certain `Tag` as receiver if you want to limit the usage
of your component to this type as its parent.

By using plain functions, it's also straight forward to parametrize your component:

```kotlin
fun RenderContext.myOtherComponent(person: Person) {
    p {
        +"Hello, my name is ${person.name}!"
    }
}

val somePerson = Person(...)
render {
    div {
        myOtherComponent(somePerson)
    }
}
```

To allow nested components, use a lambda with `RenderContext` as its receiver, or the type of the element you are
calling this lambda in:

```kotlin
// return a html element if you need it
fun RenderContext.container(content: RenderContext.() -> Unit) {
    div("container") {
        content()
    }
}

render {
    container {
        p {
            +"Hello World!"
        }
    }
}
```

Using `Div` as receiver type in the example above allows you to access the specific attributes and events of your
container-element from your content-lambda. Use `RenderContext` if this is not necessary or intended.

### Inline Styles

fritz2 also offers a function for setting the inline `style` attribute to your elements:

```kotlin
render {
    p {
        inlineStyle("color: red")
        +"this is red text"
    }
}
```

Of course, it is also possible to dynamically style an element by passing a `Flow` of CSS styles into `inlineStyle`:
```kotlin
render {
    val enabled = storeOf(true)

    div {
        inlineStyle(enabled.data.map {
            if (it) "background-color: lightgreen;"
            else "opacity: 0.5; background-color: lightgrey;"
        })
        +"Some important content"
    }
}
```

### Avoid Flicker Effects with Reactive Stylings

To set an initial CSS class (or any other attribute) immediately (for example to avoid flicker effects caused by the delay
of the first value becoming available on the flow), the respective attribute-method must be called twice.
First with the static value that should be set immediately, then with the `Flow` that provides the dynamic values:

```kotlin
val visibility: Flow<String> = ...

className("invisible")
className(visibility)
```

## Advanced Topics

### Scope

fritz2 offers the option to use a `Scope` to add some information to a tag which can then be received by any
child-tag of the corresponding DOM-subtree and which will not be rendered out by default. The values in the `Scope` are
only available for tags inside the context of the tag which sets them.

To append something to the `Scope` you have to create a `Scope.Key` by using the
`Scope.keyOf()` function.
```kotlin
val myKey = Scope.keyOf<String>("myKeyName")
```
The key is needed to set or get a value to the `Scope`.
```kotlin
val fooKey = Scope.keyOf<String>("foo")

render {
    div(scope = {
        set(fooKey, "bar")
    }) {
        div {
            // div is child of scope owner, so key is accessible
            +(scope[fooKey] ?: "")
        }
    }
    div {
        // this div is not a child, so key is not in its (empty) scope
        +(scope[fooKey] ?: "")
    }
}
```
The result is the following:
```html
<div>
    <div>bar</div>
</div>
<div></div>
```
For debugging proposes you can use the `scope.asDataAttr()` function to set current scope to the tag and see it
in the DOM-Tree.

### Customize the Starting Point - Anchor Your global Render Function

As you already should know, you need to call the global `render` function once, in order to create an initial
`RenderContext`:

```kotlin
fun main() {
    render {
        // access to the created root `RenderContext`
        // start your UI code from within here!
    }
}
```

This of course only works in combination with some *fitting* `index.html` in `jsMain/resources`-folder, which is just a
normal web-page. This page needs two configuration aspects to be set up correct:
1. there must be some html-tag as target reference; by default the `document.body` tag is used.
2. the resulting js-artifact must be included as `<script>` tag beneath the static html.

```html
<!doctype html>
<html>
  <head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1" name="viewport">
  </head>
  <body>
    <div id="myAppAnchor">
        Loading...
    </div>
    <script src="<project-name>.js"></script>
  </body>
</html>
```

The global `render` factory accepts a 
`selector` string (see [querySelector](https://developer.mozilla.org/en-US/docs/Web/API/Document/querySelector))
or alternatively a [HTMLElement](https://developer.mozilla.org/en-US/docs/Web/API/HTMLElement)
to select the target html-tag:

```kotlin
fun main() {
    render("#myAppAnchor") { // using id selector here, leave blank to use document.body by default
        h1 { +"My App" }
        div("some-fix-css-class") {
            p(id = "someId") {
                +"Hello World!"
            }
        }
    }
}
```

When calling `render` like that, your content will be mounted to an `HTMLElement` with `id="myAppAnchor"`.
If you want to mount your content to the `body` of your `index.html`, you can omit this parameter.

The second option is to set an `override` parameter to `false`, which means that your content will be appended.
By default, all child elements will be removed otherwise before your content is appended to the target html-tag.

Run the project by calling `./gradlew jsRun` in your project's main directory. Add `-t` to enable automatic
building and reloading in the browser after changing your code.

### Reactive Styling with complex Rules

fritz2 also lets you manage multiple classes in a `List<String>` or `Flow<List<String>>` with the `classList`-attribute.

Additionally, you can build a `Map<String, Boolean>` from your model data that enables and disables single classes
dynamically:

```kotlin
render {
    div {
        classMap(toDoStore.data.map {
            mapOf(
                "completed" to it.completed, // a boolean-attribute in the data-model
                "editing" to it.editing
            )
        })
    }
}
```

### Rendering on Stand-alone Flows

Coming soon

### Custom Tags / RenderContexts

Coming soon 
