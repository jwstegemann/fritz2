[fritz2](../../index.md) / [io.fritz2.validation](../index.md) / [Validation](./index.md)

# Validation

`@FlowPreview @ExperimentalCoroutinesApi interface Validation<D, M : `[`ValidationMessage`](../-validation-message/index.md)`, T>`

### Properties

| [validator](validator.md) | `abstract val validator: `[`Validator`](../-validator/index.md)`<D, M, T>` |

### Functions

| [msgs](msgs.md) | `open fun msgs(): Flow<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<M>>` |
| [validate](validate.md) | `open fun validate(data: D, context: T): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

