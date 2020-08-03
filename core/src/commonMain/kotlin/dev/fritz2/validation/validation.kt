package dev.fritz2.validation


/**
 * Minimal interface that has to be implemented and contains the message from
 * validation process.
 */
interface ValidationMessage {
    /**
     * decides if the [ValidationMessage] is a fail which is needed
     * to determine if validation is successful or not
     *
     * @return is failed or not
     */
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


