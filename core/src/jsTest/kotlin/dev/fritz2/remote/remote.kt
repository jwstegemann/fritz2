package dev.fritz2.remote

import dev.fritz2.test.runTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.singleOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

/**
 * See [Httpbin]((https://httpbin.org/) for testing endpoints
 */
class RemoteTests {

    private val remote = remote("https://httpbin.org")
    private val codes = listOf<Short>(401, 500)

    @Test
    fun testHTTPMethods() = runTest {
        remote.get("get").onError { fail("get request not working") }.first()
        remote.delete("delete").onError { fail("delete request not working") }.first()
        remote.body("").patch("patch").onError { fail("patch request not working") }.first()
        remote.body("").post("post").onError { fail("post request not working") }.first()
        remote.body("").put("put").onError { fail("put request not working") }.first()
    }

    @Test
    fun testBasicAuth() = runTest {
        val user = "test-user"
        val password = "awl12@d+aw23"
        remote.basicAuth(user, password).get("basic-auth/$user/$password")
            .onError { fail("basic auth not working") }.first()

        assertEquals(null, remote.basicAuth(user, password).get("basic-auth/$user/${password}a")
            .onError { ex -> assertEquals(401, ex.statusCode, "basic auth should not have working") }.singleOrNull(), "basic auth response should be null")
    }

    @Test
    fun testGetStatusCodes() = runTest {
        val method = "get"
        for(code in codes) {
            assertEquals(null,
                remote.get("status/$code").onError { ex -> assertEquals(code, ex.statusCode, "$method: expected status code not matched") }.singleOrNull(),
                "$method: expected response should be null"
            )
        }
    }

    @Test
    fun testDeleteStatusCodes() = runTest {
        val method = "delete"
        for(code in codes) {
            assertEquals(null,
                remote.delete("status/$code").onError { ex -> assertEquals(code, ex.statusCode, "$method: expected status code not matched") }.singleOrNull(),
                "$method: expected response should be null"
            )
        }
    }

    @Test
    fun testPatchStatusCodes() = runTest {
        val method = "patch"
        for(code in codes) {
            assertEquals(null,
                remote.body("").patch("status/$code").onError { ex -> assertEquals(code, ex.statusCode, "$method: expected status code not matched") }.singleOrNull(),
                "$method: expected response should be null"
            )
        }
    }

    @Test
    fun testPostStatusCodes() = runTest {
        val method = "post"
        for(code in codes) {
            assertEquals(null,
                remote.body("").post("status/$code").onError { ex -> assertEquals(code, ex.statusCode, "$method: expected status code not matched") }.singleOrNull(),
                "$method: expected response should be null"
            )
        }
    }

    @Test
    fun testPutStatusCodes() = runTest {
        val method = "put"
        for(code in codes) {
            assertEquals(null,
                remote.body("").put("status/$code").onError { ex -> assertEquals(code, ex.statusCode, "$method: expected status code not matched") }.singleOrNull(),
                "$method: expected response should be null"
            )
        }
    }

    @Test
    fun testGetHeaders() = runTest {
        val body = remote.acceptJson().cacheControl("no-cache").get("headers").body().singleOrNull()
        assertTrue(body?.contains(Regex("""Accept.+application/json""")) ?: false, "Accept header not found")
        assertTrue(body?.contains(Regex("""Cache-Control.+no-cache""")) ?: false, "Cache-Control header not found")
    }
}