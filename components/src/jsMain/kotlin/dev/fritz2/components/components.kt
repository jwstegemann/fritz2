package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.components.validation.Severity
import dev.fritz2.dom.WithEvents
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.SeverityStyles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.w3c.dom.Element

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
    val events: ComponentProperty<WithEvents<T>.() -> Unit>
}

class EventMixin<T : Element> : EventProperties<T> {
    override val events: ComponentProperty<WithEvents<T>.() -> Unit> = ComponentProperty {}
}

interface ElementProperties<T> {
    val element: ComponentProperty<T.() -> Unit>
}

// TODO: Constraint für Typ: T : Tag<E> ?
class ElementMixin<T> : ElementProperties<T> {
    override val element: ComponentProperty<T.() -> Unit> = ComponentProperty {}
}

interface FormProperties {
    val disabled: DynamicComponentProperty<Boolean>

    fun enabled(value: Flow<Boolean>) {
        disabled(value.map { !it })
    }

    fun enabled(value: Boolean) {
        enabled(flowOf(value))
    }
}

open class FormMixin : FormProperties {
    override val disabled = DynamicComponentProperty(flowOf(false))
}

interface InputFormProperties : FormProperties {
    val readonly: DynamicComponentProperty<Boolean>
}

class InputFormMixinMixin : InputFormProperties, FormMixin() {
    override val readonly = DynamicComponentProperty(flowOf(false))
}

interface SeverityProperties {
    val severity: NullableDynamicComponentProperty<Severity?>

    class SeverityContext {
        val info: Severity = Severity.Info
        val warning: Severity = Severity.Warning
        val error: Severity = Severity.Error
    }

    fun severity(value: SeverityContext.() -> Severity) {
        severity(value(SeverityContext()))
    }

    fun severityClassOf(
        severityStyle: SeverityStyles,
        prefix: String
    ): Flow<StyleClass> =
        severity.values.map {
            when (it) {
                Severity.Info -> staticStyle("${prefix}-severity-info", severityStyle.info)
                Severity.Success -> staticStyle("${prefix}-severity-info", severityStyle.success)
                Severity.Warning -> staticStyle("${prefix}-severity-warning", severityStyle.warning)
                Severity.Error -> staticStyle("${prefix}-severity-error", severityStyle.error)
                else -> StyleClass.None
            }
        }
}

class SeverityMixin : SeverityProperties {
    override val severity = NullableDynamicComponentProperty<Severity?>(flowOf(null))
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
    val closeButtonStyle: ComponentProperty<Style<BasicParams>>

    fun closeButton(
        styling: BasicParams.() -> Unit = {},
        baseClass: StyleClass? = null,
        id: String? = null,
        prefix: String = this.prefix,
        build: PushButtonComponent.() -> Unit = {}
    )
}

class CloseButtonMixin(
    override val closeButtonStyle: ComponentProperty<Style<BasicParams>>,
    override val prefix: String = "close-button"
) : CloseButtonProperty {

    override val hasCloseButton = ComponentProperty(true)

    override val closeButton = ComponentProperty<(RenderContext.(SimpleHandler<Unit>) -> Unit)?>(null)

    override fun closeButton(
        styling: BasicParams.() -> Unit,
        baseClass: StyleClass?,
        id: String?,
        prefix: String,
        build: PushButtonComponent.() -> Unit
    ) {
        closeButton { closeHandle ->
            clickButton({
                closeButtonStyle.value()
                styling()
            }, baseClass, id, prefix) {
                variant { ghost }
                icon { fromTheme { close } }
                build()
            }.map { } handledBy closeHandle
        }
    }
}