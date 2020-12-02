[core](../../index.md) / [dev.fritz2.webcomponents](../index.md) / [WebComponent](index.md) / [attributeChanges](./attribute-changes.md)

# attributeChanges

(js) `val attributeChanges: Flow<`[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>`

a [Flow](#) of all changes made to observed attributes.

(js) `fun attributeChanges(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

convenience method to get a [Flow](#) of the changes of a specific observed attributes.

### Parameters

`name` - of the observed attribute