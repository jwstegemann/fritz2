---
title: Toast
description: "A toast is a notification-like component that can be shown in arbitrary locations on the screen for a
limited amount of time."
layout: layouts/docs.njk
permalink: /headless/toast/
eleventyNavigation:
    key: toast
    title: Toast
    parent: headless
    order: 115
teaser: true
demoHash: toast
demoHeight: 28rem
---

## Basic Example

In order to create a ``toast`` you first need to set up a container, that will act as a host for the toasts.
You can create such a container via the `toastContainer` factory with a *name* as parameter. The name defines the
identity of the container, with acts as reference for the toasts. You also have to style the container in order to
define the screen position in which the toasts should appear.

```kotlin
toastContainer(
    "default", // name
    "absolute top-5 right-5 flex flex-col gap-2 items-start"
)
```

Toasts are built using the `toast` factory function. It is mandatory to provide the name of the container
in which the toast should be added. Optionally you can pass a `duration` that defines the time of appearance in
milliseconds.

```kotlin
toast("default") {
    +"Toast"
}
```

::: info
Please note that the functionality of the headless toast component is intentionally limited.
Consider using another component (e.g. [data collection](/headless/datacollection)) for more complex uses cases like
filtering or sorting the toast list, or displaying more complex data.
:::

## Multiple Containers

You can create as many containers as you need. Just call the `toastContainer` factory for each container using
unique names.

When creating a toast, just refer to the appropriate container name to place the toast.

One scenario may be a notification system that always renders notifications in one corner of the screen whereas another
one might have to support a number of different places.

```kotlin
toastContainer("default")
toastContainer("important")

toast("default") {
    +"Some basic information"
}

toast("important") {
    +"Some more important information"
}
```


## Automatic closing

The time in milliseconds before the toast closes itself can be overridden via the `duration` parameter of the factory.

By default every toast is closed after a specific time; if no duration is set a default value of `5000` milliseconds 
is used.

```kotlin
toast(
    "default",
    duration = 10000L
) {
    +"Toast"
}
```

The toast can be kept indefinitely (until manually closed) by setting the `duration` to `0`.

## Manual closing

Optionally, a toast can be dismissed before the configured duration is over by calling the `close` handler available in
the configuration context of the toast. A common use case would be a close button.

This feature is also important for toasts with `duration` set to `0`, which will be kept indefinitely.

```kotlin
toast("default") {
    +"Toast"

    button {
        icon(
            classes = "w-4 h-4 text-primary-900",
            content = HeroIcons.x
        )
        clicks handledBy close // call close handler
    }
}
```

## API

### Summary / Sketch

```kotlin
toastContainer(name, classes, id, scope, tag) // repeat for each desired location

toast(containerName, duration, classes, id, scope, tag) {
    val close: Handler<Unit>
}
```

### toastContainer

Parameters: `name`, `classes`, `id`, `scope`, `tag`

Default-Tag: `ul`

### toast

Parameters: `containerName`, `duration`, `classes`, `id`, `scope`, `tag`, `initialize`

Default-Tag: `li`

| Scope property | Typ             | Description                   |
|----------------|-----------------|-------------------------------|
| `close`        | `Handler<Unit>` | closes the toast when invoked |
