[core](../../index.md) / [dev.fritz2.webcomponents](../index.md) / [WebComponent](./index.md)

# WebComponent

(js) `@ExperimentalCoroutinesApi abstract class WebComponent<T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`>`

Implement this class to build a WebComponent.

### Constructors

| (js) [&lt;init&gt;](-init-.md) | Implement this class to build a WebComponent.`WebComponent(observeAttributes: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true)` |

### Properties

| (js) [attributeChangedCallback](attribute-changed-callback.md) | this callback is used, when building the component in native-js (since ES2015-classes are not supported by Kotlin/JS by now)`lateinit var attributeChangedCallback: (name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| (js) [attributeChanges](attribute-changes.md) | a [Flow](#) of all changes made to observed attributes.`val attributeChanges: Flow<`[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>` |

### Functions

| (js) [adoptedCallback](adopted-callback.md) | lifecycle-callback that is called, when an instance is adopted by another DOM`open fun adoptedCallback(element: `[`HTMLElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-element/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| (js) [attributeChanges](attribute-changes.md) | convenience method to get a [Flow](#) of the changes of a specific observed attributes.`fun attributeChanges(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| (js) [connectedCallback](connected-callback.md) | lifecycle-callback that is called, when an instance of the component is added to the DOM`open fun connectedCallback(element: `[`HTMLElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-element/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| (js) [disconnectedCallback](disconnected-callback.md) | lifecycle-callback that is called, when an instance of the component is removed from the DOM`open fun disconnectedCallback(element: `[`HTMLElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-element/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| (js) [init](init.md) | this method builds the content of the WebComponent that will be added to it's shadow-DOM.`abstract fun init(element: `[`HTMLElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-element/index.html)`, shadowRoot: `[`ShadowRoot`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-shadow-root/index.html)`): `[`Tag`](../../dev.fritz2.dom/-tag/index.md)`<T>` |
| (js) [linkStylesheet](link-stylesheet.md) | call this method in init to link an external stylesheet.`fun linkStylesheet(shadowRoot: `[`ShadowRoot`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-shadow-root/index.html)`, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| (js) [setStylesheet](set-stylesheet.md) | call this method in add style information from a static [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html).`fun setStylesheet(shadowRoot: `[`ShadowRoot`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-shadow-root/index.html)`, text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

