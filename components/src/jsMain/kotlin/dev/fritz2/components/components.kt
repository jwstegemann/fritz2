package dev.fritz2.components

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * A marker to separate the layers of calls in the type-safe-builder pattern.
 */
@DslMarker
annotation class ComponentMarker

class ComponentProperty<T>(var value: T) {
    operator fun invoke(newValue: T) {
        value = newValue
    }
}

class DynamicComponentProperty<T>(var values: Flow<T>) {
    operator fun invoke(newValue: T) {
        values = flowOf(newValue)
    }

    operator fun invoke(newValues: Flow<T>) {
        values = newValues
    }
}

interface ElementProperties<T> {
    var element: ComponentProperty<T.() -> Unit>
}

// TODO: Constraint für Typ: T : Tag<E> ?
class Element<T> : ElementProperties<T> {
    override var element: ComponentProperty<T.() -> Unit> = ComponentProperty {}
}

interface FormProperties {
    var disabled: DynamicComponentProperty<Boolean>

    fun enabled(value: Flow<Boolean>) {
        disabled(value.map { !it })
    }

    fun enabled(value: Boolean) {
        enabled(flowOf(value))
    }
}

open class Form : FormProperties {
    override var disabled = DynamicComponentProperty(flowOf(false))
}

interface InputFormProperties : FormProperties {
    var readonly: DynamicComponentProperty<Boolean>
}

class InputForm : InputFormProperties, Form() {
    override var readonly = DynamicComponentProperty(flowOf(false))
}

interface TextInputFormProperties : InputFormProperties {
    // TODO Some further properties are equal between input type=text and textarea; could be worth to centralize!
}