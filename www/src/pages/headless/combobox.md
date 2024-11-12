---
title: Combobox
description: "A robust component for selecting a single item from a list of options with suggestions as you type, 
keyboard navigation, and more."
layout: layouts/docs.njk
permalink: /headless/combobox/
eleventyNavigation:
  key: combobox
  title: Combobox
  parent: headless
  order: 11
teaser: true
demoHash: combobox
demoHeight: 25rem
---

## Basic Example

A combo box is created by the factory function `fun <T> combobox()`. `T` is the data type of the choices to be offered,
such as a country.

When the input created via `comboboxInput` is focused, a dropdown with suggestions is shown and updated as you
type. When focused, the input shows the current input. Otherwise, the currently selected item is displayed.

It is mandatory to specify a data stream or a store of type `T?` as data binding via the `value` property. The component
supports two-way data binding, i.e. it reflects a selected element from the outside by a `Flow<T>`
but also emits the updated selection to the outside via a `Handler`.  

A combo box may not hold a value (e.g. if initially there is  no selection or the implementation lets the user
un-select his choice). Thus, the type parameter of the data-binding is nullable. It is possible to specify a
placeholder text via the vanilla `placeholder` attribute exposed by the [`comboboxInput`](#comboboxinput)'s 
input element.

Within the selection list the user can navigate using the keyboard. An item is selected via [[Enter]], [[Space]] or a 
mouse click. If the combo box input loses focus or the user clicks outside the selection list, the dropdown is hidden.

As typical use cases may offer thousands of items to choose from, the component reduces and filters those in order to
support the visual recognition of a user down to a feasible size, which can be configured via `maximumDisplayedItems`.

As a result, the combo box constantly evaluates which items to show based on the user's query. The results are emitted
by the
`results` Flow available in the scope of the items brick.  
The intended pattern is to iterate over the results and re-render all of them when the results change.

The selection dropdown is populated via the `comboboxItems` brick. Within the scope of this function, individual items
can be rendered via the `comboboxItem` brick.

::: warning
__Beware:__ Due to the inner workings of the combobox, rendering items via `renderEach` _does not work_ and results in
undefined behavior!

Instead, apply an ordinary `render` on the `results`-flow and create the items via a `forEach`-call on the provided
`List<T>`.
:::

```kotlin
// some domain type for this example, a collection to choose from, and an external store
val countries = listOf("Germany", "Netherlands", "France")
val country = storeOf<String?>(null)

combobox<String> {
    items(countries)

    // set up (two-way) data binding
    value(country)

    comboboxInput {}

    comboboxItems("bg-white") {
        // using a combination of 'render' and 'forEach' is the intended
        // way of populating the dropdown
        results.render { result ->
            result.items.forEach { item ->
                comboboxItem(item) {
                    span {
                        className(active.map { if (it) "underline" else "" })
                        +item.value
                    }
                }
            }
        }
    }
}
```

::: info
Do not forget to include a `portalRoot()`-call at the end of your initial `RenderContext` as explained
[here](/headless/#portalling)!
:::

## Styling the active or selected Item

A `comboboxItem` has two special states:

- When the mouse is hovering above it or the user navigates to the item via keyboard, it is considered `active`.
- A click or [[Enter]] selects the active item. It is then `selected`.

In the scope of a `comboboxItem`, `active` and `selected` are available as a `Flow<Boolean>` to apply styling or to
render or hide certain elements based on the state:

```kotlin
comboboxItem(item) {
    //change fore- und background-color is item is active 
    className(active.map {
        if (it) "text-amber-900 bg-amber-100" else "text-gray-300"
    })

    span { +item.value }

    //render checked-icon only if item is selected
    selected.render {
        if (it) {
            svg { content(HeroIcons.check) }
        }
    }
}
```

## Highlight matching items

When rendering items based on the user's query, it is often a requirement to highlight the passages of each
item's String representation that match the given query.
For this purpose, the current `query` is part of the `results` Flow.

```kotlin
comboboxItems {
    results.render { (query, items, _) ->
        items.forEach { item ->
            comboboxItem(item) {
                // separate the item's String representation into matching and
                // non-matching segments
                val segments = item.value.split(
                    Regex(
                        "(?<=($highlight))|(?=($highlight))",
                        RegexOption.IGNORE_CASE
                    )
                )

                for (s in segments) {
                    span(
                        joinClasses(
                            "underline".takeIf {
                                s.contentEquals(query, ignoreCase = true)
                            }
                        )
                    ) { +s }
                }
            }
        }
    }
}
```

## Truncated result list

The `results` Flow only displays a fixed number of items at a time, configured via the `maximumDisplayedItems` property.
If more items match the given query than configured to be displayed, the `truncated` flag available in the `results`
Flow is set to `true` so an appropriate hint can be displayed.

```kotlin
comboboxItems {
    results.render { (_, items, truncated) ->
        items.forEach { item ->
            comboboxItem(item) {
                span { +item.value }
            }
        }
        if (truncated) {
            span {
                +"Refine your query for more results"
            }
        }
    }
}
```

## Adding a Label

The combo box can be supplemented with a label using `comboboxLabel`, e.g. for use in forms or for a screen reader:

```kotlin
combobox<String> {
    comboboxLabel {
        span { +"Select a country" }
    }
}
```

## Open State

Combobox is an [`OpenClose` component](#closable-content---openclose). There are different `Flow`s and `Handler`s
like `opened` available in its scope to control the open state of the combo box based on state changes.

Most of the time, the open state managed by the component itself should be enough for all use cases, though.

### Automatic Opening Behavior

The combo box's dropdown can be configured to automatically open based on different events:

| Configuration method | Description                                                                      |
|----------------------|----------------------------------------------------------------------------------|
| `lazily()`           | The dropdown is opened when the user starts typing.                              |
| `eagerly()`          | The dropdown is opened as soon as the user focuses the combo box's input element |

The configuration is done via the `openDropdown` hook available in the configuration scope.

Both strategies are good choices and mostly depend on subjective preferences. By __default__, the dropdown opens 
itself _eagerly_.

Example:

```kotlin
combobox {
    openDropdown.lazily()
    // OR
    openDropdown.eagerly()
}
```

## Auto-Selecting Items

By default, the user needs to manually select an item for it to be accepted as the combo box's value.
It can, however, also be configured to automatically select matching items for a query via the `selectionStrategy`
property.

The following modes are offered:

| Configuration method | Description                                 |
|----------------------|---------------------------------------------|
| `autoSelectMatch()`  | Matching items are automatically selected   |
| `manual()`           | Matching items need to be selected manually |

```kotlin
combobox {
    selectionStrategy.autoSelectMatch()
    // OR
    selectionStrategy.manual()
}
```

## Transitions

Showing and hiding the selection list can be easily animated with the help of `transition`:

```kotlin
comboboxItems {
    transition(
        opened,
        enter = "transition duration-100 ease-out",
        enterStart = "opacity-0 scale-95",
        enterEnd = "opacity-100 scale-100",
        leave = "transition duration-100 ease-in",
        leaveStart = "opacity-100 scale-100",
        leaveEnde = "opacity-0 scale-95"
    )
}
```

## Positioning

`comboboxItems` is a [`PopUpPanel`](#floating-content---popuppanel) and therefore provides a set of
configuration options to control the position or distance of the list box from the `comboboxButton`
as a reference element:

```kotlin
comboboxItems {
    placement = PlacementValues.top
    addMiddleware(offset(20))
}
```

::: info
In practice, the [`comboboxInput`](#comboboxinput) element might be wrapped in additional elements that are considered
to be a part
of it. Since by default the dropdown is positioned relative to the [`comboboxInput`](#comboboxinput) element, the
dropdown may
appear out of place. In those cases, the outermost wrapping element can be created via the
[`comboboxPanelReference`](#comboboxpanelreference) brick to fix the positioning.

In order to see an example of the `comboboxPanelReference` in action, have a look at the source code of the combobox
demo
within the `headless-demo` module. You can observe the consequences of removing and re-adding it there.
:::

The anchor element of the dropdown is determined based on a number of conditions:

1) If a [`comboboxInput`](#comboboxinput) is present, the panel is placed relative to it.
2) If a [`comboboxPanelReference`](#comboboxpanelreference) is present, it is _used as the reference instead_.
3) Otherwise, the dropdown references a minimal fallback input element.

## Validation

The data-binding allows the Combobox component to process the validation messages and provide its own building
block `lcomboboxValidationMessages` that can beused to render the messages if present. The messages are exposed within
its scope as a `msgs` Flow.

```kotlin
combobox<String> {
    value(country)

    comboboxValidationMessages(tag = RenderContext::ul) {
        msgs.renderEach {
            li { +it.message }
        }
    }
}
```

## Focus Management

When the `comboboxInput` element is focused, the selection list (dropdown) is visible and can be used to select
items. The input remains focused until a selection is made, even if the user navigates to an item via keyboard.  
When an item is `active` and [[Enter]] is pressed, it will be selected and the dropdown is closed.  
When the input elements loses focus, the dropdown will be closed as well and the displayed value is reset to match the
last selection.

## Mouse Interaction

A click on the `comboboxInput` focuses the element and opens the selection dropdown as described above. A click outside
the opened selection list closes it. If the mouse is moved over an item in the open list, it is marked as active.
Clicking on an item when the list is open selects it and closes the list.

## Keyboard Interaction

| Command                                          | Description                    |
|:-------------------------------------------------|:-------------------------------|
| [[⬆]] [[⬇]] when the combobox is open            | Activates previous / next item |
| [[Home]] [[End]] when the combobox is open       | Activates first / last item    |
| [[Esc]] when the combobox is open                | Closes the combobox            |
| [[Enter]] [[Space]] when the combobox is open    | Selects the active item        |

## Performance

The combo box component uses a specific internal pipeline to filter and display the selection items:

1) Retrieve the user's input
2) _Debounce_
3) Find matches
4) _Debounce_
5) Render matches

The debouncing is in place because the above workflow consists of two relatively expensive operations: _filtering_ and
_rendering_.

While typing, the query may be manipulated multiple times per second. In order for the filter function to run as few
times as possible, the flow of inputs is debounced.

The same goes for the actual rendering: It is by far the most expensive operation in the workflow so it is debounced to
not be executed multiple times in a row.

Adding to the above, the number of displayed items in the dropdown also has an impact on the rendering performance.

::: info
Most of the time, the default behavior should be working fine. There might be cases, however, where the implementing
component has a consistently high/low amount of items or other niche scenarios. In those cases, the debouncing and
other performance-related parameters can be configured via the DSL.
:::

| Property                | Type   | Default | Description                                                      |
|:------------------------|:-------|:--------|:-----------------------------------------------------------------|
| `maximumDisplayedItems` | `Int`  | `20`    | Maximum number of items to display                               |
| `inputDebounceMillis`   | `Long` | `50L`   | Time to wait and debounce before the filter function is invoked  |
| `renderDebounceMillis`  | `Long` | `50L`   | Time to wait and debounce before the filter results are rendered |

See [`combobox`](#combobox) for more api information.

## API

### Summary / Sketch

```kotlin
combobox<T> {
    val items: ItemsHook()
    // params: List<T> / Flow<List<T>>

    var itemFormat: (T) -> String

    val value: DatabindingProperty<T?>

    var filterBy: FilterFunctionProperty
    // params: (Sequence<T>, String) -> Sequence<T> / T.() -> String

    val openDropdown: DropdownOpeningHook
    // methods: lazily() / eagerly()

    val selectionStrategy: SelectionStrategyProperty
    // methods: autoSelectMatch() / manual()

    var maximumDisplayedItems: Int = 20
    var inputDebounceMillis: Long = 50L
    var renderDebounceMillis: Long = 50L

    comboboxInput() { }
    comboboxPanelReference() {
        // this brick is often used with a nested
        // comboboxInput() { }
    }
    comboboxLabel() { }
    comboboxItems() {
        // inherited by `PopUpPanel`
        var placement: Placement
        var strategy: Strategy
        var flip: Boolean
        var skidding: Int
        var distance: int

        val results: Flow<QueryResult.ItemList<T>>

        // results.render {
            // for each QueryResult.ItemList<T>.Item<T> {
                comboboxItem(Item<T>) { }
            // }
        // }
    }
    comboboxValidationMessages() {
        val msgs: Flow<List<ComponentValidationMessage>>
    }
}
```

### combobox

Parameters: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property          | Type                           | Description                                                                                                                                                                                                                            |
|:------------------------|:-------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `items`                 | `Combobox<T>.ItemsHook`        | Mandatory `List<T>` or `Flow<List<T>>` of items to offer (invoke)                                                                                                                                                                      |
| `itemFormat`            | `(T) -> String`                | Recommended formatting function used to display an item's String representation in the `comboboxInput`.                                                                                                                                |
| `value`                 | `DatabindingProperty<T>`       | Mandatory (tow-way) data binding for a selected item.                                                                                                                                                                                  |
| `filterBy`              | `FilterFunctionProperty`       | Recommended filter function to find matching items based on the query. Accepts either a String getter (`T.() -> String`) or a fully custom filter function (`(Sequence<T>, String) -> Sequence<T>`). _Mandatory for non-String items!_ |
| `openDropdown`          | `DropdownOpeningHook`          | Optional strategy to configure when the combo box's dropdown should open (lazily or eagerly)                                                                                                                                           |                     
| `selectionStrategy`     | `SelectionStrategyProperty`    | Optional strategy to configure whether exact matches are automatically selected. Invoke either `autoSelectMatch()` or `manual()`                                                                                                       |
| `maximumDisplayedItems` | `Int`                          | Maxmimum number of items to display in the dropdown. Defaults to 20                                                                                                                                                                    |
| `inputDebounceMillis`   | `Long`                         | Time to wait and debounce before the filter function is invoked. Defaults to 50 milliseconds.                                                                                                                                          |
| `renderDebounceMillis`  | `Long`                         | Time to wait and debounce before the filter results are rendered. Defaults to 50 milliseconds.                                                                                                                                         |
| `openState`             | `DatabindingProperty<Boolean>` | Optional (two-way) data binding for opening and closing.                                                                                                                                                                               |
| `opened`                | `Flow<Boolean>`                | Data stream that provides Boolean values related to the "open" state. Quite useless within a list box, as it is always `true`                                                                                                          |
| `close`                 | `SimpleHandler<Unit>`          | Handler to close the list box from inside. Should not be used, as the component handles this internally.                                                                                                                               |
| `open`                  | `SimpleHandler<Unit>`          | handler to open; does not make sense to use within a list box!                                                                                                                                                                         |
| `toggle`                | `SimpleHandler<Unit>`          | handler for switching between open and closed; does not make sense to use within a list box.                                                                                                                                           |

### comboboxPanelReference

Available in the scope of: `combobox`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

### comboboxInput

Available in the scope of: `combobox`, `comboboxPanelReference`

Parameters: `classes`, `scope`, `initialize`

### comboboxLabel

Available in the scope of: `combobox`, `comboboxPanelReference`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `label`

### comboboxValidationMessages

Available in the scope of: `combobox`, `comboboxPanelReference`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ                                      | Description                                                           |
|:---------------|:-----------------------------------------|:----------------------------------------------------------------------|
| `msgs`         | `Flow<List<ComponentValidationMessage>>` | provides a data stream with a list of ``ComponentValidationMessage``s |

### comboboxItems

Available in the scope of: `combobox`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ                             | Description                                                               |
|:---------------|:--------------------------------|:--------------------------------------------------------------------------|
| `results`      | `Flow<QueryResult.ItemList<T>>` | Emits the current list of items to be displayed in the selection dropdown |

### comboboxItem

Available in the scope of: `comboboxItems`

Parameters: `item`, `classes`, `scope`, `tag`, `initialize`

Default-Tag: `button`

| Scope property | Typ             | Description                                                                                                                                  |
|:---------------|:----------------|:---------------------------------------------------------------------------------------------------------------------------------------------|
| `selected`     | `Flow<Boolean>` | This data stream provides the selection state of the managed option: `true` the option is selected, `false` if not.                          |
| `active`       | `Flow<Boolean>` | This data stream indicates whether an item has focus: `true` the option has focus, `false` if not. Only one option can have focus at a time. |
