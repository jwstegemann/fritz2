package dev.fritz2.components.typeAhead

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.Store
import dev.fritz2.binding.storeOf
import dev.fritz2.components.*
import dev.fritz2.dom.EventContext
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.theme.FormSizes
import dev.fritz2.styling.theme.InputFieldVariants
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLElement


typealias Proposal = (String) -> Flow<List<String>>

class DraftStore(private val propose: Proposal, timeout: Long) : RootStore<String>("") {
    val proposals = data
        .debounce(timeout)
        .flatMapLatest { propose(it) }
        .stateIn(MainScope(), SharingStarted.Lazily, emptyList())

    fun acceptOnlyProposals(value: String) = if (isProposal(value)) value else ""
    fun acceptDraft(value: String) = value

    private fun isProposal(draft: String) = proposals.value.contains(draft)
}

/**
 * This component class manages the configuration of a [formGroup] and does the rendering.
 *
 * For details of the usage see its factory function [formGroup].
 *
 * @see formGroup
 */
open class TypeAheadComponent(protected val valueStore: Store<String>?) :
    Component<Unit>,
    InputFormProperties by InputFormMixin(),
    SeverityProperties by SeverityMixin() {

    val value = DynamicComponentProperty(flowOf(""))
    val propose = ComponentProperty<Proposal> { draft -> flowOf(listOf(draft)) }
    val strict = ComponentProperty(true)
    val debounce = ComponentProperty(250L)

    // TODO: Check if there is a nicer way to expose those input specific properties!
    val variant = ComponentProperty<InputFieldVariants.() -> Style<BasicParams>> { Theme().input.variants.outline }
    val size = ComponentProperty<FormSizes.() -> Style<BasicParams>> { Theme().input.sizes.normal }
    val placeholder = DynamicComponentProperty(flowOf(""))

    class EventsContext<String>(private val element: RenderContext, val value: Flow<String>) :
        EventContext<HTMLElement> by element

    val events = ComponentProperty<EventsContext<String>.() -> Unit> {}

    protected val result = storeOf("")

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        val draft = DraftStore(propose.value, debounce.value)
        val proposalsId = "proposals-{${uniqueId()}}"
        val resultPicker = if (strict.value) draft::acceptOnlyProposals else draft::acceptDraft

        context.apply {
            (this@TypeAheadComponent.valueStore?.data
                ?: this@TypeAheadComponent.value.values) handledBy this@TypeAheadComponent.result.update
            // TODO: How do take first draft from value flow too if store is null?
            this@TypeAheadComponent.valueStore?.current?.let { draft.update(it) }

            inputField(
                {
                    this as BoxParams
                    styling(this)
                },
                value = draft, baseClass, id, prefix
            ) {
                variant { this@TypeAheadComponent.variant.value(Theme().input.variants) }
                size { this@TypeAheadComponent.size.value(Theme().input.sizes) }
                placeholder(this@TypeAheadComponent.placeholder.values)
                element {
                    attr("list", proposalsId)
                    autocomplete("off") // needed for FF
                }
                events {
                    inputs.events.map { domNode.value } handledBy draft.update
                    // TODO: Extract this for better doccumenting!
                    //  e.g. give examples with states!
                    blurs.events.combine(this@TypeAheadComponent.result.data) { _, result -> result }.map {
                        it.ifBlank { "" }
                    } handledBy draft.update
                    // must be called *after* blurs!!! -> changing draft after valid result shall not clean draft!
                    inputs.events.map { resultPicker(domNode.value) } handledBy this@TypeAheadComponent.result.update
                }
            }
            datalist(id = proposalsId) {
                draft.proposals.renderEach {
                    option {
                        attr("value", it)
                    }
                }
            }

            EventsContext(this, this@TypeAheadComponent.result.data).apply {
                this@TypeAheadComponent.events.value(this)
                this@TypeAheadComponent.valueStore?.let { value handledBy it.update }
            }
        }
    }
}
