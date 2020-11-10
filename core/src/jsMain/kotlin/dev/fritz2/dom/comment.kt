package dev.fritz2.dom

import kotlinx.browser.document
import org.w3c.dom.Node

/**
 * provides functionality to handle comments.
 */
interface WithComment<out T : Node> : WithDomNode<T> {

    /**
     * adds a comment at this position.
     *
     * @receiver comment-content
     */
    fun String.asComment() {
        domNode.appendChild(document.createComment(this))
    }

    /**
     * adds a comment in your HTML by using !"Comment Text".
     *
     * @receiver comment-content
     */
    operator fun String.not(): Node = domNode.appendChild(document.createComment(this))
}