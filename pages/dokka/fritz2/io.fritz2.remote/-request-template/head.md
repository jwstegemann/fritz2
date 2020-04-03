[fritz2](../../index.md) / [io.fritz2.remote](../index.md) / [RequestTemplate](index.md) / [head](./head.md)

# head

`@ExperimentalCoroutinesApi fun head(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = ""): Flow<`[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html)`>`

issues a head request returning a flow of it's response

### Parameters

`url` - function to derive the url (so you can use baseUrl or other (inherited) parameters