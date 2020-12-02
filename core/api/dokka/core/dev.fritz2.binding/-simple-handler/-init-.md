[core](../../index.md) / [dev.fritz2.binding](../index.md) / [SimpleHandler](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

(js) `SimpleHandler(inline execute: (Flow<A>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)`

A [SimpleHandler](index.md) defines, how to handle actions in your [Store](../-store/index.md). Each Handler accepts actions of a defined type.
If your handler just needs the current value of the [Store](../-store/index.md) and no action, use [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html).

### Parameters

`execute` - defines how to handle the values of the connected [Flow](#)