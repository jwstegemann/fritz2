package dev.fritz2.lenses

interface Lens<P, T> {
    val _id: String
    fun get(parent: P): T
    fun set(parent: P, value: T): P
    fun apply(parent: P, mapper: (T) -> T): P = set(parent, mapper(get(parent)))


    operator fun <X> plus(other: Lens<T, X>): Lens<P, X> = object :
        Lens<P, X> {
        override val _id = "${this@Lens._id}.${other._id}"
        override fun get(parent: P): X = other.get(this@Lens.get(parent))
        override fun set(parent: P, value: X): P = this@Lens.set(parent, other.set(this@Lens.get(parent), value))
    }
}

inline fun <P, T> buildLens(id: String, crossinline getter: (P) -> T, crossinline setter: (P, T) -> P) = object :
    Lens<P, T> {
    override val _id = id
    override fun get(parent: P): T = getter(parent)
    override fun set(parent: P, value: T): P = setter(parent, value)
}

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