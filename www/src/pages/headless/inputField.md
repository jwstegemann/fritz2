---
title: InputField
description: "An InputField offers a single line of text input."
layout: layouts/docs.njk
permalink: /headless/inputfield/
eleventyNavigation:
    key: inputfield
    title: InputField
    parent: headless
    order: 30
teaser: true
demoHash: inputfield
demoHeight: 14rem
---

## Basic Example

An InputField is created with the `inputField` component factory function. Within its scope a `string` based data
binding named `value` has to be initialized.

Optionally, a placeholder text can be set using the `placeholder` attribute hook.

Furthermore, the actual input element must be created using `inputTextfield`.

```kotlin
val name = storeOf("")

inputField {
    value(name)
    placeholder("The name is...")
    inputTextfield { }
}
```

## Add Label and Description

Within the scope there are the two block functions `inputLabel` and `inputDescription`, that can be used to enhance the
input field with additional label or descriptions.

If the HTML `label` tag is used for the label (by default), a mouse click on the label causes the input field to be
focused.

```kotlin
val name = storeOf("")

inputField {
    value(name)
    placeholder("The name is...")
    inputLabel {
        +"Enter the framework's name"
    }
    inputTextfield { }
    inputDescription {
        +"The name should reflect the concept of the whole framework."
    }
}
```

## Deactivate

The InputField component supports the (dynamic) deactivation and activation of the input field. To do this, the boolean
Attribute hook `disabled` must be set accordingly.

```kotlin
val toggle = storeOf(false) 

button {
    +"Enable / Disable"
    clicks.map{ !toggle.current } handledBy toggle.update
}

inputField {
    value(name)
    
    // values on the `FLow` will disable or enable the input field
    disabled(toggle.data)
    
    inputTextfield { }
}
```

## Validation

Data binding allows the InputField component to grab the validation messages and provide its own building
block `inputValidationMessages` that is only rendered when there are some messages. These messages are exposed within
its scope as a data stream `msgs`.

```kotlin
inputField {
    value(name)
    inputTextfield { }
    
    inputValidationMessages(tag = RenderContext::ul) {
        msgs.renderEach { li { +it.message } }
    }
}
```

## API

### Summary / Sketch
```kotlin
inputField() {
    val value: DatabindingProperty<String>
    val placeHolder: AttributeHook<String>
    val disabled: BooleanAttributeHook

    inputTextfield() { }
    inputLabel() { }
    inputDescription() { } // use multiple times
    inputValidationMessages() { 
        msgs: Flow<List<ComponentValidationMessage>>
    }
}
```

### inputField

Parameters: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ                           | Description                                             |
|----------------|-------------------------------|---------------------------------------------------------|
| `value`        | `DatabindingProperty<String>` | Mandatory (two-way) data-binding for the input value.   |
| `placeHolder`  | `AttributeHook<String>`       | Optional hook to (dynamically) set a placeholder.       |
| `disabled`     | `BooleanAttributeHook`        | Optional hook to (dynamically) enable or disable input. |


### inputTextfield

Available in the scope of: `inputField`

Parameters: `classes`, `scope`, `tag`, `initialize`

Tag: `input` (not customizable!)


### inputLabel

Available in the scope of: `inputField`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`


### inputDescription

Available in the scope of: `inputField`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `p`


### inputValidationMessages

Available in the scope of: `inputField`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ                                      | Description                                                           |
|----------------|------------------------------------------|-----------------------------------------------------------------------|
| `msgs`         | `Flow<List<ComponentValidationMessage>>` | provides a data stream with a list of ``ComponentValidationMessage``s |
