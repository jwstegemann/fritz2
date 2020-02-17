[fritz2](../../index.md) / [io.fritz2.remote](../index.md) / [RequestTemplate](index.md) / [push](./push.md)

# push

`inline fun push(crossinline url: `[`RequestTemplate`](index.md)`.() -> `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): Flow<`[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html)`>`

issues a push request returning a flow of it's response

### Parameters

`url` - function to derive the url (so you can use baseUrl or other (inherited) parameters

`body` - content to send in the body of the request