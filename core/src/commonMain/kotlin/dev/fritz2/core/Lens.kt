package dev.fritz2.core

/**
 * Used by the fritz2 gradle-plugin to identify data classes it should generate [Lens]es for.
 */
@Target(AnnotationTarget.CLASS)
annotation class Lenses

/**
 * Used by the fritz2 gradle-plugin to identify properties in sealed classes or interfaces, that should get ignored
 * by the lens generation.
 *
 * Typical use case are const properties, that are overridden inside the data class body and not the ctor.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class NoLens

/**
 * Describes a focus point into a data structure, i.e. a property of a given complex entity for read and write
 * access.
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
     * appends to [Lens]es so that the resulting [Lens] points from the parent of the [Lens] this is called on to
     * the target of [other]
     *
     * @param other [Lens] to append to this one
     */
    operator fun <X> plus(other: Lens<T, X>): Lens<P, X> = object : Lens<P, X> {
        override val id = "${this@Lens.id}.${other.id}".trimEnd('.')
        override fun get(parent: P): X = other.get(this@Lens.get(parent))
        override fun set(parent: P, value: X): P = this@Lens.set(parent, other.set(this@Lens.get(parent), value))
    }

    /**
     * For a lens on a non-nullable parent this method creates a lens that can be used on a nullable-parent
     * Use this method only if you made sure, that it is never called on a null parent.
     * Otherwise, a [NullPointerException] is thrown.
     */
    fun withNullParent(): Lens<P?, T> = object : Lens<P?, T> {
        override val id: String = this@Lens.id

        override fun get(parent: P?): T =
            if (parent != null) this@Lens.get(parent)
            else throw NullPointerException("get called with null parent on not-nullable lens@$id")

        override fun set(parent: P?, value: T): P? =
            if (parent != null) this@Lens.set(parent, value)
            else throw NullPointerException("set called with null parent on not-nullable lens@$id")
    }
}

/**
 * convenience function to create a [Lens]
 *
 * @param id of the [Lens]
 * @param getter of the [Lens]
 * @param setter of the [Lens]
 */
inline fun <P, T> lensOf(id: String, crossinline getter: (P) -> T, crossinline setter: (P, T) -> P): Lens<P, T> =
    object : Lens<P, T> {
        override val id: String = id
        override fun get(parent: P): T = getter(parent)
        override fun set(parent: P, value: T): P = setter(parent, value)
    }

/**
 * creates a [Lens] converting [P] to and from a [String]
 *
 * @param format function for formatting a [P] to [String]
 * @param parse function for parsing a [String] to [P]
 */
inline fun <P> lensOf(crossinline format: (P) -> String, crossinline parse: (String) -> P): Lens<P, String> =
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
 * Occurs when [Lens] points to non-existing element.
 */
class CollectionLensGetException(message: String) : Exception(message) // is needed to cancel the coroutine correctly

/**
 * Occurs when [Lens] tries to update a non-existing element.
 */
class CollectionLensSetException(message: String) : Exception(message)

/**
 * creates a [Lens] pointing to a certain element in a [List]
 *
 * @param element current instance of the element to focus on
 * @param idProvider to identify the element in the list (i.e. when it's content changes over time)
 */
fun <T, I> lensForElement(element: T, idProvider: IdProvider<T, I>): Lens<List<T>, T> = object : Lens<List<T>, T> {
    override val id: String = idProvider(element).toString()

    override fun get(parent: List<T>): T = parent.find {
        idProvider(it) == idProvider(element)
    } ?: throw CollectionLensGetException(
        "no item found with id='$id' in `lensForElement(element: T, idProvider: IdProvider<T, I>)`"
    )

    override fun set(parent: List<T>, value: T): List<T> = ArrayList<T>(parent.size).apply {
        var count = 0
        parent.forEach { item ->
            if (idProvider(item) == idProvider(element)) {
                count++
                add(value)
            } else add(item)
        }
        if (count == 0) throw CollectionLensSetException("no item found with id='${idProvider(element)}'")
        else if (count > 1) throw CollectionLensSetException("$count ambiguous items found with id='${idProvider(element)}'")
    }
}

/**
 * creates a [Lens] pointing to a certain [index] in a list
 *
 * @param index position to focus on
 */
fun <T> lensForElement(index: Int): Lens<List<T>, T> = object : Lens<List<T>, T> {
    override val id: String = index.toString()

    override fun get(parent: List<T>): T =
        parent.getOrNull(index) ?: throw CollectionLensGetException(
            "no item found with id='$id' in `lensForElement(index: Int)`"
        )

    override fun set(parent: List<T>, value: T): List<T> =
        if (index < 0 || index >= parent.size) throw CollectionLensSetException("no item found with index='$index'")
        else parent.mapIndexed { i, it -> if (i == index) value else it }

}

/**
 * creates a [Lens] pointing to a certain element in a [Map]
 *
 * @param key of the entry to focus on
 */
fun <K, V> lensForElement(key: K): Lens<Map<K, V>, V> = object : Lens<Map<K, V>, V> {
    override val id: String = key.toString()

    override fun get(parent: Map<K, V>): V =
        parent[key] ?: throw CollectionLensGetException("no item found with id='$id' in `lensForElement(key: K)`")

    override fun set(parent: Map<K, V>, value: V): Map<K, V> =
        if (parent.containsKey(key)) parent + (key to value)
        else throw CollectionLensSetException("no item found with key='$key'")
}

/**
 * create a [Lens] for upcasting a base (sealed) class or interface to a specific subtype.
 */
inline fun <P, reified C : P> lensForUpcasting(): Lens<P, C> = object : Lens<P, C> {
    override val id: String = ""
    override fun get(parent: P): C =
        (parent as? C) ?: throw CollectionLensGetException("no parent='${parent.toString()}' found for upcasting")

    override fun set(parent: P, value: C): P = value
}

/**
 * Creates a [Lens] from a nullable parent to a non-nullable value using the provided [default] value.
 *
 * Use this method to apply a default value that will be used in the case that the real value is null.
 * When setting that value to the default value it will accordingly translate to null.
 *
 * The inverse Lens can be created using the [mapToNullableLens] factory.
 *
 * @param default value to be used instead of `null`
 */
internal fun <T> mapToNonNullLens(default: T): Lens<T?, T> = object : Lens<T?, T> {
    override val id: String = ""
    override fun get(parent: T?): T = parent ?: default
    override fun set(parent: T?, value: T): T? = value.takeUnless { it == default }
}

/**
 * Creates a [Lens] from a _non-nullable_ parent to a _nullable_ value, mapping the provided [placeholder] to `null`
 * and vice versa.
 *
 * Use this method in cases where a nullable Store is needed but the data model used is actually non-nullable.
 *
 * The inverse Lens can be created using the [mapToNonNullLens] factory.
 *
 * @param placeholder value to be mapped to `null`
 */
internal fun <T> mapToNullableLens(placeholder: T): Lens<T, T?> = object : Lens<T, T?> {
    override val id: String = ""
    override fun get(parent: T): T? = parent.takeUnless { parent == placeholder }
    override fun set(parent: T, value: T?): T = value ?: placeholder
}