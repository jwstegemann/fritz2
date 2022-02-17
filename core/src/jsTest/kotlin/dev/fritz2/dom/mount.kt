package dev.fritz2.dom

import dev.fritz2.core.RootStore
import dev.fritz2.core.storeOf
import dev.fritz2.identification.Id
import dev.fritz2.initDocument
import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLOptionElement
import org.w3c.dom.HTMLSelectElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MountTests {

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