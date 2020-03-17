[fritz2](../../index.md) / [io.fritz2.dom](../index.md) / [Listener](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Listener(actions: Flow<`[`Action`](../-action/index.md)`<E, X>>)`

[Listener](index.md) handles a Flow of [Action](../-action/index.md)s and gives
the [Event](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html) with [events](events.md) as Flow or
the targeting [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html) with [targets](targets.md) also as Flow back.
If you don't need either the [Event](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom.events/-event/index.html) or [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html) you can call the [Listener](index.md)
directly (e.g. `clicks()`) to get an Flow of [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) instead.

