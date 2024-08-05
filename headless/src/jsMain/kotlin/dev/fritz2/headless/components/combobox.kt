package dev.fritz2.headless.components

import dev.fritz2.core.*
import dev.fritz2.headless.foundation.*
import dev.fritz2.headless.foundation.utils.floatingui.utils.PlacementValues
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

class Combobox<T, E : HTMLElement>(tag: Tag<E>, id: String?) : Tag<E> by tag, OpenClose() {

    // TODO: Add structure hints
    private val componentId: String by lazy { id ?: Id.next() }


    val value: DatabindingProperty<T> = DatabindingProperty()

    val valueFormat: (T) -> String = { it.toString() }


    private data class RenderableItem<T>(
        val item: T,
        val index: Int,
        private val renderFunction: RenderContext.(RenderableItem<T>, String) -> Tag<HTMLElement>
    ) {
        fun RenderContext.render(query: String) = renderFunction(this, this@RenderableItem, query)
    }

    private var itemIndexCounter = 0
    private val items = mutableListOf<RenderableItem<T>>()


    private var input: Tag<HTMLInputElement>? = null


    private data class State<T>(
        val query: String,
        val items: List<RenderableItem<T>>,
        val opened: Boolean
    )

    private val internalState = object : RootStore<State<T>>(
        State(query = "", items = emptyList(), opened = false),
        job
    ) {
        val setOpened: Handler<Boolean> = handle { current, opened ->
            current.copy(
                opened = opened,
                items = current.items.takeIf { current.query.isNotEmpty() } ?: this@Combobox.items
            )
        }


        private fun filter(items: List<RenderableItem<T>>, query: String): List<RenderableItem<T>> =
            items.filter { item -> valueFormat(item.item).contains(query, ignoreCase = true) }

        val query: Handler<String> = handle { current, query ->
            val filtered = when {
                query.startsWith(current.query) -> filter(current.items, query)
                current.query.startsWith(query) -> filter(items, query)
                else -> items
            }
            current.copy(query = query, items = filtered)
        }


        val select: EmittingHandler<RenderableItem<T>, T> = handleAndEmit { _, selection ->
            State(
                query = valueFormat(selection.item),
                items = listOf(selection),
                opened = false
            ).also {
                emit(selection.item)
            }
        }
    }


    init {
        openState(storeOf(false))
    }


    fun <EB : HTMLInputElement> comboboxInput(
        classes: String? = null,
        scope: ScopeContext.() -> Unit = {},
        tag: TagFactory<Tag<EB>>,
        initialize: Tag<EB>.() -> Unit
    ) {
        tag(this, classes, "$componentId-input", scope) {
            value(internalState.data.map { it.query })
            focusins handledBy open
            inputs.values() handledBy internalState.query
            initialize()
        }.also {
            input = it
        }
    }


    inner class ComboboxItems<EI : HTMLElement>(
        renderContext: RenderContext,
        tagFactory: TagFactory<Tag<EI>>,
        classes: String?,
        scope: ScopeContext.() -> Unit,
    ) : PopUpPanel<EI>(
        renderContext,
        tagFactory,
        classes,
        id = "${componentId}-items",
        scope,
        this@Combobox.opened,
        reference = this@Combobox.input,
        ariaHasPopup = Aria.HasPopup.listbox
    ) {
        inner class ComboboxItem<EJ : HTMLElement>(
            tag: Tag<EJ>,
            val query: String
        ) : Tag<EJ> by tag

        fun <EJ : HTMLElement> comboboxItem(
            classes: String? = null,
            scope: ScopeContext.() -> Unit = {},
            tag: TagFactory<Tag<EJ>>,
            item: T,
            initialize: ComboboxItem<EJ>.() -> Unit
        ) {
            val index = itemIndexCounter++
            items += RenderableItem(item, index) { self, query ->
                tag(this, classes, "$componentId-item-$index", scope) {
                    with(ComboboxItem(this, query)) {
                        clicks.map { self } handledBy internalState.select
                        initialize()
                    }
                }
            }
        }

        override fun render() {
            super.render()
            internalState.data.render { (query, items, _) ->
                items.forEach { item ->
                    with(item) {
                        render(query)
                    }
                }
            }
        }
    }

    fun <EI : HTMLElement> comboboxItems(
        classes: String? = null,
        scope: ScopeContext.() -> Unit = {},
        tag: TagFactory<Tag<EI>>,
        initialize: ComboboxItems<EI>.() -> Unit
    ) {
        portal {
            ComboboxItems(this, tag, classes, scope).run {
                size = PopUpPanelSize.Min
                // TODO: Make placement configurable
                placement = PlacementValues.bottomStart
                initialize()
                render()
            }
        }
    }


    fun render() {
        if (!value.isSet) {
            warnAboutMissingDatabinding(propertyName = "value", componentName = "combobox", componentId, domNode)
        }

        opened handledBy internalState.setOpened
        openState.handler?.invoke(this, internalState.data.map { it.opened })

        value.data.map { valueFormat(it) } handledBy internalState.query
        value.handler?.invoke(this, internalState.select)
    }
}

fun <T, E : HTMLElement> RenderContext.combobox(
    classes: String? = null,
    id: String? = null,
    scope: ScopeContext.() -> Unit = {},
    tag: TagFactory<Tag<E>>,
    initialize: Combobox<T, E>.() -> Unit
) {
    tag(this, classes, id, scope) {
        with(Combobox<T, E>(this, id)) {
            initialize()
            render()
        }
    }
}