package io.fritz2.examples.webcomponent

import io.fritz2.dom.Tag
import io.fritz2.dom.html.html
import io.fritz2.webcomponents.WebComponent
import io.fritz2.webcomponents.registerWebComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.ShadowRoot


@ExperimentalCoroutinesApi
class WeatherCard : WebComponent<HTMLDivElement>() {

    val city = attributeChanges("city")

    override fun init(element: HTMLElement, shadowRoot: ShadowRoot): Tag<HTMLDivElement> {
        linkStylesheet(shadowRoot, "./weathercard.css")
        // setStylesheet(shadowRoot, """.weather-card { border: 1px solid red; }""")

        return html {
            div("weather-card") {
                h2 { city.bind() }
                h3 {
                    text("Cloudy")
                    span {
                        text("Wind 10km/h ")
                        span("dot") { text("•") }
                        text(" Precip 0%")
                    }
                }
                h1 { text("23°") }
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
                        td { text("TUE") }
                        td { text("WED") }
                        td { text("THU") }
                        td { text("FRI") }
                        td { text("SAT") }
                    }
                    tr {
                        td { text("30°") }
                        td { text("34°") }
                        td { text("36°") }
                        td { text("34°") }
                        td { text("37°") }
                    }
                    tr {
                        td { text("17°") }
                        td { text("22°") }
                        td { text("19°") }
                        td { text("23°") }
                        td { text("19°") }
                    }
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    registerWebComponent("weather-card", WeatherCard::class, "city")
}
