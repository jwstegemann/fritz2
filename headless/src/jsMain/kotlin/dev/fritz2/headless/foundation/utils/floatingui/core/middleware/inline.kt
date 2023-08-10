@file:JsModule("@floating-ui/core")
@file:JsNonModule

package dev.fritz2.headless.foundation.utils.floatingui.core.middleware

import dev.fritz2.headless.foundation.utils.floatingui.core.Middleware
import dev.fritz2.headless.foundation.utils.floatingui.utils.ClientRectObject
import dev.fritz2.headless.foundation.utils.floatingui.utils.Padding

external fun getRectsByLine(rects: Array<ClientRectObject>): Array<ClientRectObject>

external interface InlineOptions {
    /**
     * Viewport-relative `x` coordinate to choose a `ClientRect`.
     * @default undefined
     */
    var x: Number?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Viewport-relative `y` coordinate to choose a `ClientRect`.
     * @default undefined
     */
    var y: Number?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Represents the padding around a disjoined rect when choosing it.
     * @default 2
     */
    var padding: Padding?
        get() = definedExternally
        set(value) = definedExternally
}

/**
 * Provides improved positioning for inline reference elements that can span
 * over multiple lines, such as hyperlinks or range selections.
 * @see https://floating-ui.com/docs/inline
 */
external fun inline(options: InlineOptions? = definedExternally): Middleware;