package dev.fritz2.headless.components

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.ScopeContext
import dev.fritz2.dom.html.addGlobalStyles
import dev.fritz2.headless.foundation.*
import dev.fritz2.identification.Id
import dev.fritz2.utils.classes
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement


@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
class PopOver<C : HTMLElement>(tag: Tag<C>, id: String?) : Tag<C> by tag, OpenClose() {

    companion object {
        init {
            addGlobalStyles(listOf(
            """.popper[data-popper-reference-hidden] {
                visibility: hidden;
                pointer-events: none;
            }""".trimIndent(),
            """.popper-arrow,
            .popper-arrow::before {
                position: absolute;
                width: 8px;
                height: 8px;
                background: inherit;
            }""".trimIndent(),
            """.popper-arrow {
                visibility: hidden;
            }""".trimIndent(),
            """.popper-arrow::before {
                visibility: visible;
                content: '';
                transform: rotate(45deg);
            }""".trimIndent(),
            """.popper[data-popper-placement^='top'] > .popper-arrow {
                bottom: -4px;
            }""".trimIndent(),
            """.popper[data-popper-placement^='bottom'] > .popper-arrow {
                top: -4px;
            }""".trimIndent(),
            """.popper[data-popper-placement^='left'] > .popper-arrow {
                right: -4px;
            }""".trimIndent(),
            """.popper[data-popper-placement^='right'] > .popper-arrow {
                left: -4px;
            }""".trimIndent(),
            """.popper[data-popper-placement='bottom'] > .transform {
                transform-origin: top;
            }""".trimIndent(),
            """.popper[data-popper-placement='bottom-start'] > .transform {
                transform-origin: top left;
            }""".trimIndent(),
            """.popper[data-popper-placement='bottom-right'] > .transform {
                transform-origin: top right;
            }""".trimIndent(),
            """.popper[data-popper-placement='top'] > .transform {
                transform-origin: bottom;
            }""".trimIndent(),
            """.popper[data-popper-placement='top-start'] > .transform {
                transform-origin: bottom left;
            }""".trimIndent(),
            """.popper[data-popper-placement='top-right'] > .transform {
                transform-origin: bottom right;
            }""".trimIndent(),
            """.popper[data-popper-placement='left'] > .transform {
                transform-origin: right;
            }""".trimIndent(),
            """.popper[data-popper-placement='left-start'] > .transform {
                transform-origin: top right;
            }""".trimIndent(),
            """.popper[data-popper-placement='left-end'] > .transform {
                transform-origin: bottom right;
            }""".trimIndent(),
            """.popper[data-popper-placement='right'] > .transform {
                transform-origin: left;
            }""".trimIndent(),
            """.popper[data-popper-placement='right-start'] > .transform {
                transform-origin: top left;
            }""".trimIndent(),
            """.popper[data-popper-placement='right-end'] > .transform {
                transform-origin: bottom left;
            }""".trimIndent()
                )
            )
        }
    }

    val componentId: String by lazy { id ?: Id.next() }

    private var button: Tag<HTMLElement>? = null

    fun render() {
    }

    fun <CB : HTMLElement> RenderContext.popOverButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CB>>,
        content: Tag<CB>.() -> Unit
    ) = tag(this, classes, "$componentId-button", scope) {
        if (!openClose.isSet) openClose(storeOf(false))
        content()
        attr(Aria.expanded, opened.asString())
        handleOpenCloseEvents()
    }.also { button = it }

    fun RenderContext.popOverButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLButtonElement>.() -> Unit
    ) = popOverButton(classes, scope, RenderContext::button, content).apply {
        attr("type", "button")
    }

    inner class PopOverPanel<C : HTMLElement>(
        val renderContext: RenderContext,
        tagFactory: TagFactory<Tag<C>>,
        classes: String?,
        scope: ScopeContext.() -> Unit
    ) : PopUpPanel<C>(renderContext, tagFactory, classes, "$componentId-items", scope, this@PopOver, button)

    fun <CP : HTMLElement> RenderContext.popOverPanel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CP>>,
        initialize: PopOverPanel<CP>.() -> Unit
    ) {
        PopOverPanel(this, tag, classes, scope).run {
            initialize()
            render()
        }
    }

    fun RenderContext.popOverPanel(
        classes: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        initialize: PopOverPanel<HTMLDivElement>.() -> Unit
    ) = popOverPanel(classes, internalScope, RenderContext::div, initialize)
}

fun <C : HTMLElement> RenderContext.popOver(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: PopOver<C>.() -> Unit
): Tag<C> = tag(this, classes(classes, "relative"), id, scope) {
    PopOver(this, id).run {
        initialize(this)
        render()
    }
    trapFocus()
}

fun RenderContext.popOver(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: PopOver<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = popOver(classes, id, scope, RenderContext::div, initialize)
