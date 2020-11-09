package dev.fritz2.dom

import dev.fritz2.dom.html.RenderContext
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.Node
import org.w3c.dom.Text


/**
 * Interface providing functionality to handle text-content
 */
interface WithText<N : Node> : WithDomNode<N>, RenderContext {
    /**
     * adds static text-content at this position
     *
     * @param content text-content
     */
    fun text(content: String): Node = domNode.appendChild(document.createTextNode(content))

    /**
     * adds text-content of a [Flow] at this position
     *
     * @param content text-content
     */
    fun text(content: Flow<String>) {
        mountDomNode(job, domNode) { childJob ->
            childJob.cancelChildren()
            content.map { TextNode(it) }
        }
    }

    /**
     * adds static text-content at this position
     *
     * @receiver text-content
     */
    operator fun String.unaryPlus(): Node = text(this)
}

/**
 * Represents a DOM-TextNode
 *
 * @param content text-content
 * @param domNode wrapped domNode (created by default)
 */
class TextNode(private val content: String, override val domNode: Text = window.document.createTextNode(content)) :
    WithDomNode<Text>
