[fritz2](../../index.md) / [io.fritz2.binding](../index.md) / [MapRoute](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`MapRoute(default: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>)`

[MapRoute](index.md) marshals and unmarshals a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) to and from *window.location.hash*.
It is like using url parameters with pairs of key and value.
In the begin there is only a **#** instead of **?**.

### Parameters

`default` - [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) to use when no explicit *window.location.hash* was set before