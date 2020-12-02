[core](../../index.md) / [dev.fritz2.binding](../index.md) / [Handler](./index.md)

# Handler

(js) `interface Handler<A>`

### Properties

| (js) [execute](execute.md) | `abstract val execute: (Flow<A>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| (js) [EmittingHandler](../-emitting-handler/index.md) | An [EmittingHandler](../-emitting-handler/index.md) is a special [SimpleHandler](../-simple-handler/index.md) that constitutes a new [Flow](#) by itself. You can emit values to this [Flow](#) from your code and connect it to other [SimpleHandler](../-simple-handler/index.md)s on this or on other [Store](../-store/index.md)s. This way inter-store-communication is done in fritz2.`class EmittingHandler<A, E> : `[`Handler`](./index.md)`<A>, Flow<E>` |
| (js) [SimpleHandler](../-simple-handler/index.md) | A [SimpleHandler](../-simple-handler/index.md) defines, how to handle actions in your [Store](../-store/index.md). Each Handler accepts actions of a defined type. If your handler just needs the current value of the [Store](../-store/index.md) and no action, use [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html).`class SimpleHandler<A> : `[`Handler`](./index.md)`<A>` |

