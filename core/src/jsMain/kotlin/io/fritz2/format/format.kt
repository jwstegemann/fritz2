package io.fritz2.format

import io.fritz2.binding.RootStore
import io.fritz2.binding.Store
import io.fritz2.binding.Update
import io.fritz2.lenses.Lens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A [Store] representing the formatted value of it's parent [Store].
 * Use this to transparently bind a Date, an Int or some other data-type in your model to an HTML-input (that can only handle [String]s).
 * Do not create an instance by yourself. Use the factory-method at [SubStore]
 *
 * @param parent parent [Store]
 * @param rootStore [RootStore] in this chain of [Store]s
 * @param rootLens concatenated [Lens] pointing to the element in the [RootStore]s type representing the value of this [Store]
 * @param format the [Format] used to parse and format (serialize and deserialize) the value
 *
 */
class FormatStore<R, P>(
    private val parent: Store<P>,
    private val rootStore: RootStore<R>,
    private val rootLens: Lens<R, P>,
    private val format: Format<P>
) : Store<String>() {

    /**
     * id of this [Store]
     */
    override val id: String by lazy { parent.id }

    /**
     * applies the given [Format] before handling an [Update]
     */
    override suspend fun enqueue(update: Update<String>) {
        rootStore.enqueue {
            rootLens.apply(it, { t -> format.parse(update(format.format(t))) })
        }
    }

    /**
     * applies the given [Format] to the data-[Flow] of the parent [Store]
     */
    override val data: Flow<String> = parent.data.map { format.format(it) }
}