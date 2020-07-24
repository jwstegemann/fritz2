package dev.fritz2.validation

/**
 * Implement this interface to describe, how a certain data-model should be validated.
 */
actual abstract class Validator<D, M : ValidationMessage, T> actual constructor() {

    actual abstract fun validate(data: D, metadata: T): List<M>

    /**
     * A [Boolean] representing the current state of the model (valid or not).
     */
    fun isValid(data: D, metadata: T): Boolean = validate(data, metadata).none(ValidationMessage::failed)
}