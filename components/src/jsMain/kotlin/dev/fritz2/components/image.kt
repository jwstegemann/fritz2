package dev.fritz2.components

import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.Img
import org.w3c.dom.HTMLImageElement


class ImageComponent : BaseComponent(prefix), Application<Img> by ApplicationDelegate() {
    companion object {
        internal const val prefix = "image"
    }

    val src = StringAttributeDelegate<HTMLImageElement, Img>("src")
}

fun HtmlElements.image(build: ImageComponent.() -> Unit = {}) {
    val component = ImageComponent().apply {
        build()
    }

    img(component.cssClasses) {
        component.use(
            this,
            component.src.value,
            component.application
        )
    }
}

