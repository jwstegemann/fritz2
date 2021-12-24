package dev.fritz2.remote

import dev.fritz2.test.*
import kotlinx.serialization.Serializable
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

@Serializable
data class Principal(
    val username: String = "",
    val token: String? = null,
)

class AuthenticatedRemoteTests {
    private val authentication = object : Authentication<Principal>() {

        override val statusCodesEnforcingAuthentication: List<Int> = listOf(401, 403)

        override  fun authenticate() {
            complete(Principal("NameOfUser", "123456789"))
        }

        override fun addAuthentication(request: Request, principal: Principal?): Request {
            return principal?.token?.let { request.header("authtoken", it) } ?: request
        }

    }

    @Test
    fun testHTTPMethods() = runTest {
        val remoteWithoutServerCheck = testHttpServerAuthenticated(test, authentication)
        val remoteWithServerCheck = testHttpServerAuthenticated(testauthenticated, authentication)

        authentication.clear()
        remoteWithoutServerCheck.get("get")

        authentication.clear()
        remoteWithServerCheck.get("get")

        authentication.clear()
        remoteWithoutServerCheck.delete("delete")

        authentication.clear()
        remoteWithServerCheck.delete("delete")

        authentication.clear()
        remoteWithoutServerCheck.head("head")

        authentication.clear()
        remoteWithServerCheck.head("head")

        authentication.clear()
        remoteWithoutServerCheck.body("").patch("patch")

        authentication.clear()
        remoteWithServerCheck.body("").patch("patch")

        authentication.clear()
        remoteWithoutServerCheck.body("").post("post")

        authentication.clear()
        remoteWithServerCheck.body("").post("post")

        authentication.clear()
        remoteWithoutServerCheck.body("").put("put")

        authentication.clear()
        remoteWithServerCheck.body("").put("put")

    }

    @Test
    fun testBasicAuthWithoutServerCheck() = runTest {
        authentication.clear()
        val remoteWithoutServerCheck = testHttpServerAuthenticated(test, authentication)

        val user = "test"
        val password = "password"
        remoteWithoutServerCheck.basicAuth(user, password).get("basicAuth")

        authentication.clear()
        assertFailsWith(FetchException::class) {
            remoteWithoutServerCheck.basicAuth(user, password+"w").get("basicAuth")
        }
    }

    @Test
    fun testBasicAuthWithServerCheck() = runTest {
        authentication.clear()
        val remoteWithServerCheck = testHttpServerAuthenticated(testauthenticated, authentication)

        val user = "test"
        val password = "password"

        remoteWithServerCheck.basicAuth(user, password).get("basicAuth")

        authentication.clear()

        assertFailsWith(FetchException::class) {
            remoteWithServerCheck.basicAuth(user, password+"w").get("basicAuth")
        }
    }


    @Test
    fun testErrorStatusCodesWithoutServerCheck() = runTest {
        authentication.clear()
        val remoteWithoutServerCheck = testHttpServerAuthenticated(test, authentication)
        for(code in codes) {
            assertFailsWith(FetchException::class) {
                remoteWithoutServerCheck.get("status/$code")
            }
        }
    }

    @Test
    fun testErrorStatusCodesWithServerCheck() = runTest {
        authentication.clear()
        val remoteWithServerCheck = testHttpServerAuthenticated(testauthenticated, authentication)
        for(code in codes) {
            assertFailsWith(FetchException::class) {
                remoteWithServerCheck.get("status/$code")
            }
        }
    }

    @Test
    fun testHeadersWithoutServerCheck() = runTest {
        authentication.clear()
        val remoteWithoutServerCheck = testHttpServerAuthenticated(test, authentication)
        val body: String = remoteWithoutServerCheck
            .acceptJson()
            .cacheControl("no-cache")
            .header("test", "this is a test")
            .get("headers").body()
        assertTrue(body.contains(Regex("""accept.+application/json""")), "Accept header not found")
        assertTrue(body.contains(Regex("""cache-control.+no-cache""")), "Cache-Control header not found")
        assertTrue(body.contains(Regex("""test.+this is a test""")), "Test header not found")
    }

    @Test
    fun testHeadersWithServerCheck() = runTest {
        authentication.clear()
        val remoteWithServerCheck = testHttpServerAuthenticated(testauthenticated, authentication)
        val body: String = remoteWithServerCheck
            .acceptJson()
            .cacheControl("no-cache")
            .header("test", "this is a test")
            .get("headers").body()
        assertTrue(body.contains(Regex("""accept.+application/json""")), "Accept header not found")
        assertTrue(body.contains(Regex("""cache-control.+no-cache""")), "Cache-Control header not found")
        assertTrue(body.contains(Regex("""test.+this is a test""")), "Test header not found")
    }

    @Test
    fun testCRUDMethodsWithoutServerCheck() = runTest {
        authentication.clear()
        val remoteWithoutServerCheck = testHttpServerAuthenticated(rest, authentication)
        val texts = mutableListOf<String>()
        val ids = mutableListOf<String>()
        for (i in 0..5) {
            val text = Random.nextLong().toString()
            texts.add(text)
            val saved = remoteWithoutServerCheck.body("""{"text": "$text"}""")
                .contentType("application/json").post().body()
            val id = JSON.parse<dynamic>(saved)._id as String
            ids.add(id)
            assertTrue(saved.contains(text), "saved entity not like posted")
        }
        authentication.clear()
        val load = remoteWithoutServerCheck.acceptJson().get().body()
        for (text in texts) {
            assertTrue(load.contains(text), "posted entity is not in list")
        }
        for (id in ids) {
            authentication.clear()
            remoteWithoutServerCheck.delete(id)
        }
        authentication.clear()
        val empty = remoteWithoutServerCheck.acceptJson().get().body()
        for (text in texts) {
            assertFalse(empty.contains(text), "deleted entity is in list")
        }
    }

    @Test
    fun testCRUDMethodsWithServerCheck() = runTest {
        authentication.clear()
        val remoteWithServerCheck = testHttpServerAuthenticated(restauthenticated, authentication)
        val texts = mutableListOf<String>()
        val ids = mutableListOf<String>()
        for (i in 0..5) {
            val text = Random.nextLong().toString()
            texts.add(text)
            val saved = remoteWithServerCheck.body("""{"text": "$text"}""")
                .contentType("application/json").post().body()
            val id = JSON.parse<dynamic>(saved)._id as String
            ids.add(id)
            assertTrue(saved.contains(text), "saved entity not like posted")
        }
        authentication.clear()
        val load = remoteWithServerCheck.acceptJson().get().body()
        for (text in texts) {
            assertTrue(load.contains(text), "posted entity is not in list")
        }
        for (id in ids) {
            authentication.clear()
            remoteWithServerCheck.delete(id)
        }
        authentication.clear()
        val empty = remoteWithServerCheck.acceptJson().get().body()
        for (text in texts) {
            assertFalse(empty.contains(text), "deleted entity is in list")
        }
    }

    @Test
    fun testByteArrayWithoutServerCheck() = runTest {
        authentication.clear()
        val remoteWithoutServerCheck = testHttpServerAuthenticated("extra/arraybuffer", authentication)
        val data = Uint8Array(arrayOf(1, 2, 3))
        val response = remoteWithoutServerCheck.arrayBuffer(data.buffer).post()
        val result = Uint8Array(response.arrayBuffer())
        var i = 0
        while (i < result.length) {
            assertEquals(data[i], result[i], "binary data is not matched")
            i++
        }
    }

    @Test
    fun testByteArrayWithServerCheck() = runTest {
        authentication.clear()
        val remoteWithServerCheck = testHttpServerAuthenticated("extraauthenticated/arraybuffer", authentication)
        val data = Uint8Array(arrayOf(1, 2, 3))
        val response = remoteWithServerCheck.arrayBuffer(data.buffer).post()
        val result = Uint8Array(response.arrayBuffer())
        var i = 0
        while (i < result.length) {
            assertEquals(data[i], result[i], "binary data is not matched")
            i++
        }
    }

}