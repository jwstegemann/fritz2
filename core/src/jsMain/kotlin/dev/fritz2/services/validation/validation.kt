package dev.fritz2.services.validation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

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

    private val state = MutableStateFlow<List<M>>(emptyList())

    /**
     * contains the [List] of [ValidationMessage]s which gets updated every time when [isValid] gets called.
     * If no messages result from validation its returns an empty list.
     */
    val msgs = state as Flow<List<M>>

    /**
     * validates the given data by using the given metadata and returns
     * a [List] of [ValidationMessage]s.
     *
     * @param data data to validate
     * @param metadata extra information for the validation process
     * @return [List] of [ValidationMessage]s
     */
    actual abstract fun validate(data: D, metadata: T): List<M>

    /**
     * validates the given model by using the [validate] functions
     * which must be implemented with the validation rules.
     *
     * @param data model to validate
     * @param metadata extra information for the validation process
     * @return a [Boolean] for using in if conditions
     */
    fun isValid(data: D, metadata: T): Boolean {
        val messages = validate(data, metadata)
        state.value = messages
        return messages.none(ValidationMessage::failed)
    }

    /**
     * A [Flow] representing the current state of the model (valid or not).
     */
    val isValid: Flow<Boolean> by lazy { msgs.map { list -> list.none(ValidationMessage::failed) } }
}

