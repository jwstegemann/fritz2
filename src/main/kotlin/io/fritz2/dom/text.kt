package io.fritz2.dom

import io.fritz2.binding.SingleMountPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.w3c.dom.Text
import kotlin.browser.window

@ExperimentalCoroutinesApi
interface WithText<T : org.w3c.dom.Node> : WithDomNode<T> {
    operator fun String.unaryPlus() = domNode.appendChild(TextNode(this).domNode)

    operator fun Flow<String>.unaryPlus(): SingleMountPoint<WithDomNode<Text>> = this.bind()

    //TODO: what does conflate realy mean?
    fun Flow<String>.bind() = DomMountPoint<Text>(this.map {
        TextNode(it)
    }.distinctUntilChanged().conflate(), domNode)
}

class TextNode(private val content: String, override val domNode: Text = window.document.createTextNode(content)): WithDomNode<Text>
