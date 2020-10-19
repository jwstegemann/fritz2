package dev.fritz2.components

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.IconDefinition
import dev.fritz2.styling.theme.Icons
import dev.fritz2.styling.theme.theme
import kotlinx.browser.document
import org.w3c.dom.svg.SVGElement


//FIXME: move to HtmlElements...
const val xmlns = "http://www.w3.org/2000/svg"

fun createIconSvgElement(baseClass: String?): SVGElement {
    val elem = document.createElementNS(xmlns, "svg").unsafeCast<SVGElement>()
    baseClass?.let { elem.setAttributeNS(null, "class", it) }
    return elem
}

class Svg(
    id: String? = null, baseClass: String? = null, override val domNode: SVGElement = createIconSvgElement(baseClass)
) : Tag<SVGElement>(domNode = domNode, tagName = "")


fun HtmlElements.svg(baseClass: String?, id: String?, init: Svg.() -> Unit): Svg {
    return register(Svg(baseClass = baseClass), init)
}

class IconComponent {
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

fun HtmlElements.icon(
    styling: BasicParams.() -> Unit = {},
    baseClass: String? = null,
    id: String? = null,
    prefix: String = "icon",
    build: IconComponent.() -> Unit = {}
) {
    val component = IconComponent().apply(build)

    component.def?.let {
        (::svg.styled(IconComponent.staticCss, id, prefix) {
            styling()
        }) {
            domNode.setAttributeNS(null, "viewBox", it.viewBox)
            domNode.setAttributeNS(null, "fill", "none")

            val path = document.createElementNS(xmlns, "path")
            path.setAttributeNS(null, "d", it.path)
            path.setAttributeNS(null, "fill", "currentColor")

            domNode.appendChild(path)
        }
    }
}


