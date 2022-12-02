package dev.fritz2.core

/**
 *  gives you a new [Inspector] as starting point.
 */
fun <D> inspectorOf(data: D): Inspector<D> = RootInspector(data)

/**
 * represents the data and corresponding id of certain value
 * in a deep nested model structure.
 *
 * @property data [D] representation of stored data
 * @property path [String] representation of the corresponding path in model
 */
interface Inspector<D> {
    val data: D
    val path: String

    /**
     * creates a new [Inspector] for a part of your underlying data-model
     *
     * @param lens a [Lens] describing, of which part of your data
     * model you want to have the next [Inspector]
     */
    fun <X> map(lens: Lens<D, X>): Inspector<X> = SubInspector(this, lens)
}


/**
 * [RootInspector] is the starting point for getting your [data] and corresponding [path]s from your
 * deep nested model structure. Get this by calling the factory method [inspectorOf].
 *
 * [Inspector] is useful in validation process to know which model attribute is not valid.
 */
class RootInspector<T>(
    override val data: T
) : Inspector<T> {
    override val path: String = ""
}

/**
 * A [Inspector] that is derived from a parent [Inspector] mapping its data by a given [Lens].
 */
class SubInspector<P, T>(
    val parent: Inspector<P>,
    private val lens: Lens<P, T>
) : Inspector<T> {

    /**
     * generates the corresponding [path]
     */
    override val path: String by lazy { "${parent.path}.${lens.id}".trimEnd('.') }

    /**
     * returns the underlying [data]
     */
    override val data: T = lens.get(parent.data)
}

/**
 * Creates a new [Inspector] containing the element for the given [element] and [idProvider]
 * from the original [Inspector]'s [List].
 *
 * @param element current instance of the entity to focus on
 * @param idProvider to identify the same entity (i.e. when it's content changed)
 */
fun <D, I> Inspector<List<D>>.mapByElement(element: D, idProvider: IdProvider<D, I>): Inspector<D> =
    SubInspector(this, lensForElement(element, idProvider))

/**
 * Performs the given [action] on each [Inspector].
 *
 * @param idProvider to get the id from an instance
 * @param action function which gets applied to all [Inspector]s
 */
fun <D, I> Inspector<List<D>>.inspectEach(idProvider: IdProvider<D, I>, action: (Inspector<D>) -> Unit) {
    this.data.onEach { element -> action(mapByElement(element, idProvider)) }
}

/**
 * Creates a new [Inspector] containing the element for the given [index] from the original [Inspector]'s [List]
 *
 * @param index position in the list to point to
 */
fun <D> Inspector<List<D>>.mapByIndex(index: Int): Inspector<D> =
    SubInspector(this, lensForElement(index))

/**
 * Performs the given [action] on each [Inspector].
 *
 * @param action function which gets applied to all [Inspector]s
 */
fun <D> Inspector<List<D>>.inspectEach(action: (Inspector<D>) -> Unit) {
    this.data.onEachIndexed { index, _ -> action(mapByIndex(index)) }
}

/**
 * Creates a new [Inspector] containing the value for the given [key] from the original [Inspector]'s [Map]
 *
 * @param key to the corresponding value in the map
 */
fun <K, V> Inspector<Map<K, V>>.mapByKey(key: K): Inspector<V> =
    SubInspector(this, lensForElement(key))

/**
 * Performs the given [action] on each [Inspector].
 *
 * @param action function which gets applied to all [Inspector]s
 */
fun <K, V> Inspector<Map<K, V>>.inspectEach(action: (Inspector<V>) -> Unit) {
    this.data.onEach { (k , _) -> action(mapByKey(k)) }
}