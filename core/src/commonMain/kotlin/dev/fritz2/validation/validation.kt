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
 * Describes the logic for validating a given data-model.
 * By implementing this you must describe, how a certain data-model should be validated.
 * This is done by returning a [List] of [ValidationMessage]s in the [validate] functions.
 *
 * It is recommended to put the concrete implementation of this [Validator] right next to your data classes
 * in the `commonMain` section of your Kotlin multiplatform project.
 * Then you can write the validation logic once and use them in the JS and JVM world.
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

/**
 * convenience method for creating a new [Validator]
 */
fun <D, M : ValidationMessage, T> validator(doValidation: (D, T) -> List<M>): Validator<D, M, T> =
    object : Validator<D, M, T>() {
        override fun validate(data: D, metadata: T): List<M> = doValidation(data, metadata)
    }


