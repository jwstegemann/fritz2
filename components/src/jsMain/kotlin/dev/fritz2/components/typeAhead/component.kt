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


/**
 * This type alias defines the type of the proposal function of a [TypeAheadComponent]
 *
 * The typical pattern of writing such a function is to start with the implicit `it` and use common [Flow] operators
 * on it (pun intended):
 * ```
 * // just map the draft to a list:
 * val proposal: Proposal = {
 *     it.map { draft -> /* ... some API call or alike */ }
 * }
 *
 * // combine the draft with some other Flow (controlled by some other component)
 * val languages = storeOf(listOf("Kotlin", "Scala", "Java", "OCaml", "Haskell"))
 * val proposal: Proposal = {
 *     it.combine(languages.data) { draft, languages -> /* check if draft is contained or alike */ }
 * }
 *
 * ```
 */
typealias Proposal = suspend (Flow<String>) -> Flow<List<String>>

/**
 * This extension function offers a short, convenience method to use a static [List] as [Proposal].
 *
 * As default behaviour it checks, whether the current draft is contained in any item of the list ignoring case
 * sensitivity:
 * ```
 * val languages = listOf("Kotlin", "Scala", "Java", "OCaml", "Haskell")
 * val proposal = languages.asProposal() // called with draft = "ca" -> ["Scala", "OCaml"]
 *                                       //                                ^^       ^^
 * ```
 *
 * A predicate factory for the filter function can be passed as optional parameter:
 * ```
 * // just take only items from the list, that starts with draft:
 * languages.asProposal { draft -> { item -> item.startsWith(draft) } }
 *
 * @param predicate a factory for a predicate function applied to the internal filter; the current draft is the
 *                  outer parameter, the inner one is the current element of the list.
 * ```
 */
fun List<String>.asProposal(
    predicate: (String) -> (String) -> Boolean = { draft ->
        { item ->
            item.contains(
                draft,
                ignoreCase = true
            )
        }
    }
): Proposal = {
    it.map { draft -> this.filter(predicate(draft)) }
}

/**
 * Some internal abbreviation for the transformation of the current draft to an exposed result.
 * This is used for the two options offered by [TypeAheadComponent.strict]:
 * - `true` -> only return a value, if draft matches exactly one item of [TypeAheadComponent.items]
 * - `false` -> just return the draft as it is
 *
 * @see StateStore
 * @see TypeAheadComponent
 */
internal typealias Accepted = (List<String>, String) -> String

/**
 * The fitting state type for the internal state handling of the [TypeAheadComponent] used by [StateStore].
 */
internal data class State(
    val draft: String = "",
    val selected: String = "",
    val proposals: List<String> = emptyList()
)

/**
 * The store for managing the state of [TypeAheadComponent].
 *
 * It offers specific data [Flow]s for the current [draft], the [selected] item and the current valid [proposals]
 * excluding the current draft if it would be part of.
 *
 * It also offers a function to [preselect] some item initially.
 */
internal class StateStore(private val propose: Proposal, accepted: Accepted) : RootStore<State>(State()) {
    val draft = data.map { it.draft }
    val selected = data.map { it.selected }
    val proposals = data.map { it.proposals.filter { proposal -> proposal != it.draft } }

    /**
     * This flag is used to enable the preselection possibility just for exactly one time at first rendering of
     * the [TypeAheadComponent]. This is important to break the circuit between external store and current draft!
     * Without this safety belt, the draft would be deleted completely immediately after dropping the last char
     * of the draft for example:
     * ```
     * // during some later point in time:
     * (1) draft: Germany -> selected: Germany -> external Store: Germany
     * (2) user presses `Backspace`
     * (3) draft: German -> selected: <empty> -> external Store: <empty> -{preselect}-> draft: <empty>
     * //         ^^^^^^                                                 ^^^^^^^^^^^^^^
     * //         we want to keep this!                                  this must be prohibited!
     * //
     * // with unsecured preselect <empty> would be passed as draft by the external store to the internal state store!
     * ```
     * Have a look at [TypeAheadComponent.render] to see how [preselect] is getting called!
     */
    private var preselectEnabled = true

    val preselect = handle<String> { state, draft ->
        if (preselectEnabled) {
            preselectEnabled = false
            val proposals = propose(flowOf(draft)).first()
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
open class TypeAheadComponent(protected val valueStore: Store<String>?, protected val items: Proposal) :
    Component<Unit>,
    InputFormProperties by InputFormMixin(),
    SeverityProperties by SeverityMixin() {

    val value = DynamicComponentProperty(flowOf(""))
    val strict = ComponentProperty(true)
    val debounce = ComponentProperty(250L)

    // TODO: Check if there is a nicer way to expose those input specific properties!
    val variant = ComponentProperty<InputFieldVariants.() -> Style<BasicParams>> { Theme().input.variants.outline }
    val size = ComponentProperty<FormSizes.() -> Style<BasicParams>> { Theme().input.sizes.normal }
    val placeholder = DynamicComponentProperty(flowOf(""))

    class EventsContext<String>(private val element: RenderContext, val value: Flow<String>) :
        EventContext<HTMLElement> by element

    val events = ComponentProperty<EventsContext<String>.() -> Unit> {}

    private val acceptOnlyProposals: Accepted = { proposals, value ->
        if (proposals.contains(value)) value else ""
    }

    private val acceptDraft: Accepted = { _, value -> value }

    private val draft: MutableStateFlow<String> = MutableStateFlow("")

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        val accepted = if (strict.value) acceptOnlyProposals else acceptDraft
        val internalStore = StateStore(items, accepted)
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
                        .onEach { this@TypeAheadComponent.draft.value = domNode.value }
                        .flatMapLatest { this@TypeAheadComponent.items(this@TypeAheadComponent.draft) }
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
