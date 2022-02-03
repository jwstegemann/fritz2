package dev.fritz2.headless.components

import dev.fritz2.dom.Tag
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
import org.w3c.dom.*

abstract class AbstractHeadlessSwitch<C : HTMLElement>(tag: Tag<C>, private val explicitId: String?) :
    Tag<C> by tag {

    class ToggleDatabindingHook : DatabindingHook<Tag<HTMLElement>, Unit, Boolean>() {
        override fun Tag<HTMLElement>.render(payload: Unit) {
            handler?.invoke(data.flatMapLatest { state -> clicks.map { !state } })
        }
    }

    class KeyboardNavigationHook(
        private val value: ToggleDatabindingHook
    ) : BasicHook<Tag<HTMLElement>, Unit, Unit>() {
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

    val componentId: String by lazy { explicitId ?: value.id ?: Id.next() }

    protected var validationMessages: Tag<HTMLElement>? = null

    abstract fun render()

    protected fun renderSwitchCore(renderContext: Tag<HTMLElement>) = renderContext.apply {
        attr("role", Aria.Role.switch)
        attr(Aria.checked, enabled.asString())
        attr(Aria.invalid, "true".whenever(value.hasError))
        attr("tabindex", "0")
        hook(value, Unit)
        hook(withKeyboardNavigation, Unit)
    }

    fun <CV : HTMLElement> RenderContext.switchValidationMessages(
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

    fun RenderContext.switchValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLDivElement>.(List<ComponentValidationMessage>) -> Unit
    ) = switchValidationMessages(classes, scope, RenderContext::div, content)
}

class HeadlessSwitchWithLabel<C : HTMLElement>(tag: Tag<C>, id: String?) :
    AbstractHeadlessSwitch<C>(tag, id) {

    private var toggle: Tag<HTMLElement>? = null
    private var label: Tag<HTMLElement>? = null
    private var description: Tag<HTMLElement>? = null

    override fun render() {
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

    fun <CT : HTMLElement> RenderContext.switchToggle(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CT>>,
        content: Tag<CT>.() -> Unit
    ) = tag(this, classes, "$componentId-toggle", scope) {
        content()
        renderSwitchCore(this)
    }.also { toggle = it }

    fun RenderContext.switchToggle(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLButtonElement>.() -> Unit
    ) = switchToggle(classes, scope, RenderContext::button, content).apply {
        attr("type", "button")
    }

    fun <CL : HTMLElement> RenderContext.switchLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ) = tag(this, classes, "$componentId-label", scope, content).apply {
        hook(value, Unit)
    }.also { label = it }

    fun RenderContext.switchLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLLabelElement>.() -> Unit
    ) = switchLabel(classes, scope, RenderContext::label) {
        content()
        `for`(componentId)
    }

    fun <CL : HTMLElement> RenderContext.switchDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ) = tag(this, classes, "$componentId-description", scope, content).also { description = it }

    fun RenderContext.switchDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLSpanElement>.() -> Unit
    ) = switchDescription(classes, scope, RenderContext::span, content)

}

fun <C : HTMLElement> RenderContext.headlessSwitchWithLabel(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: HeadlessSwitchWithLabel<C>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    HeadlessSwitchWithLabel(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.headlessSwitchWithLabel(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessSwitchWithLabel<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = headlessSwitchWithLabel(classes, id, scope, RenderContext::div, initialize)


class HeadlessSwitch<C : HTMLElement>(tag: Tag<C>, explicitId: String?) :
    AbstractHeadlessSwitch<C>(tag, explicitId) {

    override fun render() {
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

fun <C : HTMLElement> RenderContext.headlessSwitch(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: HeadlessSwitch<C>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    HeadlessSwitch(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.headlessSwitch(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessSwitch<HTMLButtonElement>.() -> Unit
): Tag<HTMLButtonElement> = headlessSwitch(classes, id, scope, RenderContext::button, initialize)
    .apply { attr("type", "button") }
