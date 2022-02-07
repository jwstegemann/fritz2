package dev.fritz2.headless.components

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.*
import dev.fritz2.headless.foundation.Aria
import dev.fritz2.headless.foundation.DatabindingProperty
import dev.fritz2.headless.foundation.TagFactory
import dev.fritz2.headless.validation.ComponentValidationMessage
import dev.fritz2.identification.Id
import kotlinx.coroutines.flow.*
import org.w3c.dom.*


abstract class AbstractSwitch<C : HTMLElement>(tag: Tag<C>, private val explicitId: String?) :
    Tag<C> by tag {

    val value = DatabindingProperty<Boolean>()
    val enabled: Flow<Boolean> = flowOf(false).flatMapLatest { value.data }

    val componentId: String by lazy { explicitId ?: value.id ?: Id.next() }

    protected var validationMessages: Tag<HTMLElement>? = null

    abstract fun render()

    protected fun renderSwitchCore(renderContext: Tag<HTMLElement>) = renderContext.apply {
        attr("role", Aria.Role.switch)
        attr(Aria.checked, enabled.asString())
        attr(Aria.invalid, "true".whenever(value.hasError))
        attr("tabindex", "0")
        value.handler?.invoke(value.data.flatMapLatest { state -> clicks.map { !state } })
        value.handler?.invoke(
            value.data.flatMapLatest { state ->
                keydowns.filter { shortcutOf(it) == Keys.Space }.map {
                    it.stopImmediatePropagation()
                    it.preventDefault()
                    !state
                }
            })
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

class SwitchWithLabel<C : HTMLElement>(tag: Tag<C>, id: String?) :
    AbstractSwitch<C>(tag, id) {

    private var toggle: Tag<HTMLElement>? = null
    private var label: Tag<HTMLElement>? = null
    private var descriptions: MutableList<Tag<HTMLElement>> = mutableListOf()

    override fun render() {
        attr("id", componentId)
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
        value.handler?.invoke(value.data.flatMapLatest { state -> clicks.map { !state } })
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
    ) = tag(
        this,
        classes,
        "$componentId-description-${descriptions.size}",
        scope,
        content
    ).also { descriptions.add(it) }

    fun RenderContext.switchDescription(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLSpanElement>.() -> Unit
    ) = switchDescription(classes, scope, RenderContext::span, content)

}

fun <C : HTMLElement> RenderContext.switchWithLabel(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: SwitchWithLabel<C>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    SwitchWithLabel(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.switchWithLabel(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: SwitchWithLabel<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = switchWithLabel(classes, id, scope, RenderContext::div, initialize)


class Switch<C : HTMLElement>(tag: Tag<C>, explicitId: String?) :
    AbstractSwitch<C>(tag, explicitId) {

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

fun <C : HTMLElement> RenderContext.switch(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: Switch<C>.() -> Unit
): Tag<C> = tag(this, classes, id, scope) {
    Switch(this, id).run {
        initialize()
        render()
    }
}

fun RenderContext.switch(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: Switch<HTMLButtonElement>.() -> Unit
): Tag<HTMLButtonElement> = switch(classes, id, scope, RenderContext::button, initialize)
    .apply { attr("type", "button") }
