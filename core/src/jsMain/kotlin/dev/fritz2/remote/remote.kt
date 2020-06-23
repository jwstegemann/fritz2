package dev.fritz2.remote

import kotlinx.coroutines.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.w3c.fetch.*
import kotlin.browser.window as browserWindow


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
fun remote(baseUrl: String = "") = Request(baseUrl = baseUrl)

/**
 * Represents the common fields an attributes of a given set of http requests.
 *
 * Use it to define common headers, error-handling, base url, etc. for a specific API for example.
 * By calling one of the executing methods like [get] or [post] a specific request is built from the template and send to the server.
 *
 * @property baseUrl the common base of all urls that you want to call using this template
 */
open class Request(
    private val baseUrl: String = "",
    private val headers: Map<String, String> = emptyMap(),
    private val body: String? = undefined,
    private val referrer: String? = undefined,
    private val referrerPolicy: dynamic = undefined,
    private val mode: RequestMode? = undefined,
    private val credentials: RequestCredentials? = undefined,
    private val cache: RequestCache? = undefined,
    private val redirect: RequestRedirect? = undefined,
    private val integrity: String? = undefined,
    private val keepalive: Boolean? = undefined,
    private val reqWindow: Any? = undefined
) {

    /**
     * builds a request, sends it to the server, awaits the response (async), creates a flow of it and attaches the defined errorHandler
     *
     * @param subUrl function do derive the url (so you can use baseUrl)
     * @param init an instance of [RequestInit] defining the attributes of the request
     */
    private fun execute(subUrl: String, init: RequestInit): Flow<Response> = flow {
        val url = "${baseUrl.trimEnd('/')}/${subUrl.trimStart('/')}"
        val response = browserWindow.fetch(url, init).await()

        if (response.ok) emit(response)
        else throw FetchException(response.status, response.text().await(), response)
    }

    /**
     * builds a [RequestInit] with a body from the template using [method]
     *
     * @param method the http method to use (GET, POST, etc.)
     */
    private fun buildInit(method: String): RequestInit {
        // Headers has no methods for reading key-value-pairs
        val reqHeader = Headers()
        for ((k,v) in headers) reqHeader.set(k,v)
        return RequestInit(
            method = method,
            body = body,
            headers = reqHeader,
            referrer = referrer,
            referrerPolicy = referrerPolicy,
            mode = mode,
            credentials = credentials,
            cache = cache,
            redirect = redirect,
            integrity = integrity,
            keepalive = keepalive,
            window = reqWindow
        )
    }

    // Methods

    /**
     * issues a get request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the baseUrl with `/`
     */
    fun get(subUrl: String = "") = execute(subUrl, buildInit("GET"))

    /**
     * issues a head request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the baseUrl with `/`
     */
    fun head(subUrl: String = "") = execute(subUrl, buildInit("HEAD"))

    /**
     * issues a connect request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the baseUrl with `/`
     */
    fun connect(subUrl: String = "") = execute(subUrl, buildInit("CONNECT"))

    /**
     * issues a options request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the baseUrl with `/`
     */
    fun options(subUrl: String = "") = execute(subUrl, buildInit("OPTIONS"))

    /**
     * issues a delete request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the baseUrl with `/`
     */
    open fun delete(subUrl: String = ""): Flow<Response> {
        return execute(subUrl, buildInit("DELETE"))
    }

    /**
     * issues a post request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the baseUrl with `/`
     */
    fun post(subUrl: String = ""): Flow<Response> {
        return execute(subUrl, buildInit("POST"))
    }

    /**
     * issues a put request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the baseUrl with `/`
     */
    fun put(subUrl: String = ""): Flow<Response> {
        return execute(subUrl, buildInit("PUT"))
    }

    /**
     * issues a patch request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the baseUrl with `/`
     */
    fun patch(subUrl: String = ""): Flow<Response> {
        return execute(subUrl, buildInit("PATCH"))
    }

    /**
     * sets the body content to the request
     *
     * @param content body as string
     */
    fun body(content: String) = Request(
        baseUrl, headers, content, referrer, referrerPolicy, mode,
        credentials, cache, redirect, integrity, keepalive, reqWindow
    )

    /**
     * adds the given http header to the request
     *
     * @param name name of the http header to add
     * @param value value of the header field
     */
    fun header(name: String, value: String) = Request(
        baseUrl, headers.plus(name to value), body, referrer, referrerPolicy, mode,
        credentials, cache, redirect, integrity, keepalive, reqWindow)

    /**
     * adds the given [Content-Type](https://developer.mozilla.org/de/docs/Web/HTTP/Headers/Content-Type)
     * value to the http headers
     *
     * @param value cache-control value
     */
    fun contentType(value: String) = header("Content-Type", value)

    /**
     * adds the basic [Authorization](https://developer.mozilla.org/de/docs/Web/HTTP/Headers/Authorization)
     * header for the given username and password
     *
     * @param username name of the user
     * @param password password of the user
     */
    fun basicAuth(username: String, password: String) =
        header("Authorization", "Basic ${btoa("$username:$password")}")

    /**
     * adds the given [Cache-Control](https://developer.mozilla.org/de/docs/Web/HTTP/Headers/Cache-Control)
     * value to the http headers
     *
     * @param value cache-control value
     */
    fun cacheControl(value: String) = header("Cache-Control", value)

    /**
     * adds the given [Accept](https://developer.mozilla.org/de/docs/Web/HTTP/Headers/Accept)
     * value to the http headers, e.g "application/pdf"
     *
     * @param value media type to accept
     */
    fun accept(value: String) = header("Accept", value)

    /**
     * adds a header to accept JSON as response
     */
    fun acceptJson() = accept("application/json")

    /**
     * sets the referrer property of the [Request]
     *
     * @param value of the property
     */
    fun referrer(value: String) = Request(
        baseUrl, headers, body, value, referrerPolicy, mode,
        credentials, cache, redirect, integrity, keepalive, reqWindow)

    /**
     * sets the referrerPolicy property of the [Request]
     *
     * @param value of the property
     */
    fun referrerPolicy(value: dynamic) = Request(
        baseUrl, headers, body, referrer, value, mode,
        credentials, cache, redirect, integrity, keepalive, reqWindow)

    /**
     * sets the requestMode property of the [Request]
     *
     * @param value of the property
     */
    fun requestMode(value: RequestMode) = Request(
        baseUrl, headers, body, referrer, referrerPolicy, value,
        credentials, cache, redirect, integrity, keepalive, reqWindow)

    /**
     * sets the credentials property of the [Request]
     *
     * @param value of the property
     */
    fun credentials(value: RequestCredentials) = Request(
        baseUrl, headers, body, referrer, referrerPolicy, mode,
        value, cache, redirect, integrity, keepalive, reqWindow)

    /**
     * sets the cache property of the [Request]
     *
     * @param value of the property
     */
    fun cache(value: RequestCache) = Request(
        baseUrl, headers, body, referrer, referrerPolicy, mode,
        credentials, value, redirect, integrity, keepalive, reqWindow)

    /**
     * sets the redirect property of the [Request]
     *
     * @param value of the property
     */
    fun redirect(value: RequestRedirect) = Request(
        baseUrl, headers, body, referrer, referrerPolicy, mode,
        credentials, cache, value, integrity, keepalive, reqWindow)

    /**
     * sets the integrity property of the [Request]
     *
     * @param value of the property
     */
    fun integrity(value: String) = Request(
        baseUrl, headers, body, referrer, referrerPolicy, mode,
        credentials, cache, redirect, value, keepalive, reqWindow)

    /**
     * sets the keepalive property of the [Request]
     *
     * @param value of the property
     */
    fun keepalive(value: Boolean) = Request(
        baseUrl, headers, body, referrer, referrerPolicy, mode,
        credentials, cache, redirect, integrity, value, reqWindow)

    /**
     * sets the reqWindow property of the [Request]
     *
     * @param value of the property
     */
    fun reqWindow(value: Any) = Request(
        baseUrl, headers, body, referrer, referrerPolicy, mode,
        credentials, cache, redirect, integrity, keepalive, value)
}

// Response

/**
 * extracts the body as string from the given [Response]
 */
fun Flow<Response>.body() = this.map {
    it.text().await()
}

/**
 * extracts the body as blob from the given [Response]
 */
fun Flow<Response>.blob() = this.map {
    it.blob().await()
}

/**
 * extracts the body as arrayBuffer from the given [Response]
 */
fun Flow<Response>.arrayBuffer() = this.map {
    it.arrayBuffer().await()
}

/**
 * extracts the body as formData from the given [Response]
 */
fun Flow<Response>.formData() = this.map {
    it.formData().await()
}

/**
 * extracts the body as json from the given [Response]
 */
fun Flow<Response>.json() = this.map {
    it.json().await()
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