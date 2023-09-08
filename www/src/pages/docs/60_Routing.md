---
title: Routing
description: "Learn how to do client-side routing in fritz2"
layout: layouts/docs.njk
permalink: /docs/routing/
eleventyNavigation:
    key: routing
    parent: documentation
    title: Routing
    order: 60
---

When writing a Single Page Application (SPA), you might need a way to render a certain view depending on url-parameters. 
This is called routing. fritz2 uses the hash-part of the url which starts with a `#`: `https://my.doma.in/path#hash`. 
Url-parameters (`?`) are usually handled by the server, while url-hashes (`#`) are handled by the browser.

fritz2 offers a general mechanism for using client-site routing in your SPA. 
It includes a class called `Router` which registers itself to listen for the specific dom-events of the hash changing. 
Additionally, a `Router` needs a `Route` which is an interface you have to implement to use it:
```kotlin
interface Route<T> {
    val default: T
    fun deserialize(hash: String): T
    fun serialize(route: T): String
}
```
A `Route` always has a default route to start with.
It also needs a way to serialize and deserialize the url-hash string to a kotlin (data-)class and vice versa.

To make this mechanism easier to use, we implemented two ways of handling routing in fritz2: 
* `StringRoute` uses the url-hash how it is.
* `MapRoute` serializes and deserializes the url-hash to `Map<String,String>` where `&` is separating the entries.

You can easily create a new instance of `Router` by using the global `routerOf()` function. 
There are currently three of them, one for each type of `Route`:
* `routerOf(default: String): Router<String>` uses `StringRoute`
* `routerOf(default: Map<String, String>): MapRouter` uses `MapRoute`
* `<T> routerOf(default: Route<T>): Router<T>` uses your custom implementation of the `Route` interface

Routing is straightforward using simple `String`s by `StringRoute`:
```kotlin
val router = routerOf("welcome")

render {
    section {
        router.data.render { site ->
            when(site) {
                "welcome" -> div { +"Welcome" }
                "pageA" -> div { +"Page A" }
                "pageB" -> div { +"Page B" }
                else -> div { +"not found" }
            }
        }
    }
}
```

## MapRouter
In bigger web-apps often need additional routing information when navigating to another site.
Here, use the `MapRouter` instead of the string router:
```kotlin
val router = routerOf(mapOf("page" to "welcome", "foo" to "bar"))

render {
    section {
        // use a child store for two-way data binding
        val foo = router.map("foo")
        foo.update("bars")

        // or use special select() function where 'other' contains the rest 
        // of the map entries for one-way data binding
        router.select(key = "page").render { (page, other) ->
            when(page) {
                "welcome" -> div { +"Welcome" }
                "pageA" -> div {
                    +"Page A: "
                    foo.data.renderText()
                }
                "pageB" -> div { +"Page B: ${other["foo"] ?: ""}" }
                else -> div { +"not found" }
            }
        }
    }
}
```
A router using a `MapRoute` offers two extra `select` methods:
* `fun select(key: String): Flow<Pair<String?, Map<String, String>>>` extracts the values as `Pair` for the given `key` 
and the rest of the route
* `fun select(key: String, orElse: String): Flow<String>` extracts the value for a given `key` when available, otherwise 
it returns `orElse`

Or use the well-known `map()` function by providing a `key` to get a new `Store` to render its data and to handle updates.

If you want to use your own `Route` instead, try this:
```kotlin
class SetRoute(override val default: Set<String>) : Route<Set<String>> {
    private val separator = "&"
    override fun deserialize(hash: String): Set<String> = hash.split(separator).toSet()
    override fun serialize(route: Set<String>): String = route.joinToString(separator)
}

val router = routerOf(SetRoute(setOf("welcome")))

fun main() {
    render {
        section {
            router.data.render { route ->
                when {
                    route.contains("welcome") -> div { +"Welcome" }
                    route.contains("pageA") -> div { +"Page A" }
                    route.contains("pageB") -> div { +"Page B" }
                    else -> div { +"not found" }
                }
            }
        }
    }
}
```

In more complex scenarios, it's common to create your own implementation of `Router` by using either the
`StringRouter` or `MapRouter` as parent class:
```kotlin
object MyRouter : MapRouter(mapOf("page" to "overview")) {

    val overview = handle {
        it + ("page" to "overview")
    }

    val details = handle<String> { route, id ->
        route + mapOf("page" to "details", "detailsId" to id)
    }
}
```
This lets you create custom handlers inside your router to improve internal state managing, analogous to our `Store`s.


If you want to change your current route (e.g. when an event fires), 
you can do so by calling the predefined handler `navTo`: 
```kotlin
val router = routerOf("welcome")

render {
    button {
        +"Navigate to Page A"
        clicks.map { "pageA" } handledBy router.navTo
    }
}
// or call handler directly
router.navTo("pageA")
```
Or call your own handlers in case you created any:
```kotlin
object MyRouter : MapRouter(mapOf("page" to "overview")) {
    val overview = handle { it + ("page" to "overview") }
    val details = handle<String> { route, id -> route + mapOf("page" to "details", "detailsId" to id) }
}

fun main() {
    render {
        button {
            +"Show overview"
            clicks handledBy MyRouter.overview
        }
        button {
            +"Show details"
            clicks.map { "12" } handledBy MyRouter.details
        }
    }
    // or call handler directly
    MyRouter.details("12")
}
```

Have a look at our [routing example](/examples/routing) to see how it works and to play around with it.
