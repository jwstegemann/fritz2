package dev.fritz2.remote

import dev.fritz2.test.rest
import dev.fritz2.test.runTest
import dev.fritz2.test.test
import dev.fritz2.test.testHttpServer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import kotlin.random.Random
import kotlin.test.*

val codes = listOf<Short>(400, 401, 403, 404, 429, 500, 501, 503)

class RemoteTests {

    @Test
    fun testHTTPMethods() = runTest {
        val remote = testHttpServer(test)
        remote.get("get")
        remote.delete("delete")
        remote.head("head")
        remote.body("").patch("patch")
        remote.body("").post("post")
        remote.body("").put("put")
    }


    @Test
    fun testBasicAuth() = runTest {
        val remote = testHttpServer(test)
        val user = "test"
        val password = "password"
        remote.basicAuth(user, password).get("basicAuth")

        assertFailsWith(FetchException::class) {
            remote.basicAuth(user, password+"w").get("basicAuth")
        }
    }


    @Test
    fun testErrorStatusCodes() = runTest {
        val remote = testHttpServer(test)
        for(code in codes) {
            assertFailsWith(FetchException::class) {
                remote.get("status/$code")
            }
        }
    }

    @Test
    fun testHeaders() = runTest {
        val remote = testHttpServer(test)
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
        val remote = testHttpServer(rest)
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