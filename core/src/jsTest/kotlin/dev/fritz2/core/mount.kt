package dev.fritz2.core

import dev.fritz2.checkSingleFlow
import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLOptionElement
import org.w3c.dom.HTMLSelectElement
import kotlin.js.Promise
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@OptIn(DelicateCoroutinesApi::class)
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

        store.apply {
            store.data handledBy {}
        }

        return GlobalScope.promise {
            values.forEach { value ->
                store.enqueue { value }
            }
            done.await()
        }
    }


    @Test
    fun testOrderOfSingleMountPointCreation() = runTest {

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
                            mounts += 1
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
    fun testNestedLifecycleHandler() = runTest {
        val outerStore = storeOf(0)
        val innerStore = storeOf(0)
        val listStore = storeOf(listOf("123"))

        var outerMountsCounter = 0
        var outerUnmountsCounter = 0

        var innerMountsCounter = 0
        var innerUnmountsCounter = 0

        var listMountsCounter = 0
        var listUnmountsCounter = 0

        render {
            outerStore.data.render {
                afterMount { _, _ -> outerMountsCounter += 1 }
                beforeUnmount { _, _ -> outerUnmountsCounter += 1 }

                innerStore.data.render {
                    afterMount { _, _ -> innerMountsCounter += 1 }
                    beforeUnmount { _, _ -> innerUnmountsCounter += 1 }

                    listStore.renderEach({ it }) {
                        div {
                            afterMount { _, _ -> listMountsCounter += 1 }
                            beforeUnmount { _, _ -> listUnmountsCounter += 1 }
                        }
                    }
                }
            }
        }

        /**
         * Checks, whether the amount of afterMount/beforeUnmount-Calls match the excepted values for the given action
         */
        suspend fun check(
            title: String,
            outerMount: Int = 0,
            outerUnmounts: Int = 0,
            innerMounts: Int = 0,
            innerUnmounts: Int = 0,
            listMounts: Int = 0,
            listUnmounts: Int = 0,
            action: () -> Unit
        ) {
            // Reset Values
            outerMountsCounter = 0
            outerUnmountsCounter = 0
            innerMountsCounter = 0
            innerUnmountsCounter = 0
            listMountsCounter = 0
            listUnmountsCounter = 0

            // Invoke Aktion
            action.invoke()
            delay(100)

            // Check Results
            assertEquals(outerMount, outerMountsCounter, "$title - outerMounts wrong")
            assertEquals(outerUnmounts, outerUnmountsCounter, "$title - outerUnmounts wrong")
            assertEquals(innerMounts, innerMountsCounter, "$title - innerMounts wrong")
            assertEquals(innerUnmounts, innerUnmountsCounter, "$title - innerUnmounts wrong")
            assertEquals(listMounts, listMountsCounter, "$title - listMounts wrong")
            assertEquals(listUnmounts, listUnmountsCounter, "$title - listUnmounts wrong")
        }

        check(
            "Initial",
            outerMount = 1,
            innerMounts = 1,
            listMounts = 1,
        ) {}

        check(
            "Add to listStore",
            listMounts = 1
        ) { listStore.update(listOf("123", "234")) }

        check(
            "Remove from listStore",
            listUnmounts = 1
        ) { listStore.update(listOf("123")) }

        check(
            "Update InnerStore",
            innerMounts = 1, innerUnmounts = 1,
            listMounts = 1, listUnmounts = 1
        ) { innerStore.update(1) }

        check(
            "Update OuterStore",
            outerMount = 1, outerUnmounts = 1,
            innerMounts = 1, innerUnmounts = 1,
            listMounts = 1, listUnmounts = 1
        ) { outerStore.update(1) }

        check(
            "Add to listStore",
            listMounts = 1
        ) { listStore.update(listOf("123", "234")) }

        check(
            "Update OuterStore",
            outerMount = 1, outerUnmounts = 1,
            innerMounts = 1, innerUnmounts = 1,
            listMounts = 2, listUnmounts = 2
        ) { outerStore.update(2) }
    }


    @Test
    fun testNestedRenderEachLifecycleHandler() = runTest {
        val renderStore = storeOf(0)
        val renderEachStore = storeOf(listOf("123"))

        var renderMountsCounter = 0
        var renderUnmountsCounter = 0

        var renderEachMountsCounter = 0
        var renderEachUnmountsCounter = 0

        render {
            renderEachStore.renderEach({ it }) {
                div {
                    afterMount { _, _ -> renderEachMountsCounter += 1 }
                    beforeUnmount { _, _ -> renderEachUnmountsCounter += 1 }

                    renderStore.data.render {
                        afterMount { _, _ -> renderMountsCounter += 1 }
                        beforeUnmount { _, _ -> renderUnmountsCounter += 1 }
                    }

                }
            }

        }

        /**
         * Checks, whether the amount of afterMount/beforeUnmount-Calls match the excepted values for the given action
         */
        suspend fun check(
            title: String,
            renderMounts: Int = 0,
            renderUnmounts: Int = 0,
            renderEachMounts: Int = 0,
            renderEachUnmounts: Int = 0,
            action: () -> Unit
        ) {
            // Reset Values
            renderMountsCounter = 0
            renderUnmountsCounter = 0
            renderEachMountsCounter = 0
            renderEachUnmountsCounter = 0

            // Invoke Aktion
            action.invoke()
            delay(100)

            // Check Results
            assertEquals(renderMounts, renderMountsCounter, "$title - renderMounts wrong")
            assertEquals(renderUnmounts, renderUnmountsCounter, "$title - renderUnmounts wrong")
            assertEquals(renderEachMounts, renderEachMountsCounter, "$title - renderEachMounts wrong")
            assertEquals(renderEachUnmounts, renderEachUnmountsCounter, "$title - renderEachUnmounts wrong")
        }

        check(
            "Initial",
            renderMounts = 1,
            renderEachMounts = 1,
        ) {}

        check(
            "update renderStore",
            renderMounts = 1,
            renderUnmounts = 1
        ) {
            renderStore.update(2)
        }

        check(
            "Add to renderEach",
            renderMounts = 1, renderEachMounts = 1
        ) {
            renderEachStore.update(listOf("123", "222"))
        }

        check(
            "Remove second element from renderEach",
            renderUnmounts = 1, renderEachUnmounts = 1
        ) {
            renderEachStore.update(listOf("123"))
        }

        check(
            "Remove all elements from renderEach",
            renderUnmounts = 1, renderEachUnmounts = 1
        ) {
            renderEachStore.update(emptyList())
        }


    }


    @Test
    fun testLifecycleOnGlobalRender() = runTest {

        var mounts = 0

        render {
            div {
                afterMount { _, _ ->
                    mounts += 1
                }
            }
        }

        delay(100)
        assertEquals(1, mounts, "afterMount not called")
    }

    @Test
    fun testValueAttributeMountPoint() = runTest {

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
        assertEquals(
            "",
            option1.getAttribute("selected"),
            "initial first option.getAttribute(\"selected\") is not filled"
        )
        assertEquals(false, option2.selected, "initial second option.selected is not false")
        assertEquals(
            null,
            option2.getAttribute("selected"),
            "initial second option.getAttribute(\"selected\") is not empty"
        )

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