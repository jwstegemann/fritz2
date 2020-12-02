[core](../../index.md) / [dev.fritz2.remote](../index.md) / [Request](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

(js) `Request(baseUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", headers: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`> = emptyMap(), body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = undefined, referrer: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = undefined, referrerPolicy: dynamic = undefined, mode: `[`RequestMode`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-request-mode/index.html)`? = undefined, credentials: `[`RequestCredentials`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-request-credentials/index.html)`? = undefined, cache: `[`RequestCache`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-request-cache/index.html)`? = undefined, redirect: `[`RequestRedirect`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-request-redirect/index.html)`? = undefined, integrity: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = undefined, keepalive: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = undefined, reqWindow: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`? = undefined)`

Represents the common fields an attributes of a given set of http requests.

Use it to define common headers, error-handling, base url, etc. for a specific API for example.
By calling one of the executing methods like [get](get.md) or [post](post.md) a specific request is built from the template and send to the server.

