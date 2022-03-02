---
title: Modal
layout: layouts/docs.njk
permalink: /headless/modal/
eleventyNavigation:
    key: modal
    title: Modal
    parent: headless
    order: 60
demoHash: modal
teaser: "A modal dialog displays any content on a layer in front of the rest of the application.
Intelligent mouse and keyboard management limit the interaction to the modal window as long as it is open."
---

## Basic Example

To create a modal dialog, the `modal` factory function must be called. Since this does not render anything per se, it
does not get any of the standard parameters as a big exception. The topmost visible structure is created by
`modalPanel` factory function. Within its scope, the complete content of the modal window including the so-called
overlay area (`modalOverlay`) is created.

Boolean data binding must always be specified, since the opening of a modal dialog can only be triggered by an external
upcoming event, e.g. by clicking on a button.

Closing is usually triggered within the modal dialog. The scope of `modal` provides the `close` handler for this task.

```kotlin
val toggle = storeOf(false)

button {
    +"Open"
    clicks.map { true } handledBy toggle.update
}

modal {
    openClose(toggle)
    modalPanel {
        modalOverlay {
            // add styling and maybe some pleasant transition
        }
        div {
            p { +"I am some modal dialog! Press Cancel to exit."}
            // you should at lease add one focusable element to the modal!
            button {
                type("button")
                +"Cancel"
                clicks handledBy close // use handler to set the state to `false`
            }
        }
    }
}
```

## Focus Management

In order to prevent interaction with the rest of the application via keyboard interaction, the modal dialog uses a
so-called focus trap by default. Therefore, the focus will cycle through all the focusable elements using [[Tab]]
without leaving the modal panel.

For this reason there should always be at least one focusable element in the modal window. If there is no focusable
element found, a warning should appear in the log:
```text
There are no focusable elements inside the focus-trap!
```

This behaviour can be customized by providing other values for the `setInitialFocus` field of the [`modal`](#modal)
scope. For example `TryToSet` to suppress the warning. But it is recommended to design modals always with some focusable
element, so the warning will help you to detect design flaws!

By default, the first focusable element is always given the focus. In order to focus a specific tag initially,
the `setInitialFocus` function can be called inside a `Tag`.

```kotlin
modal {
    openClose(toggle)
    modalPanel {
        p { +"I am some modal dialog! Press Cancel to exit."}
        // first focusable element
        button {
            type("button")
            +"Cancel"
            clicks handledBy close 
        }
        // second focusable element
        button {
            type("button")
            +"I will get the initial focus!"
            // adds a data-attribute to the tag so the focus can be delegated here
            setInitialFocus()
        }
    }
}
```

## Add Label and Description

For accessibility reasons, a modal dialog should provide a title and at least one descriptive element. This is done via
the factory functions `modalTitle` and `modalDescription`.

```kotlin
modal {
    openClose(toggle)
    modalPanel {         
        // add title and description
        modalTitle { +"Example Dialog" }
        modalDescription { 
            +"I am some modal dialog! Press Cancel to exit."
        }
        
        button {
            type("button")
            +"Cancel"
            clicks handledBy close
        }
    }
}
```

## Transitions

Showing and hiding the modal dialog can be easily animated with the help of `transition`:

```kotlin
modal {
    openClose(toggle)
    modalPanel {
        modalOverlay {
            // some nice fade in/out effect for the overlay
            transition(
                enter = "ease-out duration-300",
                enterStart = "opacity-0",
                enterEnd = "opacity-100",
                leave = "ease-in duration-200",
                leaveStart = "opacity-100",
                leaveEnd = "opacity-0"
            )            
        }
        div {
            // some nice fade in/out and scale in/out effect for the content
            transition(
                enter = "transition duration-100 ease-out",
                enterStart = "opacity-0 scale-95",
                enterEnd = "opacity-100 scale-100",
                leave = "transition duration-100 ease-in",
                leaveStart = "opacity-100 scale-100",
                leaveEnd = "opacity-0 scale-95"
            )
            
            p { +"I am some modal dialog! Press Cancel to exit."}
            button {
                type("button")
                +"Cancel"
                clicks handledBy close 
            }
        }
    }
}
```

## Mouse Interaction

By default, no mouse interaction is supported through the modal dialog. Typically, within the modal dialog there should
be some clickable element, which triggers the `close` handler and thus the dialog closes.

## Keyboard Interaction

| Command               | Description                                                                                                           |
|-----------------------|-----------------------------------------------------------------------------------------------------------------------|
| [[Tab]]               | Cycles through all focusable elements of an open modal window. There should always be at least one focusable element! |
| [[Shift]] + [[Tab]]   | Cycles backwards through all focusable elements of an open modal window.                                              |

## API

### Summary / Sketch
```kotlin
modal() {
    var restoreFocus: Boolean
    var setInitialFocus: Boolean    
    // inherited by `OpenClose`
    val openState: DatabindingProperty<Boolean>
    val opened: Flow<Boolean>
    val close: SimpleHandler<Unit>
    val open: SimpleHandler<Unit>
    val toggle: SimpleHandler<Unit>
    
    modalPanel() {
        modalOverlay() { }
        modalTitle() { }
        modalDescription() { } // use multiple times
        
        // setInitialFocus() within one tag is possible
    }
}
```

### modal

Parameters: **no**

Default-Tag: No tag is rendered!

| Scope property     | Typ                            | Description                                                                                                                                         |
|--------------------|--------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| `restoreFocus`     | `Boolean`                      | If `true` (default) the focus will be reset to the last focused element after closing the modal.                                                    |
| `setInitialFocus`  | `InitialFocus`                 | If `InsistToSet` (default), the focus is set to the first focusable DOM element or the element marked with [`setInitialFocus()`](focus-management). |
| `openState`        | `DatabindingProperty<Boolean>` | Mandatory (two-way) data-binding for opening and closing.                                                                                           |
| `opened`           | `Flow<Boolean>`                | Data stream that provides Boolean values related to the "open" state. Quite useless within a modal, as it is always `true`                          |
| `close`            | `SimpleHandler<Unit>`          | Handler to close the list box from inside.                                                                                                          |
| `open`             | `SimpleHandler<Unit>`          | handler to open; does not make sense to use within a modal!                                                                                         |
| `toggle`           | `SimpleHandler<Unit>`          | handler for switching between open and closed; does not make sense to use within a modal.                                                           |


### modalPanel

Available in the scope of: `modal`

Parameters: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

### modalOverlay

Available in the scope of: `modalPanel`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

### modalTitle

Available in the scope of: `modalPanel`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `h2`

### modalDescription

Available in the scope of: `modalPanel`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `p`