package dev.fritz2.services.validation

/**
 * Describes the logic for validating a given data-model.
 * By implementing this you must describe, how a certain data-model should be validated.
 * This is done by returning a [List] of [ValidationMessage]s in the [validate] functions.
 *
 * It is recommended to put the concrete implementation of this [Validator] right next to your data classes
 * in the `commonMain` section of your Kotlin multiplatform project.
 * Then you can write the validation logic once and use them in the JS and JVM world.
 */
actual abstract class Validator<D, M : ValidationMessage, T> actual constructor() {

    actual abstract fun validate(data: D, metadata: T): List<M>

    /**
     * evaluates the [List] of [ValidationMessage] to see if your [data] is valid or not
     *
     * @param data model to validate
     * @param metadata extra information for the validation process
     * @return a [Boolean] for using in if conditions
     */
    fun isValid(data: D, metadata: T): Boolean = validate(data, metadata).none(ValidationMessage::failed)
}