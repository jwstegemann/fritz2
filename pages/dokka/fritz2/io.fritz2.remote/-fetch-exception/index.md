[fritz2](../../index.md) / [io.fritz2.remote](../index.md) / [FetchException](./index.md)

# FetchException

`class FetchException : `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)

exception type for handling http exceptions

### Constructors

| [&lt;init&gt;](-init-.md) | exception type for handling http exceptions`FetchException(statusCode: `[`Short`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-short/index.html)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, response: `[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html)`)` |

### Properties

| [body](body.md) | the body of the error-response`val body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [response](response.md) | `val response: `[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html) |
| [statusCode](status-code.md) | the http response status code`val statusCode: `[`Short`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-short/index.html) |

