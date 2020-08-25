package dev.fritz2.remote

import dev.fritz2.identification.uniqueId
import dev.fritz2.test.localServer
import dev.fritz2.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * See [Httpbin](https://httpbin.org/) for testing endpoints
 */
class RemoteTests {

    private val remote = remote("https://httpbin.org")
    private val codes = listOf<Short>(401, 500)


    @Test
    fun testHTTPMethods() = runTest {
        remote.get("get")
        remote.delete("delete")
        remote.body("").patch("patch")
        remote.body("").post("post")
        remote.body("").put("put")
    }


    @Test
    fun testBasicAuth() = runTest {
        val user = "test-user"
        val password = "awl12@d+aw23"
        remote.basicAuth(user, password).get("basic-auth/$user/$password")

        assertFailsWith(FetchException::class) {
            remote.basicAuth(user, password).get("basic-auth/$user/${password}a")
        }
    }


    @Test
    fun testGetStatusCodes() = runTest {
        for(code in codes) {
            assertFailsWith(FetchException::class) {
                remote.get("status/$code")
            }
        }
    }


    @Test
    fun testDeleteStatusCodes() = runTest {
        for(code in codes) {
            assertFailsWith(FetchException::class) {
                remote.delete("status/$code")
            }
        }
    }

    @Test
    fun testPatchStatusCodes() = runTest {
        for(code in codes) {
            assertFailsWith(FetchException::class) {
                remote.body("").patch("status/$code")
            }
        }
    }

    @Test
    fun testPostStatusCodes() = runTest {
        for(code in codes) {
            assertFailsWith(FetchException::class) {
                remote.body("").post("status/$code")
            }
        }
    }

    @Test
    fun testPutStatusCodes() = runTest {
        for(code in codes) {
            assertFailsWith(FetchException::class) {
                remote.body("").put("status/$code")
            }
        }
    }

    @Test
    fun testGetHeaders() = runTest {
        val body: String = remote.acceptJson().cacheControl("no-cache").get("headers").getBody()
        assertTrue(body.contains(Regex("""Accept.+application/json""")), "Accept header not found")
        assertTrue(body.contains(Regex("""Cache-Control.+no-cache""")), "Cache-Control header not found")
    }

    @Test
    fun testCRUDMethods() = runTest {
        val posts = localServer("/posts")
        val texts = mutableListOf<String>()
        val ids = mutableListOf<Int>()
        for (i in 1..5) {
            val text = uniqueId()
            texts.add(text)
            val saved =
                posts.body("""{"text": "$text"}""").contentType("application/json").post().getBody()
            val id = JSON.parse<dynamic>(saved).id
            if (id != undefined) ids.add(id as Int)
            assertTrue(saved.contains(text), "saved entity not like posted")
        }
        val load = posts.acceptJson().get().getBody()
        for (text in texts) {
            assertTrue(load.contains(text), "posted entity is not in list")
        }
        for (id in ids) {
            posts.delete(id.toString())
        }
        val empty = posts.acceptJson().get().getBody()
        for (text in texts) {
            assertFalse(empty.contains(text), "deleted entity is in list")
        }
    }

}