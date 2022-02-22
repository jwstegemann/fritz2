package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.foundation.utils.scrollintoview.HeadlessScrollOptions
import dev.fritz2.headless.foundation.utils.scrollintoview.scrollIntoView
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLLabelElement
import kotlin.math.max

/**
 * This class provides the building blocks to implement a listbox.
 *
 * Use [listbox] functions to create an instance, set up the needed [Hook]s or [Property]s and refine the
 * component by using the further factory methods offered by this class.
 *
 * For more information refer to the [official documentation](https://docs.fritz2.dev/headless/listbox/)
 */
@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
class Listbox<T, C : HTMLElement>(tag: Tag<C>, id: String?) : Tag<C> by tag, OpenClose() {

    val value = DatabindingProperty<T>()
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

    fun render() {
        attr("id", componentId)

        opened.drop(1).filter { !it } handledBy {
            button?.setFocus()
        }
    }

    /**
     * Factory function to create a [listboxButton].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/listbox/#listboxbutton)
     */
    fun <CB : HTMLElement> RenderContext.listboxButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CB>>,
        content: Tag<CB>.() -> Unit
    ) = tag(this, classes, "$componentId-button", scope) {
        if (!openState.isSet) openState(storeOf(false))
        content()
        attr(Aria.expanded, opened.asString())
        toggleOnClicksEnterAndSpace()
    }.also { button = it }

    /**
     * Factory function to create a [listboxButton] with a [HTMLButtonElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/listbox/#listboxbutton)
     */
    fun RenderContext.listboxButton(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLButtonElement>.() -> Unit
    ) = listboxButton(classes, scope, RenderContext::button, content).apply {
        attr("type", "button")
    }

    /**
     * Factory function to create a [listboxLabel].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/listbox/#listboxlabel)
     */
    fun <CL : HTMLElement> RenderContext.listboxLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CL>>,
        content: Tag<CL>.() -> Unit
    ) = tag(this, classes, "$componentId-label", scope, content).also { label = it }

    /**
     * Factory function to create a [listboxLabel] with a [HTMLLabelElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/listbox/#listboxlabel)
     */
    fun RenderContext.listboxLabel(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        content: Tag<HTMLLabelElement>.() -> Unit
    ) = listboxLabel(classes, scope, RenderContext::label, content)

    /**
     * Factory function to create a [listboxValidationMessages].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/listbox/#listboxvalidationmessages)
     */
    fun <CV : HTMLElement> RenderContext.listboxValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CV>>,
        initialize: ValidationMessages<CV>.() -> Unit
    ) {
        value.validationMessages.map { it.isNotEmpty() }.distinctUntilChanged().render { isNotEmpty ->
            if(isNotEmpty) {
                tag(this, classes, "$componentId-${ValidationMessages.ID_SUFFIX}", scope) {
                    validationMessages = this
                    initialize(ValidationMessages(value.validationMessages, this))
                }
            }
        }
    }

    /**
     * Factory function to create a [listboxValidationMessages] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/listbox/#listboxvalidationmessages)
     */
    fun RenderContext.listboxValidationMessages(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        initialize: ValidationMessages<HTMLDivElement>.() -> Unit
    ) = listboxValidationMessages(classes, scope, RenderContext::div, initialize)

    inner class ListboxItems<CI : HTMLElement>(
        val renderContext: RenderContext,
        tagFactory: TagFactory<Tag<CI>>,
        classes: String?,
        scope: ScopeContext.() -> Unit
    ) : PopUpPanel<CI>(renderContext, tagFactory, classes, "$componentId-items", scope, this@Listbox, button) {

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
            trapFocus(restoreFocus = false)

            closeOnEscape()
            closeOnBlur()

            attrIfNotSet("tabindex", "0")
            attr("role", Aria.Role.listbox)
            attr(Aria.invalid, "true".whenever(value.hasError))
            label?.let { attr(Aria.labelledby, it.id) }
            attr(Aria.activedescendant, activeIndex.data.map { if (it == -1) null else "$componentId-item-$it" })

            state.flatMapLatest { (currentIndex, entries) ->
                keydowns.mapNotNull { event ->
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
                keydowns.mapNotNull { event ->
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
                    keydowns.filter {
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

            opened.filter { it }.flatMapLatest {
                setFocus()
                value.data.flatMapLatest { current ->
                    entries.data.map { entries ->
                        val selectedIndex = entries.indexOfFirst { it.value == current }
                        if (selectedIndex == -1) firstItem(entries) else selectedIndex
                    }
                }
            } handledBy activeIndex.update
        }

        inner class ListboxItem<CM : HTMLElement>(val entry: T, tag: Tag<CM>, val index: Int) : Tag<CM> by tag {
            val active = activeIndex.data.map { it == index }
            val selected = value.data.map { it == entry }

            // no value should appear when list is still empty
            val disabled = entries.data.mapNotNull { it.getOrNull(index)?.disabled }
            val disable by lazy { entries.disabledHandler(index) }

            fun render() {
                mouseenters.mapNotNull { if (entries.current[index].disabled) null else index } handledBy activeIndex.update

                attr("tabindex", "-1")
                attr("role", Aria.Role.option)

                active.filter { it } handledBy {
                    scrollIntoView(domNode, HeadlessScrollOptions)
                }

                value.handler?.invoke(
                    mousedowns.mapNotNull { e ->
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

        /**
         * Factory function to create a [listboxItem].
         *
         * For more information refer to the
         * [official documentation](https://docs.fritz2.dev/headless/listbox/#listboxitem)
         */
        fun <CM : HTMLElement> RenderContext.listboxItem(
            entry: T,
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            tag: TagFactory<Tag<CM>>,
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

        /**
         * Factory function to create a [listboxItem] with a [HTMLButtonElement] as default [Tag].
         *
         * For more information refer to the
         * [official documentation](https://docs.fritz2.dev/headless/listbox/#listboxitem)
         */
        fun RenderContext.listboxItem(
            entry: T,
            classes: String? = null,
            scope: (ScopeContext.() -> Unit) = {},
            initialize: ListboxItem<HTMLButtonElement>.() -> Unit
        ) = listboxItem(entry, classes, scope, RenderContext::button, initialize)
    }

    /**
     * Factory function to create a [listboxItems].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/listbox/#listboxitems)
     */
    fun <CI : HTMLElement> RenderContext.listboxItems(
        classes: String? = null,
        scope: (ScopeContext.() -> Unit) = {},
        tag: TagFactory<Tag<CI>>,
        initialize: ListboxItems<CI>.() -> Unit
    ) {
        if (!openState.isSet) openState(storeOf(false))
        ListboxItems(this, tag, classes, scope).run {
            initialize()
            render()
        }
    }

    /**
     * Factory function to create a [listboxItems] with a [HTMLDivElement] as default [Tag].
     *
     * For more information refer to the
     * [official documentation](https://docs.fritz2.dev/headless/listbox/#listboxitems)
     */
    fun RenderContext.listboxItems(
        classes: String? = null,
        internalScope: (ScopeContext.() -> Unit) = {},
        initialize: ListboxItems<HTMLDivElement>.() -> Unit
    ) = listboxItems(classes, internalScope, RenderContext::div, initialize)
}

/**
 * Factory function to create a [Listbox].
 *
 * API-Sketch:
 * ```kotlin
 * listbox<T>() {
 *     val value: DatabindingProperty<T>
 *     // inherited by `OpenClose`
 *     val openClose = DatabindingProperty<Boolean>()
 *     val opened: Flow<Boolean>
 *     val close: SimpleHandler<Unit>
 *     val open: SimpleHandler<Unit>
 *     val toggle: SimpleHandler<Unit>
 *
 *     listboxButton() { }
 *     listboxLabel() { }
 *     listboxValidationMessages() {
 *         val msgs: Flow<List<ComponentValidationMessage>>
 *     }
 *     listboxItems() {
 *         // inherited by `PopUpPanel`
 *         var placement: Placement
 *         var strategy: Strategy
 *         var flip: Boolean
 *         var skidding: Int
 *         var distance: int
 *
 *         // for each T {
 *             listboxItem(entry: T) {
 *                 val index: Int
 *                 val selected: Flow<Boolean>
 *                 val active: Flow<Boolean>
 *                 val disabled: Flow<Boolean>
 *                 val disable: SimpleHandler<Boolean>
 *             }
 *         // }
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://docs.fritz2.dev/headless/listbox/#listbox)
 */
fun <T, C : HTMLElement> RenderContext.listbox(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    tag: TagFactory<Tag<C>>,
    initialize: Listbox<T, C>.() -> Unit
): Tag<C> = tag(this, classes(classes, "relative"), id, scope) {
    Listbox<T, C>(this, id).run {
        initialize(this)
        render()
    }
}

/**
 * Factory function to create a [Listbox] with a [HTMLDivElement] as default root [Tag].
 *
 * API-Sketch:
 * ```kotlin
 * listbox<T>() {
 *     val value: DatabindingProperty<T>
 *     // inherited by `OpenClose`
 *     val openClose = DatabindingProperty<Boolean>()
 *     val opened: Flow<Boolean>
 *     val close: SimpleHandler<Unit>
 *     val open: SimpleHandler<Unit>
 *     val toggle: SimpleHandler<Unit>
 *
 *     listboxButton() { }
 *     listboxLabel() { }
 *     listboxValidationMessages() {
 *         val msgs: Flow<List<ComponentValidationMessage>>
 *     }
 *     listboxItems() {
 *         // inherited by `PopUpPanel`
 *         var placement: Placement
 *         var strategy: Strategy
 *         var flip: Boolean
 *         var skidding: Int
 *         var distance: int
 *
 *         // for each T {
 *             listboxItem(entry: T) {
 *                 val index: Int
 *                 val selected: Flow<Boolean>
 *                 val active: Flow<Boolean>
 *                 val disabled: Flow<Boolean>
 *                 val disable: SimpleHandler<Boolean>
 *             }
 *         // }
 *     }
 * }
 * ```
 *
 * For more information refer to the [official documentation](https://docs.fritz2.dev/headless/listbox/#listbox)
 */
fun <T> RenderContext.listbox(
    classes: String? = null,
    id: String? = null,
    scope: (ScopeContext.() -> Unit) = {},
    initialize: Listbox<T, HTMLDivElement>.() -> Unit
): Tag<HTMLDivElement> = listbox(classes, id, scope, RenderContext::div, initialize)
