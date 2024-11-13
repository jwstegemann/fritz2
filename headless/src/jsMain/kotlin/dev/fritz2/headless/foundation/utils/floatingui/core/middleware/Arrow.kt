@file:JsModule("@floating-ui/core")
@file:JsNonModule
package dev.fritz2.headless.foundation.utils.floatingui.core.middleware

import dev.fritz2.headless.foundation.utils.floatingui.core.Middleware
import dev.fritz2.headless.foundation.utils.floatingui.utils.Padding

external interface ArrowOptions {
    /**
     * The arrow element to be positioned.
     * @default undefined
     */
    var element: Any?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * The padding between the arrow element and the floating element edges.
     * Useful when the floating element has rounded corners.
     * @default 0
     */
    var padding: Padding?
        get() = definedExternally
        set(value) = definedExternally
}

/**
 * Provides data to position an inner element of the floating element so that it
 * appears centered to the reference element.
 * @see https://floating-ui.com/docs/arrow
 */
external fun arrow(options: ArrowOptions? = definedExternally): Middleware
