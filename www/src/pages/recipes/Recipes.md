---
title: Recipes
layout: layouts/docsWithContentNav.njk
permalink: /recipes/
eleventyNavigation:
    key: recipes
    title: Recipes
    order: 30
    icon: book-open
    classes: "font-bold capitalize"
---

## Automatische Syncronisierung
Um bei einem neuen Wert in einen Store automatisch einen bestimmten Handler zu übergeben (Syncronisation), 
reicht es auf dem `data`-flow ein `drop(1)` aufzurufen, um den `initalData` Wert zu ignorieren.
````kotlin
val storeToSyncFrom: RootStore<A>
val storeToSyncTo: RootStore<B>
        
storeToSyncFrom.data.drop(1) handledBy storeToSyncTo.handle { b, a ->
    //...
}
// or without target store
storeToSyncFrom.data.drop(1) handledBy { a ->
    //... 
}
````

## Precise data-binding of lists when they could be emtpy
Wenn das umschließende tag nur gerendert werden soll, wenn die Liste im Flow auch Einträge besitzt, kann
das folgende pattern verwendet werden, um dies zu erreichen.
```kotlin
val messages: Flow<List<String>>

messages.map { it.isNotEmpty() }.distinctUntilChanged().render { isNotEmpty ->
    if (isNotEmpty) {
        div { // only shown, when list is not empty
            messages.renderEach {
                //...
            }
        }
    }
}
```