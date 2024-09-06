package dev.fritz2.lens

import com.google.devtools.ksp.processing.KSPLogger

enum class Severity {
    Warning, Error
}

data class Message(val severity: Severity, val message: String)

fun KSPLogger.log(message: Message) {
    when (message.severity) {
        Severity.Warning -> this.warn(message.message)
        Severity.Error -> this.error(message.message)
    }
}