package dev.fritz2.components

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.styling.params.BasicStyleParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.use
import dev.fritz2.styling.staticStyle
import kotlinx.browser.document
import org.w3c.dom.svg.SVGElement

const val xmlns = "http://www.w3.org/2000/svg"

class IconDefinition(
    val displayName: String,
    val viewBox: String = "0 0 24 24",
    val path: String
)


class Svg(
    override val domNode: SVGElement =
        document.createElementNS(xmlns, "svg").unsafeCast<SVGElement>()
) : Tag<SVGElement>("", domNode = domNode)


val iconFoundations = staticStyle(
    "icon",
    """
    width: 1.25rem;
    height: 1.25rem;
    color: currentColor;
    display: inline-block;
    vertical-align: middle;
    flex-shrink: 0;
    backface-visibility: hidden;
    
    :not(":root") {
        overflow: hidden;
    }
"""
)


fun HtmlElements.Icon(styles: Style<BasicStyleParams> = {}, def: IconDefinition) {
    val classAttribute = "$iconFoundations ${use(styles, "icon")}"
    val element = Svg()
    register(element, {
        it.domNode.setAttributeNS(null, "viewBox", def.viewBox)
        it.domNode.setAttributeNS(null, "fill", "none")
        it.domNode.setAttributeNS(null, "class", classAttribute)

        val path = document.createElementNS(xmlns, "path")
        path.setAttributeNS(null, "d", def.path)
        path.setAttributeNS(null, "fill", "currentColor")

        it.domNode.appendChild(path)
    })
}


