[fritz2](../index.md) / [io.fritz2.dom](./index.md)

## Package io.fritz2.dom

### Types

| [AttributeDelegate](-attribute-delegate/index.md) | `object AttributeDelegate` |
| [AttributeMountPoint](-attribute-mount-point/index.md) | `class AttributeMountPoint : `[`SingleMountPoint`](../io.fritz2.binding/-single-mount-point/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [DomMountPoint](-dom-mount-point/index.md) | `class DomMountPoint<T : `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`> : `[`SingleMountPoint`](../io.fritz2.binding/-single-mount-point/index.md)`<`[`WithDomNode`](-with-dom-node/index.md)`<T>>` |
| [DomMultiMountPoint](-dom-multi-mount-point/index.md) | `class DomMultiMountPoint<T : `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`> : `[`MultiMountPoint`](../io.fritz2.binding/-multi-mount-point/index.md)`<`[`WithDomNode`](-with-dom-node/index.md)`<T>>` |
| [Tag](-tag/index.md) | `abstract class Tag<out T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](-with-dom-node/index.md)`<T>, `[`WithAttributes`](-with-attributes/index.md)`<T>, `[`WithEvents`](-with-events/index.md)`<T>, `[`HtmlElements`](../io.fritz2.dom.html/-html-elements/index.md) |
| [TextNode](-text-node/index.md) | `class TextNode : `[`WithDomNode`](-with-dom-node/index.md)`<`[`Text`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-text/index.html)`>` |
| [WithAttributes](-with-attributes/index.md) | `interface WithAttributes<out T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](-with-dom-node/index.md)`<T>` |
| [WithDomNode](-with-dom-node/index.md) | `interface WithDomNode<out T : `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`>` |
| [WithEvents](-with-events/index.md) | `abstract class WithEvents<out T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](-with-dom-node/index.md)`<T>` |
| [WithText](-with-text/index.md) | `interface WithText<T : `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`> : `[`WithDomNode`](-with-dom-node/index.md)`<T>` |

### Annotations

| [HtmlTagMarker](-html-tag-marker/index.md) | `annotation class HtmlTagMarker` |

### Extensions for External Classes

| [kotlinx.coroutines.flow.Flow](kotlinx.coroutines.flow.-flow/index.md) |  |
| [org.w3c.dom.Element](org.w3c.dom.-element/index.md) |  |

### Functions

| [mount](mount.md) | `fun <X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> `[`Tag`](-tag/index.md)`<X>.mount(targetId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

