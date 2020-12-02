[core](../../index.md) / [dev.fritz2.format](../index.md) / [Format](./index.md)

# Format

(js, jvm) `interface Format<T>`

[parse](parse.md#dev.fritz2.format.Format$parse(kotlin.String))s and [format](format.md#dev.fritz2.format.Format$format(dev.fritz2.format.Format.T))s the given
[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) value from and to the target type.

### Functions

| (js, jvm) [format](format.md) | `abstract fun format(value: T): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| (js, jvm) [parse](parse.md) | `abstract fun parse(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): T` |

