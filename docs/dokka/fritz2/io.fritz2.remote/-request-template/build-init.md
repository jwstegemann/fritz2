[fritz2](../../index.md) / [io.fritz2.remote](../index.md) / [RequestTemplate](index.md) / [buildInit](./build-init.md)

# buildInit

`fun buildInit(method: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`RequestInit`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-request-init/index.html)

builds a [RequestInit](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-request-init/index.html) without a body from the template using [method](build-init.md#io.fritz2.remote.RequestTemplate$buildInit(kotlin.String)/method)

### Parameters

`method` - the http method to use (GET, POST, etc.)`fun buildInit(method: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`RequestInit`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-request-init/index.html)

builds a [RequestInit](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-request-init/index.html) with a body from the template using [method](build-init.md#io.fritz2.remote.RequestTemplate$buildInit(kotlin.String, kotlin.String)/method)

### Parameters

`method` - the http method to use (GET, POST, etc.)

`body` - content of the request