package dev.fritz2.components

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.section
import dev.fritz2.styling.style
import dev.fritz2.styling.theme.Theme
import kotlinx.browser.document
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map


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
 * The dropdown can be placed _to the left_, _to the right_ or _below_ the toggle-element. This can be specified via the
 * `placement` property. The default placement is below the toggle.
 * ```
 */
open class DropdownComponent : Component<Unit> {

    private val containerCss = style("menu-container") {
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
    private val visibilityStore = storeOf(false)
    val visible = DynamicComponentProperty(visibilityStore.data)


    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            box(baseClass = this@DropdownComponent.containerCss, id = id) {
                box {
                    this@DropdownComponent.toggle.value(this)
                    clicks.events.map { true } handledBy this@DropdownComponent.visibilityStore.update
                }

                val dropdownId = "dropdown-${randomId()}"
                this@DropdownComponent.visible.values.render { visible ->
                    if (visible) {
                        this@DropdownComponent.renderDropdown(this, styling, baseClass, dropdownId, prefix)
                    } else {
                        box { }
                    }
                }
                this@DropdownComponent.visible.values.render { visible ->
                    if (visible) {
                        try {
                            document.getElementById(dropdownId).asDynamic().focus()
                        } catch (e: Exception) {
                        }
                    }
                }
            }
        }
    }

    private fun renderDropdown(
        renderContext: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String,
        prefix: String
    ) {
        renderContext.apply {
            section(
                style = {
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
                id = id,
                prefix = prefix
            ) {
                attr("tabindex", "-1")
                blurs.events.map { false } handledBy this@DropdownComponent.visibilityStore.update

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
