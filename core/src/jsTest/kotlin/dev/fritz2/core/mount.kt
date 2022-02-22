package dev.fritz2.core

import dev.fritz2.checkSingleFlow
import dev.fritz2.initDocument
import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.promise
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLOptionElement
import org.w3c.dom.HTMLSelectElement
import kotlin.js.Promise
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class MountTests {

    @Test
    fun testStore(): Promise<Boolean> {

        val store = RootStore("")

        val values = listOf(
            "",
            "1",
            "1-2",
            "1-2-3",
            "1-2-3-4"
        )


        val done = CompletableDeferred<Boolean>()
        checkSingleFlow(done, store.data) { _, value ->
            assertTrue(values.contains(value))
            value == values.last()
        }

        store.data handledBy {}

        return GlobalScope.promise {
            values.forEach { value ->
                store.enqueue { value }
            }
            done.await()
        }
    }


    @Test
    fun testOrderOfSingleMountPointCreation() = runTest {
        initDocument()

        val outer = Id.next()
        val inner1 = Id.next()
        val inner2 = Id.next()

        val text = flowOf("test")

        render {
            div(id = outer) {
                text.render {
                    div(id = inner1) {
                        +it
                    }
                }
                div(id = inner2) {
                    +"hallo"
                }
            }
        }

        delay(250)

        val outerElement = document.getElementById(outer) as HTMLDivElement
        assertEquals(inner1, outerElement.firstElementChild?.firstElementChild?.id, "first element id does not match")
        assertEquals(inner2, outerElement.lastElementChild?.id, "last element id does not match")
    }

    @Test
    fun testOrderOfMultiMountPointCreation() = runTest {
        initDocument()

        val outer = Id.next()
        val inner1 = Id.next()
        val inner2 = Id.next()
        val inner3 = Id.next()

        val text = flowOf(listOf(inner1, inner2))

        render {
            div(id = outer) {
                text.renderEach {
                    div(id = it) {}
                }
                div(id = inner3) {}
            }
        }

        delay(250)

        val outerElement = document.getElementById(outer) as HTMLDivElement
        assertEquals(inner1, outerElement.firstElementChild?.firstElementChild?.id, "first element id does not match")
        assertEquals(
            inner2,
            outerElement.firstElementChild?.firstElementChild?.nextElementSibling?.id,
            "second element id does not match"
        )
        assertEquals(inner3, outerElement.lastElementChild?.id, "last element id does not match")
    }

    @Test
    fun testOrderOfTextNodeCreation() = runTest {
        initDocument()

        val id = Id.next()

        val text = flowOf("test")

        render {
            div(id = id) {
                +"start-"
                text.renderText()
                +"-end"
            }
        }

        delay(250)

        val div = document.getElementById(id) as HTMLDivElement
        assertEquals("start-test-end", div.innerText, "order of text does not match")
    }

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
                        }
                        beforeUnmount { _, _ ->
                            unmounts += 1
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


    @Test
    fun testLifecycleOnGlobalRender() = runTest {
        initDocument()

        var mounts = 0

        render {
            div {
                afterMount { _, _ ->
                    mounts += 1;
                }
            }
        }

        delay(100)
        assertEquals(1, mounts, "afterMount not called")
    }

    @Test
    fun testValueAttributeMountPoint() = runTest {
        initDocument()

        val id = Id.next()

        val store = object : RootStore<String>("test") {
            val modify = handle {
                "modified"
            }
        }

        render {
            div {
                input(id = id) {
                    type("text")
                    value(store.data)
                    clicks handledBy store.modify
                }
            }
        }

        delay(250)

        val input = document.getElementById(id) as HTMLInputElement
        assertEquals("test", input.value, "initial elem.value is not equal")
        assertEquals("test", input.getAttribute("value"), "initial elem.getAttribute(\"value\") is not equal")

        input.click()
        delay(250)

        assertEquals("modified", input.value, "modified elem.value is not equal")
        assertEquals("modified", input.getAttribute("value"), "modified elem.getAttribute(\"value\") is not equal")
    }

    @Test
    fun testCheckedAttributeMountPoint() = runTest {
        initDocument()

        val id = Id.next()

        val store = object : RootStore<Boolean>(true) {
            val modify = handle { model ->
                !model
            }
        }

        render {
            div {
                input(id = id) {
                    type("checkbox")
                    checked(store.data)
                    clicks handledBy store.modify
                }
            }
        }

        delay(250)

        val input = document.getElementById(id) as HTMLInputElement
        assertEquals(true, input.checked, "initial elem.checked is not equal")
        assertEquals("", input.getAttribute("checked"), "initial elem.getAttribute(\"checked\") is not equal")

        input.click()
        delay(250)

        assertEquals(false, input.checked, "modified elem.checked is not equal")
        assertEquals(null, input.getAttribute("checked"), "modified elem.getAttribute(\"checked\") is not equal")
    }

    @Test
    fun testSelectedAttributeMountPoint() = runTest {
        initDocument()

        val id = Id.next()
        val option1Id = "option1-${Id.next()}"
        val option2Id = "option2-${Id.next()}"

        val store = object : RootStore<String>("option1") {
            val select = handle<String> { _, action ->
                action
            }
        }

        render {
            div {
                select(id = id) {
                    option(id = option1Id) {
                        +"option1"
                        selected(store.data.map { it == "option1" })
                    }
                    option(id = option2Id) {
                        +"option2"
                        selected(store.data.map { it == "option2" })
                    }
                    changes.selectedText() handledBy store.select
                }
            }
        }

        delay(250)

        val select = document.getElementById(id) as HTMLSelectElement
        val option1 = document.getElementById(option1Id) as HTMLOptionElement
        val option2 = document.getElementById(option2Id) as HTMLOptionElement
        assertEquals(0, select.selectedIndex, "initial first option is not selected")
        assertEquals(true, option1.selected, "initial first option.selected is not true")
        assertEquals("", option1.getAttribute("selected"), "initial first option.getAttribute(\"selected\") is not filled")
        assertEquals(false, option2.selected, "initial second option.selected is not false")
        assertEquals(null, option2.getAttribute("selected"), "initial second option.getAttribute(\"selected\") is not empty")

        store.select("option2")
        delay(250)

        assertEquals(1, select.selectedIndex, "modified second option is not selected")
        assertEquals(false, option1.selected, "modified first option.selected is not false")
        assertEquals(
            null,
            option1.getAttribute("selected"),
            "modified first option.getAttribute(\"selected\") is not empty"
        )
        assertEquals(true, option2.selected, "modified second option.selected is not true")
        assertEquals(
            "",
            option2.getAttribute("selected"),
            "modified second option.getAttribute(\"selected\") is not filled"
        )
    }

    @Test
    fun testMountTargetNotFoundException() = runTest {
        initDocument()

        assertFailsWith(MountTargetNotFoundException::class) {
            render("error") {
                div {
                    +"div1"
                }
            }
            delay(100)
        }
    }
}