package dev.fritz2.binding

import dev.fritz2.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class HandlersTests {

    @Test
    fun testSimpleHandler() = runTest {
        val store = object : RootStore<Int>(0) {
            override fun errorHandler(exception: Throwable, oldValue: Int): Int {
                fail(exception.message)
            }
        }

        store.handle { assertTrue(true); it }()
        store.handle { assertTrue(true); it }(Unit)
        store.handle<String> { n, s -> assertFalse(s::class == String::class); n }()
        store.handle<String> { n, s -> assertTrue(s::class == Unit::class); n }()
        store.handle<String> { n, s -> assertTrue(s.length == undefined); n }()
        store.handle<String> { n, s -> assertTrue(s.substring(1) == undefined); n }()
        store.handle<String> { n, _ -> assertTrue(true); n }("Hello")
    }

}
