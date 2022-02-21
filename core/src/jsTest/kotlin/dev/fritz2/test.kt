package dev.fritz2

import dev.fritz2.core.mountSimple
import dev.fritz2.remote.Request
import dev.fritz2.remote.http
import kotlinx.browser.document
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

fun <T> runTest(block: suspend () -> T): dynamic = MainScope().promise {
    delay(50)
    block()
    delay(50)
}

fun initDocument() {
    document.clear()
    document.write("""<body>Loading...</body>""")
}

fun <T> checkSingleFlow(
    done: CompletableDeferred<Boolean> = CompletableDeferred(),
    upstream: Flow<T>,
    check: (Int, T) -> Boolean
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