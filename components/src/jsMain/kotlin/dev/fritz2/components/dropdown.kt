package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.watch
import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.div
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.section
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import org.w3c.dom.HTMLElement


/**
 * This class combines the _configuration_ and rendering of a dropdown.
 *
 * A dropdown consists of a toggle element as well as it's actual content in form of any fritz2-component. The
 * `toggle` property can be used to specify a non-default toggle and the `content` property is used to specify the
 * dropdown's content.
 * A button with a standard menu-icon is used if no other toggle-element is specified.
 *
 * The dropdown floats around the toggle-element and can be closed by simply clicking outside the dropdown.
 * The opening an closing behavior can manually be controlled as well by specifying the `visible` property. It takes
 * a flow of values to determine whether the dropdown should be visible or not.
 *
 * The dropdown can be placed _to the left_, _to the right_, on top of, or _below_ the toggle-element. Additionally it
 * can either be aligned to the start or end of the placement's cross-axis.
 * This can be specified via the respective `placement` and `alignment` properties.
 * The default positioning is bottom start.
 *
 * Example usage:
 * ```kotlin
 * dropdown {
 *     toggle {
 *         // some layout
 *     }
 *     placement { bottom }
 *     alignment { start }
 *     content {
 *         // some layout
 *     }
 * }
 * ```
 */
open class DropdownComponent : Component<Unit> {

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
            icon { menu }
            variant { ghost }
        }
    }
    val placement = ComponentProperty<PlacementContext.() -> Placement> { bottom }
    val alignment = ComponentProperty<AlignmentContext.() -> Alignment> { start }
    val content = ComponentProperty<RenderContext.() -> Unit> { }

    private val visible = object : RootStore<Boolean>(false) {
        val toggle = handle { !it }
    }

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            div(Theme().dropdown.container, id = id) {
                div {
                    this@DropdownComponent.toggle.value(this)
                    clicks handledBy this@DropdownComponent.visible.toggle
                }

                lateinit var dropDown: HTMLElement
                this@DropdownComponent.visible.data.render { visible ->
                    if (visible) {
                        dropDown = this@DropdownComponent.renderDropdown(
                            this,
                            styling,
                            baseClass,
                            prefix
                        )
                    }
                }
                this@DropdownComponent.visible.data.onEach { visible ->
                    if (visible) dropDown.focus()
                }.watch()
            }
        }
    }

    private fun renderDropdown(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        prefix: String
    ): HTMLElement = with(context) {
        section(
            {
                Theme().dropdown.dropdown()
                val placement = this@DropdownComponent.placement.value.invoke(PlacementContext)
                val isVerticalPlacement = (placement == Placement.Top || placement == Placement.Bottom)
                when (placement) {
                    Placement.Left -> Theme().dropdown.placements.left
                    Placement.Right -> Theme().dropdown.placements.right
                    Placement.Top -> Theme().dropdown.placements.top
                    Placement.Bottom -> Theme().dropdown.placements.bottom
                }.invoke()

                val alignments = Theme().dropdown.alignments
                when (this@DropdownComponent.alignment.value.invoke(AlignmentContext)) {
                    Alignment.Start -> if (isVerticalPlacement) alignments.horizontalStart else alignments.verticalStart
                    Alignment.End -> if (isVerticalPlacement) alignments.horizontalEnd else alignments.verticalEnd
                }.invoke()
            },
            styling,
            baseClass,
            prefix = prefix
        ) {
            attr("tabindex", "-1")
            blurs.map{}.debounce(100) handledBy this@DropdownComponent.visible.toggle

            this@DropdownComponent.content.value(this)
        }
    }.domNode

}

/**
 * Creates a dropdown component.
 *
 * @see DropdownComponent
 * @param styling a lambda expression for declaring the styling of the actual dropdown as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of element
 * @param prefix the prefix for the generated CSS class resulting in the form `$prefix-$hash`
 * @param build a lambda expression for setting up the component itself
 */
fun RenderContext.dropdown(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "dropdown",
    build: DropdownComponent.() -> Unit,
) = DropdownComponent().apply(build).render(this, styling, baseClass, id, prefix)
