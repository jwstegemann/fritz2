package dev.fritz2.identification

import dev.fritz2.lenses.Lens
import dev.fritz2.lenses.elementLens
import dev.fritz2.lenses.idProvider
import dev.fritz2.lenses.positionLens

/**
 * represents the id of certain element in a deep nested model structure.
 *
 * @property id [String] representation of the id
 */
interface ModelId<T> {
    val id: String

    /**
     * method to create a [ModelId] for a part of your data-model
     *
     * @param lens a [Lens] describing, of which part of your data model you want the id
     */
    fun <X> sub(lens: Lens<T, X>): ModelId<X>
}


/**
 * Starting point for getting your [ModelId]s.
 * Call [sub] to get the underlying ids of a an deep nested model structure
 * It's useful in validation process to know which html elements are not valid.
 */
class RootModelId<T>(override val id: String = "") : ModelId<T> {

    /**
     * method to create a [ModelId] for a part of your data-model
     *
     * @param lens a [Lens] describing, of which part of your data model you want the id
     */
    override fun <X> sub(lens: Lens<T, X>): ModelId<X> =
        SubModelId(this, lens, this, lens)
}


/**
 *  Gives the next [ModelId] in a deep nested model structure.
 *  It's useful in validation process to know which html elements are not valid.
 */
class SubModelId<R, P, T>(
        private val parent: ModelId<P>,
        private val lens: Lens<P, T>,
        val rootStore: RootModelId<R>,
        val rootLens: Lens<R, T>
) : ModelId<T> {
    /**
     * defines how the id of a part is derived from the one of it's parent
     */
    override val id: String by lazy { "${parent.id}.${lens._id}" }

    /**
     * method to create a [ModelId] for a part of your data-model
     *
     * @param lens a [Lens] describing, of which part of your data model you want the id
     */
    override fun <X> sub(lens: Lens<T, X>): SubModelId<R, T, X> =
        SubModelId(this, lens, rootStore, this.rootLens + lens)
}

/**
 * create a [ModelId] for an element in your [ModelId]'s list.
 *
 * @param element to get the [ModelId] for
 * @param idProvider to get the id from an instance
 */
fun <X> RootModelId<List<X>>.sub(element: X, idProvider: idProvider<X>): ModelId<X> {
    val lens = elementLens(element, idProvider)
    return SubModelId(this, lens, this, lens)
}

/**
 * create a [ModelId] for an element in your [ModelId]'s list.
 *
 * @param index you need the [ModelId] for
 */
fun <X> RootModelId<List<X>>.sub(index: Int): ModelId<X> {
    val lens = positionLens<X>(index)
    return SubModelId(this, lens, this, lens)
}

/**
 * create a [ModelId] for an element in your [ModelId]'s list.
 *
 * @param element to get the [ModelId] for
 * @param idProvider to get the id from an instance
 */
fun <R, P, X> SubModelId<R, P, List<X>>.sub(element: X, idProvider: idProvider<X>): ModelId<X> {
    val lens = elementLens(element, idProvider)
    return SubModelId<R, List<X>, X>(this, lens, this.rootStore, this.rootLens + lens)
}

/**
 * create a [ModelId] for an element in your [ModelId]'s list.
 *
 * @param index you need the [ModelId] for
 */
fun <R, P, X> SubModelId<R, P, List<X>>.sub(index: Int): ModelId<X> {
    val lens = positionLens<X>(index)
    return SubModelId<R, List<X>, X>(this, lens, this.rootStore, this.rootLens + lens)
}
