package dev.fritz2.dom.html

import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.KeyboardEventInit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShortcutTests {

    @Test
    fun testCanCreateShortcutFromString() {
        assertEquals(shortcutOf("k"), Shortcut("k"))
        assertEquals(shortcutOf("K"), Shortcut("K"))
    }

    @Test
    fun testCanCreateShortcutFromKeyboardEvent() {
        val event = KeyboardEvent(
            "keydown",
            KeyboardEventInit("K", shiftKey = true, altKey = true, metaKey = true, ctrlKey = true)
        )
        assertEquals(shortcutOf(event), Shortcut("K", true, true, true, true))
        assertEquals(Shortcut(event), Shortcut("K", true, true, true, true))
    }

    @Test
    fun testCanCombineShortcutWithModifierShortcut() {
        // single concatenation
        assertEquals(Shortcut("K", shift = true), Shortcut("K") + Keys.Shift)
        assertEquals(Shortcut("K", alt = true), Shortcut("K") + Keys.Alt)
        assertEquals(Shortcut("K", ctrl = true), Shortcut("K") + Keys.Control)
        assertEquals(Shortcut("K", meta = true), Shortcut("K") + Keys.Meta)

        // double concatenations
        assertEquals(Shortcut("K", shift = true, alt = true), Shortcut("K") + Keys.Shift + Keys.Alt)
        assertEquals(Shortcut("K", shift = true, alt = true), Shortcut("K") + Keys.Alt + Keys.Shift)

        // triple  concatenations
        assertEquals(
            Shortcut("K", shift = true, alt = true, ctrl = true),
            Shortcut("K") + Keys.Shift + Keys.Alt + Keys.Control
        )
        assertEquals(
            Shortcut("K", shift = true, alt = true, ctrl = true),
            Shortcut("K") + Keys.Control + Keys.Shift + Keys.Alt
        )
        assertEquals(
            Shortcut("K", shift = true, alt = true, ctrl = true),
            Shortcut("K") + Keys.Alt + Keys.Control + Keys.Shift
        )

        // plus is idempotent
        assertEquals(Shortcut("K", shift = true), Shortcut("K") + Keys.Shift + Keys.Shift)
        assertEquals(
            Shortcut("K", shift = true, alt = true),
            Shortcut("K") + Keys.Shift + Keys.Alt + Keys.Shift + Keys.Alt + Keys.Shift + Keys.Alt
        )
    }

    @Test
    fun testCanCombineModifierShortcutWithShortcut() {
        // single concatenation
        assertEquals(Shortcut("K", shift = true), Keys.Shift + Shortcut("K"))
        assertEquals(Shortcut("K", alt = true), Keys.Alt + Shortcut("K"))
        assertEquals(Shortcut("K", ctrl = true), Keys.Control + Shortcut("K"))
        assertEquals(Shortcut("K", meta = true), Keys.Meta + Shortcut("K"))

        // double concatenations
        assertEquals(Shortcut("K", shift = true, alt = true), Keys.Shift + Keys.Alt + Shortcut("K"))
        assertEquals(Shortcut("K", shift = true, alt = true), Keys.Alt + Keys.Shift + Shortcut("K"))

        // triple  concatenations
        assertEquals(
            Shortcut("K", shift = true, alt = true, ctrl = true),
            Keys.Shift + Keys.Alt + Keys.Control + Shortcut("K")
        )
        assertEquals(
            Shortcut("K", shift = true, alt = true, ctrl = true),
            Keys.Control + Keys.Shift + Keys.Alt + Shortcut("K")
        )
        assertEquals(
            Shortcut("K", shift = true, alt = true, ctrl = true),
            Keys.Alt + Keys.Control + Keys.Shift + Shortcut("K")
        )
    }

    @Test
    fun testCanCombineModifierShortcutWithString() {
        assertEquals(Shortcut("K", alt = true, shift = true), Keys.Alt + Keys.Shift + "K")
    }

    @Test
    fun testCanCombineMultipleModifierShortcuts() {
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