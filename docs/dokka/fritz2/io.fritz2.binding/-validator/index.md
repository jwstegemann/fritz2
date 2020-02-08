[fritz2](../../index.md) / [io.fritz2.binding](../index.md) / [Validator](./index.md)

# Validator

`@FlowPreview @ExperimentalCoroutinesApi abstract class Validator<D, M : `[`WithSeverity`](../-with-severity/index.md)`, T>`

### Constructors

| [&lt;init&gt;](-init-.md) | `Validator()` |

### Properties

| [msgs](msgs.md) | `val msgs: Flow<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<M>>` |

### Functions

| [isValid](is-valid.md) | `fun isValid(msg: M): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>`fun isValid(): Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [validate](validate.md) | `abstract fun validate(data: D, metadata: T): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<M>` |

