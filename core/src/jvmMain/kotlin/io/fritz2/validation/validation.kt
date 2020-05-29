package io.fritz2.validation

/**
 * Implement this interface to describe, how a certain data-model should be validated.
 */
actual abstract class Validator<D, M : ValidationMessage, T> {
    actual abstract fun validate(data: D, metadata: T): List<M>
}