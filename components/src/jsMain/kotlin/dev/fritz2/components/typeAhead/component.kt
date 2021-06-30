package dev.fritz2.components.typeAhead

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.Store
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
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLElement


internal typealias Proposal = (String) -> Flow<List<String>>
internal typealias Accepted = (List<String>, String) -> String

internal data class State(
    val draft: String = "",
    val selected: String = "",
    val proposals: List<String> = emptyList()
)

internal class StateStore(private val propose: Proposal, accepted: Accepted) : RootStore<State>(State()) {
    val draft = data.map { it.draft }
    val selected = data.map { it.selected }
    val proposals = data.map { it.proposals.filter { proposal -> proposal != it.draft } }

    private var preselectEnabled = true

    val preselect = handle<String> { state, draft ->
        if (preselectEnabled) {
            preselectEnabled = false
            val proposals = propose(draft).first()
            State(draft, selected = accepted(proposals, draft), proposals = proposals)
        } else state
    }

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

    private fun acceptOnlyProposals(proposals: List<String>, value: String) =
        if (proposals.contains(value)) value else ""

    // TODO: Howto drop warning "unused param" here? (Signature *must* remain imho!)
    private fun acceptDraft(proposals: List<String>, value: String) = value

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        val accepted = if (strict.value) ::acceptOnlyProposals else ::acceptDraft
        val internalStore = StateStore(propose.value, accepted)
        val proposalsId = "proposals-{${uniqueId()}}"

        context.apply {
            (this@TypeAheadComponent.valueStore?.data
                ?: this@TypeAheadComponent.value.values) handledBy internalStore.preselect

            inputField(
                {
                    this as BoxParams
                    styling(this)
                },
                baseClass = baseClass, id = id, prefix = prefix
            ) {
                variant { this@TypeAheadComponent.variant.value(Theme().input.variants) }
                size { this@TypeAheadComponent.size.value(Theme().input.sizes) }
                placeholder(this@TypeAheadComponent.placeholder.values)
                element {
                    attr("list", proposalsId)
                    autocomplete("off") // needed for FF
                }
                value(internalStore.draft)
                events {
                    inputs.events.debounce(this@TypeAheadComponent.debounce.value)
                        .flatMapLatest { this@TypeAheadComponent.propose.value(domNode.value) }
                        .map { proposals ->
                            with(domNode.value) {
                                State(this, accepted(proposals, this), proposals)
                            }
                        } handledBy internalStore.update

                    blurs.events.map {
                        with(internalStore.current) { copy(draft = this.selected.ifBlank { "" }) }
                    } handledBy internalStore.update
                }
            }
            datalist(id = proposalsId) {
                internalStore.proposals.renderEach {
                    option {
                        attr("value", it)
                    }
                }
            }

            EventsContext(this, internalStore.selected).apply {
                this@TypeAheadComponent.events.value(this)
                this@TypeAheadComponent.valueStore?.let { value handledBy it.update }
            }
        }
    }
}
