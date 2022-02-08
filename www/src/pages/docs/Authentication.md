---
layout: layouts/docsWithContentNav.njk
title: Authentication
permalink: /docs/authentication/
eleventyNavigation:
    key: authentication
    parent: documentation
    title: Authentication
    order: 112
---

In fritz2, you want to implement the authentication process of your SPA as a `Middleware` for its remote-API. To do
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

Whenever the authentication middleware receives such a response, it starts the client-side authentication-process 
you defined by implementing the abstract `authenticate`-method. You are free to do here whatever your authentication 
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
            // show some error-message
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
        }
        else request
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

If you have to get the current principal at a given point in time, you can do so using the `current`-property of your Authentication.

If the first request requires authentication, subsequent requests that use the same authentication middleware 
will wait for the started authentication process to finish. So make sure you always complete or cancel it and use 
a fresh endpoint within for remote requests required (login, get roles, etc.).





