[fritz2](../../index.md) / [io.fritz2.binding](../index.md) / [Validator](./index.md)

# Validator

`@FlowPreview @ExperimentalCoroutinesApi abstract class Validator<D, M : `[`Failable`](../-failable/index.md)`, T>`

### Constructors

| [&lt;init&gt;](-init-.md) | `Validator()` |

### Properties

| [isValid](is-valid.md) | `val isValid: Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [msgs](msgs.md) | `val msgs: Flow<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<M>>` |

### Functions

| [validate](validate.md) | `abstract fun validate(data: D, metadata: T): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<M>` |

