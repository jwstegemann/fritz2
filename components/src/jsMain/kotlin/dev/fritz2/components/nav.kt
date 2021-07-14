package dev.fritz2.components

import dev.fritz2.components.foundations.Component
import dev.fritz2.components.foundations.ComponentProperty
import dev.fritz2.components.foundations.DynamicComponentProperty
import dev.fritz2.dom.Listener
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.*
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.events.MouseEvent

/**
 * This class combines the _configuration_ and the core rendering of an appFrame navigation-link.
 *
 * You can configure the
 * - icon
 * - text
 * - active state

 * The rendering function is used by the component factory functions [navLink], so it is
 * not meant to be called directly unless you plan to implement your own navLink.
 */
@ExperimentalCoroutinesApi
open class NavLinkComponent : Component<Listener<MouseEvent>> {
    private val activeStyle = style("navlink-active") {
        Theme().appFrame.activeNavLink()
    }

    val icon = ComponentProperty<IconComponent.() -> Unit> { fromTheme { bookmark } }
    val text = DynamicComponentProperty(flowOf("Navigation Link"))
    val active = ComponentProperty<Flow<Boolean>?>(null)

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ): Listener<MouseEvent> {
        var clickEvents: Listener<MouseEvent>? = null

        context.apply {
            lineUp({
                Theme().appFrame.navLink()
                styling(this as BoxParams)
            }, baseClass, id, prefix) {
                spacing { small }
                items {
                    this@NavLinkComponent.active.value?.let {
                        className(this@NavLinkComponent.activeStyle.whenever(it).name)
                    }
                    icon(build = this@NavLinkComponent.icon.value)
                    a { this@NavLinkComponent.text.values.asText() }
                }
                events {
                    clickEvents = clicks.stopImmediatePropagation()
                }
            }
        }

        return clickEvents!!
    }
}


/**
 * This component generates a navigation link inside the [appFrame]'s sidebar.
 *
 * You can set the an icon, text and activce state.
 * For a detailed overview about the possible properties of the component object itself, have a look at
 * [NavLinkComponent]
 *
 * Do not use this component outside of the [appFrame] since it's styling and responsive behaviour are
 * built to work with the frame.
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form `$prefix-$hash`
 * @param build a lambda expression for setting up the component itself. Details in [NavLinkComponent]
 * @return a listener that offers the clicks on this link
 */
@ExperimentalCoroutinesApi
fun RenderContext.navLink(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "navlink",
    build: NavLinkComponent.() -> Unit = {}
): Listener<MouseEvent> = NavLinkComponent().apply(build)
    .render(this, styling, baseClass, id, prefix)


/**
 * This component generates a navigation section header inside the [appFrame]'s sidebar.
 *
 * Do not use this component outside of the [appFrame] since it's styling and responsive behaviour are
 * built to work with the frame.
 *
 * @param text of the section header
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form `$prefix-$hash`
 */
@ExperimentalCoroutinesApi
fun RenderContext.navSection(
    text: String,
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "navsection",
) {
    h3({
        Theme().appFrame.navSection()
        styling()
    }, baseClass, id, prefix) { +text }
}
