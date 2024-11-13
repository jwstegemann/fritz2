package dev.fritz2.headless.foundation

import dev.fritz2.headless.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PropertyTest {

    @Test
    fun testProperty() = runTest {
        val prop = object : Property<String>() {}
        assertFalse(prop.isSet)
        assertEquals(null, prop.value)
        prop.use("test")
        assertTrue(prop.isSet)
        assertEquals("test", prop.value)
    }
}