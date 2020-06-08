package io.fritz2.validation


/**
 * Minimal interface that has to be implemented and contains the message from
 * validation process.
 */
interface ValidationMessage {
    fun failed(): Boolean
}


/**
 * Implement this interface to describe, how a certain data-model should be validated.
 */
expect abstract class Validator<D, M : ValidationMessage, T>() {

    /**
     * method that has to be implemented to describe the validation-rules
     *
     * @param data model-instance to be validated
     * @param metadata some data to be used as parameters for the validation (validate differently for the steps in a process)
     * @return a [List] of messages (your result-type implementing [ValidationMessage])
     */
    abstract fun validate(data: D, metadata: T): List<M>

}


