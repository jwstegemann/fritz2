package dev.fritz2.dom

import dev.fritz2.binding.SingleMountPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.w3c.dom.Node
import org.w3c.dom.Text
import kotlin.browser.window


/**
 * Interface providing functionality to handle text-content
 */
interface WithText<T : org.w3c.dom.Node> : WithDomNode<T> {
    /**
     * adds static text-content at this position
     *
     * @param value text-content
     */
    fun text(value: String): Node = domNode.appendChild(TextNode(value).domNode)

    operator fun String.unaryPlus(): Node = domNode.appendChild(TextNode(this).domNode)

    /**
     * binds a [Flow] of [String]s at this position (creates a [DomMountPoint] here as a placeholder)
     *
     * @param value text-content
     */
    //conflate because updates that occur faster than dom-manipulation should be ommitted
    fun Flow<String>.bind(preserveOrder: Boolean = false): SingleMountPoint<WithDomNode<Text>> {
        val upstream = this.map {
            TextNode(it)
        }.distinctUntilChanged()

        return if (preserveOrder) DomMountPointPreserveOrder(upstream, domNode)
        else DomMountPoint(upstream, domNode)
    }
}

/**
 * Represents a DOM-TextNode
 *
 * @param content text-content
 * @param domNode wrapped domNode (created by default)
 */
class TextNode(private val content: String, override val domNode: Text = window.document.createTextNode(content)) :
    WithDomNode<Text>
