[core](../../../index.md) / [dev.fritz2.binding](../../index.md) / [Patch](../index.md) / [Insert](./index.md)

# Insert

(js) `data class Insert<T> : `[`Patch`](../index.md)`<T>`

A [Patch](../index.md) saying, that a new element has been inserted

### Parameters

`element` - the new element that has been inserted

`index` - the element has been inserted at this index

### Constructors

| (js) [&lt;init&gt;](-init-.md) | A [Patch](../index.md) saying, that a new element has been inserted`Insert(element: T, index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)` |

### Properties

| (js) [element](element.md) | the new element that has been inserted`val element: T` |
| (js) [index](--index--.md) | the element has been inserted at this index`val index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| (js) [map](map.md) | maps the new element`fun <R> map(mapping: (T) -> R): `[`Patch`](../index.md)`<R>` |

