package dev.fritz2.headless.foundation

import dev.fritz2.dom.Tag
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Element


class AttributeHook<C : Tag<*>, T>(
    private val valueSetter: C.(T) -> Unit,
    private val flowOfValueSetter: C.(Flow<T>) -> Unit
) : Hook<C, Unit, Unit>() {

    operator fun invoke(value: T?) {
        this.value = value?.let { v -> { valueSetter(v) } }
    }

    operator fun invoke(value: Flow<T>) {
        this.value = { flowOfValueSetter(value) }
    }
}

class BooleanAttributeHook<C : Tag<*>>(
    private val valueSetter: C.(Boolean, String) -> Unit,
    private val flowOfValueSetter: C.(Flow<Boolean>, String) -> Unit,
    private val trueValue: String = ""
) : Hook<C, Unit, Unit>() {

    operator fun invoke(value: Boolean?) {
        this.value = value?.let { v ->
            { valueSetter(v, trueValue) }
        }
    }

    operator fun invoke(value: Flow<Boolean>) {
        this.value = { flowOfValueSetter(value, trueValue) }
    }
}

class RawAttributeHook<C : Tag<*>, T>(private val name: String) : Hook<C, Unit, Unit>() {
    operator fun invoke(value: T?) {
        this.value = value?.let { { attr(name, it) } }
    }

    operator fun invoke(value: Flow<T>) {
        this.value = { attr(name, value) }
    }

    operator fun invoke(value: Boolean?, trueValue: String = "") {
        this.value = value?.let { v -> { attr(name, v, trueValue) } }
    }

    operator fun invoke(value: Flow<Boolean>, trueValue: String = "") {
        this.value = { attr(name, value, trueValue) }
    }

    operator fun invoke(values: List<String>?, separator: String = " ") {
        this.value = values?.let { v -> { attr(name, v, separator) } }
    }

    operator fun invoke(values: Flow<List<String>>, separator: String = " ") {
        this.value = { attr(name, values, separator) }
    }

    operator fun invoke(values: Map<String, Boolean>?, separator: String = " ") {
        this.value = values?.let { v -> { attr(name, v, separator) } }
    }

    operator fun invoke(values: Flow<Map<String, Boolean>>, separator: String = " ") {
        this.value = { attr(name, values, separator) }
    }
}

/**
 * Sets an attribute only if it is not present yet.
 *
 * This is intended only for attributes, that have a *static* character, like an ARIA "role" for example.
 * It enables a client to overrule a default attribute set by a lower layer of a component library, if the latter
 * uses this defensive function to set its default attribute.
 *
 * @param name to use
 * @param value to use
 */
fun <N : Element> Tag<N>.attrIfNotSet(name: String, value: String) {
    if (!domNode.hasAttribute(name)) attr(name, value)
}
