package dev.fritz2.headless.hooks

import dev.fritz2.binding.Store
import dev.fritz2.dom.HtmlTag
import dev.fritz2.dom.html.handledBy
import dev.fritz2.headless.validation.ComponentValidationMessage
import dev.fritz2.headless.validation.Severity
import dev.fritz2.headless.validation.validationMessages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map


abstract class DatabindingHook<C : HtmlTag<*>, P, T> : BasicHook<C, Unit, P>(), Usable<DatabindingHook<C, P, T>> {
    lateinit var data: Flow<T>
    var id: String? = null
    var handler: ((Flow<T>) -> Unit)? = null

    protected var messages: Flow<List<ComponentValidationMessage>>? = null

    val hasError: Flow<Boolean> by lazy {
        messages?.map { messages -> messages.any { it.severity == Severity.Error } } ?: flowOf(false)
    }

    val validationMessages: Flow<List<ComponentValidationMessage>> by lazy {
        messages ?: flowOf(emptyList())
    }

    abstract fun C.render(payload: P)

    /**
     * Be aware that for implementations of [DatabindingHook] that add properties, this method must be implemented
     * again as well in order to copy the new properties. Calling this method by `super` from within is encouraged
     * of course.
     */
    override fun use(other: DatabindingHook<C, P, T>) {
        apply = other.apply
        data = other.data
        id = other.id
        handler = other.handler
        messages = other.messages
    }

    open operator fun invoke(
        id: String? = null,
        data: Flow<T>,
        messages: Flow<List<ComponentValidationMessage>>? = null,
        handler: ((Flow<T>) -> Unit)? = null
    ) {
        this.id = id
        this.data =
            data //FIXME: if (data is SharedFlow<T>) data else data.shareIn(MainScope() + job, SharingStarted.Lazily)
        this.handler = handler
        this.messages = messages
        apply = {
            render(it)
        }
    }

    open operator fun invoke(store: Store<T>) {
        this.invoke(store.id, store.data, store.validationMessages()) { it handledBy store.update }
    }
}

abstract class ItemDatabindingHook<C : HtmlTag<*>, P, T> : DatabindingHook<C, P, T>() {
    abstract fun isSelected(item: P): Flow<Boolean>
}
