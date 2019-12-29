import kotlinx.coroutines.flow.Flow
import org.w3c.dom.*

object HTML {

    class DomMountPoint(upstream: Flow<HtmlNode>, val target: Node?) : SingleMountPoint<HtmlNode>(upstream) {
        override fun set(value: HtmlNode, last: HtmlNode?) {
            last?.let { target?.replaceChild(value.domNode, last.domNode) }
                    ?: target?.appendChild(value.domNode)
        }
    }

    fun Flow<HtmlNode>.mount(targetId: String) {
        Browser.document.getElementById(targetId)?.let { DomMountPoint(this, it) }
    }

    fun div(content: Div.() -> Unit): Div = Div().also { it.content() }


    interface WithDomNode {
        val domNode: Node
    }

    interface WithText : WithDomNode {
        operator fun String.unaryPlus() = domNode?.appendChild(Browser.document.createTextNode(this))
    }

    abstract class HtmlNode(override val domNode: Node) : WithDomNode {
        fun div(content: Div.() -> Unit): Div = Div().also {
            it.content()
            domNode.appendChild(it.domNode)
        }

        fun mount(upstream: Flow<HtmlNode>): SingleMountPoint<HtmlNode> = DomMountPoint(upstream, domNode)
    }


    class Div(): HtmlNode(Browser.document.createElement("div")), WithText


}

