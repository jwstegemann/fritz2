package dev.fritz2.format

import dev.fritz2.binding.*
import dev.fritz2.lenses.Lens
import dev.fritz2.lenses.buildLens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A [Store] representing the formatted value of it's parent [Store].
 * Use this to transparently bind a Date, an Int or some other data-type in your model to an HTML-input (that can only handle [String]s).
 * Do not create an instance by yourself. Use the [SubStore.using] instead.
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
) : Store<String>, CoroutineScope by MainScope() {

    //FIXME: replace by Substore using this lens, maybe change format's signature to fit the lens methods
    val formatLens = rootLens + buildLens("", format::format, { p, v -> format.parse(v) })

    /**
     * id of this [Store]
     */
    override val id: String by lazy { parent.id }

    /**
     * applies the given [Format] before handling an [Update]
     */
    override suspend fun enqueue(queuedUpdate: QueuedUpdate<String>) {
        rootStore.enqueue(QueuedUpdate<R>({
            try {
                formatLens.apply(it, queuedUpdate.update)
            } catch (e: Throwable) {
                formatLens.apply(it, { oldValue -> queuedUpdate.errorHandler(e, oldValue) })
            }
        }, rootStore::errorHandler, queuedUpdate.transaction))
    }

    /**
     * applies the given [Format] to the data-[Flow] of the parent [Store]
     */
    override val data: Flow<String> = parent.data.map { format.format(it) }

    /**
     * a simple [SimpleHandler] that just takes the given action-value as the new value for the [Store].
     */
    override val update = handle<String> { _, newValue -> newValue }
}