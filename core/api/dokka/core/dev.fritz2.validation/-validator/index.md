[core](../../index.md) / [dev.fritz2.validation](../index.md) / [Validator](./index.md)

# Validator

(js, jvm) `abstract class Validator<D, M : `[`ValidationMessage`](../-validation-message/index.md)`, T>`

Implement this interface to describe, how a certain data-model should be validated.

### Constructors

| (js, jvm) [&lt;init&gt;](-init-.md) | Implement this interface to describe, how a certain data-model should be validated.`<init>()` |

### Properties

| (js) [isValid](is-valid.md) | A [Flow](#) representing the current state of the model (valid or not). Use this to easily make actions possible of not depending on the validity of your model (save-button, etc.)`val isValid: Flow<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| (js) [msgs](msgs.md) | The [Flow](#) of messages (validation-results). For each run of the [Validation](../-validation/index.md) a new values appears on this [Flow](#). If no messages result from a run, the new value is an empty list.`val msgs: Flow<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<M>>` |

### Functions

| (js, jvm) [validate](validate.md) | method that has to be implemented to describe the validation-rules`abstract fun validate(data: D, metadata: T): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<M>` |

