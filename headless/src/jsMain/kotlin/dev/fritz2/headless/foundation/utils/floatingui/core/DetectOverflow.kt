@file:JsModule("@floating-ui/core")
@file:JsNonModule

package dev.fritz2.headless.foundation.utils.floatingui.core

import dev.fritz2.headless.foundation.utils.floatingui.utils.Padding
import dev.fritz2.headless.foundation.utils.floatingui.utils.SideObject
import kotlin.js.Promise

external interface DetectOverflowOptions {
    /**
     * The clipping element(s) or area in which overflow will be checked.
     * @default 'clippingAncestors'
     */
    var boundary: Boundary?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * The root clipping area in which overflow will be checked.
     * @default 'viewport'
     */
    var rootBoundary: RootBoundary?
        get() = definedExternally
        set(value) = definedExternally


    /**
     * The element in which overflow is being checked relative to a boundary.
     * @default 'floating'
     */
    var elementContext: ElementContext?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Whether to check for overflow using the alternate element's boundary
     * (`clippingAncestors` boundary only).
     * @default false
     */
    var altBoundary: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Virtual padding for the resolved overflow detection offsets.
     * @default 0
     */
    var padding: Padding?
        get() = definedExternally
        set(value) = definedExternally
}

/**
 * Resolves with an object of overflow side offsets that determine how much the
 * element is overflowing a given clipping boundary on each side.
 * - positive = overflowing the boundary by that number of pixels
 * - negative = how many pixels left before it will overflow
 * - 0 = lies flush with the boundary
 * @see https://floating-ui.com/docs/detectOverflow
 */
external fun detectOverflow(state: MiddlewareState, options: DetectOverflowOptions = definedExternally): Promise<SideObject>;