[core](../index.md) / [dev.fritz2.dom](index.md) / [append](./append.md)

# append

(js) `fun <X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> append(targetId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg flows: Flow<`[`Tag`](-tag/index.md)`<X>>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

appends one or more [Flow](#)s of [Tag](-tag/index.md)s to the content of a constant element

### Parameters

`targetId` - id of the element to mount to

`flows` - the [Flow](#)s to mount to this element(js) `fun <X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> append(targetId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg tags: `[`Tag`](-tag/index.md)`<X>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

appends one or more static [Tag](-tag/index.md)s to an elements content

### Parameters

`targetId` - id of the element to mount to