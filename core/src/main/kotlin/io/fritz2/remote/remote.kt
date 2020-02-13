package io.fritz2.remote

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.url.URL
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.browser.window



@ExperimentalCoroutinesApi
fun URL.get() = flow {
    val response = window.fetch(this@get, RequestInit(
        method = "GET"
    )).await()

    if (response.ok) emit(response)
    else throw FetchException(response.status, response.text().await())
}

fun URL.post(body: String)= flow {
    emit(window.fetch(this@post, RequestInit(
        method = "POST",
        body = body
    )).await())
}

fun Flow<Response>.body() = this.map {
    it.text().await()
}

class FetchException(val statusCode: Short, val body: String) : Exception()

fun Flow<Response>.onError(handler: (FetchException) -> Unit) = this.catch { e ->
    if (e is FetchException) handler(e)
    else throw e
}