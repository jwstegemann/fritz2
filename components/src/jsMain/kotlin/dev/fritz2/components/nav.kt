import dev.fritz2.components.*
import dev.fritz2.dom.html.RenderContext
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
) {
    val component = NavLinkComponent().apply(build)

    lineUp({
        Theme().pwa.navLink()
        styling()
    }, baseClass, id, prefix) {
        items {
            component.active.value?.let { className(NavLinkComponent.activeStyle.whenever(it).name) }
            icon(build = component.icon.value)
            a { component.text.values.asText() }
        }
    }
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
        Theme().pwa.navSection()
        styling()
    }) { +text }
}