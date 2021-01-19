package dev.fritz2.components.validation

import dev.fritz2.validation.ValidationMessage
import dev.fritz2.validation.Validator

data class ComponentValidationMessage(val id: String, val severity: Severity, val message: String) : ValidationMessage {
    override fun isError(): Boolean = severity > Severity.Warning
}

fun infoMessage(id: String, message: String) =
    ComponentValidationMessage(id, Severity.Info, message)

fun warningMessage(id: String, message: String) =
    ComponentValidationMessage(id, Severity.Warning, message)

fun errorMessage(id: String, message: String) =
    ComponentValidationMessage(id, Severity.Error, message)

enum class Severity {
    Info, Warning, Error
}

abstract class ComponentValidator<D, T> : Validator<D, ComponentValidationMessage, T>()
