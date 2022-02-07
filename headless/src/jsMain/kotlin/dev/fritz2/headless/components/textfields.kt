package dev.fritz2.headless.components

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.*
import dev.fritz2.dom.values
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.validation.ComponentValidationMessage
import dev.fritz2.identification.Id
import kotlinx.coroutines.flow.map
import org.w3c.dom.*


abstract class Textfield<C : HTMLElement, CT : Tag<HTMLElement>>(tag: Tag<C>, id: String?) : Tag<C> by tag {

    val value = DatabindingProperty<String>()
    abstract val placeholder: AttributeHook<CT, String>
    abstract val disabled: BooleanAttributeHook<CT>

    val componentId: String by lazy { id ?: value.id ?: Id.next() }
    protected val fieldId by lazy { "$componentId-field" }

    protected var label: Tag<HTMLElement>? = null
    protected var descriptions: MutableList<Tag<HTMLElement>> = mutableListOf()
    protected var validationMessages: Tag<HTMLElement>? = null
    protected var field: Tag<HTMLElement>? = null

    fun render() {
        attr("id", componentId)
        field?.apply {
            label?.let { attr(Aria.labelledby, it.id) }
            attr(
                Aria.describedby,
                value.validationMessages.map { messages ->
                    if (messages.isNotEmpty()) validationMessages?.id
                    else descriptions.map { it.id }.joinToString(" ")
                }
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
        `for`(fieldId)
    }

    protected fun <CD : HTMLElement> RenderContext.textfieldDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CD>>,
        content: Tag<CD>.() -> Unit
    ) = tag(
        this,
        classes,
        "$componentId-description-${descriptions.size}",
        scope,
        content
    ).also { descriptions.add(it) }

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

class InputField<C : HTMLElement>(tag: Tag<C>, id: String?) :
    Textfield<C, HtmlTag<HTMLInputElement>>(tag, id) {

    override val placeholder =
        AttributeHook(HtmlTag<HTMLInputElement>::placeholder, HtmlTag<HTMLInputElement>::placeholder)
    override val disabled = BooleanAttributeHook(
        HtmlTag<HTMLInputElement>::disabled,
        HtmlTag<HTMLInputElement>::disabled
    )
    val type = AttributeHook(HtmlTag<HTMLInputElement>::type, HtmlTag<HTMLInputElement>::type).apply { this("text") }

    fun RenderContext.inputTextfield(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLInputElement>.() -> Unit
    ) = input(classes, id = fieldId, scope = scope, content).apply {
        attr(Aria.invalid, "true".whenever(value.hasError))
        value.handler?.invoke(changes.values())
        value(value.data)
        hook(placeholder, type, disabled)
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

fun <C : HTMLElement> RenderContext.inputField(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: InputField<C>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    InputField(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.inputField(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: InputField<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = inputField(classes, id, scope, RenderContext::div, initialize)


class TextArea<C : HTMLElement>(tag: Tag<C>, id: String?) :
    Textfield<C, HtmlTag<HTMLTextAreaElement>>(tag, id) {

    override val placeholder =
        AttributeHook(HtmlTag<HTMLTextAreaElement>::placeholder, HtmlTag<HTMLTextAreaElement>::placeholder)
    override val disabled =
        BooleanAttributeHook(HtmlTag<HTMLTextAreaElement>::disabled, HtmlTag<HTMLTextAreaElement>::disabled)

    fun RenderContext.textareaTextfield(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: HtmlTag<HTMLTextAreaElement>.() -> Unit
    ) = textarea(classes, id = fieldId, scope = scope, content).apply {
        attr(Aria.invalid, "true".whenever(value.hasError))
        value.handler?.invoke(changes.values())
        value(value.data)
        hook(placeholder, disabled)
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

fun <C : HTMLElement> RenderContext.textArea(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: TextArea<C>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    TextArea(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.textArea(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: TextArea<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = textArea(classes, id, scope, RenderContext::div, initialize)
