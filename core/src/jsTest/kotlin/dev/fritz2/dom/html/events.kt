package dev.fritz2.dom.html

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KeyTests {

    @Test
    fun testCreateKeyWithUpperCaseWillConvertToLowercase() {
        assertEquals(Key("K"), Key("k"))
        assertEquals(Keys.Control + "K", Key("k", ctrl = true))
    }

    @Test
    fun testKeysOfPredefinedKeyValuesWillRemainMixedCase() {
        assertEquals(Keys.ArrowDown.name, "ArrowDown")
        assertEquals(Key("ArrowDown").name, "ArrowDown")
    }

    /**
     * This is a drawback of using private constructors with data classes!
     * So this test is rather documentation of the pure fact than useful expected behaviour ;-)
     *
     * But keep in mind that the normalization process to convert all none predefined key names (that is all keys with
     * a name longer than one character) is just a benevolent act to users - if somehow one would like to create a
     * `Key` object with uppercase letter we cannot prevent this for all cases!
     */
    @Test
    fun testUsingCopyCanCreateKeysWithSingleUppercaseLetter() {
        assertEquals("k", Key("Foo").copy(name = "k").name)
    }

    @Test
    fun testCanCombineKeyWithModifierKey() {
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
    fun testCanCombineModifierKeyWithKey() {
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
    fun testCanCombineModifierKeyWithString() {
        assertEquals(Key("K", alt = true, shift = true), Keys.Alt + Keys.Shift + "K")
    }

    @Test
    fun testCanCombineMultipleModifierKeys() {
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