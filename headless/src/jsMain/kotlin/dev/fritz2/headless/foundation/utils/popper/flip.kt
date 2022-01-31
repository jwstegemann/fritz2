package dev.fritz2.headless.foundation.utils.popper

import org.w3c.dom.HTMLElement

external interface FlipOptions {
    var fallbackPlacements: Array<Placement>? // [oppositePlacement]
        get() = definedExternally
        set(value) = definedExternally

    var padding: Int?
        get() = definedExternally
        set(value) = definedExternally

    var boundary: HTMLElement?
        get() = definedExternally
        set(value) = definedExternally

    var rootBoundary: dynamic
        get() = definedExternally
        set(value) = definedExternally

    var flipVariations: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    var allowedAutoPlacements: Array<Placement>?
        get() = definedExternally
        set(value) = definedExternally
}

fun FlipOptionsInit(
    fallbackPlacements: Array<Placement>? = null,
    padding: Int? = null,
    boundary: HTMLElement? = null,
    rootBoundary: dynamic = null,
    flipVariations: Boolean? = null,
    allowedAutoPlacements: Array<Placement>? = null
): FlipOptions {
    val o = js("({})")
    if (fallbackPlacements != null) o["fallbackPlacements"] = fallbackPlacements
    if (padding != null) o["padding"] = padding
    if (boundary != null) o["boundary"] = boundary
    if (rootBoundary != null) o["rootBoundary"] = rootBoundary
    if (flipVariations != null) o["flipVariations"] = flipVariations
    if (allowedAutoPlacements != null) o["allowedAutoPlacements"] = allowedAutoPlacements
    return o
}

fun Flip(enabled: Boolean = true): Modifier<FlipOptions> = ModifierInit("flip", enabled, FlipOptionsInit())
