package io.fritz2.examples.gettingstarted

import io.fritz2.binding.RootStore
import io.fritz2.binding.each
import io.fritz2.dom.html.EventType
import io.fritz2.dom.html.Events
import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.events.Event
import kotlin.browser.document

@ExperimentalCoroutinesApi
@FlowPreview
fun main() {

    val store = object : RootStore<Int>(0) {
    }

    val counter = store.data.map { number ->
        html {
            p("value") {
                +number.toString()
                val x = subscribe(Events.click)
            }
        }
    }.conflate()

    document.write("""
            <body id="target">
                Loading...
            </body>
        """.trimIndent())

    html {
        div {
            counter.bind()
            button {
                +"start updates"
                domNode.addEventListener("click", { e: Event ->
                    val values = flow<Int> {
                        for (i in 1..500000) {
                            emit(i)
                        }
                    }

                    store.update <= values
                })
            }
        }
    }.mount("target")

}