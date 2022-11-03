package dev.fritz2.examples.webcomponent

import dev.fritz2.core.*
import dev.fritz2.webcomponents.WebComponent
import dev.fritz2.webcomponents.registerWebComponent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.ShadowRoot

@JsModule("@mat3e-ux/stars")
@JsNonModule
external object Stars

class M3Stars(job: Job, scope: Scope) : HtmlTag<HTMLElement>("m3-stars", job = job, scope = scope) {
    fun max(value: Flow<Int>) = attr("max", value.asString())
    fun max(value: Int) = attr("max", value)
    fun current(value: Flow<Float>) = attr("current", value.asString())
    fun current(value: Float) = attr("current", value)
}

fun RenderContext.m3Stars(content: M3Stars.() -> Unit): M3Stars = register(M3Stars(job, scope), content)

object WeatherCard : WebComponent<HTMLDivElement>() {

    private val city: Flow<String> = attributeChanges("city")

    override fun RenderContext.init(element: HTMLElement, shadowRoot: ShadowRoot): HtmlTag<HTMLDivElement> {
        linkStylesheet(shadowRoot, "./weathercard.css")
        // setStylesheet(shadowRoot, """.weather-card { border: 1px solid red; }""")

        // add you Stores, etc. here

        return div("weather-card") {
            h2 { city.renderText() }
            m3Stars {
                max(5)
                current(2f)
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

fun main() {
    registerWebComponent("weather-card", WeatherCard, "city")
}
