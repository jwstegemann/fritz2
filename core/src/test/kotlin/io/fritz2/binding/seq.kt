package io.fritz2.binding

import io.fritz2.dom.html.html
import io.fritz2.dom.mount
import io.fritz2.test.initDocument
import io.fritz2.test.randomId
import io.fritz2.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLLIElement
import org.w3c.dom.HTMLUListElement
import org.w3c.dom.get
import kotlin.browser.document
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

@FlowPreview
@ExperimentalCoroutinesApi
class SeqTests {

    @Test @Ignore
    fun testSeqMap() = runTest {
        initDocument()

        val testId = randomId()

        val store = object : RootStore<List<Int>>(listOf(0)) {
            val replaceList = handle<Any> { _, _ ->
                (0..10).toList()
            }

            val addAtBeginning = handle<Any> { list, _ ->
                listOf(0) + list
            }

            val addAtEnd = handle<Any> { list, _ ->
                list + 10
            }

            val addAtMiddle = handle<Any> { list, _ ->
                list.subList(0, 7) + listOf(4, 5, 6) + list.subList(7, list.size)
            }

            val removeAtBeginning = handle<Any> { list, _ ->
                list.subList(1, list.size)
            }

            val removeAtEnd = handle<Any> { list, _ ->
                list.subList(0, list.size - 1)
            }

            val removeAtMiddle = handle<Any> { list, _ ->
                list.subList(0, 6) + list.subList(9, list.size)
            }

            val filterEven = handle<Any> { list, _ ->
                list.filter { it % 2 == 0 }
            }

            val reverse = handle<Any> { list, _ ->
                list.asReversed()
            }
        }


        html {
            section {
                ul(testId) {
                    store.data.each().map { i ->
                        html {
                            li("entry$i") {
                                +i.toString()
                            }
                        }
                    }.bind()
                }

                button("replaceList") {
                    +"replaceList"
                    store.replaceList <= clicks()
                }
                button("addAtBeginning") {
                    +"addAtBeginning"
                    store.addAtBeginning <= clicks()
                }
                button("addAtEnd") {
                    +"addAtEnd"
                    store.addAtEnd <= clicks()
                }
                button("addAtMiddle") {
                    +"addAtMiddle"
                    store.addAtMiddle <= clicks()
                }
                button("removeAtBeginning") {
                    +"removeAtBeginning"
                    store.removeAtBeginning <= clicks()
                }
                button("removeAtEnd") {
                    +"removeAtEnd"
                    store.removeAtEnd <= clicks()
                }
                button("removeAtMiddle") {
                    +"removeAtMiddle"
                    store.removeAtMiddle <= clicks()
                }
                button("filterEven") {
                    +"filterEven"
                    store.filterEven <= clicks()
                }
                button("reverse") {
                    +"reverse"
                    store.reverse <= clicks()
                }
            }
        }.mount("target")

        delay(100)

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


//    @Test @Ignore
//    fun testSeqFlatMap() = runTest {
//
//        val store1 = object : RootStore<List<Int>>((0..10).toList()) {
//            val filterEven = handle<Any> { list, _ ->
//                list.filter { it % 2 == 0 }
//            }
//        }
//
//        val store2 = object : RootStore<List<Int>>((0..10).toList()) {
//            val filterOdd = handle<Any> { list, _ ->
//                list.filter { it % 2 == 1 }
//            }
//        }
//
//
//        html {
//            section {
//                ul("list") {
//                    store1.data.each().flatMap { i1 ->
//                        store2.data.each().map { i2 ->
//                            html {
//                                li("entry$i") {
//                                    +i.toString()
//                                }
//                            }
//                        }
//                    }.bind()
//                }
//
//                button("filterEven") {
//                    +"filterEven"
//                    store1.filterEven <= clicks
//                }
//                button("filterOdd") {
//                    +"filterOdd"
//                    store2.filterOdd <= clicks
//                }
//            }
//        }.mount("target")
//
//        delay(250)
//
//        val list = document.getElementById("list").unsafeCast<HTMLUListElement>()
//        val until = list.children.length - 1
//
//        fun check(expected: List<Int>) {
//            for (i in 0..until) {
//                val element = list.children[i].unsafeCast<HTMLLIElement>()
//                assertEquals("entry${expected[i]}", element.id)
//                assertEquals(expected[i].toString(), element.innerText)
//            }
//        }
//
//        //inital
//        check(listOf(0))
//    }

}