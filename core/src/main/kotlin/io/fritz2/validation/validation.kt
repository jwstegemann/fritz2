package io.fritz2.validation

import io.fritz2.flow.asSharedFlow
import io.fritz2.optics.WithId
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Minimal interface that has to be implemented by the result-type of a [Validation]
 */
interface ValidationMessage : WithId {
    fun failed(): Boolean
}


/**
 * Implement this interface to describe, how a certain data-model should be validated.
 */
abstract class Validator<D, M : ValidationMessage, T> {

    internal val channel = ConflatedBroadcastChannel<List<M>>()

    /**
     * The [Flow] of messages (validation-results). For each run of the [Validation] a new values appears on this [Flow].
     * If no messages result from a run, the new value is an empty list.
     */
    val msgs = channel.asFlow().distinctUntilChanged().asSharedFlow()

    /**
     * method that has to be implemented to describe the validation-rules
     *
     * @param data model-instance to be validated
     * @param metadata some data to be used as parameters for the validation (validate differently for the steps in a process)
     * @return a [List] of messages (your result-type implementing [ValidationMessage])
     */
    abstract fun validate(data: D, metadata: T): List<M>

    /**
     * A [Flow] representing the current state of the model (valid or not).
     * Use this to easily make actions possible of not depending on the validity of your model (save-button, etc.)
     */
    val isValid by lazy { msgs.map { list -> list.none(ValidationMessage::failed) } }
}


/**
 * An interface that can be inherited into a [Store] that allows to easily use the validation in your [Handler]s and templates.
 */
interface Validation<D, M : ValidationMessage, T> {

    /**
     * the [Validator] to be used validating the current value of the [Store]
     */
    val validator: Validator<D, M, T>

    /**
     * call this method from your [Handler]s to validate the current model and react to the result
     */
    fun validate(data: D, metadata: T): Boolean {
        val messages = validator.validate(data, metadata)
        validator.channel.offer(messages)
        return messages.none(ValidationMessage::failed)
    }

    /**
     * bind this [Flow] in your templates to show the result of the last validation
     */
    fun msgs(): Flow<List<M>> = validator.msgs
}