[fritz2](../../index.md) / [io.fritz2.remote](../index.md) / [RequestTemplate](./index.md)

# RequestTemplate

`class RequestTemplate`

Represents the common fields an attributes of a given set of http requests.

Use it to define common headers, error-handling, base url, etc. for a specific API for example.
By calling one of the executing methods like [get](get.md) or [post](post.md) a specific request is built from the template and send to the server.

### Constructors

| [&lt;init&gt;](-init-.md) | Represents the common fields an attributes of a given set of http requests.`RequestTemplate(baseUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "")` |

### Properties

| [baseUrl](base-url.md) | the common base of all urls that you want to call using this template`val baseUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| [accept](accept.md) | adds a header to accept a given media type (like application/pdf)`fun accept(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`RequestTemplate`](./index.md) |
| [acceptJson](accept-json.md) | adds a header to accept JSON as response`fun acceptJson(): `[`RequestTemplate`](./index.md) |
| [delete](delete.md) | issues a delete request returning a flow of it's response`fun delete(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = ""): Flow<`[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html)`>` |
| [execute](execute.md) | builds a request, sends it to the server, awaits the response (async), creates a flow of it and attaches the defined errorHandler`fun execute(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, init: `[`RequestInit`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-request-init/index.html)`): Flow<`[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html)`>` |
| [get](get.md) | issues a get request returning a flow of it's response`fun get(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = ""): Flow<`[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html)`>` |
| [head](head.md) | issues a head request returning a flow of it's response`fun head(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = ""): Flow<`[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html)`>` |
| [header](header.md) | adds the given http header to the request`fun header(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`RequestTemplate`](./index.md) |
| [patch](patch.md) | issues a patch request returning a flow of it's response`fun patch(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): Flow<`[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html)`>` |
| [post](post.md) | issues a post request returning a flow of it's response`fun post(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): Flow<`[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html)`>` |
| [push](push.md) | issues a push request returning a flow of it's response`fun push(url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", body: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): Flow<`[`Response`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.fetch/-response/index.html)`>` |

