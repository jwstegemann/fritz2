@file:Suppress("unused")

package dev.fritz2.headless.foundation.utils.floatingui.core

import dev.fritz2.headless.foundation.utils.floatingui.utils.*
import org.w3c.dom.Element
import kotlin.js.Promise


external interface Platform {
    fun getElementRects(
        reference: ReferenceElement,
        floating: FloatingElement,
        strategy: Strategy,
    ): Promise<ElementRects>;

    fun getClippingRect(
        element: Any,
        boundary: Boundary,
        rootBoundary: RootBoundary,
        strategy: Strategy
    ): Promise<Rect>;

    fun getDimensions(element: Any): Promise<Dimensions>

    fun convertOffsetParentRelativeRectToViewportRelativeRect(
        rect: Rect,
        offsetParent: Any,
        strategy: Strategy,
    ): Promise<Rect>;

    fun getOffsetParent(element: Any): Promise<Any>;
    fun isElement(value: Any): Promise<Boolean>;
    fun getDocumentElement(element: Any): Promise<Any>;
    fun getClientRects(element: Any): Promise<Array<ClientRectObject>>;
    fun isRTL(element: Any): Promise<Boolean>
    fun getScale(element: Any): Promise<Coords>
}

external interface Arrow : Coords {
    var centerOffset: Number
}

external interface Overflows {
    var placement: Placement
    var overflows: Array<Number>
}

external interface AutoPlacement {
    var index: Number?
    var overflow: Array<Overflows>
}

external interface Flip {
    var index: Number
    var overflows: Array<Overflows>
}

external interface Hide {
    var referenceHidden: Boolean;
    var escaped: Boolean;
    var referenceHiddenOffsets: SideObject?;
    var escapedOffsets: SideObject?

}

external interface MiddlewareData {
    var arrow: Arrow
    var autoPlacement: AutoPlacement;
    var flip: Flip
    var hide: Hide?
    var offset: Coords?
    var shift: Coords?
}

external interface ComputePositionConfig {
    /**
     * Object to interface with the current platform.
     */
    var platform: Platform?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Where to place the floating element relative to the reference element.
     */
    var placement: Placement?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * The strategy to use when positioning the floating element.
     */
    var strategy: Strategy?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Array of middleware objects to modify the positioning or provide data for
     * rendering.
     */
    var middleware: Array<Middleware>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ComputePositionReturn : Coords {
    /**
     * The final chosen placement of the floating element.
     */
    val placement: Placement?
        get() = definedExternally

    /**
     * The strategy used to position the floating element.
     */
    val strategy: Strategy?
        get() = definedExternally

    /**
     * Object containing data returned from all middleware, keyed by their name.
     */
    val middlewareData: MiddlewareData?
        get() = definedExternally
}


external interface Coords {
    var x: Number?
        get() = definedExternally
        set(value) = definedExternally
    var y: Number?
        get() = definedExternally
        set(value) = definedExternally
};

external interface Reset {
    var placement: Placement?
        get() = definedExternally
        set(value) = definedExternally
    var rects: ElementRects?
        get() = definedExternally
        set(value) = definedExternally
}

external interface MiddlewareReturn : Coords {
    var data: Map<String, dynamic>?
        get() = definedExternally
        set(value) = definedExternally
    var reset: Reset?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Middleware {
    var name: String?
        get() = definedExternally
        set(value) = definedExternally
    var options: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var fn: ((state: MiddlewareState) -> Promise<MiddlewareReturn>)?
        get() = definedExternally
        set(value) = definedExternally
}

external interface Dimensions {
    var width: Number
    var height: Number
};

typealias ReferenceElement = Element
typealias FloatingElement = Element

external interface Elements {
    var reference: ReferenceElement;
    var floating: FloatingElement;
}


external interface MiddlewareState : Coords {
    var initialPlacement: Placement;
    var placement: Placement;
    var strategy: Strategy;
    var middlewareData: MiddlewareData;
    var elements: Elements;
    var rects: ElementRects;
    var platform: Platform;
}

/**
 * @deprecated use `MiddlewareState` instead.
 */
typealias MiddlewareArguments = MiddlewareState

typealias Boundary = Any

typealias RootBoundary = Any

object RootBoundaryValues {
    val viewport: RootBoundary = "viewport"
    val document: RootBoundary = "document"
    fun rect(rect: Rect): RootBoundary = rect
}


typealias ElementContext = String

object ElementContextValues {
    val reference: ElementContext = "reference"
    val floating: ElementContext = "floating"
}