package io.fritz2.binding

import io.fritz2.optics.withId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

interface Failable: withId {
    fun isFail(): Boolean
}

@FlowPreview
@ExperimentalCoroutinesApi
abstract class Validator<D, M: Failable, T> {

    internal val channel = ConflatedBroadcastChannel<List<M>>()
    val msgs = channel.asFlow().distinctUntilChanged()

    abstract fun validate(data: D, metadata: T): List<M>

    val isValid by lazy {msgs.map { list -> list.none(Failable::isFail)}}
}

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