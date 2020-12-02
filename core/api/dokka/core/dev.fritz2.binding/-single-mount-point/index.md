[core](../../index.md) / [dev.fritz2.binding](../index.md) / [SingleMountPoint](./index.md)

# SingleMountPoint

(js) `abstract class SingleMountPoint<T> : CoroutineScope`

A [SingleMountPoint](./index.md) collects the values of a given [Flow](#) one by one. Use this for data-types that represent a single (simple or complex) value.

### Parameters

`upstream` - the Flow that should be mounted at this point.

### Constructors

| (js) [&lt;init&gt;](-init-.md) | A [SingleMountPoint](./index.md) collects the values of a given [Flow](#) one by one. Use this for data-types that represent a single (simple or complex) value.`SingleMountPoint(upstream: Flow<T>)` |

### Functions

| (js) [set](set.md) | this method is called for each new value on the upstream-[Flow](#)`abstract fun set(value: T, last: T?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| (js) [AttributeMountPoint](../../dev.fritz2.dom/-attribute-mount-point/index.md) | a [SingleMountPoint](./index.md) to mount the values of a [Flow](#) to a DOM-attribute.`class AttributeMountPoint : `[`SingleMountPoint`](./index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| (js) [DomMountPoint](../../dev.fritz2.dom/-dom-mount-point/index.md) | A [SingleMountPoint](./index.md) to mount the values of a [Flow](#) of [WithDomNode](../../dev.fritz2.dom/-with-dom-node/index.md)s (mostly [Tag](../../dev.fritz2.dom/-tag/index.md)s) at this point in the DOM.`class DomMountPoint<T : `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`> : `[`SingleMountPoint`](./index.md)`<`[`WithDomNode`](../../dev.fritz2.dom/-with-dom-node/index.md)`<T>>` |
| (js) [ValueAttributeMountPoint](../../dev.fritz2.dom/-value-attribute-mount-point/index.md) | [ValueAttributeDelegate](#) is a special [SingleMountPoint](./index.md) for the html value attribute without calling `setAttribute` method.`class ValueAttributeMountPoint : `[`SingleMountPoint`](./index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

