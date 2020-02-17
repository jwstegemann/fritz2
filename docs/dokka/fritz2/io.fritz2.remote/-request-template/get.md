[fritz2](../../index.md) / [io.fritz2.remote](../index.md) / [RequestTemplate](index.md) / [get](./get.md)

# get

`@ExperimentalCoroutinesApi fun get(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = ""): Flow<`[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html)`>`

issues a get request returning a flow of it's response

### Parameters

`url` - function to derive the url (so you can use baseUrl or other (inherited) parameters