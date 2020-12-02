[core](../../index.md) / [dev.fritz2.dom.html](../index.md) / [IFrame](./index.md)

# IFrame

(js) `class IFrame : `[`Tag`](../../dev.fritz2.dom/-tag/index.md)`<`[`HTMLIFrameElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-i-frame-element/index.html)`>, `[`WithText`](../../dev.fritz2.dom/-with-text/index.md)`<`[`HTMLIFrameElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-i-frame-element/index.html)`>`

Exposes the JavaScript [HTMLIFrameElement](https://developer.mozilla.org/en/docs/Web/API/HTMLIFrameElement) to Kotlin

### Constructors

| (js) [&lt;init&gt;](-init-.md) | Exposes the JavaScript [HTMLIFrameElement](https://developer.mozilla.org/en/docs/Web/API/HTMLIFrameElement) to Kotlin`IFrame(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, baseClass: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)` |

### Properties

| (js) [allowFullscreen](allow-fullscreen.md) | `var allowFullscreen: Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| (js) [allowUserMedia](allow-user-media.md) | `var allowUserMedia: Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| (js) [height](height.md) | `var height: Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| (js) [name](name.md) | `var name: Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| (js) [referrerPolicy](referrer-policy.md) | `var referrerPolicy: Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| (js) [src](src.md) | `var src: Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| (js) [srcdoc](srcdoc.md) | `var srcdoc: Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| (js) [width](width.md) | `var width: Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

### Extension Functions

| (js) [mount](../../dev.fritz2.dom/mount.md) | mounts a static [Tag](../../dev.fritz2.dom/-tag/index.md) to an elements content`fun <X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> `[`Tag`](../../dev.fritz2.dom/-tag/index.md)`<X>.mount(targetId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

