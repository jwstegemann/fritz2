package dev.fritz2.headless.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.storeOf
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.Keys
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.ScopeContext
import dev.fritz2.dom.html.shortcutOf
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.foundation.utils.scrollintoview.HeadlessScrollOptions
import dev.fritz2.headless.foundation.utils.scrollintoview.scrollIntoView
import dev.fritz2.identification.Id
import dev.fritz2.utils.classes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.math.max


@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
class Menu<C : HTMLElement>(tag: Tag<C>, id: String?) : Tag<C> by tag, OpenClose() {

    val componentId: String by lazy { id ?: Id.next() }

    private var button: Tag<HTMLElement>? = null

    private val activeIndex = storeOf(-1)
    private var numberOfItems = 0

    internal data class MenuEntry(val disabled: Boolean, val character: Char?)

    private val items = object : RootStore<List<MenuEntry>>(emptyList()) {
        val addItem = handle<MenuEntry> { old, entry -> old + entry }
        val setCharacter = handle<Pair<Int, Char?>> { old, (index, c) ->
            old.mapIndexed { i, o -> if (i == index) o.copy(character = c) else o }
        }

        fun disabledHandler(index: Int) = handle<Boolean> { state, disabled ->
            state.mapIndexed { idx, value -> if (idx == index) value.copy(disabled = disabled) else value }
        }
    }

    private val state by lazy { activeIndex.data.combine(items.data, ::Pair) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val selections = storeOf(-1)

    fun render() {
        selections.data.drop(1) handledBy selections.handle { _, _ -> -1 }

        opened.filter { !it }.drop(1) handledBy {
            button?.setFocus()
        }
    }

    fun <CB : HTMLElement> RenderContext.menuButton(
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

    fun RenderContext.menuButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLButtonElement>.() -> Unit
    ) = menuButton(classes, scope, RenderContext::button, content).apply {
        attr("type", "button")
    }

    inner class MenuItems<CI : HTMLElement>(
        val renderContext: RenderContext,
        tagFactory: TagFactory<Tag<CI>>,
        classes: String?,
        scope: ScopeContext.() -> Unit
    ) : PopUpPanel<CI>(renderContext, tagFactory, classes, "$componentId-items", scope, this@Menu, button) {

        private fun nextItem(currentIndex: Int, direction: Direction, items: List<MenuEntry>): Int =
            when (direction) {
                Direction.Next -> (items.drop(currentIndex + 1).indexOfFirst { !it.disabled } + currentIndex + 1)
                Direction.Previous -> items.take(max(0, currentIndex)).indexOfLast { !it.disabled }
            }.let { if (it == -1) currentIndex else it }


        private fun firstItem(items: List<MenuEntry>) = items.indexOfFirst { !it.disabled }
        private fun lastItem(items: List<MenuEntry>) = items.indexOfLast { !it.disabled }

        private fun itemByCharacter(entries: List<MenuEntry>, character: Char) =
            entries.indexOfFirst { !it.disabled && it.character == character }.let {
                if (it == -1) null else it
            }

        override fun render() {
            super.render()

            closeOnEscape()
            closeOnBlur()

            attr("role", Aria.Role.menu)

            state.flatMapLatest { (currentIndex, items) ->
                keydowns.mapNotNull { event ->
                    when (shortcutOf(event)) {
                        Keys.ArrowUp -> nextItem(currentIndex, Direction.Previous, items)
                        Keys.ArrowDown -> nextItem(currentIndex, Direction.Next, items)
                        Keys.Home -> firstItem(items)
                        Keys.End -> lastItem(items)
                        else -> null
                    }.also {
                        if (it != null) {
                            event.stopImmediatePropagation()
                            event.preventDefault()
                        }
                    }
                }
            } handledBy activeIndex.update

            items.data.flatMapLatest { items ->
                keydowns
                    .mapNotNull { e -> if (e.key.length == 1) e.key.first().lowercaseChar() else null }
                    .mapNotNull { c ->
                        if (c.isLetterOrDigit()) itemByCharacter(items, c)
                        else null
                    }
            } handledBy activeIndex.update

            state.flatMapLatest { (currentIndex, disabled) ->
                keydowns.filter { setOf(Keys.Enter, Keys.Space).contains(shortcutOf(it)) }.mapNotNull {
                    if (currentIndex == -1 || disabled[currentIndex].disabled) {
                        null
                    } else {
                        it.preventDefault()
                        it.stopImmediatePropagation()
                        currentIndex
                    }
                }
            } handledBy selections.update

            opened.filter { it }.flatMapLatest {
                domNode.scrollTo(0.0, 0.0)
                items.data.map {
                    firstItem(it)
                }
            } handledBy activeIndex.update
        }

        inner class MenuItem<CM : HTMLElement>(tag: Tag<CM>, val index: Int) : Tag<CM> by tag {
            val active = activeIndex.data.map { it == index }
            val selected = selections.data.filter { it == index }.map {}

            val disabled = items.data.map { it[index].disabled }
            val disable = items.disabledHandler(index)

            fun render() {
                mouseenters.mapNotNull { if (items.current[index].disabled) null else index } handledBy activeIndex.update

                attr("tabindex", "-1")
                attrIfNotSet("role", Aria.Role.menuitem)

                active.filter { it } handledBy {
                    scrollIntoView(domNode, HeadlessScrollOptions)
                }

                mousedowns.mapNotNull { e ->
                    e.preventDefault()
                    e.stopImmediatePropagation()
                    if (items.current[index].disabled) null
                    else {
                        index
                    }
                } handledBy selections.update

                selected handledBy close
            }
        }

        fun <CM : HTMLElement> RenderContext.menuItem(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CM>>,
            initialize: MenuItem<CM>.() -> Unit
        ) {
            val index = numberOfItems++
            items.addItem(MenuEntry(false, null))
            tag(this, classes, "$componentId-item-$index", scope) {
                MenuItem(this, index).run {
                    initialize()
                    render()
                }
            }.also {
                items.setCharacter((index to it.domNode.textContent?.trimStart()?.firstOrNull()?.lowercaseChar()))
            }
        }

        fun RenderContext.menuItem(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            initialize: MenuItem<HTMLButtonElement>.() -> Unit
        ) = menuItem(classes, scope, RenderContext::button, initialize)
    }

    fun <CI : HTMLElement> RenderContext.menuItems(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CI>>,
        initialize: MenuItems<CI>.() -> Unit
    ) {
        if (!openClose.isSet) openClose(storeOf(false))
        MenuItems(this, tag, classes, scope).run {
            initialize()
            render()
        }
    }

    fun RenderContext.menuItems(
        classes: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        initialize: MenuItems<HTMLDivElement>.() -> Unit
    ) = menuItems(classes, internalScope, RenderContext::div, initialize)
}


fun <C : HTMLElement> RenderContext.menu(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: Menu<C>.() -> Unit
): Tag<C> = tag(this, classes(classes, "relative"), id, scope) {
    Menu(this, id).run {
        initialize(this)
        render()
    }
}

fun RenderContext.menu(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: Menu<HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = menu(classes, id, scope, RenderContext::div, initialize)
