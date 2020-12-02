[core](../../index.md) / [dev.fritz2.dom](../index.md) / [DomMultiMountPoint](./index.md)

# DomMultiMountPoint

(js) `class DomMultiMountPoint<T : `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`> : `[`MultiMountPoint`](../../dev.fritz2.binding/-multi-mount-point/index.md)`<`[`WithDomNode`](../-with-dom-node/index.md)`<T>>`

A [MultiMountPoint](../../dev.fritz2.binding/-multi-mount-point/index.md) to mount the values of a [Flow](#) of [Patch](../../dev.fritz2.binding/-patch/index.md)es (mostly [Tag](../-tag/index.md)s) at this point in the DOM.

### Parameters

`upstream` - the Flow of [WithDomNode](../-with-dom-node/index.md)s to mount here.

### Constructors

| (js) [&lt;init&gt;](-init-.md) | A [MultiMountPoint](../../dev.fritz2.binding/-multi-mount-point/index.md) to mount the values of a [Flow](#) of [Patch](../../dev.fritz2.binding/-patch/index.md)es (mostly [Tag](../-tag/index.md)s) at this point in the DOM.`DomMultiMountPoint(upstream: Flow<`[`Patch`](../../dev.fritz2.binding/-patch/index.md)`<`[`WithDomNode`](../-with-dom-node/index.md)`<T>>>, target: `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`?)` |

### Properties

| (js) [target](target.md) | `val target: `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`?` |

### Functions

| (js) [patch](patch.md) | executes the patches on the DOM`fun patch(patch: `[`Patch`](../../dev.fritz2.binding/-patch/index.md)`<`[`WithDomNode`](../-with-dom-node/index.md)`<T>>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

