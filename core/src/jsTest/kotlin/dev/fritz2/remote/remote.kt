package dev.fritz2.remote

import dev.fritz2.identification.uniqueId
import dev.fritz2.test.runTest
import kotlin.test.*

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

    /**
     * See [crudcrud.com](https://crudcrud.com).
     */
    @Test
    fun testCURD() = runTest {
        val crudcrud = remote("https://crudcrud.com")
        val regex = """href="/Dashboard/(.+)">""".toRegex()
        val endpointId = regex.find(crudcrud.get().getBody())?.groupValues?.get(1)
                ?: fail("Could not get UniqueEndpointId for https://crudcrud.com/")
        println("See Dashboard here: https://crudcrud.com/Dashboard/$endpointId")

        val users = crudcrud.append("api/$endpointId/users")
        val names = mutableListOf<String>()
        val ids = mutableListOf<String>()
        for (i in 1..5) {
            val name = "name-${uniqueId()}"
            names.add(name)
            val saved = users.body("""{"num": $i, "name": "$name"}""").contentType("application/json").post().getBody()
            val id = JSON.parse<dynamic>(saved)._id
            if(id != undefined) ids.add(id as String)
            assertTrue(saved.contains(name), "saved entity not like posted")
        }
        val load = users.acceptJson().get().getBody()
        for(name in names) {
            assertTrue(load.contains(name), "posted entity is not in list")
        }
        for (id in ids) {
            users.delete(id)
        }
        val empty = users.acceptJson().get().getBody()
        for(name in names) {
            assertFalse(empty.contains(name), "deleted entity is in list")
        }
    }

}