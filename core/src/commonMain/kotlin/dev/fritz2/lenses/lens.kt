package dev.fritz2.lenses

import dev.fritz2.format.Format

/**
 * Describes a focus point into a data structure, i.e. a property of a given complex entity
 *
 * @property _id identifies the focus of this lens
 */
interface Lens<P, T> {
    val _id: String

    /**
     * gets the value of the focus target
     *
     * @param parent concrete instance to apply the focus to
     */
    fun get(parent: P): T

    /**
     * sets the value of the focus target
     *
     * @param parent concrete instance to apply the focus to
     * @param value the new value of the focus target
     */
    fun set(parent: P, value: T): P

    /**
     * manipulates the focus target's value inside the [parent]
     *
     * @param parent concrete instance to apply the focus to
     * @param mapper function defining the manipulation
     */
    suspend fun apply(parent: P, mapper: suspend (T) -> T): P = set(parent, mapper(get(parent)))


    /**
     * appends to [Lens]es so that the resulting [Lens] points from the parent of the [Lens] this is called on to the target of [other]
     *
     * @param other [Lens] to append to this one
     */
    operator fun <X> plus(other: Lens<T, X>): Lens<P, X> = object :
        Lens<P, X> {
        override val _id = "${this@Lens._id}.${other._id}"
        override fun get(parent: P): X = other.get(this@Lens.get(parent))
        override fun set(parent: P, value: X): P = this@Lens.set(parent, other.set(this@Lens.get(parent), value))
    }

    /**
     * creates a new [Lens] using the two given functions [parse] and [format]
     * to convert a value of type [T] to a [String] and vice versa.
     *
     * @param parse function for parsing a [String] to [T]
     * @param format function for parsing a [T] to [String]
     * @param id for prepending in resulting [Lens].id
     */
    fun using(parse: (String) -> T, format: (T) -> String, id: String = ""): Lens<P, String> =
        this + buildLens(id, format, { _, value -> parse(value)})
}

/**
 * convenience function to create a [Lens]
 *
 * @param id of the [Lens]
 * @param getter of the [Lens]
 * @param setter of the [Lens]
 */
inline fun <P, T> buildLens(id: String, crossinline getter: (P) -> T, crossinline setter: (P, T) -> P) = object :
    Lens<P, T> {
    override val _id = id
    override fun get(parent: P): T = getter(parent)
    override fun set(parent: P, value: T): P = setter(parent, value)
}

/**
 * function to derive a valid id for a given instance that does not change over time.
 */
typealias IdProvider<T, I> = (T) -> I

/**
 * creates a [Lens] pointing to a certain element in a list
 *
 * @param element current instance of the element to focus on
 * @param idProvider to identify the element in the list (i.e. when it's content changes over time)
 */
fun <T, I> elementLens(element: T, idProvider: IdProvider<T, I>): Lens<List<T>, T> = object :
    Lens<List<T>, T> {
    override val _id: String = idProvider(element).toString()

    override fun get(parent: List<T>): T = parent.find {
        idProvider(it) == idProvider(element)
    } ?: throw IndexOutOfBoundsException()

    override fun set(parent: List<T>, value: T): List<T> = parent.map {
        if (idProvider(it) == idProvider(value)) value else it
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
