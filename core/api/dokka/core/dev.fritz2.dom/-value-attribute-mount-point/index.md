[core](../../index.md) / [dev.fritz2.dom](../index.md) / [ValueAttributeMountPoint](./index.md)

# ValueAttributeMountPoint

(js) `class ValueAttributeMountPoint : `[`SingleMountPoint`](../../dev.fritz2.binding/-single-mount-point/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

[ValueAttributeDelegate](#) is a special [SingleMountPoint](../../dev.fritz2.binding/-single-mount-point/index.md) for the html value
attribute without calling `setAttribute` method.

### Constructors

| (js) [&lt;init&gt;](-init-.md) | [ValueAttributeDelegate](#) is a special [SingleMountPoint](../../dev.fritz2.binding/-single-mount-point/index.md) for the html value attribute without calling `setAttribute` method.`ValueAttributeMountPoint(upstream: Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, target: `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`?)` |

### Properties

| (js) [target](target.md) | `val target: `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`?` |

### Functions

| (js) [set](set.md) | updates the attribute-value in the DOM`fun set(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, last: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

