package dev.fritz2.components.validation

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.Store
import dev.fritz2.binding.SubStore
import kotlinx.coroutines.flow.Flow

/**
 * Interface which [RootStore]s can implement to mark them that they have
 * a [ComponentValidator]. Then all [SubStore]s can evaluate if a [ComponentValidationMessage]
 * is available for their field.
 *
 * The [validator] property must set with a [ComponentValidator] when implementing this interface.
 * To automatically call the validator when the model in [RootStore] is changed, use the [validate]
 * convenience function in the init block:
 *
 * ```kotlin
 * val store = object : RootStore<MyData>(MyData()), WithValidator<MyData, String> {
 *    override val validator = MyDataValidator
 *
 *    init { validate("my metadata") }
 *}
 * ```
 */
interface WithValidator<D, T> : Store<D> {
    /**
     * [ComponentValidator] for model data of type [D]
     */
    val validator: ComponentValidator<D, T>

    /**
     * Convenience method for calling the [ComponentValidator]
     * every time the model is changed.
     */
    fun validate(metadata: T) {
        syncBy(handle<D> { old, new ->
            if (validator.isValid(new, metadata)) new else old
        })
    }
}

/**
 * Finds the proper [ComponentValidationMessage] for the given [Store].
 *
 * @receiver [Store] for which to find a [ComponentValidationMessage]
 * @return nullable [Flow] which is null when no validator can be found
 */
fun <D> Store<D>.validationMessage(): Flow<ComponentValidationMessage?>? = when (this) {
    is RootStore<*> -> {
        if (this is WithValidator<*, *>) {
            this.validator.find { it.path == this@validationMessage.path }
        } else {
            null
        }
    }
    is SubStore<*, *> -> {
        val root = this.findRootStore()
        if (root is WithValidator<*, *>) {
            root.validator.find { it.path == this@validationMessage.path }
        } else null
    }
    else -> null
}

/**
 * Filters all proper [ComponentValidationMessage]s for the given [Store].
 *
 * @receiver [Store] for which to filter all [ComponentValidationMessage]s
 * @return nullable [Flow] which is null when no validator can be found
 */
fun <D> Store<D>.validationMessages(): Flow<List<ComponentValidationMessage>>? = when (this) {
    is RootStore<*> -> {
        if (this is WithValidator<*, *>) {
            this.validator.filter { it.path == this@validationMessages.path }
        } else {
            null
        }
    }
    is SubStore<*, *> -> {
        val root = this.findRootStore()
        if (root is WithValidator<*, *>) {
            root.validator.filter { it.path == this@validationMessages.path }
        } else null
    }
    else -> null
}

/**
 * recursively get root store of [SubStore]s
 * @return [Store] which is the root of the [SubStore]
 */
private fun <P, T> SubStore<P, T>.findRootStore(): Store<*> {
    var store: Store<*> = this
    while (store is SubStore<*, *>) {
        store = store.parent
    }
    return store
}
