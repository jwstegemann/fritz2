package dev.fritz2.validation

import dev.fritz2.identification.Inspector
import dev.fritz2.identification.inspectorOf

/**
 * Describes the logic for validating a given data-model.
 * By implementing this you must describe, how a certain data-model should be validated.
 * This is done by returning a [List] of [ValidationMessage]s in the [validate] functions.
 *
 * It is recommended to put the concrete implementation of this [Validator] right next to your data classes
 * in the `commonMain` section of your Kotlin multiplatform project.
 * Then you can write the validation logic once and use them in the JS and JVM world.
 */
actual abstract class Validator<D, M : ValidationMessage, T> {

    /**
     * validates the given [inspector] and it's containing data
     * by using the given [metadata] and adds validation messages if needed.
     * To add a message use the [MutableList.add] function.
     *
     * @param inspector inspector containing the data to validate
     * @param metadata extra information for the validation process
     */
    actual abstract fun MutableList<M>.validate(inspector: Inspector<D>, metadata: T)

    /**
     * validates the given [data] by using the given [metadata] and returns
     * a [List] of [ValidationMessage]s.
     *
     * @param data data to validate
     * @param metadata extra information for the validation process
     * @return [List] of [ValidationMessage]s
     */
    actual fun getMessages(data: D, metadata: T): List<M> = buildList { validate(inspectorOf(data), metadata) }

    /**
     * evaluates the [List] of [ValidationMessage] to see if your [data] is valid or not
     *
     * @param data model to validate
     * @param metadata extra information for the validation process
     * @return a [Boolean] for using in if conditions
     */
    fun isValid(data: D, metadata: T): Boolean = getMessages(data, metadata).none(ValidationMessage::isError)
}