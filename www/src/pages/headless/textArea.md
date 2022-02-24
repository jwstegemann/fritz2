---
title: TextArea
layout: layouts/headlessWithContentNav.njk
permalink: /headless/textarea/
eleventyNavigation:
    key: textarea
    title: TextArea
    parent: headless
    order: 110
demoHash: textarea
teaser: "A TextArea offers a multi-line text input."
---

## Basic Example

A TextArea is created with the `textArea` component factory function. Within its scope a `string` based data
binding named `value` has to be initialized.

Optionally, a placeholder text can be set using the `placeholder` attribute hook.

Furthermore, the actual input element must be created using `textareaTextfield`.

```kotlin
val name = storeOf("")

textArea {
    value(name)
    placeholder("The name is...")
    textareaTextfield { }
}
```

## Add Label and Description

Within the scope there are the two block functions `textareaLabel` and `textareaDescription`, that can be used to
enhance the input field with additional label or descriptions.

If the HTML `label` tag is used for the label (by default), a mouse click on the label causes the input field to be
focused.

```kotlin
val name = storeOf("")

textArea {
    value(name)
    placeholder("The name is...")
    textareaLabel {
        +"Enter the framework's name"
    }
    textareaTextfield { }
    textareaDescription {
        +"The name should reflect the concept of the whole framework."
    }
}
```

## Deactivate

The TextArea component supports the (dynamic) deactivation and activation of the input field. To do this, the boolean
Attribute hook `disabled` must be set accordingly.

```kotlin
val toggle = storeOf(false) 

button {
    +"Enable / Disable"
    clicks.map{ !toggle.current } handledBy toggle.update
}

textArea {
    value(name)
    
    // values on the `FLow` will disable or enable the textarea field
    disabled(toggle.data)
    
    textareaTextfield { }
}
```

## Validation

Data binding allows the TextArea component to grab the validation messages and provide its own building
block `textareaValidationMessages` that is only rendered when there are some messages. These messages are exposed within
its scope as a data stream `msgs`.

```kotlin
textArea {
    value(name)
    textareaTextfield { }
    
    textareaValidationMessages(tag = RenderContext::ul) {
        msgs.renderEach { li { +it.message } }
    }
}
```

## API

### Summary / Sketch
```kotlin
textArea() {
    val value: DatabindingProperty<String>
    val placeHolder: AttributeHook<String>
    val disabled: BooleanAttributeHook

    textareaTextfield() { }
    textareaLabel() { }
    textareaDescription() { } // use multiple times
    textareaValidationMessages() {
        val msgs: Flow<List<ComponentValidationMessage>>
    }
}
```

### textArea

Parameters: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ                           | Description                                             |
|----------------|-------------------------------|---------------------------------------------------------|
| `value`        | `DatabindingProperty<String>` | Mandatory (two-way) data-binding for the input value.   |
| `placeHolder`  | `AttributeHook<String>`       | Optional hook to (dynamically) set a placeholder.       |
| `disabled`     | `BooleanAttributeHook`        | Optional hook to (dynamically) enable or disable input. |

### textareaTextfield

Available in the scope of: `textArea`

Parameters: `classes`, `scope`, `tag`, `initialize`

Tag: `textarea` ((not customizable!)


### textareaLabel

Available in the scope of: `textArea`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`


### textareaDescription

Available in the scope of: `textArea`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `p`


### textareaValidationMessages

Available in the scope of: `textArea`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ                                      | Description                                                           |
|----------------|------------------------------------------|-----------------------------------------------------------------------|
| `msgs`         | `Flow<List<ComponentValidationMessage>>` | provides a data stream with a list of ``ComponentValidationMessage``s |
