package dev.fritz2.headless.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.storeOf
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.*
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.foundation.utils.scrollintoview.scrollIntoView
import dev.fritz2.headless.hooks.DatabindingHook
import dev.fritz2.headless.hooks.hook
import dev.fritz2.headless.validation.ComponentValidationMessage
import dev.fritz2.identification.Id
import dev.fritz2.utils.classes
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLElement
import kotlin.math.max

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
class HeadlessListbox<T, C : Tag<HTMLElement>>(val tag: C, id: String?) : RenderContext by tag,
    OpenClose by OpenCloseDelegate() {

    class ListboxDatabindingHook<T> : DatabindingHook<Tag<HTMLElement>, T, T>() {

        override fun Tag<HTMLElement>.render(payload: T) {
        }
    }

    val value = ListboxDatabindingHook<T>()
    val componentId: String by lazy { id ?: value.id ?: Id.next() }

    private var button: Tag<HTMLElement>? = null

    private val activeIndex = storeOf(-1)
    private var numberOfItems = 0
    private var label: Tag<HTMLElement>? = null
    private var validationMessages: Tag<HTMLElement>? = null

    internal data class ListboxEntry<T>(val value: T, val disabled: Boolean, var character: Char?)

    private val entries = object : RootStore<List<ListboxEntry<T>>>(emptyList()) {
        val addEntry = handle<ListboxEntry<T>> { old, entry -> old + entry }
        val setCharacter = handle<Pair<Int, Char?>> { old, (index, c) ->
            old.mapIndexed { i, o -> if (i == index) o.copy(character = c) else o }
        }

        fun disabledHandler(index: Int) = handle<Boolean> { state, disabled ->
            state.withIndex().map { if (it.index == index) it.value.copy(disabled = disabled) else it.value }
        }
    }

    private val state by lazy { activeIndex.data.combine(entries.data, ::Pair) }

    fun C.render() {
        attr("id", componentId)
        opened.drop(1).filter { !it } handledBy {
            button?.setFocus()
        }
    }

    fun <CB : Tag<HTMLElement>> RenderContext.listboxButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CB>,
        content: CB.() -> Unit
    ) = tag(this, classes, "$componentId-button", scope) {
        if (!openClose.isSet) openClose(storeOf(false))
        content()
        attr(Aria.expanded, opened.asString())
        hook(openClose)
    }.also { button = it }

    fun RenderContext.listboxButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Button.() -> Unit
    ) = listboxButton(classes, scope, RenderContext::button, content).apply {
        attr("type", "button")
    }

    fun <CL : Tag<HTMLElement>> RenderContext.listboxLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CL>,
        content: CL.() -> Unit
    ) = tag(this, classes, "$componentId-label", scope, content).also { label = it }

    fun RenderContext.listboxLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Label.() -> Unit
    ) = listboxLabel(classes, scope, RenderContext::label, content)

    fun <CV : Tag<HTMLElement>> RenderContext.listboxValidationMessages(
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

    fun RenderContext.listboxValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Div.(List<ComponentValidationMessage>) -> Unit
    ) = listboxValidationMessages(classes, scope, RenderContext::div, content)

    inner class ListboxItems<CI : Tag<HTMLElement>>(
        val renderContext: RenderContext,
        tagFactory: TagFactory<CI>,
        classes: String?,
        scope: ScopeContext.() -> Unit
    ) : PopUpPanel<CI>(renderContext, tagFactory, classes, "$componentId-items", scope, this@HeadlessListbox, button) {

        private fun nextItem(currentIndex: Int, direction: Direction, entries: List<ListboxEntry<T>>): Int =
            when (direction) {
                Direction.Next -> (entries.drop(currentIndex + 1).indexOfFirst { !it.disabled } + currentIndex + 1)
                Direction.Previous -> entries.take(max(0, currentIndex)).indexOfLast { !it.disabled }
            }.let { if (it == -1) currentIndex else it }


        private fun firstItem(entries: List<ListboxEntry<T>>) = entries.indexOfFirst { !it.disabled }
        private fun lastItem(entries: List<ListboxEntry<T>>) = entries.indexOfLast { !it.disabled }

        private fun itemByCharacter(entries: List<ListboxEntry<T>>, character: Char) =
            entries.indexOfFirst { !it.disabled && it.character == character }.let {
                if (it == -1) null else it
            }

        override fun render() {
            super.render()

            closeOnEscape()
            closeOnBlur()

            tag.apply {
                attr("tabindex", "0")
                attr("role", Aria.Role.listbox)
                attr(Aria.invalid, "true".whenever(value.hasError))
                label?.let { attr(Aria.labelledby, it.id) }
                attr(Aria.activedescendant, activeIndex.data.map { if (it == -1) null else "$componentId-item-$it" })

                state.flatMapLatest { (currentIndex, entries) ->
                    keydowns.events.mapNotNull { event ->
                        when (shortcutOf(event)) {
                            Keys.ArrowUp -> nextItem(currentIndex, Direction.Previous, entries)
                            Keys.ArrowDown -> nextItem(currentIndex, Direction.Next, entries)
                            Keys.Home -> firstItem(entries)
                            Keys.End -> lastItem(entries)
                            else -> null
                        }.also {
                            if (it != null) {
                                event.preventDefault()
                                event.stopImmediatePropagation()
                            }
                        }
                    }
                } handledBy activeIndex.update

                entries.data.flatMapLatest { entries ->
                    keydowns.events
                        .mapNotNull { event ->
                            if (!Keys.NamedKeys.contains(event.key)) {
                                event.preventDefault()
                                event.stopImmediatePropagation()
                                event.key.first().lowercaseChar()
                            } else null
                        }
                        .mapNotNull { c ->
                            if (c.isLetterOrDigit()) itemByCharacter(entries, c)
                            else null
                        }
                } handledBy activeIndex.update

                value.handler?.invoke(
                    state.flatMapLatest { (currentIndex, entries) ->
                        keydowns.events.filter {
                            setOf(Keys.Enter, Keys.Space).contains(shortcutOf(it))
                        }.mapNotNull {
                            if (currentIndex == -1 || entries[currentIndex].disabled) {
                                null
                            } else {
                                it.preventDefault()
                                it.stopImmediatePropagation()
                                entries[currentIndex].value
                            }
                        }
                    }
                )
            }

            opened.filter { it }.flatMapLatest {
                value.data.flatMapLatest { current ->
                    entries.data.map { entries ->
                        val selectedIndex = entries.indexOfFirst { it.value == current }
                        if (selectedIndex == -1) firstItem(entries) else selectedIndex
                    }
                }
            } handledBy activeIndex.update
        }

        inner class ListboxItem<CM : Tag<HTMLElement>>(val entry: T, val tag: CM, val index: Int) {
            val active = activeIndex.data.map { it == index }
            val selected = value.data.map { it == entry }

            val disabled = entries.data.map { it[index].disabled }
            val disable = entries.disabledHandler(index)

            fun CM.render() {
                mouseenters.events.mapNotNull { if (entries.current[index].disabled) null else index } handledBy activeIndex.update

                attr("tabindex", "-1")
                attr("role", Aria.Role.option)

                active.filter { it } handledBy {
                    scrollIntoView(domNode, HeadlessScrollOptions)
                }

                value.handler?.invoke(
                    mousedowns.events.mapNotNull { e ->
                        e.preventDefault()
                        e.stopImmediatePropagation()
                        entries.current[index].let {
                            if (it.disabled) null
                            else it.value
                        }
                    })

                value.data.map {} handledBy close
            }
        }

        fun <CM : Tag<HTMLElement>> RenderContext.listboxItem(
            entry: T,
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<CM>,
            initialize: ListboxItem<CM>.() -> Unit
        ) {
            val index = numberOfItems++

            entries.addEntry(ListboxEntry(entry, false, null))
            tag(this, classes, "$componentId-item-$index", scope) {
                ListboxItem(entry, this, index).run {
                    initialize()
                    render()
                }
            }.also {
                entries.setCharacter((index to it.domNode.textContent?.trimStart()?.firstOrNull()?.lowercaseChar()))
            }
        }

        fun RenderContext.listboxItem(
            entry: T,
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            initialize: ListboxItem<Button>.() -> Unit
        ) = listboxItem(entry, classes, scope, RenderContext::button, initialize)
    }

    fun <CI : Tag<HTMLElement>> RenderContext.listboxItems(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CI>,
        initialize: ListboxItems<CI>.() -> Unit
    ) {
        if (!openClose.isSet) openClose(storeOf(false))
        ListboxItems(this, tag, classes, scope).run {
            initialize()
            render()
        }
    }

    fun RenderContext.listboxItems(
        classes: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        initialize: ListboxItems<Div>.() -> Unit
    ) = listboxItems(classes, internalScope, RenderContext::div, initialize)
}


fun <T, C : Tag<HTMLElement>> RenderContext.headlessListbox(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<C>,
    initialize: HeadlessListbox<T, C>.() -> Unit
): C = tag(this, classes(classes, "relative"), id, scope) {
    HeadlessListbox<T, C>(this, id).run {
        initialize(this)
        render()
    }
}

fun <T> RenderContext.headlessListbox(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessListbox<T, Div>.() -> Unit
): Div = headlessListbox(classes, id, scope, RenderContext::div, initialize)

