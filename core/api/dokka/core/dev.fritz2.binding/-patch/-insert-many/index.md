[core](../../../index.md) / [dev.fritz2.binding](../../index.md) / [Patch](../index.md) / [InsertMany](./index.md)

# InsertMany

(js) `data class InsertMany<T> : `[`Patch`](../index.md)`<T>`

A [Patch](../index.md) saying, that a several element have been inserted

### Parameters

`elements` - the new elements that have been inserted

`index` - the elements have been inserted at this index

### Constructors

| (js) [&lt;init&gt;](-init-.md) | A [Patch](../index.md) saying, that a several element have been inserted`InsertMany(elements: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)` |

### Properties

| (js) [elements](elements.md) | the new elements that have been inserted`val elements: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>` |
| (js) [index](--index--.md) | the elements have been inserted at this index`val index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| (js) [map](map.md) | maps each of the new elements`fun <R> map(mapping: (T) -> R): `[`Patch`](../index.md)`<R>` |

