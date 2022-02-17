package dev.fritz2.headless.foundation

import dev.fritz2.dom.Tag
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Element

/**
 * This Hook encapsulates the effect of setting an attribute to a `Tag`.
 *
 * The attribute itself has to be defined by the owner of the hook as constructor parameter, the specific value
 * however can be provided by the user.
 *
 * If the user does not provide any value, no attribute will be attached to the `Tag`!
 */
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

/**
 * This Hook encapsulates the effect of setting a boolean attribute to a `Tag`.
 *
 * The boolean attribute itself has to be defined by the owner of the hook as constructor parameter, the specific value
 * however can be provided by the user.
 *
 * If the user does not provide any value, no attribute will be attached to the `Tag`!
 */
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

/**
 * Sets an attribute only if it is not present yet.
 *
 * This is intended only for attributes, that have a *static* character, like an ARIA "role" for example.
 * It enables a client to overrule a default attribute set by some component, if the latter uses this defensive
 * function to set its default attribute.
 *
 * @param name to use
 * @param value to use
 */
fun <N : Element> Tag<N>.attrIfNotSet(name: String, value: String) {
    if (!domNode.hasAttribute(name)) attr(name, value)
}
