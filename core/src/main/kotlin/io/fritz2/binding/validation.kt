package io.fritz2.binding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.scan

interface WithSeverity: WithId {
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

    val msgs = ConflatedBroadcastChannel<List<M>>()

    abstract fun validate(data: D, metadata: T): List<M>

    fun isValid(msg: M): Boolean {
        return msg.severity < Severity.Error
    }

    fun isValid(): Flow<Boolean> {
        return msgs.asFlow().scan(true) { b, msgs -> msgs.all { msg -> isValid(msg) } }
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
interface Validation<D, M: WithSeverity, T> {

    val validator: Validator<D, M, T>

    fun validate(data: D, metadata: T): Boolean {
        val messages = validator.validate(data, metadata)
        println(messages)
        validator.msgs.offer(messages)
        return messages.all { m -> validator.isValid(m) }
    }

    fun msgs(): Flow<List<M>> = validator.msgs.asFlow()
}