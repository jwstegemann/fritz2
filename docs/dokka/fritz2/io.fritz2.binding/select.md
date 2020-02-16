[fritz2](../index.md) / [io.fritz2.binding](index.md) / [select](./select.md)

# select

`@FlowPreview @ExperimentalCoroutinesApi fun <X> `[`Router`](-router/index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>.select(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, mapper: (`[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>) -> X): Flow<X>`

Select return a [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html) of the value
and the complete routing [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) for the given key in the [mapper](select.md#io.fritz2.binding$select(io.fritz2.binding.Router((kotlin.collections.Map((kotlin.String, )))), kotlin.String, kotlin.Function1((kotlin.Pair((kotlin.String, kotlin.collections.Map((, )))), io.fritz2.binding.select.X)))/mapper) function.

