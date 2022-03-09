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

A data collection supports filtering the items by offering a `Handler<(List<T) -> List<T>>` called `filterBy`. Whenever this `Handler` is provided with a new filter function, it is applied to the collections data. So you can easily provide buttons for predefined filterings for example:

```kotlin
val filterForLongNames = button { +"just long names" }.clicks.map { it.filter { it.fullName.length() > 20 } }
val filterForOldPeople = button { +"just old people" }.clicks.map { it.filter { it.birthdate.startsWith("19") } }

dataCollection<Person> {
    // ...
    
    dataCollectionItems {
        merge(filterForLongNames, filterForOldPeople) handledBy filterBy
    }
}

```



## Transitions

Showing and hiding the modal dialog can be easily animated with the help of `transition`:

```kotlin
modal {
    openClose(toggle)
    modalPanel {
        modalOverlay {
            // some nice fade in/out effect for the overlay
            transition(
                enter = "ease-out duration-300",
                enterStart = "opacity-0",
                enterEnd = "opacity-100",
                leave = "ease-in duration-200",
                leaveStart = "opacity-100",
                leaveEnd = "opacity-0"
            )            
        }
        div {
            // some nice fade in/out and scale in/out effect for the content
            transition(
                enter = "transition duration-100 ease-out",
                enterStart = "opacity-0 scale-95",
                enterEnd = "opacity-100 scale-100",
                leave = "transition duration-100 ease-in",
                leaveStart = "opacity-100 scale-100",
                leaveEnd = "opacity-0 scale-95"
            )
            
            p { +"I am some modal dialog! Press Cancel to exit."}
            button {
                type("button")
                +"Cancel"
                clicks handledBy close 
            }
        }
    }
}
```

## Mouse Interaction

By default, no mouse interaction is supported through the modal dialog. Typically, within the modal dialog there should
be some clickable element, which triggers the `close` handler and thus the dialog closes.

## Keyboard Interaction

| Command               | Description                                                                                                           |
|-----------------------|-----------------------------------------------------------------------------------------------------------------------|
| [[Tab]]               | Cycles through all focusable elements of an open modal window. There should always be at least one focusable element! |
| [[Shift]] + [[Tab]]   | Cycles backwards through all focusable elements of an open modal window.                                              |

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