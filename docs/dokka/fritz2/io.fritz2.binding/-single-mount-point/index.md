[fritz2](../../index.md) / [io.fritz2.binding](../index.md) / [SingleMountPoint](./index.md)

# SingleMountPoint

`abstract class SingleMountPoint<T> : CoroutineScope`

### Constructors

| [&lt;init&gt;](-init-.md) | `SingleMountPoint(upstream: Flow<T>)` |

### Functions

| [set](set.md) | `abstract fun set(value: T, last: T?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| [AttributeMountPoint](../../io.fritz2.dom/-attribute-mount-point/index.md) | `class AttributeMountPoint : `[`SingleMountPoint`](./index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [DomMountPoint](../../io.fritz2.dom/-dom-mount-point/index.md) | `class DomMountPoint<T : `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`> : `[`SingleMountPoint`](./index.md)`<`[`WithDomNode`](../../io.fritz2.dom/-with-dom-node/index.md)`<T>>` |

