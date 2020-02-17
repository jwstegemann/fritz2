[fritz2](../../index.md) / [io.fritz2.remote](../index.md) / [RequestTemplate](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`RequestTemplate(baseUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", errorHandler: (`[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = loggingErrorHandler)`

Represents the common fields an attributes of a given set of http requests.

Use it to define common headers, error-handling, base url, etc. for a specific API for example.
By calling one of the executing methods like [get](get.md) or [post](post.md) a specific request is built from the template and send to the server.

