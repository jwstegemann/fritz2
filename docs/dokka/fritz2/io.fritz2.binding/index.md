[fritz2](../index.md) / [io.fritz2.binding](./index.md)

## Package io.fritz2.binding

### Types

| [Const](-const/index.md) | `class Const<T> : Flow<T>` |
| [Failable](-failable/index.md) | `interface Failable : withId` |
| [MultiMountPoint](-multi-mount-point/index.md) | `abstract class MultiMountPoint<T>` |
| [Patch](-patch/index.md) | `data class Patch<out T>` |
| [RootStore](-root-store/index.md) | `open class RootStore<T> : `[`Store`](-store/index.md)`<T>` |
| [Seq](-seq/index.md) | `class Seq<T>` |
| [SingleMountPoint](-single-mount-point/index.md) | `abstract class SingleMountPoint<T>` |
| [Store](-store/index.md) | A Store is the plave to "store" the data, on which changes you want to react.`abstract class Store<T>` |
| [SubStore](-sub-store/index.md) | `class SubStore<R, P, T> : `[`Store`](-store/index.md)`<T>` |
| [Update](-update.md) | An update on a store is a function to infer the next model from the current.`typealias Update<T> = (T) -> T` |
| [Validation](-validation/index.md) | `interface Validation<D, M : `[`Failable`](-failable/index.md)`, T>` |
| [Validator](-validator/index.md) | `abstract class Validator<D, M : `[`Failable`](-failable/index.md)`, T>` |

### Extensions for External Classes

| [kotlinx.coroutines.flow.Flow](kotlinx.coroutines.flow.-flow/index.md) |  |

### Functions

| [eachStore](each-store.md) | `fun <T : withId> `[`RootStore`](-root-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.eachStore(): `[`Seq`](-seq/index.md)`<`[`SubStore`](-sub-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>>`<br>`fun <R, P, T : withId> `[`SubStore`](-sub-store/index.md)`<R, P, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.eachStore(): `[`Seq`](-seq/index.md)`<`[`SubStore`](-sub-store/index.md)`<R, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>>` |
| [sub](sub.md) | `fun <T : withId> `[`RootStore`](-root-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.sub(element: T): `[`SubStore`](-sub-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>`<br>`fun <R, P, T : withId> `[`SubStore`](-sub-store/index.md)`<R, P, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.sub(element: T): `[`SubStore`](-sub-store/index.md)`<R, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>` |

