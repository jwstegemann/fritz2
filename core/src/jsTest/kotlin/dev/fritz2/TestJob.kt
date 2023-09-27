package dev.fritz2

import dev.fritz2.core.RootStore
import dev.fritz2.core.render
import dev.fritz2.core.storeOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlin.test.Test
import kotlin.test.assertEquals


class TestJob {

    @Test
    fun testRenderContextJob() = runTest {
        // handledBy nutzt den Job des RenderContexts
        val globalStore = storeOf(0)
        render {
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
        assertEquals(1, RootStore.ACTIVE_FLOWS,"ACTIVE_FLOWS is not 1")
        assertEquals(1, RootStore.ACTIVE_JOBS,"ACTIVE_JOBS is not 1")
    }

    @Test
    fun testStoreJob() = runTest {
        // handledBy nutzt den Job des lokalen Stores
        val globalStore = storeOf(0)
        render {
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
        assertEquals(1, RootStore.ACTIVE_FLOWS,"ACTIVE_FLOWS is not 1")
        assertEquals(1, RootStore.ACTIVE_JOBS,"ACTIVE_JOBS is not 1")
    }

    @Test
    fun testGlobalJob() = runTest {
        // handledBy nutzt den Job des globalen Stores
        val globalStore = storeOf(0)
        render {
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
        assertEquals(1, globalStore.current, "GlobalStore is not 1")
        assertEquals(1, RootStore.ACTIVE_FLOWS,"ACTIVE_FLOWS is not 1")
        assertEquals(1, RootStore.ACTIVE_JOBS,"ACTIVE_JOBS is not 1")
    }

    @Test
    fun test() = runTest {
        // handledBy nutzt den Job des globalen Stores
        val globalStore = storeOf(0)
        render {
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
        assertEquals(1, RootStore.ACTIVE_FLOWS,"ACTIVE_FLOWS is not 1")
        assertEquals(1, RootStore.ACTIVE_JOBS,"ACTIVE_JOBS is not 1")
    }
}