---
title: CheckboxGroup 
layout: layouts/docs.njk
permalink: /headless/checkboxgroup/ 
eleventyNavigation:
    key: checkbox
    title: CheckboxGroup
    parent: headless 
    order: 10 
demoHash: checkboxGroup
demoHeight: 27rem
teaser: "A CheckboxGroup serves as the basis for multiple selection of any element."
---

## Basic Example

CheckboxGroups are created using the generic component factory function `fun <T> checkboxGroup()`.
The type parameter `T` can be replaced by any type, e.g. a domain class.

An option can be selected or deselected by clicking on that option or by pressing the [[Space]] key if the option is 
focused. Any number of options can be selected.

It is therefore mandatory to specify a data stream or a store  of a `List<T>` as data binding via the `value` property.
The component supports two-way-data-binding, i.e. it reflects selected elements from the outside by a `Flow<List<T>>`
but also emits the updated selection to the outside by some `Handler`.

The available options are not injected directly into the component as parameter, but one at a time by calling the
factory `checkboxGroupOption` and providing the option as first parameter. A typical pattern is therefore the use of a
loop in which this factory function is called accordingly.

::: warning
**Beware:** Options cannot be removed from the CheckboxGroup. Once an option is added to the group
by `checkboxGroupOption`, it will remain inside forever. So never use some reactive pattern for populating the
CheckboxGroup as some `Flow<List<T>>` combined with some call to `renderEach` or alike!

If the options change, you must re-render the whole component.
:::

For selecting or deselecting, a `Tag` must be created using `checkboxGroupOptionToggle`.

```kotlin
// some domain type for this example, a collection to choose from, and an external store
data class Newsletter(val id: Int, val title: String, val description: String, val users: Int)
val mailingList = listOf<Newsletter>(/* ... */)
val subscriptions = storeOf(emptyList<Newsletter>())

checkboxGroup<HTMLFieldSetElement, Newsletter>(tag = RenderContext::fieldset) {
    // set up (two-way-)data-binding
    value(subscriptions)
    
    // using a loop is a typical pattern to create the options
    mailingList.forEach { option ->
        checkboxGroupOption(option) { // needs to be created for each selectable option!
            checkboxGroupOptionToggle(tag = RenderContext::label) {
                +option.title
            }
        }
    }
}
```

## Styling the selected Option

Since a headless component does not provide any styling, the component supports the user to react to the current
selection.

Within the scope of the `checkboxGroupOption` factory, the component provides the boolean stream `selected`. This can be
used to query whether this option is currently selected or not.

A common pattern is to dynamically add or remove CSS classes. Of course also whole elements can be added or removed from
the DOM depending on the state `selected`.

```kotlin
checkboxGroup<HTMLFieldSetElement, Newsletter>(tag = RenderContext::fieldset) {
    value(subscriptions)
    mailingList.forEach { option ->
        checkboxGroupOption(option) {
            checkboxGroupOptionToggle(tag = RenderContext::label) {
                // use `selected`-Flow with `className` to react to state changes
                className(selected.map {
                    if (it) "ring-2 ring-indigo-500 border-transparent"
                    else "border-gray-300"
                })
            }
            // render some Icon to support the visual "selected" impression only if option is selected
            selected.render {
                if (it) {
                    svg("h-5 w-5 text-indigo-600") {
                        content(HeroIcons.check_circle)
                        fill("currentColor")
                    }
                }
            }
        }
    }
}
```

## Add Label and Description

The CheckboxGroup can be labeled using `checkboxGroupLabel`, the individual options can be enriched per
`checkboxGroupOptionLabel` and `checkboxGroupOptionDescription` with a label and some descriptions.

```kotlin
checkboxGroup<HTMLFieldSetElement, Newsletter>(tag = RenderContext::fieldset) {
    value(subscriptions)
    
    // describe the whole checkbox-group itself
    checkboxGroupLabel(tag = RenderContext::legend) { +"Select some mailing lists" }
    
    mailingList.forEach { option ->
        checkboxGroupOption(option) {
            checkboxGroupOptionToggle(tag = RenderContext::label) {
                div("flex-1 flex") {
                    div("flex flex-col") {
                        // enrich the single options with label and description(s)
                        checkboxGroupOptionLabel("block text-sm font-medium text-gray-900") {
                            +option.title
                        }
                        checkboxGroupOptionDescription("flex items-center text-sm text-gray-500") {
                            +option.description
                        }
                        checkboxGroupOptionDescription("mt-2 text-sm font-medium text-gray-900") {
                            +"${option.users} users"
                        }
                    }
                }
            }
        }
    }
}
```

## Validation

Data binding allows the CheckboxGroup component to grab the validation messages and provide its own building
block `checkboxGroupValidationMessages` that is only rendered when there are some messages. These messages are exposed
within its scope as a data stream `msgs`.

```kotlin
checkboxGroup<HTMLFieldSetElement, Newsletter>(tag = RenderContext::fieldset) {
    value(subscriptions)
    mailingList.forEach { option ->
        checkboxGroupOption(option) { 
            checkboxGroupOptionToggle(tag = RenderContext::label) {
                +option.title
            }
        }
    }

    checkboxGroupValidationMessages(tag = RenderContext::ul) {
        msgs.renderEach { li { +it.message } }
    }
}
```

## Mouse Interaction

Clicking on an element created with ``checkboxGroupOptionToggle`` selects or deselects the corresponding option.

## Keyboard Interaction

| Command                                    | Description                                    |
|--------------------------------------------|------------------------------------------------|
| [[Space]] when an option-toggle is focused | Selects or deselects the corresponding option. |

## API

### Summary / Sketch
```kotlin
checkboxGroup<T>() {
    val value: DatabindingPropert<List<T>>

    checkboxGroupLabel() { }
    checkboxGroupValidationMessages() {
        val msgs: Flow<List<ComponentValidationMessage>>
    }
    // for each T {
        checkboxGroupOption(option: T) {
            val selected: Flow<Boolean>
    
            checkboxGroupOptionToggle() { }
            checkboxGroupOptionLabel() { }
            checkboxGroupOptionDescription() { } // use multiple times
        }
    // }
}
```

### checkboxGroup

Parameters: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ                            | Description                                                          |
|----------------|--------------------------------|----------------------------------------------------------------------|
| `value`        | `DatabindingProperty<List<T>>` | Mandatory (two-way) data-binding for any number of selected options. |


### checkboxGroupLabel

Available in the scope of: `checkboxGroup`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`


### checkboxGroupValidationMessages

Available in the scope of: `checkboxGroup`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ                                      | Description                                                           |
|----------------|------------------------------------------|-----------------------------------------------------------------------|
| `msgs`         | `Flow<List<ComponentValidationMessage>>` | provides a data stream with a list of ``ComponentValidationMessage``s |


### checkboxGroupOption

Available in the scope of: `checkboxGroup`

Parameters:
- `option: T`: Mandatory instance of an option that this block should handle.
- `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ             | Description                                                                                                                         |
|----------------|-----------------|-------------------------------------------------------------------------------------------------------------------------------------|
| `selected`     | `Flow<Boolean>` | This data stream provides the selection state of its managed option: `true` the option is part of the selection, `false` elsewhere. |


### checkboxGroupOptionToggle

Available in the scope of: `checkboxGroupOption`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`


### checkboxGroupOptionLabel

Available in the scope of: `checkboxGroupOption`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`


### checkboxGroupOptionDescription

Available in the scope of: `checkboxGroupOption`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `span`