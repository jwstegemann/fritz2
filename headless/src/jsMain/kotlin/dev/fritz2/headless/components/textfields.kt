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
import org.w3c.dom.*


abstract class HeadlessTextfield<C : HTMLElement, CT : Tag<HTMLElement>>(tag: Tag<C>, id: String?) :
    Tag<C> by tag {

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

    fun render() {
        attr("id", componentId)
        field?.apply {
            label?.let { attr(Aria.labelledby, it.id) }
            attr(
                Aria.describedby,
                value.validationMessages.map { messages -> if (messages.isNotEmpty()) validationMessages?.id else description?.id }
            )
        }
    }

    protected fun <CL : HTMLElement> RenderContext.textfieldLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ) = tag(this, classes, "$componentId-label", scope, content).also { label = it }

    protected fun RenderContext.textfieldLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLLabelElement>.() -> Unit
    ) = textfieldLabel(classes, scope, RenderContext::label, content).apply {
        //FIXME: reset
        //`for`(fieldId)
    }

    protected fun <CD : HTMLElement> RenderContext.textfieldDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CD>>,
        content: Tag<CD>.() -> Unit
    ) = tag(this, classes, "$componentId-description", scope, content).also { description = it }

    protected fun RenderContext.textfieldDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLParagraphElement>.() -> Unit
    ) = textfieldDescription(classes, scope, RenderContext::p, content)

    protected fun <CV : HTMLElement> RenderContext.textfieldValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CV>>,
        content: Tag<CV>.(List<ComponentValidationMessage>) -> Unit
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
        content: Tag<HTMLDivElement>.(List<ComponentValidationMessage>) -> Unit
    ) = textfieldValidationMessages(classes, scope, RenderContext::div, content)
}

class HeadlessInput<C : HTMLElement>(tag: Tag<C>, id: String?) :
    HeadlessTextfield<C, HtmlTag<HTMLInputElement>>(tag, id) {

    class InputDatabindingHook : TextDatabindingHook<HtmlTag<HTMLInputElement>>() {
        override fun HtmlTag<HTMLInputElement>.render(payload: Unit) {
            handler?.invoke(changes.values())
            value(data)
        }
    }

    override val value = InputDatabindingHook()
    override val placeholder = AttributeHook(HtmlTag<HTMLInputElement>::placeholder, HtmlTag<HTMLInputElement>::placeholder)
    override val disabled = BooleanAttributeHook<HtmlTag<HTMLInputElement>>(HtmlTag<HTMLInputElement>::disabled, HtmlTag<HTMLInputElement>::disabled)
    val type = AttributeHook(HtmlTag<HTMLInputElement>::type, HtmlTag<HTMLInputElement>::type).apply { this("text") }

    fun RenderContext.inputTextfield(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLInputElement>.() -> Unit
    ) = input(classes, id = fieldId, scope = scope, content).apply {
        attr(Aria.invalid, "true".whenever(value.hasError))
        hook(value, placeholder, type, disabled, payload = Unit)
    }.also { field = it }

    fun <CL : HTMLElement> RenderContext.inputLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ) = textfieldLabel(classes, scope, tag, content)

    fun RenderContext.inputLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLLabelElement>.() -> Unit
    ) = textfieldLabel(classes, scope, content)

    fun <CD : HTMLElement> RenderContext.inputDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CD>>,
        content: Tag<CD>.() -> Unit
    ) = textfieldDescription(classes, scope, tag, content)

    fun RenderContext.inputDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLParagraphElement>.() -> Unit
    ) = textfieldDescription(classes, scope, content)

    fun <CV : HTMLElement> RenderContext.inputValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CV>>,
        content: Tag<CV>.(List<ComponentValidationMessage>) -> Unit
    ) = textfieldValidationMessages(classes, scope, tag, content)

    fun RenderContext.inputValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLDivElement>.(List<ComponentValidationMessage>) -> Unit
    ) = textfieldValidationMessages(classes, scope, content)
}

fun <C : HTMLElement> RenderContext.headlessInput(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: HeadlessInput<C>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    HeadlessInput(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.headlessInput(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessInput<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = headlessInput(classes, id, scope, RenderContext::div, initialize)


class HeadlessTextarea<C : HTMLElement>(tag: Tag<C>, id: String?) :
    HeadlessTextfield<C, HtmlTag<HTMLTextAreaElement>>(tag, id) {

    class TextAreaDatabindingHook : HeadlessTextfield.TextDatabindingHook<HtmlTag<HTMLTextAreaElement>>() {
        override fun HtmlTag<HTMLTextAreaElement>.render(payload: Unit) {
            handler?.invoke(changes.values())
            value(data)
        }
    }

    override val value = TextAreaDatabindingHook()
    override val placeholder = AttributeHook(HtmlTag<HTMLTextAreaElement>::placeholder, HtmlTag<HTMLTextAreaElement>::placeholder)
    override val disabled = BooleanAttributeHook(HtmlTag<HTMLTextAreaElement>::disabled, HtmlTag<HTMLTextAreaElement>::disabled)

    fun RenderContext.textareaTextfield(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTextAreaElement>.() -> Unit
    ) = textarea(classes, id = fieldId, scope = scope, content).apply {
        attr(Aria.invalid, "true".whenever(value.hasError))
        hook(value, placeholder, disabled, payload = Unit)
    }.also { field = it }

    fun <CL : HTMLElement> RenderContext.textareaLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ) = textfieldLabel(classes, scope, tag, content)

    fun RenderContext.textareaLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLLabelElement>.() -> Unit
    ) = textfieldLabel(classes, scope, content)

    fun <CD : HTMLElement> RenderContext.textareaDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CD>>,
        content: Tag<CD>.() -> Unit
    ) = textfieldDescription(classes, scope, tag, content)

    fun RenderContext.textareaDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLParagraphElement>.() -> Unit
    ) = textfieldDescription(classes, scope, content)

    fun <CV : HTMLElement> RenderContext.textareaValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CV>>,
        content: Tag<CV>.(List<ComponentValidationMessage>) -> Unit
    ) = textfieldValidationMessages(classes, scope, tag, content)

    fun RenderContext.textareaValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLDivElement>.(List<ComponentValidationMessage>) -> Unit
    ) = textfieldValidationMessages(classes, scope, content)
}

fun <C : HTMLElement> RenderContext.headlessTextarea(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: HeadlessTextarea<C>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    HeadlessTextarea(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.headlessTextarea(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessTextarea<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = headlessTextarea(classes, id, scope, RenderContext::div, initialize)
