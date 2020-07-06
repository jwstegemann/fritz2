package dev.fritz2.lenses

/**
 * function to derive a valid id for a given instance that does not change over time.
 */
typealias idProvider<T> = (T) -> String

/**
 * creates a [Lens] pointing to a certain element in a list
 *
 * @param element current instance of the element to focus on
 * @param id [idProvider] to identify the element in the list (i.e. when it's content changes over time)
 */
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

/**
 * creates a [Lens] pointing to a certain position in a list
 *
 * @param index position to focus on
 */
fun <T> positionLens(index: Int): Lens<List<T>, T> = object : Lens<List<T>, T> {
    override val _id: String = index.toString()

    override fun get(parent: List<T>): T = parent[index]

    override fun set(parent: List<T>, value: T): List<T> =
        parent.subList(0, index) + value + parent.subList(index + 1, parent.size)
}
