package dev.fritz2.headless.foundation.utils.floatingui.core.middleware

import dev.fritz2.headless.foundation.utils.floatingui.core.Middleware
import dev.fritz2.headless.foundation.utils.floatingui.obj


/**
 * @see arrow
 */
fun arrow(init: ArrowOptions.() -> Unit): Middleware = arrow(options = obj(init))

/**
 * @see autoPlacement
 */
fun autoPlacement(init: AutoPlacementOptions.() -> Unit): Middleware = autoPlacement(options = obj(init))


/**
 * @see flip
 */
fun flip(init: FlipOptions.() -> Unit): Middleware = flip(options = obj(init))

typealias HideStrategy = String

object HideStrategyValues {
    val referenceHidden: HideStrategy = "referenceHidden"
    val escaped: HideStrategy = "escaped"
}


/**
 * @see hide
 */
fun hide(init: HideOptions.() -> Unit): Middleware = hide(options = obj(init))

/**
 * @see inline
 */
fun inline(init: InlineOptions.() -> Unit): Middleware = inline(options = obj(init))


/**
 * @see offset
 */
fun offset(init: OffsetOptions.() -> Unit): Middleware = offset(options = obj(init))


/**
 * @see offset
 */
fun offset(mainAxis: Number): Middleware = offset { this.mainAxis = mainAxis }

/**
 * @see shift
 */
fun shift(init: ShiftOptions.() -> Unit): Middleware = shift(options = obj(init))

/**
 * @see limitShift
 */
fun limitShift(init: LimitShiftOptions.() -> Unit): ShiftOptionsLimiter = limitShift(options = obj(init))

/**
 * @see size
 */
fun size(init: SizeOptions.() -> Unit): Middleware = size(options = obj(init))