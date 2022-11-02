package dev.fritz2.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LensesTests {

    data class Tree(val name: String, val age: Int, val size: Size, val tags: List<String>)
    data class Size(val height: Double)

    private val heightLens = lens(Size::height.name, Size::height) { p, v -> p.copy(height = v) }
    private val ageLens = lens(Tree::age.name, Tree::age) { p, v -> p.copy(age = v) }
    private val sizeLens = lens(Tree::size.name, Tree::size) { p, v -> p.copy(size = v) }
    private val tagLens = lens(Tree::tags.name, Tree::tags) { p, v -> p.copy(tags = v) }

    @Test
    fun testFormatLens() {
        val p = Tree("Mammut Tree", 3000, Size(84.3), emptyList())

        val intFormatLens = format(String::toInt, Int::toString)
        val doubleFormatLens = format(String::toDouble, Double::toString)

        val formattedAgeLens = ageLens + intFormatLens
        assertEquals(p.age.toString(), formattedAgeLens.get(p), "get on formattedAgeLens did not work")
        assertEquals(Tree::age.name, formattedAgeLens.id, "id on formattedAgeLens did not work")
        assertEquals(formattedAgeLens.set(p, "2000").age, 2000, "set on formattedAgeLens did not work")

        val formattedSizeLens = sizeLens + heightLens + doubleFormatLens
        assertEquals(p.size.height.toString(), formattedSizeLens.get(p), "get on formattedSizeLens did not work")
        assertEquals(
            "${Tree::size.name}.${Size::height.name}",
            formattedSizeLens.id,
            "id on formattedSizeLens did not work"
        )
        assertEquals(formattedSizeLens.set(p, "55.12").size.height, 55.12, "set on formattedSizeLens did not work")
    }

    @Test
    fun testCollectionLensSetExceptions() {
        val list = listOf("a", "b", "c", "a")
        val map = mapOf(1 to "a", 2 to "b", 3 to "c")
        assertEquals(listOf("a", "d", "c", "a"), lensOf("b") { it }.set(list, "d"))
        assertEquals(listOf("a", "d", "c", "a"), lensOf<String>(1).set(list, "d"))
        assertEquals(mapOf(1 to "a", 2 to "d", 3 to "c"), lensOf<Int, String>(2).set(map, "d"))

        assertFailsWith<CollectionLensSetException> {
            lensOf("d") { it }.set(list, "e")
        }
        assertFailsWith<CollectionLensSetException> {
            lensOf("a") { it }.set(list, "d")
        }
        assertFailsWith<CollectionLensSetException> {
            lensOf<String>(-1).set(list, "d")
        }
        assertFailsWith<CollectionLensSetException> {
            lensOf<String>(list.size).set(list, "d")
        }
        assertFailsWith<CollectionLensSetException> {
            lensOf<Int, String>(0).set(map, "d")
        }
    }

}