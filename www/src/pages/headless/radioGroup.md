---
title: RadioGroup 
layout: layouts/docs.njk 
permalink: /headless/radioGroup/ 
eleventyNavigation:
    key: radioGroup
    title: RadioGroup
    parent: headless 
    order: 80 
demoHash: radioGroup 
teaser: "A RadioGroup serves as a basis for the single selection of any element."
---

## Basic Example

RadioGroups are created using the generic component factory function `fun <T> radioGroup()`.
The type parameter `T` can be replaced by any type, e.g. a domain class.

By clicking on an option or by using the [[↑]] and [[↓]] keys if the RadioGroup is focused,
an option can be selected. Only one option can be selected at a time; the previously selected option
is automatically deselected accordingly.

It is therefore mandatory to specify a data stream or a store  of a `List<T>` as data binding via the `value` property.
The component supports two-way-data-binding, i.e. it reflects selected element from the outside by a `Flow<T>`
but also emits the updated selection to the outside by some `Handler`.

The available options are not injected directly into the component as parameter, but one at a time by calling the
factory `radioGroupOption` and providing the option as first parameter. A typical pattern is therefore the use of a
loop in which this factory function is called accordingly.

::: warning
**Beware:** Options cannot be removed from the RadioGroup. Once an option is added to the group by `radioGroupOption`,
it will remain inside forever. So never use some reactive pattern for populating the RadioGroup as some `Flow<List<T>>`
combined with some call to `renderEach` or alike!

If the options change, you must re-render the whole component.
:::

For selecting, a `Tag` must be created using `radioGroupOptionToggle`.

```kotlin
// some domain type for this example, a collection to choose from, and an external store
data class Plan(val name: String, val ram: String, val cpus: String, val disk: String, val price: String)
val plans = listOf<Plan>(/* ... */)
val choice = storeOf<Plan?>(null)

radioGroup<HTMLFieldSetElement, Plan?>(tag = RenderContext::fieldset) {
    // set up (two-way-)data-binding
    value(choice)
    
    // using a loop is a typical pattern to create the options
    plans.forEach { option ->
        radioGroupOption(option) { // needs to be created for each selectable option!
            radioGroupOptionToggle {
                +option.name
            }
        }
    }
}
```

## Styling the selected Element

Since a headless component does not provide any styling, the component supports the user to react to the current
selection.

Within the scope of the `radioGroupOption` factory, the component provides the boolean stream `selected`. This can be
used to query whether this option is currently selected or not.

A common pattern is to dynamically add or remove CSS classes. Of course also whole elements can be added or removed from
the DOM depending on the state `selected`.

```kotlin
radioGroup<HTMLFieldSetElement, Plan?>(tag = RenderContext::fieldset) {
    value(choice)
    plans.forEach { option ->
        radioGroupOption(option) {
            radioGroupOptionToggle {
                // use `selected`-Flow with `className` to react to state changes
                className(selected.map {
                    if (it) "bg-indigo-200" else "bg-white"
                })
                +option.name
            }
        }
    }
}
```

## Styling the active Element

A RadioGroup also provides information about which option is currently active, i.e. has the focus.

For this purpose, the scope of `radioGroupOption` offers the Boolean data stream `active`. This one can (and should)
be used to provide a specific style for the `true` state.

Since often both the status of the selection and the focus stylistically affect the same elements, it is a
typical pattern to combine both data streams. Use the `combine` method on `Flow`s for this purpose:

```kotlin
radioGroup<HTMLFieldSetElement, Plan?>(tag = RenderContext::fieldset) {
    value(choice)
    plans.forEach { option ->
        radioGroupOption(option) {
            radioGroupOptionToggle {
                // combine `selected` and `active`-Flow with `className` to react to state changes
                className(selected.combine(active) { sel, act ->
                    // use `classes` to attach both styling results
                    classes(
                        if (sel) "bg-indigo-200" else "bg-white",
                        if (act) "ring-2 ring-indigo-500 border-transparent" else "border-gray-300"
                    )
                })
                +option.name
            }
        }
    }
}
```

## Add Label and Description

The RadioGroup can be labeled using `radioGroupLabel`, the individual options can be enriched per
`radioGroupOptionLabel` and `radioGroupOptionDescription` with a label and some descriptions.

```kotlin
radioGroup<HTMLFieldSetElement, Plan?>(tag = RenderContext::fieldset) {
    value(choice)

    // describe the whole radio-group itself
    radioGroupLabel("sr-only") { +"Server size" }
    
    plans.forEach { option ->
        radioGroupOption(option) {
            radioGroupOptionToggle {
                // enrich the single options with label and (multiple) description(s)                        
                div("flex items-center") {
                    div("text-sm") {
                        radioGroupOptionLabel("font-medium text-gray-900", tag = RenderContext::p) {
                            +option.name
                        }
                        radioGroupOptionDescription("text-gray-500", tag = RenderContext::div) {
                            p("sm:inline") { +option.cpus }
                            span("hidden sm:inline sm:mx-1") {
                                attr(Aria.hidden, "true")
                                +"·"
                            }
                            p("sm:inline") { +option.ram }
                        }
                    }
                }
                radioGroupOptionDescription(
                    "mt-2 flex text-sm sm:mt-0 sm:block sm:ml-4 sm:text-right",
                    tag = RenderContext::div
                ) {
                    div("font-medium text-gray-900") { +option.price }
                    div("ml-1 text-gray-500 sm:ml-0") { +"""/mo""" }
                }
            }
        }
    }
}
```

## Validation

Data binding allows the RadioGroup component to grab the validation messages and provide its own building
block `radioGroupValidationMessages` that is only rendered when there are some messages. These messages are exposed
within its scope as a data stream `msgs`.


```kotlin
radioGroup<HTMLFieldSetElement, Plan?>(tag = RenderContext::fieldset) {
    value(choice)
    plans.forEach { option ->
        radioGroupOption(option) {
            radioGroupOptionToggle {
                +option.name
            }
        }
    }

    checkboxGroupValidationMessages(tag = RenderContext::ul) {
        msgs.renderEach { li { +it.message } }
    }
}
```

## Mouse Interaction

Clicking on an element created with ``radioGroupOptionToggle`` selects the underlying option and deselects the
previously selected option.

## Keyboard Interaction

| Command                                        | Description                                     |
|------------------------------------------------|-------------------------------------------------|
| [[↑]] [[↓]] when an option-toggle is focused   | (Reverse) Cyclic selection through all options. |

## API

### Summary / Sketch
```kotlin
radioGroup<T>() {
    val value: DatabindingPropert<T>

    radioGroupLabel() { }
    radioGroupValidationMessages() {
        val msgs: Flow<List<ComponentValidationMessage>>
    }
    // for each T {
        radioGroupOption(option: T) {
            val selected: Flow<Boolean>
            val active: Flow<Boolean>
    
            radioGroupOptionToggle() { }
            radioGroupOptionLabel() { }
            radioGroupOptionDescription() { } // use multiple times
        }
    // }
}
```

### radioGroup

Parameters: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ                       | Description                                        |
|----------------|---------------------------|----------------------------------------------------|
| `value`        | `DatabindingProperty<T>`  | Mandatory (two-way) data-binding for the selection |


### radioGroupLabel

Available in the scope of: `radioGroup`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`


### radioGroupValidationMessages

Available in the scope of: `radioGroup`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ                                      | Description                                                           |
|----------------|------------------------------------------|-----------------------------------------------------------------------|
| `msgs`         | `Flow<List<ComponentValidationMessage>>` | provides a data stream with a list of ``ComponentValidationMessage``s |


### radioGroupOption

Available in the scope of: `radioGroup`

Parameters:
- `option: T`: Mandatory option object that is to be managed by this option block.
- `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ             | Description                                                                                                                                     |
|----------------|-----------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| `selected`     | `Flow<Boolean>` | This data stream provides the selection status of the managed option: `true` the option is selected, `false` if not.                            |
| `active`       | `Flow<Boolean>` | This data stream indicates whether an option has focus: `true` the option has focus, `false` if not. Only one option can have focus at a time.  |


### radioGroupOptionToggle

Available in the scope of: `radioGroupOption`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`


### radioGroupOptionLabel

Available in the scope of: `radioGroupOption`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`


### radioGroupOptionDescription

Available in the scope of: `radioGroupOption`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `span`