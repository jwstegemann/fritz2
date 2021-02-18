import dev.fritz2.components.*
import dev.fritz2.dom.Listener
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.stopImmediatePropagation
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.name
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.Theme
import dev.fritz2.styling.theme.important
import dev.fritz2.styling.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.MouseEvent

@ExperimentalCoroutinesApi
open class NavLinkComponent {
    companion object {
        val activeStyle = staticStyle("navlink-active") {
            background { color { "rgba(0,0,0,0.2)" } }
            borders {
                left {
                    color { lightGray.important }
                }
            }
        }
    }

    var icon = ComponentProperty<IconComponent.() -> Unit> { fromTheme { bookmark } }
    var text = DynamicComponentProperty<String>(flowOf("Navigation Link"))
    var active = ComponentProperty<Flow<Boolean>?>(null)
}

@ExperimentalCoroutinesApi
fun RenderContext.navLink(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "navlink",
    build: NavLinkComponent.() -> Unit = {}
): Listener<MouseEvent, HTMLElement> {
    val component = NavLinkComponent().apply(build)
    var clickEvents: Listener<MouseEvent, HTMLElement>? = null

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