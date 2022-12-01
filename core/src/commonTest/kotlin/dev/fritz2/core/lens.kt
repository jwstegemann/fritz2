package dev.fritz2.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LensesTests {

    data class Tree(val name: String, val age: Int, val size: Size, val tags: List<String>)
    data class Size(val height: Double)

    private val heightLens = lensOf(Size::height.name, Size::height) { p, v -> p.copy(height = v) }
    private val ageLens = lensOf(Tree::age.name, Tree::age) { p, v -> p.copy(age = v) }
    private val sizeLens = lensOf(Tree::size.name, Tree::size) { p, v -> p.copy(size = v) }
    private val tagLens = lensOf(Tree::tags.name, Tree::tags) { p, v -> p.copy(tags = v) }

    @Test
    fun testFormatLens() {
        val p = Tree("Mammut Tree", 3000, Size(84.3), emptyList())

        val intFormatLens = formatOf(String::toInt, Int::toString)
        val doubleFormatLens = formatOf(String::toDouble, Double::toString)

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
        assertEquals(listOf("a", "d", "c", "a"), lensForElement("b") { it }.set(list, "d"))
        assertEquals(listOf("a", "d", "c", "a"), lensForElement<String>(1).set(list, "d"))
        assertEquals(mapOf(1 to "a", 2 to "d", 3 to "c"), lensForElement<Int, String>(2).set(map, "d"))

        assertFailsWith<CollectionLensSetException> {
            lensForElement("d") { it }.set(list, "e")
        }
        assertFailsWith<CollectionLensSetException> {
            lensForElement("a") { it }.set(list, "d")
        }
        assertFailsWith<CollectionLensSetException> {
            lensForElement<String>(-1).set(list, "d")
        }
        assertFailsWith<CollectionLensSetException> {
            lensForElement<String>(list.size).set(list, "d")
        }
        assertFailsWith<CollectionLensSetException> {
            lensForElement<Int, String>(0).set(map, "d")
        }
    }

    @Test
    fun testDefaultLens() {
        val defaultValue = "fritz2"
        val nonNullValue = "some value"
        val defaultLens = defaultLens("", defaultValue)

        assertEquals(defaultValue, defaultLens.get(null), "default value not applied on null")
        assertEquals(nonNullValue, defaultLens.get(nonNullValue), "wrong value on not-null")

        assertEquals(nonNullValue, defaultLens.set(null, nonNullValue), "value not set on null")
        assertEquals(nonNullValue, defaultLens.set("old Value", nonNullValue), "value not set on null")
        assertEquals(null, defaultLens.set(nonNullValue, defaultValue), "not set to null on default")
    }

    @Test
    fun testNotNullLens() {
        data class PostalAddress(val street: String, val co: String?)

        val streetLens = lensOf("street", PostalAddress::street) { p, v -> p.copy(street = v) }
        val someStreet = "some street"
        val newValue = "new value"
        val someCo = "some co"

        val addressWithCo = PostalAddress(someStreet, someCo)

        val notNullLens: Lens<PostalAddress?, String> = streetLens.withNullParent()

        assertEquals(someStreet, notNullLens.get(addressWithCo), "not null lens does get value on non null parent")
        assertFailsWith(NullPointerException::class, "not null lens does not throw exception when get on null parent") { notNullLens.get(null) }

        assertEquals(newValue, notNullLens.set(addressWithCo, newValue)?.street, "not null lens does set value on non null parent")
        assertFailsWith(NullPointerException::class, "not null lens does not throw exception when set on null parent") { notNullLens.set(null, newValue)?.street }
    }

}