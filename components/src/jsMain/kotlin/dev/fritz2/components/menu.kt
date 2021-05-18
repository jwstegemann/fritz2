package dev.fritz2.components

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.h5
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.style
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.Icons
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.HTMLButtonElement


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
 * A Menu consists of different types of entries that are aligned vertically.
 * By default the following types can be added to the menu:
 * - Items (menu buttons)
 * - Subheaders
 * - Dividers
 *
 * It is also possible to add any other fritz2 component. In this case all menu-specific styling (such as paddings) has
 * to be done manually, however.
 *
 * Example usage:
 * ```kotlin
 * menu {
 *      entries {
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
 */
open class MenuComponent : Component<Unit> {

    companion object {
        private val menuContainerCss = staticStyle("menu-container") {
            minWidth { "50px" }
            maxWidth { maxContent }
            paddings {
                vertical { smaller }
            }
        }
    }

    val entries = ComponentProperty<MenuEntriesContext.() -> Unit> { }

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        val entriesContext = MenuEntriesContext().apply(entries.value)
        context.apply {
            box({ this as BoxParams; styling() }, baseClass + menuContainerCss, id, prefix) {
                entriesContext.entries.forEach {
                    it.render(this, { }, StyleClass.None, null, "menu-entry")
                }
            }
        }
    }
}

/**
 * Creates a Menu.
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself.
 */
fun RenderContext.menu(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "menu",
    build: MenuComponent.() -> Unit,
) = MenuComponent()
    .apply(build)
    .render(this, styling, baseClass, id, prefix)


/**
 * This class combines the _configuration_ and the core rendering of a dropdown menu.
 *
 * A dropdown menu is a special kind of [MenuComponent] that floats around a toggle element and is revealed when the
 * latter is clicked.
 * It is configured the same way as a regular [MenuComponent] but has an additional `dropdown`-property for
 * customization of the menu-dropdown.
 *
 * Have a look at the usage example of [MenuComponent] for more information. Note that the factory method is
 * `dropdownMenu { ... }` in this case!
 */
open class DropdownMenuComponent : Component<Unit>, WithDropdown by DropdownMixin() {

    val entries = ComponentProperty<MenuEntriesContext.() -> Unit> { }

    // TODO: Pass down styling params
    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.renderDropdown {
            content {
                menu {
                    entries(this@DropdownMenuComponent.entries.value)
                }
            }
        }
    }
}

/**
 * Creates a dropdown menu.
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself.
 */
fun RenderContext.dropdownMenu(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "dropdown-menu",
    build: DropdownMenuComponent.() -> Unit,
) = DropdownMenuComponent()
    .apply(build)
    .render(this, styling, baseClass, id, prefix)


/**
 * A special [Component] that can be used as an entry in a [MenuComponent].
 *
 * @see MenuItemComponent
 * @see MenuDividerComponent
 * @see MenuHeaderComponent
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

    fun addEntry(entry: MenuEntryComponent) = _entries.add(entry)


    fun item(build: MenuItemComponent.() -> Unit) = MenuItemComponent()
        .apply(build)
        .run(::addEntry)

    fun custom(build: RenderContext.() -> Unit) = CustomMenuItemComponent()
        .apply { content(build) }
        .run(::addEntry)

    fun header(build: MenuHeaderComponent.() -> Unit) = MenuHeaderComponent()
        .apply(build)
        .run(::addEntry)

    fun header(text: String) = header { text(text) }

    fun divider() = addEntry(MenuDividerComponent())
}


/**
 * This class combines the _configuration_ and the core rendering of a MenuItemComponent.
 *
 * A MenuItem is a special kind of button consisting of a label and an optional icon used in dropdown menus.
 * Just like a regular button it is clickable and can be enabled/disabled.
 *
 * It can be configured with an _icon_, a _text_ and a boolean-[Flow] to determine whether the item is enabled.
 */
open class MenuItemComponent :
    MenuEntryComponent,
    EventProperties<HTMLButtonElement> by EventMixin(),
    FormProperties by FormMixin()
{

    private val itemButtonCss = style("menu-item-button") {
        display { flex }
        justifyContent { start }
        css("user-select: none")

        hover {
            background { color { primary.highlight } }
        }

        disabled {
            opacity { "0.4" }
            css("cursor: not-allowed")
        }
    }


    val icon = ComponentProperty<(Icons.() -> IconDefinition)?>(value = null)
    val text = ComponentProperty("")


    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            button((staticMenuEntryCss + this@MenuItemComponent.itemButtonCss).name) {
                this@MenuItemComponent.icon.value?.let {
                    icon({
                        margins { right { smaller } }
                    }) {
                        fromTheme(it)
                    }
                }
                span { +this@MenuItemComponent.text.value }

                disabled(this@MenuItemComponent.disabled.values)
                this@MenuItemComponent.events.value.invoke(this)
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
 * This class combines the _configuration_ and the core rendering of a MenuHeaderComponent.
 *
 * A header can be used to introduce a group of menu entries and separate them from the entries above.
 * It simply consists of a static, styled _text_.
 */
open class MenuHeaderComponent : MenuEntryComponent {

    val text = ComponentProperty("")

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            box(baseClass = staticMenuEntryCss, styling = Theme().menu.header) {
                +this@MenuHeaderComponent.text.value
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