[core](../../index.md) / [dev.fritz2.validation](../index.md) / [Validation](./index.md)

# Validation

(js) `interface Validation<D, M : `[`ValidationMessage`](../-validation-message/index.md)`, T>`

An interface that can be inherited into a [Store](#) that allows to easily use the validation in your [Handler](#)s and templates.

### Properties

| (js) [validator](validator.md) | the [Validator](../-validator/index.md#dev.fritz2.validation.Validator) to be used validating the current value of the [Store](#)`abstract val validator: `[`Validator`](../-validator/index.md)`<D, M, T>` |

### Functions

| (js) [msgs](msgs.md) | bind this [Flow](#) in your templates to show the result of the last validation`open fun msgs(): Flow<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<M>>` |
| (js) [validate](validate.md) | call this method from your [Handler](#)s to validate the current model and react to the result`open fun validate(data: D, metadata: T): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

