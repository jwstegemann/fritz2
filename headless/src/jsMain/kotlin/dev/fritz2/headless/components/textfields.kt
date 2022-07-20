package dev.fritz2.headless.components


import dev.fritz2.core.*
import dev.fritz2.headless.foundation.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.w3c.dom.*

/**
 * This base class provides the building blocks to implement textfields.
 *
 * There exist two different implementations:
 * - [InputField] for a single line input. For more information refer to the
 *      [official documentation](https://www.fritz2.dev/headless/inputfield/)
 * - [TextArea] for a multi line input. for a single line input. For more information refer to the
 *      [official documentation](https://www.fritz2.dev/headless/textarea/)
 *
 */
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
    ): Tag<CL> {
        addComponentStructureInfo("textfieldLabel", this@textfieldLabel.scope, this)
        return tag(this, classes, "$componentId-label", scope, content).also { label = it }
    }

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
    ): Tag<CD> {
        addComponentStructureInfo("textfieldDescription", this@textfieldDescription.scope, this)
        return tag(
            this,
            classes,
            "$componentId-description-${descriptions.size}",
            scope,
            content
        ).also { descriptions.add(it) }
    }

    protected fun RenderContext.textfieldDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLParagraphElement>.() -> Unit
    ) = textfieldDescription(classes, scope, RenderContext::p, content)

    protected fun <CV : HTMLElement> RenderContext.textfieldValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CV>>,
        initialize: ValidationMessages<CV>.() -> Unit
    ) {
        value.validationMessages.map { it.isNotEmpty() }.distinctUntilChanged().render { isNotEmpty ->
            if (isNotEmpty) {
                addComponentStructureInfo(
                    "textfieldValidationMessages",
                    this@textfieldValidationMessages.scope,
                    this@Textfield
                )
                tag(this, classes, "$componentId-${ValidationMessages.ID_SUFFIX}", scope) {
                    validationMessages = this
                    initialize(ValidationMessages(value.validationMessages, this))
                }
            }
        }
    }

    protected fun RenderContext.textfieldValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: ValidationMessages<HTMLDivElement>.() -> Unit
    ) = textfieldValidationMessages(classes, scope, RenderContext::div, initialize)
}

/**
 * This class provides the building blocks to implement an input-field.
 *
 * Use [inputField] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/inputfield/)
 */
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
    ): Tag<HTMLInputElement> {
        addComponentStructureInfo("inputTextfield", this@inputTextfield.scope, this)
        return input(classes, id = fieldId, scope = scope, content).apply {
            attr(Aria.invalid, "true".whenever(value.hasError))
            value.handler?.invoke(changes.values())
            value(value.data)
            hook(placeholder, type, disabled)
        }.also { field = it }
    }

    /**
     * Factory function to create a [inputLabel].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/inputfield/#inputlabel)
     */
    fun <CL : HTMLElement> RenderContext.inputLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ) = textfieldLabel(classes, scope, tag, content)

    /**
     * Factory function to create a [inputLabel] with a [HTMLLabelElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/inputfield/#inputlabel)
     */
    fun RenderContext.inputLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLLabelElement>.() -> Unit
    ) = textfieldLabel(classes, scope, content)

    /**
     * Factory function to create a [inputDescription].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/inputfield/#inputdescription)
     */
    fun <CD : HTMLElement> RenderContext.inputDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CD>>,
        content: Tag<CD>.() -> Unit
    ) = textfieldDescription(classes, scope, tag, content)

    /**
     * Factory function to create a [inputDescription] with a [HTMLParagraphElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/inputfield/#inputdescription)
     */
    fun RenderContext.inputDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLParagraphElement>.() -> Unit
    ) = textfieldDescription(classes, scope, content)

    /**
     * Factory function to create a [inputValidationMessages].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/inputfield/#inputvalidationmessages)
     */
    fun <CV : HTMLElement> RenderContext.inputValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CV>>,
        initialize: ValidationMessages<CV>.() -> Unit
    ) = textfieldValidationMessages(classes, scope, tag, initialize)

    /**
     * Factory function to create a [inputValidationMessages] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/inputfield/#inputvalidationmessages)
     */
    fun RenderContext.inputValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: ValidationMessages<HTMLDivElement>.() -> Unit
    ) = textfieldValidationMessages(classes, scope, initialize)
}

/**
 * Factory function to create a [InputField].
 *
 * API-Sketch:
 * ```kotlin
 * inputField() {
 *     val value: DatabindingProperty<String>
 *     val placeHolder: AttributeHook<String>
 *     val disabled: BooleanAttributeHook
 *
 *     inputTextfield() { }
 *     inputLabel() { }
 *     inputDescription() { } // use multiple times
 *     inputValidationMessages() {
 *         msgs: Flow<List<ComponentValidationMessage>>
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/inputfield/#inputfield)
 */
fun <C : HTMLElement> RenderContext.inputField(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: InputField<C>.() -> Unit
): Tag<C> {
    addComponentStructureInfo("inputField", this@inputField.scope, this)
    return tag(this, classes, id, scope) {
        InputField(this, id).run {
            initialize()
            render()
        }
    }
}

/**
 * Factory function to create a [InputField] with a [HTMLDivElement] as default root [Tag].
 *
 * API-Sketch:
 * ```kotlin
 * inputField() {
 *     val value: DatabindingProperty<String>
 *     val placeHolder: AttributeHook<String>
 *     val disabled: BooleanAttributeHook
 *
 *     inputTextfield() { }
 *     inputLabel() { }
 *     inputDescription() { } // use multiple times
 *     inputValidationMessages() {
 *         msgs: Flow<List<ComponentValidationMessage>>
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/inputfield/#inputfield)
 */
fun RenderContext.inputField(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: InputField<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = inputField(classes, id, scope, RenderContext::div, initialize)

/**
 * This class provides the building blocks to implement a textarea.
 *
 * Use [textArea] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/textarea/)
 */
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
    ): Tag<HTMLTextAreaElement> {
        addComponentStructureInfo("textareaTextfield", this@textareaTextfield.scope, this)
        return textarea(classes, id = fieldId, scope = scope, content).apply {
            attr(Aria.invalid, "true".whenever(value.hasError))
            value.handler?.invoke(changes.values())
            value(value.data)
            hook(placeholder, disabled)
        }.also { field = it }
    }

    /**
     * Factory function to create a [textareaLabel].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/textarea/#textarealabel)
     */
    fun <CL : HTMLElement> RenderContext.textareaLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ) = textfieldLabel(classes, scope, tag, content)

    /**
     * Factory function to create a [textareaLabel] with a [HTMLLabelElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/textarea/#textarealabel)
     */
    fun RenderContext.textareaLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLLabelElement>.() -> Unit
    ) = textfieldLabel(classes, scope, content)

    /**
     * Factory function to create a [textareaDescription].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/textarea/#textareadescription)
     */
    fun <CD : HTMLElement> RenderContext.textareaDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CD>>,
        content: Tag<CD>.() -> Unit
    ) = textfieldDescription(classes, scope, tag, content)

    /**
     * Factory function to create a [textareaDescription] with a [HTMLParagraphElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/textarea/#textareadescription)
     */
    fun RenderContext.textareaDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLParagraphElement>.() -> Unit
    ) = textfieldDescription(classes, scope, content)

    /**
     * Factory function to create a [textareaValidationMessages].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/textarea/#textareavalidationmessages)
     */
    fun <CV : HTMLElement> RenderContext.textareaValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CV>>,
        initialize: ValidationMessages<CV>.() -> Unit
    ) = textfieldValidationMessages(classes, scope, tag, initialize)

    /**
     * Factory function to create a [textareaValidationMessages] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://www.fritz2.dev/headless/textarea/#textareavalidationmessages)
     */
    fun RenderContext.textareaValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: ValidationMessages<HTMLDivElement>.() -> Unit
    ) = textfieldValidationMessages(classes, scope, initialize)
}

/**
 * Factory function to create a [TextArea].
 *
 * API-Sketch:
 * ```kotlin
 * textArea() {
 *     val value: DatabindingProperty<String>
 *     val placeHolder: AttributeHook<String>
 *     val disabled: BooleanAttributeHook
 *
 *     textareaTextfield() { }
 *     textareaLabel() { }
 *     textareaDescription() { } // use multiple times
 *     textareaValidationMessages() {
 *         val msgs: Flow<List<ComponentValidationMessage>>
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/textarea/#textarea)
 */
fun <C : HTMLElement> RenderContext.textArea(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: TextArea<C>.() -> Unit
): Tag<C> {
    addComponentStructureInfo("textArea", this@textArea.scope, this)
    return tag(this, classes, id, scope) {
        TextArea(this, id).run {
            initialize()
            render()
        }
    }
}

/**
 * Factory function to create a [TextArea] with a [HTMLDivElement] as default root [Tag].
 *
 * API-Sketch:
 * ```kotlin
 * textArea() {
 *     val value: DatabindingProperty<String>
 *     val placeHolder: AttributeHook<String>
 *     val disabled: BooleanAttributeHook
 *
 *     textareaTextfield() { }
 *     textareaLabel() { }
 *     textareaDescription() { } // use multiple times
 *     textareaValidationMessages() {
 *         val msgs: Flow<List<ComponentValidationMessage>>
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://www.fritz2.dev/headless/textarea/#textarea)
 */
fun RenderContext.textArea(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: TextArea<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = textArea(classes, id, scope, RenderContext::div, initialize)
