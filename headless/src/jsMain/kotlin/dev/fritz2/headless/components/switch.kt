package dev.fritz2.headless.components

import dev.fritz2.dom.HtmlTag
import dev.fritz2.dom.html.*
import dev.fritz2.headless.foundation.Aria
import dev.fritz2.headless.foundation.TagFactory
import dev.fritz2.headless.foundation.whenever
import dev.fritz2.headless.hooks.BasicHook
import dev.fritz2.headless.hooks.DatabindingHook
import dev.fritz2.headless.hooks.hook
import dev.fritz2.headless.validation.ComponentValidationMessage
import dev.fritz2.identification.Id
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLElement

abstract class AbstractHeadlessSwitch<C : HtmlTag<HTMLElement>>(val renderContext: C, private val id: String?) :
    RenderContext by renderContext {

    class ToggleDatabindingHook : DatabindingHook<HtmlTag<HTMLElement>, Unit, Boolean>() {
        override fun HtmlTag<HTMLElement>.render(payload: Unit) {
            handler?.invoke(data.flatMapLatest { state -> clicks.map { !state } })
        }
    }

    class KeyboardNavigationHook(
        private val value: ToggleDatabindingHook
    ) : BasicHook<HtmlTag<HTMLElement>, Unit, Unit>() {
        operator fun invoke() = this.also { hook ->
            apply = {
                hook.value.handler?.invoke(
                    hook.value.data.flatMapLatest { state ->
                        keydowns.events.filter { shortcutOf(it) == Keys.Space }.map {
                            it.stopImmediatePropagation()
                            it.preventDefault()
                            !state
                        }
                    })
            }
        }
    }

    val value = ToggleDatabindingHook()
    val enabled: Flow<Boolean> = flowOf(false).flatMapLatest { value.data }

    val withKeyboardNavigation = KeyboardNavigationHook(value)

    val componentId: String by lazy { id ?: value.id ?: Id.next() }

    protected var validationMessages: HtmlTag<HTMLElement>? = null

    abstract fun C.render()

    protected fun renderSwitchCore(renderContext: HtmlTag<HTMLElement>) = renderContext.apply {
        attr("role", Aria.Role.switch)
        attr(Aria.checked, enabled.asString())
        attr(Aria.invalid, "true".whenever(value.hasError))
        attr("tabindex", "0")
        hook(value, Unit)
        hook(withKeyboardNavigation, Unit)
    }

    fun <CV : HtmlTag<HTMLElement>> RenderContext.switchValidationMessages(
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

    fun RenderContext.switchValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Div.(List<ComponentValidationMessage>) -> Unit
    ) = switchValidationMessages(classes, scope, RenderContext::div, content)
}

class HeadlessSwitchWithLabel<C : HtmlTag<HTMLElement>>(renderContext: C, id: String?) :
    AbstractHeadlessSwitch<C>(renderContext, id) {

    private var toggle: HtmlTag<HTMLElement>? = null
    private var label: HtmlTag<HTMLElement>? = null
    private var description: HtmlTag<HTMLElement>? = null

    override fun C.render() {
        attr("id", componentId)
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

    fun <CT : HtmlTag<HTMLElement>> RenderContext.switchToggle(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CT>,
        content: CT.() -> Unit
    ) = tag(this, classes, "$componentId-toggle", scope) {
        content()
        renderSwitchCore(this)
    }.also { toggle = it }

    fun RenderContext.switchToggle(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Button.() -> Unit
    ) = switchToggle(classes, scope, RenderContext::button, content).apply {
        attr("type", "button")
    }

    fun <CL : HtmlTag<HTMLElement>> RenderContext.switchLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CL>,
        content: CL.() -> Unit
    ) = tag(this, classes, "$componentId-label", scope, content).apply {
        hook(value, Unit)
    }.also { label = it }

    fun RenderContext.switchLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Label.() -> Unit
    ) = switchLabel(classes, scope, RenderContext::label) {
        content()
        `for`(componentId)
    }

    fun <CL : HtmlTag<HTMLElement>> RenderContext.switchDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CL>,
        content: CL.() -> Unit
    ) = tag(this, classes, "$componentId-description", scope, content).also { description = it }

    fun RenderContext.switchDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Span.() -> Unit
    ) = switchDescription(classes, scope, RenderContext::span, content)

}

fun <C : HtmlTag<HTMLElement>> RenderContext.headlessSwitchWithLabel(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<C>,
    initialize: HeadlessSwitchWithLabel<C>.() -> Unit
): C = tag(this, classes, id, scope) {
    HeadlessSwitchWithLabel(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.headlessSwitchWithLabel(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessSwitchWithLabel<Div>.() -> Unit
): Div = headlessSwitchWithLabel(classes, id, scope, RenderContext::div, initialize)


class HeadlessSwitch<C : HtmlTag<HTMLElement>>(renderContext: C, id: String?) :
    AbstractHeadlessSwitch<C>(renderContext, id) {

    override fun C.render() {
        attr("id", componentId)
        renderSwitchCore(this)
        attr(
            Aria.describedby,
            value.validationMessages.map { messages ->
                if (messages.isNotEmpty()) validationMessages?.id else null
            }
        )
    }
}

fun <C : HtmlTag<HTMLElement>> RenderContext.headlessSwitch(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<C>,
    initialize: HeadlessSwitch<C>.() -> Unit
): C = tag(this, classes, id, scope) {
    HeadlessSwitch(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.headlessSwitch(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessSwitch<Button>.() -> Unit
): Button = headlessSwitch(classes, id, scope, RenderContext::button, initialize)
    .apply { attr("type", "button") }
