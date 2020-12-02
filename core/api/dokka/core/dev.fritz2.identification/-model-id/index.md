[core](../../index.md) / [dev.fritz2.identification](../index.md) / [ModelId](./index.md)

# ModelId

(js, jvm) `interface ModelId<T>`

represents the id of certain element in a deep nested model structure.

### Properties

| (js, jvm) [id](id.md) | [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) representation of the id`abstract val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| (js, jvm) [sub](sub.md) | method to create a [ModelId](index.md#dev.fritz2.identification.ModelId) for a part of your data-model`abstract fun <X> sub(lens: `[`Lens`](../../dev.fritz2.lenses/-lens/index.md)`<T, X>): `[`ModelId`](./index.md)`<X>` |

### Inheritors

| (js, jvm) [ModelIdRoot](../-model-id-root/index.md) | Starting point for creating your [ModelId](index.md#dev.fritz2.identification.ModelId)s. Same as [RootStore](#) but just for ids. Use it in validation for example.`class ModelIdRoot<T> : `[`ModelId`](./index.md)`<T>` |
| (js, jvm) [ModelIdSub](../-model-id-sub/index.md) | Same as [SubStore](#) but just for ids. Use it in validation for example.`class ModelIdSub<R, P, T> : `[`ModelId`](./index.md)`<T>` |

