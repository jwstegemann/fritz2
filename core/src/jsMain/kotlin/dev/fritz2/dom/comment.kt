package dev.fritz2.dom

import org.w3c.dom.Comment
import org.w3c.dom.Node
import kotlin.browser.window

/**
 * Interface providing functionality to handle comments
 */
interface WithComment<out T : org.w3c.dom.Node> : WithDomNode<T> {
    /**
     * adds static comment at this position
     *
     * @param value comment-content
     */
    fun comment(value: String): Node = domNode.appendChild(CommentNode(value).domNode)

    /*
     * Shortcut to create a comment in your HTML by using !"Comment Text"
     */
    operator fun String.not(): Node = domNode.appendChild(CommentNode(this).domNode)
}

/**
 * Represents a DOM-Comment
 *
 * @param content comment-content
 * @param domNode wrapped domNode (created by default)
 */
class CommentNode(private val content: String, override val domNode: Comment = window.document.createComment(content)) :
    WithDomNode<Comment>