[core](../../index.md) / [dev.fritz2.identification](../index.md) / [ModelIdSub](./index.md)

# ModelIdSub

(js, jvm) `class ModelIdSub<R, P, T> : `[`ModelId`](../-model-id/index.md)`<T>`

Same as [SubStore](#) but just for ids. Use it in validation for example.

### Constructors

| (js, jvm) [&lt;init&gt;](-init-.md) | Same as [SubStore](#) but just for ids. Use it in validation for example.`<init>(parent: `[`ModelId`](../-model-id/index.md)`<P>, lens: `[`Lens`](../../dev.fritz2.lenses/-lens/index.md)`<P, T>, rootStore: `[`ModelIdRoot`](../-model-id-root/index.md)`<R>, rootLens: `[`Lens`](../../dev.fritz2.lenses/-lens/index.md)`<R, T>)` |

### Properties

| (js, jvm) [id](id.md) | defines how the id of a part is derived from the one of it's parent`val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| (js, jvm) [rootLens](root-lens.md) | `val rootLens: `[`Lens`](../../dev.fritz2.lenses/-lens/index.md)`<R, T>` |
| (js, jvm) [rootStore](root-store.md) | `val rootStore: `[`ModelIdRoot`](../-model-id-root/index.md)`<R>` |

### Functions

| (js, jvm) [sub](sub.md) | method to create a [ModelId](../-model-id/index.md#dev.fritz2.identification.ModelId) for a part of your data-model`fun <X> sub(lens: `[`Lens`](../../dev.fritz2.lenses/-lens/index.md)`<T, X>): `[`ModelIdSub`](./index.md)`<R, T, X>` |

