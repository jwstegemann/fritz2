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

val codes = listOf(400, 401, 403, 404, 429, 500, 501, 503)

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
        for(code in codes) {
            assertEquals(code, remote.get("status/$code").status)
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

}