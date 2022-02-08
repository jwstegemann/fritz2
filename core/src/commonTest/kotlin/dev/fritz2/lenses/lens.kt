package dev.fritz2.lenses

import kotlin.test.Test
import kotlin.test.assertEquals

class LensesTests {

    data class Size(val height: Double)

    private val heightLens = lens(Size::height.name, Size::height) { p, v -> p.copy(height = v) }

    data class Tree(val name: String, val age: Int, val size: Size)

    private val ageLens = lens(Tree::age.name, Tree::age) { p, v -> p.copy(age = v) }
    private val sizeLens = lens(Tree::size.name, Tree::size) { p, v -> p.copy(size = v) }

    @Test
    fun testFormat() {
        val p = Tree("Mammut Tree", 3000, Size(84.3))

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

}