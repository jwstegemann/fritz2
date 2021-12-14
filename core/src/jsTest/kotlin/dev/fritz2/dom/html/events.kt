package dev.fritz2.dom.html

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KeyTests {

    @Test
    fun testCanCombineKeyWithExtraKey() {
        // single concatenation
        assertEquals(Key("K", shift = true), Key("K") + Keys.Shift)
        assertEquals(Key("K", alt = true), Key("K") + Keys.Alt)
        assertEquals(Key("K", ctrl = true), Key("K") + Keys.Control)
        assertEquals(Key("K", meta = true), Key("K") + Keys.Meta)

        // double concatenations
        assertEquals(Key("K", shift = true, alt = true), Key("K") + Keys.Shift + Keys.Alt)
        assertEquals(Key("K", shift = true, alt = true), Key("K") + Keys.Alt + Keys.Shift)

        // triple  concatenations
        assertEquals(
            Key("K", shift = true, alt = true, ctrl = true),
            Key("K") + Keys.Shift + Keys.Alt + Keys.Control
        )
        assertEquals(
            Key("K", shift = true, alt = true, ctrl = true),
            Key("K") + Keys.Control + Keys.Shift + Keys.Alt
        )
        assertEquals(
            Key("K", shift = true, alt = true, ctrl = true),
            Key("K") + Keys.Alt + Keys.Control + Keys.Shift
        )

        // plus is idempotent
        assertEquals(Key("K", shift = true), Key("K") + Keys.Shift + Keys.Shift)
        assertEquals(
            Key("K", shift = true, alt = true),
            Key("K") + Keys.Shift + Keys.Alt + Keys.Shift + Keys.Alt + Keys.Shift + Keys.Alt
        )
    }

    @Test
    fun testCanCombineExtraKeyWithKey() {
        // single concatenation
        assertEquals(Key("K", shift = true), Keys.Shift + Key("K"))
        assertEquals(Key("K", alt = true), Keys.Alt + Key("K"))
        assertEquals(Key("K", ctrl = true), Keys.Control + Key("K"))
        assertEquals(Key("K", meta = true), Keys.Meta + Key("K"))

        // double concatenations
        assertEquals(Key("K", shift = true, alt = true), Keys.Shift + Keys.Alt + Key("K"))
        assertEquals(Key("K", shift = true, alt = true), Keys.Alt + Keys.Shift + Key("K"))

        // triple  concatenations
        assertEquals(
            Key("K", shift = true, alt = true, ctrl = true),
            Keys.Shift + Keys.Alt + Keys.Control + Key("K")
        )
        assertEquals(
            Key("K", shift = true, alt = true, ctrl = true),
            Keys.Control + Keys.Shift + Keys.Alt + Key("K")
        )
        assertEquals(
            Key("K", shift = true, alt = true, ctrl = true),
            Keys.Alt + Keys.Control + Keys.Shift + Key("K")
        )
    }

    @Test
    fun testCanCombineExtraKeyWithString() {
        assertEquals(Key("K", alt = true, shift = true), Keys.Alt + Keys.Shift + "K")
    }

    @Test
    fun testCanCombineMultipleExtraKeys() {
        with(Keys.Alt + Keys.Shift) {
            assertTrue(this.alt)
            assertTrue(this.shift)
        }

        with(Keys.Alt + Keys.Shift + Keys.Control) {
            assertTrue(this.alt)
            assertTrue(this.shift)
            assertTrue(this.ctrl)
        }

        with(Keys.Alt + Keys.Shift + Keys.Control + Keys.Meta) {
            assertTrue(this.alt)
            assertTrue(this.shift)
            assertTrue(this.ctrl)
            assertTrue(this.meta)
        }
    }
}