package dev.fritz2.binding

import dev.fritz2.flow.asSharedFlow
import dev.fritz2.lenses.Lens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * A [Store] that is derived from your [RootStore] or another [SubStore] that represents a part of the data-model of it's parent.
 * Use the .sub-factory-method on the parent [Store] to create it.
 */
class SubStore<R, P, T>(
    private val parent: Store<P>,
    private val lens: Lens<P, T>,
    val root: RootStore<R>,
    internal val rootLens: Lens<R, T>
) : Store<T>, CoroutineScope by MainScope() {


    /**
     * defines how to infer the id of the sub-part from the parent's id.
     */
    override val id: String by lazy { "${parent.id}.${lens.id}".trimEnd('.') }

    /**
     * Since a [SubStore] is just a view on a [RootStore] holding the real value, it forwards the [Update] to it, using it's [Lens] to transform it.
     */
    override suspend fun enqueue(update: QueuedUpdate<T>) {
        root.enqueue(QueuedUpdate({
            try {
                rootLens.apply(it, update.update)
            } catch (e: Throwable) {
                rootLens.apply(it, { oldValue -> update.errorHandler(e, oldValue) })
            }
        }, root::errorHandler, update.transaction))
    }

    /**
     * a simple [SimpleHandler] that just takes the given action-value as the new value for the [Store].
     */
    override val update = handle<T> { _, newValue -> newValue }

    /**
     * the current value of the [SubStore] is derived from the data of it's parent using the given [Lens].
     */
    override val data: Flow<T> = parent.data.map {
        lens.get(it)
    }.distinctUntilChanged().asSharedFlow()

    /**
     * creates a new [SubStore] using this one as it's parent.
     *
     * @param lens a [Lens] describing which part to create the [SubStore] for
     */
    fun <X> sub(lens: Lens<T, X>): SubStore<R, T, X> =
        SubStore(this, lens, root, rootLens + lens)
}