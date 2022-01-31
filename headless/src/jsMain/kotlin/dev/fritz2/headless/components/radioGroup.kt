package dev.fritz2.headless.components

import dev.fritz2.binding.Store
import dev.fritz2.binding.storeOf
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.*

import dev.fritz2.identification.Id
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.hooks.BasicHook
import dev.fritz2.headless.hooks.ItemDatabindingHook
import dev.fritz2.headless.hooks.hook
import dev.fritz2.headless.validation.ComponentValidationMessage
import kotlinx.browser.document
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLElement

class HeadlessRadioGroup<C : Tag<HTMLElement>, T>(val renderContext: C, private val id: String?) :
    RenderContext by renderContext {

    class DatabindingHook<T> : ItemDatabindingHook<Tag<HTMLElement>, T, T>() {

        override fun Tag<HTMLElement>.render(payload: T) {
            val event = if (this is Input) changes else clicks
            handler?.invoke(event.map { payload })
        }

        override fun isSelected(item: T): Flow<Boolean> = data.map { it == item }
    }

    class KeyboardNavigationHook<T>(
        private val value: DatabindingHook<T>,
        private val isActive: Store<T?>
    ) : BasicHook<Tag<HTMLElement>, Unit, List<T>>() {
        operator fun invoke() = this.also { hook ->
            apply = { options ->
                hook.value.handler?.invoke(
                    hook.value.data.flatMapLatest { option ->
                        keydowns.events.mapNotNull { event ->
                            when (shortcutOf(event)) {
                                Keys.ArrowDown -> options.rotateNext(option)
                                Keys.ArrowUp -> options.rotatePrevious(option)
                                else -> null
                            }.also {
                                if (it != null) {
                                    event.stopImmediatePropagation()
                                    event.preventDefault()
                                    hook.isActive.update(it)
                                }
                            }
                        }
                    })
            }
        }
    }

    private var label: Tag<HTMLElement>? = null
    private var validationMessages: Tag<HTMLElement>? = null

    val componentId: String by lazy { id ?: value.id ?: Id.next() }
    private val isActive: Store<T?> = storeOf(null)
    val value = DatabindingHook<T>()
    val withKeyboardNavigation = KeyboardNavigationHook(value, isActive)

    var options: List<T> = emptyList()


    fun C.render() {
        attr("id", componentId)
        attr("role", Aria.Role.radiogroup)
        attr(Aria.invalid, "true".whenever(value.hasError))
        label?.let { attr(Aria.labelledby, it.id) }
        hook(withKeyboardNavigation, options)
    }

    fun <CL : Tag<HTMLElement>> RenderContext.radioGroupLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CL>,
        content: CL.() -> Unit
    ) = tag(this, classes, "$componentId-label", scope, content).also { label = it }

    fun RenderContext.radioGroupLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Label.() -> Unit
    ) = radioGroupLabel(classes, scope, RenderContext::label, content)

    fun <CV : Tag<HTMLElement>> RenderContext.radioGroupValidationMessages(
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

    fun RenderContext.radioGroupValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Div.(List<ComponentValidationMessage>) -> Unit
    ) = radioGroupValidationMessages(classes, scope, RenderContext::div, content)

    // TODO: Make it rather a Fragment than a Tag
    inner class RadioGroupOption<CO : Tag<HTMLElement>>(
        val tag: CO,
        private val option: T,
        id: String?
    ) : RenderContext by tag {

        val selected = value.isSelected(option)
        val active = isActive.data.map { it == option }.distinctUntilChanged()

        private var toggle: Tag<HTMLElement>? = null
        private var label: Tag<HTMLElement>? = null
        private var description: Tag<HTMLElement>? = null

        val optionId = "$componentId-${id ?: Id.next()}"

        fun CO.render() {
            toggle?.apply {
                label?.let { attr(Aria.labelledby, it.id) }
                attr(
                    Aria.describedby,
                    value.validationMessages.map { messages ->
                        if (messages.isNotEmpty()) validationMessages?.id else description?.id
                    }
                )
            }
        }

        fun <CT : Tag<HTMLElement>> RenderContext.radioGroupOptionToggle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<CT>,
            content: CT.() -> Unit
        ) = tag(this, classes, optionId, scope) {
            content()
            attr("role", Aria.Role.radio)
            attr(Aria.checked, selected.asString())
            attr("tabindex", selected.map { if (it) "0" else "-1" })
            if (this is Input && domNode.getAttribute("name") == null) {
                attr("name", componentId)
            }
            active handledBy {
                if (it && domNode != document.activeElement) {
                    domNode.focus()
                }
            }
            focuss.map { option } handledBy isActive.update
            blurs.map { null } handledBy isActive.update

            hook(value, option)
        }.also { toggle = it }

        fun RenderContext.radioGroupOptionToggle(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Div.() -> Unit
        ) = radioGroupOptionToggle(classes, scope, RenderContext::div, content)

        fun <CL : Tag<HTMLElement>> RenderContext.radioGroupOptionLabel(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<CL>,
            content: CL.() -> Unit
        ) = tag(this, classes, "$optionId-label", scope, content).also { label = it }

        fun RenderContext.radioGroupOptionLabel(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Label.() -> Unit
        ) = radioGroupOptionLabel(classes, scope, RenderContext::label) {
            content()
            `for`(optionId)
        }

        fun <CL : Tag<HTMLElement>> RenderContext.radioGroupOptionDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<CL>,
            content: CL.() -> Unit
        ) = tag(this, classes, "$optionId-description", scope, content).also { description = it }

        fun RenderContext.radioGroupOptionDescription(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            content: Span.() -> Unit
        ) = radioGroupOptionDescription(classes, scope, RenderContext::span, content)
    }

    fun <CO : Tag<HTMLElement>> RenderContext.radioGroupOption(
        option: T,
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CO>,
        initialize: RadioGroupOption<CO>.() -> Unit
    ): CO = tag(this, classes, id, scope) {
        RadioGroupOption(this, option, id).run {
            initialize()
            render()
        }
    }

    fun RenderContext.radioGroupOption(
        option: T,
        classes: String? = null,
        id: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: RadioGroupOption<Div>.() -> Unit
    ): Div = radioGroupOption(option, classes, id, scope, RenderContext::div, initialize)
}

fun <C : Tag<HTMLElement>, T> RenderContext.headlessRadioGroup(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<C>,
    initialize: HeadlessRadioGroup<C, T>.() -> Unit
): C = tag(this, classes, id, scope) {
    HeadlessRadioGroup<C, T>(this, id).run {
        initialize()
        render()
    }
}

fun <T> RenderContext.headlessRadioGroup(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessRadioGroup<Div, T>.() -> Unit
): Div = headlessRadioGroup(classes, id, scope, RenderContext::div, initialize)
