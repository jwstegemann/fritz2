[fritz2](../../index.md) / [io.fritz2.remote](../index.md) / [kotlinx.coroutines.flow.Flow](index.md) / [onError](./on-error.md)

# onError

`@ExperimentalCoroutinesApi fun Flow<`[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html)`>.onError(handler: (`[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Flow<`[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html)`>`

defines, how to handle an error that occurred during a http request.

### Parameters

`handler` - function that describes, how to handle a thrown [FetchException](../-fetch-exception/index.md)