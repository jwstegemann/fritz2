package dev.fritz2.headless.components

import dev.fritz2.dom.Listener
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.*
import dev.fritz2.headless.foundation.Aria
import dev.fritz2.headless.foundation.DatabindingProperty
import dev.fritz2.headless.foundation.TagFactory
import dev.fritz2.headless.validation.ComponentValidationMessage
import dev.fritz2.identification.Id
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.w3c.dom.*

class CheckboxGroup<C : HTMLElement, T>(tag: Tag<C>, private val explicitId: String?) :
    Tag<C> by tag {

    private var label: Tag<HTMLElement>? = null
    private var validationMessages: Tag<HTMLElement>? = null

    val value = DatabindingProperty<List<T>>()

    val componentId: String by lazy { explicitId ?: value.id ?: Id.next() }

    fun render() {
        attr("id", componentId)
        attr("role", Aria.Role.group)
        attr(Aria.invalid, "true".whenever(value.hasError))
        label?.let { attr(Aria.labelledby, it.id) }
    }

    fun <CL : HTMLElement> RenderContext.checkboxGroupLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ) = tag(this, classes, "$componentId-label", scope, content).also { label = it }

    fun RenderContext.checkboxGroupLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLLabelElement>.() -> Unit
    ) = checkboxGroupLabel(classes, scope, RenderContext::label, content)

    fun <CV : HTMLElement> RenderContext.checkboxGroupValidationMessages(
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

    fun RenderContext.checkboxGroupValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLDivElement>.(List<ComponentValidationMessage>) -> Unit
    ) = checkboxGroupValidationMessages(classes, scope, RenderContext::div, content)

    inner class CheckboxGroupOption<CO : HTMLElement>(
        tag: Tag<CO>,
        private val option: T,
        id: String?
    ) : Tag<CO> by tag {

        val selected = value.data.map { it.contains(option) }

        private var toggle: Tag<HTMLElement>? = null
        private var label: Tag<HTMLElement>? = null
        private var descriptions: MutableList<Tag<HTMLElement>> = mutableListOf()

        val optionId = "$componentId-${id ?: Id.next()}"

        fun render() {
            toggle?.apply {
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

        fun <CT : HTMLElement> RenderContext.checkboxGroupOptionToggle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CT>>,
            content: Tag<CT>.() -> Unit
        ) = tag(this, classes, optionId, scope) {
            content()
            attr("role", Aria.Role.checkbox)
            attr(Aria.checked, selected.asString())
            attr("tabindex", "0")
            var withKeyboardNavigation = true
            var toggleEvent: Listener<*, *> = clicks
            if (domNode is HTMLInputElement) {
                if (domNode.getAttribute("name") == null) {
                    attr("name", componentId)
                }
                withKeyboardNavigation = false
                toggleEvent = changes
            }
            value.handler?.invoke(value.data.flatMapLatest { value ->
                toggleEvent.map { if (value.contains(option)) value - option else value + option }
            })
            if (withKeyboardNavigation) {
                value.handler?.invoke(
                    value.data.flatMapLatest { value ->
                        keydowns.filter { shortcutOf(it) == Keys.Space }.map {
                            it.stopImmediatePropagation()
                            it.preventDefault()
                            if (value.contains(option)) value - option else value + option
                        }
                    })
            }
        }.also { toggle = it }

        fun RenderContext.checkboxGroupOptionToggle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLDivElement>.() -> Unit
        ) = checkboxGroupOptionToggle(classes, scope, RenderContext::div, content)

        fun <CL : HTMLElement> RenderContext.checkboxGroupOptionLabel(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CL>>,
            content: Tag<CL>.() -> Unit
        ) = tag(this, classes, "$optionId-label", scope, content).also { label = it }

        fun RenderContext.checkboxGroupOptionLabel(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLLabelElement>.() -> Unit
        ) = checkboxGroupOptionLabel(classes, scope, RenderContext::label) {
            content()
            `for`(optionId)
        }

        fun <CL : HTMLElement> RenderContext.checkboxGroupOptionDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CL>>,
            content: Tag<CL>.() -> Unit
        ) = tag(
            this,
            classes,
            "$optionId-description-${descriptions.size}",
            scope,
            content
        ).also { descriptions.add(it) }

        fun RenderContext.checkboxGroupOptionDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Tag<HTMLSpanElement>.() -> Unit
        ) = checkboxGroupOptionDescription(classes, scope, RenderContext::span, content)
    }

    fun <CO : HTMLElement> RenderContext.checkboxGroupOption(
        option: T,
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CO>>,
        initialize: CheckboxGroupOption<CO>.() -> Unit
    ): Tag<CO> = tag(this, classes, id, scope) {
        CheckboxGroupOption(this, option, id).run {
            initialize()
            render()
        }
    }

    fun RenderContext.checkboxGroupOption(
        option: T,
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: CheckboxGroupOption<HTMLDivElement>.() -> Unit
    ): Tag<HTMLDivElement> = checkboxGroupOption(option, classes, id, scope, RenderContext::div, initialize)
}

fun <C : HTMLElement, T> RenderContext.checkboxGroup(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: CheckboxGroup<C, T>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    CheckboxGroup<C, T>(this, id).run {
        initialize()
        render()
    }
}

fun <T> RenderContext.checkboxGroup(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: CheckboxGroup<HTMLDivElement, T>.() -> Unit
): Tag<HTMLDivElement> = checkboxGroup(classes, id, scope, RenderContext::div, initialize)
