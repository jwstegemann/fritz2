[fritz2](../../index.md) / [io.fritz2.binding](../index.md) / [MultiMountPoint](./index.md)

# MultiMountPoint

`abstract class MultiMountPoint<T>`

### Constructors

| [&lt;init&gt;](-init-.md) | `MultiMountPoint(upstream: Flow<`[`Patch`](../-patch/index.md)`<T>>)` |

### Functions

| [patch](patch.md) | `abstract fun patch(patch: `[`Patch`](../-patch/index.md)`<T>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| [DomMultiMountPoint](../../io.fritz2.dom/-dom-multi-mount-point/index.md) | `class DomMultiMountPoint<T : `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`> : `[`MultiMountPoint`](./index.md)`<`[`WithDomNode`](../../io.fritz2.dom/-with-dom-node/index.md)`<T>>` |

