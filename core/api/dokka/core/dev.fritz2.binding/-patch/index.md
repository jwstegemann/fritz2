[core](../../index.md) / [dev.fritz2.binding](../index.md) / [Patch](./index.md)

# Patch

(js) `sealed class Patch<out T>`

A [Patch](./index.md) describes the changes made to a [Seq](../-seq/index.md)

### Types

| (js) [Delete](-delete/index.md) | nothing to be mapped here...`data class Delete<T> : `[`Patch`](./index.md)`<T>` |
| (js) [Insert](-insert/index.md) | A [Patch](./index.md) saying, that a new element has been inserted`data class Insert<T> : `[`Patch`](./index.md)`<T>` |
| (js) [InsertMany](-insert-many/index.md) | A [Patch](./index.md) saying, that a several element have been inserted`data class InsertMany<T> : `[`Patch`](./index.md)`<T>` |
| (js) [Move](-move/index.md) | A [Patch](./index.md) saying, that an element has been moved from one position to another. This is only used on [Seq](../-seq/index.md) of types implementing [WithId](../../dev.fritz2.lenses/-with-id/index.md#dev.fritz2.lenses.WithId).`data class Move<T> : `[`Patch`](./index.md)`<T>` |

### Functions

| (js) [map](map.md) | a convenience-method, to map the values encapsulated in a [Patch](./index.md)`abstract fun <R> map(mapping: (T) -> R): `[`Patch`](./index.md)`<R>` |

