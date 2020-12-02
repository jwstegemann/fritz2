[core](../../index.md) / [dev.fritz2.binding](../index.md) / [MultiMountPoint](./index.md)

# MultiMountPoint

(js) `abstract class MultiMountPoint<T> : CoroutineScope`

A [MultiMountPoint](./index.md) collects the values of a given [Flow](#) one by one. Use this for data-types that represent a sequence of values.

### Parameters

`upstream` - the Flow that should be mounted at this point.

### Constructors

| (js) [&lt;init&gt;](-init-.md) | A [MultiMountPoint](./index.md) collects the values of a given [Flow](#) one by one. Use this for data-types that represent a sequence of values.`MultiMountPoint(upstream: Flow<`[`Patch`](../-patch/index.md)`<T>>)` |

### Functions

| (js) [patch](patch.md) | this method is called for each new value on the upstream-[Flow](#)`abstract fun patch(patch: `[`Patch`](../-patch/index.md)`<T>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| (js) [DomMultiMountPoint](../../dev.fritz2.dom/-dom-multi-mount-point/index.md) | A [MultiMountPoint](./index.md) to mount the values of a [Flow](#) of [Patch](../-patch/index.md)es (mostly [Tag](../../dev.fritz2.dom/-tag/index.md)s) at this point in the DOM.`class DomMultiMountPoint<T : `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`> : `[`MultiMountPoint`](./index.md)`<`[`WithDomNode`](../../dev.fritz2.dom/-with-dom-node/index.md)`<T>>` |

