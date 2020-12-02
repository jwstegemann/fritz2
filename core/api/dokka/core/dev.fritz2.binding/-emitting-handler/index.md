[core](../../index.md) / [dev.fritz2.binding](../index.md) / [EmittingHandler](./index.md)

# EmittingHandler

(js) `class EmittingHandler<A, E> : `[`Handler`](../-handler/index.md)`<A>, Flow<E>`

An [EmittingHandler](./index.md) is a special [SimpleHandler](../-simple-handler/index.md) that constitutes a new [Flow](#) by itself. You can emit values to this [Flow](#) from your code
and connect it to other [SimpleHandler](../-simple-handler/index.md)s on this or on other [Store](../-store/index.md)s. This way inter-store-communication is done in fritz2.

### Parameters

`bufferSize` - number of values of the new [Flow](#) to buffer

`executeWithChannel` - defines how to handle the values of the connected [Flow](#)

### Constructors

| (js) [&lt;init&gt;](-init-.md) | An [EmittingHandler](./index.md) is a special [SimpleHandler](../-simple-handler/index.md) that constitutes a new [Flow](#) by itself. You can emit values to this [Flow](#) from your code and connect it to other [SimpleHandler](../-simple-handler/index.md)s on this or on other [Store](../-store/index.md)s. This way inter-store-communication is done in fritz2.`EmittingHandler(bufferSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, executeWithChannel: (Flow<A>, SendChannel<E>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)` |

### Properties

| (js) [execute](execute.md) | `val execute: (Flow<A>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| (js) [executeWithChannel](execute-with-channel.md) | defines how to handle the values of the connected [Flow](#)`val executeWithChannel: (Flow<A>, SendChannel<E>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Functions

| (js) [collect](collect.md) | implementing the [Flow](#)-interface`suspend fun collect(collector: FlowCollector<E>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

