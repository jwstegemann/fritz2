import dev.fritz2.components.*
import dev.fritz2.dom.DomListener
import dev.fritz2.dom.Listener
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.name
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.Theme
import dev.fritz2.styling.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLElement
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
open class NavLinkComponent {
    companion object {
        val activeStyle = staticStyle("navlink-active") {
            Theme().appFrame.activeNavLink()
        }
    }

    val icon = ComponentProperty<IconComponent.() -> Unit> { fromTheme { bookmark } }
    val text = DynamicComponentProperty<String>(flowOf("Navigation Link"))
    val active = ComponentProperty<Flow<Boolean>?>(null)
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
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [NavLinkComponent]
 * @return a listener that offers the clicks on this link
 */
@ExperimentalCoroutinesApi
fun RenderContext.navLink(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "navlink",
    build: NavLinkComponent.() -> Unit = {}
): Listener<MouseEvent> {
    val component = NavLinkComponent().apply(build)
    var clickEvents: Listener<MouseEvent>? = null

    lineUp({
        Theme().appFrame.navLink()
        styling()
    }, baseClass, id, prefix) {
        spacing { small }
        items {
            component.active.value?.let { className(NavLinkComponent.activeStyle.whenever(it).name) }
            icon(build = component.icon.value)
            a { component.text.values.asText() }
        }
        events {
            clickEvents = clicks.stopImmediatePropagation()
        }

    }

    return clickEvents!!
}

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
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 */
@ExperimentalCoroutinesApi
fun RenderContext.navSection(
    text: String,
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "navsection",
) {
    (::h3.styled(baseClass, id, prefix) {
        Theme().appFrame.navSection()
        styling()
    }) { +text }
}