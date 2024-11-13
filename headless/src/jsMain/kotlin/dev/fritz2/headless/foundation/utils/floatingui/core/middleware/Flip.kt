@file:JsModule("@floating-ui/core")
@file:JsNonModule

package dev.fritz2.headless.foundation.utils.floatingui.core.middleware

import dev.fritz2.headless.foundation.utils.floatingui.core.DetectOverflowOptions
import dev.fritz2.headless.foundation.utils.floatingui.core.Middleware
import dev.fritz2.headless.foundation.utils.floatingui.utils.Placement

external interface FlipOptions : DetectOverflowOptions {
    /**
     * The axis that runs along the side of the floating element. Determines
     * whether overflow along this axis is checked to perform a flip.
     * @default true
     */
    var mainAxis: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * The axis that runs along the alignment of the floating element. Determines
     * whether overflow along this axis is checked to perform a flip.
     * @default true
     */
    var crossAxis: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Placements to try sequentially if the preferred `placement` does not fit.
     * @default [oppositePlacement] (computed)
     */
    var fallbackPlacements: Array<Placement>?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * What strategy to use when no placements fit.
     * @default 'bestFit'
     */
    var fallbackStrategy: String?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Whether to allow fallback to the perpendicular axis of the preferred
     * placement, and if so, which side direction along the axis to prefer.
     * @default 'none' (disallow fallback)
     */
    var fallbackAxisSideDirection: String?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Whether to flip to placements with the opposite alignment if they fit
     * better.
     * @default true
     */
    var flipAlignment: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

/**
 * Optimizes the visibility of the floating element by flipping the `placement`
 * in order to keep it in view when the preferred placement(s) will overflow the
 * clipping boundary. Alternative to `autoPlacement`.
 * @see https://floating-ui.com/docs/flip
 */
external fun flip(options: FlipOptions? = definedExternally): Middleware
