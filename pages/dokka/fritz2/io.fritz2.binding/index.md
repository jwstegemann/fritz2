[fritz2](../index.md) / [io.fritz2.binding](./index.md)

## Package io.fritz2.binding

### Types

| [Applicator](-applicator/index.md) | `class Applicator<A, X>` |
| [Handler](-handler/index.md) | `class Handler<A>` |
| [MultiMountPoint](-multi-mount-point/index.md) | `abstract class MultiMountPoint<T> : CoroutineScope` |
| [Patch](-patch/index.md) | `data class Patch<out T>` |
| [RootStore](-root-store/index.md) | `open class RootStore<T> : `[`Store`](-store/index.md)`<T>` |
| [Seq](-seq/index.md) | `class Seq<T>` |
| [SingleMountPoint](-single-mount-point/index.md) | `abstract class SingleMountPoint<T> : CoroutineScope` |
| [Store](-store/index.md) | `abstract class Store<T> : CoroutineScope` |
| [SubStore](-sub-store/index.md) | `class SubStore<R, P, T> : `[`Store`](-store/index.md)`<T>` |
| [Update](-update.md) | `typealias Update<T> = (T) -> T` |

### Extensions for External Classes

| [kotlinx.coroutines.flow.Flow](kotlinx.coroutines.flow.-flow/index.md) |  |

### Functions

| [const](const.md) | `fun <T> const(value: T): Flow<T>` |
| [eachStore](each-store.md) | `fun <T : WithId> `[`RootStore`](-root-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.eachStore(): `[`Seq`](-seq/index.md)`<`[`SubStore`](-sub-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>>`<br>`fun <R, P, T : WithId> `[`SubStore`](-sub-store/index.md)`<R, P, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.eachStore(): `[`Seq`](-seq/index.md)`<`[`SubStore`](-sub-store/index.md)`<R, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>>` |
| [sub](sub.md) | `fun <T : WithId> `[`RootStore`](-root-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.sub(element: T): `[`SubStore`](-sub-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>`<br>`fun <R, P, T : WithId> `[`SubStore`](-sub-store/index.md)`<R, P, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.sub(element: T): `[`SubStore`](-sub-store/index.md)`<R, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>` |
| [uniqueId](unique-id.md) | `fun uniqueId(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

