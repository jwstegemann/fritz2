package dev.fritz2.headless.model

import dev.fritz2.headless.validation.ComponentValidationMessage
import dev.fritz2.headless.validation.errorMessage
import dev.fritz2.lenses.Lens
import dev.fritz2.lenses.Lenses
import dev.fritz2.lenses.lens
import dev.fritz2.validation.validation

@Lenses
data class TestModel(
    val switch: Boolean = false
) {
    companion object {
        val switch: Lens<TestModel, Boolean> = lens("switch", TestModel::switch) { m, n -> m.copy(switch = n) }

        val validation = validation<TestModel, ComponentValidationMessage> { insp ->
            val switch = insp.sub(TestModel.switch)
            if(switch.data) add(switch.errorMessage("error"))
        }
    }
}
