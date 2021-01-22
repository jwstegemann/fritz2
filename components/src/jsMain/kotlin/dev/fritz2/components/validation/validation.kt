package dev.fritz2.components.validation

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.Store
import dev.fritz2.binding.SubStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

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
 * Exception which gets thrown when [RootStore] does not implement the [WithValidator]
 * interface.
 *
 * @param id id of the current field derived from model
 */
class RootStoreHasNoValidator(id: String) :
    Exception("RootStore of data with id=$id must implement WithValidator interface")

/**
 * Finds the proper [ComponentValidationMessage] for the given [Store].
 *
 * @receiver [Store] for which to find a [ComponentValidationMessage]
 * @throws RootStoreHasNoValidator when no [ComponentValidator] can be found in corresponding [RootStore]
 */
fun <D> Store<D>.validationMessage(): Flow<ComponentValidationMessage?> = when (this) {
    is RootStore<*> -> {
        if (this is WithValidator<*, *>) {
            this.validator.find { it.id == this@validationMessage.id }
        } else {
            throw RootStoreHasNoValidator(id)
        }
    }
    is SubStore<*, *, *> -> {
        val root = this.root
        if (root is WithValidator<*, *>) {
            root.validator.find { it.id == this@validationMessage.id }
        } else throw RootStoreHasNoValidator(id)
    }
    else -> emptyFlow()
}

/**
 * Filters all proper [ComponentValidationMessage]s for the given [Store].
 *
 * @receiver [Store] for which to filter all [ComponentValidationMessage]s
 * @throws RootStoreHasNoValidator when no [ComponentValidator] can be found in corresponding [RootStore]
 */
fun <D> Store<D>.validationMessages(): Flow<List<ComponentValidationMessage>> = when (this) {
    is RootStore<*> -> {
        if (this is WithValidator<*, *>) {
            this.validator.filter { it.id == this@validationMessages.id }
        } else {
            throw RootStoreHasNoValidator(id)
        }
    }
    is SubStore<*, *, *> -> {
        val root = this.root
        if (root is WithValidator<*, *>) {
            root.validator.filter { it.id == this@validationMessages.id }
        } else throw RootStoreHasNoValidator(id)
    }
    else -> emptyFlow()
}