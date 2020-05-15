package io.fritz2.webcomponents

import io.fritz2.dom.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import org.w3c.dom.Element
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
                shadowRoot.appendChild(this.webComponent.content.domNode)
                console.log("Hallo!!!")
            }
            
            static get observedAttributes() {
                if (_attributes) return _attributes;
            }
            
            attributeChangedCallback(attrName, oldVal, newVal) {
                console.log("changed " + attrName + "=" + newVal)
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
        callbackFlow {
            attributeChangedCallback = { name, value ->
                offer(Pair(name, value))
            }
            awaitClose {}
        }
    } else {
        flowOf()
    }

}

fun <X : Element, T : WebComponent<X>> registerWebComponent(
    localName: String,
    constructor: KClass<T>,
    vararg observedAttributes: String
) {
    val customElementConstructor = createClass<X, T>()(constructor.js, observedAttributes)
    window.customElements.define(localName, customElementConstructor)
}
