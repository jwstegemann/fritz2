package dev.fritz2.remote

import kotlinx.coroutines.await
import org.khronos.webgl.ArrayBuffer
import org.w3c.dom.url.URLSearchParams
import org.w3c.fetch.*
import org.w3c.files.Blob
import org.w3c.xhr.FormData
import kotlinx.browser.window as browserWindow
import org.w3c.fetch.Response as FetchResponse

external fun btoa(decoded: String): String

/**
 * Represents the common fields and attributes of an HTTP response.
 * It contains also the original [Request] which was made to get this [Response].
 *
 * More info at [MDN](https://developer.mozilla.org/en-US/docs/Web/API/Response)
 */
open class Response(
    private val response: FetchResponse,
    val request: Request,
    val propagate: Boolean = true
) {
    /**
     * A boolean indicating whether the response was successful (status in the range 200 – 299) or not.
     */
    val ok: Boolean = response.ok

    /**
     * The status code of the response. (This will be 200 for a success).
     */
    val status: Int = response.status.toInt()

    /**
     * The status message corresponding to the status code. (e.g., OK for 200).
     */
    val statusText: String = response.statusText

    /**
     * The URL of the response.
     */
    val url: String = response.url

    /**
     * The type of the response (e.g., basic, cors).
     */
    val type: ResponseType = response.type

    /**
     * Indicates whether or not the response is the result of a redirect (that is, its URL list has more than one entry).
     */
    val redirected: Boolean = response.redirected

    /**
     * returns the [Headers] from the given [Response]
     */
    val headers = response.headers

    /**
     * extracts the body as string from the given [Response]
     */
    suspend fun body() = response.text().await()

    /**
     * extracts the body as blob from the given [Response]
     */
    suspend fun blob() = response.blob().await()

    /**
     * extracts the body as arrayBuffer from the given [Response]
     */
    suspend fun arrayBuffer() = response.arrayBuffer().await()

    /**
     * extracts the body as formData from the given [Response]
     */
    suspend fun formData() = response.formData().await()

    /**
     * extracts the body as json from the given [Response]
     */
    suspend fun json() = response.json().await()

    /**
     * creates a copy of the [Response].
     */
    fun copy(
        response: FetchResponse = this.response,
        request: Request = this.request,
        propagate: Boolean = this.propagate
    ) = Response(response, request, propagate)
}

/**
 * Represents the common fields and attributes of an HTTP request.
 *
 * Use it to define common headers, error-handling, base url, etc. for a specific API for example.
 * By calling one of the executing methods like [get] or [post] a specific request is built from
 * the template and send to the server.
 */
open class Request(
    val url: String = "",
    val method: String = "",
    val headers: Map<String, String> = emptyMap(),
    val body: dynamic = undefined,
    val referrer: String? = undefined,
    val referrerPolicy: dynamic = undefined,
    val mode: RequestMode? = undefined,
    val credentials: RequestCredentials? = undefined,
    val cache: RequestCache? = undefined,
    val redirect: RequestRedirect? = undefined,
    val integrity: String? = undefined,
    val keepalive: Boolean? = undefined,
    val reqWindow: Any? = undefined,
    val middlewares: List<Middleware> = emptyList(),
) {

    /**
     * creates a copy of the [Request].
     */
    open fun copy(
        url: String = this.url,
        method: String = this.method,
        headers: Map<String, String> = this.headers,
        body: dynamic = this.body,
        referrer: String? = this.referrer,
        referrerPolicy: dynamic = this.referrerPolicy,
        mode: RequestMode? = this.mode,
        credentials: RequestCredentials? = this.credentials,
        cache: RequestCache? = this.cache,
        redirect: RequestRedirect? = this.redirect,
        integrity: String? = this.integrity,
        keepalive: Boolean? = this.keepalive,
        reqWindow: Any? = this.reqWindow,
        middlewares: List<Middleware> = this.middlewares,
    ) = Request(
        url, method, headers, body, referrer, referrerPolicy,
        mode, credentials, cache, redirect, integrity, keepalive,
        reqWindow, middlewares
    )

    /**
     * executes the HTTP call and sends it to the server, awaits the response (async) and returns a [Response].
     */
    suspend fun execute(): Response {

        var request = this
        for (interceptor in middlewares) request = interceptor.enrichRequest(request)

        val init = request.buildInit()

        var response = Response(browserWindow.fetch(url, init).await(), request)
        for (interceptor in middlewares.reversed()) {
            if (!response.propagate) break
            response = interceptor.handleResponse(response)
        }

        return response
    }


    /**
     * builds a [RequestInit] object.
     */
    private fun buildInit(): RequestInit {
        // Headers class has no methods for reading key-value-pairs
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

    /**
     * issues a get request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [url] with `/`
     * @param parameters query parameters which are encoded and appended to the [url]
     */
    suspend fun get(subUrl: String? = null, parameters: Map<String, String>? = null): Response =
        (subUrl?.let { append(it) } ?: this)
            .run { parameters?.let { queryParameters(parameters) } ?: this }
            .copy(method = "GET").execute()

    /**
     * issues a head request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [url] with `/`
     */
    suspend fun head(subUrl: String? = null): Response =
        (subUrl?.let { append(it) } ?: this).copy(method = "HEAD").execute()

    /**
     * issues a connect request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [url] with `/`
     */
    suspend fun connect(subUrl: String? = null): Response =
        (subUrl?.let { append(it) } ?: this).copy(method = "CONNECT").execute()

    /**
     * issues a options request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [url] with `/`
     */
    suspend fun options(subUrl: String? = null): Response =
        (subUrl?.let { append(it) } ?: this).copy(method = "OPTIONS").execute()

    /**
     * issues a delete request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [url] with `/`
     */
    open suspend fun delete(subUrl: String? = null): Response =
        (subUrl?.let { append(it) } ?: this).copy(method = "DELETE").execute()

    /**
     * issues a post request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [url] with `/`
     */
    suspend fun post(subUrl: String? = null): Response =
        (subUrl?.let { append(it) } ?: this).copy(method = "POST").execute()

    /**
     * issues a put request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [url] with `/`
     */
    suspend fun put(subUrl: String? = null): Response =
        (subUrl?.let { append(it) } ?: this).copy(method = "PUT").execute()


    /**
     * issues a patch request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [url] with `/`
     */
    suspend fun patch(subUrl: String? = null): Response =
        (subUrl?.let { append(it) } ?: this).copy(method = "PATCH").execute()

    /**
     * appends the given [subUrl] to the [url]
     *
     * @param subUrl url which getting appended to the [url] with `/`
     */
    fun append(subUrl: String): Request = copy(url = "${url.trimEnd('/')}/${subUrl.trimStart('/')}")

    /**
     * appends the given [parameters] to the [url]
     *
     * @param parameters set of key-value pairs to be added
     */
    fun queryParameters(parameters: Map<String, String>): Request {
        val params = URLSearchParams()
        parameters.forEach { (k, v) -> params.append(k, v) }
        return this.copy(url = "$url?$params")
    }

    /**
     * sets the body content to the request
     *
     * @param content body as [String]
     */
    fun body(content: String): Request = copy(body = content)

    /**
     * sets the [ArrayBuffer] content to the request
     *
     * @param content body as [ArrayBuffer]
     */
    fun arrayBuffer(content: ArrayBuffer): Request = copy(body = content)

    /**
     * sets the [FormData] content to the request
     *
     * @param content body as [FormData]
     */
    fun formData(content: FormData): Request = copy(body = content)

    /**
     * sets the [Blob] content to the request
     *
     * @param content body as [Blob]
     */
    fun blob(content: Blob): Request = copy(body = content)

    /**
     * adds the given http header to the request
     *
     * @param name name of the http header to add
     * @param value value of the header field
     */
    fun header(name: String, value: String): Request = copy(headers = headers + (name to value))

    /**
     * adds the given [Content-Type](https://developer.mozilla.org/en/docs/Web/HTTP/Headers/Content-Type)
     * value to the http headers
     *
     * @param value cache-control value
     */
    fun contentType(value: String): Request = header("Content-Type", value)

    /**
     * adds the basic [Authorization](https://developer.mozilla.org/en/docs/Web/HTTP/Headers/Authorization)
     * header for the given username and password
     *
     * @param username name of the user
     * @param password password of the user
     */
    fun basicAuth(username: String, password: String): Request =
        header("Authorization", "Basic ${btoa("$username:$password")}")

    /**
     * adds the given [Cache-Control](https://developer.mozilla.org/en/docs/Web/HTTP/Headers/Cache-Control)
     * value to the http headers
     *
     * @param value cache-control value
     */
    fun cacheControl(value: String): Request = header("Cache-Control", value)

    /**
     * adds the given [Accept](https://developer.mozilla.org/en/docs/Web/HTTP/Headers/Accept)
     * value to the http headers, e.g "application/pdf"
     *
     * @param value media type to accept
     */
    fun accept(value: String): Request = header("Accept", value)

    /**
     * adds a header to accept JSON as response
     */
    fun acceptJson(): Request = accept("application/json")

    /**
     * sets the referrer property of the [Request]
     *
     * @param value of the property
     */
    fun referrer(value: String): Request = copy(referrer = value)

    /**
     * sets the referrerPolicy property of the [Request]
     *
     * @param value of the property
     */
    fun referrerPolicy(value: dynamic): Request = copy(referrerPolicy = value)

    /**
     * sets the requestMode property of the [Request]
     *
     * @param value of the property
     */
    fun requestMode(value: RequestMode): Request = copy(mode = value)

    /**
     * sets the credentials property of the [Request]
     *
     * @param value of the property
     */
    fun credentials(value: RequestCredentials): Request = copy(credentials = value)

    /**
     * sets the cache property of the [Request]
     *
     * @param value of the property
     */
    fun cache(value: RequestCache): Request = copy(cache = value)

    /**
     * sets the redirect property of the [Request]
     *
     * @param value of the property
     */
    fun redirect(value: RequestRedirect): Request = copy(redirect = value)

    /**
     * sets the integrity property of the [Request]
     *
     * @param value of the property
     */
    fun integrity(value: String): Request = copy(integrity = value)

    /**
     * sets the keepalive property of the [Request]
     *
     * @param value of the property
     */
    fun keepalive(value: Boolean): Request = copy(keepalive = value)

    /**
     * sets the reqWindow property of the [Request]
     *
     * @param value of the property
     */
    fun reqWindow(value: Any): Request = copy(reqWindow = value)

    /**
     * adds an [Middleware] to handle all requests and responses.
     *
     * @param middleware [Middleware] to use by this request
     */
    fun use(middleware: Middleware): Request = copy(middlewares = middlewares + middleware)

    /**
     * adds [Middleware]s to handle all requests and responses.
     *
     * @param middlewares [Middleware] to use by this request
     */
    fun use(vararg middlewares: Middleware): Request = copy(middlewares = this.middlewares + middlewares.asList())

}

/**
 * creates a new [Request]
 *
 * @param baseUrl the common base of all urls that you want to call using the template
 */
fun http(baseUrl: String = "") = Request(url = baseUrl)

