[fritz2](../../index.md) / [io.fritz2.binding](../index.md) / [SubStore](./index.md)

# SubStore

`@FlowPreview @ExperimentalCoroutinesApi class SubStore<R, P, T> : `[`Store`](../-store/index.md)`<T>`

### Constructors

| [&lt;init&gt;](-init-.md) | `SubStore(parent: `[`Store`](../-store/index.md)`<P>, lens: Lens<P, T>, rootStore: `[`RootStore`](../-root-store/index.md)`<R>, rootLens: Lens<R, T>)` |

### Properties

| [data](data.md) | `val data: Flow<T>` |
| [id](id.md) | `val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [rootLens](root-lens.md) | `val rootLens: Lens<R, T>` |
| [rootStore](root-store.md) | `val rootStore: `[`RootStore`](../-root-store/index.md)`<R>` |

### Functions

| [enqueue](enqueue.md) | `fun enqueue(update: `[`Update`](../-update.md)`<T>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [sub](sub.md) | `fun <X> sub(lens: Lens<T, X>): `[`SubStore`](./index.md)`<R, T, X>` |

### Extension Functions

| [eachStore](../each-store.md) | `fun <R, P, T : withId> `[`SubStore`](./index.md)`<R, P, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.eachStore(): `[`Seq`](../-seq/index.md)`<`[`SubStore`](./index.md)`<R, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>>` |
| [sub](../sub.md) | `fun <R, P, T : withId> `[`SubStore`](./index.md)`<R, P, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.sub(element: T): `[`SubStore`](./index.md)`<R, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>` |

