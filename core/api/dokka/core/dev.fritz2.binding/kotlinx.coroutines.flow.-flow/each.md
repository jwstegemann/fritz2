[core](../../index.md) / [dev.fritz2.binding](../index.md) / [kotlinx.coroutines.flow.Flow](index.md) / [each](./each.md)

# each

(js) `fun <T : `[`WithId`](../../dev.fritz2.lenses/-with-id/index.md)`> Flow<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.each(): `[`Seq`](../-seq/index.md)`<T>`

factory method to create a [Seq](../-seq/index.md) from a [Flow](#) of a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of a type implementing [WithId](../../dev.fritz2.lenses/-with-id/index.md#dev.fritz2.lenses.WithId)
Call it for example on the data-[Flow](#) of your (Sub-)Store.
The [Patch](../-patch/index.md)es are determined using Myer's diff-algorithm. Elements with the same id are considered the same element.
This allows the detection os moves.
Keep in mind, that no [Patch](../-patch/index.md) is derived, when an element stays the same, but changes it's internal values.

