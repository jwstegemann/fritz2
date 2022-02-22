package dev.fritz2.headless.model

import dev.fritz2.core.Lens
import dev.fritz2.core.Lenses
import dev.fritz2.core.lens
import dev.fritz2.headless.validation.ComponentValidationMessage
import dev.fritz2.headless.validation.errorMessage
import dev.fritz2.validation.validation

val listBoxEntries = listOf("a", "b", "c", "d", "e", "f", "g")

@Lenses
data class TestModel(
    val switch: Boolean = false,
    val listBox: String = listBoxEntries.first()
) {
    companion object {
        val switch: Lens<TestModel, Boolean> = lens("switch", TestModel::switch) { m, n -> m.copy(switch = n) }
        val listBox: Lens<TestModel, String> = lens("listBox", TestModel::listBox) { m, n -> m.copy(listBox = n) }

        val validation = validation<TestModel, ComponentValidationMessage> { insp ->
            val switch = insp.sub(TestModel.switch)
            if(switch.data) add(switch.errorMessage("error"))
            val listBox = insp.sub(TestModel.listBox)
            if(listBox.data == listBoxEntries.last()) add(listBox.errorMessage("error"))
        }
    }
}
