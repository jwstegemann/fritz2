package io.fritz2.binding

import kotlin.test.Test
import kotlin.test.assertEquals

class MountPointTests {

    @Test
    fun singleMountPointTest(): Unit {
        assertEquals(1.toString(), "1", "there can be only one")
    }

}