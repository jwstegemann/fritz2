[core](../../index.md) / [dev.fritz2.binding](../index.md) / [Store](./index.md)

# Store

(js) `abstract class Store<T> : CoroutineScope`

The [Store](./index.md) is the main type for all data binding activities. It the base class of all concrete Stores like [RootStore](../-root-store/index.md), [SubStore](../-sub-store/index.md), etc.

### Constructors

| (js) [&lt;init&gt;](-init-.md) | The [Store](./index.md) is the main type for all data binding activities. It the base class of all concrete Stores like [RootStore](../-root-store/index.md), [SubStore](../-sub-store/index.md), etc.`Store()` |

### Properties

| (js) [data](data.md) | the [Flow](#) representing the current value of the [Store](./index.md). Use this to bind it to ui-elements or derive calculated values by using [map](#) for example.`abstract val data: Flow<T>` |
| (js) [id](id.md) | base-id of this [Store](./index.md). ids of depending [Store](./index.md)s are concatenated separated by a dot.`abstract val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| (js) [update](update.md) | a simple [SimpleHandler](../-simple-handler/index.md) that just takes the given action-value as the new value for the [Store](./index.md).`val update: `[`SimpleHandler`](../-simple-handler/index.md)`<T>` |

### Functions

| (js) [apply](apply.md) | factory method, to create an [Applicator](../-applicator/index.md).`fun <A, X> apply(mapper: suspend (A) -> Flow<X>): `[`Applicator`](../-applicator/index.md)`<A, X>` |
| (js) [enqueue](enqueue.md) | abstract method defining, how this [Store](./index.md) handles an [Update](../-update.md)`abstract suspend fun enqueue(update: `[`Update`](../-update.md)`<T>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| (js) [handle](handle.md) | factory method to create a [SimpleHandler](../-simple-handler/index.md) mapping the actual value of the [Store](./index.md) and a given Action to a new value.`fun <A> handle(execute: (T, A) -> T): `[`SimpleHandler`](../-simple-handler/index.md)`<A>`<br>factory method to create a [SimpleHandler](../-simple-handler/index.md) that does not take an Action`fun handle(execute: (T) -> T): `[`SimpleHandler`](../-simple-handler/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>` |
| (js) [handleAndEmit](handle-and-emit.md) | factory method to create a [EmittingHandler](../-emitting-handler/index.md) taking an action-value and the current store value to derive the new value. An [EmittingHandler](../-emitting-handler/index.md) is a [Flow](#) by itself and can therefore be connected to other [SimpleHandler](../-simple-handler/index.md)s even in other [Store](./index.md)s.`fun <A, E> handleAndEmit(bufferSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 1, execute: SendChannel<E>.(T, A) -> T): `[`EmittingHandler`](../-emitting-handler/index.md)`<A, E>`<br>factory method to create an [EmittingHandler](../-emitting-handler/index.md) that does not take an action in it's [execute](handle-and-emit.md#dev.fritz2.binding.Store$handleAndEmit(kotlin.Int, kotlin.Function2((kotlinx.coroutines.channels.SendChannel((dev.fritz2.binding.Store.handleAndEmit.E)), dev.fritz2.binding.Store.T, )))/execute)-lambda.`fun <A, E> handleAndEmit(bufferSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 1, execute: SendChannel<E>.(T) -> T): `[`EmittingHandler`](../-emitting-handler/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`, E>` |

### Inheritors

| (js) [FormatStore](../../dev.fritz2.format/-format-store/index.md) | A [Store](./index.md) representing the formatted value of it's parent [Store](./index.md). Use this to transparently bind a Date, an Int or some other data-type in your model to an HTML-input (that can only handle [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)s). Do not create an instance by yourself. Use the factory-method at [SubStore](#)`class FormatStore<R, P> : `[`Store`](./index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| (js) [RootStore](../-root-store/index.md) | A [Store](./index.md) can be initialized with a given value. Use a [RootStore](../-root-store/index.md) to "store" your model and create [SubStore](../-sub-store/index.md)s from here.`open class RootStore<T> : `[`Store`](./index.md)`<T>` |
| (js) [SubStore](../-sub-store/index.md) | A [Store](./index.md) that is derived from your [RootStore](../-root-store/index.md) or another [SubStore](../-sub-store/index.md) that represents a part of the data-model of it's parent. Use the .sub-factory-method on the parent [Store](./index.md) to create it.`class SubStore<R, P, T> : `[`Store`](./index.md)`<T>` |

