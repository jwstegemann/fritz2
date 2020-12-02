[core](../../index.md) / [dev.fritz2.binding](../index.md) / [RootStore](./index.md)

# RootStore

(js) `open class RootStore<T> : `[`Store`](../-store/index.md)`<T>`

A [Store](../-store/index.md) can be initialized with a given value. Use a [RootStore](./index.md) to "store" your model and create [SubStore](../-sub-store/index.md)s from here.

### Parameters

`initialData` - : the first current value of this [Store](../-store/index.md)

`id` - : the id of this store. ids of [SubStore](../-sub-store/index.md)s will be concatenated.

`bufferSize` - : number of values to buffer

### Constructors

| (js) [&lt;init&gt;](-init-.md) | A [Store](../-store/index.md) can be initialized with a given value. Use a [RootStore](./index.md) to "store" your model and create [SubStore](../-sub-store/index.md)s from here.`RootStore(initialData: T, id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", bufferSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 1)` |

### Properties

| (js) [data](data.md) | the current value of a [RootStore](./index.md) is derived be applying the updates on the internal channel one by one to get the next value. the [Flow](#) only emit's a new value, when the value is differs from the last one to avoid calculations and updates that are not necessary. This has to be a SharedFlow, because the updated should only be applied once, regardless how many depending values or ui-elements or bound to it.`open val data: Flow<T>` |
| (js) [id](id.md) | : the id of this store. ids of [SubStore](../-sub-store/index.md)s will be concatenated.`open val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| (js) [enqueue](enqueue.md) | in a [RootStore](./index.md) an [Update](../-update.md) is handled by sending it to the internal [updates](#)-channel.`open suspend fun enqueue(update: `[`Update`](../-update.md)`<T>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| (js) [sub](sub.md) | create a [SubStore](../-sub-store/index.md) that represents a certain part of your data model.`fun <X> sub(lens: `[`Lens`](../../dev.fritz2.lenses/-lens/index.md)`<T, X>): `[`SubStore`](../-sub-store/index.md)`<T, T, X>` |

### Extension Functions

| (js) [eachStore](../each-store.md) | convenience-method to create a [Seq](../-seq/index.md) of [SubStores](#), one for each element of the [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html). You can also call [each](../kotlinx.coroutines.flow.-flow/each.md) and inside it's lambda create the [SubStore](../-sub-store/index.md) using [sub](../sub.md).`fun <T : `[`WithId`](../../dev.fritz2.lenses/-with-id/index.md)`> `[`RootStore`](./index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.eachStore(): `[`Seq`](../-seq/index.md)`<`[`SubStore`](../-sub-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>>` |
| (js) [sub](../sub.md) | factory-method to create a [SubStore](../-sub-store/index.md) using a [RootStore](./index.md) as parent.`fun <T : `[`WithId`](../../dev.fritz2.lenses/-with-id/index.md)`> `[`RootStore`](./index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.sub(element: T): `[`SubStore`](../-sub-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>` |

