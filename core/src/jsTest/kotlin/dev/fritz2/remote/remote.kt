package dev.fritz2.remote

import dev.fritz2.test.rest
import dev.fritz2.test.runTest
import dev.fritz2.test.test
import dev.fritz2.test.testServer
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * See [Httpbin](https://httpbin.org/) for testing endpoints
 */
class RemoteTests {

    private val codes = listOf<Short>(400, 401, 403, 404, 429, 500, 501, 503)


    @Test
    fun testHTTPMethods() = runTest {
        val remote = testServer(test)
        remote.get("get")
        remote.delete("delete")
        remote.head("head")
        remote.body("").patch("patch")
        remote.body("").post("post")
        remote.body("").put("put")
    }


    @Test
    fun testBasicAuth() = runTest {
        val remote = testServer(test)
        val user = "test"
        val password = "password"
        remote.basicAuth(user, password).get("basicAuth")

        assertFailsWith(FetchException::class) {
            remote.basicAuth(user, password+"w").get("basicAuth")
        }
    }


    @Test
    fun testErrorStatusCodes() = runTest {
        val remote = testServer(test)
        for(code in codes) {
            assertFailsWith(FetchException::class) {
                remote.get("status/$code")
            }
        }
    }

    @Test
    fun testHeaders() = runTest {
        val remote = testServer(test)
        val body: String = remote
            .acceptJson()
            .cacheControl("no-cache")
            .header("test", "this is a test")
            .get("headers").getBody()
        assertTrue(body.contains(Regex("""accept.+application/json""")), "Accept header not found")
        assertTrue(body.contains(Regex("""cache-control.+no-cache""")), "Cache-Control header not found")
        assertTrue(body.contains(Regex("""test.+this is a test""")), "Test header not found")
    }

    @Test
    fun testCRUDMethods() = runTest {
        val remote = testServer(rest)
        val texts = mutableListOf<String>()
        val ids = mutableListOf<String>()
        for (i in 0..5) {
            val text = Random.nextLong().toString()
            texts.add(text)
            val saved = remote.body("""{"text": "$text"}""")
                .contentType("application/json").post().getBody()
            val id = JSON.parse<dynamic>(saved)._id as String
            ids.add(id)
            assertTrue(saved.contains(text), "saved entity not like posted")
        }
        val load = remote.acceptJson().get().getBody()
        for (text in texts) {
            assertTrue(load.contains(text), "posted entity is not in list")
        }
        for (id in ids) {
            remote.delete(id)
        }
        val empty = remote.acceptJson().get().getBody()
        for (text in texts) {
            assertFalse(empty.contains(text), "deleted entity is in list")
        }
    }

}