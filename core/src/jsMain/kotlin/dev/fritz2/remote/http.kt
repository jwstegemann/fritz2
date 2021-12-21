package dev.fritz2.remote

import dev.fritz2.binding.Store
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.await
import org.khronos.webgl.ArrayBuffer
import org.w3c.fetch.*
import org.w3c.files.Blob
import org.w3c.xhr.FormData
import kotlinx.browser.window as browserWindow
import org.w3c.fetch.Response as FetchResponse


/**
 * [Exception] type for handling http exceptions
 *
 * @property statusCode the http response status code
 * @property body the body of the error-response
 */
class FetchException(val statusCode: Int, val body: String, val response: Response) : Exception(
    "code=$statusCode, url=${response.url}, body=$body"
)

/**
 * creates a new [Request]
 *
 * @param baseUrl the common base of all urls that you want to call using the template
 */
fun http(baseUrl: String = "") = Request(url = baseUrl)

interface RequestEnricher {
    suspend fun enrichRequest(request: Request): Request
}

interface ResponseInterceptor {
    suspend fun handleResponse(response: Response): Response
}

open class Response(
    private val response: FetchResponse,
    val request: Request
) {

    val ok: Boolean get() = response.ok

    val status: Int get() = response.status.toInt()

    val url: String get() = response.url

    val statusText: String get() = response.statusText

    /**
     * returns the [Headers] from the given [Response]
     */
    val headers get() = response.headers

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
}

/**
 * Represents the common fields an attributes of a given set of http requests.
 *
 * Use it to define common headers, error-handling, base url, etc. for a specific API for example.
 * By calling one of the executing methods like [get] or [post] a specific request is built from the template and send to the server.
 *
 * @property url the common base of all urls that you want to call using this template
 */
open class Request(
    private val method: String = "",
    private val url: String = "",
    private val headers: Map<String, String> = emptyMap(),
    private val body: dynamic = undefined,
    private val referrer: String? = undefined,
    private val referrerPolicy: dynamic = undefined,
    private val mode: RequestMode? = undefined,
    private val credentials: RequestCredentials? = undefined,
    private val cache: RequestCache? = undefined,
    private val redirect: RequestRedirect? = undefined,
    private val integrity: String? = undefined,
    private val keepalive: Boolean? = undefined,
    private val reqWindow: Any? = undefined,
    private val requestEnricher: RequestEnricher? = null,
    private val responseInterceptor: ResponseInterceptor? = null
) {

    open fun copy(
        method: String = this.method,
        url: String = this.url,
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
        requestEnrichers: RequestEnricher? = this.requestEnricher,
        responseInterceptor: ResponseInterceptor? = this.responseInterceptor
    ) = Request(
        method, url, headers, body, referrer, referrerPolicy,
        mode, credentials, cache, redirect, integrity, keepalive,
        reqWindow, requestEnrichers, responseInterceptor
    )

    /**
     * builds a request, sends it to the server, awaits the response (async), creates a flow of it.
     * When request failed a [FetchException] will be thrown.
     *
     * @throws FetchException when request failed
     */
    suspend fun execute(): Response {

        var request = this
        if(requestEnricher != null) request = requestEnricher.enrichRequest(request)

        val init = request.buildInit()

        var response = Response(browserWindow.fetch(url, init).await(), request)
        if(responseInterceptor != null) {
            response = responseInterceptor.handleResponse(response)
        }

//        if (authentication != null) {
//            if (authentication.errorcodesEnforcingAuthentication.contains(response.status)) {
//                //authentication.startAuthentication()
//                if (!authentication.isAuthRunning()) {
//                    authentication.startAuthentication()
//                }
//                // Wir warten aufs Login...
//                authentication.getPrincipal()
//                val redo = authentication.enrichRequest(this).buildInit(init.method!!)
//                response = browserWindow.fetch(url, redo).await()
//            }
//        }
        if (response.ok) return response
        else throw FetchException(response.status, response.body(), response)
    }


    /**
     * builds a [RequestInit] with a body from the template using [method]
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

    // Methods

    /**
     * issues a get request returning a flow of it's response
     *
     * @param subUrl endpoint url which getting appended to the [url] with `/`
     */
    suspend fun get(subUrl: String? = null): Response =
        (subUrl?.let { append(it) } ?: this).copy(method = "GET").execute()

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

    fun enrichRequest(enricher: RequestEnricher): Request = copy(requestEnrichers = enricher)

    fun interceptResponse(interceptor: ResponseInterceptor): Request = copy(responseInterceptor = interceptor)
}

external fun btoa(decoded: String): String

interface Authentication<P>: RequestEnricher, ResponseInterceptor, Store<P> {

    val statusCodesEnforcingAuthentication: List<Int>
        get() = listOf(401, 403)

    private fun http(baseUrl: String = ""): Request = dev.fritz2.remote.http(baseUrl)
        .enrichRequest(this).interceptResponse(this)

    fun CompletableDeferred<P>.authenticate()

    override suspend fun handleResponse(response: Response): Response {
        return if (statusCodesEnforcingAuthentication.contains(response.status)) {
            val state = CompletableDeferred<P>(job)
            state.authenticate()
            update(state.await())
            enrichRequest(response.request).execute()
        } else response
    }
}

///**
// * Represents the functions needed to authenticate a user
// * and in which cases the authentication should be made.
// * The typeparameter P represents a class for a principal,
// * an object containing all login-information needed by the
// * user of this api. The principal-object is held in a fritz2-store
// * and could be read as a flow [principal].
// * There are two functions reading whether the authentication is done,
// * [authenticated] delivers a flow of Boolean-type
// * [isAuthenticated] delivers the boolean itself.
// */
//abstract class Authentication<P> {
//
//    private val principalStore = storeOf<P?>(null)
//
//    private var state: CompletableDeferred<P>? = null
//
//    /**
//     * List of HTTP-Status-Codes forcing an authentication.
//     * Defaults are 401 (unauthorized) and 403 (forbidden)
//     */
//    open val errorcodesEnforcingAuthentication: List<Short>
//        get() = listOf(401, 403)
//
//    /**
//     * function enriching the request with authentication information depending on the
//     * servers need. For example the server could expect sepcial header-information, that could be
//     * set by this function.
//     *
//     * @param request the request-object that is enriched with the login-information.
//     */
//    abstract suspend fun enrichRequest(request: Request): Request
//
//    /**
//     * function doing the authentication
//     */
//    abstract fun authenticate()
//
//    /**
//     * function performing a logout
//     */
//    fun logout() {
//        state = null
//        principalStore.update(null)
//    }
//
//    internal fun startAuthentication() {
//        state = CompletableDeferred()
//        authenticate()
//    }
//
//    /**
//     * function returning a boolean showing whether the user is authenticated or not
//     */
//    fun isAuthenticated(): Boolean = state != null && !isAuthRunning()
//
//    /**
//     * function returning a flow with the information whether the user is authenticated or not
//     */
//    val authenticated: Flow<Boolean> = principalStore.data.map { it != null }
//
//    /**
//     * function returning a flow with the principal-information.
//     *
//     */
//    val principal = principalStore.data
//
//    /**
//     * function returning the information wether an authentication-process is running
//     */
//    internal fun isAuthRunning() = state.let {it?.isActive} ?: false
//
//    /**
//     * function returning the principal-information:
//     * if the prinicpal is available it is returned
//     * if an authentication-process is running  the function is waiting for the process to be finished
//     * else null is returned, if no authentication-process is running nor a principal is available.
//     */
//    suspend fun getPrincipal(): P? = state.let {it?.await()}
//
//    /**
//     * function offering the possibility to set the principal after a successfull authentication
//     */
//    fun login(p: P){
//
//        if(state==null) {
//            state = CompletableDeferred()
//        }
//        state?.complete(p)
//        principalStore.update(p)
//    }
//}