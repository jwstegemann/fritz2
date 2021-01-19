package dev.fritz2.components.validation

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.Store
import dev.fritz2.binding.SubStore
import dev.fritz2.validation.Validator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface Validatable<D, T> {
    val validator: Validator<D, ComponentValidationMessage, T>
}

fun <R, P, T> Store<T>.hasValidator(): Boolean = when(this) {
    is RootStore<*> -> this is Validatable<*, *>
    is SubStore<*, *, *> -> this is Validatable<*, *>
    else -> false
}

fun <D> Validatable<D, *>.validationMessage(id: String): Flow<String> =
    this.validator.find { it.id == id }.map { it.message }