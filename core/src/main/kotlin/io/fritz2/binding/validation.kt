package io.fritz2.binding

import io.fritz2.optics.withId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

interface WithSeverity: withId {
    val severity: Severity
}

enum class Severity {
    Info,
    Warning,
    Error,
    Fatal,
}

@FlowPreview
@ExperimentalCoroutinesApi
abstract class Validator<D, M: WithSeverity, T> {

    internal val channel = ConflatedBroadcastChannel<List<M>>()
    val msgs = channel.asFlow().distinctUntilChanged()

    abstract fun validate(data: D, metadata: T): List<M>

    fun isValid(msg: M): Boolean {
        return msg.severity < Severity.Error
    }

    fun isValid(): Flow<Boolean> {
        //TODO optimize
        return msgs.map { list -> list.none {m -> !isValid(m)}}
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
interface Validation<D, M: WithSeverity, T> {

    val validator: Validator<D, M, T>

    fun validate(data: D, metadata: T): Boolean {
        val messages = validator.validate(data, metadata)
        println(messages)
        validator.channel.offer(messages)
        return messages.all { m -> validator.isValid(m) }
    }

    fun msgs(): Flow<List<M>> = validator.msgs
}