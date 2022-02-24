package dev.fritz2.headless.foundation

import kotlin.test.Test
import kotlin.test.assertEquals

class TestRotateNextFunctions {

    @Test
    fun rotateNextOnFirstElementGivesSecondElement() {
        assertEquals("second", listOf("first", "second").rotateNext("first"))
        assertEquals("second", listOf("first", "second", "third").rotateNext("first"))
    }

    @Test
    fun rotateNextOnSomeElementGivesNextElement() {
        assertEquals("third", listOf("first", "second", "third").rotateNext("second"))
    }

    @Test
    fun rotateNextOnLastElementGivesFirstElement() {
        assertEquals("first", listOf("first").rotateNext("first"))
        assertEquals("first", listOf("first", "second").rotateNext("second"))
        assertEquals("first", listOf("first", "second", "third").rotateNext("third"))
    }

    @Test
    fun rotateNextWithUnknownElementGivesNull() {
        assertEquals(null, emptyList<String>().rotateNext("third"))
        assertEquals(null, listOf("first", "second").rotateNext("third"))
    }
}

class TestRotatePreviousFunctions {

    @Test
    fun rotatePreviousOnSecondElementGivesFirstElement() {
        assertEquals("first", listOf("first", "second").rotatePrevious("second"))
        assertEquals("first", listOf("first", "second", "third").rotatePrevious("second"))
    }

    @Test
    fun rotatePreviousOnSomeElementGivesPreviousElement() {
        assertEquals("second", listOf("first", "second", "third").rotatePrevious("third"))
    }

    @Test
    fun rotatePreviousOnFirstElementGivesLastElement() {
        assertEquals("first", listOf("first").rotatePrevious("first"))
        assertEquals("second", listOf("first", "second").rotatePrevious("first"))
        assertEquals("third", listOf("first", "second", "third").rotatePrevious("first"))
    }

    @Test
    fun rotatePreviousWithUnknownElementGivesNull() {
        assertEquals(null, emptyList<String>().rotatePrevious("third"))
        assertEquals(null, listOf("first", "second").rotatePrevious("third"))
    }
}