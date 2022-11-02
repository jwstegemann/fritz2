package dev.fritz2.examples.performance

import dev.fritz2.core.*
import kotlinx.browser.window
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import kotlin.js.Date

@FlowPreview
fun main() {

    val startStore = object : RootStore<Int>(1000, "start") {

        val start = handleAndEmit<Int> { maxCount ->
            val now = Date()
            for (i in 0..maxCount) {
                delay(1)
                emit(i)
                if(i == maxCount)
                    window.alert("Duration: ${Date().getTime() - now.getTime()} ms")
            }
            maxCount
        }

        val dummyHandler = handle { model ->
            model
        }
    }

    val countStore = object : RootStore<Int>(0) {
        init {
            startStore.start handledBy update
        }
    }

    val isFinished = startStore.data
        .combine(countStore.data) { max, current ->
            max == current
        }

    val key = Scope.keyOf<String>("key")

    render("#target") {
        div("form-group") {

            div("form-group") {
                label {
                    `for`(startStore.id)
                    +"Max iterations"
                }
                input("form-control", id = startStore.id) {
                    type("number")
                    value(startStore.data.asString())

                    changes.values().map { it.toInt() } handledBy startStore.update
                }
            }

            countStore.data.render {
                p(scope = {
                    set(key, "$it")
                }) {
                    +"number of updates: $it"
                    clicks handledBy startStore.dummyHandler //register dummy handler
                    span {
                        scope.asDataAttr()
                    }
                }
            }

            div("progress") {
                div("progress-bar") {
                    attr("role", "progressbar")
                    inlineStyle(countStore.data
                        .sample(1000)
                        .combine(startStore.data) { count, maxIterations ->
                        "width: ${(count.toDouble() / maxIterations) * 100}%;"
                    })
                }
            }

            hr { }

            button("btn btn-primary mr-2") {
                +"Start"
                className(isFinished.map { if(it) ".d-none" else "" })

                clicks handledBy startStore.start
            }
            button("btn btn-secondary") {
                +"Reset"

                clicks.map { 0 } handledBy countStore.update
            }

        }
    }
}