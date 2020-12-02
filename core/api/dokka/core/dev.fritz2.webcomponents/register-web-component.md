[core](../index.md) / [dev.fritz2.webcomponents](index.md) / [registerWebComponent](./register-web-component.md)

# registerWebComponent

(js) `fun <X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`, T : `[`WebComponent`](-web-component/index.md)`<X>> registerWebComponent(localName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, constructor: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>, vararg observedAttributes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

registers a [WebComponent](-web-component/index.md) at the browser's registry so you can use it in fritz2 by custom-[Tag](../dev.fritz2.dom/-tag/index.md) or in HTML.
So to make a component that can be added by just importing your javascript, call this in main.

### Parameters

`localName` - name of the new custom tag (must contain a '-')

`constructor` - class describing the component to register implementing [WebComponent](-web-component/index.md)

`observedAttributes` - attributes to be observed, changes will occur on [WebComponent.attributeChanges](-web-component/attribute-changes.md)