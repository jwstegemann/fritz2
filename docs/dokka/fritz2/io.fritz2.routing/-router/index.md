[fritz2](../../index.md) / [io.fritz2.routing](../index.md) / [Router](./index.md)

# Router

`@FlowPreview @ExperimentalCoroutinesApi open class Router<T> : CoroutineScope`

Router register the event-listener for hashchange-event and
handles route-changes. Therefore it uses a [Route](../-route/index.md) object
which can [Route.marshal](../-route/marshal.md) and [Route.unmarshal](../-route/unmarshal.md) the given type.

### Parameters

`T` - type to marshal and unmarshal

### Constructors

| [&lt;init&gt;](-init-.md) | Router register the event-listener for hashchange-event and handles route-changes. Therefore it uses a [Route](../-route/index.md) object which can [Route.marshal](../-route/marshal.md) and [Route.unmarshal](../-route/unmarshal.md) the given type.`Router(route: `[`Route`](../-route/index.md)`<T>)` |

### Properties

| [navTo](nav-to.md) | Handler vor setting a new [Route](../-route/index.md) based on given [Flow](#).`val navTo: `[`Handler`](../../io.fritz2.binding/-handler/index.md)`<T>` |
| [routes](routes.md) | Gives the actual route as [Flow](#)`val routes: Flow<T>` |

### Extension Functions

| [select](../select.md) | Select return a [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html) of the value and the complete routing [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) for the given key in the [mapper](../select.md#io.fritz2.routing$select(io.fritz2.routing.Router((kotlin.collections.Map((kotlin.String, )))), kotlin.String, kotlin.Function1((kotlin.Pair((kotlin.String, kotlin.collections.Map((, )))), io.fritz2.routing.select.X)))/mapper) function.`fun <X> `[`Router`](./index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>.select(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, mapper: (`[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>) -> X): Flow<X>` |

