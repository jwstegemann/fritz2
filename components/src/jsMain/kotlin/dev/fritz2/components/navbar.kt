package dev.fritz2.components


import dev.fritz2.binding.Store
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle

@ComponentMarker
open class NavbarComponent {
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

    var brand: Div.() -> Unit = { }

    fun brand(value: Div.() -> Unit) {
        brand = value
    }


    var actions: Div.() -> Unit = { }

    fun actions(value: Div.() -> Unit) {
        actions = value
    }


}


fun RenderContext.navBar(
    styling: BasicParams.() -> Unit = {},
    store: Store<String>? = null,
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "navBar",
    build: NavbarComponent.() -> Unit = {}
) {
    val component = NavbarComponent().apply(build)

    nav((NavbarComponent.staticHeaderCss + baseClass).name) {
        (::div.styled(baseClass = NavbarComponent.staticContentCss) {
            borders {
                top {
                    width { "6px" }
                    style { solid }
                    color { primary }
                }


                bottom {
                    width { "2px" }
                    style { solid }
                    color { light }
                }
            }
            styling()
        }) {
            div(NavbarComponent.staticBrandCss.name) {
                component.brand(this)
            }
            div(NavbarComponent.staticActionsCss.name) {
                component.actions(this)
            }
        }
    }

}
