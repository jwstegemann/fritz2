package dev.fritz2.dom

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Comment
import org.w3c.dom.Node

/**
 * Provides functionality to handle comments.
 */
interface WithComment<out T : Node> : WithDomNode<T> {

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