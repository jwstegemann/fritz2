package dev.fritz2.components.validation

import dev.fritz2.validation.ValidationMessage

class ComponentValidationMessage(val id: String, val severity: Severity, val message: String) : ValidationMessage {
    override fun isError(): Boolean = severity > Severity.Info
}

enum class Severity {
    Info, Warning, Error
}
