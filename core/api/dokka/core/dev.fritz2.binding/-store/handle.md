[core](../../index.md) / [dev.fritz2.binding](../index.md) / [Store](index.md) / [handle](./handle.md)

# handle

(js) `inline fun <A> handle(crossinline execute: (T, A) -> T): `[`SimpleHandler`](../-simple-handler/index.md)`<A>`

factory method to create a [SimpleHandler](../-simple-handler/index.md) mapping the actual value of the [Store](index.md) and a given Action to a new value.

### Parameters

`execute` - lambda that is executed whenever a new action-value appears on the connected event-[Flow](#).(js) `inline fun handle(crossinline execute: (T) -> T): `[`SimpleHandler`](../-simple-handler/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

factory method to create a [SimpleHandler](../-simple-handler/index.md) that does not take an Action

### Parameters

`execute` - lambda that is execute for each event on the connected [Flow](#)