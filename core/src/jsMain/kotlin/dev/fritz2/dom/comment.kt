package dev.fritz2.dom

import dev.fritz2.dom.html.HtmlElements
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.Comment
import org.w3c.dom.Node

/**
 * Provides functionality to handle comments.
 */
interface WithComment<out T : Node> : WithDomNode<T>, HtmlElements {

    /**
     * Adds comment-content of a [Flow] at this position
     *
     * @receiver comment-content
     */
    fun Flow<String>.asComment() {
        mountDomNode(job, domNode, this.map { CommentNode(it) })
    }

    /**
     * Adds comment-content of a [Flow] at this position
     *
     * @receiver comment-content
     */
    fun <T> Flow<T>.asComment() {
        mountDomNode(job, domNode, this.map { CommentNode(it.toString()) })
    }

    /**
     * Adds a comment in your HTML by using !"Comment Text".
     *
     * @receiver comment-content
     */
    operator fun String.not(): Node = domNode.appendChild(document.createComment(this))
}

/**
 * Represents a DOM-CommentNode
 *
 * @param content comment-content
 * @param domNode wrapped domNode (created by default)
 */
class CommentNode(private val content: String, override val domNode: Comment = window.document.createComment(content)) :
        WithDomNode<Comment>