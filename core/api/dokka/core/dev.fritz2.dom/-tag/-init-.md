[core](../../index.md) / [dev.fritz2.dom](../index.md) / [Tag](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

(js) `Tag(tagName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, baseClass: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, domNode: T = createDomElement(tagName, id, baseClass).unsafeCast<T>())`

Represents a tag in the resulting HTML. Sorry for the name, but we needed to delimit it from the [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html) it is wrapping.

### Parameters

`tagName` - name of the tag. Used to create the corresponding [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)

`id` - the DOM-id of the element to be created

`baseClass` - a static base value for the class-attribute. All dynamic values for this attribute will be concatenated to this base-value.

`domNode` - the [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)-instance that is wrapped by this [Tag](index.md) (you should never have to pass this by yourself, just let it be created by the default)