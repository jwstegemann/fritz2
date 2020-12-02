[core](../../index.md) / [dev.fritz2.dom](../index.md) / [DomMountPoint](./index.md)

# DomMountPoint

(js) `class DomMountPoint<T : `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`> : `[`SingleMountPoint`](../../dev.fritz2.binding/-single-mount-point/index.md)`<`[`WithDomNode`](../-with-dom-node/index.md)`<T>>`

A [SingleMountPoint](../../dev.fritz2.binding/-single-mount-point/index.md) to mount the values of a [Flow](#) of [WithDomNode](../-with-dom-node/index.md)s (mostly [Tag](../-tag/index.md)s) at this point in the DOM.

### Parameters

`upstream` - the Flow of [WithDomNode](../-with-dom-node/index.md)s to mount here.

### Constructors

| (js) [&lt;init&gt;](-init-.md) | A [SingleMountPoint](../../dev.fritz2.binding/-single-mount-point/index.md) to mount the values of a [Flow](#) of [WithDomNode](../-with-dom-node/index.md)s (mostly [Tag](../-tag/index.md)s) at this point in the DOM.`DomMountPoint(upstream: Flow<`[`WithDomNode`](../-with-dom-node/index.md)`<T>>, target: `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`?)` |

### Properties

| (js) [target](target.md) | `val target: `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`?` |

### Functions

| (js) [set](set.md) | updates the elements in the DOM`fun set(value: `[`WithDomNode`](../-with-dom-node/index.md)`<T>, last: `[`WithDomNode`](../-with-dom-node/index.md)`<T>?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

