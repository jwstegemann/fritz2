package dev.fritz2.lenses

typealias idProvider<T> = (T) -> String

fun <T> elementLens(element: T, id: idProvider<T>): Lens<List<T>, T> = object :
    Lens<List<T>, T> {
    override val _id: String = id(element)

    override fun get(parent: List<T>): T = parent.find {
        id(it) == id(element)
    } ?: throw IndexOutOfBoundsException()

    override fun set(parent: List<T>, value: T): List<T> = parent.map {
        if (id(it) == id(value)) value else it
    }

}
