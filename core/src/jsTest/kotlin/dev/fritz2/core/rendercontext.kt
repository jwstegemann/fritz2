package dev.fritz2.core

import dev.fritz2.runTest
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLBodyElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.asList
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class RenderContextTests {

    @Test
    fun testMountTargetNotFoundException() = runTest {
        assertFailsWith(MountTargetNotFoundException::class) {
            render("missing") {
                div { +"div" }
            }
        }
    }

    @Test
    fun testShortRenderFunction() = runTest {

        val store = storeOf(true)

        val divId = Id.next()

        render {
            section {
                store.data.render { value ->
                    div(id = divId) {
                        +if (value) "on" else "off"
                    }
                }
            }
        }

        delay(100)

        val div = document.getElementById(divId) as HTMLDivElement

        assertEquals("on", div.textContent)

        store.update(false)
        delay(200)

        val div2 = document.getElementById(divId) as HTMLDivElement

        assertEquals("off", div2.textContent)
    }

    @Test
    fun testRenderIfFunction() = runTest {
        val store = storeOf(true)
        val div1 = Id.next()
        val div2 = Id.next()

        render {
            section {
                store.data.renderIf({ it }) {
                    div(id = div1) {
                        +"on"
                    }
                }
                store.data.renderIf({ !it }) {
                    div(id = div2) {
                        +"off"
                    }
                }
            }
        }

        delay(100)

        fun div(id: String) = document.getElementById(id) as HTMLDivElement?

        assertEquals("on", div(div1)?.textContent)
        assertNull(div(div2))

        store.update(false)
        delay(100)

        assertEquals("off", div(div2)?.textContent)
        assertNull(div(div1))

        store.update(true)
        delay(100)

        assertEquals("on", div(div1)?.textContent)
        assertNull(div(div2))
    }

    @Test
    fun testRenderNotNullFunction() = runTest {
        val store = storeOf<String?>(null)
        val div1 = Id.next()

        render {
            section {
                store.data.renderNotNull { value ->
                    div(id = div1) {
                        +value
                    }
                }
            }
        }

        delay(100)

        fun div(id: String) = document.getElementById(id) as HTMLDivElement?

        assertNull(div(div1))

        store.update("")
        delay(100)

        assertEquals("", div(div1)?.textContent)

        store.update("abc")
        delay(100)

        assertEquals("abc", div(div1)?.textContent)

        store.update(null)
        delay(100)

        assertNull(div(div1))
    }

    interface I
    class A: I
    class B: I

    @Test
    fun testRenderIsFunction() = runTest {

        val store = storeOf<I?>(A())
        val div1 = Id.next()
        val div2 = Id.next()

        render {
            section {
                store.data.renderIs(A::class) {
                    div(id = div1) {
                        +"A"
                    }
                }
                store.data.renderIs(B::class) {
                    div(id = div2) {
                        +"B"
                    }
                }
            }
        }

        delay(100)

        fun div(id: String) = document.getElementById(id) as HTMLDivElement?

        assertEquals("A", div(div1)?.textContent)
        assertNull(div(div2))

        store.update(B())
        delay(100)

        assertEquals("B", div(div2)?.textContent)
        assertNull(div(div1))

        store.update(null)
        delay(100)

        assertNull(div(div1))
        assertNull(div(div2))
    }

    @Test
    fun testRenderFunction() = runTest {
        document.body?.id = "target"

        val store = storeOf(true)
        val divId = Id.next()

        render("#target") {
            div(id = divId) {
                store.data.render { value ->
                    if (value) div { +"on" } else span { +"off" }
                }
            }
        }

        delay(100)

        val target = document.getElementById("target") as HTMLBodyElement
        assertEquals("target", target.id)
        assertEquals(1, target.childElementCount)
        assertEquals(divId, target.firstElementChild?.id)

        val div = document.getElementById(divId) as HTMLDivElement

        assertEquals(1, div.firstElementChild?.childElementCount)
        assertEquals("DIV", div.firstElementChild?.firstChild?.nodeName)
        assertEquals("on", div.firstElementChild?.textContent)

        store.update(false)
        delay(200)

        val span = document.getElementById(divId) as HTMLDivElement
        assertEquals(1, span.firstElementChild?.childElementCount)
        assertEquals("SPAN", span.firstElementChild?.firstChild?.nodeName)
        assertEquals("off", span.firstElementChild?.textContent)
    }

    @Test
    fun testRenderReactsOnlyToNewValues() = runTest {
        fun getSpanText(id: String) = (document.getElementById(id) as HTMLSpanElement).textContent

        data class Model(val static: String, val reactive: String)

        val uniqueHashes = generateSequence(1) { it + 1 }.map(Int::toString).iterator()
        val model = Model("fritz", "RC-3")
        val store = storeOf(model)
        val idValueReactive = Id.next()
        val idHash = Id.next()

        render {
            div {
                store.data.map { it.static }.render { _ ->
                    span(id = idHash) { +uniqueHashes.next() }
                }
                store.data.map { it.reactive }.render { reactive ->
                    span(id = idValueReactive) { +reactive }
                }
            }
        }

        delay(100)

        assertEquals("RC-3", getSpanText(idValueReactive))
        assertEquals("1", getSpanText(idHash))

        // update with repeated value should omit rendering
        store.update(model.copy(reactive = "RC-4"))
        delay(100)

        assertEquals("RC-4", getSpanText(idValueReactive))
        assertEquals("1", getSpanText(idHash))

        // update with new value should trigger re-render
        store.update(Model("fritz2", "1.0-FINAL"))
        delay(100)

        assertEquals("1.0-FINAL", getSpanText(idValueReactive))
        assertEquals("2", getSpanText(idHash))
    }


    @Test
    fun renderEachWithInsertingItemsSoThatTheSameInsertPatchMightBeCreatedWillStillRenderInsertedItemsInExpectedPosition() =
        runTest {
            val idList = Id.next()
            fun joinLiTextNodes() = document.getElementById(idList)!!.childNodes.asList()
                .mapNotNull { it.textContent }.joinToString("")

            data class Item(val id: String)

            val a = Item("1")
            val b = Item("2")
            val c = Item("3")

            val storedItems = storeOf(listOf(a, b, c))
            val insertFirstItemAtBeginning = storedItems.handle { state ->
                buildList {
                    add(a)
                    addAll(state)
                }
            }

            render {
                ul(id = idList) {
                    storedItems.data.renderEach(into = this) { item ->
                        li { +item.id }
                    }
                }
            }

            delay(100)

            insertFirstItemAtBeginning() // could generate Patch `Insert(element=Item("1"), index=1)`
            delay(50)
            assertEquals("1123", joinLiTextNodes())

            insertFirstItemAtBeginning() // could generate Patch `Insert(element=Item("1"), index=1)` ->
            // the same as above, must pass `distinctUntilChanged` filter of `mountSimple`!
            delay(50)
            assertEquals("11123", joinLiTextNodes())
        }


    @Test
    fun renderEachWithInsertingItemsSoThatTheSameInsertMayPatchMightBeCreatedWillStillRenderInsertedItemsInExpectedPosition() =
        runTest {
            val idList = Id.next()
            fun joinLiTextNodes() = document.getElementById(idList)!!.childNodes.asList()
                .mapNotNull { it.textContent }.joinToString("")

            data class Item(val id: String)

            val a = Item("1")
            val b = Item("2")
            val c = Item("3")

            val storedItems = storeOf(listOf(a, b, c))
            val insertFirstItemAtBeginning = storedItems.handle { state ->
                buildList {
                    add(a)
                    add(b)
                    addAll(state)
                }
            }

            render {
                ul(id = idList) {
                    storedItems.data.renderEach(into = this) { item ->
                        li { +item.id }
                    }
                }
            }

            delay(100)

            insertFirstItemAtBeginning() // could generate Patch `InsertMany(element=[Item("1"), Item("2")], index=1)`
            delay(50)
            assertEquals("12123", joinLiTextNodes())

            insertFirstItemAtBeginning() // could generate Patch `InsertMany(element=[Item("1"), Item("2")], index=1)` ->
            // the same as above, must pass `distinctUntilChanged` filter of `mountSimple`!
            delay(50)
            assertEquals("1212123", joinLiTextNodes())
        }

    @Test
    fun renderEachWithDeletingItemsSoThatTheSameDeletePatchIsCreatedWillStillRemoveDeletedItemsFromDom() = runTest {

        data class Item(val id: Int)

        val storedItems = storeOf((1..3).map { Item(it) })
        val dropFirstItem = storedItems.handle { state -> state.drop(1) }

        val idList = Id.next()

        render {
            ul(id = idList) {
                storedItems.data.renderEach(into = this) { item ->
                    li { +item.toString() }
                }
            }
        }

        delay(100)

        val ul = document.getElementById(idList)
        assertEquals(3, ul?.childElementCount)

        dropFirstItem() // will generate Patch `Delete(0, 1)`
        delay(50)
        assertEquals(2, ul?.childElementCount)

        dropFirstItem() // will generate Patch `Delete(0, 1)` -> the same as above, must pass `distinctUntilChanged`
        // filter of `mountSimple`!
        delay(50)
        assertEquals(1, ul?.childElementCount)
    }

    @Test
    fun renderEachWithSwitchingItemsSoThatTheSameMovePatchIsCreatedWillStillRenderMovedItemsInExpectedOrder() =
        runTest {
            val idList = Id.next()
            fun joinLiTextNodes() = document.getElementById(idList)!!.childNodes.asList()
                .mapNotNull { it.textContent }.joinToString("")

            data class Item(val id: String)

            val a = Item("1")
            val b = Item("2")
            val c = Item("3")

            val storedItems = storeOf(listOf(a, b, c))
            val swapFirstAndSecondElement = storedItems.handle { state ->
                buildList {
                    add(state.drop(1).take(1).first())
                    add(state.first())
                    addAll(state.drop(2))
                }
            }


            render {
                ul(id = idList) {
                    storedItems.data.renderEach(into = this) { item ->
                        li { +item.id }
                    }
                }
            }

            delay(100)

            swapFirstAndSecondElement() // will generate Patch `Move(0, 2)`
            delay(50)
            assertEquals("213", joinLiTextNodes())

            swapFirstAndSecondElement() // will generate Patch `Move(0, 2)` -> the same as above,
            // must pass `distinctUntilChanged` filter of `mountSimple`!
            delay(50)
            assertEquals("123", joinLiTextNodes())
        }
}