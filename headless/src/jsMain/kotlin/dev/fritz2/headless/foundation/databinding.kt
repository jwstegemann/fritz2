package dev.fritz2.headless.foundation

import dev.fritz2.core.Store
import dev.fritz2.core.handledBy
import dev.fritz2.core.messages
import dev.fritz2.core.valid
import dev.fritz2.headless.validation.ComponentValidationMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * This property keeps track of the external data-binding of some component.
 *
 * There are exactly four parts that defines a fully functional data-binding:
 * - an [id]
 * - the [data] [Flow] to react to change from the external data source
 * - a [handler] to push changes to the external data holder
 * - a [messages] [Flow] to react to the external validation state
 *
 * This property enables components to expose a uniform API to plug the external data source into a component.
 *
 * All four parts can be set individually by the basic [invoke] operation, but there is also a convenience [invoke]
 * function, which accepts a [Store] of [T] and automatically grab the four aspects out of it.
 *
 * As only the [data] field is mandatory, this [Property] enables one-way-data-binding as well as two-way-data-binding.
 *
 * In order to deal nicely with [ComponentValidationMessage]s, there are two convenience functions to use:
 * - [hasError] offers a [Boolean] [Flow] that simply signals if there are errors at all or not. This is especially
 * useful for components, that display such messages. Often the DOM structure can be simplified if there is no message.
 * - [validationMessages] offers a none `null` [Flow] which can be used to render the messages without nullability
 * checks.
 */
class DatabindingProperty<T> : Property<DatabindingProperty.DataBinding<T>>() {

    data class DataBinding<T>(
        val id: String? = null,
        val data: Flow<T>,
        val handler: ((Flow<T>) -> Unit)? = null,
        val messages: Flow<List<ComponentValidationMessage>>? = null
    )

    val id: String?
        get() = value?.id

    val data: Flow<T> by lazy { value!!.data }

    val handler: ((Flow<T>) -> Unit)? by lazy { value?.handler }

    val hasError: Flow<Boolean> by lazy { value?.messages?.valid ?: flowOf(false) }

    val validationMessages: Flow<List<ComponentValidationMessage>> by lazy {
        value?.messages ?: flowOf(emptyList())
    }

    operator fun invoke(
        id: String? = null,
        data: Flow<T>,
        messages: Flow<List<ComponentValidationMessage>>? = null,
        handler: ((Flow<T>) -> Unit)? = null
    ) {
        //FIXME: if (data is SharedFlow<T>) data else data.shareIn(MainScope() + job, SharingStarted.Lazily)
        value = DataBinding(id, data, handler, messages)
    }

    operator fun invoke(store: Store<T>) {
        this.invoke(store.id, store.data, store.messages()) { it handledBy store.update }
    }
}
