[core](../../index.md) / [dev.fritz2.dom](../index.md) / [WithDomNode](./index.md)

# WithDomNode

(js) `interface WithDomNode<out T : `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`>`

base-interface for everything that represents a node in the DOM.

### Properties

| (js) [domNode](dom-node.md) | `abstract val domNode: T` |

### Inheritors

| (js) [Tag](../-tag/index.md) | Represents a tag in the resulting HTML. Sorry for the name, but we needed to delimit it from the [Element](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html) it is wrapping.`open class Tag<out T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](./index.md)`<T>, `[`WithAttributes`](../-with-attributes/index.md)`<T>, `[`WithEvents`](../-with-events/index.md)`<T>, `[`HtmlElements`](../../dev.fritz2.dom.html/-html-elements/index.md) |
| (js) [TextNode](../-text-node/index.md) | Represents a DOM-TextNode`class TextNode : `[`WithDomNode`](./index.md)`<`[`Text`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-text/index.html)`>` |
| (js) [WithAttributes](../-with-attributes/index.md) | This interface allows instances of implementing classes to set and bind DOM-attributes.`interface WithAttributes<out T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](./index.md)`<T>` |
| (js) [WithEvents](../-with-events/index.md) | this interfaces offers [Listener](../-listener/index.md)s for all DOM-events available`abstract class WithEvents<out T : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> : `[`WithDomNode`](./index.md)`<T>` |
| (js) [WithText](../-with-text/index.md) | Interface providing functionality to handle text-content`interface WithText<T : `[`Node`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-node/index.html)`> : `[`WithDomNode`](./index.md)`<T>` |

