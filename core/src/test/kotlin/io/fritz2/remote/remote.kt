package io.fritz2.remote

import io.fritz2.test.runTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.singleOrNull
import kotlin.test.*

/**
 * See [Httpbin]((https://httpbin.org/) for testing endpoints
 */
class RemoteTests {

    val remote = remote("https://httpbin.org")
    val codes = listOf<Short>(401, 500)

    @Test
    fun testHTTPMethods() = runTest {
        remote.get("get").onError { fail("get request not working") }.first()
        remote.delete("delete").onError { fail("delete request not working") }.first()
        remote.patch("patch", body = "").onError { fail("patch request not working") }.first()
        remote.post("post", body = "").onError { fail("post request not working") }.first()
        remote.put("put", body = "").onError { fail("put request not working") }.first()
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
                remote.patch("status/$code", body = "").onError { ex -> assertEquals(code, ex.statusCode, "$method: expected status code not matched") }.singleOrNull(),
                "$method: expected response should be null"
            )
        }
    }

    @Test
    fun testPostStatusCodes() = runTest {
        val method = "post"
        for(code in codes) {
            assertEquals(null,
                remote.post("status/$code", body = "").onError { ex -> assertEquals(code, ex.statusCode, "$method: expected status code not matched") }.singleOrNull(),
                "$method: expected response should be null"
            )
        }
    }

    @Test
    fun testPutStatusCodes() = runTest {
        val method = "put"
        for(code in codes) {
            assertEquals(null,
                remote.put("status/$code", body = "").onError { ex -> assertEquals(code, ex.statusCode, "$method: expected status code not matched") }.singleOrNull(),
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