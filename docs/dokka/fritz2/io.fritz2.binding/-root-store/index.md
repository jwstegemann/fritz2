[fritz2](../../index.md) / [io.fritz2.binding](../index.md) / [RootStore](./index.md)

# RootStore

`@FlowPreview @ExperimentalCoroutinesApi open class RootStore<T> : `[`Store`](../-store/index.md)`<T>`

### Constructors

| [&lt;init&gt;](-init-.md) | `RootStore(initialData: T, id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", bufferSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 1)` |

### Properties

| [data](data.md) | `open val data: Flow<T>` |
| [id](id.md) | `open val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| [enqueue](enqueue.md) | `open suspend fun enqueue(update: `[`Update`](../-update.md)`<T>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [sub](sub.md) | `open fun <X> sub(lens: Lens<T, X>): `[`SubStore`](../-sub-store/index.md)`<T, T, X>` |

### Extension Functions

| [eachStore](../each-store.md) | `fun <T : withId> `[`RootStore`](./index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.eachStore(): `[`Seq`](../-seq/index.md)`<`[`SubStore`](../-sub-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>>` |
| [sub](../sub.md) | `fun <T : withId> `[`RootStore`](./index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.sub(element: T): `[`SubStore`](../-sub-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>` |

