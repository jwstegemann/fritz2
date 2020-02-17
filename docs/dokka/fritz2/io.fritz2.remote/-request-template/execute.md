[fritz2](../../index.md) / [io.fritz2.remote](../index.md) / [RequestTemplate](index.md) / [execute](./execute.md)

# execute

`@ExperimentalCoroutinesApi inline fun execute(crossinline url: `[`RequestTemplate`](index.md)`.() -> `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, init: `[`RequestInit`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-request-init/index.html)`): Flow<`[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html)`>`

builts a request, sends it to the server, awaits the response (async), creates a flow of it and attaches the defined errorHandler

### Parameters

`url` - function do derive the url (so you can use baseUrl)

`init` - an instance of [RequestInit](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-request-init/index.html) defining the attributes of the request