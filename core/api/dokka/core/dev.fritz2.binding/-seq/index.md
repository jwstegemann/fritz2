[core](../../index.md) / [dev.fritz2.binding](../index.md) / [Seq](./index.md)

# Seq

(js) `inline class Seq<T>`

Defines a sequence of values

### Parameters

`data` - the upstream-[Flow](#) defining the current state of the [Seq](./index.md) by [Patch](../-patch/index.md)es

### Constructors

| (js) [&lt;init&gt;](-init-.md) | Defines a sequence of values`Seq(data: Flow<`[`Patch`](../-patch/index.md)`<T>>)` |

### Properties

| (js) [data](data.md) | the upstream-[Flow](#) defining the current state of the [Seq](./index.md) by [Patch](../-patch/index.md)es`val data: Flow<`[`Patch`](../-patch/index.md)`<T>>` |

### Functions

| (js) [map](map.md) | convenience-method to easily map each value in the [Seq](./index.md)`fun <X> map(mapper: (T) -> X): `[`Seq`](./index.md)`<X>` |

