package dev.fritz2.headlessdemo.components

import dev.fritz2.core.Lenses

@Lenses
data class ComboboxDemoConfig(
    val readOnly: Boolean = false,
    val autoSelectMatch: Boolean = false,
    val openDropdownLazily: Boolean = false,
) {
    companion object
}
