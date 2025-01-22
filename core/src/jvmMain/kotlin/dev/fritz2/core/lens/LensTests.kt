package dev.fritz2.core.lens

import dev.fritz2.core.Lens
import dev.fritz2.core.inspectorOf
import dev.fritz2.core.lensOf

/*
public fun <PARENT> Lens<PARENT, Wohngebaeude>.voraussichtlicherBezugstermin():
        Lens<PARENT, LocalDate?> = this + Wohngebaeude.voraussichtlicherBezugstermin()

 */

data class Name(val vorname: String, val nachname: String)

data class Person(
    val name: Name
)

data class Item(val person: Person)

val personLens = lensOf<Item, Person>("person", { p -> p.person }, { p, v -> p.copy(person = v) })
val nameLens = lensOf<Person, Name>("name", { p -> p.name }, { p, v -> p.copy(name = v) })
val vornameLens = lensOf<Name, String>("vorname", { p -> p.vorname }, { p, v -> p.copy(vorname = v) })

val nothingLens = lensOf<Person, Person>("", { p -> p }, { p, v -> v })

//fun Lens<Item, Person>.nothing(): Lens<Person, Person> = this.plus(nothingLens)

fun <Parent> Lens<Parent, Person>.name(): Lens<Parent, Name> = this.plus(nameLens)
fun <Parent> Lens<Parent, Name>.vorname(): Lens<Parent, String> = this.plus(vornameLens)

fun main(args: Array<String>) {

    val chris = Item(Person(Name("Chris", "Hausknecht")))


    val inspector = inspectorOf(chris)

    //val vornameInspector = inspector.map(personLens).map(nothingLens).map(nameLens).map(vornameLens)
    val chainedInspector = inspector.map(personLens).map(nothingLens.name().vorname())
    //val formatInspector = inspector.map(personLens).map(nothingLens).map(nameLens)

    //println(vornameInspector.path)
    println(chainedInspector.path)
    //println(formatInspector.path)
    //println(vornameInspector.path == chainedInspector.path)
}