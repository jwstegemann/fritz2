---
title: Data Collection
layout: layouts/docs.njk
permalink: /headless/collection/
eleventyNavigation:
    key: dataCollection
    title: Data Collection
    parent: headless
    order: 180
demoHash: collection
teaser: "A headless component to render collections of data, i.e. a data table, complex lists that support sorting, filtering, selection of items and keyboard navigation."
---

## Basic Example

A data collection is created by the factory function `fun <T> dataCollection()`. `T` is the data type of the items of the collection, such as a domain data type like persons.

It is mandatory to specify a data stream of type `List<T>` as data source using the `data` property. Additionally, you can provide an `IdProvider` to tell `DataCollection` how to get a unique id for each item in the list.

Since we want to build a simple data table in our example, we next create the `table` and it's header.

Next we use `dataCollectionItems` to create the `tbody`. `dataCollectionItems` offers `items` in it's context. This is a `Flow<List<T>>` that respects the current sorting and filtering set for your collection. So we use this it iterate over and render the rows of the table. If you told the `DataCollection` to use an `IdProvider` above, it is a good idea to use the same `IdProvider` here in `renderEach`.

```kotlin
val persons = storeOf(listOf(Person( /*... */ )))

dataCollection<Person> {
    data(persons.data, Person::id)

    table {
        thead {
            tr {
                td{ +"Name" }
                td{ +"EMail" }
                td{ +"Birthdate" }
            }
        }
        dataCollectionItems( tag = RenderContext::tbody) {
            items.renderEach(Person::id, into = this) { item ->
                dataCollectionItem(item, tag = RenderContext::tr) {
                    td { +item.fullName }
                    td { +item.email }
                    td { +item.birthday }
                }
            }
        }
    }
}
```

## Sorting

In order to sort the items of your data collection `dataCollectionItems` offers a `Handler` called `sortBy`. You can use this to provide a `SortOrder<T>` that will be used to sort your collection. A `SortOrder` consists of a `Sorting` definining one `Comparator<T>` each for ascending and descending sorting as well as the current `SortDirection` (`NONE`, `ASC` oder `DESC`). Since it is a `Handler` you bind it to some `Flow` offering the current `SortOrder` (from a `Menu` for example) or call it directly.

Since it is a common use case to switch between the different `SortingDirections`, `dataCollectionItems` offers another `Handler` called `toggleSorting`. For a given `Sorting` this one switches from direction `NONE` to `ASC` and on the `DESC` every time a new value appears on the `Flow`.

To make it even more convenient the data collection also offers a specialized brick to build an element to toggle and display your current sorting:

```kotlin
thead {
    tr {
        td{
            div {
                p { + "Name" }
                dataCollectionSortButton(compareBy(Person::fullName), compareByDescending(Person::fullName)) {
                    direction.render {
                        when (it) { 
                            SortDirection.NONE -> /* show Icon */
                            SortDirection.ASC -> /* show Icon */
                            SortDirection.DESC -> /* show Icon */
                        }
                    }
                }
            }
        }
    }
}
```

## Filtering

A data collection supports filtering the items by offering a `Handler<(List<T) -> List<T>>` called `filterBy`. Whenever this `Handler` is provided with a new filter function, it is applied to the collections data. So you can easily provide buttons for predefined filtering for example:

```kotlin
val filterForLongNames = button { +"just long names" }.clicks.map {
    { list: List<Person> -> list.filter { it.fullName.length > 20 } }
}

val filterForOldPeople = button { +"just old people" }.clicks.map {
    { list: List<Person> -> list.filter { it.birthday.startsWith("19") } }
}

dataCollection<Person> {
    // ...

    dataCollectionItems {
        merge(filterForLongNames, filterForOldPeople) handledBy filterBy
    }
}
```

A common use-case is filtering your items for a certain text. The data collection provides a specialized `Handler<String>` called `filterByText()` for this purpose. By default, the `toString`-method of your items is used to produce a `String` that is checked to contain (case-insensitive) the filter-text provided to the `Handeler`. You can provide a lambda to create a different `String` representation for an item to be searched:

```kotlin
    val filterStore = storeOf("")
    inputField {
        value(filterStore)
        inputTextfield { placeholder("filter...") }
    }

    dataCollection<Person> {
        // ...
        filterStore.data handledBy filterByText { "${it.fullName} ${it.email}"}
        // ...
    }
```

## Active Item

You can navigate your data-collection by mouse or keyboard (when it is focused). Whenever you move your pointer over an item or navigate to a certain item by keyboard, this item is activated. The `active` state is available as a `Flow<Boolean>` in the scope of `dataCollectionItem` to be used for styling, etc.:

```kotlin
dataCollectionItem(item, tag = RenderContext::tr) {
    className(selected.map {
        if (it) "bg-indigo-50" else "odd:bg-white even:bg-gray-50"
    })
 
    // ...
}
```

If your data collection is (or is embedded in) a scrollable container, you might want to scroll to the active item when it is not visible when it gets activated. You can enable this by calling `scrollIntoView()`:

```kotlin
dataCollectionItems {
    // ...
    scrollIntoView(vertical = ScrollPosition.center)
    // ...
}
```

You can fine tune scroll behavior (auto or smooth), mode (only-if-needed, always) and the desired horizontal and vertical position of the active item (start, center, end or nearest).


## Selection

`DataCollection` supports selecting single or multiple items. To enable selection for your data collection you have to provide a suitable two-way-data-binding:

```kotlin
dataCollection<Person> {
    data(storedPersons.data, Person::id)

    // for single-selection
    selection.single(selectionStore) // or provide id, data-flow, update-handler, etc. separately
    
    // for multi-selection
    selection.multi(selectionStore)
}
```

## Styling selected Items

The `selected` state of an item is available as a `Flow<Boolean>` in the scope of `dataCollectionItem` to be used for styling, etc.:

```kotlin
dataCollectionItem(item, tag = RenderContext::tr) {
    className(selected.combine(active) { sel, act ->
        if (sel) {
            if (act) "bg-indigo-200" else "bg-indigo-100"
        } else {
            if (act) "bg-indigo-50" else "odd:bg-white even:bg-gray-50"
        }
    })
 
    // ...
}
```

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