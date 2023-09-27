package dev.fritz2.examples.performance

import dev.fritz2.core.*
import kotlinx.browser.window
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlin.js.Date

@FlowPreview
fun main() {

    val startStore = object : RootStore<Int>(1000, "start", job = Job()) {

        val start = handleAndEmit<Int> { maxCount ->
            val now = Date()
            for (i in 0..maxCount) {
                delay(1)
                emit(i)
                if (i == maxCount)
                    window.alert("Duration: ${Date().getTime() - now.getTime()} ms")
            }
            maxCount
        }

        val dummyHandler = handle { model ->
            model
        }
    }

    val countStore = object : RootStore<Int>(0, job = Job(), id = "count") {
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
                    if (it != 0 && it != startStore.current) {
                        //   clicks handledBy startStore.dummyHandler //register dummy handler
                        span {
                            scope.asDataAttr()
                        }

                        val store = storeOf(0, id = "local")

                        countStore.data handledBy store.update
                        store.data handledBy {}


                        // TODO irgendwo wird ein RootStore.data geleaked! Eventuell durch Events!


                        // TODO leaking RootStore
                        /*   countStore.apply {
                               countStore.data handledBy store.update
                               store.data handledBy {}
                           }*/

                        flow<Int> {
                            emit(0)
                            delay(Long.MAX_VALUE)
                        } handledBy store.update

                        val listStore = storeOf((0..it).toList())

                        listStore.mapByIndex(0).data.render {
                            countStore.data handledBy store.update
                            store.data handledBy {}

                        }


                        listStore.renderEach({ it }) { substore ->
                            div {
                                countStore.data handledBy store.update
                                substore.data handledBy {}

                                // TODO leaking RootStore
                                /*  countStore.apply {
                                      countStore.data handledBy store.update
                                      substore.data handledBy {}
                                  }*/
                            }

                        }
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
                className(isFinished.map { if (it) ".d-none" else "" })

                clicks handledBy startStore.start
            }

            val clicks = button("btn btn-secondary") {
                +"Reset"
                clicks.map { 0 } handledBy countStore.update
            }.clicks

            merge(clicks, countStore.data.filter { it == 0 || it == startStore.current }).onEach {
                console.log(it)
            }.onStart { delay(1000) }
                .flatMapLatest { countStore.data.map { Id.next() } }
                .render {
                    +" Number of active Store-Flows: ${RootStore.ACTIVE_FLOWS}"
                    +" | Number of active Store-Jobs: ${RootStore.ACTIVE_JOBS}"
                    +" | Click 'Reset' to get latest numbers"
                }
        }
    }
}