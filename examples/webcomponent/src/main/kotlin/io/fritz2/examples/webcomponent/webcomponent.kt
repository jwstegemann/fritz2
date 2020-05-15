package io.fritz2.examples.webcomponent

import io.fritz2.dom.html.html
import io.fritz2.webcomponents.WebComponent
import io.fritz2.webcomponents.registerWebComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLDivElement


@ExperimentalCoroutinesApi
class WeatherCard : WebComponent<HTMLDivElement>() {
    override val content = html {
        div {
            p {
                text("Hallo, da bin ich...")
                attributeChanges.map { (name, value) -> value }.bind()
            }
        }
    }
}

fun main(args: Array<String>) {
    registerWebComponent("weather-card", WeatherCard::class, "text")
}
