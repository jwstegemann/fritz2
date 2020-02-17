package io.fritz2.remote

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.w3c.fetch.*

private val loggingErrorHandler = {e: FetchException ->
    console.error("error fetching request: ${e.statusCode} responding ${e.body}")
}

/**
 * Repesents the common fields an attributes of a given set of http requests.
 *
 * Use it to define common headers, error-handling, base url, etc. for a specific API for example.
 * By calling one of the executing methods like [get] or [post] a specific request is built from the template and send to the server.
 *
 * @property baseUrl the common base of all urls that you want to call using this template
 * @property errorHandler a common error handler for all requests you will send using this template. By default this just logs the error to the console.
 */
class RequestTemplate(val baseUrl : String = "", val errorHandler: (FetchException) -> Unit = loggingErrorHandler) {
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
     * builts a request, sends it to the server, awaits the response (async), creates a flow of it and attaches the defined errorHandler
     *
     * @param url function do derive the url (so you can use baseUrl)
     * @param init an instance of [RequestInit] defining the attributes of the request
     */
    @ExperimentalCoroutinesApi
    inline fun execute(url: String, init: RequestInit): Flow<Response> = flow {
        val response = kotlin.browser.window.fetch("$baseUrl/$url", init).await()

        if (response.ok) emit(response)
        else throw FetchException(response.status, response.text().await())
    }.onError(errorHandler)

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
     */
    private fun buildInit(method: String, body: String) = RequestInit(
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

    /**
     * issues a get request returning a flow of it's response
     *
     * @param url function to derive the url (so you can use baseUrl or other (inherited) parameters
     */
    fun get(url: String = "") = execute(url, buildInit("GET"))

    /**
     * issues a delete request returning a flow of it's response
     *
     * @param url function to derive the url (so you can use baseUrl or other (inherited) parameters
     */
    fun delete(url: String = "") = execute(url, buildInit("DELETE"))

    /**
     * issues a head request returning a flow of it's response
     *
     * @param url function to derive the url (so you can use baseUrl or other (inherited) parameters
     */
    fun head(url: String = "") = execute(url, buildInit("HEAD"))

    /**
     * issues a post request returning a flow of it's response
     *
     * @param url function to derive the url (so you can use baseUrl or other (inherited) parameters
     * @param body content to send in the body of the request
     */
    fun post(url: String = "", body: String) = execute(url, buildInit("POST", body))

    /**
     * issues a push request returning a flow of it's response
     *
     * @param url function to derive the url (so you can use baseUrl or other (inherited) parameters
     * @param body content to send in the body of the request
     */
    fun push(url: String = "", body: String) = execute(url, buildInit("PUSH", body))

    /**
     * issues a patch request returning a flow of it's response
     *
     * @param url function to derive the url (so you can use baseUrl or other (inherited) parameters
     * @param body content to send in the body of the request
     */
    fun patch(url: String = "", body: String) = execute(url, buildInit("PUSH", body))

    /**
     * checks if a [Headers] obect has been initialized, does so if not and attaches the given http header
     *
     * @param name name of the http header to add
     * @param value value of the header field
     */
    private inline fun addHeader(name: String, value: String): RequestTemplate =  apply {
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
     * adds a header to accept a given media type (like application/pdf)
     *
     * @param value media type to accept
     */
    fun accept(value: String)= addHeader("Accept", value)

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
 * exception type for handling http exceptions
 *
 * @property statusCode the http reponse status code
 * @property body the body of the error-response
 */
class FetchException(val statusCode: Short, val body: String) : Exception()

/**
 * defines, how to handle an error that occured during a http request.
 *
 * @param handler function that describes, how to handle a thrown [FetchException]
 */
@ExperimentalCoroutinesApi
fun Flow<Response>.onError(handler: (FetchException) -> Unit) = this.catch { e ->
    if (e is FetchException) handler(e)
    else throw e
}