package dev.fritz2.components.validation

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.Store
import dev.fritz2.binding.SubStore
import dev.fritz2.validation.Validator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

interface Validatable<D, T> : Store<D> {
    val validator: Validator<D, ComponentValidationMessage, T>
    fun validate(metadata: T) {
        syncBy(handle<D> { old, new ->
            if (validator.isValid(new, metadata)) new else old
        })
    }
}

class RootStoreIsNotValidatable(id: String) :
    Exception("RootStore of data with id=$id must implement Validatable interface")

fun <D> Store<D>.validationMessage(): Flow<String> = when (this) {
    is RootStore<*> -> {
        if (this is Validatable<*, *>) {
            this.validator.find { it.id == this@validationMessage.id }.map { it?.message ?: "" }
        } else {
            throw RootStoreIsNotValidatable(id)
        }
    }
    is SubStore<*, *, *> -> {
        val root = this.root
        if (root is Validatable<*, *>) {
            root.validator.find { it.id == this@validationMessage.id }.map { it?.message ?: "" }
        } else throw RootStoreIsNotValidatable(id)
    }
    else -> flowOf("")
}