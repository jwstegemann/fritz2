[core](../../index.md) / [dev.fritz2.dom](../index.md) / [WithAttributes](index.md) / [bindAttr](./bind-attr.md)

# bindAttr

(js) `open fun Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>.bindAttr(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`AttributeMountPoint`](../-attribute-mount-point/index.md)

bind a [Flow](#) to an attribute. The attribute will be updated in the DOM whenever a new value appears on this [Flow](#).

### Parameters

`name` - of the attribute

**Receiver**
the [Flow](#) to bind

(js) `open fun Flow<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>.bindAttr(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`AttributeMountPoint`](../-attribute-mount-point/index.md)

bind a [Flow](#) of [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)s to an attribute. The attribute will be updated in the DOM whenever a new value appears on this [Flow](#).
The elements of the list will be concatenated by empty space.

### Parameters

`name` - of the attribute

**Receiver**
the [Flow](#) to bind

(js) `open fun Flow<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>>.bindAttr(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`AttributeMountPoint`](../-attribute-mount-point/index.md)

bind a [Flow](#) of [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)s to an attribute. The attribute will be updated in the DOM whenever a new value appears on this [Flow](#).
The value will be set to the keys of the map that are mapped to true separated by empty space.
Use it to build dynamic lists of style-classes, i.e.

### Parameters

`name` - of the attribute

**Receiver**
the [Flow](#) to bind

