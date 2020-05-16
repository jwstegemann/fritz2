package io.fritz2.webcomponents

import io.fritz2.dom.Tag
import io.fritz2.flow.asSharedFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.w3c.dom.*
import kotlin.browser.window
import kotlin.reflect.KClass

@JsName("Function")
private external fun <T> nativeFunction(vararg params: String, block: String): T


internal fun <X : Element, T : WebComponent<X>> createClass(): (constructor: JsClass<T>, observedAttributes: Array<out String>) -> () -> dynamic =
    nativeFunction(
        "_component", "_attributes", block = """
        return class extends HTMLElement {
        
            constructor() {
                super();
                this.webComponent = new _component();
                
                const shadowRoot = this.attachShadow({mode: 'open'});
                
                let content = this.webComponent.init(this, shadowRoot)
                shadowRoot.appendChild(content.domNode)
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
    @JsName("init")
    abstract fun init(element: HTMLElement, shadowRoot: ShadowRoot): Tag<T>

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


    fun linkStylesheet(shadowRoot: ShadowRoot, url: String) =
        shadowRoot.ownerDocument?.let {
            shadowRoot.appendChild(it.createElement("link").unsafeCast<HTMLLinkElement>().apply {
                rel = "stylesheet"
                href = "weathercard.css"
            })
        }

    fun setStylesheet(shadowRoot: ShadowRoot, text: String) =
        shadowRoot.ownerDocument?.let {
            shadowRoot.appendChild(it.createElement("style").unsafeCast<HTMLStyleElement>().apply {
                innerText = text
            })
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
