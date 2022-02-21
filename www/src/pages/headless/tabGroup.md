---
title: TabGroup
layout: layouts/headlessWithContentNav.njk
permalink: /headless/tabgroup/
eleventyNavigation:
    key: tabgroup
    title: TabGroup
    parent: headless
    order: 100
demoHash: tabGroup
teaser: "A TabGroup allows content to be switched across a horizontal or vertical list of tabs."
---

## Basic Example

TabGroups are created using the `tabGroup` factory function. It is divided into two areas: In the
`tabList` shows the available `tab`s, `tabPanel` the content of the currently active tab.

By clicking on a tab or by selecting via the keyboard, the current tab can be selected and the content of the panel will
be switched accordingly.

The TabGroup is completely agnostic about a tab's type as well as its content. That's why no information about the
available tabs are needed. The tabs themselves are internally managed via the index when they get added to the `tabList`
. Structure and content of tabs and panels can be designed completely freely.

::: warning
**Beware:** Tabs cannot be removed from the TabGroup. Once a tab is added to the group by `tab`, it will remain inside
forever. So never use some reactive pattern for populating the TabGroup as some `Flow<List<T>>` combined with some 
call to `renderEach` or alike!

If the tabs change, you must re-render the whole component.
:::

A [data binding](#set-and-query-the-active-tab-from-outside) is purely optional and does not have to be specified.

```kotlin
// Some domain type and a collection of data to be displayed inside a tab-group 
data class Posting(val id: Int, val title: String, val date: String, val commentCount: Int, val shareCount: Int)

val categories = mapOf(
    "Recent" to listOf(
        Posting(1, "Does drinking coffee make you smarter?", "5h ago", 5, 2),
        Posting(2, "So you've bought coffee... now what?", "2h ago", 3, 2)
    ), // ...
)

tabGroup {
    tabList {
        // typical pattern to use a loop to create the tabList
        categories.keys.forEach { category ->
            tab { +category }
        }
    }
    tabPanels {
        // for each tab there must be a corresponding panel
        categories.values.forEach { postings ->
            panel {
                ul {
                    postings.forEach { posting ->
                        li { +posting.title }
                    }
                }
            }
        }
    }
}
```

## Styling the active Tab

In order to distinguish the active tab from the rest in terms of style, within the scope of `tab` the boolean data
stream
`selected` is available.

This can be used in combination with `className` to apply different styles to a tab or even show and hide entire
elements (e.g. an icon for the selected tab).

```kotlin
tabGroup {
    tabList {
        categories.keys.forEach { category ->
            tab {                 
                // use `selected` flow in order to apply separate styling to the tabs 
                className(selected.map { sel ->
                    if (sel == index) "bg-white shadow"
                    else "text-blue-100 hover:bg-white/[0.12] hover:text-white"
                })
                
                +category 
            }
        }
    }
    tabPanels {
        // omitted
    }
}
```

## Set and Query the active Tab from outside

As already described at the beginning, the tabs are only managed via their index (`0` based!).

For this reason, an `Int` based data binding `value` in the scope of the `tabGroup` factory function can optionally be
specified. This can be used to determine the initially active tab as well as to query the currently selected tab by some
provided handler.

If the data binding is not specified, the first active tab is always selected initially.

```kotlin
val currentIndex = storeOf(1) // preselect *second* tab (0-based as all collections in Kotlin)

currentIndex.data handledBy {
    console.log("Current Index is: $it")
}

tabGroup {
    
    // apply two-way-data-binding via index based store
    value(currentIndex)
    
    tabList {
        categories.keys.forEach { category ->
            tab { +category }
        }
    }
    tabPanels {
        // omitted
    }
}
```

## Deactivate Tabs

Tabs can be activated and deactivated dynamically. Disabled tabs can neither be activated by using mouse or keyboard,
nor are they selected as the initial tab.

By default, each tab is always active at first.

To activate or deactivate a tab, the Boolean handler `disable` is available in the scope of `tab`.

The current status can be queried via the boolean data stream `disabled`. The latter is primary relevant for styling
reasons, because a deactivated tab should visually stand out from active ones.

```kotlin
tabGroup {
    tabList {
        categories.keys.forEach { category ->
            tab {
                // reduce opacity for disabled tabs
                className(disabled.map { sel ->
                    if (sel == index) "opacity-50" else ""
                })
                
                +category
                
                // simply disable tab "Trending" forever
                if(category == "trending") disable(true)
                
                // toggle disable state of tab "Popular" every 5 seconds
                if(category == "Popular") {
                    generateSequence { listOf(true, false) }.flatten().asFlow()
                        .onEach { delay(5000) } handledBy disable
                }
            }
        }
    }
    tabPanels {
        // omitted
    }
}
```

## Vertical TabGroup

A TabGroup can be displayed both horizontally (default) and vertically. This distinction in itself is only dependent on
the optical design, but changes the operation via keyboard and must therefore be made known explicitly to the component.

With a horizontal TabGroup you can use the arrow keys [[←]] and [[→]] to switch between the tabs, use [[↑]] and [[↓]]
for vertical TabGroups.

For this purpose there is the property `orientation`, which can accept the two enum values `Horizontal` or `Vertical`.

```kotlin
tabGroup {
    
    // will be evaluated only once at the initial rendering, so it is not reactive!
    orientation = Orientation.Vertical
    
    tabList {
        // omitted        
    }
    tabPanels {
        // omitted
    }
}
```

## Mouse Interaction

Clicking on an element created with ``tab`` activates the tab and renders the associated panel as content,
unless the tab is disabled.

## Keyboard Interaction

| Command                                       | Description                                        |
|-----------------------------------------------|----------------------------------------------------|
| [[←]] [[→]]                                   | Cyclically selects the previous / next active tab. |
| [[↑]] [[↓]] when `orientation` is `Vertical`  | Cyclically selects the previous / next active tab. |
| [[Home]] [[PageUp]]                           | Selects the first active Tab.                      |
| [[End]] [[PageDown]]                          | Selects the last active Tab.                       |


## API

### Summary / Sketch
```kotlin
tabGroup() {
    val value: DatabindingProperty<Int> // optional
    val selected: Flow<Int>
    var orientation: Orientation

    tabList() {
        // for each tab {
            tab() {
                val index: Int
                val disabled: Flow<Int>
                val disable: SimpleHandler<Int>
            }
        // }
    }

    tabPanels() {
        // for each tab {
            panel() { }
        // }        
    }
}
```

### tabGroup

Parameters: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property    | Typ                        | Description                                                                              |
|---------------|----------------------------|------------------------------------------------------------------------------------------|
| `value`       | `DatabindingProperty<Int>` | Optionally (two-way) data-binding for setting or querying the current active tab's index |
| `selected`    | `Flow<Int>`                | Data stream of the current tab index.                                                    |
| `orientation` | `Orientation`              | Field for setting the orientation. Default is `Horizontal`                               |


### tabList

Available in the scope of: `tabGroup`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`


### tab

Available in the scope of: `tabList`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property | Typ                      | Description                                                    |
|----------------|--------------------------|----------------------------------------------------------------|
| `index`        | `Int`                    | The index of the tab in the group.                             |
| `disabled`     | `Flow<Boolean>`          | Stream of data indicating whether a tab is active or inactive. |
| `disable`      | `SimpleHandler<Boolean>` | Handler for setting the inactive state.                        |


### tabPanels

Available in the scope of: `tabGroup`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`


### panel

Available in the scope of: `tabPanels`

Parameters: `classes`, `scope`, `tag`, `initialize`

Default-Tag: `div`