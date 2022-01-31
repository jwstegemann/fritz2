package dev.fritz2.headless.hooks

import dev.fritz2.dom.HtmlTag
import kotlinx.coroutines.flow.Flow


class AttributeHook<C : HtmlTag<*>, T>(
    private val valueSetter: C.(T) -> Unit,
    private val flowOfValueSetter: C.(Flow<T>) -> Unit
) : BasicHook<C, Unit, Unit>(), Usable<AttributeHook<C, T>> {

    operator fun invoke(value: T?) {
        apply = value?.let { v -> { valueSetter(v) } }
    }

    operator fun invoke(value: Flow<T>) {
        apply = { flowOfValueSetter(value) }
    }

    override fun use(other: AttributeHook<C, T>) {
        apply = other.apply
    }
}

class BooleanAttributeHook<C : HtmlTag<*>>(
    private val valueSetter: C.(Boolean, String) -> Unit,
    private val flowOfValueSetter: C.(Flow<Boolean>, String) -> Unit,
    private val trueValue: String = ""
) : BasicHook<C, Unit, Unit>(), Usable<BooleanAttributeHook<C>> {

    operator fun invoke(value: Boolean?) {
        apply = value?.let { v ->
            { valueSetter(v, trueValue) }
        }
    }

    operator fun invoke(value: Flow<Boolean>) {
        apply = { flowOfValueSetter(value, trueValue) }
    }

    override fun use(other: BooleanAttributeHook<C>) {
        apply = other.apply
    }
}

class RawAttributeHook<C : HtmlTag<*>, T>(private val name: String) : BasicHook<C, Unit, Unit>() {
    operator fun invoke(value: T?) {
        apply = value?.let { { attr(name, it) } }
    }

    operator fun invoke(value: Flow<T>) {
        apply = { attr(name, value) }
    }

    operator fun invoke(value: Boolean?, trueValue: String = "") {
        apply = value?.let { v -> { attr(name, v, trueValue) } }
    }

    operator fun invoke(value: Flow<Boolean>, trueValue: String = "") {
        apply = { attr(name, value, trueValue) }
    }

    operator fun invoke(values: List<String>?, separator: String = " ") {
        apply = values?.let { v-> { attr(name, v, separator) } }
    }

    operator fun invoke(values: Flow<List<String>>, separator: String = " ") {
        apply = { attr(name, values, separator) }
    }

    operator fun invoke(values: Map<String, Boolean>?, separator: String = " ") {
        apply = values?.let { v -> { attr(name, v, separator) } }
    }

    operator fun invoke(values: Flow<Map<String, Boolean>>, separator: String = " ") {
        apply = { attr(name, values, separator) }
    }
}
