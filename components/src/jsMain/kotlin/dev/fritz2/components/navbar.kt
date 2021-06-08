package dev.fritz2.components

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.div
import dev.fritz2.styling.nav
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.Theme

open class NavbarComponent : Component<Unit> {
    companion object {

        val staticHeaderCss = staticStyle(
            "navbar-header",
            """
                transition: box-shadow 0.2s;
                position: fixed;
                top: 0;
                left: 0;
                right: 0;
                width: 100%;
            """
        )

        val staticContentCss = staticStyle(
            "navbar-content",
            """
                display: flex;
                align-items: center;
                justify-content: space-between;
                width: 100%;
            """
        )

        val staticBrandCss = staticStyle(
            "navbar-brand",
            """
                display: flex;
                align-items: center;
            """
        )

        val staticActionsCss = staticStyle(
            "navbar-actions",
            """
                display: flex;
                align-items: center;
            """
        )
    }

    val brand = ComponentProperty<RenderContext.() -> Unit> { }
    val actions = ComponentProperty<RenderContext.() -> Unit> {}

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        context.apply {
            nav({
                Theme().navBar.header()
            }, staticHeaderCss + baseClass, id) {
                div({
                    Theme().navBar.content()
                    styling()
                }, staticContentCss, prefix) {
                    div(staticBrandCss.name) {
                        this@NavbarComponent.brand.value(this)
                    }
                    div(staticActionsCss.name) {
                        this@NavbarComponent.actions.value(this)
                    }
                }
            }
        }
    }
}

fun RenderContext.navBar(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "navbar",
    build: NavbarComponent.() -> Unit = {}
) {
    NavbarComponent().apply(build).render(this, styling, baseClass, id, prefix)
}