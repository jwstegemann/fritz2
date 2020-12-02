[core](../../index.md) / [dev.fritz2.binding](../index.md) / [RootStore](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

(js) `RootStore(initialData: T, id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", bufferSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 1)`

A [Store](../-store/index.md) can be initialized with a given value. Use a [RootStore](index.md) to "store" your model and create [SubStore](../-sub-store/index.md)s from here.

### Parameters

`initialData` - : the first current value of this [Store](../-store/index.md)

`id` - : the id of this store. ids of [SubStore](../-sub-store/index.md)s will be concatenated.

`bufferSize` - : number of values to buffer