---
title: Switch
layout: layouts/docs.njk
permalink: /headless/switch/
eleventyNavigation:
    key: switch
    title: Switch (Toggle)
    parent: headless
    order: 90
demoHash: switch
teaser: "The switch component can be used for switching between two states, i.e. on or off, yes or no, etc. 
The semantics and operation correspond to those of a single checkbox."
---

## Basic Example

Switches are built using the ``switch`` factory function. It is mandatory to pass a boolean data stream into the
component via the ``value`` property.

The current status can be queried via the ``enabled`` data stream.

```kotlin
val switchState = storeOf(false)

switch {
    value(switchState)
    className(enabled.map { if (it) "bg-indigo-600" else "bg-gray-200" })
    span {
        className(enabled.map { if (it) "translate-x-5" else "translate-x-0" })
    }
}
```

## Add Label and Description

If you want to enrich a switch with a label or an additional description, you have to use the factory
function ``switchWithLabel`` instead of ``switch``. Beware that the toggle element must be explicitly created inside the
scope using the `switchToggle` factory in this case.

Inside the scope of `switchWithLabel` there are the bricks `switchLabel` and `switchDescription` to add the 
appropriate information. It is possible to provide more than one description.

```kotlin
    switchWithLabel {
        value(switchState)
        span {
            switchLabel(tag = RenderContext::span) { //default tag ist ``label``
                +"Available to hire"
            }
            switchDescription(tag = RenderContext::span) { //default tag ist ``p``
                +"Nulla amet tempus sit accumsan."
            }
        }
        switchToggle {
            className(enabled.map { if (it) "bg-indigo-600" else "bg-gray-200" })
            span {
                className(enabled.map { if (it) "translate-x-5" else "translate-x-0" })
            }
        }
    }
```

## Validation

Data binding allows the Switch component to grab the validation messages and provide its own building
block `switchValidationMessages` that is only rendered when there are some messages. These messages are exposed
within its scope as a data stream `msgs`.

```kotlin
val switchState = storeOf(false)

switch {
    value(switchState)
    className(enabled.map { if (it) "bg-indigo-600" else "bg-gray-200" })
    span {
        className(enabled.map { if (it) "translate-x-5" else "translate-x-0" })
    }
    switchValidationMessages(tag = RenderContext::ul) { 
        msgs.renderEach { li { +it.message } }
    }
}
```

## Mouse Interaction

Clicking on an element created with ``switch``, ``switchWithLabel`` or ``switchLabel`` toggles between the
both states back and forth.

## Keyboard Interaction

| Command                              | Description                                     |
|--------------------------------------|-------------------------------------------------|
| [[Space]] when a `Switch` is focused | Switches back and forth between the two states  |

## API

### Summary / Sketch
```kotlin
switch() {
    val value: DatabindingProperty<Boolean>
    val enabled: Flow<Boolean>
    
    switchValidationMessages() {
        val msgs: Flow<List<ComponentValidationMessage>>
    }
}

switchWithLabel() {
    val value: DatabindingProperty<Boolean>
    val enabled: Flow<Boolean>
    
    switchToggle() { }
    switchLabel() { }
    switchDescription() { } // use multiple times
    switchValidationMessages() {
        val msgs: Flow<List<ComponentValidationMessage>>
    }
}

```

### switch

Parameters: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `button`

| Scope property | Typ                            | Description                                                      |
|----------------|--------------------------------|------------------------------------------------------------------|
| `value`        | `DatabindingProperty<Boolean>` | Mandatory (two-way) data-binding representing the boolean state. |
| `enabled`      | `Flow<Boolean>`                | Current state. (Default `false`)                                 |


### switchWithLabel

Parameters: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ                            | Description                                                      |
|----------------|--------------------------------|------------------------------------------------------------------|
| `value`        | `DatabindingProperty<Boolean>` | Mandatory (two-way) data-binding representing the boolean state. |
| `enabled`      | `Flow<Boolean>`                | Current state. (Default `false`)                                 |


### switchValidationMessages

Available in the scope of: `switch`, `switchWithLabel`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ                                      | Description                                                           |
|----------------|------------------------------------------|-----------------------------------------------------------------------|
| `msgs`         | `Flow<List<ComponentValidationMessage>>` | provides a data stream with a list of ``ComponentValidationMessage``s |


### switchToggle

Available in the scope of: ``switchWithLabel``

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `button`


### switchLabel

Available in the scope of: ``switchWithLabel``

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`


### switchDescription

Available in the scope of: ``switchWithLabel``

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `span`
