@file:JsModule("@floating-ui/core")
@file:JsNonModule

package dev.fritz2.headless.foundation.utils.floatingui.core.middleware

import dev.fritz2.headless.foundation.utils.floatingui.core.DetectOverflowOptions
import dev.fritz2.headless.foundation.utils.floatingui.core.Middleware
import dev.fritz2.headless.foundation.utils.floatingui.core.MiddlewareState
import kotlin.js.Promise


external interface SizeOptionsArgs : MiddlewareState {
    var availableWidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var availableHeight: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SizeOptions : DetectOverflowOptions {
    /**
     * Function that is called to perform style mutations to the floating element
     * to change its size.
     * @default undefined
     */
    fun apply(args: SizeOptionsArgs): Promise<Unit>
}

/**
 * Provides data that allows you to change the size of the floating element —
 * for instance, prevent it from overflowing the clipping boundary or match the
 * width of the reference element.
 * @see https://floating-ui.com/docs/size
 */
external fun  size(options: SizeOptions? = definedExternally) : Middleware