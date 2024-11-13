@file:JsModule("@floating-ui/core")
@file:JsNonModule

package dev.fritz2.headless.foundation.utils.floatingui.core.middleware

import dev.fritz2.headless.foundation.utils.floatingui.core.*

external interface ShiftOptionsLimiter {
    var fn: ((state: MiddlewareState) -> Coords)?
        get() = definedExternally
        set(value) = definedExternally
    var options: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ShiftOptions : DetectOverflowOptions {
    /**
     * The axis that runs along the alignment of the floating element. Determines
     * whether overflow along this axis is checked to perform shifting.
     * @default true
     */
    var mainAxis: Boundary?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * The axis that runs along the side of the floating element. Determines
     * whether overflow along this axis is checked to perform shifting.
     * @default false
     */
    var crossAxis: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Accepts a function that limits the shifting done in order to prevent
     * detachment.
     */
    var limiter: ShiftOptionsLimiter?
        get() = definedExternally
        set(value) = definedExternally
}

/**
 * Optimizes the visibility of the floating element by shifting it in order to
 * keep it in view when it will overflow the clipping boundary.
 * @see https://floating-ui.com/docs/shift
 */
external fun shift(options: ShiftOptions? = definedExternally): Middleware


external interface LimitShiftOffset /*= number | Partial<*/ {
    /**
     * Offset the limiting of the axis that runs along the alignment of the
     * floating element.
     */
    var mainAxis: Number?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Offset the limiting of the axis that runs along the side of the
     * floating element.
     */
    var crossAxis: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface LimitShiftOptions {
    /**
     * Offset when limiting starts. `0` will limit when the opposite edges of the
     * reference and floating elements are aligned.
     * - positive = start limiting earlier
     * - negative = start limiting later
     */
    var offset: LimitShiftOffset?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Whether to limit the axis that runs along the alignment of the floating
     * element.
     */
    var mainAxis: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Whether to limit the axis that runs along the side of the floating element.
     */
    var crossAxis: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

/**
 * Built-in `limiter` that will stop `shift()` at a certain point.
 */
external fun limitShift(options: LimitShiftOptions? = definedExternally): ShiftOptionsLimiter