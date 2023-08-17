package dev.fritz2.headless.foundation.utils.popper

enum class Placement(val parameter: String) {
    auto("auto"),
    autoStart("auto-start"),
    autoEnd("auto-end"),
    top("top"),
    topStart("top-start"),
    topEnd("top-end"),
    bottom("bottom"),
    bottomStart("bottom-start"),
    bottomEnd("bottom-end"),
    right("right"),
    rightStart("right-start"),
    rightEnd("right-end"),
    left("left"),
    leftStart("left-start"),
    leftEnd("left-end"),
}

enum class Strategy {
    absolute, fixed
}

external interface PopperOptions {
    var placement: String?
        get() = definedExternally
        set(value) = definedExternally

    var strategy: String?
        get() = definedExternally
        set(value) = definedExternally

    var modifiers: Array<Modifier<*>>?
        get() = definedExternally
        set(value) = definedExternally
}

fun PopperOptionsInit(
    placement: Placement,
    strategy: Strategy? = null,
    vararg modifiers: Modifier<*>,
): PopperOptions {
    val o = js("({})")
    o["placement"] = placement.parameter
    if (strategy != null) o["strategy"] = strategy.name
    o["modifiers"] = modifiers
    return o
}
