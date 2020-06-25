package dev.fritz2.identification

import dev.fritz2.lenses.Lens

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