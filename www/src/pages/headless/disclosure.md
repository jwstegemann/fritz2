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

::: warning  
__Important__  

The headless disclosure only provides the logic to determine the state of the panel. The actual visibility
of the panel must be implemented by the developer using CSS!

The simplest way is to hide and show the panel via the `display` property.  

For more in-depth information, see the section on [State-Dependent Styling](#state-dependent-styling).  
:::

```kotlin
disclosure {
    disclosureButton {
        +"When is being headless a good thing?"
    }
    disclosurePanel {
        inlineStyle(opened.map {
            if (it) "display: block;" else "display: none;"
        })
        +"As foundation for web components!"
    }
}
```

## State-Dependent Styling

In order to design the styling or entire structures depending on the state of the disclosure, you can refer to the
Boolean data stream `opened`.

::: warning  
As mentioned earlier, the disclosure not only _allows_ you to tp provide styling depending on the state, it also
_requires_ you to do so.  
:::

This is due to the fact of the panel not rendering and removing itself from the DOM, but only hiding and showing it.
Since fritz2 is framework-agnostic, it does not provide any styling or classes for that purpose.

There are multiple reasons for this design decision:
- Creating a mount-point is not related to styling, thus it can easily be done in headless. Hiding/showing an element 
is - that's why it cannot be done in headless.
- Leaving the entire styling (including the styles used for hiding/showing the element) to the implementor allows for 
greater flexibility on how a concrete disclosure component might work.
- By leaving the styling to the user, all styling can be applied in a single place whithout some internal mechanics
working against you.

Styling of the `opened` and `closed` states is not limited to just hiding or showing the panel. You are free to design
the panel in any way you like. For example, the disclosure may be designed to behave more like a spoiler element
that blurs its content when closed.

In order to work with transitions, you can use the `transition` function. Because of the specific nature of the 
disclosure component, you have to use a special overloaded version of the `transition` function, which essentially
combines the `transition` function with the `inlineStyle` function. This enables you to define both the styling and the
transition effect in one place, based on the `opened` state.

```kotlin
disclosure {
    disclosureButton("flex items-center gap-2") {
        opened.render {
            svg("h-6 w-6 transform") {
                // change Icon depending on `opened` state
                if (it) content(HeroIcons.chevron_up)
                else content(HeroIcons.chevron_down)
            }
        }
        +"When is being headless a good thing?"
    }
    disclosurePanel {
        transition(
            opened,
            enter = "transition duration-250 ease-out",
            enterStart = "opacity-0 scale-y-95",
            enterEnd = "opacity-100 scale-y-100",
            leave = "transition duration-250 ease-in",
            leaveStart = "opacity-100 scale-y-100",
            leaveEnd = "opacity-0 scale-y-95",
            hasLeftClasses = "hidden",
            initialClasses = "hidden"
        )

        +"As a foundation for web components!"
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
        inlineStyle(opened.map {
            if (it) "display: block;" else "display: none;"
        })
        
        +"As a foundation for web components!"
        
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
        inlineStyle(opened.map {
            if (it) "display: block;" else "display: none;"
        })
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