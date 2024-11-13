@file:JsModule("@floating-ui/core")
@file:JsNonModule

package dev.fritz2.headless.foundation.utils.floatingui.core.middleware

import dev.fritz2.headless.foundation.utils.floatingui.core.Coords
import dev.fritz2.headless.foundation.utils.floatingui.core.Middleware
import dev.fritz2.headless.foundation.utils.floatingui.core.MiddlewareState
import kotlin.js.Promise

external interface OffsetOptions {
    /**
     * The axis that runs along the side of the floating element. Represents
     * the distance (gutter or margin) between the reference and floating
     * element.
     * @default 0
     */
    var mainAxis: Number?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * The axis that runs along the alignment of the floating element.
     * Represents the skidding between the reference and floating element.
     * @default 0
     */
    var crossAxis: Number?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * The same axis as `crossAxis` but applies only to aligned placements
     * and inverts the `end` alignment. When set to a number, it overrides the
     * `crossAxis` value.
     *
     * A positive number will move the floating element in the direction of
     * the opposite edge to the one that is aligned, while a negative number
     * the reverse.
     * @default null
     */
    var alignmentAxis: Number?
        get() = definedExternally
        set(value) = definedExternally
}


external fun convertValueToCoords(state: MiddlewareState, options: OffsetOptions): Promise<Coords>

/**
 * Modifies the placement by translating the floating element along the
 * specified axes.
 * A number (shorthand for `mainAxis` or distance), or an axes configuration
 * object may be passed.
 * @see https://floating-ui.com/docs/offset
 */
external fun offset(options: OffsetOptions): Middleware = definedExternally
