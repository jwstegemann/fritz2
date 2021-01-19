package dev.fritz2.components

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * A marker to separate the layers of calls in the type-safe-builder pattern.
 */
@DslMarker
annotation class ComponentMarker

interface ElementProperties<T> {
    var element: (T.() -> Unit)?

    fun element(value: T.() -> Unit) {
        element = value
    }
}

// TODO: Constraint für Typ: T : Tag<E> ?
class Element<T> : ElementProperties<T> {
    override var element: (T.() -> Unit)? = null
}

interface FormProperties {
    var disabled: Flow<Boolean>

    fun disabled(value: Flow<Boolean>) {
        disabled = value
    }

    fun disabled(value: Boolean) {
        disabled(flowOf(value))
    }

    fun enabled(value: Flow<Boolean>) {
        disabled = value.map { !it }
    }

    fun enabled(value: Boolean) {
        enabled(flowOf(value))
    }
}

open class Form : FormProperties {
    override var disabled: Flow<Boolean> = flowOf(false)
}

interface InputFormProperties : FormProperties {

    var readonly: Flow<Boolean>

    fun readonly(value: Flow<Boolean>) {
        readonly = value
    }
    fun readonly(value: Boolean) {
        readonly(flowOf(value))
    }
}

class InputForm : InputFormProperties, Form() {
    override var readonly: Flow<Boolean> = flowOf(false)
}
