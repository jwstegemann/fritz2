package dev.fritz2.headless.foundation

import dev.fritz2.core.Tag
import dev.fritz2.headless.validation.ComponentValidationMessage
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.HTMLElement

class ValidationMessages<CL : HTMLElement>(
    val msgs: Flow<List<ComponentValidationMessage>>,
    tag: Tag<CL>
) : Tag<CL> by tag {
    companion object {
        const val ID_SUFFIX = "validation-messages"
    }
}