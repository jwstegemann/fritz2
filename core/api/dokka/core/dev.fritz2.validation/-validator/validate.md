[core](../../index.md) / [dev.fritz2.validation](../index.md) / [Validator](index.md) / [validate](./validate.md)

# validate

(js, jvm) `abstract fun validate(data: D, metadata: T): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<M>`

method that has to be implemented to describe the validation-rules

### Parameters

`data` - model-instance to be validated

`metadata` - some data to be used as parameters for the validation (validate differently for the steps in a process)

**Return**
a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of messages (your result-type implementing [ValidationMessage](../-validation-message/index.md#dev.fritz2.validation.ValidationMessage))

