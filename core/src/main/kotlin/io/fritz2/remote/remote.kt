package io.fritz2.remote

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.url.URL
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.browser.window


@ExperimentalCoroutinesApi
fun URL.get() = flow {
    emit(window.fetch(this@get, RequestInit(
        method = "GET"
    )).await())
}


fun URL.post(body: String)= flow {
    emit(window.fetch(this@post, RequestInit(
        method = "POST",
        body = body
    )).await())
}


//TODO: error-handling

fun Flow<Response>.body() = this.map {
    it.text().await()
}