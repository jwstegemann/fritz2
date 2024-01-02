---
title: Tooltip
description: "A tooltip is a small floating container that is displayed next to the element it describes whenever it gets
hovered using a pointer device."
layout: layouts/docs.njk
permalink: /headless/tooltip/
eleventyNavigation:
    key: tooltip
    title: Tooltip
    parent: headless
    order: 120
teaser: true
demoHash: tooltip
demoHeight: 18rem
---

## Basic Example

Adding a tooltip to an existing `Tag` (a standard HTML-tag or one built by a headless component) is straight forward.
Just call the `tooltip`-factory function on the `Tag` you want to describe:

````kotlin
button {
    + "some button"
}.tooltip {
    + "some description"
}
````

Of course you can style your tooltip by adding css-classes, add an id and specify the `Tag` that is rendered, like you
are used to by other headless components, in case you don't want a `HTMLDivElement`.

::: warning
**Beware:** The `tooltip`-function should be called, when the `Tag` it is called on has already been added to it's
parent in the DOM. Therefore, call it on the result of the factory function used to create the `Tag` you want to
describe - either directly or using a scope-method like `apply`.
:::

::: info
Do not forget to include a `portalRoot()`-call at the end of your initial `RenderContext` as explained
[here](/headless/#portalling)!
:::

## Transitions

Showing and hiding the tooltip can easily be animated with the help of `transition`:

```kotlin
button {
    + "some button"
}.tooltip {
    +"some description"

    transition(
        opened,
        enter = "transition duration-100 ease-out",
        enterStart = "opacity-0",
        enterEnd = "opacity-100",
        leave = "transition duration-100 ease-in",
        leaveStart = "opacity-100",
        leaveEnde = "opacity-0"
    )
}
```

## Positioning

A `tooltip` is a [`PopUpPanel`](#floating-content---popuppanel) and therefore provides in its scope some
configuration options, e.g. to control the position or distance of the tooltip box from the  reference element:

```kotlin
button {
    + "some button"
}.tooltip {
    + "some description"

    placement = PlacementValues.top
    addMiddleware(offset(20))
}
```

## Arrow

An arrow pointing to the `Tag` the `tooltip` is called on can easily be added:

```kotlin
button {
    + "some button"
}.tooltip {
    +"some description"
    arrow()
}
```

By default, the arrow is 8 pixels wide and inherits the background-color from the `tooltip` but its size and offset can
be adapted:

```kotlin
arrow("h-3 w-3", 8)
```


## Mouse Interaction

The `tooltip` is show whenever the mouse enters the `Tag` it is called on and is hidden again, when the mouse leaves
the `Tag`.

## API

### Summary / Sketch

```kotlin
Tag<HTMLElement>.tooltip() {
    // inherited by `PopUpPanel`
    var placement: Placement
    var strategy: Strategy
    var flip: Boolean
    var skidding: Int
    var distance: int

    arrow()
}
```


### tooltip

Parameters: `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `div`

| Scope property  | Typ         | Description                                                                                                                                                                                                                 |
|-------------|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `placement` | `Placement` | Defines the position of the building block, e.g. `Placement.top`, `Placement.bottomRight`, etc. Default is `Placement.auto`. The presumably best position is determined automatically based on the available visible space. |
| `strategy`  | `Strategy`  | Determines whether the block should be positioned `absolute` (default) or `fixed`.                                                                                                                                          |
| `flip`      | `Boolean`   | If the block comes too close to the edge of the visible area, the position automatically changes to the other side if more space is available there.                                                                        |
| `skidding`  | `Int`       | Defines the shifting of the tooltip along the reference element in pixels. The default value is 0.                                                                                                                          |
| `distance`  | `Int`       | Defines the distance of the distance from the reference element in pixels. The default value is 10.                                                                                                                         |
