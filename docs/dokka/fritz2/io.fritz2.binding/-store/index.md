[fritz2](../../index.md) / [io.fritz2.binding](../index.md) / [Store](./index.md)

# Store

`@ExperimentalCoroutinesApi abstract class Store<T> : CoroutineScope`

### Constructors

| [&lt;init&gt;](-init-.md) | `Store()` |

### Properties

| [data](data.md) | `abstract val data: Flow<T>` |
| [id](id.md) | `abstract val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [update](update.md) | `val update: `[`Handler`](../-handler/index.md)`<T>` |

### Functions

| [andThen](and-then.md) | `infix fun <A, X> `[`Applicator`](../-applicator/index.md)`<A, X>.andThen(nextHandler: `[`Handler`](../-handler/index.md)`<X>): `[`Handler`](../-handler/index.md)`<A>` |
| [apply](apply.md) | `fun <A, X> apply(mapper: suspend (A) -> Flow<X>): `[`Applicator`](../-applicator/index.md)`<A, X>` |
| [enqueue](enqueue.md) | `abstract suspend fun enqueue(update: `[`Update`](../-update.md)`<T>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [handle](handle.md) | `fun <A> handle(handler: (T, A) -> T): `[`Handler`](../-handler/index.md)`<A>` |
| [sub](sub.md) | `abstract fun <X> sub(lens: Lens<T, X>): `[`Store`](./index.md)`<X>` |

### Inheritors

| [RootStore](../-root-store/index.md) | `open class RootStore<T> : `[`Store`](./index.md)`<T>` |
| [SubStore](../-sub-store/index.md) | `class SubStore<R, P, T> : `[`Store`](./index.md)`<T>` |

