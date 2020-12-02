[core](../../index.md) / [dev.fritz2.format](../index.md) / [FormatStore](./index.md)

# FormatStore

(js) `class FormatStore<R, P> : `[`Store`](../../dev.fritz2.binding/-store/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

A [Store](../../dev.fritz2.binding/-store/index.md) representing the formatted value of it's parent [Store](../../dev.fritz2.binding/-store/index.md).
Use this to transparently bind a Date, an Int or some other data-type in your model to an HTML-input (that can only handle [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)s).
Do not create an instance by yourself. Use the factory-method at [SubStore](#)

### Parameters

`parent` - parent [Store](../../dev.fritz2.binding/-store/index.md)

`rootStore` - [RootStore](../../dev.fritz2.binding/-root-store/index.md) in this chain of [Store](../../dev.fritz2.binding/-store/index.md)s

`rootLens` - concatenated [Lens](../../dev.fritz2.lenses/-lens/index.md#dev.fritz2.lenses.Lens) pointing to the element in the [RootStore](../../dev.fritz2.binding/-root-store/index.md)s type representing the value of this [Store](../../dev.fritz2.binding/-store/index.md)

`format` - the [Format](../-format/index.md#dev.fritz2.format.Format) used to parse and format (serialize and deserialize) the value

### Constructors

| (js) [&lt;init&gt;](-init-.md) | A [Store](../../dev.fritz2.binding/-store/index.md) representing the formatted value of it's parent [Store](../../dev.fritz2.binding/-store/index.md). Use this to transparently bind a Date, an Int or some other data-type in your model to an HTML-input (that can only handle [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)s). Do not create an instance by yourself. Use the factory-method at [SubStore](#)`FormatStore(parent: `[`Store`](../../dev.fritz2.binding/-store/index.md)`<P>, rootStore: `[`RootStore`](../../dev.fritz2.binding/-root-store/index.md)`<R>, rootLens: `[`Lens`](../../dev.fritz2.lenses/-lens/index.md)`<R, P>, format: `[`Format`](../-format/index.md)`<P>)` |

### Properties

| (js) [data](data.md) | applies the given [Format](../-format/index.md#dev.fritz2.format.Format) to the data-[Flow](#) of the parent [Store](../../dev.fritz2.binding/-store/index.md)`val data: Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| (js) [id](id.md) | id of this [Store](../../dev.fritz2.binding/-store/index.md)`val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| (js) [enqueue](enqueue.md) | applies the given [Format](../-format/index.md#dev.fritz2.format.Format) before handling an [Update](../../dev.fritz2.binding/-update.md)`suspend fun enqueue(update: `[`Update`](../../dev.fritz2.binding/-update.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

