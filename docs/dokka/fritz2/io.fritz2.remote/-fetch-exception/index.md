[fritz2](../../index.md) / [io.fritz2.remote](../index.md) / [FetchException](./index.md)

# FetchException

`class FetchException : `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)

exception type for handling http exceptions

### Constructors

| [&lt;init&gt;](-init-.md) | exception type for handling http exceptions`FetchException(statusCode: `[`Short`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-short/index.html)`, body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Properties

| [body](body.md) | the body of the error-response`val body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [statusCode](status-code.md) | the http reponse status code`val statusCode: `[`Short`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-short/index.html) |

