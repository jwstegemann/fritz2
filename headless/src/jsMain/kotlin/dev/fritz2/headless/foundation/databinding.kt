package dev.fritz2.headless.foundation

import dev.fritz2.binding.Store
import dev.fritz2.dom.html.handledBy
import dev.fritz2.headless.validation.ComponentValidationMessage
import dev.fritz2.headless.validation.Severity
import dev.fritz2.validation.messages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

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

    val hasError: Flow<Boolean> by lazy {
        value?.messages?.map { messages -> messages.any { it.severity == Severity.Error } } ?: flowOf(false)
    }

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
