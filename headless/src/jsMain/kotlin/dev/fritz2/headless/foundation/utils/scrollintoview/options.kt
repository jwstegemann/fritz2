package dev.fritz2.headless.foundation.utils.scrollintoview

enum class ScrollBehavior {
    auto,
    smooth,
}

enum class ScrollMode(val parameter: String) {
    ifNeeded("if-needed"),
    always("always"),
}

enum class ScrollPosition {
    start,
    center,
    end,
    nearest,
}

external interface ScrollIntoViewOptions {
    var behavior: String?
        get() = definedExternally
        set(value) = definedExternally

    var scrollMode: String?
        get() = definedExternally
        set(value) = definedExternally

    var block: String?
        get() = definedExternally
        set(value) = definedExternally

    var inline: String?
        get() = definedExternally
        set(value) = definedExternally
}

fun ScrollIntoViewOptionsInit(
    behavior: ScrollBehavior? = null,
    mode: ScrollMode? = null,
    block: ScrollPosition? = null,
    inline: ScrollPosition? = null,
): ScrollIntoViewOptions {
    val o = js("({})")
    if (behavior != null) o["behavior"] = behavior.name
    if (mode != null) o["scrollMode"] = mode.parameter
    if (block != null) o["block"] = block.name
    if (inline != null) o["inline"] = inline.name
    return o
}

internal val HeadlessScrollOptions = ScrollIntoViewOptionsInit(
    ScrollBehavior.smooth,
    ScrollMode.ifNeeded,
    ScrollPosition.nearest,
    ScrollPosition.nearest,
)
