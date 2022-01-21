package dev.fritz2.remote

import dev.fritz2.test.authenticated
import dev.fritz2.test.runTest
import dev.fritz2.test.test
import dev.fritz2.test.testHttpServer
import kotlinx.browser.window
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

data class Principal(
    val username: String = "",
    val token: String? = null,
)

abstract class TestAuthenticationMiddleware : Authentication<Principal>() {

    val valid = Principal("NameOfUser", "123456789")

    final override fun addAuthentication(request: Request, principal: Principal?): Request {
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

        assertEquals(null, simple.getPrincipal())

        simple.clear()
        testHttpServer(authenticated).use(simple).get("get")

        simple.clear()
        testHttpServer(test).use(simple).get("get")

        assertFailsWith(FetchException::class) {
            testHttpServer(authenticated).get("get")
        }
    }

    @Test
    fun testMultipleAuthentication() = runTest {
        val simple = object : TestAuthenticationMiddleware() {
            override fun authenticate() {
                window.setTimeout({
                    complete(valid)
                    println("completed\n")
                }, 500)
            }
        }
        val remote = testHttpServer(authenticated).use(simple)

        remote.get("get")
        remote.get("get")
        remote.get("get")
        remote.get("get")
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

        assertEquals(simple.valid, simple.getPrincipal())

        val remote = testHttpServer(authenticated).use(simple)

        remote.get("get")
        remote.get("get")
        remote.get("get")
        remote.get("get")
    }
}