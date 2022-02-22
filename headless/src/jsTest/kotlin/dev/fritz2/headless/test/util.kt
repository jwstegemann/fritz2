package dev.fritz2.headless.test

import dev.fritz2.core.Scope
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.promise
import org.w3c.dom.Element

fun <T> runTest(block: suspend () -> T): dynamic = MainScope().promise {
    delay(50)
    block()
    delay(50)
}

fun initDocument() {
    document.clear()
    document.write("""<body>Loading...</body>""")
}

inline fun <reified E: Element> getElementById(id: String) = document.getElementById(id) as E

val scopeTestKey = Scope.keyOf<String>("testScope")
val scopeTestValue = "testScopeValue"