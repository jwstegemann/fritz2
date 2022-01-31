package dev.fritz2.remote

import dev.fritz2.test.authenticated
import dev.fritz2.test.runTest
import dev.fritz2.test.test
import dev.fritz2.test.testHttpServer
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
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
        println("add principal: ${principal?.username}\n")
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
        assertEquals("GET", testHttpServer(authenticated).use(simple).get("get").body())

        simple.clear()
        assertEquals("GET", testHttpServer(test).use(simple).get("get").body())

        assertFailsWith(FetchException::class) {
            testHttpServer(authenticated).get("get")
        }
    }

    @Test
    fun testMultipleAuthentication() = runTest {
        val simple = object : TestAuthenticationMiddleware() {

            var countAuthenticate = 0
            
            override fun authenticate() {
                countAuthenticate++
                println("authenticate")
                window.setTimeout({
                    complete(valid)
                    println("completed\n")
                }, 1000)
            }
        }
        val remote = testHttpServer(authenticated).use(simple)

        buildList {
            repeat(4) {
                add(MainScope().launch {
                    assertEquals("GET", remote.get("get").body())
                })
            }
        }.joinAll()
        assertTrue(simple.countAuthenticate == 1)
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

        val remote = testHttpServer(authenticated).use(simple)

        assertEquals("GET", remote.get("get").body())
        assertEquals("GET", remote.get("get").body())
        assertEquals("GET", remote.get("get").body())
        assertEquals("GET", remote.get("get").body())
        assertEquals("GET", remote.get("get").body())
    }
}