---
title: Http
description: "Learn how to do HTTP calls to backends in fritz2"
layout: layouts/docs.njk
permalink: /docs/http/
eleventyNavigation:
    key: http
    parent: documentation
    title: Http
    order: 60
---

Using the browser's default fetch API can get quite tiresome, which is why fritz2 offers a small fluent api wrapper for it.

First, you create a `Request` which points to your endpoint url:
```kotlin
val swapiApi = http("https://swapi.dev/api").acceptJson().contentType("application/json")
```
The remote service offers some [convenience methods](https://www.fritz2.dev/api/core/dev.fritz2.remote/-request/index.html)
to configure your API calls, like the `acceptJson()` above, 
which simply adds the correct header to each request sent using the template.

Sending a request is pretty straightforward:
```kotlin
swapiApi.get("planets/$num").body()
```
`body()` returns the body of the response as a `String`. Alternatively you can use the following methods to get different results:
* `blob(): Blob`
* `arrayBuffer(): ArrayBuffer`
* `formData(): FormData`
* `json(): Any?`

If your request was not successful (`Response.ok` property returns `false` according to the
[fetch](https://developer.mozilla.org/en-US/docs/Web/API/Response/ok) API), a `FetchException` will be thrown.

The same works for `POST` and all other HTTP methods - just use different parameters for the body to send.

The remote service is primarily designed for use in your `Store`'s `Handler`s when 
exchanging data with the backend. 
Here is a short example which uses [https://github.com/Kotlin/kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)
for parsing the returning JSON:
```kotlin
val swapiStore = object : RootStore<String>("") {

    private val api = http("https://swapi.dev/api")
        .acceptJson()
        .contentType("application/json")

    val planetName = handle<Int> { _, num ->
        val resp = api.get("planets/$num")
        if (resp.ok)
            Json.parseToJsonElement(resp.body())
                .jsonObject["name"]?.jsonPrimitive?.content ?: throw NoSuchElementException()
        else throw IllegalArgumentException()
    }
}
``` 

You can use this `Handler` like any other to handle `Flow`s of actions:

```kotlin
render {
    label {
        +"Planet Name: "
        swapiStore.data.render {
            span {
                +it
            }
        }
    }
    flowOf(1) handledBy swapiStore.planetName
    // or just
    swapiStore.planetName(1)
}
```

To see a complete example of this, visit our 
[remote example](/examples/remote).

In the real world, instead of creating the JSON manually, better use 
[kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization).
Get inspired by our [repositories example](/examples/repositories)
and use our [repositories API](/docs/repositories).


You can easily set up your local webpack server to proxy services (avoid CORS, etc.) when developing locally in 
your `build.gradle.kts`:
```kotlin
kotlin {
    js(IR) {
        browser {
            runTask {
                devServer?.apply {
                    port = 9000
                    proxy?.apply {
                        put("/members", "http://localhost:8080")
                        put("/chat", mapOf(
                            "target" to "ws://localhost:8080",
                            "ws" to true
                        ))
                    }
                }
            }
        }
    }.binaries.executable()
}
```
You can find a working example in the [Ktor Chat](https://github.com/jamowei/fritz2-ktor-chat) project.

Want to do bidirectional communications? See [Websockets](/docs/websockets).

## Middleware

You can intercept calls made by the remote api, for example to implement cross-cutting concerns like logging,
generic error handling, etc.

To write your own `Middleware`, implement the following interface:

```kotlin
interface Middleware {
    suspend fun enrichRequest(request: Request): Request
    suspend fun handleResponse(response: Response): Response
}
```

Make a `Request` by passing a `Middleware` to its `use` method:

```kotlin
val myEndpoint = http("/myAPI").use(someMiddleware)
myEndpoint.get("some/Path").body()
```

`enrichRequest` is called before each `Request` you configured to use this `Middleware`. You can add additional headers, 
parameters, etc. here. `handleResponse` is called on each `Response`.

To implement a simple logging `Middleware`, you could write the following: 

```kotlin
val logging = object : Middleware {
    override suspend fun enrichRequest(request: Request): Request {
        console.log("doing request: $request")
        return request
    }

    override suspend fun handleResponse(response: Response): Response {
        console.log("getting response: $response")
        return response
    }
}

val myAPI = http("/myAPI").use(logging)
```

You can add multiple Middlewares in one row with .use(mw1, mw2, mw3). The `enrichRequest` functions will be called from 
left to right (mw1,mw2,mw3), the `handleResponse` functions from right to left (mw3, mw2, mw1). 

You can stop the processing of a Response by Middlewares further down the chain by returning `response.stopPropagation()`.

## Authentication

In fritz2, you want to implement the authentication process of your SPA as a `Middleware` for its remote API. To do
this conveniently, start by inheriting from

```kotlin
abstract class Authentication<P> : Middleware {

    abstract fun addAuthentication(request: Request, principal: P?): Request

    abstract fun authenticate()
    
}
```

fritz2's authentication allows you to specify the data type of the current authenticated user (principal).
This could be, for example:

```kotlin
// This class holds the information of the principal currently authenticated
@Lenses
@Serializable
data class Principal(val name: String, val roles: List<String> = emptyList()) {
    companion object
}
```

When you add this `Middleware` to your endpoint(s), it will intercept each response with status code unauthorized
(401) or forbidden (403). You can change this by overwriting

```kotlin
override val statusCodesEnforcingAuthentication: List<Int> = listOf(401, 403, /* some more */)
```

Whenever the authentication middleware receives such a response, it starts the client-side authentication process
you defined by implementing the abstract `authenticate` method. You are free to do here whatever your authentication
process requires. For example, you could open a modal window to ask the user for their credentials and send them
to another remote service to get a [JSON Web Token](https://jwt.io/) for subsequent requests, as well as name and
roles of the user. To successfully complete the authentication process with an identified principal,
just call `complete(someValidPrincipal)`. To cancel the running authentication process, call `clear()`.

```kotlin
// This class holds the information entered in your login form
@Lenses
@Serializable
data class Credentials(val name: String = "", val password: String = "") {
    companion object
}

object MyAuthentication : Authentication<Principal>() {
    val loginStore = storeOf(Credentials())
    
    val login = loginStore.handle {
        val form = FormData()
        form.set("username", it.name)
        form.set("password", it.password)
        try {
            val principal = Json.decodeFromString<Principal>(http("/login").formData(form).post().body())
            complete(principal)
            closeTheLoginModal() // example
            Credentials() // clear the input form
        } catch (e: Exception) {
            // show some error message
            it
        }
    }

    override fun authenticate() {
        createSomeModal { // example
            input {
                loginStore.sub(Credentials.name()).let {
                    value(it.data)
                    changes.values() handledBy it.update
                }
                placeholder("login")
            }

            input {
                loginStore.sub(Credentials.password()).let {
                    value(it.data)
                    changes.values() handledBy it.update
                }
                placeholder("password")
            }
            
            button {
                +"login"
                clicks handledBy login
            }

        }
    }
}
```

Now you can use your principal's data to enrich each subsequent request by adding a token as a header, for example:

```kotlin
object MyAuthentication : Authentication<Principal>() {
    
    //...
    
    override fun addAuthentication(request: Request, principal: Principal?): Request =
        if (principal != null) {
            request.header("Authorization", "Bearer ${principal.token}")
        } else request
}
```

You can also access the principal to control your user interface:

```kotlin
val vip = MyAuthentication.data.map { it?.roles?.contains("SomeVipRole") ?: false }

MyAuthentication.authenticated.render {
    if (!it) {
        button {
            + "login"
            
            clicks handledBy {
                MyAuthentication.start() // start the authentication process manually
            }
        }
    }
    else {
        button {
            +"logout "
            MyAuthentication.data.map { it?.name ?: "" }.renderText()

            clicks handledBy {
                MyAuthentication.clear() // logout, but in real life you would want to inform the backend
            }
        }

        button {
            + "only for VIP"
            disabled(vip.map {!it} )
        }
    }
    
    //....
    
}
```

If you have to get the current principal at a given point in time, you can do so using the `current` property of your Authentication.

If the first request requires authentication, subsequent requests that use the same authentication middleware
will wait for the started authentication process to finish. So make sure you always complete or cancel it and use
a fresh endpoint within for remote requests required (login, get roles, etc.).