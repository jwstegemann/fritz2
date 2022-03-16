---
title: Data Collection
description: "A headless component to render collections of data, i.e. a data table, complex lists that support sorting,
filtering, selection of items and keyboard navigation."
layout: layouts/docs.njk
permalink: /headless/datacollection/
eleventyNavigation:
    key: dataCollection
    title: Data Collection
    parent: headless
    order: 15
teaser: true
demoHash: datacollection
demoHeight: 40rem
---

## Basic Example

A data collection is created by the factory function `fun <T> dataCollection()`. `T` is the data type of the items of
the collection, such as a domain data type like persons.

It is mandatory to specify a data stream of type `List<T>` as data source using the `data` property. Additionally, you
can provide an `IdProvider` to tell `DataCollection` how to get a unique id for each item in the list.

Since we want to build a simple data table in our example, we next create the `table` and it's header.

Next we use `dataCollectionItems` to create the `tbody`. `dataCollectionItems` offers `items` in its context. This is
a `Flow<List<T>>` that respects the current sorting and filtering set for your collection. So we use this to iterate
over and render the rows of the table. If you told the `DataCollection` to use an `IdProvider` above, it is a good idea
to use the same `IdProvider` here in `renderEach`.

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

In order to sort the items of your data collection `dataCollection` offers a `Handler` called `sortBy`. You can use
this to provide a `SortOrder<T>` that will be used to sort your collection. A `SortOrder` consists of a `Sorting`
defining one `Comparator<T>` each for ascending and descending sorting as well as the current `SortDirection` (`NONE`
, `ASC` oder `DESC`). Since it is a `Handler` you bind it to some `Flow` offering the current `SortOrder` (from a `Menu`
for example) or call it directly.

Since it is a common use case to switch between the different `SortingDirections`, `dataCollection` offers
another `Handler` called `toggleSorting`. For a given `Sorting` this one switches from direction `NONE` to `ASC` and on
the `DESC` every time a new value appears on the `Flow`.

To make it even more convenient the data collection also offers a specialized brick `dataCollectionSortButton` to build
an element to toggle and display your current sorting:

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

A data collection supports filtering the items by offering a `Handler<(List<T) -> List<T>>` called `filterBy`. Whenever
this `Handler` is provided with a new filter function, it is applied to the collections' data. So you can easily provide
buttons for predefined filtering for example:

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

A common use-case is filtering your items for a certain text. The data collection provides a
specialized `Handler<String>` called `filterByText()` for this purpose. By default, the `toString`-method of your items
is used to produce a `String` that is checked to contain (case-insensitive) the filter-text provided to the `Handeler`.
You can provide a lambda to create a different `String` representation for an item to be searched:

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

::: info
**Hint:** Be sure to add some *separator* character between a composed filter string like above. Without the `space`
between the name and the email address, it would be possible to match overlapping patterns. Take the name `kotlin`
and a mail address `nux@kernel.org`: The pattern `linux` would match without the space: `kotlinux@kernel.org`, which
is not what is expected!
:::

## Active Item

You can navigate your data-collection by mouse or keyboard (when it is focused). Whenever you move your pointer over an
item or navigate to a certain item by keyboard, this item is activated. The `active` state is available as
a `Flow<Boolean>` in the scope of `dataCollectionItem` to be used for styling, etc.:

```kotlin
dataCollectionItem(item, tag = RenderContext::tr) {
    className(active.map {
        if (it) "bg-indigo-50" else "odd:bg-white even:bg-gray-50"
    })
 
    // ...
}
```

If your data collection is (or is embedded in) a scrollable container, you might want to scroll to the active item when
it is not visible when it gets activated. You can enable this by calling `scrollIntoView()`:

```kotlin
dataCollectionItems {
    // ...
    scrollIntoView(vertical = ScrollPosition.center)
    // ...
}
```

You can fine tune scroll behavior (auto or smooth), mode (only-if-needed, always) and the desired horizontal and
vertical position of the active item (start, center, end or nearest).

## Selection

`DataCollection` supports selecting single or multiple items. To enable selection for your data collection you have to
provide a suitable two-way-data-binding:

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

The `selected` state of an item is available as a `Flow<Boolean>` in the scope of `dataCollectionItem` to be used for
styling, etc.:

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

## Mouse Interaction

Hovering with the mouse over an `dataCollectionItem` will make it *active*. The state can be accessed by the corresponding
`active` property of the `dataCollectionItem` that offers a `Flow<Boolean>` to react to.

A click on a `dataCollectionItem` will toggle the selection state, if any selection is configured by the `selection`
property of the `dataCollection`.

## Keyboard Interaction

| Command                                                                        | Description                                          |
|--------------------------------------------------------------------------------|------------------------------------------------------|
| [[Enter]] [[Space]] when `dataCollectionItem` is active and `selection` is set | Toggles the selection state of an item               |
| [[⬆]] [[⬇]] when some item is active                                           | Activates previous / next item                       |
| [[Home]] [[End]] when some item is active                                      | Activates first / last item                          |

::: info
If `scrollIntoView` on `dataCollectionItems` is configured, the navigating keyboard actions will also scroll the
content.
:::

## API

### Summary / Sketch
```kotlin
dataColection<T>() {
    val data: CollectionDataProperty<T>
    val sortBy: SimpleHandler<SortingOrder<T>?>
    val toggleSorting: SimpleHandler<Sorting<T>>
    val filterBy: SimpleHandler<((List<T>) -> List<T>)?>
    val selection: SelectionMode<T>
    
    fun filterByText(toString: (T) -> String) : SimpleHandler<String>
    
    // use multiple times
    dataCollectionSortButton(sort: Sorting<T>) {
        val direction: Flow<SortDirection>
    }

    dataCollectionItems() {
        val scrollIntoView: ScrollIntoViewProperty
        val items: Flow<List<T>>

        // items.renderEach T {
            dataCollectionItem(item: T) {
                val selected: Flow<Boolean>
                val active: Flow<Boolean>
            }
        // }
    }
}
```

### dataCollection

Parameters: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property  | Typ                                      | Description                                                                                                                         |
|-----------------|------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------|
| `data`          | `CollectionDataProperty<T>`              | Mandatory one-way data-binding for the items the collection should manage.                                                          |
| `sortBy`        | `SimpleHandler<SortingOrder<T>?>`        | Optional `Handler` that can implement any sorting logic based upon a `SortingOrder<T>` instance.                                    |
| `toggleSorting` | `SimpleHandler<Sorting<T>>`              | Optional `Handler` that implements a toggling sort logic, that is switching between the three directions `NONE`, `ASC` and `DESC`   |
| `filterBy`      | `SimpleHandler<((List<T>) -> List<T>)?>` | Optional `Handler` that can implement any logic to reduce the current `List<T>` items to the filtered `List<T>`.                    |
| `selection`     | `SelectionMode<T>`                       | Basically an optional intermediate property to offer `DataBinding` properties for either `single` or `mulit` selection modes.       |

Functions:

- `filterByText(toString: (T) -> String) : SimpleHandler<String>`: Factory to create a `Handler` to do a filtering based
  upon one item `T`.

### dataCollectionSortButton

Available in the scope of: `dataCollection`

Parameters:
- `sort: Sorting<T>`: Mandatory instance of the sorting configuration.
- (overloaded variant) `comparatorAscending: Comparator<T>, comparatorDescending: Comparator<T>`: Just provide
  the `Comparator`s so the sorting instance gets automatically derived from this.
- `classes`, `scope`, `tag`, `initialize`

Default-Tag: `button`

| Scope property | Typ                                  | Description                                         |
|----------------|--------------------------------------|-----------------------------------------------------|
| `direction`    | `Flow<SortDirection>`                | This property offers the current sorting direction. |

### dataCollectionItems

Available in the scope of: `dataCollection`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property   | Typ                              | Description                                                                                                                   |
|------------------|----------------------------------|-------------------------------------------------------------------------------------------------------------------------------|
| `scrollIntoView` | `ScrollIntoViewProperty`         | Optional property to configure the scrolling behaviour. If omitted there will be no automatic scrolling!                      |
| `items`          | `Flow<List<T>>`                  | Flow of the currently visible items (think of this as the result of filtering and sorting applied on the original data)       |


### dataCollectionItem

Available in the scope of: `dataCollectionItems`

Parameters:
- `item: T`: Mandatory instance of one `T` that represents the actual item
- `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ             | Description                                                                                                                                |
|----------------|-----------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `selected`     | `Flow<Boolean>` | This data stream provides the selection status of the managed item: `true` the item is selected, `false` if not.                           |
| `active`       | `Flow<Boolean>` | This data stream indicates whether an option has focus: `true` the item has focus, `false` if not. Only one item can have focus at a time. |
