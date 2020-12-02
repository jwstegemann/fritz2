[core](../index.md) / [dev.fritz2.binding](index.md) / [eachStore](./each-store.md)

# eachStore

(js) `fun <T : `[`WithId`](../dev.fritz2.lenses/-with-id/index.md)`> `[`RootStore`](-root-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.eachStore(): `[`Seq`](-seq/index.md)`<`[`SubStore`](-sub-store/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>>`
`fun <R, P, T : `[`WithId`](../dev.fritz2.lenses/-with-id/index.md)`> `[`SubStore`](-sub-store/index.md)`<R, P, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>>.eachStore(): `[`Seq`](-seq/index.md)`<`[`SubStore`](-sub-store/index.md)`<R, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, T>>`

convenience-method to create a [Seq](-seq/index.md) of [SubStores](#), one for each element of the [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html).
You can also call [each](kotlinx.coroutines.flow.-flow/each.md) and inside it's lambda create the [SubStore](-sub-store/index.md) using [sub](sub.md).

