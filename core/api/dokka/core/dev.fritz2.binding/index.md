[core](../index.md) / [dev.fritz2.binding](./index.md)

## Package dev.fritz2.binding

### Types

| (js) [Applicator](-applicator/index.md) | `class Applicator<A, X>` |
| (js) [EmittingHandler](-emitting-handler/index.md) | An [EmittingHandler](-emitting-handler/index.md) is a special [SimpleHandler](-simple-handler/index.md) that constitutes a new [Flow](#) by itself. You can emit values to this [Flow](#) from your code and connect it to other [SimpleHandler](-simple-handler/index.md)s on this or on other [Store](-store/index.md)s. This way inter-store-communication is done in fritz2.`class EmittingHandler<A, E> : `[`Handler`](-handler/index.md)`<A>, Flow<E>` |
| (js) [Handler](-handler/index.md) | `interface Handler<A>` |
| (js) [MultiMountPoint](-multi-mount-point/index.md) | A [MultiMountPoint](-multi-mount-point/index.md) collects the values of a given [Flow](#) one by one. Use this for data-types that represent a sequence of values.`abstract class MultiMountPoint<T> : CoroutineScope` |
| (js) [Patch](-patch/index.md) | A [Patch](-patch/index.md) describes the changes made to a [Seq](-seq/index.md)`sealed class Patch<out T>` |
| (js) [RootStore](-root-store/index.md) | A [Store](-store/index.md) can be initialized with a given value. Use a [RootStore](-root-store/index.md) to "store" your model and create [SubStore](-sub-store/index.md)s from here.`open class RootStore<T> : `[`Store`](-store/index.md)`<T>` |
| (js) [Seq](-seq/index.md) | Defines a sequence of values`class Seq<T>` |
| (js) [SimpleHandler](-simple-handler/index.md) | A [SimpleHandler](-simple-handler/index.md) defines, how to handle actions in your [Store](-store/index.md). Each Handler accepts actions of a defined type. If your handler just needs the current value of the [Store](-store/index.md) and no action, use [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html).`class SimpleHandler<A> : `[`Handler`](-handler/index.md)`<A>` |
| (js) [SingleMountPoint](-single-mount-point/index.md) | A [SingleMountPoint](-single-mount-point/index.md) collects the values of a given [Flow](#) one by one. Use this for data-types that represent a single (simple or complex) value.`abstract class SingleMountPoint<T> : CoroutineScope` |
| (js) [Store](-store/index.md) | The [Store](-store/index.md) is the main type for all data binding activities. It the base class of all concrete Stores like [RootStore](-root-store/index.md), [SubStore](-sub-store/index.md), etc.`abstract class Store<T> : CoroutineScope` |
| (js) [SubStore](-sub-store/index.md) | A [Store](-store/index.md) that is derived from your [RootStore](-root-store/index.md) or another [SubStore](-sub-store/index.md) that represents a part of the data-model of it's parent. Use the .sub-factory-method on the parent [Store](-store/index.md) to create it.`class SubStore<R, P, T> : `[`Store`](-store/index.md)`<T>` |
| (js) [Update](-update.md) | defines a type for transforming one value into the next`typealias Update<T> = (T) -> T` |

### Extensions for External Classes

| (js) [kotlinx.coroutines.flow.Flow](kotlinx.coroutines.flow.-flow/index.md) |  |

### Functions

| (js) [const](const.md) | convenience-method to create a never changing [Flow](#) just to be more readable in templates, etc.`fun <T> const(value: T): Flow<T>` |
| (js) [eachStore](each-store.md) | convenience-method to create a [Seq](-seq/index.md) of [SubStores](#), one for each element of the [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html). You can also call [each](kotlinx.coroutines.flow.-flow/each.md) and inside it's lambda create the [SubStore](-sub-store/index.md) using [sub](sub.md).`fun <T : `[`WithId`](../dev.fritz2.lenses/-with-id/index.md)`> `[`RootStore`](-root-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.eachStore(): `[`Seq`](-seq/index.md)`<`[`SubStore`](-sub-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>>`<br>`fun <R, P, T : `[`WithId`](../dev.fritz2.lenses/-with-id/index.md)`> `[`SubStore`](-sub-store/index.md)`<R, P, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.eachStore(): `[`Seq`](-seq/index.md)`<`[`SubStore`](-sub-store/index.md)`<R, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>>` |
| (js) [sub](sub.md) | factory-method to create a [SubStore](-sub-store/index.md) using a [RootStore](-root-store/index.md) as parent.`fun <T : `[`WithId`](../dev.fritz2.lenses/-with-id/index.md)`> `[`RootStore`](-root-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.sub(element: T): `[`SubStore`](-sub-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>`<br>factory-method to create a [SubStore](-sub-store/index.md) using another [SubStore](-sub-store/index.md) as parent.`fun <R, P, T : `[`WithId`](../dev.fritz2.lenses/-with-id/index.md)`> `[`SubStore`](-sub-store/index.md)`<R, P, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.sub(element: T): `[`SubStore`](-sub-store/index.md)`<R, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>` |

