package dev.fritz2.core

import dev.fritz2.renderWithJob
import dev.fritz2.runTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


class JobLifecycleTests {
    @BeforeTest
    fun init() {
        RootStore.resetCounters()
    }

    @Test
    fun testRenderContextJob() = runTest {
        assertEquals(0, RootStore.ACTIVE_JOBS, "ACTIVE_JOBS is not initially 0")
        assertEquals(0, RootStore.ACTIVE_FLOWS, "ACTIVE_FLOWS is not initially 0")


        // handledBy nutzt den Job des RenderContexts,
        val globalStore = storeOf(0)

        renderWithJob {
            globalStore.data.render {
                if (it == 0) {
                    val localStore = storeOf(0)
                    localStore.data.debounce(100).map { it + 1 } handledBy globalStore.update
                    globalStore.data.drop(1) handledBy localStore.update
                }
            }
        }
        delay(500)
        assertEquals(1, globalStore.current)
        assertEquals(1, RootStore.ACTIVE_JOBS, "ACTIVE_JOBS is not 1")
        assertEquals(1, RootStore.ACTIVE_FLOWS, "ACTIVE_FLOWS is not 1")
    }

    @Test
    fun testStoreJob() = runTest {
        assertEquals(0, RootStore.ACTIVE_JOBS, "ACTIVE_JOBS is not initially 0")
        assertEquals(0, RootStore.ACTIVE_FLOWS, "ACTIVE_FLOWS is not initially 0")

        // handledBy nutzt den Job des lokalen Stores
        val globalStore = storeOf(0)
        renderWithJob {
            globalStore.data.render {
                if (it == 0) {
                    val localStore = storeOf(0)
                    localStore.apply {
                        localStore.data.debounce(100).map { it + 1 } handledBy globalStore.update
                        globalStore.data.drop(1) handledBy localStore.update
                    }
                }
            }
        }
        delay(500)
        assertEquals(1, globalStore.current)
        assertEquals(1, RootStore.ACTIVE_JOBS, "ACTIVE_JOBS is not 1")
        assertEquals(1, RootStore.ACTIVE_FLOWS, "ACTIVE_FLOWS is not 1")
    }

    @Test
    @Ignore // Store.apply {} does not use the Store-Job anymore, so RenderContext-Job used here
    fun testGlobalJob() = runTest {
        assertEquals(0, RootStore.ACTIVE_JOBS, "ACTIVE_JOBS is not initially 0")
        assertEquals(0, RootStore.ACTIVE_FLOWS, "ACTIVE_FLOWS is not initially 0")

        // handledBy nutzt den Job des globalen Stores
        val globalStore = storeOf(0)
        renderWithJob {
            globalStore.data.render {
                if (it == 0) {
                    val localStore = storeOf(0)
                    globalStore.apply {
                        localStore.data.debounce(100).map { it + 1 } handledBy globalStore.update
                        globalStore.data.drop(1) handledBy localStore.update
                    }
                }
            }
        }
        delay(1000)
        assertEquals(2, globalStore.current, "GlobalStore is not 1")
        assertEquals(1, RootStore.ACTIVE_JOBS, "ACTIVE_JOBS is not 1")
        assertEquals(1, RootStore.ACTIVE_FLOWS, "ACTIVE_FLOWS is not 1")
    }

    @Test
    fun test() = runTest {
        assertEquals(0, RootStore.ACTIVE_JOBS, "ACTIVE_JOBS is not initially 0")
        assertEquals(0, RootStore.ACTIVE_FLOWS, "ACTIVE_FLOWS is not initially 0")


        // handledBy nutzt den Job des globalen Stores
        val globalStore = storeOf(0)
        renderWithJob {
            globalStore.data.render {
                if (it < 10)
                    p {
                        val localStore = storeOf(it)
                        localStore.data.renderText()
                        clicks.map { localStore.current + 1 } handledBy localStore.update
                        localStore.data.map { it + 1 } handledBy globalStore.update
                    }
            }
        }
        delay(100)
        assertEquals(1, RootStore.ACTIVE_JOBS, "ACTIVE_JOBS is not 1")
        assertEquals(1, RootStore.ACTIVE_FLOWS, "ACTIVE_FLOWS is not 1")
    }


}