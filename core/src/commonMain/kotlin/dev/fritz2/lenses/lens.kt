package dev.fritz2.lenses

/**
 * Describes a focus point into a data structure, i.e. a property of a given complex entity
 *
 * @property id identifies the focus of this lens
 */
interface Lens<P, T> {
    val id: String

    /**
     * gets the value of the focus target
     *
     * @param parent concrete instance to apply the focus tos
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
        override val id = "${this@Lens.id}.${other.id}".trimEnd('.')
        override fun get(parent: P): X = other.get(this@Lens.get(parent))
        override fun set(parent: P, value: X): P = this@Lens.set(parent, other.set(this@Lens.get(parent), value))
    }
}

/**
 * convenience function to create a [Lens]
 *
 * @param id of the [Lens]
 * @param getter of the [Lens]
 * @param setter of the [Lens]
 */
inline fun <P, T> buildLens(id: String, crossinline getter: (P) -> T, crossinline setter: (P, T) -> P): Lens<P, T> =
    object : Lens<P, T> {
        override val id: String = id
        override fun get(parent: P): T = getter(parent)
        override fun set(parent: P, value: T): P = setter(parent, value)
    }

/**
 * creates a [Lens] converting [P] to and from a [String]
 *
 * @param parse function for parsing a [String] to [P]
 * @param format function for formatting a [P] to [String]
 */
inline fun <P> format(crossinline parse: (String) -> P, crossinline format: (P) -> String): Lens<P, String> =
    object : Lens<P, String> {
        override val id: String = ""
        override fun get(parent: P): String = format(parent)
        override fun set(parent: P, value: String): P = parse(value)
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
    override val id: String = idProvider(element).toString()

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
    override val id: String = index.toString()

    override fun get(parent: List<T>): T = parent[index]

    override fun set(parent: List<T>, value: T): List<T> =
        parent.subList(0, index) + value + parent.subList(index + 1, parent.size)
}
