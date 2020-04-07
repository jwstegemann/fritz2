[fritz2](../../index.md) / [io.fritz2.dom.html](../index.md) / [Meter](./index.md)

# Meter

`@ExperimentalCoroutinesApi @FlowPreview class Meter : `[`Tag`](../../io.fritz2.dom/-tag/index.md)`<`[`HTMLMeterElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-meter-element/index.html)`>, `[`WithText`](../../io.fritz2.dom/-with-text/index.md)`<`[`HTMLMeterElement`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-h-t-m-l-meter-element/index.html)`>`

Exposes the JavaScript [HTMLMeterElement](https://developer.mozilla.org/en/docs/Web/API/HTMLMeterElement) to Kotlin

### Constructors

| [&lt;init&gt;](-init-.md) | Exposes the JavaScript [HTMLMeterElement](https://developer.mozilla.org/en/docs/Web/API/HTMLMeterElement) to Kotlin`Meter(id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, baseClass: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null)` |

### Properties

| [high](high.md) | `var high: Flow<`[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>` |
| [low](low.md) | `var low: Flow<`[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>` |
| [max](max.md) | `var max: Flow<`[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>` |
| [min](min.md) | `var min: Flow<`[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>` |
| [optimum](optimum.md) | `var optimum: Flow<`[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>` |
| [value](value.md) | `var value: Flow<`[`Double`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)`>` |

### Extension Functions

| [mount](../../io.fritz2.dom/mount.md) | `fun <X : `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`> `[`Tag`](../../io.fritz2.dom/-tag/index.md)`<X>.mount(targetId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

