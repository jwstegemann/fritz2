package dev.fritz2.lenses

fun <T : WithId> elementLens(element: T): Lens<List<T>, T> = object :
    Lens<List<T>, T> {
    override val _id: String = element.id

    override fun get(parent: List<T>): T = checkNotNull(parent.find {
        it.id == element.id
    })

    override fun set(parent: List<T>, value: T): List<T> = parent.map {
        if (it.id == value.id) value else it
    }

}

interface WithId {
    val id: String
}