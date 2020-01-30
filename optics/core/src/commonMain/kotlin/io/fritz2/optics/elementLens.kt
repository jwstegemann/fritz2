package io.fritz2.optics

fun <T : withId> elementLens(element: T): Lens<List<T>, T> = object : Lens<List<T>, T> {
    override fun get(parent: List<T>): T = checkNotNull(parent.find {
        it.id == element.id
    })

    override fun set(parent: List<T>, value: T): List<T> = parent.map {
        if (it.id == value.id) value else it
    }
}

interface withId {
    val id: String
}