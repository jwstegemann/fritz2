package dev.fritz2.remote

import dev.fritz2.binding.storeOf
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Special [Interceptor] to use in at [http] API to provide an authentication
 * for every request. The type-parameter [P] represents the principal information,
 * which contains all login-information needed to authenticate against the external HTTP API.
 * The principal information is held inside and available as flow by calling [principal].
 * To use this [Interceptor] you need to implement the [addAuthentication] method, where you
 * get the principal information, when available, to append it to your requests.
 * To start the authentication process you also need to implement the [authenticate] method in which
 * you specify what is needed to authenticate the user (e.g. open up a login modal).
 * When your authentication process is done you have to call the [complete] function and set your
 * principal. Then all requests, that were made in the meantime, getting re-executed with the additional
 * authentication information provided by the [addAuthentication] method.
 * When the user logs out you have to call the [clear] function to clear all authentication information.
 */
abstract class Authentication<P> : Interceptor {

    private val principalStore = storeOf<P?>(null)

    private var state: CompletableDeferred<P>? = null

    final override suspend fun enrichRequest(request: Request): Request = addAuthentication(request, getPrincipal())

    final override suspend fun handleResponse(response: Response): Response =
        if (statusCodesEnforcingAuthentication.contains(response.status)) {
            if (state.let {it?.isActive} != true) {
                start()
            }
            getPrincipal()
            response.request.execute()
        } else response

    /**
     * List of HTTP-Status-Codes forcing an authentication.
     * Defaults are 401 (unauthorized) and 403 (forbidden).
     */
    open val statusCodesEnforcingAuthentication: Set<Int> = setOf(401, 403)

    /**
     * Adds the authentication information to all requests by using the given [principal].
     *
     * @param request [Request] to enrich
     * @param principal principle containing authentication information
     * @return enriched [Request]
     */
    abstract fun addAuthentication(request: Request, principal: P?): Request

    /**
     * Returns the current principal information.
     * When the principal is available it is returned.
     * When an authentication-process is running the function is waiting for the process to be finished.
     * Otherwise, it returns null if no authentication-process is running or no principal information is available.
     *
     * @return P principal information
     */
    suspend fun getPrincipal(): P? = state?.await()

    /**
     * handles the authentication process.
     * E.g. opens up a login-modal or brwosing to the login-page.
     */
    abstract fun authenticate()

    /**
     * starts the authentication process.
     */
    fun start() {
        if (state == null) {
            state = CompletableDeferred()
            authenticate()
        }
    }

    /**
     * completes the authentication by setting the principal
     *
     * @param principal principal to set
     */
    fun complete(principal: P) {
        if (state == null) throw IllegalStateException("there is no running authentication that can be completed!")
        else {
            state!!.complete(principal)
            principalStore.update(principal)
        }
    }

    /**
     * clears the current principal information.
     * Needed when performing a logout.
     */
    fun clear() {
        state = null
        principalStore.update(null)
    }

    /**
     * flow with the information whether the user is authenticated or not
     */
    val authenticated: Flow<Boolean> = principalStore.data.map { it != null }

    /**
     * flow with the principal information
     */
    val principal: Flow<P?> = principalStore.data
}