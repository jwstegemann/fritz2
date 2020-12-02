[core](../index.md) / [dev.fritz2.validation](./index.md)

## Package dev.fritz2.validation

### Types

| (js) [Validation](-validation/index.md) | An interface that can be inherited into a [Store](#) that allows to easily use the validation in your [Handler](#)s and templates.`interface Validation<D, M : `[`ValidationMessage`](-validation-message/index.md)`, T>` |
| (js, jvm) [ValidationMessage](-validation-message/index.md) | Minimal interface that has to be implemented and contains the message from validation process.`interface ValidationMessage` |
| (js, jvm) [Validator](-validator/index.md) | Implement this interface to describe, how a certain data-model should be validated.`abstract class Validator<D, M : `[`ValidationMessage`](-validation-message/index.md)`, T>` |

