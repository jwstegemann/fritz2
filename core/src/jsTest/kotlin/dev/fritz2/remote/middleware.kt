package dev.fritz2.remote

import dev.fritz2.test.runTest
import dev.fritz2.test.test
import dev.fritz2.test.testHttpServer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class MiddlewareTests {

    @Test
    fun testMiddleware() = runTest {
        var runMiddlewares = 0

        val headerMiddleware = object : Middleware {
            override suspend fun enrichRequest(request: Request): Request = request.header("test", "this is a test")

            override suspend fun handleResponse(response: Response): Response {
                runMiddlewares += 1
                return response.stopPropagation()
            }
        }

        val shouldNotRunMiddleware = object : Middleware {
            override suspend fun enrichRequest(request: Request): Request = request

            override suspend fun handleResponse(response: Response): Response {
                runMiddlewares += 1
                return response
            }
        }

        val remote = testHttpServer(test).use(shouldNotRunMiddleware, headerMiddleware)

        val body: String = remote
            .acceptJson()
            .cacheControl("no-cache")
            .get("headers").body()

        assertTrue(body.contains(Regex("""test.+this is a test""")), "Test header not found")
        assertEquals(1, runMiddlewares)
    }

}