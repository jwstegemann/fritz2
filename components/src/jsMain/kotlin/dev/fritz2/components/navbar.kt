package dev.fritz2.components

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle

open class NavbarComponent : Component<Unit> {
    companion object {

        val staticHeaderCss = staticStyle(
            "navbar-header",
            """
                transition: box-shadow 0.2s;
                position: fixed;
                top: 0;
                z-index: 199;
                background: #FFFFFF;
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
                height: 4.5rem;
                width: 100%;
                padding-left: 1.5rem;
                padding-right: 1.5rem;
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
        with(context) {
            nav((staticHeaderCss + baseClass).name, id) {
                (::div.styled(baseClass = staticContentCss, prefix = prefix) {
                    borders {
                        top {
                            width { "6px" }
                            style { solid }
                            color { primary.main }
                        }

                        bottom {
                            width { "2px" }
                            style { solid }
                            color { gray300 }
                        }
                    }
                    styling()
                }) {
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