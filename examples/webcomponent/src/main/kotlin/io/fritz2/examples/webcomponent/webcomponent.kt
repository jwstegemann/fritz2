package io.fritz2.examples.webcomponent

import io.fritz2.dom.Tag
import io.fritz2.dom.html.html
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import kotlin.browser.window

@JsName("Function")
private external fun <T> nativeFunction(vararg params: String, block: String): T

fun <X : Element, T : WebComponent<X>> createClass(): (constructor: JsClass<T>) -> () -> dynamic = nativeFunction(
    "_init", "_attributes", block = """
        return class extends HTMLElement {
        
            constructor() {
                super();
                this.webComponent = new _init();
                
                const shadowRoot = this.attachShadow({mode: 'open'});
                shadowRoot.appendChild(this.webComponent.content.domNode)
            }
            
            static get observedAttributes() {
                if (myObservedAttributes) return myObservedAttributes;
            }

        }
    """
)


@JsName("WebComponent")
interface WebComponent<T : Element> {
    @JsName("observedAttributes")
    val observedAttributes: Array<String>

    @JsName("content")
    val content: Tag<T>
}


class WeatherCard : WebComponent<HTMLDivElement> {
    override val observedAttributes: Array<String> = arrayOf("text")

    override val content = html {
        div {
            p {
                text("Hallo, da bin ich")
            }
        }
    }

}

fun <X : Element, T : WebComponent<X>> registerWebComponent(localName: String, constructor: JsClass<T>) {
    val customElementConstructor = createClass<X, T>()(constructor)
    window.customElements.define(localName, customElementConstructor)
}


fun main(args: Array<String>) {
    registerWebComponent("weather-card", WeatherCard::class.js)
}

/*
fun main(args: Array<String>) {
    val xTestSpec = customElementSpec().apply {
        observedAttributes = arrayOf("prop")
        init = XTest::init.unsafeCast<(HTMLElement) -> Unit>()
        attributeChangedCallback = XTest::attributeChangedCallback.unsafeCast<(HTMLElement, String, String, String) -> Unit>()
    }
    val xTestConstructor = getCustomElementConstructor(xTestSpec)
    window.customElements.define("weather-card", xTestConstructor)
}

abstract external class XTest : HTMLElement

fun XTest.init() {
    val shadow = attachShadow(ShadowRootInit(ShadowRootMode.OPEN)).unsafeCast<HTMLElement>()
    shadow.innerHTML = "<p>Works !!!</p>"
}

fun XTest.attributeChangedCallback(attrName: String, oldVal: String, newVal: String) {
    println("$attrName: $oldVal -> $newVal")
}

private fun customElementSpec(): CustomElementSpec = js("{}").unsafeCast<CustomElementSpec>()

private external interface CustomElementSpec {
    var observedAttributes: Array<String>?
    var init: ((receiver: HTMLElement) -> Unit)?
    var connectedCallback: ((receiver: HTMLElement) -> Unit)?
    var disconnectedCallback: ((receiver: HTMLElement) -> Unit)?
    var attributeChangedCallback: ((receiver: HTMLElement, attrName: String, oldVal: String, newVal: String) -> Unit)?
    var adoptedCallback: ((receiver: HTMLElement) -> Unit)?
}

private val getCustomElementConstructor: (spec: CustomElementSpec) -> () -> dynamic by lazy {
    function<(spec: CustomElementSpec) -> () -> dynamic>("spec", block = BLOCK)
}

@JsName("Function")
private external fun <T> function(vararg params: String, block: String): T

private const val BLOCK = """const myObservedAttributes = spec.observedAttributes;
const myInit = spec.init;
const myConnectedCallback = spec.connectedCallback;
const myDisconnectedCallback = spec.disconnectedCallback;
const myAttributeChangedCallback = spec.attributeChangedCallback;
const myAdoptedCallback = spec.adoptedCallback;

return class extends HTMLElement {

    static get observedAttributes() {
        if (myObservedAttributes) {
            return myObservedAttributes;
        }
    }

    constructor() {
        super();
        if (myInit) {
            myInit(this);
        }
    }

    connectedCallback() {
        if (myConnectedCallback) {
            myConnectedCallback(this);
        }
    }

    disconnectedCallback() {
        if (myDisconnectedCallback) {
            myDisconnectedCallback(this);
        }
    }

    attributeChangedCallback(attrName, oldVal, newVal) {
        if (myAttributeChangedCallback) {
            myAttributeChangedCallback(this, attrName, oldVal, newVal);
        }
    }

    adoptedCallback() {
        if (myAdoptedCallback) {
            myAdoptedCallback(this);
        }
    }
};"""
*/