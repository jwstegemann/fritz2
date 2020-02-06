package io.fritz2.binding

import io.fritz2.optics.withId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

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

    open fun validPredicate(msg: M): Boolean {
        return msg.severity < Severity.Error
    }

    fun isValid(): Flow<Boolean> {
        return msgs.map { list -> list.none {m -> !validPredicate(m)}}
    }

    fun isNotValid(): Flow<Boolean> {
        return msgs.map { list -> list.any {m -> validPredicate(m)}}
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
        return messages.none { m -> !validator.validPredicate(m) }
    }

    fun msgs(): Flow<List<M>> = validator.msgs
}