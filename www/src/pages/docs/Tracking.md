---
layout: layouts/docsWithContentNav.njk
title: Tracking
permalink: /docs/tracking/
eleventyNavigation:
    key: tracking
    parent: documentation
    title: Tracking
    order: 74
---

## Track Processing State of Stores

When one of your `Handler`s contains long running actions (like server-calls, etc.) you might want to keep the user 
informed about that something is going on.

Using fritz2 you can use the `tracker`-service to implement this:

```kotlin
val store = object : RootStore<String>("") {
    val tracking = tracker()

    val save = handle { model ->
        tracking.track("myTransaction") {
            delay(1500) // do something that takes a while
            "$model."
        }
    }
}

render {
    button("btn") {
        className(store.tracking.data.map {
            if(it) "spinner" else ""
        })
        +"save"
        clicks handledBy store.save
    }
}
```
The service provides you with a `Flow` representing the description of the currently running transaction or `null`.

Filter the `Flow` using the meta-data you chose when calling `track(meta-data)` if you want to react to only certain transactions.

Of course, you can also use the meta-data to show to the user what is currently running (in a status-bar for example). 

Our [repositories example](https://examples.fritz2.dev/repositories/build/distributions/index.html) uses tracking, to show you a spinning wheel when you click on the save button.
