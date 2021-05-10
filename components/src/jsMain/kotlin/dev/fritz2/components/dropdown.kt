package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.style
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlin.js.Date


/**
 * This class combines the _configuration_ and the core rendering of a dropdown.
 *
 * A menu consists of a toggle element as well as it's actual content in form of a drop-down with customizable entries.
 * The dropdown floats around the toggle-element and can be closed by simply clicking outside the dropdown.
 *
 * The toggle-element can be any component and is passed via the `toggle` property. A button with a standard
 * menu-icon is used if no toggle-element is specified.
 *
 * The dropdown can be placed _to the left_, _to the right_ or _below_ the toggle-element. This can be specified via the
 * `placement` property. The default placement is below the toggle.
 * ```
 */
// TODO: Adjust documentation
open class DropdownComponent : Component<Unit> {

    private val containerCss = style("menu-container") {
        position(
            sm = { static },
            md = { relative { } }
        )
        display { inlineFlex }
        width { minContent }
    }


    enum class DropdownPlacement {
        Left,
        Right,
        BottomLeftFacing,
        BottomRightFacing
    }

    object DropdownPlacementContext {
        val left = DropdownPlacement.Left
        val right = DropdownPlacement.Right
        val bottomLeftFacing = DropdownPlacement.BottomLeftFacing
        val bottomRightFacing = DropdownPlacement.BottomRightFacing
    }


    private val visibilityStore = object : RootStore<Boolean>(false) {
        val show = handle { true }
        val dismiss = handle { false }
    }


    val toggle = ComponentProperty<RenderContext.() -> Unit> {
        pushButton {
            icon { fromTheme { menu } }
            variant { outline }
        }
    }
    val placement = ComponentProperty<DropdownPlacementContext.() -> DropdownPlacement> { bottomRightFacing }
    val content = ComponentProperty<RenderContext.() -> Unit> { }


    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            box(baseClass = this@DropdownComponent.containerCss) {

                box(id = "menu-toggle-${uniqueId()}") {
                    this@DropdownComponent.toggle.value(this)
                    clicks.events.map { } handledBy this@DropdownComponent.visibilityStore.show
                }

                this@DropdownComponent.visibilityStore.data.render { visible ->
                    if (visible) {
                        this@DropdownComponent.renderDropdown(this, styling, baseClass, id, prefix)
                    } else {
                        box { /* just an empty placeholder */ }
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
        val uniqueDropdownId = id ?: "menu-dropdown-${uniqueId()}"

        renderContext.apply {
            box(
                styling = { this as BoxParams
                    styling()
                    Theme().menu.dropdown()
                    when(this@DropdownComponent.placement.value.invoke(DropdownPlacementContext)) {
                        DropdownPlacement.Left -> Theme().menu.placements.left
                        DropdownPlacement.Right -> Theme().menu.placements.right
                        DropdownPlacement.BottomLeftFacing -> Theme().menu.placements.bottomLeftFacing
                        DropdownPlacement.BottomRightFacing -> Theme().menu.placements.bottomRightFacing
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
            val startListeningMillis = Date.now() + 200

            dev.fritz2.dom.Window.clicks.events
                .filter { event ->
                    if (kotlin.js.Date.now() < startListeningMillis)
                        return@filter false

                    val dropdownElement = kotlinx.browser.document.getElementById(dropdownId)
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
                .map { } handledBy this@DropdownComponent.visibilityStore.dismiss
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
// TODO: Adjust documentation
fun RenderContext.dropdown(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String = "menu-dropdown-${uniqueId()}",
    prefix: String = "menu-dropdown",
    build: DropdownComponent.() -> Unit,
) = DropdownComponent()
    .apply(build)
    .render(this, styling, baseClass, id, prefix)
