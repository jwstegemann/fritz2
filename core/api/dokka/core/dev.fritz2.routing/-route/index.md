[core](../../index.md) / [dev.fritz2.routing](../index.md) / [Route](./index.md)

# Route

(js) `interface Route<T>`

A Route is a abstraction for routes
which needed for routing

### Parameters

`T` - type to marshal and unmarshal from

### Properties

| (js) [default](default.md) | Gives the default value when initialising the routing`abstract val default: T` |

### Functions

| (js) [marshal](marshal.md) | Marshals a given object of type [T](index.md#T) to [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) for setting it to the *window.location.hash*`abstract fun marshal(route: T): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| (js) [unmarshal](unmarshal.md) | Unmarshals the *window.location.hash* to the given type [T](index.md#T) after getting the hashchange-event.`abstract fun unmarshal(hash: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): T` |

### Inheritors

| (js) [MapRoute](../-map-route/index.md) | [MapRoute](../-map-route/index.md) marshals and unmarshals a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) to and from *window.location.hash*. It is like using url parameters with pairs of key and value. In the begin there is only a **#** instead of **?**.`class MapRoute : `[`Route`](./index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
| (js) [StringRoute](../-string-route/index.md) | [StringRoute](../-string-route/index.md) is a simple [Route](./index.md) which marshals and unmarshals nothing.`class StringRoute : `[`Route`](./index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

