package dev.fritz2.test

import dev.fritz2.binding.mountSingle
import dev.fritz2.remote.Request
import dev.fritz2.remote.http
import kotlinx.browser.document
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

fun <T> runTest(block: suspend () -> T): dynamic = GlobalScope.promise {
    delay(50)
    block()
    delay(50)
}

val targetId = "target"

fun initDocument() {
    document.clear()
    document.write(
        """
            <body id="$targetId">
                Loading...
            </body>
        """.trimIndent()
    )
}

fun <T> checkSingleFlow(
    done: CompletableDeferred<Boolean> = CompletableDeferred(),
    upstream: Flow<T>,
    check: (Int, T, T?) -> Boolean
) {
    var count = 0
    mountSingle(Job(), upstream) { value, last ->
        val result = check(count, value, last)
        count++
        if (result) done.complete(true)
    }
}

typealias Endpoint = String

const val test: Endpoint = "test"
const val rest: Endpoint = "rest"

suspend fun testHttpServer(endpoint: Endpoint): Request {
    val r = http("http://localhost:3000/$endpoint")
    if (endpoint == rest) r.get("clear")
    return r
}