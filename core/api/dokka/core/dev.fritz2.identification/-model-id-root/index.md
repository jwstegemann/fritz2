[core](../../index.md) / [dev.fritz2.identification](../index.md) / [ModelIdRoot](./index.md)

# ModelIdRoot

(js, jvm) `class ModelIdRoot<T> : `[`ModelId`](../-model-id/index.md)`<T>`

Starting point for creating your [ModelId](../-model-id/index.md#dev.fritz2.identification.ModelId)s. Same as [RootStore](#) but just for ids. Use it in validation for example.

### Constructors

| (js, jvm) [&lt;init&gt;](-init-.md) | Starting point for creating your [ModelId](../-model-id/index.md#dev.fritz2.identification.ModelId)s. Same as [RootStore](#) but just for ids. Use it in validation for example.`<init>(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "")` |

### Properties

| (js, jvm) [id](id.md) | [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) representation of the id`val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| (js, jvm) [sub](sub.md) | method to create a [ModelId](../-model-id/index.md#dev.fritz2.identification.ModelId) for a part of your data-model`fun <X> sub(lens: `[`Lens`](../../dev.fritz2.lenses/-lens/index.md)`<T, X>): `[`ModelId`](../-model-id/index.md)`<X>` |

