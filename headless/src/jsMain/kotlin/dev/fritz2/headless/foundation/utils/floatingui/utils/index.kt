@file:JsModule("@floating-ui/utils")
@file:JsNonModule

package dev.fritz2.headless.foundation.utils.floatingui.utils

external fun clamp(start: Number, value: Number, end: Number): Number
external fun <T, P> evaluate(value: T, param: P): T
external fun <T,P> evaluate(value:((param: P) -> T), param: P):T
external fun getSide(placement: Placement): Side
external fun getAlignment(placement: Placement): Alignment?
external fun getOppositeAxis(axis: Axis): Axis
external fun getAxisLength(axis: Axis): Length
external fun getSideAxis(placement: Placement): Axis
external fun getAlignmentAxis(placement: Placement): Axis
external fun getAlignmentSides(placement: Placement, rects: ElementRects, rtl: Boolean?): Array<Side>
external fun getExpandedPlacements(placement: Placement): Array<Placement>
external fun <T> getOppositeAlignmentPlacement(placement: T): T
external fun getOppositeAxisPlacements(placement: Placement, flipAlignment: Boolean, direction: Alignment, rtl: Boolean?): Array<Placement>
external fun <T> getOppositePlacement(placement: T): T
external fun expandPaddingObject(padding: SideObject): SideObject
external fun getPaddingObject(padding: Padding): SideObject
external fun rectToClientRect(rect: Rect): ClientRectObject