[core](../index.md) / [dev.fritz2.identification](./index.md)

## Package dev.fritz2.identification

### Types

| (js, jvm) [ModelId](-model-id/index.md) | represents the id of certain element in a deep nested model structure.`interface ModelId<T>` |
| (js, jvm) [ModelIdRoot](-model-id-root/index.md) | Starting point for creating your [ModelId](-model-id/index.md#dev.fritz2.identification.ModelId)s. Same as [RootStore](#) but just for ids. Use it in validation for example.`class ModelIdRoot<T> : `[`ModelId`](-model-id/index.md)`<T>` |
| (js, jvm) [ModelIdSub](-model-id-sub/index.md) | Same as [SubStore](#) but just for ids. Use it in validation for example.`class ModelIdSub<R, P, T> : `[`ModelId`](-model-id/index.md)`<T>` |

### Functions

| (js, jvm) [uniqueId](unique-id.md) | creates something like an UUID`fun uniqueId(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

