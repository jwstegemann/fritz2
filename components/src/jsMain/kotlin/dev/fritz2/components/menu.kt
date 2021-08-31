package dev.fritz2.components

import dev.fritz2.components.menu.*
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BoxParams

/**
 * This component creates a [menu].
 *
 * A menu consists of different types of children that are aligned vertically.
 * By default the following types can be added to the menu:
 * - entries (see [MenuEntry])
 * - headers (see [MenuHeader])
 * - dividers (see [MenuDivider])
 * - submenu (see [SubMenuComponent])
 *
 * It is also possible to add any other fritz2 component via the `custom` context.
 * All menu items are created directly within the [MenuComponent]'s build context.
 *
 * Example usage:
 * ```kotlin
 * menu {
 *      entry {
 *          icon { add }
 *          text("Item")
 *      }
 *      divider()
 *      submenu {
 *          text("A subsection starts here")
 *          icon { menu }
 *          custom {
 *              // custom content
 *              inputField { }
 *          }
 *      }
 * }
 * ```
 *
 * @see MenuComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself.
 */
fun RenderContext.menu(
    styling: BoxParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "menu",
    build: MenuComponent.() -> Unit,
) = MenuComponent(scope)
    .apply(build)
    .render(this, styling, baseClass, id, prefix)