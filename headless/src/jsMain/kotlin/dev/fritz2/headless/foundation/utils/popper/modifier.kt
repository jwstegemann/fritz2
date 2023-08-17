package dev.fritz2.headless.foundation.utils.popper

external interface Modifier<O> {
    var name: String?
        get() = definedExternally
        set(value) = definedExternally

    var enabled: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    var options: O?
        get() = definedExternally
        set(value) = definedExternally
}

fun <O> ModifierInit(name: String, enabled: Boolean = true, options: O? = null): Modifier<O> {
    val o = js("({})")
    o["name"] = name
    if (!enabled) o["enabled"] = false
    if (options != null) o["options"] = options
    return o
}
