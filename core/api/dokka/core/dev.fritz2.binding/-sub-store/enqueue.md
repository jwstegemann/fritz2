[core](../../index.md) / [dev.fritz2.binding](../index.md) / [SubStore](index.md) / [enqueue](./enqueue.md)

# enqueue

(js) `suspend fun enqueue(update: `[`Update`](../-update.md)`<T>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Since a [SubStore](index.md) is just a view on a [RootStore](../-root-store/index.md) holding the real value, it forwards the [Update](../-update.md) to it, using it's [Lens](../../dev.fritz2.lenses/-lens/index.md#dev.fritz2.lenses.Lens) to transform it.

