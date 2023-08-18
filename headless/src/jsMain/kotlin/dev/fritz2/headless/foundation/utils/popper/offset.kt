package dev.fritz2.headless.foundation.utils.popper

external interface OffsetOptions {
    var offset: Array<Int>?
        get() = definedExternally
        set(value) = definedExternally
}

fun OffsetOptionsInit(skidding: Int, distance: Int): OffsetOptions {
    val o = js("({})")
    o["offset"] = arrayOf(skidding, distance)
    return o
}

fun Offset(skidding: Int, distance: Int): Modifier<OffsetOptions> =
    ModifierInit("offset", options = OffsetOptionsInit(skidding, distance))
