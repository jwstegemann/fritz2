package dev.fritz2.remote

/**
 * interface to do interceptions at http calls.
 * It can modify each request and handles all responses in a specified way.
 */
interface Middleware {

    /**
     * enriches requests with additional information.
     * E.g. it could append special header-information, which are needed for authentication.
     *
     * @param request [Request] that gets enriched
     * @return enriched [Request]
     */
    suspend fun enrichRequest(request: Request): Request

    /**
     * handles responses.
     * E.g. it could handle specific status-codes and log error messages.
     *
     * @param response [Response] to handle
     * @return handled [Response]
     */
    suspend fun handleResponse(response: Response): Response

    /**
     * stops propagation of a response to the following [Middleware]s in the chain
     */
    fun Response.stopPropagation() = copy(propagate = false)
}
