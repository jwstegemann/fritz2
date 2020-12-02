[core](../../index.md) / [dev.fritz2.binding](../index.md) / [kotlinx.coroutines.flow.Flow](index.md) / [handledBy](./handled-by.md)

# handledBy

(js) `infix fun <A> Flow<A>.handledBy(handler: `[`Handler`](../-handler/index.md)`<A>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

bind a [Flow](#) of actions/events to an [Handler](../-handler/index.md)

### Parameters

`handler` - [Handler](../-handler/index.md) that will be called for each action/event on the [Flow](#)

**Receiver**
[Flow](#) of action/events to bind to an [Handler](../-handler/index.md)

