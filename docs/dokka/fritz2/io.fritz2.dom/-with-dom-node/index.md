[fritz2](../../index.md) / [io.fritz2.dom](../index.md) / [WithDomNode](./index.md)

# WithDomNode

`interface WithDomNode<out T : `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`>`

### Properties

| [domNode](dom-node.md) | `abstract val domNode: T` |

### Inheritors

| [Tag](../-tag/index.md) | `abstract class Tag<out T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](./index.md)`<T>, `[`WithAttributes`](../-with-attributes/index.md)`<T>, `[`WithEvents`](../-with-events/index.md)`<T>, `[`HtmlElements`](../../io.fritz2.dom.html/-html-elements/index.md) |
| [TextNode](../-text-node/index.md) | `class TextNode : `[`WithDomNode`](./index.md)`<`[`Text`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-text/index.html)`>` |
| [WithAttributes](../-with-attributes/index.md) | `interface WithAttributes<out T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](./index.md)`<T>` |
| [WithEvents](../-with-events/index.md) | `abstract class WithEvents<out T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](./index.md)`<T>` |
| [WithText](../-with-text/index.md) | `interface WithText<T : `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`> : `[`WithDomNode`](./index.md)`<T>` |

