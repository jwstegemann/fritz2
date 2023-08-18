package dev.fritz2.remote

import dev.fritz2.authenticatedEndpoint
import dev.fritz2.runTest
import dev.fritz2.testEndpoint
import dev.fritz2.testHttpServer
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

data class Principal(
    val username: String = "",
    val token: String? = null,
)

abstract class TestAuthenticationMiddleware : Authentication<Principal>() {

    val valid = Principal("NameOfUser", "123456789")
    var countAddAuthentication = 0

    final override fun addAuthentication(request: Request, principal: Principal?): Request {
        countAddAuthentication++
        return principal?.token?.let { request.header("authtoken", it) } ?: request
    }
}

class AuthenticatedRemoteTests {

    @Test
    fun testSingleAuthentication() = runTest {
        val simple = object : TestAuthenticationMiddleware() {
            override fun authenticate() {
                complete(valid)
            }
        }

        assertEquals(null, simple.current)

        simple.clear()
        assertEquals("GET", testHttpServer(authenticatedEndpoint).use(simple).get("get").body())

        simple.clear()
        assertEquals("GET", testHttpServer(testEndpoint).use(simple).get("get").body())

        val resp = testHttpServer(authenticatedEndpoint).get("get")
        assertFalse(resp.ok)
        assertEquals(401, resp.status)
    }

    @Test
    fun testMultipleAuthentication() = runTest {
        val simple = object : TestAuthenticationMiddleware() {

            var countAuthenticate = 0

            override fun authenticate() {
                countAuthenticate++
                window.setTimeout({
                    complete(valid)
                }, 1000)
            }
        }
        val remote = testHttpServer(authenticatedEndpoint).use(simple)

        buildList {
            repeat(4) {
                add(
                    MainScope().launch {
                        assertEquals("GET", remote.get("get").body())
                    },
                )
            }
        }.joinAll()
        assertEquals(1, simple.countAuthenticate)
        assertTrue(simple.countAddAuthentication <= 8)
    }

    @Test
    fun testPreAuthentication() = runTest {
        val simple = object : TestAuthenticationMiddleware() {
            override fun authenticate() {
                throw Exception("should not be called!")
            }

            init {
                complete(valid)
            }
        }

        assertEquals(simple.valid, simple.current)

        val remote = testHttpServer(authenticatedEndpoint).use(simple)

        buildList {
            repeat(4) {
                add(
                    MainScope().launch {
                        assertEquals("GET", remote.get("get").body())
                    },
                )
            }
        }.joinAll()
        assertEquals(4, simple.countAddAuthentication)
        // test pure sequential request too
        assertEquals("GET", remote.get("get").body())
    }
}
