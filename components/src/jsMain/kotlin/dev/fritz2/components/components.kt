package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.dom.WithEvents
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement

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

class NullableDynamicComponentProperty<T>(var values: Flow<T?>) {
    operator fun invoke(newValue: T?) {
        values = flowOf(newValue)
    }

    operator fun invoke(newValues: Flow<T?>) {
        values = newValues
    }
}

interface EventProperties<T : Element> {
    var events: ComponentProperty<WithEvents<T>.() -> Unit>
}

class Event<T : Element> : EventProperties<T> {
    override var events: ComponentProperty<WithEvents<T>.() -> Unit> = ComponentProperty {}
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

class MultiSelectionStore<T> : RootStore<List<T>>(emptyList()) {
    val toggle = handleAndEmit<T, List<T>> { selectedRows, new ->
        val newSelection = if (selectedRows.contains(new))
            selectedRows - new
        else
            selectedRows + new
        emit(newSelection)
        newSelection
    }
}

class SingleSelectionStore : RootStore<Int?>(null) {
    val toggle = handleAndEmit<Int, Int> { _, new ->
        emit(new)
        new
    }
}

interface CloseButtonProperty {
    val prefix: String
    val hasCloseButton: ComponentProperty<Boolean>
    val closeButton: ComponentProperty<(RenderContext.(SimpleHandler<Unit>) -> Unit)?>
    val closeButtonStyle: Style<BasicParams>

    fun closeButton(
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = this.prefix,
        build: PushButtonComponent.() -> Unit = {}
    )
}

class CloseButton(
    override val closeButtonStyle: Style<BasicParams>,
    override val prefix: String = "close-button"
) : CloseButtonProperty {

    override val hasCloseButton = ComponentProperty(true)

    override var closeButton = ComponentProperty<(RenderContext.(SimpleHandler<Unit>) -> Unit)?>(null)

    override fun closeButton(
        styling: BasicParams.() -> Unit,
        baseClass: StyleClass?,
        id: String?,
        prefix: String,
        build: PushButtonComponent.() -> Unit
    ) {
        closeButton { closeHandle ->
            clickButton({
                closeButtonStyle()
                styling()
            }, baseClass, id, prefix) {
                variant { ghost }
                icon { fromTheme { close } }
                build()
            }.map { } handledBy closeHandle
        }
    }
}