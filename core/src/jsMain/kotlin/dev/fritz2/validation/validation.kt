package dev.fritz2.validation

import dev.fritz2.flow.asSharedFlow
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Implement this interface to describe, how a certain data-model should be validated.
 */
actual abstract class Validator<D, M : ValidationMessage, T> actual constructor() {

    internal val channel = ConflatedBroadcastChannel<List<M>>()

    /**
     * The [Flow] of messages (validation-results). For each run of the [Validation] a new values appears on this [Flow].
     * If no messages result from a run, the new value is an empty list.
     */
    val msgs = channel.asFlow().distinctUntilChanged().asSharedFlow()

    actual abstract fun validate(data: D, metadata: T): List<M>

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