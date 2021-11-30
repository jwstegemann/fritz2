package dev.fritz2.webcomponents

import dev.fritz2.dom.Tag
import dev.fritz2.dom.WithDomNode
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Scope
import kotlinx.browser.window
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import org.w3c.dom.*
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
                let content = this.webComponent.initializeInternal(this, shadowRoot);
                shadowRoot.appendChild(content.domNode);
            }
            
            static get observedAttributes() {
                if (_attributes) return _attributes;
            }
            
            attributeChangedCallback(attrName, oldVal, newVal) {
                this.webComponent.attributeChangedCallback(attrName, newVal);
            }
            
            connectedCallback() {
                this.webComponent.connectedCallback(this)
            }

            disconnectedCallback() {
                this.webComponent.disconnectedCallback(this)
            }

            adoptedCallback() {
                this.webComponent.adoptedCallback(this)
            }
        }
    """
    )

/**
 * Implement this class to build a WebComponent.
 */
@JsName("WebComponent")
abstract class WebComponent<E : Element>(observeAttributes: Boolean = true) {
    /**
     * this method builds the content of the WebComponent that will be added to it's shadow-DOM.
     * @param element the newly created element, when the component is used
     * @param shadowRoot the shadowRoot the content will be added to
     * @return a [Tag] representing the content of the component
     */
    abstract fun RenderContext.init(element: HTMLElement, shadowRoot: ShadowRoot): Tag<E>

    @JsName("initializeInternal")
    fun initializeInternal(element: HTMLElement, shadowRoot: ShadowRoot): Tag<E> {
        return object : RenderContext {
            override val job = Job()
            override val scope: Scope = Scope()
            override fun <N : Node, W : WithDomNode<N>> register(element: W, content: (W) -> Unit): W {
                content(element)
                return element
            }
        }.init(element, shadowRoot)
    }

    private val _attributeChanges: MutableSharedFlow<Pair<String, String>> =
        MutableSharedFlow(replay = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * this callback is used, when building the component in native-js (since ES2015-classes are not supported by Kotlin/JS by now)
     */
    //cannot be private or internal because it is used in native js
    @JsName("attributeChangedCallback")
    fun attributeChangedCallback(name: String, value: String) {
        _attributeChanges.tryEmit(Pair(name, value))
    }

    /**
     * a [Flow] of all changes made to observed attributes.
     */
    val attributeChanges: Flow<Pair<String, String>> = if (observeAttributes) {
        _attributeChanges.distinctUntilChanged()
    } else {
        flowOf()
    }

    /**
     * call this method in init to link an external stylesheet.
     *
     * @param shadowRoot the shadowRoot the link is added to
     * @param url the URL of your stylesheet
     */
    fun linkStylesheet(shadowRoot: ShadowRoot, url: String) {
        shadowRoot.ownerDocument?.let {
            shadowRoot.appendChild(it.createElement("link").unsafeCast<HTMLLinkElement>().apply {
                rel = "stylesheet"
                href = url
            })
        }
    }

    /**
     * call this method in add style information from a static [String].
     *
     * @param shadowRoot the shadowRoot the style is added to
     * @param text your css
     */
    fun setStylesheet(shadowRoot: ShadowRoot, text: String) {
        shadowRoot.ownerDocument?.let {
            shadowRoot.appendChild(it.createElement("style").unsafeCast<HTMLStyleElement>().apply {
                innerText = text
            })
        }
    }

    /**
     * convenience method to get a [Flow] of the changes of a specific observed attributes.
     *
     * @param name of the observed attribute
     */
    fun attributeChanges(name: String) = attributeChanges.filter { (n, _) -> n == name }.map { (_, value) -> value }

    /**
     * lifecycle-callback that is called, when an instance of the component is added to the DOM
     *
     * @param element of the instance
     */
    @JsName("connectedCallback")
    open fun connectedCallback(element: HTMLElement) {
    }

    /**
     * lifecycle-callback that is called, when an instance of the component is removed from the DOM
     *
     * @param element of the instance
     */
    @JsName("disconnectedCallback")
    open fun disconnectedCallback(element: HTMLElement) {
    }

    /**
     * lifecycle-callback that is called, when an instance is adopted by another DOM
     *
     * @param element of the instance
     */
    @JsName("adoptedCallback")
    open fun adoptedCallback(element: HTMLElement) {
    }
}

/**
 * registers a [WebComponent] at the browser's registry, so you can use it in fritz2 by custom-[Tag] or in HTML.
 * So to make a component that can be added by just importing your javascript, call this in main.
 *
 *  @param localName name of the new custom tag (must contain a '-')
 *  @param webComponent instance of a [WebComponent]
 *  @param observedAttributes attributes to be observed, changes will occur on [WebComponent.attributeChanges]
 */
fun <X : Element, T : WebComponent<X>> registerWebComponent(
    localName: String,
    webComponent: T,
    vararg observedAttributes: String
) {
    registerWebComponent(localName, webComponent::class, *observedAttributes)
}

/**
 * registers a [WebComponent] at the browser's registry, so you can use it in fritz2 by custom-[Tag] or in HTML.
 * So to make a component that can be added by just importing your javascript, call this in main.
 *
 *  @param localName name of the new custom tag (must contain a '-')
 *  @param constructor class describing the component to register implementing [WebComponent]
 *  @param observedAttributes attributes to be observed, changes will occur on [WebComponent.attributeChanges]
 */
fun <E : Element, T : WebComponent<E>> registerWebComponent(
    localName: String,
    constructor: KClass<T>,
    vararg observedAttributes: String
) {
    val customElementConstructor = createClass<E, T>()(constructor.js, observedAttributes)
    window.customElements.define(localName, customElementConstructor)
}
