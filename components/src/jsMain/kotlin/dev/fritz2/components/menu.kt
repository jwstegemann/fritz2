package dev.fritz2.components

import dev.fritz2.dom.HtmlTagMarker
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.button
import dev.fritz2.styling.div
import dev.fritz2.styling.params.*
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.Icons
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.HTMLButtonElement

/**
 * This class combines the _configuration_ and the core rendering of a menu.
 *
 * A Menu consists of different types of entries that are aligned vertically.
 * By default the following types can be added to the menu:
 * - Entries
 * - Headers
 * - Dividers
 *
 * It is also possible to add any other fritz2 component via the `custom` context.
 * All menu items are created directly within the [MenuComponent]'s build context.
 *
 * Example usage:
 * ```kotlin
 * menu {
 *      entry {
 *          leftIcon { add }
 *          text("Item")
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
 * `Component<Unit>` interface and are added to the Menu via [MenuComponent.addChild]
 * which is accessible from within the extension method.
 * In a way these extension methods are similar to standard fritz2 factory methods.
 *
 * The following example adds an instance of `MyMenuEntry` to the Menu.
 * Notice that `addEntry` is invoked in the end; the entry wouldn't be added otherwise!
 *
 * ```kotlin
 * fun MenuComponent.example(build: MyMenuEntry.() -> Unit) = MyMenuEntry()
 *      .apply(build)
 *      .run(::addEntry)
 * ```
 */
open class MenuComponent : Component<Unit> {

    private val children = mutableListOf<MenuChild>()
    fun addChild(child: MenuChild) = children.add(child)


    /**
     * Configures and adds a [MenuEntry] to the menu.
     *
     * Use the overloaded version of this method to specify additional styling.
     *
     * @param build Lambda to configure the component itself
     */
    fun entry(build: MenuEntry.() -> Unit) = entry(styling = { }, build)

    /**
     * Configures and adds a [MenuEntry] to the menu.
     *
     * @param styling Styling to be applied to the component
     * @param build Lambda to configure the component itself
     */
    fun entry(styling: Style<FlexParams>, build: MenuEntry.() -> Unit) = MenuEntry(styling)
        .apply(build)
        .run(::addChild)

    /**
     * Adds a custom fritz2-component to the menu.
     *
     * @param build Lambda containing the layout to render
     */
    fun custom(build: RenderContext.() -> Unit) = CustomMenuEntry()
        .apply { content(build) }
        .run(::addChild)

    /**
     * Configures and adds a [MenuHeader] to the menu.
     *
     * Use the overloaded version of this method to specify additional styling.
     *
     * @param text Text to be displayed in the header
     */
    fun header(text: String) = header(styling = { }, text)

    /**
     * Configures and adds a [MenuHeader] to the menu.
     *
     * @param styling Styling to be applied to the underlying component
     * @param text Text to be displayed in the header
     */
    fun header(styling: Style<BasicParams>, text: String) = MenuHeader(styling)
        .apply { text(text) }
        .run(::addChild)

    /**
     * Adds a [MenuDivider] to the menu.
     *
     * @param styling Optional styling to be applied to the underlying component
     */
    fun divider(styling: Style<BasicParams> = { }) = addChild(MenuDivider(styling))


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
 * An interface for renderable sub-components of a Menu all children of a Menu must implement.
 */
@HtmlTagMarker
interface MenuChild {
    fun render(context: RenderContext)
}

/**
 * This class combines the _configuration_ and the core rendering of a [MenuEntry].
 *
 * An entry is a special kind of button consisting of a label and an optional icon used in dropdown menus.
 * Just like a regular button it is clickable and can be enabled/disabled.
 *
 * It can be configured with an _icon_, a _text_ and a boolean-[Flow] to determine whether the item is enabled.
 *
 * @param styling Optional style to be applied to the underlying button-element
 */
open class MenuEntry(private val styling: Style<FlexParams> = { }) :
    MenuChild,
    EventProperties<HTMLButtonElement> by EventMixin(),
    FormProperties by FormMixin()
{
    val icon = ComponentProperty<(Icons.() -> IconDefinition)?>(null)
    val text = ComponentProperty("")

    override fun render(context: RenderContext) {
        context.apply {
            button(Theme().menu.entry + this@MenuEntry.styling) {
                this@MenuEntry.icon.value?.let {
                    icon({
                        margins { right { smaller } }
                    }) { def(it(Theme().icons)) }
                }
                span { +this@MenuEntry.text.value }

                disabled(this@MenuEntry.disabled.values)
                this@MenuEntry.events.value.invoke(this)
            }
        }
    }
}

/**
 * This class combines the _configuration_ and the core rendering of a [CustomMenuEntry].
 *
 * A custom menu entry can be any fritz2 component. The component simply wraps any layout in a container and renders it
 * to the menu.
 */
open class CustomMenuEntry : MenuChild {
    val content = ComponentProperty<RenderContext.() -> Unit> { }

    override fun render(context: RenderContext) {
        context.apply {
            div(Theme().menu.custom) {
                this@CustomMenuEntry.content.value(this)
            }
        }
    }
}

/**
 * This class combines the _configuration_ and the core rendering of a [MenuHeader].
 *
 * A header can be used to introduce a group of menu entries and separate them from the entries above.
 * It simply consists of a static, styled _text_.
 *
 * @param styling Optional styling to be applied to the underlying div-element
 */
open class MenuHeader(private val styling: Style<BasicParams> = { }) : MenuChild {

    val text = ComponentProperty("")

    override fun render(context: RenderContext) {
        context.apply {
            div(Theme().menu.header + this@MenuHeader.styling) {
                +this@MenuHeader.text.value
            }
        }
    }
}

/**
 * This class combines the _configuration_ and the core rendering of a [MenuDivider].
 *
 * Similar to a header a divider can be used to group entries together.
 * Compared to a header a divider displays a thin line rather than text.
 *
 * @param styling Optional styling to be applied to the underlying div-element
 */
open class MenuDivider(private val styling: Style<BasicParams> = { }) : MenuChild {

    override fun render(context: RenderContext) {
        context.apply {
            div(Theme().menu.divider + this@MenuDivider.styling) { }
        }
    }
}