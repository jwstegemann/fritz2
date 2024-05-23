---
title: Event Handling
description: "Dealing with events in fritz2"
layout: layouts/docs.njk
permalink: /docs/events/
eleventyNavigation:
  key: events
  parent: documentation
  title: Event Handling
  order: 45
---

## Overview

In fritz2, all HTML-Events are either encapsulated within a `Tag`-scope or within the special `Window`-object, which both
offer access to all global events. A complete list can be found in the
[events.kt](https://github.com/jwstegemann/fritz2/blob/master/core/src/jsMain/kotlin/dev/fritz2/core/events.kt) file.

The most important aspect to understand is that an event in fritz2 is just a type derived by `Flow`. You can
use all events as the source of an action which should be handled by a handler-function.

Take the `click`-event as a simple example:

```kotlin
render { 
    button {
        +"Click me!"
        clicks handledBy { alert("Button was clicked!") }
    //  ^^^^^^
    //  this is just a `Flow` that can be handled     
    }
}
```

As a naming convention, all HTML events will be suffixed with an `s` in order to emphasize them being a flow 
transporting all values that are emitted by the DOM API.

Since events can carry important data and information, events in fritz2 are more or less `Flow`s that emit `Event`-objects.
Their type is called `Listener`, but that is really just a name. Think of a `Listener` as a "flow of events".
Such a `Listener` will therefore provide those data as the value of its own flow:
```kotlin
div {
    val storedName = storeOf("")
    input {
        type("text")
        changes.map { it.target.unsafeCast<HTMLInputElement>().value } handledBy storedName.update
        //            ^^
        //            grab the value of type `Event` and access its fields
        //            there are different sub-types of `Event` like `PointerEvent`, `KeyboardEvent` or `InputEvent`
        //            in this case
    }
    storedName.data.renderText()
}
```

Depending on the `Event`-type, you can access its special fields. For common use cases like getting the value of the
input-element, there are special convenience functions to make event handling more pleasant:
```kotlin
div {
    val storedName = storeOf("")
    input {
        type("text")
        changes.values() handledBy storedName.update
        //      ^^^^^^^^
        //      extracts the value of the `InputEvent`
    }
    storedName.data.renderText()
}
```

## Essentials

### Controlling the Event-Flow

Besides using some UI-event as source of action to modify application state, there are two types of dedicated event
functions that enable further operations to control the event-flow within the DOM:
- Stopping the propagation and preventing the default behaviour.
- Filtering the event and in certain cases preventing that values are emitted

#### Stop Propagation

In order to stop an event from bubbling up the tree (or down by `capture`), the DOM-API exposes these two methods
upon the `Event`-objects:
- [stopPropagation](https://developer.mozilla.org/en-US/docs/Web/API/Event/stopPropagation)
- [stopImmediatePropagation](https://developer.mozilla.org/en-US/docs/Web/API/Event/stopImmediatePropagation)

In order to apply those methods before the `Event` is emitted on the `Flow`, fritz2 offers two variants of factory
functions to create the event-flow:
- one is named exactly like the `property` itself: `clicks(init: MouseEvent.() -> Unit)`
- the other adds an `If`-suffix like this: `clicksIf(selector: MouseEvent.() -> Boolean)`

Those two factories enable a user to control the further processing (in addition to the custom written `Flow`-`Handler`-binding).

Look at the example below:
```kotlin
div {
    +"Parent"
    button {
        +"default propagation"
        type("button")
        // first event handling
        clicks handledBy { console.log("default propagation clicked!") }
        // second event handling
        clicks handledBy { window.alert("Button was clicked!") }
    }
    // `clicks` "bubbles" per default -> event will reach the parent DOM-node, so we can react to it:
    clicks handledBy { console.log("click reached Parent!") }
}
```

When the button is clicked, a `click` event is emitted and appears on the `clicks`-Listener `Flow`.
There are two event-handlers defined inside the `button`-element. The first just logs to the console, the second
opens an alert-window.

Inside the parent-`div`-element, we establish another event handling which also reacts to a `click`-event.

The user can now click on the button. The expected result is:
- a log message in the console: `default propagation clicked!`
- an alert window opens with the text message `Button was clicked!`
- and after closing it, there is another log message `click reached Parent!`, as `click` bubbles per default.

Now let uns investigate how the behaviour changes if we use the `clicks()`-method with the `init`-parameter on
the first event-handling in order to call `stopPropagation` of the `Event`-object:

```kotlin
div {
    +"Parent"
    button {
        +"stopPropagation"
        // We use the `clicks(init: MouseEvent.() -> Unit)` variant here:
        clicks { stopPropagation() } handledBy { console.log("stopPropagation clicked!") }
        //       ^^^^^^^^^^^^^^^^^
        //       we want the event processing to stop bubbling to its parent.
        //       as the receiver type is `MouseEvent`, which derives from `Event`, we can call
        //       its method directly
        clicks handledBy { window.alert("Button was clicked!") }
    }
    // no value will appear on this `clicks`-Listener anymore!
    clicks handledBy { console.log("click reached Parent!") }
}
```

The user can now click on the button. The expected result is:
- a log message in the console: `stopPropagation clicked!`
- an alert window opens with the text message `Button was clicked!`

The log message of the parent-`div` does no longer appear!

This is the desired effect of the `Event.stopPropagation()`-call: All event handling within the DOM-Element itself will
be executed, but the bubbling stops after that.

It is not important on which event-processing code the call is made. We could easily call the `stopPropagation` within
the alert-handling without getting a different behaviour.

```kotlin
clicks handledBy { console.log("stopPropagation clicked!") }
clicks { stopPropagation() } handledBy { window.alert("Button was clicked!") }
//       ^^^^^^^^^^^^^^^^^
//       the sequence of the call is not important!
```

Now let us investigate the `stopImmediatePropagation`-method and how it differs from the none immediate variant:

```kotlin
div {
    +"Parent"
    button {
        +"stopImmediatePropagation"
        clicks { stopImmediatePropagation() } handledBy { console.log("stopImmediatePropagation clicked!") }
        //       ^^^^^^^^^^^^^^^^^^^^^^^^^^
        //       we want the event processing to stop bubbling to its parent *and* all following handlers
        
        // this listener follows the `stopImmediatePropagation` -> no value will appear on its `clicks`-Listener 
        // anymore
        clicks handledBy { window.alert("Button was clicked!") }
    }
    // no value will appear on this `clicks`-Listener anymore!
    clicks handledBy { console.log("click reaches Parent!") }
}
```

The user can now click on the button. The expected result is:
- a log message in the console: `stopPropagation clicked!` - nothing more!

The difference becomes obvious now: All listeners following the `stopImmediatePropagation` call will not get any of the 
emitted values. Additionally, the bubbling also is stopped, so no handlers of any parent-elements will be called.

So this time, the sequential order is important! If we switch the sequence, the alert will appear again, but
other events will not:

```kotlin
// this handler will be called -> stopping is not applied yet
clicks handledBy { window.alert("Button was clicked!") }

clicks { stopImmediatePropagation() } handledBy { console.log("stopImmediatePropagation clicked!") }

// this listener follows the `stopImmediatePropagation` -> no value will appear on its `clicks`-Listener anymore!
clicks handledBy { console.log("This will not be logged!") }
```

::: info
The same behaviour applies to *event capturing* - it then works the other way round, so the event values won't appear
on handlers within child-elements any longer.
:::

#### Prevent Default Behaviour

Just like the propagation stopping, the `preventDefault()`-method can be easily called on an `Event` by using the
dedicated factory-functions with an `init`-parameter:

```kotlin
clicks { preventDefault() } handledBy { ... }
```

#### Filtering events

Sometimes it is useful to filter certain events based on the  individual event's values. Good examples are all kinds of
`KeyboardEvent`s, like `keydowns` driving the behaviour of a component, such as reacting specifically to keys like
`Enter`, `Escape` or alike only.

For those cases, specialized event-factories marked with an `If` suffix are provided.
`keydownsIf(selector: KeyboardEvent.() -> Boolean)` is one of them.

As you can see, they require a `selector`, so the underlying mechanism will emit the `Event` only if the selector
resolves to `true`, but drop it otherwise.

```kotlin
keydownsIf { shortcutOf(this) == Keys.Space } handledBy { ... }
//           ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//           Boolean expression: If "Space" is pressed, resolve to `true` -> emit the event, so it can be handled. 
//           (This syntax is explained in a following section about "Keyboard-Events and Shortcut-API")
```

Often you need to combine this with `Event`-modifying calls like `preventDefault` or `stopPropagation`. This can be
easily done here, too:

```kotlin
keydownsIf {
    if (shortcutOf(this) == Keys.Space) {
        // if "Space" was pressed manipulate the event processing
        stopImmediatePropagation() 
        preventDefault()
        true 
    } else false
} handledBy { ... }
```

### Global Event-Handling With the Window-Object

In order to bind events to a handler reacting on the top outermost level, fritz2 offers the `Window`-object.

It offers all events that regular DOM-elements expose as well, without the need to be called within a specific node;
those events can be called from everywhere inside the initial `render`-function.

This can be useful to react to certain actions on a global level, like specific keys a user might press.

For example, imagine an app which prohibits vertical scrolling by hitting the "Space" key.
This can be realized with the following code snippet:
```kotlin
render {
    Window.keydowns {
        if (shortcutOf(this) == Keys.Space) {
            preventDefault()
        }
    } handledBy { }
}
```

### Keyboard-Events and Shortcut-API

fritz2 offers a handy shortcut API that allows easy combination of shortcuts with modifier shortcuts, constructing
those from a KeyboardEvent, and also prevents meaningless combinations of different shortcuts:

```kotlin
// Constructing a shortcut by hand
Shortcut("K") 
// -> Shortcut(key = "K", ctrl = false, alt = false, shift = false, meta = false)

// Or use factory function:
shortcutOf("K")

// Set modifier states, need to use constructor:
Shortcut("K", ctrl = true) // Shortcut(key= "K", ctrl = true, alt = false, shift = false, meta = false)

// Constructing a shortcut from a KeyboardEvent
div {
    keydowns.map { shortcutOf(it) } handledBy { /* use shortcut-object for further processing */ }
    //                        ^^
    //                        use KeyboardEvent to construct a Shortcut-object with all potential
    //                        modifier key states reflected
}

// Using predefined shortcuts from Keys object
Keys.Enter // named-key for the enter keystroke, a `Shortcut`
Keys.Alt // `ModifierShortcut` -> needs to be combined with a "real" shortcut in order to use it for further processing
// The same but more cumbersome and prone to typos
Shortcut("Enter")
// Not the same (!)
Shortcut("Alt") // -> Shortcut(key= "Alt", ..., alt = false)
Keys.Alt // -> ModifierKey-object with alt = true property!

// Constructing a shortcut with some modifier shortcuts
Shortcut("K") + Keys.Control
// Same result, but much more readable the other way round:
Keys.Control + "K"

// Defining some common combination:
val searchKey = Keys.Control + Keys.Shift + "F"
//              ^^^^^^^^^^^^
//              You can start with a modifier shortcut.
//              Appending a String to a ModifierKey will finally lead to a `Shortcut`.

val tabbing = setOf(Keys.Tab, Keys.Shift + Keys.Tab)

// API prevents accidental usage: WON'T COMPILE because real shortcuts can't be combined
Shortcut("F") + Shortcut("P") 

// Shortcut is a data class → equality is total:
Keys.Control + Keys.Shift + "K" == Shortcut("K", shift = true, ctrl= true, alt = false, meta = false)
// But
Keys.Control + Keys.Shift + "K" != Shortcut("K", shift = false, ctrl= true, alt = false, meta = false)
//             ^^^^^^^^^^                        ^^^^^^^^^^^^^
//                 +-----------------------------------+

// Case sensitive, too. Further impact is explained in next section.
shortcutOf("k") != shortcutOf("K")
```

Be aware of the fact that the key-property is taken from the event as it is. This is important for all upper case keys:
The browser will always send an event with shift-property set to true, so in order to match it, you must construct
the matching shortcut with the Shift-Modifier:

```kotlin
// Goal: Match upper case "K" (or to be more precise: "Shift + K")

// Failing attempt
keydowns.events.filter { shortcutOf(it) == shortcutOf("K") } handledBy { /* ... */ }
//                       ^^^^^^^^^^^^^^    ^^^^^^^^^^^^^^^
//                       |                 Shortcut(key = "K", shift = false, ...)
//                       |                                     ^^^^^^^^^^^^
//                       |                                 +-> will never match the event based shortcut!
//                       |                                 |   the modifier for shift needs to be added!
//                       Shortcut("K", shift = true, ...)--+
//                       upper case "K" is (almost) always send with enabled shift modifier.

// Working example
keydowns.events.filter { shortcutOf(it) == Keys.Shift + "K" } handledBy { /* ... */ }
```

Since most of the time you will be using the keys within the handling of a `KeyboardEvent`, there are some
common patterns relying on the standard Flow functions like `filter`, `map`, or `mapNotNull` to apply:

```kotlin
// Pattern #1: Only execute on specific shortcut:
keydowns.events.filter { shortcutOf(it) == Keys.Shift + "K"}
    .map { /* further processing if needed */ } handledBy { /* ... */ }

// Variant of #1: Only execute the same for a set of shortcuts:
keydowns.events.filter { shortcutOf(it) in setOf(Keys.Enter, Keys.Space) }
    .map { /* further processing if needed */ } handledBy { /* ... */ }

// Pattern #2: Handle a group of shortcuts with similar tasks (navigation for example)
keydowns.events.mapNotNull{ event -> // better name "it" in order to reuse it
    when (shortcutOf(event)) {
        Keys.ArrowDown -> // create / modify something to be handled
        Keys.ArrowUp -> // 
        Keys.Home -> // 
        Keys.End -> // 
        else -> null // all other key presses should be ignored, so return null to stop flow processing
    }.also { if(it != null) event.preventDefault() // page itself should not scroll up or down! }
    //          ^^^^^^^^^^
    //          Only if a shortcut was matched
} handledBy { /* ... */ }
```

## Advanced Topics

### Event Capturing

Event capturing is less commonly used within UI-Code. Nevertheless, fritz2 supports it via dedicated
event-factory-variants suffixed with the `Captured`-suffix.

Internally, they set the `capture`-flag within the `addEventListener`-function of a DOM-element.

Look at the following example:
```kotlin
render {
    div {
        clicksCaptured { stopPropagation() } handledBy { console.log("outer") }
//      ^^^^^^^^^^^^^^   ^^^^^^^^^^^^^^^^^
//      |                stop event handling at this level above the child
//      |
//      bind to event in `capture` mode        

        div(id = innerId) {
            clicks handledBy { console.log("inner") }
//          ^^^^^^
//          will never reach this DOM level because of the captured event of the parent element.
        }
    }
}
```

::: info
For all regular events, a sibling function with the `Captured`-suffix is provided as well.
:::

### Custom events

You can easily create a custom event in fritz2 by using the appropriate DOM-API types and functions, combined with
the core function `subscribe` for lifting the dispatched DOM-Event into a fritz2 `Listener` (`Flow`) for further
processing.

Have a look at its signature:
```kotlin
fun <E : Event, T : EventTarget> T.subscribe(
    name: String,
    capture: Boolean = false,
    selector: E.() -> Boolean = { true }
): Listener<E, T>
```
As a minimum, you must provide the event's name, but you can tune the handling with the other parameters.
You should have learned about those in the [essentials](#essentials)-section.

Have a look at a simple example, demonstrating the usage of `subscribe`:
```kotlin
// just a simple data class for simulating a specific payload
data class Framework(val name: String, val version: String)

// Create an event
val myEvent = CustomEvent(
    "fritz2",
    CustomEventInit(detail = Framework("fritz2", "1-0-RC18"))
)

div {
    subscribe<CustomEvent>(myEvent.type) handledBy {
//  ^^^^^^^^^ ^^^^^^^^^^^  ^^^^^^^^^^^^
//  |             |        at least provide the name
//  |          Specify the type
//  create a listener
        console.log("My custom event occurred with data: ${it.detail as Framework}")
    }

    button {
        +"Dispatch Event"
        clicks handledBy { this@div.domNode.dispatchEvent(myEvent) }
        //  ^^^^^^                              ^^^^^^^^^^^^^
        //  use another event to trigger        call DOM-API to dispatch the event
        //  the custom one - could also be
        //  based upon any other `Flow`-source`
    }
}
```