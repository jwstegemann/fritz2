@file:JsModule("@floating-ui/core")
@file:JsNonModule
package dev.fritz2.headless.foundation.utils.floatingui.core.middleware

import dev.fritz2.headless.foundation.utils.floatingui.core.DetectOverflowOptions
import dev.fritz2.headless.foundation.utils.floatingui.core.Middleware
import dev.fritz2.headless.foundation.utils.floatingui.utils.Alignment
import dev.fritz2.headless.foundation.utils.floatingui.utils.Placement

external fun getPlacementList(
    alignment: Alignment?,
    autoAlignment: Boolean,
    allowedPlacements: Array<Placement>
): Array<Placement>

external interface AutoPlacementOptions : DetectOverflowOptions {
    /**
     * The axis that runs along the alignment of the floating element. Determines
     * whether to check for most space along this axis.
     * @default false
     */
    var crossAxis: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Choose placements with a particular alignment.
     * @default undefined
     */
    var alignment: Alignment?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Whether to choose placements with the opposite alignment if the preferred
     * alignment does not fit.
     * @default true
     */
    var autoAlignment: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Which placements are allowed to be chosen. Placements must be within the
     * `alignment` option if explicitly set.
     * @default allPlacements (variable)
     */
    var allowedPlacements: Array<Placement>?
        get() = definedExternally
        set(value) = definedExternally
}

/**
 * Optimizes the visibility of the floating element by choosing the placement
 * that has the most space available automatically, without needing to specify a
 * preferred placement. Alternative to `flip`.
 * @see https://floating-ui.com/docs/autoPlacement
 */
external fun autoPlacement(options: AutoPlacementOptions? = definedExternally): Middleware

