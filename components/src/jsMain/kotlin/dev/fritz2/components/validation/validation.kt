package dev.fritz2.components.validation

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.Store
import dev.fritz2.binding.SubStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface WithValidator<D, T> : Store<D> {
    val validator: ComponentValidator<D, T>
    fun validate(metadata: T) {
        syncBy(handle<D> { old, new ->
            if (validator.isValid(new, metadata)) new else old
        })
    }
}

class RootStoreIsNotValidatable(id: String) :
    Exception("RootStore of data with id=$id must implement Validatable interface")

fun <D> Store<D>.validationMessage(): Flow<ComponentValidationMessage?> = when (this) {
    is RootStore<*> -> {
        if (this is WithValidator<*, *>) {
            this.validator.find { it.id == this@validationMessage.id }
        } else {
            throw RootStoreIsNotValidatable(id)
        }
    }
    is SubStore<*, *, *> -> {
        val root = this.root
        if (root is WithValidator<*, *>) {
            root.validator.find { it.id == this@validationMessage.id }
        } else throw RootStoreIsNotValidatable(id)
    }
    else -> emptyFlow()
}

fun <D> Store<D>.validationMessages(): Flow<List<ComponentValidationMessage>> = when (this) {
    is RootStore<*> -> {
        if (this is WithValidator<*, *>) {
            this.validator.filter { it.id == this@validationMessages.id }
        } else {
            throw RootStoreIsNotValidatable(id)
        }
    }
    is SubStore<*, *, *> -> {
        val root = this.root
        if (root is WithValidator<*, *>) {
            root.validator.filter { it.id == this@validationMessages.id }
        } else throw RootStoreIsNotValidatable(id)
    }
    else -> emptyFlow()
}