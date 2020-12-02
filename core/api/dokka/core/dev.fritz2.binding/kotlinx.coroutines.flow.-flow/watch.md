[core](../../index.md) / [dev.fritz2.binding](../index.md) / [kotlinx.coroutines.flow.Flow](index.md) / [watch](./watch.md)

# watch

(js) `fun <T> Flow<T>.watch(scope: CoroutineScope = MainScope()): Job`

If a [Store](../-store/index.md)'s data-[Flow](#) is never mounted, use this method to start the updating of derived values.

