package dev.fritz2.core

import kotlin.test.Test
import kotlin.test.assertEquals

class LensesHelperTests {

    data class Tree(val name: String, val age: Int, val size: Size, val tags: List<String>)
    data class Size(val height: Double)

    private val heightLens = lens(Size::height.name, Size::height) { p, v -> p.copy(height = v) }
    private val ageLens = lens(Tree::age.name, Tree::age) { p, v -> p.copy(age = v) }
    private val sizeLens = lens(Tree::size.name, Tree::size) { p, v -> p.copy(size = v) }
    private val tagLens = lens(Tree::tags.name, Tree::tags) { p, v -> p.copy(tags = v) }


    @Test
    fun testHelper() {
        val p = Tree("Mammut Tree", 3000, Size(84.3), listOf("giant", "heavy", "world record"))

        val formattedAgeLens = ageLens.asString()
        assertEquals(p.age.toString(), formattedAgeLens.get(p), "get on formattedAgeLens did not work")
        assertEquals(Tree::age.name, formattedAgeLens.id, "id on formattedAgeLens did not work")
        assertEquals(formattedAgeLens.set(p, "2000").age, 2000, "set on formattedAgeLens did not work")

        val formattedSizeLens = sizeLens + heightLens.asString()
        assertEquals(
            p.size.height.toString(),
            formattedSizeLens.get(p),
            "get on formattedSizeLens did not work"
        )
        assertEquals(
            "${Tree::size.name}.${Size::height.name}",
            formattedSizeLens.id,
            "id on formattedSizeLens did not work"
        )
        assertEquals(
            formattedSizeLens.set(p, "55.12").size.height,
            55.12,
            "set on formattedSizeLens did not work"
        )

        val formattedTags = tagLens.asString(", ", "[", "]")
        assertEquals(p.tags.toString(), formattedTags.get(p), "get on formattedTags did not work")
        assertEquals(
            Tree::tags.name,
            formattedTags.id,
            "id on formattedTags did not work"
        )
        assertEquals(
            formattedTags.set(p, "[giant, heavy, world record]").tags,
            p.tags,
            "set on formattedTags did not work"
        )
    }
}