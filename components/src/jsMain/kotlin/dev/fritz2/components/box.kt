package dev.fritz2.components

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.FlexParams
import dev.fritz2.styling.staticStyle


class BoxComponent : BaseComponent("box"), Application<Div> by ApplicationDelegate()

fun HtmlElements.box(build: BoxComponent.() -> Unit = {}) {
    val component = BoxComponent().apply(build)

    div(component.cssClasses) {
        component.use(this, component.application)
    }
}


class FlexBoxComponent : BaseComponent(prefix), FlexParams, Application<Div> by ApplicationDelegate() {
    companion object {
        internal const val prefix = "flex-box"
        val staticCss = staticStyle(
            prefix,
            "display: flex;"
        )
    }
}

fun HtmlElements.flexBox(build: FlexBoxComponent.() -> Unit = {}) {
    val component = FlexBoxComponent().apply {
        classes(FlexBoxComponent.staticCss) // basic classes first to be overridable
        build()
    }

    div(component.cssClasses) {
        component.use(this, component.application)
    }
}


class GridBoxComponent : BaseComponent(prefix), FlexParams, Application<Div> by ApplicationDelegate() {
    companion object {
        internal const val prefix = "grid-box"
        val staticCss = staticStyle(
            prefix,
            "display: grid;"
        )
    }
}

fun HtmlElements.gridBox(build: GridBoxComponent.() -> Unit = {}) {
    val component = GridBoxComponent().apply {
        classes(GridBoxComponent.staticCss) // basic classes first to be overridable
        build()
    }

    div(component.cssClasses) {
        component.use(this, component.application)
    }
}
