[core](../index.md) / [dev.fritz2.routing](index.md) / [select](./select.md)

# select

(js) `fun <X> `[`Router`](-router/index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>.select(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, mapper: (`[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>) -> X): Flow<X>`

Select return a [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html) of the value
and the complete routing [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) for the given key in the [mapper](select.md#dev.fritz2.routing$select(dev.fritz2.routing.Router((kotlin.collections.Map((kotlin.String, kotlin.Any)))), kotlin.String, kotlin.Function1((kotlin.Pair((kotlin.Any, kotlin.collections.Map((kotlin.String, )))), dev.fritz2.routing.select.X)))/mapper) function.

