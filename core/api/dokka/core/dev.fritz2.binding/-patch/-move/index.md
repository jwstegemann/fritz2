[core](../../../index.md) / [dev.fritz2.binding](../../index.md) / [Patch](../index.md) / [Move](./index.md)

# Move

(js) `data class Move<T> : `[`Patch`](../index.md)`<T>`

A [Patch](../index.md) saying, that an element has been moved from one position to another. This is only used on [Seq](../../-seq/index.md) of types implementing [WithId](../../../dev.fritz2.lenses/-with-id/index.md#dev.fritz2.lenses.WithId).

### Parameters

`from` - old index of the element

`to` - new index of the element

### Constructors

| (js) [&lt;init&gt;](-init-.md) | A [Patch](../index.md) saying, that an element has been moved from one position to another. This is only used on [Seq](../../-seq/index.md) of types implementing [WithId](../../../dev.fritz2.lenses/-with-id/index.md#dev.fritz2.lenses.WithId).`Move(from: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, to: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)` |

### Properties

| (js) [from](from.md) | old index of the element`val from: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| (js) [to](to.md) | new index of the element`val to: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| (js) [map](map.md) | nothing to be mapped here...`fun <R> map(mapping: (T) -> R): `[`Patch`](../index.md)`<R>` |

