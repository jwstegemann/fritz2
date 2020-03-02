package io.fritz2.test

import kotlin.browser.document

fun initDocument(): Unit {
    document.write("""
            <body id="target">
                Loading...
            </body>
        """.trimIndent())
}