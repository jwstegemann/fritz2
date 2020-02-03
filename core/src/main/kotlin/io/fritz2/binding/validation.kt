package io.fritz2.binding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

interface WithSeverity: WithId {
    val severity: Severity
}

enum class Severity(val prio: Int) {
    Info(0),
    Warning(4),
    Error(8),
    Fatal(16),
}

interface Validator<D, M: WithSeverity, T> {
    fun validate(data: D, metadata: T): List<M>
}

@FlowPreview
@ExperimentalCoroutinesApi
interface Validation<D, M: WithSeverity, T> {

    val validator: Validator<D, M, T>

    var msgs: Flow<M>

    fun isValidPredicate(msg: M): Boolean {
        return msg.severity.prio < Severity.Error.prio
    }

    fun validate(data: D, metadata: T): Boolean {
        val messages = validator.validate(data, metadata)
        msgs = flow {
            for (msg in messages) {
                emit(msg)
            }
        }
        return messages.all { m -> isValidPredicate(m) }
    }

    fun isValid(): Flow<Boolean> {
        return msgs.scan(true) { b, msg -> b && isValidPredicate(msg) }
    }
}