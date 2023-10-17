---
title: TextArea
description: "A TextArea offers a multi-line text input."
layout: layouts/docs.njk
permalink: /headless/textarea/
eleventyNavigation:
    key: textarea
    title: TextArea
    parent: headless
    order: 110
teaser: true
demoHash: textarea
demoHeight: 14rem
---

## Basic Example

A TextArea is created with the `textArea` component factory function. Within its scope a `string` based data
binding named `value` has to be initialized.

Furthermore, the actual input element must be created using `textareaTextfield`.

```kotlin
val name = storeOf("", job = Job())

textArea {
    value(name)
    textareaTextfield {
        placeholder("The name is...")
    }
}
```

## Add Label and Description

Within the scope there are the two block functions `textareaLabel` and `textareaDescription`, that can be used to
enhance the input field with additional label or descriptions.

If the HTML `label` tag is used for the label (by default), a mouse click on the label causes the input field to be
focused.

```kotlin
val name = storeOf("", job = Job())

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


## Mouse Interaction

Clicking outside the TextArea component (so the focus gets lost) will update the `value` to the content of the area.

If the HTML `label` tag is used for the label (by default), a mouse click on the label causes the text area to be
focused.

## Keyboard Interaction

| Command                                                  | Description         |
|----------------------------------------------------------|---------------------|
| Any key that will trigger a `change` event like [[Tab]]  | updates the `value` |

For more details which key will trigger a change, refer to this
[documentation](https://developer.mozilla.org/en-US/docs/Web/API/HTMLElement/change_event)

## API

### Summary / Sketch
```kotlin
textArea() {
    val value: DatabindingProperty<String>

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

| Scope property | Typ                           | Description                                           |
|----------------|-------------------------------|-------------------------------------------------------|
| `value`        | `DatabindingProperty<String>` | Mandatory (two-way) data binding for the input value. |

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
