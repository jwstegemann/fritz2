---
layout: layouts/docsWithContentNav.njk
title: History in Stores
permalink: /docs/history/
eleventyNavigation:
    key: history
    parent: documentation
    title: History in Stores
    order: 76
---

Sometimes you may want to keep the history of values in your `Store`, so you can navigate back in time to build an undo-function or maybe just for debugging...

fritz2 offers a history service to do so.

```kotlin
val store = object : RootStore<String>("") {
    val history = history<String>().sync(this)
}
```

This way you synchronise the history with the updates of your `Store`, so each new value will be added to the history automatically.

Without `sync()`, you have to add new entries to the history manually by calling `history.add(entry)`.

You can access the complete history via its `Flow` as a `List` of entries. For your convenience `history` also offers
* a `Flow<Boolean>` called `available` representing if entries are available (e.g., to show or hide an undo button)
* a `last()` method to access the latest entry
* a `back()` method to get the latest entry and remove it from the history
* a `reset()` method to clear the history

So for a `Store` with a minimal undo function you just have to write:

```kotlin
val store = object : RootStore<String>("") {
    val history = history<String>().sync(this)
    
    // your handlers go here (add history.reset() here where suitable)

    val undo = handle {
        history.back()
    }
}

...

render {
    div("form") {
        // insert your form here
    
        button("btn") {
            className(store.history.available.map { if (it) "" else "hidden" })
            +"Undo"
            
            clicks handledBy store.undo
        }
    }
}
```

To see a more mature undo function in action, go to our [repositories example](https://examples.fritz2.dev/repositories/build/distributions/index.html).
