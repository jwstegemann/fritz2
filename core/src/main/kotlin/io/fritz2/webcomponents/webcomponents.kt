package io.fritz2.webcomponents

import io.fritz2.dom.Tag
import io.fritz2.flow.asSharedFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.HTMLLinkElement
import org.w3c.dom.HTMLStyleElement
import kotlin.browser.window
import kotlin.reflect.KClass

@JsName("Function")
private external fun <T> nativeFunction(vararg params: String, block: String): T


internal fun <X : Element, T : WebComponent<X>> createClass(): (constructor: JsClass<T>, observedAttributes: Array<out String>) -> () -> dynamic =
    nativeFunction(
        "_init", "_attributes", block = """
        return class extends HTMLElement {
        
            constructor() {
                super();
                this.webComponent = new _init();
                
                const shadowRoot = this.attachShadow({mode: 'open'});

                let styleElement = this.webComponent.loadCss(document)
                if (styleElement) shadowRoot.appendChild(styleElement)

                shadowRoot.appendChild(this.webComponent.content.domNode)
            }
            
            static get observedAttributes() {
                if (_attributes) return _attributes;
            }
            
            attributeChangedCallback(attrName, oldVal, newVal) {
                if (this.webComponent.attributeChangedCallback) this.webComponent.attributeChangedCallback(attrName, newVal);
            }


        }
    """
    )


@ExperimentalCoroutinesApi
@JsName("WebComponent")
abstract class WebComponent<T : Element>(observeAttributes: Boolean = true) {
    @JsName("content")
    abstract val content: Tag<T>

    lateinit var attributeChangedCallback: (name: String, value: String) -> Unit

    val attributeChanges: Flow<Pair<String, String>> = if (observeAttributes) {
        callbackFlow<Pair<String, String>> {
            attributeChangedCallback = { name, value ->
                offer(Pair(name, value))
            }
            awaitClose {}
        }.distinctUntilChanged().asSharedFlow()
    } else {
        flowOf()
    }

    @JsName("loadCss")
    open fun loadCss(document: Document): Element? = null

    fun linkStylesheet(document: Document, url: String) =
        document.createElement("link").unsafeCast<HTMLLinkElement>().apply {
            rel = "stylesheet"
            href = "weathercard.css"
        }

    fun setStylesheet(document: Document, text: String) =
        document.createElement("style").unsafeCast<HTMLStyleElement>().apply {
            innerText = text
        }

    fun attributeChanges(name: String) = attributeChanges.filter { (n, _) -> n == name }.map { (_, value) -> value }
}

fun <X : Element, T : WebComponent<X>> registerWebComponent(
    localName: String,
    constructor: KClass<T>,
    vararg observedAttributes: String
) {
    val customElementConstructor = createClass<X, T>()(constructor.js, observedAttributes)
    window.customElements.define(localName, customElementConstructor)
}
