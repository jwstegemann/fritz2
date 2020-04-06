package io.fritz2.dom

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.w3c.dom.Node
import org.w3c.dom.Text
import kotlin.browser.window


interface WithText<T : org.w3c.dom.Node> : WithDomNode<T> {
    fun text(value: String): Node = domNode.appendChild(TextNode(value).domNode)

    //conflate because updates that occur faster than dom-manipulation should be ommitted
    fun Flow<String>.bind() = DomMountPoint<Text>(this.map {
        TextNode(it)
    }.distinctUntilChanged().conflate(), domNode)
}

class TextNode(private val content: String, override val domNode: Text = window.document.createTextNode(content)) :
    WithDomNode<Text>
