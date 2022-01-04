package dev.fritz2.validation

import dev.fritz2.identification.Inspector

/**
 * Describes the logic for validating a given data-model.
 * By implementing this you must describe, how a certain data-model should be validated.
 * This is done by adding [ValidationMessage]s in the [validate] functions which will be returned later on.
 *
 * It is recommended to put the concrete implementation of this [Validator] right next to your data classes
 * in the `commonMain` section of your Kotlin multiplatform project.
 * Then you can write the validation logic once and use them on the JS and JVM side.
 */
expect abstract class Validator<D, M : ValidationMessage, T>() {

    /**
     * validates the given [inspector] and it's containing data
     * by using the given [metadata] and adds validation messages if needed.
     * To add a message use the [MutableList.add] function.
     *
     * @param inspector inspector containing the data to validate
     * @param metadata extra information for the validation process
     */
    abstract fun MutableList<M>.validate(inspector: Inspector<D>, metadata: T)

    /**
     * validates the given [data] by using the given [metadata] and returns
     * a [List] of [ValidationMessage]s.
     *
     * @param data data to validate
     * @param metadata extra information for the validation process
     * @return [List] of [ValidationMessage]s
     */
    fun getMessages(data: D, metadata: T): List<M>
}

/**
 * Minimal interface that has to be implemented and contains the [ValidationMessage] from
 * validation process.
 */
interface ValidationMessage {

    /**
     * decides if the [ValidationMessage] is an error which is needed
     * to determine if validation is successful or not
     *
     * @return if it is an error or not
     */
    fun isError(): Boolean
}