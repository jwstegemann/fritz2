[fritz2](../../index.md) / [io.fritz2.binding](../index.md) / [Store](./index.md)

# Store

`abstract class Store<T>`

A Store is the plave to "store" the data, on which changes you want to react.

### Parameters

`T` - Type of the data that this store holds

### Types

| [Handler](-handler/index.md) | `inner class Handler<A, T>` |

### Constructors

| [&lt;init&gt;](-init-.md) | A Store is the plave to "store" the data, on which changes you want to react.`Store()` |

### Properties

| [data](data.md) | `abstract val data: Flow<T>` |
| [id](id.md) | `abstract val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [update](update.md) | `val update: Handler<T, T>` |

### Functions

| [enqueue](enqueue.md) | Enqueue a specific update of you modle.`abstract fun enqueue(update: `[`Update`](../-update.md)`<T>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [sub](sub.md) | `abstract fun <X> sub(lens: Lens<T, X>): `[`Store`](./index.md)`<X>` |

### Extension Functions

| [each](../each.md) | `fun <T : withId> `[`Store`](./index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.each(): `[`Seq`](../-seq.md)`<T>` |

### Inheritors

| [RootStore](../-root-store/index.md) | `open class RootStore<T> : `[`Store`](./index.md)`<T>` |
| [SubStore](../-sub-store/index.md) | `class SubStore<R, P, T> : `[`Store`](./index.md)`<T>` |

