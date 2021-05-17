package dev.fritz2.components

import dev.fritz2.binding.storeOf
import dev.fritz2.dom.Window
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.style
import dev.fritz2.styling.theme.Theme
import kotlinx.browser.document
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlin.js.Date


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
// TODO: Rework placement (split up into 'placement' and 'justifyContent')
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
            box(baseClass = this@DropdownComponent.containerCss) {

                box {
                    this@DropdownComponent.toggle.value(this)
                    clicks.events.map { true } handledBy this@DropdownComponent.visibilityStore.update
                }

                this@DropdownComponent.visible.values.render { visible ->
                    if (visible) {
                        this@DropdownComponent.renderDropdown(this, styling, baseClass, id, prefix)
                    } else {
                        box { }
                    }
                }
            }
        }
    }

    private fun renderDropdown(
        renderContext: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        val uniqueDropdownId = id ?: "dropdown-${randomId()}"

        renderContext.apply {
            box(
                styling = { this as BoxParams
                    styling()
                    Theme().menu.dropdown()

                    val placement = this@DropdownComponent.placement.value.invoke(PlacementContext)
                    val isVerticalPlacement = (placement == Placement.Top || placement == Placement.Bottom)
                    when(placement) {
                        Placement.Left -> Theme().menu.placements.left
                        Placement.Right -> Theme().menu.placements.right
                        Placement.Top -> Theme().menu.placements.top
                        Placement.Bottom -> Theme().menu.placements.bottom
                    }.invoke()

                    val alignments = Theme().menu.alignments
                    when(this@DropdownComponent.alignment.value.invoke(AlignmentContext)) {
                        Alignment.Start -> if (isVerticalPlacement) alignments.horizontalStart else alignments.verticalStart
                        Alignment.End -> if (isVerticalPlacement) alignments.horizontalEnd else alignments.verticalEnd
                    }.invoke()
                },
                baseClass = baseClass,
                id = uniqueDropdownId,
                prefix = prefix
            ) {
                this@DropdownComponent.content.value(this)
            }
            this@DropdownComponent.listenToWindowEvents(this, uniqueDropdownId)
        }
    }

    private fun listenToWindowEvents(renderContext: RenderContext, dropdownId: String) {
        renderContext.apply {
            // delay listening so the dropdown is not closed immediately:
            val startingTimeMillis = Date.now() + 200

            Window.clicks.events
                .filter { event ->
                    if (Date.now() < startingTimeMillis)
                        return@filter false

                    val dropdownElement = document.getElementById(dropdownId)
                    dropdownElement?.let {
                        val bounds = it.getBoundingClientRect()
                        // Only handle clicks outside of the menu dropdown
                        return@filter !(event.x >= bounds.left
                                && event.x <= bounds.right
                                && event.y >= bounds.top
                                && event.y <= bounds.bottom)
                    }
                    false
                }
                .map { false } handledBy this@DropdownComponent.visibilityStore.update
        }
    }
}

/**
 * Creates a dropdown.
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself.
 */
fun RenderContext.dropdown(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String = "dropdown-${uniqueId()}",
    prefix: String = "dropdown",
    build: DropdownComponent.() -> Unit,
) = DropdownComponent()
    .apply(build)
    .render(this, styling, baseClass, id, prefix)


/**
 * Properties for components that have a dropdown. Mainly used in combination with [DropdownMixin].
 *
 * Adds a `dropdown` context that can be used to specify commonly used dropdown-properties such as placement,
 * visibility etc.
 *
 * Furthermore this interface defines a couple of helper functions that can be used to render a dropdown directly based
 * on the underlying 'dropdown'-property.
 * See [renderDropdown] and [renderWithDropdown] for more information.
 */
interface WithDropdown {
    val dropdown: ComponentProperty<DropdownComponent.() -> Unit>

    /**
     * This method sets up a [DropdownComponent] based on the configuration that has been done via the `dropdown`-
     * property of the component and renders it. Optional overrides can be specified via the [build] parameter.
     */
    fun RenderContext.renderDropdown(build: DropdownComponent.() -> Unit = { }) = DropdownComponent()
        .apply(dropdown.value)
        .apply(build)
        .render(this, { }, StyleClass.None, null, "dropdown")

    /**
     * This method sets up a [DropdownComponent] based on the configuration that has been done via the `dropdown`-
     * property of the component and uses it as a dropdown for the given [component].
     * Optional overrides can be specified via the [build] parameter.
     */
    fun RenderContext.renderWithDropdown(
        build: DropdownComponent.() -> Unit = { },
        component: RenderContext.() -> Unit
    ) = DropdownComponent()
        .apply(dropdown.value)
        .apply(build)
        .apply { toggle { component() } }
        .render(this, { }, StyleClass.None, null, "dropdown")
}

/**
 * Default implementation of [WithDropdown] to be used as a mixin for components.
 */
class DropdownMixin : WithDropdown {
    override val dropdown = ComponentProperty<DropdownComponent.() -> Unit> { }
}