package dev.fritz2.core.lens

import dev.fritz2.core.inspectorOf
import dev.fritz2.core.lensOf
import org.junit.Test

class LensesTests {

    @Test
    fun chainingLensesWorksSameAsSubsequentialChaining() {
        data class Name(val vorname: String, val nachname: String)

        data class Person(
            val name: Name
        )

        val chris = Person(Name("Chris", "Hausknecht"))

        val nameLens = lensOf<Person, Name>("name", { p -> p.name }, { p, v -> p.copy(name = v) })
        val vornameLens = lensOf<Name, String>("vorname", { p -> p.vorname }, { p, v -> p.copy(vorname = v) })

        val inspector = inspectorOf(chris)

        val vornameInspector = inspector.map(nameLens).map(vornameLens)

    }
}