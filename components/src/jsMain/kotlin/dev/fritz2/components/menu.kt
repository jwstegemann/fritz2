package dev.fritz2.components

import dev.fritz2.components.foundations.*
import dev.fritz2.binding.storeOf
import dev.fritz2.dom.HtmlTagMarker
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.plus
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.Icons
import dev.fritz2.styling.theme.Theme
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLButtonElement

open class MenuContext {

    protected val children = mutableListOf<MenuChild>()

    fun addChild(child: MenuChild) = children.add(child)


    /**
     * Configures and adds a [MenuHeader] to the menu.
     *
     * Use the overloaded version of this method to specify additional styling.
     *
     * @param text Text to be displayed in the header
     */
    fun header(text: String) = header({}, text)

    /**
     * Configures and adds a [MenuHeader] to the menu.
     *
     * @param styling [Style] to be applied to the underlying component
     * @param text Text to be displayed in the header
     */
    fun header(styling: Style<BoxParams>, text: String) = MenuHeader(styling)
        .apply { text(text) }
        .run(::addChild)

    /**
     * Configures and adds a [MenuEntry] to the menu.
     *
     * Use the overloaded version of this method to specify additional styling.
     *
     * @param build Lambda to configure the component itself
     */
    fun entry(build: MenuEntry.() -> Unit) = entry({}, build)

    /**
     * Configures and adds a [MenuEntry] to the menu.
     *
     * @param styling [Style] to be applied to the component
     * @param build Lambda to configure the component itself
     */
    fun entry(styling: Style<BoxParams>, build: MenuEntry.() -> Unit) = MenuEntry(styling)
        .apply(build)
        .run(::addChild)

    /**
     * Configures and adds a [MenuLink] to the menu.
     *
     * Use the overloaded version of this method to specify additional styling.
     *
     * @param build Lambda to configure the component itself
     */
    fun link(build: MenuLink.() -> Unit) = link({}, build)

    /**
     * Configures and adds a [MenuLink] to the menu.
     *
     * @param styling [Style] to be applied to the component
     * @param build Lambda to configure the component itself
     */
    fun link(styling: Style<BoxParams>, build: MenuLink.() -> Unit) = MenuLink(styling)
        .apply(build)
        .run(::addChild)

    /**
     * Adds a custom fritz2-component to the menu.
     *
     * @param build Lambda containing the layout to render
     */
    fun custom(build: RenderContext.() -> Unit) = custom({}, build)

    /**
     * Adds a custom fritz2-component to the menu.
     *
     * @param styling [Style] to be applied to the component
     * @param build Lambda containing the layout to render
     */
    fun custom(styling: Style<BoxParams>, build: RenderContext.() -> Unit) = CustomMenuEntry(styling)
        .apply { content(build) }
        .run(::addChild)

    /**
     * Adds a [MenuDivider] to the menu.
     *
     * @param styling optional [Style] to be applied to the underlying component
     */
    fun divider(styling: Style<BoxParams> = {}) = addChild(MenuDivider(styling))

    /**
     * Creates a [submenu].
     *
     * @see SubMenuComponent
     *
     * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
     * @param baseClass optional CSS class that should be applied to the element
     * @param id the ID of the element
     * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
     * @param build a lambda expression for setting up the component itself.
     */
    fun submenu(
        styling: BoxParams.() -> Unit = {},
        baseClass: StyleClass = StyleClass.None,
        id: String? = null,
        prefix: String = "submenu",
        build: SubMenuComponent.() -> Unit,
    ) = SubMenuComponent(styling, baseClass, id, prefix)
        .apply(build)
        .run(::addChild)
}

/**
 * This class combines the _configuration_ and the core rendering of a menu.
 *
 * A menu consists of different types of children that are aligned vertically.
 * By default the following types can be added to the menu:
 * - entries (see [MenuEntry])
 * - headers (see [MenuHeader])
 * - dividers (see [MenuDivider])
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
 * Creates a menu.
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
 * It can be configured with an _icon_, a _text_ and can be enabled or disabled the same way as a regular button.
 *
 * @param styling Optional style to be applied to the underlying button-element
 */
open class MenuEntry(private val styling: Style<BoxParams> = {}) :
    MenuChild,
    EventProperties<HTMLButtonElement> by EventMixin(),
    FormProperties by FormMixin() {
    val icon = ComponentProperty<(Icons.() -> IconDefinition)?>(null)
    val text = ComponentProperty<String?>(null)

    override fun render(context: RenderContext) {
        context.apply {
            button(Theme().menu.entry + this@MenuEntry.styling) {
                this@MenuEntry.icon.value?.let {
                    icon(Theme().menu.icon) { def(it(Theme().icons)) }
                }
                this@MenuEntry.text.value?.let { span { +it } }
                disabled(this@MenuEntry.disabled.values)
                this@MenuEntry.events.value.invoke(this)
            }
        }
    }
}

/**
 * This class combines the _configuration_ and the core rendering of a [MenuLink].
 *
 * A link entry is a special kind of [linkButton] consisting of a label and an optional icon used in dropdown menus.
 * Just like a regular button it is clickable and can be enabled/disabled.
 *
 * It can be configured with an _icon_, a _text_ and can be enabled or disabled the same way as a regular button.
 *
 * @param styling Optional style to be applied to the underlying button-element
 */
open class MenuLink(private val styling: Style<BoxParams> = {}) :
    MenuChild,
    EventProperties<HTMLAnchorElement> by EventMixin(),
    FormProperties by FormMixin() {
    val icon = ComponentProperty<(Icons.() -> IconDefinition)?>(null)
    val text = ComponentProperty<String?>(null)
    val href = ComponentProperty<String?>(null)
    val target = ComponentProperty<String?>(null)

    override fun render(context: RenderContext) {
        context.apply {
            a(Theme().menu.entry + this@MenuLink.styling) {
                this@MenuLink.icon.value?.let {
                    icon(Theme().menu.icon) { def(it(Theme().icons)) }
                }
                this@MenuLink.text.value?.let { span { +it } }
                this@MenuLink.href.value?.let { href(it) }
                this@MenuLink.target.value?.let { target(it) }
                attr("disabled", this@MenuLink.disabled.values)
                this@MenuLink.events.value.invoke(this)
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
open class CustomMenuEntry(private val styling: Style<BoxParams> = {}) : MenuChild {
    val content = ComponentProperty<RenderContext.() -> Unit> { }

    override fun render(context: RenderContext) {
        context.apply {
            div(Theme().menu.custom + this@CustomMenuEntry.styling) {
                this@CustomMenuEntry.content.value(this)
            }
        }
    }
}

/**
 * This class combines the _configuration_ and the core rendering of a [MenuHeader].
 *
 * A header can be used to introduce a group of menu entries and separate them from the entries above.
 * It simply consists of a styled _text_.
 *
 * @param styling Optional styling to be applied to the underlying div-element
 */
open class MenuHeader(private val styling: Style<BoxParams> = {}) : MenuChild {

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
 * Compared to a header a divider displays a thin line rather than a text.
 *
 * @param styling Optional styling to be applied to the underlying div-element
 */
open class MenuDivider(private val styling: Style<BoxParams> = {}) : MenuChild {

    override fun render(context: RenderContext) {
        context.apply {
            div(Theme().menu.divider + this@MenuDivider.styling) { }
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
            div(Theme().menu.sub,
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