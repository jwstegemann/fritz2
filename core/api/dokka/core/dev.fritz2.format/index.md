[core](../index.md) / [dev.fritz2.format](./index.md)

## Package dev.fritz2.format

### Types

| (js, jvm) [Format](-format/index.md) | [parse](-format/parse.md#dev.fritz2.format.Format$parse(kotlin.String))s and [format](-format/format.md#dev.fritz2.format.Format$format(dev.fritz2.format.Format.T))s the given [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) value from and to the target type.`interface Format<T>` |
| (js) [FormatStore](-format-store/index.md) | A [Store](../dev.fritz2.binding/-store/index.md) representing the formatted value of it's parent [Store](../dev.fritz2.binding/-store/index.md). Use this to transparently bind a Date, an Int or some other data-type in your model to an HTML-input (that can only handle [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)s). Do not create an instance by yourself. Use the factory-method at [SubStore](#)`class FormatStore<R, P> : `[`Store`](../dev.fritz2.binding/-store/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

