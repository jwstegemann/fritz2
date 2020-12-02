[core](../../index.md) / [dev.fritz2.binding](../index.md) / [SimpleHandler](./index.md)

# SimpleHandler

(js) `class SimpleHandler<A> : `[`Handler`](../-handler/index.md)`<A>`

A [SimpleHandler](./index.md) defines, how to handle actions in your [Store](../-store/index.md). Each Handler accepts actions of a defined type.
If your handler just needs the current value of the [Store](../-store/index.md) and no action, use [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html).

### Parameters

`execute` - defines how to handle the values of the connected [Flow](#)

### Constructors

| (js) [&lt;init&gt;](-init-.md) | A [SimpleHandler](./index.md) defines, how to handle actions in your [Store](../-store/index.md). Each Handler accepts actions of a defined type. If your handler just needs the current value of the [Store](../-store/index.md) and no action, use [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html).`SimpleHandler(execute: (Flow<A>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)` |

### Properties

| (js) [execute](execute.md) | defines how to handle the values of the connected [Flow](#)`val execute: (Flow<A>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

