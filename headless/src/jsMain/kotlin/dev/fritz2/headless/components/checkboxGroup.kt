package dev.fritz2.headless.components

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.*
import dev.fritz2.identification.Id
import dev.fritz2.headless.foundation.Aria
import dev.fritz2.headless.foundation.TagFactory
import dev.fritz2.headless.foundation.whenever
import dev.fritz2.headless.foundation.Hook
import dev.fritz2.headless.foundation.ItemDatabindingHook
import dev.fritz2.headless.foundation.hook
import dev.fritz2.headless.validation.ComponentValidationMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLElement

class HeadlessCheckboxGroup<C : Tag<HTMLElement>, T>(val renderContext: C, private val id: String?) :
    RenderContext by renderContext {

    class DatabindingHook<T> : ItemDatabindingHook<Tag<HTMLElement>, T, List<T>>() {

        override fun Tag<HTMLElement>.render(payload: T) {
            val event = if (this is Input) changes else clicks
            handler?.invoke(data.flatMapLatest { value ->
                event.map { if (value.contains(payload)) value - payload else value + payload }
            })
        }

        override fun isSelected(item: T): Flow<Boolean> = data.map { it.contains(item) }
    }

    class KeyboardNavigationHook<T>(
        private val value: DatabindingHook<T>
    ) : Hook<Tag<HTMLElement>, Unit, T>() {
        operator fun invoke() = this.also { hook ->
            apply = { options ->
                hook.value.handler?.invoke(
                    hook.value.data.flatMapLatest { value ->
                        keydowns.events.filter { shortcutOf(it) == Keys.Space }.map {
                            it.stopImmediatePropagation()
                            it.preventDefault()
                            if (value.contains(options)) value - options else value + options
                        }
                    })
            }
        }
    }

    private var label: Tag<HTMLElement>? = null
    private var validationMessages: Tag<HTMLElement>? = null

    val value = DatabindingHook<T>()
    val withKeyboardNavigation = KeyboardNavigationHook(value)
    val componentId: String by lazy { id ?: value.id ?: Id.next() }

    var options: List<T> = emptyList()

    fun C.render() {
        attr("id", componentId)
        attr("role", Aria.Role.group)
        attr(Aria.invalid, "true".whenever(value.hasError))
        label?.let { attr(Aria.labelledby, it.id) }
    }

    fun <CL : Tag<HTMLElement>> RenderContext.checkboxGroupLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CL>,
        content: CL.() -> Unit
    ) = tag(this, classes, "$componentId-label", scope, content).also { label = it }

    fun RenderContext.checkboxGroupLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Label.() -> Unit
    ) = checkboxGroupLabel(classes, scope, RenderContext::label, content)

    fun <CV : Tag<HTMLElement>> RenderContext.checkboxGroupValidationMessages(
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

    fun RenderContext.checkboxGroupValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Div.(List<ComponentValidationMessage>) -> Unit
    ) = checkboxGroupValidationMessages(classes, scope, RenderContext::div, content)

    // TODO: Make it rather a Fragment than a Tag
    inner class CheckboxGroupOption<CO : Tag<HTMLElement>>(
        val tag: CO,
        private val option: T,
        id: String?
    ) : RenderContext by tag {

        val selected = value.isSelected(option)

        private var toggle: Tag<HTMLElement>? = null
        private var label: Tag<HTMLElement>? = null
        private var descriptions: MutableList<Tag<HTMLElement>> = mutableListOf()

        val optionId = "$componentId-${id ?: Id.next()}"

        fun CO.render() {
            toggle?.apply {
                label?.let { attr(Aria.labelledby, it.id) }
                attr(
                    Aria.describedby,
                    value.validationMessages.map { messages ->
                        if (messages.isNotEmpty()) validationMessages?.id else descriptions.map { it.id }
                            .joinToString(" ")
                    }
                )
            }
        }

        fun <CT : Tag<HTMLElement>> RenderContext.checkboxGroupOptionToggle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<CT>,
            content: CT.() -> Unit
        ) = tag(this, classes, optionId, scope) {
            content()
            attr("role", Aria.Role.checkbox)
            attr(Aria.checked, selected.asString())
            attr("tabindex", "0")
            if (this is Input && domNode.getAttribute("name") == null) {
                attr("name", componentId)
            }
            hook(value, option)
            hook(withKeyboardNavigation, option)
        }.also { toggle = it }

        fun RenderContext.checkboxGroupOptionToggle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Div.() -> Unit
        ) = checkboxGroupOptionToggle(classes, scope, RenderContext::div, content)

        fun <CL : Tag<HTMLElement>> RenderContext.checkboxGroupOptionLabel(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<CL>,
            content: CL.() -> Unit
        ) = tag(this, classes, "$optionId-label", scope, content).also { label = it }

        fun RenderContext.checkboxGroupOptionLabel(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Label.() -> Unit
        ) = checkboxGroupOptionLabel(classes, scope, RenderContext::label) {
            content()
            `for`(optionId)
        }

        fun <CL : Tag<HTMLElement>> RenderContext.checkboxGroupOptionDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<CL>,
            content: CL.() -> Unit
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
            content: Span.() -> Unit
        ) = checkboxGroupOptionDescription(classes, scope, RenderContext::span, content)
    }

    fun <CO : Tag<HTMLElement>> RenderContext.checkboxGroupOption(
        option: T,
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CO>,
        initialize: CheckboxGroupOption<CO>.() -> Unit
    ): CO = tag(this, classes, id, scope) {
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
        initialize: CheckboxGroupOption<Div>.() -> Unit
    ): Div = checkboxGroupOption(option, classes, id, scope, RenderContext::div, initialize)
}

fun <C : Tag<HTMLElement>, T> RenderContext.headlessCheckboxGroup(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<C>,
    initialize: HeadlessCheckboxGroup<C, T>.() -> Unit
): C = tag(this, classes, id, scope) {
    HeadlessCheckboxGroup<C, T>(this, id).run {
        initialize()
        render()
    }
}

fun <T> RenderContext.headlessCheckboxGroup(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessCheckboxGroup<Div, T>.() -> Unit
): Div = headlessCheckboxGroup(classes, id, scope, RenderContext::div, initialize)
