package dev.fritz2.headless.foundation.utils.floatingui.utils

import dev.fritz2.headless.foundation.utils.floatingui.core.RootBoundary

typealias Alignment = String

object AlignmentValues {
    val start: Alignment = "start"
    val end: Alignment = "end"
}


typealias Side = String

object SideValues {
    val top: Side = "top"
    val right: Side = "right"
    val bottom: Side = "bottom"
    val left: Side = "left"
}


typealias AlignedPlacement = String

object AlignedPlacementValues {
    val topStart: AlignedPlacement = "top-start"
    val topEnd: AlignedPlacement = "top-end"
    val rightStart: AlignedPlacement = "right-start"
    val rightEnd: AlignedPlacement = "right-end"
    val bottomStart: AlignedPlacement = "bottom-start"
    val bottomEnd: AlignedPlacement = "bottom-end"
    val leftStart: AlignedPlacement = "left-start"
    val leftEnd: AlignedPlacement = "left-end"
}

typealias Placement = String

object PlacementValues {
    val topStart: Placement = "top-start"
    val topEnd: Placement = "top-end"
    val rightStart: Placement = "right-start"
    val rightEnd: Placement = "right-end"
    val bottomStart: Placement = "bottom-start"
    val bottomEnd: Placement = "bottom-end"
    val leftStart: Placement = "left-start"
    val leftEnd: Placement = "left-end"
    val top: Placement = "top"
    val right: Placement = "right"
    val bottom: Placement = "bottom"
    val left: Placement = "left"
}

typealias Strategy = String

object StrategyValues {
    val absolute: Strategy = "absolute"
    val fixed: Strategy = "fixed"
}

typealias Axis = String

object AxisValues {
    val x: Axis = "x"
    val y: Axis = "y"
}

typealias Length = String

object LengthValues {
    val width: Length = "width"
    val height: Length = "height"
}

external interface SideObject {
    var top: Number?
        get() = definedExternally
        set(value) = definedExternally
    var right: Number?
        get() = definedExternally
        set(value) = definedExternally
    var bottom: Number?
        get() = definedExternally
        set(value) = definedExternally
    var left: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Rect {
    var x: Number?
        get() = definedExternally
        set(value) = definedExternally
    var y: Number?
        get() = definedExternally
        set(value) = definedExternally
    var width: Number?
        get() = definedExternally
        set(value) = definedExternally
    var height: Number?
        get() = definedExternally
        set(value) = definedExternally
}

typealias Padding = Number // number | Partial<SideObject>

external interface ClientRectObject : Rect, SideObject

external interface ElementRects {
    var reference: Rect?
        get() = definedExternally
        set(value) = definedExternally
    var floating: Rect?
        get() = definedExternally
        set(value) = definedExternally
}

