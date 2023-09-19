package dev.fritz2.remote

import dev.fritz2.restEndpoint
import dev.fritz2.runTest
import dev.fritz2.testEndpoint
import dev.fritz2.testHttpServer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

enum class HttpStatusCode(val code: Int, val description: String) {
    BadRequest(400, "Bad Request"),
    Unauthorized(401, "Unauthorized"),
    Forbidden(403, "Forbidden"),
    NotFound(404, "Not Found"),
    TooManyRequests(429, "Too Many Requests"),
    InternalServerError(500, "Internal Server Error"),
    NotImplemented(501, "Not Implemented"),
    ServiceUnavailable(503, "Service Unavailable");
}

class RemoteTests {

    @Test
    fun testHTTPMethods() = runTest {
        val remote = testHttpServer(testEndpoint)
        assertTrue(remote.get("get").ok)
        assertTrue(remote.delete("delete").ok)
        assertTrue(remote.head("head").ok)
        assertTrue(remote.body("").patch("patch").ok)
        assertTrue(remote.body("").post("post").ok)
        assertTrue(remote.body("").put("put").ok)
    }


    @Test
    fun testQueryParameters() = runTest {
        val parameters = mapOf(
            "q" to "hello",
            "orderBy" to "name",
        )
        val remote = testHttpServer(testEndpoint)
        val url = remote.get("get", parameters).request.url
        assertTrue(url.endsWith("?q=hello&orderBy=name"))
    }


    @Test
    fun testBasicAuth() = runTest {
        val remote = testHttpServer(testEndpoint)
        val user = "test"
        val password = "password"
        assertTrue(remote.basicAuth(user, password).get("basicAuth").ok)
        val resp = remote.basicAuth(user, password+"w").get("basicAuth")
        assertFalse(resp.ok)
        assertEquals(401, resp.status)
    }


    @Test
    fun testErrorStatusCodes() = runTest {
        val remote = testHttpServer(testEndpoint)
        for(statusCode in HttpStatusCode.values()) {
            assertEquals(statusCode.code, remote.get("status/${statusCode.code}").status)
        }
    }

    @Test
    fun testHeaders() = runTest {
        val remote = testHttpServer(testEndpoint)
        val body: String = remote
            .acceptJson()
            .cacheControl("no-cache")
            .header("test", "this is a test")
            .get("headers").body()
        assertTrue(body.contains(Regex("""accept.+application/json""")), "Accept header not found")
        assertTrue(body.contains(Regex("""cache-control.+no-cache""")), "Cache-Control header not found")
        assertTrue(body.contains(Regex("""test.+this is a test""")), "Test header not found")
    }

    @Test
    fun testCRUDMethods() = runTest {
        val remote = testHttpServer(restEndpoint)
        val texts = mutableListOf<String>()
        val ids = mutableListOf<String>()
        for (i in 0..5) {
            val text = Random.nextLong().toString()
            texts.add(text)
            val saved = remote.body("""{"text": "$text"}""")
                .contentType("application/json").post().body()
            val id = JSON.parse<dynamic>(saved)._id as String
            ids.add(id)
            assertTrue(saved.contains(text), "saved entity not like posted")
        }
        val load = remote.acceptJson().get().body()
        for (text in texts) {
            assertTrue(load.contains(text), "posted entity is not in list")
        }
        for (id in ids) {
            remote.delete(id)
        }
        val empty = remote.acceptJson().get().body()
        for (text in texts) {
            assertFalse(empty.contains(text), "deleted entity is in list")
        }
    }

    @Test
    fun testByteArray() = runTest {
        val remote = testHttpServer("extra/arraybuffer")
        val data = Uint8Array(arrayOf(1, 2, 3))
        val response = remote.arrayBuffer(data.buffer).post()
        val result = Uint8Array(response.arrayBuffer())
        var i = 0
        while (i < result.length) {
            assertEquals(data[i], result[i], "binary data is not matched")
            i++
        }
    }

    @Test
    fun testFailureWithBody() = runTest {
        val remote = testHttpServer("failure")
        for(statusCode in HttpStatusCode.values()) {
            val response = remote.get("${statusCode.code}")
            assertEquals(statusCode.code, response.status)
            assertEquals(statusCode.description, response.body())
        }
    }

}