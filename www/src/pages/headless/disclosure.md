---
title: Disclosure
description: "A disclosure allows showing and hiding content such as switchable accordion menus."
layout: layouts/docs.njk
permalink: /headless/disclosure/
eleventyNavigation:
    key: disclosure
    title: Disclosure
    parent: headless
    order: 20
teaser: true
demoHash: disclosure
demoHeight: 30rem
---

## Basic Example

A disclosure is generated via the `disclosure` component factory function. Within this scope a button must be generated
by `disclosureButton` and a panel via `disclosurePanel`.

The button serves as a control element for opening and closing the panel area. This is done by one
Mouse click or the [[Space]] key if the button tag has focus.

```kotlin
disclosure {
    disclosureButton {
        +"When is being headless a good thing?"
    }
    disclosurePanel {
        +"As foundation for web components!"
    }
}
```

## State depending Styling

In order to design the styling or entire structures depending on the state of the disclosure, you can refer to the
Boolean data stream `opened`.

Typical patterns are use within `className` to set different CSS classes depending on state,
or simply some conditional rendering.

```kotlin
disclosure {
    disclosureButton {
        +"When is being headless a good thing?"
        
        span("ml-6 h-7 flex items-center") {
            // add some Icon that reflects the state visually
            opened.render(into = this) {
                svg("h-6 w-6 transform") {
                    // change Icon depending on `opened` state
                    if (it) content(HeroIcons.chevron_up)
                    else content(HeroIcons.chevron_down)
                }
            }
        }
    }
    disclosurePanel {
        +"As foundation for web components!"
    }
}
```

## Open and Close

To enable opening programmatically and independently of the `disclosureButton` element, an optional
boolean data binding `openState` to be set in the component.

In addition, a disclosure itself offers a `close` handler for closing, which can be attached e.g. within the panel to 
some closing event. Imagine some custom close-button element for that purpose.

```kotlin
val toggle = storeOf(true, job = Job()) // show Panel at start

disclosure {
    
    // establish two-way data binding
    openClose(toggle)
    
    disclosureButton {
        +"When is being headless a good thing?"
    }
    disclosurePanel {
        +"As foundation for web components!"
        button {
            +"Close"
            // use `close` Handler to explicitly close the panel from within
            clicks.map { false } handledBy close
        }
    }
}
```

## Closing by the Panel

If it is necessary that an action on the panel itself closes the panel, i.e. a mouse click or the [[Space]] button, if
the panel is focused, there is a dedicated `disclosureCloseButton` building block in the scope of the panel.

```kotlin
disclosure {
    disclosureButton {
        +"When is being headless a good thing?"
    }
    disclosurePanel {
        disclosureCloseButton {
            +"As foundation for web components!"
            +"Simply click or press Space to close."
        }
    }
}
```

## Mouse Interaction

Clicking on a `disclosureButton` will open or close the panel.

## Keyboard Interaction

| Command                                     | Description                |
|---------------------------------------------|----------------------------|
| [[Space]] when `disclosureButton` has focus | Opens or closes the panel. |

## API

### Summary / Sketch
```kotlin
disclosure() {
    // inherited by `OpenClose`
    val openState: DatabindingProperty<Boolean>
    val opened: Flow<Boolean>
    val close: SimpleHandler<Unit>
    val open: SimpleHandler<Unit>
    val toggle: SimpleHandler<Unit>

    disclosureButton() { }
    disclosurePanel() {
        disclosureCloseButton() {}
    }
}
```

### disclosure

Parameters: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ                              | Description                                                                                    |
|----------------|----------------------------------|------------------------------------------------------------------------------------------------|
| `openState`    | `DatabindingProperty<Boolean>`   | Optional (two-way) data binding for opening and closing.                                       |
| `opened`       | `Flow<Boolean>`                  | Data stream that provides Boolean values related to the "open" state.                          |
| `close`        | `SimpleHandler<Unit>`            | Handler to close the disclosure from inside.                                                   |
| `open`         | `SimpleHandler<Unit>`            | handler to open; does not make sense to use within a disclosure!                               |
| `toggle`       | `SimpleHandler<Unit>`            | handler for switching between open and closed; does not make sense to use within a disclosure. |

### disclosureButton

Available in the scope of: `disclosure`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `button`

### disclosurePanel

Available in the scope of: `disclosure`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

### disclosureCloseButton

Available in the scope of: `disclosurePanel`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `button`