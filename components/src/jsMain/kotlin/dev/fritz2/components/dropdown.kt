package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.watch
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.TextElement
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.section
import dev.fritz2.styling.style
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach


/**
 * This class combines the _configuration_ and the core rendering of a dropdown.
 *
 * A dropdown consists of a toggle element as well as it's actual content in form of any fritz2-component. The
 * `content` property is used to specify the dropdown's content.
 *
 * The dropdown floats around the toggle-element and can be closed by simply clicking outside the dropdown.
 * The opening an closing behavior can manually be controlled as well by specifying the `visible` property. It takes
 * a flow of values that determine whether the dropdown should be visible or not.
 *
 * The toggle-element can be any component as well and is passed via the `toggle` property. A button with a standard
 * menu-icon is used if no toggle-element is specified.
 *
 * The dropdown can be placed _to the left_, _to the right_, on top of, or _below_ the toggle-element.
 * This can be specified via the `placement` property. The default placement is below the toggle. It's alignment on the
 * placement's cross-axis can be specified via the `alignment` property.
 * ```
 */
open class DropdownComponent : Component<Unit> {

    private val containerCss = style("dropdown-container") {
        position(
            sm = { static },
            md = { relative { } }
        )
        display { inlineFlex }
        width { minContent }
    }


    enum class Placement {
        Left,
        Right,
        Top,
        Bottom,
    }

    object PlacementContext {
        val left = Placement.Left
        val right = Placement.Right
        val top = Placement.Top
        val bottom = Placement.Bottom
    }

    enum class Alignment {
        Start,
        End
    }

    object AlignmentContext {
        val start = Alignment.Start
        val end = Alignment.End
    }


    val toggle = ComponentProperty<RenderContext.() -> Unit> {
        pushButton {
            icon { fromTheme { menu } }
            variant { ghost }
        }
    }
    val placement = ComponentProperty<PlacementContext.() -> Placement> { bottom }
    val alignment = ComponentProperty<AlignmentContext.() -> Alignment> { start }
    val content = ComponentProperty<RenderContext.() -> Unit> { }

    // Visibility is controlled by the DropdownComponent itself by default but can manually be controlled via the
    // 'visible' property:
    private val visibilityStore = object : RootStore<Boolean>(false) {
        val toggle = handle { current -> !current }
    }
    val visible = DynamicComponentProperty(visibilityStore.data)


    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            div(this@DropdownComponent.containerCss.name, id) {
                div {
                    this@DropdownComponent.toggle.value(this)
                    clicks.events.map { } handledBy this@DropdownComponent.visibilityStore.toggle
                }

                var dropDown: TextElement? = null
                this@DropdownComponent.visible.values.render { visible ->
                    if (visible) {
                        dropDown = this@DropdownComponent.renderDropdown(this, styling, baseClass, prefix)
                    }
                }
                this@DropdownComponent.visible.values.onEach { visible ->
                    if (visible) { dropDown?.domNode?.focus() }
                }.watch()
            }
        }
    }

    private fun renderDropdown(
        renderContext: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        prefix: String
    ): TextElement {
        return with(renderContext) {
            section(style = {
                    styling()
                    Theme().dropdown.dropdown()

                    val placement = this@DropdownComponent.placement.value.invoke(PlacementContext)
                    val isVerticalPlacement = (placement == Placement.Top || placement == Placement.Bottom)
                    when(placement) {
                        Placement.Left -> Theme().dropdown.placements.left
                        Placement.Right -> Theme().dropdown.placements.right
                        Placement.Top -> Theme().dropdown.placements.top
                        Placement.Bottom -> Theme().dropdown.placements.bottom
                    }.invoke()

                    val alignments = Theme().dropdown.alignments
                    when(this@DropdownComponent.alignment.value.invoke(AlignmentContext)) {
                        Alignment.Start -> if (isVerticalPlacement) alignments.horizontalStart else alignments.verticalStart
                        Alignment.End -> if (isVerticalPlacement) alignments.horizontalEnd else alignments.verticalEnd
                    }.invoke()
                },
                baseClass = baseClass,
                prefix = prefix
            ) {
                attr("tabindex", "-1")
                blurs.events
                    .debounce(100)
                    .map { } handledBy this@DropdownComponent.visibilityStore.toggle

                this@DropdownComponent.content.value(this)
            }
        }
    }
}

/**
 * Creates a dropdown.
 *
 * @param styling a lambda expression for declaring the styling of the actual dropdown as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the dropdown
 * @param id the ID of the dropdown/toggle container
 * @param prefix the prefix for the dropdown's generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself.
 */
fun RenderContext.dropdown(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "dropdown",
    build: DropdownComponent.() -> Unit,
) = DropdownComponent()
    .apply(build)
    .render(this, styling, baseClass, id, prefix)
