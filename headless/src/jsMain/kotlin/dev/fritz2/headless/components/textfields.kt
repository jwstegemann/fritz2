package dev.fritz2.headless.components

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.*
import dev.fritz2.dom.values
import dev.fritz2.headless.foundation.Aria
import dev.fritz2.headless.foundation.TagFactory
import dev.fritz2.headless.foundation.whenever
import dev.fritz2.headless.hooks.AttributeHook
import dev.fritz2.headless.hooks.BooleanAttributeHook
import dev.fritz2.headless.hooks.DatabindingHook
import dev.fritz2.headless.hooks.hook
import dev.fritz2.headless.validation.ComponentValidationMessage
import dev.fritz2.identification.Id
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLElement


abstract class HeadlessTextfield<C : Tag<HTMLElement>, CT : Tag<HTMLElement>>(val renderContext: C, id: String?) :
    RenderContext by renderContext {

    abstract class TextDatabindingHook<CT : Tag<HTMLElement>> : DatabindingHook<CT, Unit, String>()

    abstract val value: TextDatabindingHook<CT>
    abstract val placeholder: AttributeHook<CT, String>
    abstract val disabled: BooleanAttributeHook<CT>

    val componentId: String by lazy { id ?: value.id ?: Id.next() }
    protected val fieldId by lazy { "$componentId-field" }

    protected var label: Tag<HTMLElement>? = null
    protected var description: Tag<HTMLElement>? = null
    protected var validationMessages: Tag<HTMLElement>? = null
    protected var field: Tag<HTMLElement>? = null

    fun C.render() {
        attr("id", componentId)
        field?.apply {
            label?.let { attr(Aria.labelledby, it.id) }
            attr(
                Aria.describedby,
                value.validationMessages.map { messages -> if (messages.isNotEmpty()) validationMessages?.id else description?.id }
            )
        }
    }

    protected fun <CL : Tag<HTMLElement>> RenderContext.textfieldLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CL>,
        content: CL.() -> Unit
    ) = tag(this, classes, "$componentId-label", scope, content).also { label = it }

    protected fun RenderContext.textfieldLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Label.() -> Unit
    ) = textfieldLabel(classes, scope, RenderContext::label, content).apply {
        `for`(fieldId)
    }

    protected fun <CD : Tag<HTMLElement>> RenderContext.textfieldDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CD>,
        content: CD.() -> Unit
    ) = tag(this, classes, "$componentId-description", scope, content).also { description = it }

    protected fun RenderContext.textfieldDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: P.() -> Unit
    ) = textfieldDescription(classes, scope, RenderContext::p, content)

    protected fun <CV : Tag<HTMLElement>> RenderContext.textfieldValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CV>,
        content: CV.(List<ComponentValidationMessage>) -> Unit
    ) = value.validationMessages.render { messages ->
        if (messages.isNotEmpty()) {
            tag(this, classes, "$componentId-validation-messages", scope, { })
                .apply {
                    content(messages)
                }.also { validationMessages = it }
        }
    }

    protected fun RenderContext.textfieldValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Div.(List<ComponentValidationMessage>) -> Unit
    ) = textfieldValidationMessages(classes, scope, RenderContext::div, content)
}

class HeadlessInput<C : Tag<HTMLElement>>(renderContext: C, id: String?) :
    HeadlessTextfield<C, Input>(renderContext, id) {

    class InputDatabindingHook : TextDatabindingHook<Input>() {
        override fun Input.render(payload: Unit) {
            handler?.invoke(changes.values())
            value(data)
        }
    }

    override val value = InputDatabindingHook()
    override val placeholder = AttributeHook(Input::placeholder, Input::placeholder)
    override val disabled = BooleanAttributeHook(Input::disabled, Input::disabled)
    val type = AttributeHook(Input::type, Input::type).apply { this("text") }

    fun RenderContext.inputTextfield(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Input.() -> Unit
    ) = input(classes, id = fieldId, scope = scope, content).apply {
        attr(Aria.invalid, "true".whenever(value.hasError))
        hook(value, placeholder, type, disabled, payload = Unit)
    }.also { field = it }

    fun <CL : Tag<HTMLElement>> RenderContext.inputLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CL>,
        content: CL.() -> Unit
    ) = textfieldLabel(classes, scope, tag, content)

    fun RenderContext.inputLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Label.() -> Unit
    ) = textfieldLabel(classes, scope, content)

    fun <CD : Tag<HTMLElement>> RenderContext.inputDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CD>,
        content: CD.() -> Unit
    ) = textfieldDescription(classes, scope, tag, content)

    fun RenderContext.inputDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: P.() -> Unit
    ) = textfieldDescription(classes, scope, content)

    fun <CV : Tag<HTMLElement>> RenderContext.inputValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CV>,
        content: CV.(List<ComponentValidationMessage>) -> Unit
    ) = textfieldValidationMessages(classes, scope, tag, content)

    fun RenderContext.inputValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Div.(List<ComponentValidationMessage>) -> Unit
    ) = textfieldValidationMessages(classes, scope, content)
}

fun <C : Tag<HTMLElement>> RenderContext.headlessInput(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<C>,
    initialize: HeadlessInput<C>.() -> Unit
): C = tag(this, classes, id, scope) {
    HeadlessInput(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.headlessInput(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessInput<Div>.() -> Unit
): Div = headlessInput(classes, id, scope, RenderContext::div, initialize)


class HeadlessTextarea<C : Tag<HTMLElement>>(renderContext: C, id: String?) :
    HeadlessTextfield<C, TextArea>(renderContext, id) {

    class TextAreaDatabindingHook : HeadlessTextfield.TextDatabindingHook<TextArea>() {
        override fun TextArea.render(payload: Unit) {
            handler?.invoke(changes.values())
            value(data)
        }
    }

    override val value = TextAreaDatabindingHook()
    override val placeholder = AttributeHook(TextArea::placeholder, TextArea::placeholder)
    override val disabled = BooleanAttributeHook(TextArea::disabled, TextArea::disabled)

    fun RenderContext.textareaTextfield(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: TextArea.() -> Unit
    ) = textarea(classes, id = fieldId, scope = scope, content).apply {
        attr(Aria.invalid, "true".whenever(value.hasError))
        hook(value, placeholder, disabled, payload = Unit)
    }.also { field = it }

    fun <CL : Tag<HTMLElement>> RenderContext.textareaLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CL>,
        content: CL.() -> Unit
    ) = textfieldLabel(classes, scope, tag, content)

    fun RenderContext.textareaLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Label.() -> Unit
    ) = textfieldLabel(classes, scope, content)

    fun <CD : Tag<HTMLElement>> RenderContext.textareaDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CD>,
        content: CD.() -> Unit
    ) = textfieldDescription(classes, scope, tag, content)

    fun RenderContext.textareaDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: P.() -> Unit
    ) = textfieldDescription(classes, scope, content)

    fun <CV : Tag<HTMLElement>> RenderContext.textareaValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CV>,
        content: CV.(List<ComponentValidationMessage>) -> Unit
    ) = textfieldValidationMessages(classes, scope, tag, content)

    fun RenderContext.textareaValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Div.(List<ComponentValidationMessage>) -> Unit
    ) = textfieldValidationMessages(classes, scope, content)
}

fun <C : Tag<HTMLElement>> RenderContext.headlessTextarea(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<C>,
    initialize: HeadlessTextarea<C>.() -> Unit
): C = tag(this, classes, id, scope) {
    HeadlessTextarea(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.headlessTextarea(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessTextarea<Div>.() -> Unit
): Div = headlessTextarea(classes, id, scope, RenderContext::div, initialize)
