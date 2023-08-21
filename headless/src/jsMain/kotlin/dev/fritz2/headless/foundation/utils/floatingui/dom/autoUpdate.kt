package dev.fritz2.headless.foundation.utils.floatingui.dom

external interface AutoUpdateOptions {
    /**
     * Whether to update the position when an overflow ancestor is scrolled.
     * @default true
     */
    var ancestorScroll: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Whether to update the position when an overflow ancestor is resized. This
     * uses the native `resize` event.
     * @default true
     */
    var ancestorResize: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Whether to update the position when either the reference or floating
     * elements resized. This uses a `ResizeObserver`.
     * @default true
     */
    var elementResize: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Whether to update the position when the reference relocated on the screen
     * due to layout shift.
     * @default true
     */
    var layoutShift: Boolean?
        get() = definedExternally
        set(value) = definedExternally

    /**
     * Whether to update on every animation frame if necessary. Only use if you
     * need to update the position in response to an animation using transforms.
     * @default false
     */
    var animationFrame: Boolean?
        get() = definedExternally
        set(value) = definedExternally
};