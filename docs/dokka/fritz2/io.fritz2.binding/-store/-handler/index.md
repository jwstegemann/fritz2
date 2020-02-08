[fritz2](../../../index.md) / [io.fritz2.binding](../../index.md) / [Store](../index.md) / [Handler](./index.md)

# Handler

`inner class Handler<A, T>`

### Constructors

| [&lt;init&gt;](-init-.md) | `Handler(handler: (T, A) -> T)` |

### Properties

| [handler](handler.md) | `val handler: (T, A) -> T` |

### Functions

| [compareTo](compare-to.md) | `operator fun compareTo(flow: Flow<A>): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [handle](handle.md) | `fun handle(actions: Flow<A>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`fun handle(action: A): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

