package dev.fritz2.remote

import org.khronos.webgl.ArrayBuffer
import org.w3c.fetch.*
import org.w3c.files.Blob
import org.w3c.xhr.FormData

/**
 * Represents the functions needed to authenticate a user and in which cases the authentication should be made.
 * */
interface Authentication {

    /**
     * List of HTTP-Status-Codes forcing an authentication.
     * Defaults are 401 (unauthorized) and 403 (forbidden)
     */
    val errorcodesEnforcingAuthentication: List<Short>
        get() = listOf(401, 403)

    /**
     * function enriching the request with authentication information depending on the
     * servers need. For example the server could expect sepcial header-information, that could be
     * set by this function.
     *
     * @param request the request-object that is enriched with the login-information.
     */
    suspend fun enrichRequest(request: Request): Request

    /**
     * function doing the authentication
     */
    suspend fun authenticate()

    /**
     * shows whether there is a successful authentication or not.
     */
    fun isAuthenticated(): Boolean

    /**
     * performing a logout
     */
    suspend fun logout()
}

/**
 * creates a new [AuthenticatedRequest]
 *
 * @param baseUrl the common base of all urls that you want to call using the template
 * @param authentication instance of [Authentication]-Interface to enrich a [Request] with authentication-information
 */
fun http(baseUrl: String = "", authentication: Authentication) = AuthenticatedRequest(authentication, http(baseUrl))

/**
 * Represents the common fields and attributes for a given set of http requests.
 *
 * In comparison with the [Request]-object the [AuthenticatedRequest] contains information
 * about authorization necessary when performing a [Request]
 *
 * Use this class similarly to the [Request]-class enriched with authentication-information.
 *
 * @property request the Request-object containing the common base of all urls that you want to call using this template
 * @property authentication [Authentication]-object containing the information how and in which cases the authentication
 * has to be made
 */
class AuthenticatedRequest(private val authentication: Authentication, private val request: Request ) {

    /**
    * enumeration holding all remote-method-names of the [Request]
    *
    */
    enum class MethodName {
        GET, HEAD, CONNECT, OPTIONS, DELETE, POST, PUT, PATCH,
    }

    /**
    * delegation to [Request.get]
    *
    * @param subUrl see  [Request.get]
    */
    suspend fun get(subUrl: String = "") = delegateFktCall(MethodName.GET, subUrl)

    /**
    * delegation to [Request.head]
    *
    * @param subUrl see [Request.head]
    */
    suspend fun head(subUrl: String = "") = delegateFktCall(MethodName.HEAD, subUrl)

    /**
    * delegation to [Request.connect]
    *
    * @param subUrl see [Request.connect]
    */
    suspend fun connect(subUrl: String = "") = delegateFktCall(MethodName.CONNECT, subUrl)

    /**
    * delegation to [Request.options]
    *
    * @param subUrl see [Request.options]
    */
    suspend fun options(subUrl: String = "") = delegateFktCall(MethodName.OPTIONS, subUrl)

    /**
    * delegation to [Request.delete]
    *
    * @param subUrl see [Request.delete]
    */
    suspend fun delete(subUrl: String = "") = delegateFktCall(MethodName.DELETE, subUrl)

    /**
    * delegation to [Request.post]
    *
    * @param subUrl see [Request.post]
    */
    suspend fun post(subUrl: String = "") = delegateFktCall(MethodName.POST, subUrl)

    /**
    * delegation to [Request.put]
    *
    * @param subUrl see [Request.put]
    */
    suspend fun put(subUrl: String = "") = delegateFktCall(MethodName.PUT, subUrl)

    /**
    * delegation to [Request.patch]
    *
    * @param subUrl see [Request.patch]
    */
    suspend fun patch(subUrl: String = "") = delegateFktCall(MethodName.PATCH, subUrl)

    /**
    * internal method doing the delegation to [Request]
    *
    * @param methodName name of type [MethodName] for the function to call in [Request]
    * @param request object on which to call the function
    * @param subUrl endpoint url which is  appended to the [Request.baseUrl] with `/`
    */
    private suspend fun getResponse(methodName: MethodName, request: Request, subUrl: String): Response {
        return when(methodName) {
            MethodName.GET -> request.get(subUrl)
            MethodName.HEAD -> request.head(subUrl)
            MethodName.CONNECT -> request.connect(subUrl)
            MethodName.OPTIONS -> request.options(subUrl)
            MethodName.DELETE -> request.delete(subUrl)
            MethodName.POST -> request.post(subUrl)
            MethodName.PUT -> request.put(subUrl)
            MethodName.PATCH -> request.patch(subUrl)
        }
    }

    /**
     * delegate of function call which is using [Authentication] to authenticate when one
     * of the [Authentication.errorcodesEnforcingAuthentication] is given in the
     * server-response
     *
     * @param methodName name of type [MethodName] for the function to call in [Request]
     * @param subUrl endpoint url which is  appended to the [Request.baseUrl] with `/`
     */
    private suspend fun delegateFktCall(methodName: MethodName, subUrl: String): Response {
        return try {
                getResponse(methodName, authentication.enrichRequest(request), subUrl)
            } catch (fex: FetchException) {
                if (authentication.errorcodesEnforcingAuthentication.contains(fex.statusCode)) {
                    authentication.authenticate()
                    getResponse(methodName, authentication.enrichRequest(request), subUrl)
                } else {
                    throw fex
                }
            }
    }

    /**
     * delegation to [Request.append]
     *
     * @param subUrl see [Request.append]
     */
    fun append(subUrl: String): AuthenticatedRequest {
        val newRequest = request.append(subUrl)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.body]
     *
     * @param content see [Request.body]
     */
    fun body(content: String): AuthenticatedRequest {
        val newRequest = request.body(content)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.arrayBuffer]
     *
     * @param content see [Request.arrayBuffer]
     */
    fun arrayBuffer(content: ArrayBuffer):  AuthenticatedRequest {
        val newRequest = request.arrayBuffer(content)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.formData]
     *
     * @param content see [Request.formData]
     */
    fun formData(content: FormData): AuthenticatedRequest {
        val newRequest = request.formData(content)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.blob]
     *
     * @param content see [Request.blob]
     */
    fun blob(content: Blob): AuthenticatedRequest {
        val newRequest = request.blob(content)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.header]
     *
     * @param name see [Request.header]
     * @param value see [Request.header]
     */
    fun header(name: String, value: String): AuthenticatedRequest {
        val newRequest = request.header(name, value)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.contentType]
     *
     * @param value see [Request.contentType]
     */
    fun contentType(value: String): AuthenticatedRequest {
        val newRequest = request.contentType(value)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.basicAuth]
     *
     * @param username see [Request.basicAuth]
     * @param password see [Request.basicAuth]
     */
    fun basicAuth(username: String, password: String): AuthenticatedRequest {
        val newRequest = request.basicAuth(username, password)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.cacheControl]
     *
     * @param value see [Request.cacheControl]
     */
    fun cacheControl(value: String): AuthenticatedRequest {
        val newRequest = request.cacheControl(value)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.accept]
     *
     * @param value see [Request.accept]
     */
    fun accept(value: String): AuthenticatedRequest {
        val newRequest = request.accept(value)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.acceptJson]
     */
    fun acceptJson(): AuthenticatedRequest {
        val newRequest = request.acceptJson()
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.referrer]
     *
     * @param value see [Request.referrer]
     */
    fun referrer(value: String): AuthenticatedRequest {
        val newRequest = request.referrer(value)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.referrerPolicy]
     *
     * @param value see [Request.referrerPolicy]
     */
    fun referrerPolicy(value: dynamic): AuthenticatedRequest {
        val newRequest = request.referrerPolicy(value)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.requestMode]
     *
     * @param value see [Request.requestMode]
     */
    fun requestMode(value: RequestMode): AuthenticatedRequest {
        val newRequest = request.requestMode(value)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.credentials]
     *
     * @param value see [Request.credentials]
     */
    fun credentials(value: RequestCredentials): AuthenticatedRequest {
        val newRequest = request.credentials(value)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.cache]
     *
     * @param value see [Request.cache]
     */
    fun cache(value: RequestCache): AuthenticatedRequest {
        val newRequest = request.cache(value)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.redirect]
     *
     * @param value see [Request.redirect]
     */
    fun redirect(value: RequestRedirect): AuthenticatedRequest {
        val newRequest = request.redirect(value)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.integrity]
     *
     * @param value see [Request.integrity]
     */
    fun integrity(value: String) : AuthenticatedRequest {
        val newRequest = request.integrity(value)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.keepalive]
     *
     * @param value see [Request.keepalive]
     */
    fun keepalive(value: Boolean): AuthenticatedRequest {
        val newRequest = request.keepalive(value)
        return AuthenticatedRequest(authentication, newRequest)
    }

    /**
     * delegation to [Request.reqWindow]
     *
     * @param value see [Request.reqWindow]
     */
    fun reqWindow(value: Any) : AuthenticatedRequest {
        val newRequest = request.reqWindow(value)
        return AuthenticatedRequest(authentication, newRequest)
    }

}