package io.fritz2.binding

import io.fritz2.dom.html.render
import io.fritz2.dom.mount
import io.fritz2.test.initDocument
import io.fritz2.test.randomId
import io.fritz2.test.runTest
import io.fritz2.test.targetId
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLLIElement
import org.w3c.dom.HTMLUListElement
import org.w3c.dom.get
import kotlin.browser.document
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


class SeqTests {

    @Test
    @Ignore
    fun testSeqMap() = runTest {
        initDocument()

        val testId = randomId()

        val store = object : RootStore<List<Int>>(listOf(0)) {
            val replaceList = handle {
                (0..10).toList()
            }

            val addAtBeginning = handle { list ->
                listOf(0) + list
            }

            val addAtEnd = handle { list ->
                list + 10
            }

            val addAtMiddle = handle { list ->
                list.subList(0, 7) + listOf(4, 5, 6) + list.subList(7, list.size)
            }

            val removeAtBeginning = handle { list ->
                list.subList(1, list.size)
            }

            val removeAtEnd = handle { list ->
                list.subList(0, list.size - 1)
            }

            val removeAtMiddle = handle { list ->
                list.subList(0, 6) + list.subList(9, list.size)
            }

            val filterEven = handle { list ->
                list.filter { it % 2 == 0 }
            }

            val reverse = handle { list ->
                list.asReversed()
            }
        }


        render {
            section {
                ul(id = testId) {
                    store.data.each().map { i ->
                        render {
                            li(id = "entry$i") {
                                text(i.toString())
                            }
                        }
                    }.bind()
                }

                button(id = "replaceList") {
                    clicks handledBy store.replaceList
                }
                button(id = "addAtBeginning") {
                    clicks handledBy store.addAtBeginning
                }
                button(id = "addAtEnd") {
                    clicks handledBy store.addAtEnd
                }
                button(id = "addAtMiddle") {
                    clicks handledBy store.addAtMiddle
                }
                button(id = "removeAtBeginning") {
                    clicks handledBy store.removeAtBeginning
                }
                button(id = "removeAtEnd") {
                    clicks handledBy store.removeAtEnd
                }
                button(id = "removeAtMiddle") {
                    clicks handledBy store.removeAtMiddle
                }
                button(id = "filterEven") {
                    clicks handledBy store.filterEven
                }
                button(id = "reverse") {
                    clicks handledBy store.reverse
                }
            }
        }.mount(targetId)

        val list = document.getElementById(testId).unsafeCast<HTMLUListElement>()
        val until = list.children.length - 1

        fun check(expected: List<Int>) {
            for (i in 0..until) {
                val element = list.children[i].unsafeCast<HTMLLIElement>()
                assertEquals("entry${expected[i]}", element.id)
                assertEquals(expected[i].toString(), element.textContent)
            }
        }

        //inital
        check(listOf(0))

        document.getElementById("replaceList").unsafeCast<HTMLButtonElement>().click()
        delay(100)
        check(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

        document.getElementById("addAtBeginning").unsafeCast<HTMLButtonElement>().click()
        delay(100)
        check(listOf(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

        document.getElementById("addAtEnd").unsafeCast<HTMLButtonElement>().click()
        delay(100)
        check(listOf(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10))

        document.getElementById("addAtMiddle").unsafeCast<HTMLButtonElement>().click()
        delay(100)
        check(listOf(0, 0, 1, 2, 3, 4, 5, 4, 5, 6, 6, 7, 8, 9, 10, 10))

        document.getElementById("removeAtBeginning").unsafeCast<HTMLButtonElement>().click()
        delay(100)
        check(listOf(0, 1, 2, 3, 4, 5, 4, 5, 6, 6, 7, 8, 9, 10, 10))

        document.getElementById("removeAtEnd").unsafeCast<HTMLButtonElement>().click()
        delay(100)
        check(listOf(0, 1, 2, 3, 4, 5, 4, 5, 6, 6, 7, 8, 9, 10))

        document.getElementById("removeAtMiddle").unsafeCast<HTMLButtonElement>().click()
        delay(100)
        check(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

        document.getElementById("filterEven").unsafeCast<HTMLButtonElement>().click()
        delay(100)
        check(listOf(0, 2, 4, 6, 8, 10))

        document.getElementById("reverse").unsafeCast<HTMLButtonElement>().click()
        delay(100)
        check(listOf(10, 8, 6, 4, 2, 0))
    }
}