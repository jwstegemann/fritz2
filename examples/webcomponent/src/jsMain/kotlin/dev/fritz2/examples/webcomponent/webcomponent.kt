package dev.fritz2.examples.webcomponent

import dev.fritz2.core.HtmlTag
import dev.fritz2.core.RenderContext
import dev.fritz2.webcomponents.WebComponent
import dev.fritz2.webcomponents.registerWebComponent
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.ShadowRoot


object WeatherCard : WebComponent<HTMLDivElement>() {

    private val city = attributeChanges("city")

    override fun RenderContext.init(element: HTMLElement, shadowRoot: ShadowRoot): HtmlTag<HTMLDivElement> {
        linkStylesheet(shadowRoot, "./weathercard.css")
        // setStylesheet(shadowRoot, """.weather-card { border: 1px solid red; }""")

        // add you Stores, etc. here

        return div("weather-card") {
            h2 { city.renderText() }
            custom("m3-stars") {
                attr("max", "5")
                attr("current", "3.5")
            }
            h3 {
                +"Cloudy"
                span {
                    +"Wind 10km/h "
                    span("dot") { +"•" }
                    +" Precip 0%"
                }
            }
            h1 { +"23°" }
            div("sky") {
                div("sun") { }
                div("cloud") {
                    div("circle-small") {}
                    div("circle-tall") {}
                    div("circle-medium") {}
                }
            }
            table {
                tr {
                    td { +"TUE" }
                    td { +"WED" }
                    td { +"THU" }
                    td { +"FRI" }
                    td { +"SAT" }
                }
                tr {
                    td { +"30°" }
                    td { +"34°" }
                    td { +"36°" }
                    td { +"34°" }
                    td { +"37°" }
                }
                tr {
                    td { +"17°" }
                    td { +"22°" }
                    td { +"19°" }
                    td { +"23°" }
                    td { +"19°" }
                }
            }
        }
    }
}

@JsModule("@mat3e-ux/stars")
@JsNonModule
external object Stars

fun main() {
    registerWebComponent("weather-card", WeatherCard, "city")
    Stars // have to use the component somewhere to get the import executed
}
