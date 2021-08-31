package dev.fritz2.components.menu

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.submenu

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
    fun header(styling: Style<BoxParams>, text: String) {
        MenuHeader(styling)
            .apply { text(text) }
            .run(::addChild)
    }

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
    fun entry(styling: Style<BoxParams>, build: MenuEntry.() -> Unit) {
        MenuEntry(styling)
            .apply(build)
            .run(::addChild)
    }

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
    fun link(styling: Style<BoxParams>, build: MenuLink.() -> Unit) {
        MenuLink(styling)
            .apply(build)
            .run(::addChild)
    }

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
    fun custom(styling: Style<BoxParams>, build: RenderContext.() -> Unit) {
        CustomMenuEntry(styling)
            .apply { content(build) }
            .run(::addChild)
    }

    /**
     * Adds a [MenuDivider] to the menu.
     *
     * @param styling optional [Style] to be applied to the underlying component
     */
    fun divider(styling: Style<BoxParams> = {}) { addChild(MenuDivider(styling)) }

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