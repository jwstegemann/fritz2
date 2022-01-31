package dev.fritz2.headless.foundation.utils

import dev.fritz2.binding.RootStore

class ExpansionStore(initialValue: Boolean = false) : RootStore<Boolean>(initialValue) {
    val show = handle { true }
    val hide = handle { false }
    val toggle = handle { !it }
}
