[core](../../index.md) / [dev.fritz2.dom](../index.md) / [Tag](index.md) / [bind](./bind.md)

# bind

(js) `fun <X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> Flow<`[`Tag`](index.md)`<X>>.bind(): `[`SingleMountPoint`](../../dev.fritz2.binding/-single-mount-point/index.md)`<`[`WithDomNode`](../-with-dom-node/index.md)`<`[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`>>`

binds a [Flow](#) of [Tag](index.md)s at this position (creates a [DomMountPoint](../-dom-mount-point/index.md) as a placeholder and adds it to the builder)

(js) `fun <X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> `[`Seq`](../../dev.fritz2.binding/-seq/index.md)`<`[`Tag`](index.md)`<X>>.bind(): `[`MultiMountPoint`](../../dev.fritz2.binding/-multi-mount-point/index.md)`<`[`WithDomNode`](../-with-dom-node/index.md)`<`[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`>>`

binds a [Seq](../../dev.fritz2.binding/-seq/index.md) of [Tag](index.md)s at this position (creates a [DomMultiMountPoint](../-dom-multi-mount-point/index.md) as a placeholder and adds it to the builder)

