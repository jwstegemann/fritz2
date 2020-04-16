package io.fritz2.format

import io.fritz2.binding.RootStore
import io.fritz2.binding.Store
import io.fritz2.binding.Update
import io.fritz2.optics.Lens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * [Format] [parse] and [format] the given
 * [String] value in the target type.
 */
interface Format<T> {

    fun parse(value: String): T
    fun format(value: T): String

}

class FormatStore<R, P>(
    private val parent: Store<P>,
    private val rootStore: RootStore<R>,
    private val rootLens: Lens<R, P>,
    private val format: Format<P>
) : Store<String>() {

    override val id: String by lazy { parent.id }

    override suspend fun enqueue(update: Update<String>) {
        rootStore.enqueue {
            rootLens.apply(it, { t -> format.parse(update(format.format(t))) })
        }
    }

    override val data: Flow<String> = parent.data.map { format.format(it) }
}