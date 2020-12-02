[core](../../index.md) / [dev.fritz2.routing](../index.md) / [StringRoute](./index.md)

# StringRoute

(js) `class StringRoute : `[`Route`](../-route/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

[StringRoute](./index.md) is a simple [Route](../-route/index.md) which
marshals and unmarshals nothing.

### Parameters

`default` - [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) to use when no explicit *window.location.hash* was set before

### Constructors

| (js) [&lt;init&gt;](-init-.md) | [StringRoute](./index.md) is a simple [Route](../-route/index.md) which marshals and unmarshals nothing.`StringRoute(default: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Properties

| (js) [default](default.md) | [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) to use when no explicit *window.location.hash* was set before`val default: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| (js) [marshal](marshal.md) | Marshals a given object of type [T](../-route/index.md#T) to [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) for setting it to the *window.location.hash*`fun marshal(route: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| (js) [unmarshal](unmarshal.md) | Unmarshals the *window.location.hash* to the given type [T](../-route/index.md#T) after getting the hashchange-event.`fun unmarshal(hash: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

