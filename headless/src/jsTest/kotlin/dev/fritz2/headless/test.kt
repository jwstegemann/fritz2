package dev.fritz2.headless

import dev.fritz2.core.Scope
import dev.fritz2.core.Shortcut
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.promise
import org.w3c.dom.Element
import org.w3c.dom.events.EventTarget
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.KeyboardEventInit
import org.w3c.dom.pointerevents.PointerEvent
import org.w3c.dom.pointerevents.PointerEventInit

fun <T> runTest(block: suspend () -> T): dynamic = MainScope().promise {
    delay(50)
    block()
    delay(50)
}

inline fun <reified E: Element> getElementById(id: String) = document.getElementById(id) as E

suspend fun <E: EventTarget> E.keyDown(vararg shortcuts: Shortcut) {
    for(shortcut in shortcuts) {
        this.dispatchEvent(KeyboardEvent("keydown", KeyboardEventInit(shortcut.key, shortcut.key)))
        delay(10)
    }
}

fun <E: EventTarget> E.click(x: Int, y: Int) {
    this.dispatchEvent(PointerEvent("click", PointerEventInit(clientX = x, clientY = y)))
}

val scopeTestKey = Scope.keyOf<String>("testScope")
val scopeTestValue = "testScopeValue"