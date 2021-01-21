package dev.fritz2.remote

import dev.fritz2.test.*
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import kotlin.random.Random
import kotlin.test.*

class AuthenticatedRemoteTests {

    private val codes = listOf<Short>(400, 401, 403, 404, 429, 500, 501, 503)

    private val authentication = object : Authentication {
        private val EMPTY_TOKEN = ""
        private var token = EMPTY_TOKEN

        override val errorcodesEnforcingAuthentication: List<Short>
            get() = listOf(401, 403)

        override suspend fun enrichRequest(aRequest: Request): Request {
            if(this.token == EMPTY_TOKEN) {
                this.authenticate()
            }
            return aRequest.header("authtoken", token)
        }

        override suspend fun authenticate() {
            token = "123456789"
        }

        override fun isAuthenticated(): Boolean = this.token != EMPTY_TOKEN


        override suspend fun logout() {
            token = EMPTY_TOKEN
        }

        // just for test-purposes to have an invalid token
        fun setTokenInvalid() {
            token = "invalid"
        }

    }

    @Test
    fun testHTTPMethods() = runTest {
        val remoteWithoutServerCheck = testHttpServerAuthenticated(test, authentication)
        val remoteWithServerCheck = testHttpServerAuthenticated(testauthenticated, authentication)

        authentication.setTokenInvalid()
        remoteWithoutServerCheck.get("get")

        authentication.setTokenInvalid()
        remoteWithServerCheck.get("get")

        authentication.setTokenInvalid()
        remoteWithoutServerCheck.delete("delete")

        authentication.setTokenInvalid()
        remoteWithServerCheck.delete("delete")

        authentication.setTokenInvalid()
        remoteWithoutServerCheck.head("head")

        authentication.setTokenInvalid()
        remoteWithServerCheck.head("head")

        authentication.setTokenInvalid()
        remoteWithoutServerCheck.body("").patch("patch")

        authentication.setTokenInvalid()
        remoteWithServerCheck.body("").patch("patch")

        authentication.setTokenInvalid()
        remoteWithoutServerCheck.body("").post("post")

        authentication.setTokenInvalid()
        remoteWithServerCheck.body("").post("post")

        authentication.setTokenInvalid()
        remoteWithoutServerCheck.body("").put("put")

        authentication.setTokenInvalid()
        remoteWithServerCheck.body("").put("put")

    }

    @Test
    fun testBasicAuthWithoutServerCheck() = runTest {
        authentication.setTokenInvalid()
        val remoteWithoutServerCheck = testHttpServerAuthenticated(test, authentication)

        val user = "test"
        val password = "password"
        remoteWithoutServerCheck.basicAuth(user, password).get("basicAuth")

        authentication.setTokenInvalid()
        assertFailsWith(FetchException::class) {
            remoteWithoutServerCheck.basicAuth(user, password+"w").get("basicAuth")
        }
    }

    @Test
    fun testBasicAuthWithServerCheck() = runTest {
        authentication.setTokenInvalid()
        val remoteWithServerCheck = testHttpServerAuthenticated(testauthenticated, authentication)

        val user = "test"
        val password = "password"

        remoteWithServerCheck.basicAuth(user, password).get("basicAuth")

        authentication.setTokenInvalid()

        assertFailsWith(FetchException::class) {
            remoteWithServerCheck.basicAuth(user, password+"w").get("basicAuth")
        }
    }


    @Test
    fun testErrorStatusCodesWithoutServerCheck() = runTest {
        authentication.setTokenInvalid()
        val remoteWithoutServerCheck: AuthenticatedRequest = testHttpServerAuthenticated(test, authentication)
        for(code in codes) {
            assertFailsWith(FetchException::class) {
                remoteWithoutServerCheck.get("status/$code")
            }
        }
    }

    @Test
    fun testErrorStatusCodesWithServerCheck() = runTest {
        authentication.setTokenInvalid()
        val remoteWithServerCheck: AuthenticatedRequest = testHttpServerAuthenticated(testauthenticated, authentication)
        for(code in codes) {
            assertFailsWith(FetchException::class) {
                remoteWithServerCheck.get("status/$code")
            }
        }
    }

    @Test
    fun testHeadersWithoutServerCheck() = runTest {
        authentication.setTokenInvalid()
        val remoteWithoutServerCheck = testHttpServerAuthenticated(test, authentication)
        val body: String = remoteWithoutServerCheck
            .acceptJson()
            .cacheControl("no-cache")
            .header("test", "this is a test")
            .get("headers").getBody()
        assertTrue(body.contains(Regex("""accept.+application/json""")), "Accept header not found")
        assertTrue(body.contains(Regex("""cache-control.+no-cache""")), "Cache-Control header not found")
        assertTrue(body.contains(Regex("""test.+this is a test""")), "Test header not found")
    }

    @Test
    fun testHeadersWithServerCheck() = runTest {
        authentication.setTokenInvalid()
        val remoteWithServerCheck = testHttpServerAuthenticated(testauthenticated, authentication)
        val body: String = remoteWithServerCheck
            .acceptJson()
            .cacheControl("no-cache")
            .header("test", "this is a test")
            .get("headers").getBody()
        assertTrue(body.contains(Regex("""accept.+application/json""")), "Accept header not found")
        assertTrue(body.contains(Regex("""cache-control.+no-cache""")), "Cache-Control header not found")
        assertTrue(body.contains(Regex("""test.+this is a test""")), "Test header not found")
    }

    @Test
    fun testCRUDMethodsWithoutServerCheck() = runTest {
        authentication.setTokenInvalid()
        val remoteWithoutServerCheck = testHttpServerAuthenticated(rest, authentication)
        val texts = mutableListOf<String>()
        val ids = mutableListOf<String>()
        for (i in 0..5) {
            val text = Random.nextLong().toString()
            texts.add(text)
            val saved = remoteWithoutServerCheck.body("""{"text": "$text"}""")
                .contentType("application/json").post().getBody()
            val id = JSON.parse<dynamic>(saved)._id as String
            ids.add(id)
            assertTrue(saved.contains(text), "saved entity not like posted")
        }
        authentication.setTokenInvalid()
        val load = remoteWithoutServerCheck.acceptJson().get().getBody()
        for (text in texts) {
            assertTrue(load.contains(text), "posted entity is not in list")
        }
        for (id in ids) {
            authentication.setTokenInvalid()
            remoteWithoutServerCheck.delete(id)
        }
        authentication.setTokenInvalid()
        val empty = remoteWithoutServerCheck.acceptJson().get().getBody()
        for (text in texts) {
            assertFalse(empty.contains(text), "deleted entity is in list")
        }
    }

    @Test
    fun testCRUDMethodsWithServerCheck() = runTest {
        authentication.setTokenInvalid()
        val remoteWithServerCheck = testHttpServerAuthenticated(restauthenticated, authentication)
        val texts = mutableListOf<String>()
        val ids = mutableListOf<String>()
        for (i in 0..5) {
            val text = Random.nextLong().toString()
            texts.add(text)
            val saved = remoteWithServerCheck.body("""{"text": "$text"}""")
                .contentType("application/json").post().getBody()
            val id = JSON.parse<dynamic>(saved)._id as String
            ids.add(id)
            assertTrue(saved.contains(text), "saved entity not like posted")
        }
        authentication.setTokenInvalid()
        val load = remoteWithServerCheck.acceptJson().get().getBody()
        for (text in texts) {
            assertTrue(load.contains(text), "posted entity is not in list")
        }
        for (id in ids) {
            authentication.setTokenInvalid()
            remoteWithServerCheck.delete(id)
        }
        authentication.setTokenInvalid()
        val empty = remoteWithServerCheck.acceptJson().get().getBody()
        for (text in texts) {
            assertFalse(empty.contains(text), "deleted entity is in list")
        }
    }

    @Test
    fun testByteArrayWithoutServerCheck() = runTest {
        authentication.setTokenInvalid()
        val remoteWithoutServerCheck = testHttpServerAuthenticated("extra/arraybuffer", authentication)
        val data = Uint8Array(arrayOf(1, 2, 3))
        val response = remoteWithoutServerCheck.arrayBuffer(data.buffer).post()
        val result = Uint8Array(response.getArrayBuffer())
        var i = 0
        while (i < result.length) {
            assertEquals(data[i], result[i], "binary data is not matched")
            i++
        }
    }

    @Test
    fun testByteArrayWithServerCheck() = runTest {
        authentication.setTokenInvalid()
        val remoteWithServerCheck = testHttpServerAuthenticated("extraauthenticated/arraybuffer", authentication)
        val data = Uint8Array(arrayOf(1, 2, 3))
        val response = remoteWithServerCheck.arrayBuffer(data.buffer).post()
        val result = Uint8Array(response.getArrayBuffer())
        var i = 0
        while (i < result.length) {
            assertEquals(data[i], result[i], "binary data is not matched")
            i++
        }
    }

}