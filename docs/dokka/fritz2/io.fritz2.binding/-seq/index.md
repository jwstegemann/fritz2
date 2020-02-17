[fritz2](../../index.md) / [io.fritz2.binding](../index.md) / [Seq](./index.md)

# Seq

`class Seq<T>`

### Constructors

| [&lt;init&gt;](-init-.md) | `Seq(data: Flow<`[`Patch`](../-patch/index.md)`<T>>)` |

### Properties

| [data](data.md) | `val data: Flow<`[`Patch`](../-patch/index.md)`<T>>` |

### Functions

| [filter](filter.md) | `fun filter(filter: (T) -> `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Seq`](./index.md)`<T>` |
| [flatMap](flat-map.md) | `fun <X> flatMap(mapper: (T) -> `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<X>): `[`Seq`](./index.md)`<X>` |
| [map](map.md) | `fun <X> map(mapper: (T) -> X): `[`Seq`](./index.md)`<X>` |

