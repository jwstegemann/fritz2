package io.fritz2.remote

import kotlinx.coroutines.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.w3c.fetch.*

/**
 * exception type for handling http exceptions
 *
 * @property statusCode the http response status code
 * @property body the body of the error-response
 */
class FetchException(val statusCode: Short, val body: String, val response: Response) : Throwable()

private val loggingErrorHandler = { t: Throwable ->
    when (t) {
        is FetchException -> {
            console.error("error on request @ ${t.response.url}: ${t.statusCode} - ${t.body}")
        }
        else -> {
            console.error("error on request: ${t.message}")
        }
    }
}

/**
 * factory method to create a RequestTemplate
 *
 * @property baseUrl the common base of all urls that you want to call using the template
 */
fun remote(baseUrl: String = "") = RequestTemplate(baseUrl)

/**
 * Represents the common fields an attributes of a given set of http requests.
 *
 * Use it to define common headers, error-handling, base url, etc. for a specific API for example.
 * By calling one of the executing methods like [get] or [post] a specific request is built from the template and send to the server.
 *
 * @property baseUrl the common base of all urls that you want to call using this template
 * @property errorHandler a common error handler for all requests you will send using this template. By default this just logs the error to the console.
 */
class RequestTemplate(val baseUrl: String = "") {
    private var method: String? = undefined
    private var headers: Headers? = undefined
    private var body: String? = undefined
    private var referrer: String? = undefined
    private var referrerPolicy: dynamic = undefined
    private var mode: RequestMode? = undefined
    private var credentials: RequestCredentials? = undefined
    private var cache: RequestCache? = undefined
    private var redirect: RequestRedirect? = undefined
    private var integrity: String? = undefined
    private var keepalive: Boolean? = undefined
    private var window: Any? = undefined

    /**
     * builds a request, sends it to the server, awaits the response (async), creates a flow of it and attaches the defined errorHandler
     *
     * @param url function do derive the url (so you can use baseUrl)
     * @param init an instance of [RequestInit] defining the attributes of the request
     */
    private fun execute(url: String, init: RequestInit): Flow<Response> = flow {
        val response = kotlin.browser.window.fetch("$baseUrl/$url", init).await()

        if (response.ok) emit(response)
        else throw FetchException(response.status, response.text().await(), response)
    }

    /**
     * builds a [RequestInit] without a body from the template using [method]
     *
     * @param method the http method to use (GET, POST, etc.)
     */
    private fun buildInit(method: String) = RequestInit(
        method = method,
        headers = headers,
        referrer = referrer,
        referrerPolicy = referrerPolicy,
        mode = mode,
        credentials = credentials,
        cache = cache,
        redirect = redirect,
        integrity = integrity,
        keepalive = keepalive,
        window = window
    )

    /**
     * builds a [RequestInit] with a body from the template using [method]
     *
     * @param method the http method to use (GET, POST, etc.)
     * @param body content of the request
     * @param contentType content-type of the request body (default: application/json)
     */
    private fun buildInit(method: String, contentType: String = "application/json", body: String): RequestInit {
        addHeader("Content-Type", contentType)
        return RequestInit(
            method = method,
            headers = headers,
            referrer = referrer,
            referrerPolicy = referrerPolicy,
            mode = mode,
            credentials = credentials,
            cache = cache,
            redirect = redirect,
            integrity = integrity,
            keepalive = keepalive,
            window = window,
            body = body
        )
    }

    // Methods

    /**
     * issues a get request returning a flow of it's response
     *
     * @param url endpoint url which getting appended to the baseUrl with `/`
     */
    fun get(url: String = "") = execute(url, buildInit("GET"))

    /**
     * issues a head request returning a flow of it's response
     *
     * @param url endpoint url which getting appended to the baseUrl with `/`
     */
    fun head(url: String = "") = execute(url, buildInit("HEAD"))

    /**
     * issues a post request returning a flow of it's response
     *
     * @param url endpoint url which getting appended to the baseUrl with `/`
     * @param contentType content-type of the given body
     * @param body content to send in the body of the request
     */
    fun post(url: String = "", contentType: String = "application/json", body: String) =
        execute(url, buildInit("POST", contentType, body))

    /**
     * issues a put request returning a flow of it's response
     *
     * @param url endpoint url which getting appended to the baseUrl with `/`
     * @param contentType content-type of the given body
     * @param body content to send in the body of the request
     */
    fun put(url: String = "", contentType: String = "application/json", body: String) =
        execute(url, buildInit("PUT", contentType, body))

    /**
     * issues a delete request returning a flow of it's response
     *
     * @param url endpoint url which getting appended to the baseUrl with `/`
     * @param contentType content-type of the given body
     * @param body content to send in the body of the request
     */
    fun delete(url: String = "", contentType: String = "application/json", body: String? = null): Flow<Response> {
        return if(body != null) execute(url, buildInit("DELETE", contentType, body))
            else execute(url, buildInit("DELETE"))
    }

    /**
     * issues a connect request returning a flow of it's response
     *
     * @param url endpoint url which getting appended to the baseUrl with `/`
     */
    fun connect(url: String = "") = execute(url, buildInit("CONNECT"))

    /**
     * issues a options request returning a flow of it's response
     *
     * @param url endpoint url which getting appended to the baseUrl with `/`
     */
    fun options(url: String = "") = execute(url, buildInit("OPTIONS"))

    /**
     * issues a patch request returning a flow of it's response
     *
     * @param url endpoint url which getting appended to the baseUrl with `/`
     * @param contentType content-type of the given body
     * @param body content to send in the body of the request
     */
    fun patch(url: String = "", contentType: String = "application/json", body: String) = execute(url, buildInit("PATCH", contentType, body))

    // Headers

    /**
     * checks if a [Headers] object has been initialized, does so if not and attaches the given http header
     *
     * @param name name of the http header to add
     * @param value value of the header field
     */
    private fun addHeader(name: String, value: String): RequestTemplate = apply {
        if (headers == null || headers == undefined) headers = Headers()
        headers!!.append(name, value)
    }

    /**
     * adds the given http header to the request
     *
     * @param name name of the http header to add
     * @param value value of the header field
     */
    fun header(name: String, value: String) = addHeader(name, value)

    /**
     * adds the basic [Authorization](https://developer.mozilla.org/de/docs/Web/HTTP/Headers/Authorization)
     * header for the given username and password
     *
     * @param username name of the user
     * @param password password of the user
     */
    fun basicAuth(username: String, password: String) =
        addHeader("Authorization", "Basic ${btoa("$username:$password")}")

    /**
     * adds the given [Cache-Control](https://developer.mozilla.org/de/docs/Web/HTTP/Headers/Cache-Control)
     * value to the http headers
     *
     * @param value cache-control value
     */
    fun cacheControl(value: String) = addHeader("Cache-Control", value)

    /**
     * adds the given [Accept](https://developer.mozilla.org/de/docs/Web/HTTP/Headers/Accept)
     * value to the http headers, e.g "application/pdf"
     *
     * @param value media type to accept
     */
    fun accept(value: String) = addHeader("Accept", value)

    /**
     * adds a header to accept JSON as response
     */
    fun acceptJson() = accept("application/json")
}


// Response

/**
 * extracts the body from the given [Response]
 */
fun Flow<Response>.body() = this.map {
    it.text().await()
}

/**
 * defines, how to handle an error that occurred during a http request.
 *
 * @param handler function that describes, how to handle a thrown [FetchException]
 */
fun Flow<Response>.onError(handler: (FetchException) -> Unit) = this.catch {
    when (it) {
        is FetchException -> {
            handler(it)
        }
        else -> {
            loggingErrorHandler(it)
        }
    }
}

/**
 * adds a handler to log all exceptions that occur during a fetch action
 */
fun Flow<Response>.onErrorLog() = this.catch {
    loggingErrorHandler(it)
}

external fun btoa(decoded: String): String