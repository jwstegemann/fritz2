[core](../../index.md) / [dev.fritz2.dom](../index.md) / [AttributeMountPoint](./index.md)

# AttributeMountPoint

(js) `class AttributeMountPoint : `[`SingleMountPoint`](../../dev.fritz2.binding/-single-mount-point/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

a [SingleMountPoint](../../dev.fritz2.binding/-single-mount-point/index.md) to mount the values of a [Flow](#) to a DOM-attribute.

### Parameters

`name` - of the attribute

`upstream` - [Flow](#) to mount to the attribute

`target` - the element where to set the attribute

### Constructors

| (js) [&lt;init&gt;](-init-.md) | a [SingleMountPoint](../../dev.fritz2.binding/-single-mount-point/index.md) to mount the values of a [Flow](#) to a DOM-attribute.`AttributeMountPoint(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, upstream: Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, target: `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`?)` |

### Properties

| (js) [name](name.md) | of the attribute`val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| (js) [target](target.md) | the element where to set the attribute`val target: `[`Element`](https://kotlinlang.org/api/latest/jvm/stdlib/org.w3c.dom/-element/index.html)`?` |

### Functions

| (js) [set](set.md) | updates the attribute-value in the DOM`fun set(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, last: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

