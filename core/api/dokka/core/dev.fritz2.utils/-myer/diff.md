[core](../../index.md) / [dev.fritz2.utils](../index.md) / [Myer](index.md) / [diff](./diff.md)

# diff

(js) `fun <T : `[`WithId`](../../dev.fritz2.lenses/-with-id/index.md)`> diff(oldList: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, newList: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>): Flow<`[`Patch`](../../dev.fritz2.binding/-patch/index.md)`<T>>`

diffs to versions of a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of a type implementing [WithId](../../dev.fritz2.lenses/-with-id/index.md#dev.fritz2.lenses.WithId)
The definition of an id to identify the same object in both [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)s makes it possible to detect, if an object is moved from one position to another.
Also this method does not emit a [Patch](../../dev.fritz2.binding/-patch/index.md) if values within an element change.

### Parameters

`oldList` - old version of the [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)

`newLis` - new version of the [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)

**Return**
a [Flow](#) of [Patch](../../dev.fritz2.binding/-patch/index.md)es needed to transform the old list into the new one

(js) `fun <T> diff(oldList: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>, newList: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<T>): Flow<`[`Patch`](../../dev.fritz2.binding/-patch/index.md)`<T>>`

diffs to versions of a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of a type not implementing [WithId](../../dev.fritz2.lenses/-with-id/index.md#dev.fritz2.lenses.WithId)

### Parameters

`oldList` - old version of the [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)

`newLis` - new version of the [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)

**Return**
a [Flow](#) of [Patch](../../dev.fritz2.binding/-patch/index.md)es needed to transform the old list into the new one

