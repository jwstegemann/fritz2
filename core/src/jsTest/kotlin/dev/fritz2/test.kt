package dev.fritz2

import dev.fritz2.core.WithJob
import dev.fritz2.core.mountSimple
import dev.fritz2.remote.Request
import dev.fritz2.remote.http
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

fun <T> runTest(block: suspend WithJob.() -> T): dynamic = MainScope().promise {
    delay(50)
    block(object : WithJob {
        override val job: Job = Job()
    })
    delay(50)
}

fun <T> checkSingleFlow(
    done: CompletableDeferred<Boolean> = CompletableDeferred(),
    upstream: Flow<T>,
    check: (Int, T) -> Boolean,
) {
    var count = 0
    mountSimple(Job(), upstream) { value ->
        val result = check(count, value)
        count++
        if (result) done.complete(true)
    }
}

typealias Endpoint = String

const val testEndpoint: Endpoint = "test"
const val restEndpoint: Endpoint = "rest"
const val authenticatedEndpoint: Endpoint = "authenticated"

suspend fun testHttpServer(endpoint: Endpoint): Request {
    val r = http("http://localhost:3000/$endpoint")
    if (endpoint == restEndpoint) r.get("clear")
    return r
}
