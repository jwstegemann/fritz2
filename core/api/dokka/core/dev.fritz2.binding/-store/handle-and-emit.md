[core](../../index.md) / [dev.fritz2.binding](../index.md) / [Store](index.md) / [handleAndEmit](./handle-and-emit.md)

# handleAndEmit

(js) `inline fun <A, E> handleAndEmit(bufferSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 1, crossinline execute: SendChannel<E>.(T, A) -> T): `[`EmittingHandler`](../-emitting-handler/index.md)`<A, E>`

factory method to create a [EmittingHandler](../-emitting-handler/index.md) taking an action-value and the current store value to derive the new value.
An [EmittingHandler](../-emitting-handler/index.md) is a [Flow](#) by itself and can therefore be connected to other [SimpleHandler](../-simple-handler/index.md)s even in other [Store](index.md)s.

### Parameters

`bufferSize` - number of values to buffer

`execute` - lambda that is executed for each action-value on the connected [Flow](#). You can emit values from this lambda.(js) `inline fun <A, E> handleAndEmit(bufferSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 1, crossinline execute: SendChannel<E>.(T) -> T): `[`EmittingHandler`](../-emitting-handler/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`, E>`

factory method to create an [EmittingHandler](../-emitting-handler/index.md) that does not take an action in it's [execute](handle-and-emit.md#dev.fritz2.binding.Store$handleAndEmit(kotlin.Int, kotlin.Function2((kotlinx.coroutines.channels.SendChannel((dev.fritz2.binding.Store.handleAndEmit.E)), dev.fritz2.binding.Store.T, )))/execute)-lambda.

### Parameters

`bufferSize` - number of values to buffer

`execute` - lambda that is executed for each event on the connected [Flow](#). You can emit values from this lambda.