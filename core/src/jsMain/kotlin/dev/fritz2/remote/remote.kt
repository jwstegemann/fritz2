package dev.fritz2.remote

import kotlinx.coroutines.await
import org.w3c.fetch.*
import kotlin.browser.window as browserWindow


/**
 * exception type for handling http exceptions
 *
 * @property statusCode the http response status code
 * @property body the body of the error-response
 */
class FetchException(val statusCode: Short, val body: String, val response: Response) : Throwable()

private val loggingFetchException = { e: FetchException ->
    console.error("error on request @ ${e.response.url}: ${e.statusCode} - ${e.body}")
}

class ResponseResult(val response: Response? = null, val fetchException: FetchException? = null) {

    suspend fun getOrThrow(): Response = response ?: throw fetchException!!

    suspend fun isSuccess(): Boolean = response != null && fetchException == null

    suspend fun isFailure(): Boolean = response == null && fetchException != null

    suspend fun <X> mapOrElse(default: X, transform: suspend (Response) -> X): X =
        if (response != null) transform(response) else default

    suspend fun <X> mapOrCatch(transform: suspend (Response) -> X, handler: suspend (FetchException) -> X) =
        if (response != null) transform(response) else handler(fetchException!!)

    suspend fun onFailure(action: (FetchException) -> Unit): ResponseResult {
        if (fetchException != null) action(fetchException)
        return this
    }

    suspend fun onSuccess(action: (Response) -> Unit): ResponseResult {
        if (response != null) action(response)
        return this
    }
}

/**
 * creates a new [Request]
 *
 * @property baseUrl the common base of all urls that you want to call using the template
 */
fun remote(baseUrl: String = "") = Request(baseUrl = baseUrl)

/**
 * [Request] contains the common fields and attributes for making a http request.
 *
 * Use it to define common headers, base url, etc. for a specific API for example.
 * By calling one of the executing methods like [get] or [post] a specific request is built from the template and send to the server.
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
    private val reqWindow: Any? = undefined,
    private val errorHandler: (FetchException) -> Unit = {}
) {

    /**
     * builds a request, sends it to the server, awaits the response (async), creates a flow of it and attaches the defined errorHandler
     *
     * @param subUrl function do derive the url (so you can use baseUrl)
     * @param init an instance of [RequestInit] defining the attributes of the request
     */
    private suspend fun execute(
        subUrl: String,
        init: RequestInit,
        errorHandler: (FetchException) -> Unit
    ): ResponseResult {
        val url = "${baseUrl.trimEnd('/')}/${subUrl.trimStart('/')}"
        val response = browserWindow.fetch(url, init).await()
        return if (response.ok) return ResponseResult(response = response).onFailure(errorHandler)
        else ResponseResult(fetchException = FetchException(response.status, response.getBody(), response)).onFailure(
            errorHandler
        )
    }

    /**
     * builds a [RequestInit] with a body from the template using [method]
     *
     * @param method the http method to use (GET, POST, etc.)
     */
    private fun buildInit(method: String): RequestInit {
        // Headers has no methods for reading key-value-pairs
        val reqHeader = Headers()
        for ((k, v) in headers) reqHeader.set(k, v)
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
     * @param subUrl endpoint url which getting appended to the [baseUrl] with `/`
     */
    suspend fun get(subUrl: String = "") = execute(subUrl, buildInit("GET"), errorHandler)

    /**
     * issues a head request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [baseUrl] with `/`
     */
    suspend fun head(subUrl: String = "") = execute(subUrl, buildInit("HEAD"), errorHandler)

    /**
     * issues a connect request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [baseUrl] with `/`
     */
    suspend fun connect(subUrl: String = "") = execute(subUrl, buildInit("CONNECT"), errorHandler)

    /**
     * issues a options request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [baseUrl] with `/`
     */
    suspend fun options(subUrl: String = "") = execute(subUrl, buildInit("OPTIONS"), errorHandler)

    /**
     * issues a delete request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [baseUrl] with `/`
     */
    open suspend fun delete(subUrl: String = "") = execute(subUrl, buildInit("DELETE"), errorHandler)

    /**
     * issues a post request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [baseUrl] with `/`
     */
    suspend fun post(subUrl: String = "") = execute(subUrl, buildInit("POST"), errorHandler)

    /**
     * issues a put request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [baseUrl] with `/`
     */
    suspend fun put(subUrl: String = "") = execute(subUrl, buildInit("PUT"), errorHandler)


    /**
     * issues a patch request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [baseUrl] with `/`
     */
    suspend fun patch(subUrl: String = "") = execute(subUrl, buildInit("PATCH"), errorHandler)

    fun onFailure(handler: (FetchException) -> Unit): Request = Request(
        baseUrl, headers, body, referrer, referrerPolicy, mode,
        credentials, cache, redirect, integrity, keepalive, reqWindow, handler
    )

    /**
     * appends the given [subUrl] to the [baseUrl]
     *
     * @param subUrl url which getting appended to the [baseUrl] with `/`
     */
    fun append(subUrl: String) = Request(
        "${baseUrl.trimEnd('/')}/${subUrl.trimStart('/')}",
        headers, body, referrer, referrerPolicy, mode,
        credentials, cache, redirect, integrity, keepalive, reqWindow, errorHandler
    )

    /**
     * sets the body content to the request
     *
     * @param content body as string
     */
    fun body(content: String) = Request(
        baseUrl, headers, content, referrer, referrerPolicy, mode,
        credentials, cache, redirect, integrity, keepalive, reqWindow, errorHandler
    )

    /**
     * adds the given http header to the request
     *
     * @param name name of the http header to add
     * @param value value of the header field
     */
    fun header(name: String, value: String) = Request(
        baseUrl, headers.plus(name to value), body, referrer, referrerPolicy, mode,
        credentials, cache, redirect, integrity, keepalive, reqWindow, errorHandler
    )

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
        credentials, cache, redirect, integrity, keepalive, reqWindow, errorHandler
    )

    /**
     * sets the referrerPolicy property of the [Request]
     *
     * @param value of the property
     */
    fun referrerPolicy(value: dynamic) = Request(
        baseUrl, headers, body, referrer, value, mode,
        credentials, cache, redirect, integrity, keepalive, reqWindow, errorHandler
    )

    /**
     * sets the requestMode property of the [Request]
     *
     * @param value of the property
     */
    fun requestMode(value: RequestMode) = Request(
        baseUrl, headers, body, referrer, referrerPolicy, value,
        credentials, cache, redirect, integrity, keepalive, reqWindow, errorHandler
    )

    /**
     * sets the credentials property of the [Request]
     *
     * @param value of the property
     */
    fun credentials(value: RequestCredentials) = Request(
        baseUrl, headers, body, referrer, referrerPolicy, mode,
        value, cache, redirect, integrity, keepalive, reqWindow, errorHandler
    )

    /**
     * sets the cache property of the [Request]
     *
     * @param value of the property
     */
    fun cache(value: RequestCache) = Request(
        baseUrl, headers, body, referrer, referrerPolicy, mode,
        credentials, value, redirect, integrity, keepalive, reqWindow, errorHandler
    )

    /**
     * sets the redirect property of the [Request]
     *
     * @param value of the property
     */
    fun redirect(value: RequestRedirect) = Request(
        baseUrl, headers, body, referrer, referrerPolicy, mode,
        credentials, cache, value, integrity, keepalive, reqWindow, errorHandler
    )

    /**
     * sets the integrity property of the [Request]
     *
     * @param value of the property
     */
    fun integrity(value: String) = Request(
        baseUrl, headers, body, referrer, referrerPolicy, mode,
        credentials, cache, redirect, value, keepalive, reqWindow, errorHandler
    )

    /**
     * sets the keepalive property of the [Request]
     *
     * @param value of the property
     */
    fun keepalive(value: Boolean) = Request(
        baseUrl, headers, body, referrer, referrerPolicy, mode,
        credentials, cache, redirect, integrity, value, reqWindow, errorHandler
    )

    /**
     * sets the reqWindow property of the [Request]
     *
     * @param value of the property
     */
    fun reqWindow(value: Any) = Request(
        baseUrl, headers, body, referrer, referrerPolicy, mode,
        credentials, cache, redirect, integrity, keepalive, value, errorHandler
    )
}

// Response

/**
 * extracts the body as string from the given [Response]
 */
suspend fun Response.getBody() = this.text().await()

/**
 * extracts the [Headers] from the given [Response]
 */
suspend fun Response.getHeaders() = this.headers

/**
 * extracts the body as blob from the given [Response]
 */
suspend fun Response.getBlob() = this.blob().await()

/**
 * extracts the body as arrayBuffer from the given [Response]
 */
suspend fun Response.getArrayBuffer() = this.arrayBuffer().await()

/**
 * extracts the body as formData from the given [Response]
 */
suspend fun Response.getFormData() = this.formData().await()

/**
 * extracts the body as json from the given [Response]
 */
suspend fun Response.getJson() = json().await()

external fun btoa(decoded: String): String