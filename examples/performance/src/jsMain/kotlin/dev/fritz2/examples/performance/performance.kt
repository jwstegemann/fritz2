package dev.fritz2.examples.performance

import dev.fritz2.core.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlin.js.Date

@FlowPreview
fun main() {
    val startTime = storeOf(0.0, job = Job())
    val endTime = storeOf(0.0, job = Job())

    val counter = storeOf(0, job = Job())
    val increment = counter.handle { it + 1 }

    val startStore = object : RootStore<Int>(1000, "start", job = Job()) {


        val start = handleAndEmit { maxCount ->
            counter.update(0)
            endTime.update(0.0)
            startTime.update(Date().getTime())
            for (i in 0..maxCount) {
                delay(10)
                emit(i)
                if (i == maxCount)
                    endTime.update(Date().getTime())
            }
            maxCount
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
                    +"number of store-updates: $it"
                    if (it != 0 && it != startStore.current) {
                        //   clicks handledBy startStore.dummyHandler //register dummy handler
                        span {
                            scope.asDataAttr()
                        }

                        val store = storeOf(0, id = "local")

                        countStore.data handledBy store.update
                        store.data handledBy {}

                        countStore.apply {
                            countStore.data handledBy store.update
                            store.data handledBy {}
                        }

                        flow {
                            emit(0)
                            delay(Long.MAX_VALUE)
                        } handledBy store.update

                        val listStore = storeOf((0..100).toList())

                        listStore.mapByIndex(0).data.render {
                            countStore.data handledBy store.update
                            store.data handledBy {}

                        }


                        listStore.renderEach({ it }) { substore ->
                            div {
                                countStore.data handledBy store.update
                                substore.data handledBy {}

                                countStore.apply {
                                    countStore.data handledBy store.update
                                    substore.data handledBy {}
                                }

                            }

                        }

                        increment()
                    }
                }
            }

            counter.data.render { +"number of rerenders: $it" }
            div("progress") {
                div("progress-bar") {
                    attr("role", "progressbar")
                    inlineStyle(
                        countStore.data
                            .sample(100)
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

            button("btn btn-secondary") {
                +"Reset"
                clicks.map { 0 } handledBy countStore.update
            }

            (0..Int.MAX_VALUE).asFlow().onEach { delay(100) }.render {
                +" Number of active Store-Flows: ${RootStore.ACTIVE_FLOWS}"
                +" | Number of active Store-Jobs: ${RootStore.ACTIVE_JOBS}"
            }

            startTime.data.render { start ->
                if (start != 0.0)
                    endTime.data.render { end ->
                        p {
                            if (end > 0.0)
                                +"Duration: ${end - start} ms"
                            else
                                (0..Int.MAX_VALUE).asFlow().onEach { delay(100) }.render {
                                    +"Duration: ${Date().getTime() - start} ms (running)"
                                }
                        }
                    }
            }
        }
    }
}