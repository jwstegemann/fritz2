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
 * Starting point for creating your [ModelId]s. Same as [RootStore] but just for ids. Use it in validation for example.
 */
class ModelIdRoot<T>(override val id: String = "") : ModelId<T> {

    /**
     * method to create a [ModelId] for a part of your data-model
     *
     * @param lens a [Lens] describing, of which part of your data model you want the id
     */
    override fun <X> sub(lens: Lens<T, X>): ModelId<X> =
        ModelIdSub(this, lens, this, lens)
}


/**
 * Same as [SubStore] but just for ids. Use it in validation for example.
 */
class ModelIdSub<R, P, T>(
    private val parent: ModelId<P>,
    private val lens: Lens<P, T>,
    val rootStore: ModelIdRoot<R>,
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
    override fun <X> sub(lens: Lens<T, X>): ModelIdSub<R, T, X> =
        ModelIdSub(this, lens, rootStore, this.rootLens + lens)
}