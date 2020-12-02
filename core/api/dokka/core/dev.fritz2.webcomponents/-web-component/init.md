[core](../../index.md) / [dev.fritz2.webcomponents](../index.md) / [WebComponent](index.md) / [init](./init.md)

# init

(js) `abstract fun init(element: `[`HTMLElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-element/index.html)`, shadowRoot: `[`ShadowRoot`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-shadow-root/index.html)`): `[`Tag`](../../dev.fritz2.dom/-tag/index.md)`<T>`

this method builds the content of the WebComponent that will be added to it's shadow-DOM.

### Parameters

`element` - the newly created element, when the component is used

`shadowRoot` - the shadowRoot the content will be added to

**Return**
a [Tag](../../dev.fritz2.dom/-tag/index.md) representing the content of the component

