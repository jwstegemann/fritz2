package dev.fritz2.dom

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.render
import dev.fritz2.identification.Id
import dev.fritz2.test.initDocument
import dev.fritz2.test.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertEquals


class LifecycleTests {

    @Test
    fun testLifecycleHandler() = runTest {
        initDocument()

        val testId = Id.next()

        val countingStore = storeOf(0)

        var mounts = 0
        var unmounts = 0

        render {
            div {
                countingStore.data.render {
                    div(id = testId) {
                        +it.toString()

                        mountPoint()?.afterMount(this) { _, _ ->
                            mounts += 1;
                            null
                        }
                        mountPoint()?.beforeUnmount(this) { _, _ ->
                            unmounts += 1
                            null
                        }
                    }
                }
            }
        }

        fun content(): String = document.getElementById(testId)?.textContent.orEmpty()

        fun check(count: Int) {
            assertEquals(count.toString(), content(), "wrong content rendered in step $count")
            assertEquals(count + 1, mounts, "wrong number of mounts in step $count")
            assertEquals(count, unmounts, "wrong number of unmounts in step $count")
        }

        for (i in 0..3) {
//            console.log("run: $i\n")
//            console.log("  mounts  : $mounts\n")
//            console.log("  unmounts: $unmounts\n")
//            console.log("  text    : ${content()}\n")

            delay(100)
            check(i)
            countingStore.update(i + 1)
        }
    }
}