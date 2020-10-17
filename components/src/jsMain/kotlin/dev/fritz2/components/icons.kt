package dev.fritz2.components

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.Icons
import dev.fritz2.styling.theme.theme
import kotlinx.browser.document
import org.w3c.dom.svg.SVGElement

const val xmlns = "http://www.w3.org/2000/svg"


class Svg(
    override val domNode: SVGElement =
        document.createElementNS(xmlns, "svg").unsafeCast<SVGElement>()
) : Tag<SVGElement>("", domNode = domNode)


class IconComponent : BaseComponent(prefix) {
    companion object {
        const val prefix = "icon"
        val staticCss = staticStyle(
            prefix,
            """
                width: 1.25rem;
                height: 1.25rem;
                color: currentColor;
                display: inline-block;
                vertical-align: middle;
                flex-shrink: 0;
                backface-visibility: hidden;
            """
        )
    }

    var def: IconDefinition? = null

    fun fromTheme(value: Icons.() -> IconDefinition) {
        def = theme().icons.value()
    }
}


fun HtmlElements.Icon(build: IconComponent.() -> Unit) {
    val component = IconComponent().apply {
        classes(IconComponent.staticCss)
    }

    component.def?.let { def ->
        val classAttribute = component.cssClasses
        val element = Svg()
        register(element, {
            it.domNode.setAttributeNS(null, "viewBox", def.viewBox)
            it.domNode.setAttributeNS(null, "fill", "none")
            classAttribute?.let { cssClasses -> it.domNode.setAttributeNS(null, "class", cssClasses) }

            val path = document.createElementNS(xmlns, "path")
            path.setAttributeNS(null, "d", def.path)
            path.setAttributeNS(null, "fill", "currentColor")

            it.domNode.appendChild(path)
        })
    }
}


