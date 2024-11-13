@file:JsModule("@floating-ui/core")
@file:JsNonModule

package dev.fritz2.headless.foundation.utils.floatingui.core.middleware

import dev.fritz2.headless.foundation.utils.floatingui.core.DetectOverflowOptions
import dev.fritz2.headless.foundation.utils.floatingui.core.Middleware

external interface HideOptions : DetectOverflowOptions {
    /**
     * The strategy used to determine when to hide the floating element.
     */
    var strategy: HideStrategy?
        get() = definedExternally
        set(value) = definedExternally
}

/**
 * Provides data to hide the floating element in applicable situations, such as
 * when it is not in the same clipping context as the reference element.
 * @see https://floating-ui.com/docs/hide
 */
external fun hide(options: HideOptions?): Middleware