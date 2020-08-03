package dev.fritz2.lenses

import kotlin.test.Test
import kotlin.test.assertEquals

class LensesTests {

    @Lenses
    data class Size(val height: Double)

    val heightLens = buildLens(Size::height.name, Size::height) { p, v -> p.copy(height = v) }

    @Lenses
    data class Tree(val name: String, val age: Int, val size: Size)

    val ageLens = buildLens(Tree::age.name, Tree::age) { p, v -> p.copy(age = v) }
    val sizeLens = buildLens(Tree::size.name, Tree::size) { p, v -> p.copy(size = v) }

    @Test
    fun testLensesWithUsing() {
        val p = Tree("test", 5, Size(1.83))
        val ageAsStringLens = ageLens.using(String::toInt, Int::toString)
        assertEquals(p.age.toString(), ageAsStringLens.get(p), "using on lens did not work")

        val sizeAsStringLens = sizeLens + heightLens.using(String::toDouble, Double::toString)
        assertEquals(p.size.height.toString(), sizeAsStringLens.get(p), "using on combined lens did not work")
    }
}