package io.fritz2.dom

import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.test.initDocument
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.js.Promise
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class TagTests {

    @ExperimentalCoroutinesApi
    @FlowPreview
    @Test
    fun setId(): Promise<Boolean> {
        initDocument()

        val testId = "testId"

        html {
            div(testId) {}
        }.mount("target")

        return GlobalScope.promise {
            delay(250)

            val div = document.getElementById("testId").unsafeCast<HTMLDivElement>()

            assertEquals(testId, div.id)

            true
        }
    }

}