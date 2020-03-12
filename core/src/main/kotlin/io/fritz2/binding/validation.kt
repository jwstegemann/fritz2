package io.fritz2.binding

import io.fritz2.optics.withId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * [Failable] has one method [isFail] which used by the [Validator]
 * to know if the validation was successful or not.
 */
interface Failable: withId {
    fun isFail(): Boolean
}

/**
 * [Validator] has one method [validate] which getting the current data
 * from a [Store] and some metadata and returns a [List] of messages who must
 * implement the interface [Failable].
 */
@FlowPreview
@ExperimentalCoroutinesApi
abstract class Validator<D, M: Failable, T> {

    internal val channel = ConflatedBroadcastChannel<List<M>>()
    val msgs = channel.asFlow().distinctUntilChanged()

    abstract fun validate(data: D, metadata: T): List<M>

    val isValid by lazy {msgs.map { list -> list.none(Failable::isFail)}}
}

/**
 * [Validation] is an extension for a [Store] and offers
 * the [validate] method which checks if the passed data an metadata
 * is in valid state or not. Therefore it needs a [Validator] and hands
 * back a [Flow] of [List] of [Failable]s for rendering proposes.
 */
@FlowPreview
@ExperimentalCoroutinesApi
interface Validation<D, M: Failable, T> {

    val validator: Validator<D, M, T>

    fun validate(data: D, metadata: T): Boolean {
        val messages = validator.validate(data, metadata)
        println(messages)
        validator.channel.offer(messages)
        return messages.none(Failable::isFail)
    }

    fun msgs(): Flow<List<M>> = validator.msgs
}