package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.identification.uniqueId
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.h5
import dev.fritz2.styling.params.*
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.style
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.Icons
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.w3c.dom.events.MouseEvent

private val staticMenuEntryCss = staticStyle("menu-entry") {
    width { "100%" }
    paddings {
        horizontal { normal }
        vertical { smaller }
    }
    radius { "6px" }
}


/**
 * This class combines the _configuration_ and the core rendering of a menu.
 *
 * A menu consists of a toggle element as well as it's actual content in form of a drop-down with customizable entries.
 * The dropdown floats around the toggle-element and can be closed by simply clicking outside the dropdown.
 *
 * The toggle-element can be any component and is passed via the `toggle` property. A button with a standard
 * menu-icon is used if no toggle-element is specified.
 *
 * The dropdown can be placed _to the left_, _to the right_ or _below_ the toggle-element. This can be specified via the
 * `placement` property. The default placement is below the toggle.
 *
 * Menu entries are specified via a dedicated context exposed by the `items` property.
 * By default the following types of entries can be added to the menu:
 * - Items
 * - Subheaders
 * - Dividers
 *
 * It is also possible to add any other fritz2 component. In this case all menu-specific styling (such as paddings) has
 * to be done manually, however.
 *
 * Example usage:
 * ```kotlin
 * menu {
 *      toggle { pushButton { text("Toggle") } }
 *      placement { below }
 *      items {
 *          item {
 *              leftIcon { add }
 *              text("Item")
 *          }
 *          divider()
 *          subheader("A subsection starts here")
 *          custom {
 *              // custom content
 *              spinner { }
 *          }
 *      }
 * }
 * ```
 *
 * Additionally, it is also possible to extend the menu-DSL by writing extension methods. See [MenuEntriesContext] for
 * more information.
 * ```
 */
open class MenuComponent : Component<Unit> {

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
            variant { dev.fritz2.components.PushButtonComponent.VariantContext.outline }
        }
    }
    val entries = ComponentProperty<(MenuEntriesContext.() -> Unit)?>(value = null)
    val placement = ComponentProperty<DropdownPlacementContext.() -> DropdownPlacement> { bottomRightFacing }


    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            box(baseClass = this@MenuComponent.containerCss) {

                box(id = "menu-toggle-${uniqueId()}") {
                    this@MenuComponent.toggle.value(this)
                    clicks.events.map { } handledBy this@MenuComponent.visibilityStore.show
                }

                this@MenuComponent.visibilityStore.data.render { visible ->
                    if (visible) {
                        this@MenuComponent.renderDropdown(this, styling, baseClass, id, prefix)
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
                    when(this@MenuComponent.placement.value.invoke(DropdownPlacementContext)) {
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
                this@MenuComponent.entries.value?.let {
                    val entriesContext = MenuEntriesContext().apply(it)

                    entriesContext.entries.forEach { entry ->
                        entry.render(context = this, styling = {}, dev.fritz2.styling.StyleClass.None, id = null, prefix)
                    }
                }
            }
            this@MenuComponent.listenToWindowEvents(this, uniqueDropdownId)
        }
    }

    private fun listenToWindowEvents(renderContext: RenderContext, dropdownId: String) {
        renderContext.apply {
            // delay listening so the dropdown is not closed immediately:
            val startListeningMillis = kotlin.js.Date.now() + 200

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
                .map { } handledBy this@MenuComponent.visibilityStore.dismiss
        }
    }
}

/**
 * Creates a standard menu.
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself.
 */
fun RenderContext.menu(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = dev.fritz2.styling.StyleClass.None,
    id: String = "menu-dropdown-${uniqueId()}",
    prefix: String = "menu-dropdown",
    build: MenuComponent.() -> Unit,
) = MenuComponent()
    .apply(build)
    .render(this, styling, baseClass, id, prefix)


/**
 * A special [Component] that can be used as an entry in a [MenuComponent].
 *
 * @see MenuItemComponent
 * @see MenuDividerComponent
 * @see MenuSubheaderComponent
 */
typealias MenuEntryComponent = Component<Unit>

/**
 * Context used to build the entries of the menu.
 *
 * The menu-entry-DSL can be extended via standard Kotlin extension methods. Custom entries must implement the
 * [MenuEntryComponent] interface (alias for `Component<Unit>`) and are added to the Menu via [MenuEntriesContext.addEntry]
 * which is accessibly from within the extension method.
 * In many ways these extension methods are similar to standard fritz2 convenience functions. They are only available in
 * a limited context (`MenuEntriesContext`), however.
 *
 * The following method adds an instance of `MyMenuEntry` to the Menu. It can simply be called from within the `entries`
 * context of [MenuComponent].
 * Notice that `addEntry` is invoked in the end; the entry wouldn't be added otherwise!
 *
 * ```kotlin
 * fun MenuEntriesContext.example(build: MyMenuEntry.() -> Unit) = MyMenuEntry()
 *      .apply(build)
 *      .run(::addEntry)
 * ```
 */
open class MenuEntriesContext {

    private val _entries = mutableListOf<MenuEntryComponent>()
    val entries: List<MenuEntryComponent>
        get() = _entries.toList()

    fun addEntry(entry: MenuEntryComponent) {
        _entries += entry
    }


    fun item(build: MenuItemComponent.() -> Unit): Flow<MouseEvent> = MenuItemComponent()
        .apply(build)
        .also(::addEntry)
        .run { clicks }

    fun custom(build: RenderContext.() -> Unit) = CustomMenuItemComponent()
        .apply { content(build) }
        .run(::addEntry)

    fun subheader(build: MenuSubheaderComponent.() -> Unit) = MenuSubheaderComponent()
        .apply(build)
        .run(::addEntry)

    fun subheader(text: String) = subheader { text(text) }

    fun divider() = addEntry(MenuDividerComponent())
}


/**
 * This class combines the _configuration_ and the core rendering of a MenuItemComponent.
 *
 * A MenuItem is a special kind of button consisting of a label and an optional icon used in dropdown menus.
 * Just like a regular button it is clickable and can be enabled/disabled.
 *
 * It can be configured with an _icon_, a _text_ and a boolean-[Flow] to determine whether the item is enabled.
 *
 * @see MenuItemComponent
 */
open class MenuItemComponent : MenuEntryComponent, FormProperties by FormMixin() {

    private val menuItemButtonCss = style("menu-item-button") {
        display { flex }
        justifyContent { start }
        css("user-select: none")

        hover {
            background { color { neutral.highlight } }
            css("filter: brightness(90%);")
        }

        disabled {
            opacity { "0.4" }
            css("cursor: not-allowed")
        }
    }


    val icon = ComponentProperty<(Icons.() -> IconDefinition)?>(value = null)
    val text = ComponentProperty("")


    private val clickStore = object : RootStore<Unit>(Unit) {
        val forwardMouseEvents = handleAndEmit<MouseEvent, MouseEvent> { _, e -> emit(e) }
    }

    val clicks: Flow<MouseEvent>
        get() = clickStore.forwardMouseEvents


    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            button((staticMenuEntryCss + this@MenuItemComponent.menuItemButtonCss).name) {
                this@MenuItemComponent.icon.value?.let {
                    icon({
                        margins { right { smaller } }
                    }) {
                        fromTheme(it)
                    }
                }
                span { +this@MenuItemComponent.text.value }

                disabled(this@MenuItemComponent.disabled.values)
                clicks.events handledBy this@MenuItemComponent.clickStore.forwardMouseEvents
            }
        }
    }
}

/**
 * This class combines the _configuration_ and the core rendering of a CustomMenuItemComponent.
 *
 * A custom menu item can be any fritz2 component. The component simply wraps any layout in a container and renders it
 * to the menu.
 */
open class CustomMenuItemComponent : MenuEntryComponent {

    val content = ComponentProperty<RenderContext.() -> Unit> { }

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            box(
                styling = {
                    this as BoxParams
                    styling()
                },
                baseClass + staticMenuEntryCss,
                id,
                prefix
            ) {
                this@CustomMenuItemComponent.content.value(this)
            }
        }
    }
}

/**
 * This class combines the _configuration_ and the core rendering of a MenuSubheaderComponent.
 *
 * A subheader can be used to introduce a group of menu entries and separate them from the entries above.
 * It simply is a styled header consisting of a static _text_.
 */
open class MenuSubheaderComponent : MenuEntryComponent {

    val text = ComponentProperty("")

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            h5(baseClass = staticMenuEntryCss, style = {
                css("white-space: nowrap")
            }) {
                +this@MenuSubheaderComponent.text.value
            }
        }
    }
}

/**
 * This class combines the _configuration_ and the core rendering of a MenuSubheaderComponent.
 *
 * Similar to a subheader a divider can be used to group entries together. Compared to a subheader a divider displays
 * a thin line rather than text.
 */
open class MenuDividerComponent : MenuEntryComponent {

    private val menuDividerCss = style("menu-divider") {
        width { "100%" }
        height { "1px" }
        margins { vertical { smaller } }
        background { color { gray300 } }
    }

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            box(baseClass = this@MenuDividerComponent.menuDividerCss) { }
        }
    }
}