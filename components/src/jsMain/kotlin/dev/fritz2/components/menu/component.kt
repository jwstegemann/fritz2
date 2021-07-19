package dev.fritz2.components.menu

import dev.fritz2.binding.storeOf
import dev.fritz2.components.foundations.*
import dev.fritz2.components.icon
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.plus
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.Icons
import dev.fritz2.styling.theme.Theme
import org.w3c.dom.HTMLButtonElement

/**
 * This class combines the _configuration_ and the core rendering of a menu.
 *
 * A menu consists of different types of children that are aligned vertically.
 * By default the following types can be added to the menu:
 * - entries (see [MenuEntry])
 * - headers (see [MenuHeader])
 * - dividers (see [MenuDivider])
 *
 * It is also possible to add any other fritz2 component via the [custom] context.
 * All menu items are created directly within the [MenuComponent]'s build context.
 *
 * The menu-entry-DSL can be extended via standard Kotlin extension methods. Custom entries must implement the
 * [MenuChild] interface and are added to the Menu via the [MenuComponent.addChild] method
 * which is accessible from within the extension method.
 *
 * ```kotlin
 * class MyMenuEntry : MenuChild {
 *      override fun render(context: RenderContext) {
 *          context.apply {
 *              //...
 *          }
 *      }
 * }
 * ```
 *
 * The following example adds an instance of `MyMenuEntry` to the Menu.
 * Notice that [MenuComponent.addChild] is invoked in the end; the entry wouldn't be added otherwise!
 *
 * ```kotlin
 * fun MenuComponent.myEntry(build: MyMenuEntry.() -> Unit) = MyMenuEntry()
 *      .apply(build)
 *      .run(::addChild)
 * ```
 */
open class MenuComponent : Component<Unit>, MenuContext() {

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            div(Theme().menu.container + styling, baseClass, id, prefix) {
                this@MenuComponent.children.forEach {
                    it.render(this)
                }
            }
        }
    }
}

/**
 * This class combines the _configuration_ and the core rendering of a submenu inside a menu.
 *
 * A submenu consists of different types of children that are aligned vertically.
 * By default the following types can be added to the menu:
 * - entries (see [MenuEntry])
 * - dividers (see [MenuDivider])
 *
 * It is also possible to add any other fritz2 component via the `custom` context.
 * All menu items are created directly within the [SubMenuComponent]'s build context.
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
 *          entry {
 *              icon { add }
 *              text("Item")
 *          }
 *      }
 *      divider()
 *      header("A subsection starts here")
 *      custom {
 *          // custom content
 *          spinner { }
 *      }
 * }
 * ```
 *
 * The menu-entry-DSL can be extended via standard Kotlin extension methods. Custom entries must implement the
 * `Component<Unit>` interface and are added to the Menu via the [MenuComponent.addChild] method
 * which is accessible from within the extension method.
 *
 * The following example adds an instance of `MyMenuEntry` to the Menu.
 * Notice that `addChild` is invoked in the end; the entry wouldn't be added otherwise!
 *
 * ```kotlin
 * fun MenuComponent.example(build: MyMenuEntry.() -> Unit) = MyMenuEntry()
 *      .apply(build)
 *      .run(::addChild)
 * ```
 */
open class SubMenuComponent(
    val styling: Style<BoxParams>,
    val baseClass: StyleClass,
    val id: String?,
    val prefix: String
) : MenuChild,
    EventProperties<HTMLButtonElement> by EventMixin(),
    FormProperties by FormMixin(),
    MenuContext() {

    companion object {
        val hidden = staticStyle("hidden", "display: none")
    }

    val icon = ComponentProperty<(Icons.() -> IconDefinition)?>(null)
    val text = ComponentProperty<String?>(null)

    private val hide = storeOf(true)

    override fun render(context: RenderContext) {
        context.apply {
            button(Theme().menu.entry + this@SubMenuComponent.styling) {
                this@SubMenuComponent.icon.value?.let {
                    icon(Theme().menu.icon) { def(it(Theme().icons)) }
                }
                this@SubMenuComponent.text.value?.let { span { +it } }
                disabled(this@SubMenuComponent.disabled.values)
                clicks.stopPropagation() handledBy this@SubMenuComponent.hide.handle { !it }
                this@SubMenuComponent.events.value.invoke(this)
            }
            div(
                Theme().menu.sub,
                this@SubMenuComponent.baseClass,
                this@SubMenuComponent.id,
                this@SubMenuComponent.prefix
            ) {
                className(hidden.whenever(this@SubMenuComponent.hide.data).name)
                this@SubMenuComponent.children.forEach {
                    it.render(this)
                }
            }
        }
    }
}