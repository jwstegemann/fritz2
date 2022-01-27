package dev.fritz2.headless.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.storeOf
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.*
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.foundation.utils.scrollintoview.*
import dev.fritz2.headless.foundation.hook
import dev.fritz2.identification.Id
import dev.fritz2.utils.classes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLElement
import kotlin.math.max

internal val HeadlessScrollOptions = ScrollIntoViewOptionsInit(
    ScrollBehavior.smooth,
    ScrollMode.ifNeeded,
    ScrollPosition.nearest,
    ScrollPosition.nearest
)

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
class HeadlessMenu<C : Tag<HTMLElement>>(val tag: C, id: String?) : RenderContext by tag,
    OpenClose by OpenCloseDelegate() {

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

    fun C.render() {
        selections.syncBy(selections.handle { -1 })

        opened.filter { !it }.drop(1) handledBy {
            button?.setFocus()
        }
    }

    fun <CB : Tag<HTMLElement>> RenderContext.menuButton(
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

    fun RenderContext.menuButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Button.() -> Unit
    ) = menuButton(classes, scope, RenderContext::button, content).apply {
        attr("type", "button")
    }

    inner class MenuItems<CI : Tag<HTMLElement>>(
        val renderContext: RenderContext,
        tagFactory: TagFactory<CI>,
        classes: String?,
        scope: ScopeContext.() -> Unit
    ) : PopUpPanel<CI>(renderContext, tagFactory, classes, "$componentId-items", scope, this@HeadlessMenu, button) {

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

            tag.apply {
                attr("tabindex", "0")
                attr("role", Aria.Role.menu)

                state.flatMapLatest { (currentIndex, items) ->
                    keydowns.events.mapNotNull { event ->
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
                    keydowns.events
                        .mapNotNull { e -> if (e.key.length == 1) e.key.first().lowercaseChar() else null }
                        .mapNotNull { c ->
                            if (c.isLetterOrDigit()) itemByCharacter(items, c)
                            else null
                        }
                } handledBy activeIndex.update

                state.flatMapLatest { (currentIndex, disabled) ->
                    keydowns.events.filter { setOf(Keys.Enter, Keys.Space).contains(shortcutOf(it)) }.mapNotNull {
                        if (currentIndex == -1 || disabled[currentIndex].disabled) {
                            null
                        } else {
                            it.preventDefault()
                            it.stopImmediatePropagation()
                            currentIndex
                        }
                    }
                } handledBy selections.update
            }

            opened.filter { it }.flatMapLatest {
                tag.domNode.scrollTo(0.0, 0.0)
                items.data.map {
                    firstItem(it)
                }
            } handledBy activeIndex.update
        }

        inner class MenuItem<CM : Tag<HTMLElement>>(val tag: CM, val index: Int) {
            val active = activeIndex.data.map { it == index }
            val selected = selections.data.filter { it == index }.map {}

            val disabled = items.data.map { it[index].disabled }
            val disable = items.disabledHandler(index)

            fun CM.render() {
                mouseenters.events.mapNotNull { if (items.current[index].disabled) null else index } handledBy activeIndex.update

                attr("tabindex", "-1")
                attrIfNotSet("role", Aria.Role.menuitem)

                active.filter { it } handledBy {
                    scrollIntoView(domNode, HeadlessScrollOptions)
                }

                mousedowns.events.mapNotNull { e ->
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

        fun <CM : Tag<HTMLElement>> RenderContext.menuItem(
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<CM>,
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
            initialize: MenuItem<Button>.() -> Unit
        ) = menuItem(classes, scope, RenderContext::button, initialize)
    }

    fun <CI : Tag<HTMLElement>> RenderContext.menuItems(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<CI>,
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
        initialize: MenuItems<Div>.() -> Unit
    ) = menuItems(classes, internalScope, RenderContext::div, initialize)
}


fun <C : Tag<HTMLElement>> RenderContext.headlessMenu(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<C>,
    initialize: HeadlessMenu<C>.() -> Unit
): C = tag(this, classes(classes, "relative"), id, scope) {
    HeadlessMenu(this, id).run {
        initialize(this)
        render()
    }
}

fun RenderContext.headlessMenu(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: HeadlessMenu<Div>.() -> Unit
): Div = headlessMenu(classes, id, scope, RenderContext::div, initialize)

