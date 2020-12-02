[core](../../index.md) / [dev.fritz2.binding](../index.md) / [SubStore](./index.md)

# SubStore

(js) `class SubStore<R, P, T> : `[`Store`](../-store/index.md)`<T>`

A [Store](../-store/index.md) that is derived from your [RootStore](../-root-store/index.md) or another [SubStore](./index.md) that represents a part of the data-model of it's parent.
Use the .sub-factory-method on the parent [Store](../-store/index.md) to create it.

### Constructors

| (js) [&lt;init&gt;](-init-.md) | A [Store](../-store/index.md) that is derived from your [RootStore](../-root-store/index.md) or another [SubStore](./index.md) that represents a part of the data-model of it's parent. Use the .sub-factory-method on the parent [Store](../-store/index.md) to create it.`SubStore(parent: `[`Store`](../-store/index.md)`<P>, lens: `[`Lens`](../../dev.fritz2.lenses/-lens/index.md)`<P, T>, rootStore: `[`RootStore`](../-root-store/index.md)`<R>, rootLens: `[`Lens`](../../dev.fritz2.lenses/-lens/index.md)`<R, T>)` |

### Properties

| (js) [data](data.md) | the current value of the [SubStore](./index.md) is derived from the data of it's parent using the given [Lens](../../dev.fritz2.lenses/-lens/index.md#dev.fritz2.lenses.Lens).`val data: Flow<T>` |
| (js) [id](id.md) | defines how to infer the id of the sub-part from the parent's id.`val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| (js) [enqueue](enqueue.md) | Since a [SubStore](./index.md) is just a view on a [RootStore](../-root-store/index.md) holding the real value, it forwards the [Update](../-update.md) to it, using it's [Lens](../../dev.fritz2.lenses/-lens/index.md#dev.fritz2.lenses.Lens) to transform it.`suspend fun enqueue(update: `[`Update`](../-update.md)`<T>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| (js) [sub](sub.md) | factory-method to create another [SubStore](./index.md) using this one as it's parent.`fun <X> sub(lens: `[`Lens`](../../dev.fritz2.lenses/-lens/index.md)`<T, X>): `[`SubStore`](./index.md)`<R, T, X>` |
| (js) [using](using.md) | a factory-method to create a [FormatStore](../../dev.fritz2.format/-format-store/index.md) from this [Store](../-store/index.md) using the given [Format](../../dev.fritz2.format/-format/index.md#dev.fritz2.format.Format) to convert the current value as well as [Update](../-update.md)s`infix fun using(format: `[`Format`](../../dev.fritz2.format/-format/index.md)`<T>): `[`FormatStore`](../../dev.fritz2.format/-format-store/index.md)`<R, T>` |

### Extension Functions

| (js) [eachStore](../each-store.md) | convenience-method to create a [Seq](../-seq/index.md) of [SubStores](#), one for each element of the [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html). You can also call [each](../kotlinx.coroutines.flow.-flow/each.md) and inside it's lambda create the [SubStore](./index.md) using [sub](../sub.md).`fun <R, P, T : `[`WithId`](../../dev.fritz2.lenses/-with-id/index.md)`> `[`SubStore`](./index.md)`<R, P, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.eachStore(): `[`Seq`](../-seq/index.md)`<`[`SubStore`](./index.md)`<R, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>>` |
| (js) [sub](../sub.md) | factory-method to create a [SubStore](./index.md) using another [SubStore](./index.md) as parent.`fun <R, P, T : `[`WithId`](../../dev.fritz2.lenses/-with-id/index.md)`> `[`SubStore`](./index.md)`<R, P, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.sub(element: T): `[`SubStore`](./index.md)`<R, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>` |

