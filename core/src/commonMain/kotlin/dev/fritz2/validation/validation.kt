package dev.fritz2.validation

import dev.fritz2.identification.Inspector
import dev.fritz2.identification.inspectorOf


///**
// * convenience method for creating a new [Validator]
// */
//fun <D, M : ValidationMessage, T> validator(doValidation: (Inspector<D>, T) -> List<M>) =
//    object : Validator<D, M, T>() {
//        override fun validate(inspector: Inspector<D>, metadata: T): List<M> = doValidation(inspector, metadata)
//    }
//
///**
// * Describes the logic for validating a given data-model.
// * By implementing this you must describe, how a certain data-model should be validated.
// * This is done by returning a [List] of [ValidationMessage]s in the [validate] functions.
// *
// * It is recommended to put the concrete implementation of this [Validator] right next to your data classes
// * in the `commonMain` section of your Kotlin multiplatform project.
// * Then you can write the validation logic once and use them in the JS and JVM world.
// */
//expect abstract class Validator<D, M : ValidationMessage, T>() {
//
//    /**
//     * validates the given [inspector] and it's containing data
//     * by using the given [metadata] and returns
//     * a [List] of [ValidationMessage]s.
//     *
//     * @param inspector inspector containing the data to validate
//     * @param metadata extra information for the validation process
//     * @return [List] of [ValidationMessage]s
//     */
//    abstract fun validate(inspector: Inspector<D>, metadata: T): List<M>
//
//    /**
//     * validates the given [data] by using the given [metadata] and returns
//     * a [List] of [ValidationMessage]s.
//     *
//     * @param data data to validate
//     * @param metadata extra information for the validation process
//     * @return [List] of [ValidationMessage]s
//     */
//    fun validate(data: D, metadata: T): List<M>
//}

typealias Validation<D, T, M> = (D, T?) -> List<M>

fun <D, T, M> validation(validate: MutableList<M>.(Inspector<D>, T?) -> Unit): Validation<D, T, M> = { data, metadata ->
    buildList<M> { validate(inspectorOf(data), metadata) }
}

fun <D, M> validation(validate: MutableList<M>.(Inspector<D>) -> Unit): Validation<D, Unit, M> = { data, _ ->
    buildList<M> { validate(inspectorOf(data)) }
}

operator fun <D, M> Validation<D, Unit, M>.invoke(data: D): List<M> = this(data, null)

/**
 * Minimal interface that has to be implemented and contains the message from
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

fun <M: ValidationMessage> List<M>.isValid(): Boolean = none { it.isError() }