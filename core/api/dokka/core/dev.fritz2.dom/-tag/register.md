[core](../../index.md) / [dev.fritz2.dom](../index.md) / [Tag](index.md) / [register](./register.md)

# register

(js) `open fun <X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`, T : `[`Tag`](index.md)`<X>> register(element: T, content: (T) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): T`

creates the content of the [Tag](index.md) and appends it as a child to the wrapped [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)

### Parameters

`element` - the parent element of the new content

`content` - lamda building the content (following the type-safe-builder pattern)