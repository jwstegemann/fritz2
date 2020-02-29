[fritz2](../../index.md) / [io.fritz2.routing](../index.md) / [MapRoute](./index.md)

# MapRoute

`class MapRoute : `[`Route`](../-route/index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>`

[MapRoute](./index.md) marshals and unmarshals a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) to and from *window.location.hash*.
It is like using url parameters with pairs of key and value.
In the begin there is only a **#** instead of **?**.

### Parameters

`default` - [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) to use when no explicit *window.location.hash* was set before

### Constructors

| [&lt;init&gt;](-init-.md) | [MapRoute](./index.md) marshals and unmarshals a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) to and from *window.location.hash*. It is like using url parameters with pairs of key and value. In the begin there is only a **#** instead of **?**.`MapRoute(default: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>)` |

### Properties

| [default](default.md) | [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) to use when no explicit *window.location.hash* was set before`val default: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

### Functions

| [marshal](marshal.md) | Marshals a given object of type [T](../-route/index.md#T) to [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) for setting it to the *window.location.hash*`fun marshal(route: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [unmarshal](unmarshal.md) | Unmarshals the *window.location.hash* to the given type [T](../-route/index.md#T) after getting the hashchange-event.`fun unmarshal(hash: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

