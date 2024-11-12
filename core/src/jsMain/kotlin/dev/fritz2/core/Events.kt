@file:Suppress("unused")

package dev.fritz2.core

import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import org.w3c.dom.*
import org.w3c.dom.Window
import org.w3c.dom.clipboard.ClipboardEvent
import org.w3c.dom.events.*
import org.w3c.xhr.ProgressEvent

/**
 * Contains all javascript event types.
 * Take a look [here](https://www.w3schools.com/jsref/dom_obj_event.asp).
 *
 * Sometimes it is necessary to use a more generic type (like [Event])
 * because the type that is offered to the listener is not always consistent
 * (on different browsers, different actions, etc.)
 */
interface WithEvents<out T : EventTarget> {

    companion object {
        private const val ABORT = "abort"
        private const val AFTERPRINT = "afterprint"
        private const val ANIMATIONEND = "animationend"
        private const val ANIMATIONITERATION = "animationiteration"
        private const val ANIMATIONSTART = "animationstart"
        private const val BEFOREPRINT = "beforeprint"
        private const val BEFOREUNLOAD = "beforeunload"
        private const val BLUR = "blur"
        private const val CANPLAY = "canplay"
        private const val CANPLAYTHROUGH = "canplaythrough"
        private const val CHANGE = "change"
        private const val CLICK = "click"
        private const val CONTEXTMENU = "contextmenu"
        private const val COPY = "copy"
        private const val CUT = "cut"
        private const val DBLCLICK = "dblclick"
        private const val DRAG = "drag"
        private const val DRAGEND = "dragend"
        private const val DRAGENTER = "dragenter"
        private const val DRAGLEAVE = "dragleave"
        private const val DRAGOVER = "dragover"
        private const val DRAGSTART = "dragstart"
        private const val DROP = "drop"
        private const val DURATIONCHANGE = "durationchange"
        private const val ENDED = "ended"
        private const val ERROR = "error"
        private const val FOCUS = "focus"
        private const val FOCUSIN = "focusin"
        private const val FOCUSOUT = "focusout"
        private const val FULLSCREENCHANGE = "fullscreenchange"
        private const val FULLSCREENERROR = "fullscreenerror"
        private const val HASHCHANGE = "hashchange"
        private const val INPUT = "input"
        private const val INVALID = "invalid"
        private const val KEYDOWN = "keydown"
        private const val KEYPRESS = "keypress"
        private const val KEYUP = "keyup"
        private const val LOAD = "load"
        private const val LOADEDDATA = "loadeddata"
        private const val LOADEDMETADATA = "loadedmetadata"
        private const val MOUSEENTER = "mouseenter"
        private const val MOUSELEAVE = "mouseleave"
        private const val MOUSEMOVE = "mousemove"
        private const val MOUSEOVER = "mouseover"
        private const val MOUSEOUT = "mouseout"
        private const val MOUSEUP = "mouseup"
        private const val OFFLINE = "offline"
        private const val ONLINE = "online"
        private const val OPEN = "open"
        private const val PAGEHIDE = "pagehide"
        private const val PAGESHOW = "pageshow"
        private const val PASTE = "paste"
        private const val LOADSTART = "loadstart"
        private const val MESSAGE = "message"
        private const val MOUSEDOWN = "mousedown"
        private const val PAUSE = "pause"
        private const val PLAY = "play"
        private const val PLAYING = "playing"
        private const val POPSTATE = "popstate"
        private const val PROGRESS = "progress"
        private const val RATECHANGE = "ratechange"
        private const val RESIZE = "resize"
        private const val RESET = "reset"
        private const val SCROLL = "scroll"
        private const val SEARCH = "search"
        private const val SEEKED = "seeked"
        private const val SEEKING = "seeking"
        private const val SELECT = "select"
        private const val SHOW = "show"
        private const val STALLED = "stalled"
        private const val STORAGE = "storage"
        private const val SUBMIT = "submit"
        private const val SUSPEND = "suspend"
        private const val TIMEUPDATE = "timeupdate"
        private const val TOGGLE = "toggle"
        private const val TOUCHCANCEL = "touchcancel"
        private const val TOUCHEND = "touchend"
        private const val TOUCHMOVE = "touchmove"
        private const val TOUCHSTART = "touchstart"
        private const val TRANSITIONEND = "transitionend"
        private const val UNLOAD = "unload"
        private const val VOLUMECHANGE = "volumechange"
        private const val WAITING = "waiting"
        private const val WHEEL = "wheel"
    }

    /**
     * Creates an [Listener] for the given event [eventName].
     *
     * @param eventName the [DOM-API name](https://developer.mozilla.org/en-US/docs/Web/API/Element#events) of an event.
     * Can be a custom name.
     * @param capture if `true`, activates capturing mode, else remains in `bubble` mode (default)
     * @param selector optional lambda expression to select specific events with option to manipulate it
     * (e.g. `preventDefault` or `stopPropagation`).
     *
     * @return a [Listener]-object, which is more or less a [Flow] of the specific `Event`-type.
     */
    fun <X : Event> subscribe(
        eventName: String,
        capture: Boolean = false,
        selector: X.() -> Boolean = { true }
    ): Listener<X, T>

    /**
     * occurs when the loading of a media is aborted
     */
    val aborts: Listener<Event, T> get() = subscribe(ABORT)

    /**
     * occurs when the loading of a media is aborted
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun aborts(init: Event.() -> Unit): Listener<Event, T> = subscribe(ABORT) { init(); true }

    /**
     * occurs when the loading of a media is aborted
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun abortsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(ABORT, selector = selector)

    /**
     * occurs when a page has started printing, or if the print dialogue box has been closed
     */
    val afterprints: Listener<Event, T> get() = subscribe(AFTERPRINT)

    /**
     * occurs when a page has started printing, or if the print dialogue box has been closed
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun afterprints(init: Event.() -> Unit): Listener<Event, T> = subscribe(AFTERPRINT) { init(); true }

    /**
     * occurs when a page has started printing, or if the print dialogue box has been closed
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun afterprintsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(AFTERPRINT, selector = selector)

    /**
     * occurs when a CSS Animation has completed
     */
    val animationends: Listener<Event, T> get() = subscribe(ANIMATIONEND)

    /**
     * occurs when a CSS Animation has completed
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun animationends(init: Event.() -> Unit): Listener<Event, T> = subscribe(ANIMATIONEND) { init(); true }

    /**
     * occurs when a CSS Animation has completed
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun animationendsIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(ANIMATIONEND, selector = selector)

    /**
     * occurs when an iteration of a CSS Animation ends, and another one begins
     */
    val animationiterations: Listener<Event, T> get() = subscribe(ANIMATIONITERATION)

    /**
     * occurs when an iteration of a CSS Animation ends, and another one begins
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun animationiterations(init: Event.() -> Unit): Listener<Event, T> = subscribe(ANIMATIONITERATION) { init(); true }

    /**
     * occurs when an iteration of a CSS Animation ends, and another one begins
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun animationiterationsIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(ANIMATIONITERATION, selector = selector)

    /**
     * occurs when a CSS Animation has started
     */
    val animationstarts: Listener<Event, T> get() = subscribe(ANIMATIONSTART)

    /**
     * occurs when a CSS Animation has completed
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun animationstarts(init: Event.() -> Unit): Listener<Event, T> = subscribe(ANIMATIONSTART) { init(); true }

    /**
     * occurs when a CSS Animation has started
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun animationstartsIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(ANIMATIONSTART, selector = selector)

    /**
     * occurs when a page is about to be printed
     */
    val beforeprints: Listener<Event, T> get() = subscribe(BEFOREPRINT)

    /**
     * occurs when a page is about to be printed
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun beforeprints(init: Event.() -> Unit): Listener<Event, T> = subscribe(BEFOREPRINT) { init(); true }

    /**
     * occurs when a page is about to be printed
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun beforeprintsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(BEFOREPRINT, selector = selector)

    /**
     * occurs before the document is about to be unloaded
     */
    val beforeunloads: Listener<Event, T> get() = subscribe(BEFOREUNLOAD)

    /**
     * occurs before the document is about to be unloaded
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun beforeunloads(init: Event.() -> Unit): Listener<Event, T> = subscribe(BEFOREUNLOAD) { init(); true }

    /**
     * occurs before the document is about to be unloaded
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun beforeunloadsIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(BEFOREUNLOAD, selector = selector)

    /**
     * occurs when an element loses focus
     */
    val blurs: Listener<FocusEvent, T> get() = subscribe(BLUR)

    /**
     * occurs when an element loses focus
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun blurs(init: FocusEvent.() -> Unit): Listener<FocusEvent, T> = subscribe(BLUR) { init(); true }

    /**
     * occurs when an element loses focus
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun blursIf(selector: FocusEvent.() -> Boolean): Listener<FocusEvent, T> = subscribe(BLUR, selector = selector)

    /**
     * occurs when the browser can start playing the media (when it has buffered enough to begin)
     */
    val canplays: Listener<Event, T> get() = subscribe(CANPLAY)

    /**
     * occurs when the browser can start playing the media (when it has buffered enough to begin)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun canplays(init: Event.() -> Unit): Listener<Event, T> = subscribe(CANPLAY) { init(); true }

    /**
     * occurs when the browser can start playing the media (when it has buffered enough to begin)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun canplaysIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(CANPLAY, selector = selector)

    /**
     * occurs when the browser can play through the media without stopping for buffering
     */
    val canplaythroughs: Listener<Event, T> get() = subscribe(CANPLAYTHROUGH)

    /**
     * occurs when the browser can play through the media without stopping for buffering
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun canplaythroughs(init: Event.() -> Unit): Listener<Event, T> = subscribe(CANPLAYTHROUGH) { init(); true }

    /**
     * occurs when the browser can play through the media without stopping for buffering
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun canplaythroughsIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(CANPLAYTHROUGH, selector = selector)

    /**
     * occurs when the content of a form element, the selection, or the checked state have changed
     * (for `<input>`, `<select>`, and `<textarea>`)
     */
    val changes: Listener<Event, T> get() = subscribe(CHANGE)

    /**
     * occurs when the content of a form element, the selection, or the checked state have changed
     * (for `<input>`, `<select>`, and `<textarea>`)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun changes(init: Event.() -> Unit): Listener<Event, T> = subscribe(CHANGE) { init(); true }

    /**
     * occurs when the content of a form element, the selection, or the checked state have changed
     * (for `<input>`, `<select>`, and `<textarea>`)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun changesIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(CHANGE, selector = selector)

    /**
     * occurs when the user clicks on an element
     */
    val clicks: Listener<MouseEvent, T> get() = subscribe(CLICK)

    /**
     * occurs when the user clicks on an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun clicks(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> = subscribe(CLICK) { init(); true }

    /**
     * occurs when the user clicks on an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun clicksIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> = subscribe(CLICK, selector = selector)

    /**
     * occurs when the user right-clicks on an element to open a context menu
     */
    val contextmenus: Listener<MouseEvent, T> get() = subscribe(CONTEXTMENU)

    /**
     * occurs when the user right-clicks on an element to open a context menu
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun contextmenus(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> = subscribe(CONTEXTMENU) { init(); true }

    /**
     * occurs when the user right-clicks on an element to open a context menu
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun contextmenusIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(CONTEXTMENU, selector = selector)

    /**
     * occurs when the user copies the content of an element
     */
    val copys: Listener<ClipboardEvent, T> get() = subscribe(COPY)

    /**
     * occurs when the user copies the content of an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ClipboardEvent]s on its [Flow]
     */
    fun copys(init: ClipboardEvent.() -> Unit): Listener<ClipboardEvent, T> = subscribe(COPY) { init(); true }

    /**
     * occurs when the user copies the content of an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ClipboardEvent]s on its [Flow]
     */
    fun copysIf(selector: ClipboardEvent.() -> Boolean): Listener<ClipboardEvent, T> =
        subscribe(COPY, selector = selector)

    /**
     * occurs when the user cuts the content of an element
     */
    val cuts: Listener<ClipboardEvent, T> get() = subscribe(CUT)

    /**
     * occurs when the user cuts the content of an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ClipboardEvent]s on its [Flow]
     */
    fun cuts(init: ClipboardEvent.() -> Unit): Listener<ClipboardEvent, T> = subscribe(CUT) { init(); true }

    /**
     * occurs when the user cuts the content of an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ClipboardEvent]s on its [Flow]
     */
    fun cutsIf(selector: ClipboardEvent.() -> Boolean): Listener<ClipboardEvent, T> =
        subscribe(CUT, selector = selector)

    /**
     * occurs when the user double-clicks on an element
     */
    val dblclicks: Listener<MouseEvent, T> get() = subscribe(DBLCLICK)

    /**
     * occurs when the user double-clicks on an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun dblclicks(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> = subscribe(DBLCLICK) { init(); true }

    /**
     * occurs when the user double-clicks on an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun dblclicksIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(DBLCLICK, selector = selector)

    /**
     * occurs when an element is being dragged
     */
    val drags: Listener<DragEvent, T> get() = subscribe(DRAG)

    /**
     * occurs when an element is being dragged
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun drags(init: DragEvent.() -> Unit): Listener<DragEvent, T> = subscribe(DRAG) { init(); true }

    /**
     * occurs when an element is being dragged
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragsIf(selector: DragEvent.() -> Boolean): Listener<DragEvent, T> = subscribe(DRAG, selector = selector)

    /**
     * occurs when the user has finished dragging an element
     */
    val dragends: Listener<DragEvent, T> get() = subscribe(DRAGEND)

    /**
     * occurs when the user has finished dragging an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragends(init: DragEvent.() -> Unit): Listener<DragEvent, T> = subscribe(DRAGEND) { init(); true }

    /**
     * occurs when the user has finished dragging an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragendsIf(selector: DragEvent.() -> Boolean): Listener<DragEvent, T> = subscribe(DRAGEND, selector = selector)

    /**
     * occurs when the dragged element enters the drop target
     */
    val dragenters: Listener<DragEvent, T> get() = subscribe(DRAGENTER)

    /**
     * occurs when the dragged element enters the drop target
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragenters(init: DragEvent.() -> Unit): Listener<DragEvent, T> = subscribe(DRAGENTER) { init(); true }

    /**
     * occurs when the dragged element enters the drop target
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragentersIf(selector: DragEvent.() -> Boolean): Listener<DragEvent, T> =
        subscribe(DRAGENTER, selector = selector)

    /**
     * occurs when the dragged element leaves the drop target
     */
    val dragleaves: Listener<DragEvent, T> get() = subscribe(DRAGLEAVE)

    /**
     * occurs when the dragged element leaves the drop target
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragleaves(init: DragEvent.() -> Unit): Listener<DragEvent, T> = subscribe(DRAGLEAVE) { init(); true }

    /**
     * occurs when the dragged element leaves the drop target
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragleavesIf(selector: DragEvent.() -> Boolean): Listener<DragEvent, T> =
        subscribe(DRAGLEAVE, selector = selector)

    /**
     * occurs when the dragged element is over the drop target
     */
    val dragovers: Listener<DragEvent, T> get() = subscribe(DRAGOVER)

    /**
     * occurs when the dragged element is over the drop target
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragovers(init: DragEvent.() -> Unit): Listener<DragEvent, T> = subscribe(DRAGOVER) { init(); true }

    /**
     * occurs when the dragged element is over the drop target
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragoversIf(selector: DragEvent.() -> Boolean): Listener<DragEvent, T> =
        subscribe(DRAGOVER, selector = selector)

    /**
     * occurs when the user starts to drag an element
     */
    val dragstarts: Listener<DragEvent, T> get() = subscribe(DRAGSTART)

    /**
     * occurs when the user starts to drag an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragstarts(init: DragEvent.() -> Unit): Listener<DragEvent, T> = subscribe(DRAGSTART) { init(); true }

    /**
     * occurs when the user starts to drag an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragstartsIf(selector: DragEvent.() -> Boolean): Listener<DragEvent, T> =
        subscribe(DRAGSTART, selector = selector)

    /**
     * occurs when the dragged element is dropped on the drop target
     */
    val drops: Listener<DragEvent, T> get() = subscribe(DROP)

    /**
     * occurs when the dragged element is dropped on the drop target
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun drops(init: DragEvent.() -> Unit): Listener<DragEvent, T> = subscribe(DROP) { init(); true }

    /**
     * occurs when the dragged element is dropped on the drop target
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dropsIf(selector: DragEvent.() -> Boolean): Listener<DragEvent, T> = subscribe(DROP, selector = selector)

    /**
     * occurs when the duration of the media is changed
     */
    val durationchanges: Listener<Event, T> get() = subscribe(DURATIONCHANGE)

    /**
     * occurs when the duration of the media is changed
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun durationchanges(init: Event.() -> Unit): Listener<Event, T> = subscribe(DURATIONCHANGE) { init(); true }

    /**
     * occurs when the duration of the media is changed
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun durationchangesIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(DURATIONCHANGE, selector = selector)

    /**
     * occurs when the media has reach the end (useful for messages like "thanks for listening")
     */
    val endeds: Listener<Event, T> get() = subscribe(ENDED)

    /**
     * occurs when the media has reach the end (useful for messages like "thanks for listening")
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun endeds(init: Event.() -> Unit): Listener<Event, T> = subscribe(ENDED) { init(); true }

    /**
     * occurs when the media has reach the end (useful for messages like "thanks for listening")
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun endedsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(ENDED, selector = selector)

    /**
     * occurs when a CSS Animation has completed
     */
    val errors: Listener<Event, T> get() = subscribe(ERROR)

    /**
     * occurs when there is an error while loading an external file (e.g. a document or an image).
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun errors(init: Event.() -> Unit): Listener<Event, T> = subscribe(ERROR) { init(); true }

    /**
     * occurs when there is an error while loading an external file (e.g. a document or an image).
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun errorsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(ERROR, selector = selector)

    /**
     * occurs when an element gets focus
     */
    val focuss: Listener<FocusEvent, T> get() = subscribe(FOCUS)

    /**
     * occurs when an element gets focus
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun focuss(init: FocusEvent.() -> Unit): Listener<FocusEvent, T> = subscribe(FOCUS) { init(); true }

    /**
     * occurs when an element gets focus
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun focussIf(selector: FocusEvent.() -> Boolean): Listener<FocusEvent, T> = subscribe(FOCUS, selector = selector)

    /**
     * occurs when an element is about to get focus
     */
    val focusins: Listener<FocusEvent, T> get() = subscribe(FOCUSIN)

    /**
     * occurs when an element is about to get focus
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun focusins(init: FocusEvent.() -> Unit): Listener<FocusEvent, T> = subscribe(FOCUSIN) { init(); true }

    /**
     * occurs when an element is about to get focus
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun focusinsIf(selector: FocusEvent.() -> Boolean): Listener<FocusEvent, T> =
        subscribe(FOCUSIN, selector = selector)

    /**
     * occurs when an element is about to lose focus
     */
    val focusouts: Listener<FocusEvent, T> get() = subscribe(FOCUSOUT)

    /**
     * occurs when an element is about to lose focus
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun focusouts(init: FocusEvent.() -> Unit): Listener<FocusEvent, T> = subscribe(FOCUSOUT) { init(); true }

    /**
     * occurs when an element is about to lose focus
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun focusoutsIf(selector: FocusEvent.() -> Boolean): Listener<FocusEvent, T> =
        subscribe(FOCUSOUT, selector = selector)

    /**
     * occurs when an element is displayed in fullscreen mode
     */
    val fullscreenchanges: Listener<Event, T> get() = subscribe(FULLSCREENCHANGE)

    /**
     * occurs when an element is displayed in fullscreen mode
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun fullscreenchanges(init: Event.() -> Unit): Listener<Event, T> = subscribe(FULLSCREENCHANGE) { init(); true }

    /**
     * occurs when an element is displayed in fullscreen mode
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun fullscreenchangesIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(FULLSCREENCHANGE, selector = selector)

    /**
     * occurs when an element can not be displayed in fullscreen mode
     */
    val fullscreenerrors: Listener<Event, T> get() = subscribe(FULLSCREENERROR)

    /**
     * occurs when an element can not be displayed in fullscreen mode
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun fullscreenerrors(init: Event.() -> Unit): Listener<Event, T> = subscribe(FULLSCREENERROR) { init(); true }

    /**
     * occurs when an element can not be displayed in fullscreen mode
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun fullscreenerrorsIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(FULLSCREENERROR, selector = selector)

    /**
     * occurs when there has been changes to the anchor part of a URL
     */
    val hashchanges: Listener<HashChangeEvent, T> get() = subscribe(HASHCHANGE)

    /**
     * occurs when there has been changes to the anchor part of a URL
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [HashChangeEvent]s on its [Flow]
     */
    fun hashchanges(init: HashChangeEvent.() -> Unit): Listener<HashChangeEvent, T> =
        subscribe(HASHCHANGE) { init(); true }

    /**
     * occurs when there has been changes to the anchor part of a URL
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [HashChangeEvent]s on its [Flow]
     */
    fun hashchangesIf(selector: HashChangeEvent.() -> Boolean): Listener<HashChangeEvent, T> =
        subscribe(HASHCHANGE, selector = selector)

    /**
     * occurs when an element gets user input has to use Event as type because Chrome and Safari offer Events instead
     * of InputEvents when selecting from a datalist
     */
    val inputs: Listener<Event, T> get() = subscribe(INPUT)

    /**
     * occurs when an element gets user input has to use Event as type because Chrome and Safari offer Events instead
     * of InputEvents when selecting from a datalist
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun inputs(init: Event.() -> Unit): Listener<Event, T> = subscribe(INPUT) { init(); true }

    /**
     * occurs when an element gets user input has to use Event as type because Chrome and Safari offer Events instead
     * of InputEvents when selecting from a datalist
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun inputsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(INPUT, selector = selector)

    /**
     * occurs when an element is invalid
     */
    val invalids: Listener<Event, T> get() = subscribe(INVALID)

    /**
     * occurs when an element is invalid
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun invalids(init: Event.() -> Unit): Listener<Event, T> = subscribe(INVALID) { init(); true }

    /**
     * occurs when an element is invalid
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun invalidsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(INVALID, selector = selector)

    /**
     * occurs when the user is pressing a key
     */
    val keydowns: Listener<KeyboardEvent, T> get() = subscribe(KEYDOWN)

    /**
     * occurs when the user is pressing a key
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [KeyboardEvent]s on its [Flow]
     */
    fun keydowns(init: KeyboardEvent.() -> Unit): Listener<KeyboardEvent, T> = subscribe(KEYDOWN) { init(); true }

    /**
     * occurs when the user is pressing a key
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [KeyboardEvent]s on its [Flow]
     */
    fun keydownsIf(selector: KeyboardEvent.() -> Boolean): Listener<KeyboardEvent, T> =
        subscribe(KEYDOWN, selector = selector)

    /**
     * occurs when the user presses a key
     */
    val keypresss: Listener<KeyboardEvent, T> get() = subscribe(KEYPRESS)

    /**
     * occurs when the user presses a key
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [KeyboardEvent]s on its [Flow]
     */
    fun keypresss(init: KeyboardEvent.() -> Unit): Listener<KeyboardEvent, T> = subscribe(KEYPRESS) { init(); true }

    /**
     * occurs when the user presses a key
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [KeyboardEvent]s on its [Flow]
     */
    fun keypresssIf(selector: KeyboardEvent.() -> Boolean): Listener<KeyboardEvent, T> =
        subscribe(KEYPRESS, selector = selector)

    /**
     * occurs when the user releases a key
     */
    val keyups: Listener<KeyboardEvent, T> get() = subscribe(KEYUP)

    /**
     * occurs when the user releases a key
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [KeyboardEvent]s on its [Flow]
     */
    fun keyups(init: KeyboardEvent.() -> Unit): Listener<KeyboardEvent, T> = subscribe(KEYUP) { init(); true }

    /**
     * occurs when the user releases a key
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [KeyboardEvent]s on its [Flow]
     */
    fun keyupsIf(selector: KeyboardEvent.() -> Boolean): Listener<KeyboardEvent, T> =
        subscribe(KEYUP, selector = selector)

    /**
     * occurs when an object has loaded
     */
    val loads: Listener<Event, T> get() = subscribe(LOAD)

    /**
     * occurs when an object has loaded
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun loads(init: Event.() -> Unit): Listener<Event, T> = subscribe(LOAD) { init(); true }

    /**
     * occurs when an object has loaded
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun loadsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(LOAD, selector = selector)

    /**
     * occurs when media data is loaded
     */
    val loadeddatas: Listener<Event, T> get() = subscribe(LOADEDDATA)

    /**
     * occurs when media data is loaded
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun loadeddatas(init: Event.() -> Unit): Listener<Event, T> = subscribe(LOADEDDATA) { init(); true }

    /**
     * occurs when media data is loaded
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun loadeddatasIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(LOADEDDATA, selector = selector)

    /**
     * occurs when metadata (like dimensions and duration) are loaded
     */
    val loadedmetadatas: Listener<Event, T> get() = subscribe(LOADEDMETADATA)

    /**
     * occurs when metadata (like dimensions and duration) are loaded
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun loadedmetadatas(init: Event.() -> Unit): Listener<Event, T> = subscribe(LOADEDMETADATA) { init(); true }

    /**
     * occurs when metadata (like dimensions and duration) are loaded
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun loadedmetadatasIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(LOADEDMETADATA, selector = selector)

    /**
     * occurs when the pointer is moved onto an element
     */
    val mouseenters: Listener<MouseEvent, T> get() = subscribe(MOUSEENTER)

    /**
     * occurs when the pointer is moved onto an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseenters(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> = subscribe(MOUSEENTER) { init(); true }

    /**
     * occurs when the pointer is moved onto an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseentersIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(MOUSEENTER, selector = selector)

    /**
     * occurs when the pointer is moved out of an element
     */
    val mouseleaves: Listener<MouseEvent, T> get() = subscribe(MOUSELEAVE)

    /**
     * occurs when the pointer is moved out of an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseleaves(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> = subscribe(MOUSELEAVE) { init(); true }

    /**
     * occurs when the pointer is moved out of an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseleavesIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(MOUSELEAVE, selector = selector)

    /**
     * occurs when the pointer is moving while it is over an element
     */
    val mousemoves: Listener<MouseEvent, T> get() = subscribe(MOUSEMOVE)

    /**
     * occurs when the pointer is moving while it is over an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mousemoves(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> = subscribe(MOUSEMOVE) { init(); true }

    /**
     * occurs when the pointer is moving while it is over an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mousemovesIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(MOUSEMOVE, selector = selector)

    /**
     * occurs when the pointer is moved onto an element, or onto one of its children
     */
    val mouseovers: Listener<MouseEvent, T> get() = subscribe(MOUSEOVER)

    /**
     * occurs when the pointer is moved onto an element, or onto one of its children
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseovers(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> = subscribe(MOUSEOVER) { init(); true }

    /**
     * occurs when the pointer is moved onto an element, or onto one of its children
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseoversIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(MOUSEOVER, selector = selector)

    /**
     * occurs when a user moves the mouse pointer out of an element, or out of one of its children
     */
    val mouseouts: Listener<MouseEvent, T> get() = subscribe(MOUSEOUT)

    /**
     * occurs when a user moves the mouse pointer out of an element, or out of one of its children
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseouts(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> = subscribe(MOUSEOUT) { init(); true }

    /**
     * occurs when a user moves the mouse pointer out of an element, or out of one of its children
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseoutsIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(MOUSEOUT, selector = selector)

    /**
     * occurs when a user releases a mouse button over an element
     */
    val mouseups: Listener<MouseEvent, T> get() = subscribe(MOUSEUP)

    /**
     * occurs when a user releases a mouse button over an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseups(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> = subscribe(MOUSEUP) { init(); true }

    /**
     * occurs when a user releases a mouse button over an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseupsIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(MOUSEUP, selector = selector)

    /**
     * occurs when the browser starts to work offline
     */
    val offlines: Listener<Event, T> get() = subscribe(OFFLINE)

    /**
     * occurs when the browser starts to work offline
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun offlines(init: Event.() -> Unit): Listener<Event, T> = subscribe(OFFLINE) { init(); true }

    /**
     * occurs when the browser starts to work offline
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun offlinesIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(OFFLINE, selector = selector)

    /**
     * occurs when the browser starts to work online
     */
    val onlines: Listener<Event, T> get() = subscribe(ONLINE)

    /**
     * occurs when the browser starts to work online
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun onlines(init: Event.() -> Unit): Listener<Event, T> = subscribe(ONLINE) { init(); true }

    /**
     * occurs when the browser starts to work online
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun onlinesIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(ONLINE, selector = selector)

    /**
     * occurs when a connection with the event source is opened
     */
    val opens: Listener<Event, T> get() = subscribe(OPEN)

    /**
     * occurs when a connection with the event source is opened
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun opens(init: Event.() -> Unit): Listener<Event, T> = subscribe(OPEN) { init(); true }

    /**
     * occurs when a connection with the event source is opened
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun opensIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(OPEN, selector = selector)

    /**
     * occurs when the user navigates away from a webpage
     */
    val pagehides: Listener<PageTransitionEvent, T> get() = subscribe(PAGEHIDE)

    /**
     * occurs when the user navigates away from a webpage
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [PageTransitionEvent]s on its [Flow]
     */
    fun pagehides(init: PageTransitionEvent.() -> Unit): Listener<PageTransitionEvent, T> =
        subscribe(PAGEHIDE) { init(); true }

    /**
     * occurs when the user navigates away from a webpage
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [PageTransitionEvent]s on its [Flow]
     */
    fun pagehidesIf(selector: PageTransitionEvent.() -> Boolean): Listener<PageTransitionEvent, T> =
        subscribe(PAGEHIDE, selector = selector)

    /**
     * occurs when the user navigates to a webpage
     */
    val pageshows: Listener<PageTransitionEvent, T> get() = subscribe(PAGESHOW)

    /**
     * occurs when the user navigates to a webpage
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [PageTransitionEvent]s on its [Flow]
     */
    fun pageshows(init: PageTransitionEvent.() -> Unit): Listener<PageTransitionEvent, T> =
        subscribe(PAGESHOW) { init(); true }

    /**
     * occurs when the user navigates to a webpage
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [PageTransitionEvent]s on its [Flow]
     */
    fun pageshowsIf(selector: PageTransitionEvent.() -> Boolean): Listener<PageTransitionEvent, T> =
        subscribe(PAGESHOW, selector = selector)

    /**
     * occurs when the user pastes some content in an element
     */
    val pastes: Listener<ClipboardEvent, T> get() = subscribe(PASTE)

    /**
     * occurs when the user pastes some content in an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ClipboardEvent]s on its [Flow]
     */
    fun pastes(init: ClipboardEvent.() -> Unit): Listener<ClipboardEvent, T> = subscribe(PASTE) { init(); true }

    /**
     * occurs when the user pastes some content in an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ClipboardEvent]s on its [Flow]
     */
    fun pastesIf(selector: ClipboardEvent.() -> Boolean): Listener<ClipboardEvent, T> =
        subscribe(PASTE, selector = selector)

    /**
     * occurs when the browser starts looking for the specified media
     */
    val loadstarts: Listener<ProgressEvent, T> get() = subscribe(LOADSTART)

    /**
     * occurs when the browser starts looking for the specified media
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ProgressEvent]s on its [Flow]
     */
    fun loadstarts(init: ProgressEvent.() -> Unit): Listener<ProgressEvent, T> = subscribe(LOADSTART) { init(); true }

    /**
     * occurs when the browser starts looking for the specified media
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ProgressEvent]s on its [Flow]
     */
    fun loadstartsIf(selector: ProgressEvent.() -> Boolean): Listener<ProgressEvent, T> =
        subscribe(LOADSTART, selector = selector)

    /**
     * occurs when a message is received through the event source
     */
    val messages: Listener<Event, T> get() = subscribe(MESSAGE)

    /**
     * occurs when a message is received through the event source
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun messages(init: Event.() -> Unit): Listener<Event, T> = subscribe(MESSAGE) { init(); true }

    /**
     * occurs when a message is received through the event source
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun messagesIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(MESSAGE, selector = selector)

    /**
     * occurs when the user presses a mouse button over an element
     */
    val mousedowns: Listener<MouseEvent, T> get() = subscribe(MOUSEDOWN)

    /**
     * occurs when the user presses a mouse button over an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mousedowns(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> = subscribe(MOUSEDOWN) { init(); true }

    /**
     * occurs when the user presses a mouse button over an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mousedownsIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(MOUSEDOWN, selector = selector)

    /**
     * occurs when the media is paused either by the user or programmatically
     */
    val pauses: Listener<Event, T> get() = subscribe(PAUSE)

    /**
     * occurs when the media is paused either by the user or programmatically
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun pauses(init: Event.() -> Unit): Listener<Event, T> = subscribe(PAUSE) { init(); true }

    /**
     * occurs when the media is paused either by the user or programmatically
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun pausesIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(PAUSE, selector = selector)

    /**
     * occurs when the media has been started or is no longer paused
     */
    val plays: Listener<Event, T> get() = subscribe(PLAY)

    /**
     * occurs when the media has been started or is no longer paused
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun plays(init: Event.() -> Unit): Listener<Event, T> = subscribe(PLAY) { init(); true }

    /**
     * occurs when the media has been started or is no longer paused
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun playsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(PLAY, selector = selector)

    /**
     * occurs when the media is playing after having been paused or stopped for buffering
     */
    val playings: Listener<Event, T> get() = subscribe(PLAYING)

    /**
     * occurs when the media is playing after having been paused or stopped for buffering
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun playings(init: Event.() -> Unit): Listener<Event, T> = subscribe(PLAYING) { init(); true }

    /**
     * occurs when the media is playing after having been paused or stopped for buffering
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun playingsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(PLAYING, selector = selector)

    /**
     * occurs when the window's history changes
     */
    val popstates: Listener<PopStateEvent, T> get() = subscribe(POPSTATE)

    /**
     * occurs when the window's history changes
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [PopStateEvent]s on its [Flow]
     */
    fun popstates(init: PopStateEvent.() -> Unit): Listener<PopStateEvent, T> = subscribe(POPSTATE) { init(); true }

    /**
     * occurs when the window's history changes
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [PopStateEvent]s on its [Flow]
     */
    fun popstatesIf(selector: PopStateEvent.() -> Boolean): Listener<PopStateEvent, T> =
        subscribe(POPSTATE, selector = selector)

    /**
     * occurs when the browser is in the process of getting the media data (downloading the media)
     */
    val progresss: Listener<Event, T> get() = subscribe(PROGRESS)

    /**
     * occurs when the browser is in the process of getting the media data (downloading the media)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun progresss(init: Event.() -> Unit): Listener<Event, T> = subscribe(PROGRESS) { init(); true }

    /**
     * occurs when the browser is in the process of getting the media data (downloading the media)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun progresssIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(PROGRESS, selector = selector)

    /**
     * occurs when the playing speed of the media is changed
     */
    val ratechanges: Listener<Event, T> get() = subscribe(RATECHANGE)

    /**
     * occurs when the playing speed of the media is changed
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun ratechanges(init: Event.() -> Unit): Listener<Event, T> = subscribe(RATECHANGE) { init(); true }

    /**
     * occurs when the playing speed of the media is changed
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun ratechangesIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(RATECHANGE, selector = selector)

    /**
     * occurs when the document view is resized
     */
    val resizes: Listener<Event, T> get() = subscribe(RESIZE)

    /**
     * occurs when the document view is resized
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun resizes(init: Event.() -> Unit): Listener<Event, T> = subscribe(RESIZE) { init(); true }

    /**
     * occurs when the document view is resized
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun resizesIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(RESIZE, selector = selector)

    /**
     * occurs when a form is reset
     */
    val resets: Listener<Event, T> get() = subscribe(RESET)

    /**
     * occurs when a form is reset
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun resets(init: Event.() -> Unit): Listener<Event, T> = subscribe(RESET) { init(); true }

    /**
     * occurs when a form is reset
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun resetsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(RESET, selector = selector)

    /**
     * occurs when an element's scrollbar is being scrolled
     */
    val scrolls: Listener<Event, T> get() = subscribe(SCROLL)

    /**
     * occurs when an element's scrollbar is being scrolled
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun scrolls(init: Event.() -> Unit): Listener<Event, T> = subscribe(SCROLL) { init(); true }

    /**
     * occurs when an element's scrollbar is being scrolled
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun scrollsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(SCROLL, selector = selector)

    /**
     * occurs when the user writes something in a search field (for <input="search">)
     */
    val searchs: Listener<Event, T> get() = subscribe(SEARCH)

    /**
     * occurs when the user writes something in a search field (for <input="search">)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun searchs(init: Event.() -> Unit): Listener<Event, T> = subscribe(SEARCH) { init(); true }

    /**
     * occurs when the user writes something in a search field (for <input="search">)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun searchsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(SEARCH, selector = selector)

    /**
     * occurs when the user is finished moving/skipping to a new position in the media
     */
    val seekeds: Listener<Event, T> get() = subscribe(SEEKED)

    /**
     * occurs when the user is finished moving/skipping to a new position in the media
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun seekeds(init: Event.() -> Unit): Listener<Event, T> = subscribe(SEEKED) { init(); true }

    /**
     * occurs when the user is finished moving/skipping to a new position in the media
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun seekedsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(SEEKED, selector = selector)

    /**
     * occurs when the user starts moving/skipping to a new position in the media
     */
    val seekings: Listener<Event, T> get() = subscribe(SEEKING)

    /**
     * occurs when the user starts moving/skipping to a new position in the media
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun seekings(init: Event.() -> Unit): Listener<Event, T> = subscribe(SEEKING) { init(); true }

    /**
     * occurs when the user starts moving/skipping to a new position in the media
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun seekingsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(SEEKING, selector = selector)

    /**
     * occurs after the user selects some text (for <input> and <textarea>)
     */
    val selects: Listener<Event, T> get() = subscribe(SELECT)

    /**
     * occurs after the user selects some text (for <input> and <textarea>)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun selects(init: Event.() -> Unit): Listener<Event, T> = subscribe(SELECT) { init(); true }

    /**
     * occurs after the user selects some text (for <input> and <textarea>)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun selectsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(SELECT, selector = selector)

    /**
     * occurs when a <menu> element is shown as a context menu
     */
    val shows: Listener<Event, T> get() = subscribe(SHOW)

    /**
     * occurs when a <menu> element is shown as a context menu
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun shows(init: Event.() -> Unit): Listener<Event, T> = subscribe(SHOW) { init(); true }

    /**
     * occurs when a <menu> element is shown as a context menu
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun showsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(SHOW, selector = selector)

    /**
     * occurs when the browser is trying to get media data, but data is not available
     */
    val stalleds: Listener<Event, T> get() = subscribe(STALLED)

    /**
     * occurs when the browser is trying to get media data, but data is not available
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun stalleds(init: Event.() -> Unit): Listener<Event, T> = subscribe(STALLED) { init(); true }

    /**
     * occurs when the browser is trying to get media data, but data is not available
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun stalledsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(STALLED, selector = selector)

    /**
     * occurs when a Web Storage area is updated
     */
    val storages: Listener<StorageEvent, T> get() = subscribe(STORAGE)

    /**
     * occurs when a Web Storage area is updated
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [StorageEvent]s on its [Flow]
     */
    fun storages(init: StorageEvent.() -> Unit): Listener<StorageEvent, T> = subscribe(STORAGE) { init(); true }

    /**
     * occurs when a Web Storage area is updated
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [StorageEvent]s on its [Flow]
     */
    fun storagesIf(selector: StorageEvent.() -> Boolean): Listener<StorageEvent, T> =
        subscribe(STORAGE, selector = selector)

    /**
     * occurs when a form is submitted
     */
    val submits: Listener<Event, T> get() = subscribe(SUBMIT)

    /**
     * occurs when a form is submitted
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun submits(init: Event.() -> Unit): Listener<Event, T> = subscribe(SUBMIT) { init(); true }

    /**
     * occurs when a form is submitted
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun submitsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(SUBMIT, selector = selector)

    /**
     * occurs when the browser is intentionally not getting media data
     */
    val suspends: Listener<Event, T> get() = subscribe(SUSPEND)

    /**
     * occurs when the browser is intentionally not getting media data
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun suspends(init: Event.() -> Unit): Listener<Event, T> = subscribe(SUSPEND) { init(); true }

    /**
     * occurs when the browser is intentionally not getting media data
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun suspendsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(SUSPEND, selector = selector)

    /**
     * occurs when the playing position has changed (like when the user fast forwards to a different point in the media)
     */
    val timeupdates: Listener<Event, T> get() = subscribe(TIMEUPDATE)

    /**
     * occurs when the playing position has changed (like when the user fast forwards to a different point in the media)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun timeupdates(init: Event.() -> Unit): Listener<Event, T> = subscribe(TIMEUPDATE) { init(); true }

    /**
     * occurs when the playing position has changed (like when the user fast forwards to a different point in the media)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun timeupdatesIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(TIMEUPDATE, selector = selector)

    /**
     * occurs when the user opens or closes the <details> element
     */
    val toggles: Listener<Event, T> get() = subscribe(TOGGLE)

    /**
     * occurs when the user opens or closes the <details> element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun toggles(init: Event.() -> Unit): Listener<Event, T> = subscribe(TOGGLE) { init(); true }

    /**
     * occurs when the user opens or closes the <details> element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun togglesIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(TOGGLE, selector = selector)

    /**
     * occurs when the touch is interrupted
     */
    val touchcancels: Listener<TouchEvent, T> get() = subscribe(TOUCHCANCEL)

    /**
     * occurs when the touch is interrupted
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchcancels(init: TouchEvent.() -> Unit): Listener<TouchEvent, T> = subscribe(TOUCHCANCEL) { init(); true }

    /**
     * occurs when the touch is interrupted
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchcancelsIf(selector: TouchEvent.() -> Boolean): Listener<TouchEvent, T> =
        subscribe(TOUCHCANCEL, selector = selector)

    /**
     * occurs when a finger is removed from a touch screen
     */
    val touchends: Listener<TouchEvent, T> get() = subscribe(TOUCHEND)

    /**
     * occurs when a finger is removed from a touch screen
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchends(init: TouchEvent.() -> Unit): Listener<TouchEvent, T> = subscribe(TOUCHEND) { init(); true }

    /**
     * occurs when a finger is removed from a touch screen
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchendsIf(selector: TouchEvent.() -> Boolean): Listener<TouchEvent, T> =
        subscribe(TOUCHEND, selector = selector)

    /**
     * occurs when a finger is dragged across the screen
     */
    val touchmoves: Listener<TouchEvent, T> get() = subscribe(TOUCHMOVE)

    /**
     * occurs when a finger is dragged across the screen
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchmoves(init: TouchEvent.() -> Unit): Listener<TouchEvent, T> = subscribe(TOUCHMOVE) { init(); true }

    /**
     * occurs when a finger is dragged across the screen
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchmovesIf(selector: TouchEvent.() -> Boolean): Listener<TouchEvent, T> =
        subscribe(TOUCHMOVE, selector = selector)

    /**
     * occurs when a finger is placed on a touch screen
     */
    val touchstarts: Listener<TouchEvent, T> get() = subscribe(TOUCHSTART)

    /**
     * occurs when a finger is placed on a touch screen
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchstarts(init: TouchEvent.() -> Unit): Listener<TouchEvent, T> = subscribe(TOUCHSTART) { init(); true }

    /**
     * occurs when a finger is placed on a touch screen
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchstartsIf(selector: TouchEvent.() -> Boolean): Listener<TouchEvent, T> =
        subscribe(TOUCHSTART, selector = selector)

    /**
     * occurs when a CSS transition has completed
     */
    val transitionends: Listener<Event, T> get() = subscribe(TRANSITIONEND)

    /**
     * occurs when a CSS transition has completed
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun transitionends(init: Event.() -> Unit): Listener<Event, T> = subscribe(TRANSITIONEND) { init(); true }

    /**
     * occurs when a CSS transition has completed
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun transitionendsIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(TRANSITIONEND, selector = selector)

    /**
     * occurs once a page has unloaded (for <body>)
     */
    val unloads: Listener<Event, T> get() = subscribe(UNLOAD)

    /**
     * occurs once a page has unloaded (for <body>)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun unloads(init: Event.() -> Unit): Listener<Event, T> = subscribe(UNLOAD) { init(); true }

    /**
     * occurs once a page has unloaded (for <body>)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun unloadsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(UNLOAD, selector = selector)

    /**
     * occurs when the volume of the media has changed (includes setting the volume to "mute")
     */
    val volumechanges: Listener<Event, T> get() = subscribe(VOLUMECHANGE)

    /**
     * occurs when the volume of the media has changed (includes setting the volume to "mute")
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun volumechanges(init: Event.() -> Unit): Listener<Event, T> = subscribe(VOLUMECHANGE) { init(); true }

    /**
     * occurs when the volume of the media has changed (includes setting the volume to "mute")
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun volumechangesIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(VOLUMECHANGE, selector = selector)

    /**
     * occurs when the media has paused but is expected to resume (like when the media pauses to buffer more data)
     */
    val waitings: Listener<Event, T> get() = subscribe(WAITING)

    /**
     * occurs when the media has paused but is expected to resume (like when the media pauses to buffer more data)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun waitings(init: Event.() -> Unit): Listener<Event, T> = subscribe(WAITING) { init(); true }

    /**
     * occurs when the media has paused but is expected to resume (like when the media pauses to buffer more data)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun waitingsIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(WAITING, selector = selector)

    /**
     * occurs when the mouse wheel rolls up or down over an element
     */
    val wheels: Listener<WheelEvent, T> get() = subscribe(WHEEL)

    /**
     * occurs when the mouse wheel rolls up or down over an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [WheelEvent]s on its [Flow]
     */
    fun wheels(init: WheelEvent.() -> Unit): Listener<WheelEvent, T> = subscribe(WHEEL) { init(); true }

    /**
     * occurs when the mouse wheel rolls up or down over an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [WheelEvent]s on its [Flow]
     */
    fun wheelsIf(selector: WheelEvent.() -> Boolean): Listener<WheelEvent, T> = subscribe(WHEEL, selector = selector)

    /**
     * occurs when the loading of a media is aborted
     */
    val abortsCaptured: Listener<Event, T> get() = subscribe(ABORT, true)

    /**
     * occurs when the loading of a media is aborted
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun abortsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(ABORT, true) { init(); true }

    /**
     * occurs when the loading of a media is aborted
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun abortsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(ABORT, true, selector = selector)

    /**
     * occurs when a page has started printing, or if the print dialogue box has been closed
     */
    val afterprintsCaptured: Listener<Event, T> get() = subscribe(AFTERPRINT, true)

    /**
     * occurs when a page has started printing, or if the print dialogue box has been closed
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun afterprintsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(AFTERPRINT, true) { init(); true }

    /**
     * occurs when a page has started printing, or if the print dialogue box has been closed
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun afterprintsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(AFTERPRINT, true, selector = selector)

    /**
     * occurs when a CSS Animation has completed
     */
    val animationendsCaptured: Listener<Event, T> get() = subscribe(ANIMATIONEND, true)

    /**
     * occurs when a CSS Animation has completed
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun animationendsCaptured(init: Event.() -> Unit): Listener<Event, T> =
        subscribe(ANIMATIONEND, true) { init(); true }

    /**
     * occurs when a CSS Animation has completed
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun animationendsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(ANIMATIONEND, true, selector = selector)

    /**
     * occurs when an iteration of a CSS Animation ends, and another one begins
     */
    val animationiterationsCaptured: Listener<Event, T> get() = subscribe(ANIMATIONITERATION, true)

    /**
     * occurs when an iteration of a CSS Animation ends, and another one begins
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun animationiterationsCaptured(init: Event.() -> Unit): Listener<Event, T> =
        subscribe(ANIMATIONITERATION, true) { init(); true }

    /**
     * occurs when an iteration of a CSS Animation ends, and another one begins
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun animationiterationsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(ANIMATIONITERATION, true, selector = selector)

    /**
     * occurs when a CSS Animation has started
     */
    val animationstartsCaptured: Listener<Event, T> get() = subscribe(ANIMATIONSTART, true)

    /**
     * occurs when a CSS Animation has completed
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun animationstartsCaptured(init: Event.() -> Unit): Listener<Event, T> =
        subscribe(ANIMATIONSTART, true) { init(); true }

    /**
     * occurs when a CSS Animation has started
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun animationstartsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(ANIMATIONSTART, true, selector = selector)

    /**
     * occurs when a page is about to be printed
     */
    val beforeprintsCaptured: Listener<Event, T> get() = subscribe(BEFOREPRINT, true)

    /**
     * occurs when a page is about to be printed
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun beforeprintsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(BEFOREPRINT, true) { init(); true }

    /**
     * occurs when a page is about to be printed
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun beforeprintsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(BEFOREPRINT, true, selector = selector)

    /**
     * occurs before the document is about to be unloaded
     */
    val beforeunloadsCaptured: Listener<Event, T> get() = subscribe(BEFOREUNLOAD, true)

    /**
     * occurs before the document is about to be unloaded
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun beforeunloadsCaptured(init: Event.() -> Unit): Listener<Event, T> =
        subscribe(BEFOREUNLOAD, true) { init(); true }

    /**
     * occurs before the document is about to be unloaded
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun beforeunloadsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(BEFOREUNLOAD, true, selector = selector)

    /**
     * occurs when an element loses focus
     */
    val blursCaptured: Listener<FocusEvent, T> get() = subscribe(BLUR, true)

    /**
     * occurs when an element loses focus
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun blursCaptured(init: FocusEvent.() -> Unit): Listener<FocusEvent, T> = subscribe(BLUR, true) { init(); true }

    /**
     * occurs when an element loses focus
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun blursCapturedIf(selector: FocusEvent.() -> Boolean): Listener<FocusEvent, T> =
        subscribe(BLUR, true, selector = selector)

    /**
     * occurs when the browser can start playing the media (when it has buffered enough to begin)
     */
    val canplaysCaptured: Listener<Event, T> get() = subscribe(CANPLAY, true)

    /**
     * occurs when the browser can start playing the media (when it has buffered enough to begin)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun canplaysCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(CANPLAY, true) { init(); true }

    /**
     * occurs when the browser can start playing the media (when it has buffered enough to begin)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun canplaysCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(CANPLAY, true, selector = selector)

    /**
     * occurs when the browser can play through the media without stopping for buffering
     */
    val canplaythroughsCaptured: Listener<Event, T> get() = subscribe(CANPLAYTHROUGH, true)

    /**
     * occurs when the browser can play through the media without stopping for buffering
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun canplaythroughsCaptured(init: Event.() -> Unit): Listener<Event, T> =
        subscribe(CANPLAYTHROUGH, true) { init(); true }

    /**
     * occurs when the browser can play through the media without stopping for buffering
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun canplaythroughsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(CANPLAYTHROUGH, true, selector = selector)

    /**
     * occurs when the content of a form element, the selection, or the checked state have changed
     * (for `<input>`, `<select>`, and `<textarea>`)
     */
    val changesCaptured: Listener<Event, T> get() = subscribe(CHANGE, true)

    /**
     * occurs when the content of a form element, the selection, or the checked state have changed
     * (for `<input>`, `<select>`, and `<textarea>`)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun changesCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(CHANGE, true) { init(); true }

    /**
     * occurs when the content of a form element, the selection, or the checked state have changed
     * (for `<input>`, `<select>`, and `<textarea>`)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun changesCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(CHANGE, true, selector = selector)

    /**
     * occurs when the user clicks on an element
     */
    val clicksCaptured: Listener<MouseEvent, T> get() = subscribe(CLICK, true)

    /**
     * occurs when the user clicks on an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun clicksCaptured(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> = subscribe(CLICK, true) { init(); true }

    /**
     * occurs when the user clicks on an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun clicksCapturedIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(CLICK, true, selector = selector)

    /**
     * occurs when the user right-clicks on an element to open a context menu
     */
    val contextmenusCaptured: Listener<MouseEvent, T> get() = subscribe(CONTEXTMENU, true)

    /**
     * occurs when the user right-clicks on an element to open a context menu
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun contextmenusCaptured(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> =
        subscribe(CONTEXTMENU, true) { init(); true }

    /**
     * occurs when the user right-clicks on an element to open a context menu
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun contextmenusCapturedIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(CONTEXTMENU, true, selector = selector)

    /**
     * occurs when the user copies the content of an element
     */
    val copysCaptured: Listener<ClipboardEvent, T> get() = subscribe(COPY, true)

    /**
     * occurs when the user copies the content of an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ClipboardEvent]s on its [Flow]
     */
    fun copysCaptured(init: ClipboardEvent.() -> Unit): Listener<ClipboardEvent, T> =
        subscribe(COPY, true) { init(); true }

    /**
     * occurs when the user copies the content of an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ClipboardEvent]s on its [Flow]
     */
    fun copysCapturedIf(selector: ClipboardEvent.() -> Boolean): Listener<ClipboardEvent, T> =
        subscribe(COPY, true, selector = selector)

    /**
     * occurs when the user cuts the content of an element
     */
    val cutsCaptured: Listener<ClipboardEvent, T> get() = subscribe(CUT, true)

    /**
     * occurs when the user cuts the content of an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ClipboardEvent]s on its [Flow]
     */
    fun cutsCaptured(init: ClipboardEvent.() -> Unit): Listener<ClipboardEvent, T> =
        subscribe(CUT, true) { init(); true }

    /**
     * occurs when the user cuts the content of an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ClipboardEvent]s on its [Flow]
     */
    fun cutsCapturedIf(selector: ClipboardEvent.() -> Boolean): Listener<ClipboardEvent, T> =
        subscribe(CUT, true, selector = selector)

    /**
     * occurs when the user double-clicks on an element
     */
    val dblclicksCaptured: Listener<MouseEvent, T> get() = subscribe(DBLCLICK, true)

    /**
     * occurs when the user double-clicks on an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun dblclicksCaptured(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> =
        subscribe(DBLCLICK, true) { init(); true }

    /**
     * occurs when the user double-clicks on an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun dblclicksCapturedIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(DBLCLICK, true, selector = selector)

    /**
     * occurs when an element is being dragged
     */
    val dragsCaptured: Listener<DragEvent, T> get() = subscribe(DRAG, true)

    /**
     * occurs when an element is being dragged
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragsCaptured(init: DragEvent.() -> Unit): Listener<DragEvent, T> = subscribe(DRAG, true) { init(); true }

    /**
     * occurs when an element is being dragged
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragsCapturedIf(selector: DragEvent.() -> Boolean): Listener<DragEvent, T> =
        subscribe(DRAG, true, selector = selector)

    /**
     * occurs when the user has finished dragging an element
     */
    val dragendsCaptured: Listener<DragEvent, T> get() = subscribe(DRAGEND, true)

    /**
     * occurs when the user has finished dragging an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragendsCaptured(init: DragEvent.() -> Unit): Listener<DragEvent, T> = subscribe(DRAGEND, true) { init(); true }

    /**
     * occurs when the user has finished dragging an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragendsCapturedIf(selector: DragEvent.() -> Boolean): Listener<DragEvent, T> =
        subscribe(DRAGEND, true, selector = selector)

    /**
     * occurs when the dragged element enters the drop target
     */
    val dragentersCaptured: Listener<DragEvent, T> get() = subscribe(DRAGENTER, true)

    /**
     * occurs when the dragged element enters the drop target
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragentersCaptured(init: DragEvent.() -> Unit): Listener<DragEvent, T> =
        subscribe(DRAGENTER, true) { init(); true }

    /**
     * occurs when the dragged element enters the drop target
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragentersCapturedIf(selector: DragEvent.() -> Boolean): Listener<DragEvent, T> =
        subscribe(DRAGENTER, true, selector = selector)

    /**
     * occurs when the dragged element leaves the drop target
     */
    val dragleavesCaptured: Listener<DragEvent, T> get() = subscribe(DRAGLEAVE, true)

    /**
     * occurs when the dragged element leaves the drop target
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragleavesCaptured(init: DragEvent.() -> Unit): Listener<DragEvent, T> =
        subscribe(DRAGLEAVE, true) { init(); true }

    /**
     * occurs when the dragged element leaves the drop target
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragleavesCapturedIf(selector: DragEvent.() -> Boolean): Listener<DragEvent, T> =
        subscribe(DRAGLEAVE, true, selector = selector)

    /**
     * occurs when the dragged element is over the drop target
     */
    val dragoversCaptured: Listener<DragEvent, T> get() = subscribe(DRAGOVER, true)

    /**
     * occurs when the dragged element is over the drop target
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragoversCaptured(init: DragEvent.() -> Unit): Listener<DragEvent, T> =
        subscribe(DRAGOVER, true) { init(); true }

    /**
     * occurs when the dragged element is over the drop target
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragoversCapturedIf(selector: DragEvent.() -> Boolean): Listener<DragEvent, T> =
        subscribe(DRAGOVER, true, selector = selector)

    /**
     * occurs when the user starts to drag an element
     */
    val dragstartsCaptured: Listener<DragEvent, T> get() = subscribe(DRAGSTART, true)

    /**
     * occurs when the user starts to drag an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragstartsCaptured(init: DragEvent.() -> Unit): Listener<DragEvent, T> =
        subscribe(DRAGSTART, true) { init(); true }

    /**
     * occurs when the user starts to drag an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dragstartsCapturedIf(selector: DragEvent.() -> Boolean): Listener<DragEvent, T> =
        subscribe(DRAGSTART, true, selector = selector)

    /**
     * occurs when the dragged element is dropped on the drop target
     */
    val dropsCaptured: Listener<DragEvent, T> get() = subscribe(DROP, true)

    /**
     * occurs when the dragged element is dropped on the drop target
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dropsCaptured(init: DragEvent.() -> Unit): Listener<DragEvent, T> = subscribe(DROP, true) { init(); true }

    /**
     * occurs when the dragged element is dropped on the drop target
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [DragEvent]s on its [Flow]
     */
    fun dropsCapturedIf(selector: DragEvent.() -> Boolean): Listener<DragEvent, T> =
        subscribe(DROP, true, selector = selector)

    /**
     * occurs when the duration of the media is changed
     */
    val durationchangesCaptured: Listener<Event, T> get() = subscribe(DURATIONCHANGE, true)

    /**
     * occurs when the duration of the media is changed
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun durationchangesCaptured(init: Event.() -> Unit): Listener<Event, T> =
        subscribe(DURATIONCHANGE, true) { init(); true }

    /**
     * occurs when the duration of the media is changed
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun durationchangesCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(DURATIONCHANGE, true, selector = selector)

    /**
     * occurs when the media has reach the end (useful for messages like "thanks for listening")
     */
    val endedsCaptured: Listener<Event, T> get() = subscribe(ENDED, true)

    /**
     * occurs when the media has reach the end (useful for messages like "thanks for listening")
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun endedsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(ENDED, true) { init(); true }

    /**
     * occurs when the media has reach the end (useful for messages like "thanks for listening")
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun endedsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(ENDED, true, selector = selector)

    /**
     * occurs when a CSS Animation has completed
     */
    val errorsCaptured: Listener<Event, T> get() = subscribe(ERROR, true)

    /**
     * occurs when there is an error while loading an external file (e.g. a document or an image).
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun errorsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(ERROR, true) { init(); true }

    /**
     * occurs when there is an error while loading an external file (e.g. a document or an image).
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun errorsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(ERROR, true, selector = selector)

    /**
     * occurs when an element gets focus
     */
    val focussCaptured: Listener<FocusEvent, T> get() = subscribe(FOCUS, true)

    /**
     * occurs when an element gets focus
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun focussCaptured(init: FocusEvent.() -> Unit): Listener<FocusEvent, T> = subscribe(FOCUS, true) { init(); true }

    /**
     * occurs when an element gets focus
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun focussCapturedIf(selector: FocusEvent.() -> Boolean): Listener<FocusEvent, T> =
        subscribe(FOCUS, true, selector = selector)

    /**
     * occurs when an element is about to get focus
     */
    val focusinsCaptured: Listener<FocusEvent, T> get() = subscribe(FOCUSIN, true)

    /**
     * occurs when an element is about to get focus
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun focusinsCaptured(init: FocusEvent.() -> Unit): Listener<FocusEvent, T> =
        subscribe(FOCUSIN, true) { init(); true }

    /**
     * occurs when an element is about to get focus
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun focusinsCapturedIf(selector: FocusEvent.() -> Boolean): Listener<FocusEvent, T> =
        subscribe(FOCUSIN, true, selector = selector)

    /**
     * occurs when an element is about to lose focus
     */
    val focusoutsCaptured: Listener<FocusEvent, T> get() = subscribe(FOCUSOUT, true)

    /**
     * occurs when an element is about to lose focus
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun focusoutsCaptured(init: FocusEvent.() -> Unit): Listener<FocusEvent, T> =
        subscribe(FOCUSOUT, true) { init(); true }

    /**
     * occurs when an element is about to lose focus
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [FocusEvent]s on its [Flow]
     */
    fun focusoutsCapturedIf(selector: FocusEvent.() -> Boolean): Listener<FocusEvent, T> =
        subscribe(FOCUSOUT, true, selector = selector)

    /**
     * occurs when an element is displayed in fullscreen mode
     */
    val fullscreenchangesCaptured: Listener<Event, T> get() = subscribe(FULLSCREENCHANGE, true)

    /**
     * occurs when an element is displayed in fullscreen mode
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun fullscreenchangesCaptured(init: Event.() -> Unit): Listener<Event, T> =
        subscribe(FULLSCREENCHANGE, true) { init(); true }

    /**
     * occurs when an element is displayed in fullscreen mode
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun fullscreenchangesCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(FULLSCREENCHANGE, true, selector = selector)

    /**
     * occurs when an element can not be displayed in fullscreen mode
     */
    val fullscreenerrorsCaptured: Listener<Event, T> get() = subscribe(FULLSCREENERROR, true)

    /**
     * occurs when an element can not be displayed in fullscreen mode
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun fullscreenerrorsCaptured(init: Event.() -> Unit): Listener<Event, T> =
        subscribe(FULLSCREENERROR, true) { init(); true }

    /**
     * occurs when an element can not be displayed in fullscreen mode
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun fullscreenerrorsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(FULLSCREENERROR, true, selector = selector)

    /**
     * occurs when there has been changes to the anchor part of a URL
     */
    val hashchangesCaptured: Listener<HashChangeEvent, T> get() = subscribe(HASHCHANGE, true)

    /**
     * occurs when there has been changes to the anchor part of a URL
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [HashChangeEvent]s on its [Flow]
     */
    fun hashchangesCaptured(init: HashChangeEvent.() -> Unit): Listener<HashChangeEvent, T> =
        subscribe(HASHCHANGE, true) { init(); true }

    /**
     * occurs when there has been changes to the anchor part of a URL
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [HashChangeEvent]s on its [Flow]
     */
    fun hashchangesCapturedIf(selector: HashChangeEvent.() -> Boolean): Listener<HashChangeEvent, T> =
        subscribe(HASHCHANGE, true, selector = selector)

    /**
     * occurs when an element gets user input has to use Event as type because Chrome and Safari offer Events instead
     * of InputEvents when selecting from a datalist
     */
    val inputsCaptured: Listener<Event, T> get() = subscribe(INPUT, true)

    /**
     * occurs when an element gets user input has to use Event as type because Chrome and Safari offer Events instead
     * of InputEvents when selecting from a datalist
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun inputsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(INPUT, true) { init(); true }

    /**
     * occurs when an element gets user input has to use Event as type because Chrome and Safari offer Events instead
     * of InputEvents when selecting from a datalist
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun inputsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(INPUT, true, selector = selector)

    /**
     * occurs when an element is invalid
     */
    val invalidsCaptured: Listener<Event, T> get() = subscribe(INVALID, true)

    /**
     * occurs when an element is invalid
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun invalidsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(INVALID, true) { init(); true }

    /**
     * occurs when an element is invalid
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun invalidsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(INVALID, true, selector = selector)

    /**
     * occurs when the user is pressing a key
     */
    val keydownsCaptured: Listener<KeyboardEvent, T> get() = subscribe(KEYDOWN, true)

    /**
     * occurs when the user is pressing a key
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [KeyboardEvent]s on its [Flow]
     */
    fun keydownsCaptured(init: KeyboardEvent.() -> Unit): Listener<KeyboardEvent, T> =
        subscribe(KEYDOWN, true) { init(); true }

    /**
     * occurs when the user is pressing a key
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [KeyboardEvent]s on its [Flow]
     */
    fun keydownsCapturedIf(selector: KeyboardEvent.() -> Boolean): Listener<KeyboardEvent, T> =
        subscribe(KEYDOWN, true, selector = selector)

    /**
     * occurs when the user presses a key
     */
    val keypresssCaptured: Listener<KeyboardEvent, T> get() = subscribe(KEYPRESS, true)

    /**
     * occurs when the user presses a key
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [KeyboardEvent]s on its [Flow]
     */
    fun keypresssCaptured(init: KeyboardEvent.() -> Unit): Listener<KeyboardEvent, T> =
        subscribe(KEYPRESS, true) { init(); true }

    /**
     * occurs when the user presses a key
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [KeyboardEvent]s on its [Flow]
     */
    fun keypresssCapturedIf(selector: KeyboardEvent.() -> Boolean): Listener<KeyboardEvent, T> =
        subscribe(KEYPRESS, true, selector = selector)

    /**
     * occurs when the user releases a key
     */
    val keyupsCaptured: Listener<KeyboardEvent, T> get() = subscribe(KEYUP, true)

    /**
     * occurs when the user releases a key
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [KeyboardEvent]s on its [Flow]
     */
    fun keyupsCaptured(init: KeyboardEvent.() -> Unit): Listener<KeyboardEvent, T> =
        subscribe(KEYUP, true) { init(); true }

    /**
     * occurs when the user releases a key
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [KeyboardEvent]s on its [Flow]
     */
    fun keyupsCapturedIf(selector: KeyboardEvent.() -> Boolean): Listener<KeyboardEvent, T> =
        subscribe(KEYUP, true, selector = selector)

    /**
     * occurs when an object has loaded
     */
    val loadsCaptured: Listener<Event, T> get() = subscribe(LOAD, true)

    /**
     * occurs when an object has loaded
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun loadsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(LOAD, true) { init(); true }

    /**
     * occurs when an object has loaded
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun loadsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(LOAD, true, selector = selector)

    /**
     * occurs when media data is loaded
     */
    val loadeddatasCaptured: Listener<Event, T> get() = subscribe(LOADEDDATA, true)

    /**
     * occurs when media data is loaded
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun loadeddatasCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(LOADEDDATA, true) { init(); true }

    /**
     * occurs when media data is loaded
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun loadeddatasCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(LOADEDDATA, true, selector = selector)

    /**
     * occurs when metadata (like dimensions and duration) are loaded
     */
    val loadedmetadatasCaptured: Listener<Event, T> get() = subscribe(LOADEDMETADATA, true)

    /**
     * occurs when metadata (like dimensions and duration) are loaded
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun loadedmetadatasCaptured(init: Event.() -> Unit): Listener<Event, T> =
        subscribe(LOADEDMETADATA, true) { init(); true }

    /**
     * occurs when metadata (like dimensions and duration) are loaded
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun loadedmetadatasCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(LOADEDMETADATA, true, selector = selector)

    /**
     * occurs when the pointer is moved onto an element
     */
    val mouseentersCaptured: Listener<MouseEvent, T> get() = subscribe(MOUSEENTER, true)

    /**
     * occurs when the pointer is moved onto an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseentersCaptured(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> =
        subscribe(MOUSEENTER, true) { init(); true }

    /**
     * occurs when the pointer is moved onto an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseentersCapturedIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(MOUSEENTER, true, selector = selector)

    /**
     * occurs when the pointer is moved out of an element
     */
    val mouseleavesCaptured: Listener<MouseEvent, T> get() = subscribe(MOUSELEAVE, true)

    /**
     * occurs when the pointer is moved out of an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseleavesCaptured(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> =
        subscribe(MOUSELEAVE, true) { init(); true }

    /**
     * occurs when the pointer is moved out of an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseleavesCapturedIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(MOUSELEAVE, true, selector = selector)

    /**
     * occurs when the pointer is moving while it is over an element
     */
    val mousemovesCaptured: Listener<MouseEvent, T> get() = subscribe(MOUSEMOVE, true)

    /**
     * occurs when the pointer is moving while it is over an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mousemovesCaptured(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> =
        subscribe(MOUSEMOVE, true) { init(); true }

    /**
     * occurs when the pointer is moving while it is over an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mousemovesCapturedIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(MOUSEMOVE, true, selector = selector)

    /**
     * occurs when the pointer is moved onto an element, or onto one of its children
     */
    val mouseoversCaptured: Listener<MouseEvent, T> get() = subscribe(MOUSEOVER, true)

    /**
     * occurs when the pointer is moved onto an element, or onto one of its children
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseoversCaptured(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> =
        subscribe(MOUSEOVER, true) { init(); true }

    /**
     * occurs when the pointer is moved onto an element, or onto one of its children
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseoversCapturedIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(MOUSEOVER, true, selector = selector)

    /**
     * occurs when a user moves the mouse pointer out of an element, or out of one of its children
     */
    val mouseoutsCaptured: Listener<MouseEvent, T> get() = subscribe(MOUSEOUT, true)

    /**
     * occurs when a user moves the mouse pointer out of an element, or out of one of its children
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseoutsCaptured(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> =
        subscribe(MOUSEOUT, true) { init(); true }

    /**
     * occurs when a user moves the mouse pointer out of an element, or out of one of its children
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseoutsCapturedIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(MOUSEOUT, true, selector = selector)

    /**
     * occurs when a user releases a mouse button over an element
     */
    val mouseupsCaptured: Listener<MouseEvent, T> get() = subscribe(MOUSEUP, true)

    /**
     * occurs when a user releases a mouse button over an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseupsCaptured(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> =
        subscribe(MOUSEUP, true) { init(); true }

    /**
     * occurs when a user releases a mouse button over an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mouseupsCapturedIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(MOUSEUP, true, selector = selector)

    /**
     * occurs when the browser starts to work offline
     */
    val offlinesCaptured: Listener<Event, T> get() = subscribe(OFFLINE, true)

    /**
     * occurs when the browser starts to work offline
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun offlinesCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(OFFLINE, true) { init(); true }

    /**
     * occurs when the browser starts to work offline
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun offlinesCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(OFFLINE, true, selector = selector)

    /**
     * occurs when the browser starts to work online
     */
    val onlinesCaptured: Listener<Event, T> get() = subscribe(ONLINE, true)

    /**
     * occurs when the browser starts to work online
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun onlinesCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(ONLINE, true) { init(); true }

    /**
     * occurs when the browser starts to work online
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun onlinesCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(ONLINE, true, selector = selector)

    /**
     * occurs when a connection with the event source is opened
     */
    val opensCaptured: Listener<Event, T> get() = subscribe(OPEN, true)

    /**
     * occurs when a connection with the event source is opened
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun opensCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(OPEN, true) { init(); true }

    /**
     * occurs when a connection with the event source is opened
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun opensCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(OPEN, true, selector = selector)

    /**
     * occurs when the user navigates away from a webpage
     */
    val pagehidesCaptured: Listener<PageTransitionEvent, T> get() = subscribe(PAGEHIDE, true)

    /**
     * occurs when the user navigates away from a webpage
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [PageTransitionEvent]s on its [Flow]
     */
    fun pagehidesCaptured(init: PageTransitionEvent.() -> Unit): Listener<PageTransitionEvent, T> =
        subscribe(PAGEHIDE, true) { init(); true }

    /**
     * occurs when the user navigates away from a webpage
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [PageTransitionEvent]s on its [Flow]
     */
    fun pagehidesCapturedIf(selector: PageTransitionEvent.() -> Boolean): Listener<PageTransitionEvent, T> =
        subscribe(PAGEHIDE, true, selector = selector)

    /**
     * occurs when the user navigates to a webpage
     */
    val pageshowsCaptured: Listener<PageTransitionEvent, T> get() = subscribe(PAGESHOW, true)

    /**
     * occurs when the user navigates to a webpage
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [PageTransitionEvent]s on its [Flow]
     */
    fun pageshowsCaptured(init: PageTransitionEvent.() -> Unit): Listener<PageTransitionEvent, T> =
        subscribe(PAGESHOW, true) { init(); true }

    /**
     * occurs when the user navigates to a webpage
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [PageTransitionEvent]s on its [Flow]
     */
    fun pageshowsCapturedIf(selector: PageTransitionEvent.() -> Boolean): Listener<PageTransitionEvent, T> =
        subscribe(PAGESHOW, true, selector = selector)

    /**
     * occurs when the user pastes some content in an element
     */
    val pastesCaptured: Listener<ClipboardEvent, T> get() = subscribe(PASTE, true)

    /**
     * occurs when the user pastes some content in an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ClipboardEvent]s on its [Flow]
     */
    fun pastesCaptured(init: ClipboardEvent.() -> Unit): Listener<ClipboardEvent, T> =
        subscribe(PASTE, true) { init(); true }

    /**
     * occurs when the user pastes some content in an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ClipboardEvent]s on its [Flow]
     */
    fun pastesCapturedIf(selector: ClipboardEvent.() -> Boolean): Listener<ClipboardEvent, T> =
        subscribe(PASTE, true, selector = selector)

    /**
     * occurs when the browser starts looking for the specified media
     */
    val loadstartsCaptured: Listener<ProgressEvent, T> get() = subscribe(LOADSTART, true)

    /**
     * occurs when the browser starts looking for the specified media
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ProgressEvent]s on its [Flow]
     */
    fun loadstartsCaptured(init: ProgressEvent.() -> Unit): Listener<ProgressEvent, T> =
        subscribe(LOADSTART, true) { init(); true }

    /**
     * occurs when the browser starts looking for the specified media
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [ProgressEvent]s on its [Flow]
     */
    fun loadstartsCapturedIf(selector: ProgressEvent.() -> Boolean): Listener<ProgressEvent, T> =
        subscribe(LOADSTART, true, selector = selector)

    /**
     * occurs when a message is received through the event source
     */
    val messagesCaptured: Listener<Event, T> get() = subscribe(MESSAGE, true)

    /**
     * occurs when a message is received through the event source
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun messagesCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(MESSAGE, true) { init(); true }

    /**
     * occurs when a message is received through the event source
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun messagesCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(MESSAGE, true, selector = selector)

    /**
     * occurs when the user presses a mouse button over an element
     */
    val mousedownsCaptured: Listener<MouseEvent, T> get() = subscribe(MOUSEDOWN, true)

    /**
     * occurs when the user presses a mouse button over an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mousedownsCaptured(init: MouseEvent.() -> Unit): Listener<MouseEvent, T> =
        subscribe(MOUSEDOWN, true) { init(); true }

    /**
     * occurs when the user presses a mouse button over an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [MouseEvent]s on its [Flow]
     */
    fun mousedownsCapturedIf(selector: MouseEvent.() -> Boolean): Listener<MouseEvent, T> =
        subscribe(MOUSEDOWN, true, selector = selector)

    /**
     * occurs when the media is paused either by the user or programmatically
     */
    val pausesCaptured: Listener<Event, T> get() = subscribe(PAUSE, true)

    /**
     * occurs when the media is paused either by the user or programmatically
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun pausesCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(PAUSE, true) { init(); true }

    /**
     * occurs when the media is paused either by the user or programmatically
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun pausesCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(PAUSE, true, selector = selector)

    /**
     * occurs when the media has been started or is no longer paused
     */
    val playsCaptured: Listener<Event, T> get() = subscribe(PLAY, true)

    /**
     * occurs when the media has been started or is no longer paused
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun playsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(PLAY, true) { init(); true }

    /**
     * occurs when the media has been started or is no longer paused
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun playsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(PLAY, true, selector = selector)

    /**
     * occurs when the media is playing after having been paused or stopped for buffering
     */
    val playingsCaptured: Listener<Event, T> get() = subscribe(PLAYING, true)

    /**
     * occurs when the media is playing after having been paused or stopped for buffering
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun playingsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(PLAYING, true) { init(); true }

    /**
     * occurs when the media is playing after having been paused or stopped for buffering
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun playingsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(PLAYING, true, selector = selector)

    /**
     * occurs when the window's history changes
     */
    val popstatesCaptured: Listener<PopStateEvent, T> get() = subscribe(POPSTATE, true)

    /**
     * occurs when the window's history changes
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [PopStateEvent]s on its [Flow]
     */
    fun popstatesCaptured(init: PopStateEvent.() -> Unit): Listener<PopStateEvent, T> =
        subscribe(POPSTATE, true) { init(); true }

    /**
     * occurs when the window's history changes
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [PopStateEvent]s on its [Flow]
     */
    fun popstatesCapturedIf(selector: PopStateEvent.() -> Boolean): Listener<PopStateEvent, T> =
        subscribe(POPSTATE, true, selector = selector)

    /**
     * occurs when the browser is in the process of getting the media data (downloading the media)
     */
    val progresssCaptured: Listener<Event, T> get() = subscribe(PROGRESS, true)

    /**
     * occurs when the browser is in the process of getting the media data (downloading the media)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun progresssCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(PROGRESS, true) { init(); true }

    /**
     * occurs when the browser is in the process of getting the media data (downloading the media)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun progresssCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(PROGRESS, true, selector = selector)

    /**
     * occurs when the playing speed of the media is changed
     */
    val ratechangesCaptured: Listener<Event, T> get() = subscribe(RATECHANGE, true)

    /**
     * occurs when the playing speed of the media is changed
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun ratechangesCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(RATECHANGE, true) { init(); true }

    /**
     * occurs when the playing speed of the media is changed
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun ratechangesCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(RATECHANGE, true, selector = selector)

    /**
     * occurs when the document view is resized
     */
    val resizesCaptured: Listener<Event, T> get() = subscribe(RESIZE, true)

    /**
     * occurs when the document view is resized
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun resizesCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(RESIZE, true) { init(); true }

    /**
     * occurs when the document view is resized
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun resizesCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(RESIZE, true, selector = selector)

    /**
     * occurs when a form is reset
     */
    val resetsCaptured: Listener<Event, T> get() = subscribe(RESET, true)

    /**
     * occurs when a form is reset
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun resetsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(RESET, true) { init(); true }

    /**
     * occurs when a form is reset
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun resetsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(RESET, true, selector = selector)

    /**
     * occurs when an element's scrollbar is being scrolled
     */
    val scrollsCaptured: Listener<Event, T> get() = subscribe(SCROLL, true)

    /**
     * occurs when an element's scrollbar is being scrolled
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun scrollsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(SCROLL, true) { init(); true }

    /**
     * occurs when an element's scrollbar is being scrolled
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun scrollsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(SCROLL, true, selector = selector)

    /**
     * occurs when the user writes something in a search field (for <input="search">)
     */
    val searchsCaptured: Listener<Event, T> get() = subscribe(SEARCH, true)

    /**
     * occurs when the user writes something in a search field (for <input="search">)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun searchsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(SEARCH, true) { init(); true }

    /**
     * occurs when the user writes something in a search field (for <input="search">)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun searchsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(SEARCH, true, selector = selector)

    /**
     * occurs when the user is finished moving/skipping to a new position in the media
     */
    val seekedsCaptured: Listener<Event, T> get() = subscribe(SEEKED, true)

    /**
     * occurs when the user is finished moving/skipping to a new position in the media
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun seekedsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(SEEKED, true) { init(); true }

    /**
     * occurs when the user is finished moving/skipping to a new position in the media
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun seekedsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(SEEKED, true, selector = selector)

    /**
     * occurs when the user starts moving/skipping to a new position in the media
     */
    val seekingsCaptured: Listener<Event, T> get() = subscribe(SEEKING, true)

    /**
     * occurs when the user starts moving/skipping to a new position in the media
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun seekingsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(SEEKING, true) { init(); true }

    /**
     * occurs when the user starts moving/skipping to a new position in the media
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun seekingsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(SEEKING, true, selector = selector)

    /**
     * occurs after the user selects some text (for <input> and <textarea>)
     */
    val selectsCaptured: Listener<Event, T> get() = subscribe(SELECT, true)

    /**
     * occurs after the user selects some text (for <input> and <textarea>)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun selectsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(SELECT, true) { init(); true }

    /**
     * occurs after the user selects some text (for <input> and <textarea>)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun selectsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(SELECT, true, selector = selector)

    /**
     * occurs when a <menu> element is shown as a context menu
     */
    val showsCaptured: Listener<Event, T> get() = subscribe(SHOW, true)

    /**
     * occurs when a <menu> element is shown as a context menu
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun showsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(SHOW, true) { init(); true }

    /**
     * occurs when a <menu> element is shown as a context menu
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun showsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> = subscribe(SHOW, true, selector = selector)

    /**
     * occurs when the browser is trying to get media data, but data is not available
     */
    val stalledsCaptured: Listener<Event, T> get() = subscribe(STALLED, true)

    /**
     * occurs when the browser is trying to get media data, but data is not available
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun stalledsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(STALLED, true) { init(); true }

    /**
     * occurs when the browser is trying to get media data, but data is not available
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun stalledsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(STALLED, true, selector = selector)

    /**
     * occurs when a Web Storage area is updated
     */
    val storagesCaptured: Listener<StorageEvent, T> get() = subscribe(STORAGE, true)

    /**
     * occurs when a Web Storage area is updated
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [StorageEvent]s on its [Flow]
     */
    fun storagesCaptured(init: StorageEvent.() -> Unit): Listener<StorageEvent, T> =
        subscribe(STORAGE, true) { init(); true }

    /**
     * occurs when a Web Storage area is updated
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [StorageEvent]s on its [Flow]
     */
    fun storagesCapturedIf(selector: StorageEvent.() -> Boolean): Listener<StorageEvent, T> =
        subscribe(STORAGE, true, selector = selector)

    /**
     * occurs when a form is submitted
     */
    val submitsCaptured: Listener<Event, T> get() = subscribe(SUBMIT, true)

    /**
     * occurs when a form is submitted
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun submitsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(SUBMIT, true) { init(); true }

    /**
     * occurs when a form is submitted
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun submitsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(SUBMIT, true, selector = selector)

    /**
     * occurs when the browser is intentionally not getting media data
     */
    val suspendsCaptured: Listener<Event, T> get() = subscribe(SUSPEND, true)

    /**
     * occurs when the browser is intentionally not getting media data
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun suspendsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(SUSPEND, true) { init(); true }

    /**
     * occurs when the browser is intentionally not getting media data
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun suspendsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(SUSPEND, true, selector = selector)

    /**
     * occurs when the playing position has changed (like when the user fast forwards to a different point in the media)
     */
    val timeupdatesCaptured: Listener<Event, T> get() = subscribe(TIMEUPDATE, true)

    /**
     * occurs when the playing position has changed (like when the user fast forwards to a different point in the media)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun timeupdatesCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(TIMEUPDATE, true) { init(); true }

    /**
     * occurs when the playing position has changed (like when the user fast forwards to a different point in the media)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun timeupdatesCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(TIMEUPDATE, true, selector = selector)

    /**
     * occurs when the user opens or closes the <details> element
     */
    val togglesCaptured: Listener<Event, T> get() = subscribe(TOGGLE, true)

    /**
     * occurs when the user opens or closes the <details> element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun togglesCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(TOGGLE, true) { init(); true }

    /**
     * occurs when the user opens or closes the <details> element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun togglesCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(TOGGLE, true, selector = selector)

    /**
     * occurs when the touch is interrupted
     */
    val touchcancelsCaptured: Listener<TouchEvent, T> get() = subscribe(TOUCHCANCEL, true)

    /**
     * occurs when the touch is interrupted
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchcancelsCaptured(init: TouchEvent.() -> Unit): Listener<TouchEvent, T> =
        subscribe(TOUCHCANCEL, true) { init(); true }

    /**
     * occurs when the touch is interrupted
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchcancelsCapturedIf(selector: TouchEvent.() -> Boolean): Listener<TouchEvent, T> =
        subscribe(TOUCHCANCEL, true, selector = selector)

    /**
     * occurs when a finger is removed from a touch screen
     */
    val touchendsCaptured: Listener<TouchEvent, T> get() = subscribe(TOUCHEND, true)

    /**
     * occurs when a finger is removed from a touch screen
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchendsCaptured(init: TouchEvent.() -> Unit): Listener<TouchEvent, T> =
        subscribe(TOUCHEND, true) { init(); true }

    /**
     * occurs when a finger is removed from a touch screen
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchendsCapturedIf(selector: TouchEvent.() -> Boolean): Listener<TouchEvent, T> =
        subscribe(TOUCHEND, true, selector = selector)

    /**
     * occurs when a finger is dragged across the screen
     */
    val touchmovesCaptured: Listener<TouchEvent, T> get() = subscribe(TOUCHMOVE, true)

    /**
     * occurs when a finger is dragged across the screen
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchmovesCaptured(init: TouchEvent.() -> Unit): Listener<TouchEvent, T> =
        subscribe(TOUCHMOVE, true) { init(); true }

    /**
     * occurs when a finger is dragged across the screen
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchmovesCapturedIf(selector: TouchEvent.() -> Boolean): Listener<TouchEvent, T> =
        subscribe(TOUCHMOVE, true, selector = selector)

    /**
     * occurs when a finger is placed on a touch screen
     */
    val touchstartsCaptured: Listener<TouchEvent, T> get() = subscribe(TOUCHSTART, true)

    /**
     * occurs when a finger is placed on a touch screen
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchstartsCaptured(init: TouchEvent.() -> Unit): Listener<TouchEvent, T> =
        subscribe(TOUCHSTART, true) { init(); true }

    /**
     * occurs when a finger is placed on a touch screen
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [TouchEvent]s on its [Flow]
     */
    fun touchstartsCapturedIf(selector: TouchEvent.() -> Boolean): Listener<TouchEvent, T> =
        subscribe(TOUCHSTART, true, selector = selector)

    /**
     * occurs when a CSS transition has completed
     */
    val transitionendsCaptured: Listener<Event, T> get() = subscribe(TRANSITIONEND, true)

    /**
     * occurs when a CSS transition has completed
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun transitionendsCaptured(init: Event.() -> Unit): Listener<Event, T> =
        subscribe(TRANSITIONEND, true) { init(); true }

    /**
     * occurs when a CSS transition has completed
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun transitionendsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(TRANSITIONEND, true, selector = selector)

    /**
     * occurs once a page has unloaded (for <body>)
     */
    val unloadsCaptured: Listener<Event, T> get() = subscribe(UNLOAD, true)

    /**
     * occurs once a page has unloaded (for <body>)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun unloadsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(UNLOAD, true) { init(); true }

    /**
     * occurs once a page has unloaded (for <body>)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun unloadsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(UNLOAD, true, selector = selector)

    /**
     * occurs when the volume of the media has changed (includes setting the volume to "mute")
     */
    val volumechangesCaptured: Listener<Event, T> get() = subscribe(VOLUMECHANGE, true)

    /**
     * occurs when the volume of the media has changed (includes setting the volume to "mute")
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun volumechangesCaptured(init: Event.() -> Unit): Listener<Event, T> =
        subscribe(VOLUMECHANGE, true) { init(); true }

    /**
     * occurs when the volume of the media has changed (includes setting the volume to "mute")
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun volumechangesCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(VOLUMECHANGE, true, selector = selector)

    /**
     * occurs when the media has paused but is expected to resume (like when the media pauses to buffer more data)
     */
    val waitingsCaptured: Listener<Event, T> get() = subscribe(WAITING, true)

    /**
     * occurs when the media has paused but is expected to resume (like when the media pauses to buffer more data)
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun waitingsCaptured(init: Event.() -> Unit): Listener<Event, T> = subscribe(WAITING, true) { init(); true }

    /**
     * occurs when the media has paused but is expected to resume (like when the media pauses to buffer more data)
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [Event]s on its [Flow]
     */
    fun waitingsCapturedIf(selector: Event.() -> Boolean): Listener<Event, T> =
        subscribe(WAITING, true, selector = selector)

    /**
     * occurs when the mouse wheel rolls up or down over an element
     */
    val wheelsCaptured: Listener<WheelEvent, T> get() = subscribe(WHEEL, true)

    /**
     * occurs when the mouse wheel rolls up or down over an element
     *
     * @param init expression to manipulate the event dispatching like calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [WheelEvent]s on its [Flow]
     */
    fun wheelsCaptured(init: WheelEvent.() -> Unit): Listener<WheelEvent, T> = subscribe(WHEEL, true) { init(); true }

    /**
     * occurs when the mouse wheel rolls up or down over an element
     *
     * @param selector expression to evaluate, which specific event should be emitted to the [Flow]. It is also
     * possible and encouraged to manipulate the event dispatching by calling `stopPropagation` or similar DOM-API.
     *
     * @return a [Listener] that emits [WheelEvent]s on its [Flow]
     */
    fun wheelsCapturedIf(selector: WheelEvent.() -> Boolean): Listener<WheelEvent, T> =
        subscribe(WHEEL, true, selector = selector)
}

/**
 * Represents all [Event]s of the browser [window] object as [Event]-flows
 */
object Window : WithEvents<Window> {

    private val scope = MainScope()

    override fun <X : Event> subscribe(
        eventName: String,
        capture: Boolean,
        selector: X.() -> Boolean
    ): Listener<X, Window> =
        Listener(window.subscribe<X, Window>(eventName, capture, selector).shareIn(scope, SharingStarted.Lazily))

    override val aborts by lazy { super.aborts }
    override val afterprints by lazy { super.afterprints }
    override val animationends by lazy { super.animationends }
    override val animationiterations by lazy { super.animationiterations }
    override val animationstarts by lazy { super.animationstarts }
    override val beforeprints by lazy { super.beforeprints }
    override val beforeunloads by lazy { super.beforeunloads }
    override val blurs by lazy { super.blurs }
    override val canplays by lazy { super.canplays }
    override val canplaythroughs by lazy { super.canplaythroughs }
    override val changes by lazy { super.changes }
    override val clicks by lazy { super.clicks }
    override val contextmenus by lazy { super.contextmenus }
    override val copys by lazy { super.copys }
    override val cuts by lazy { super.cuts }
    override val dblclicks by lazy { super.dblclicks }
    override val drags by lazy { super.drags }
    override val dragends by lazy { super.dragends }
    override val dragenters by lazy { super.dragenters }
    override val dragleaves by lazy { super.dragleaves }
    override val dragovers by lazy { super.dragovers }
    override val dragstarts by lazy { super.dragstarts }
    override val drops by lazy { super.drops }
    override val durationchanges by lazy { super.durationchanges }
    override val endeds by lazy { super.endeds }
    override val errors by lazy { super.errors }
    override val focuss by lazy { super.focuss }
    override val focusins by lazy { super.focusins }
    override val focusouts by lazy { super.focusouts }
    override val fullscreenchanges by lazy { super.fullscreenchanges }
    override val fullscreenerrors by lazy { super.fullscreenerrors }
    override val hashchanges by lazy { super.hashchanges }
    override val inputs by lazy { super.inputs }
    override val invalids by lazy { super.invalids }
    override val keydowns by lazy { super.keydowns }
    override val keypresss by lazy { super.keypresss }
    override val keyups by lazy { super.keyups }
    override val loads by lazy { super.loads }
    override val loadeddatas by lazy { super.loadeddatas }
    override val loadedmetadatas by lazy { super.loadedmetadatas }
    override val loadstarts by lazy { super.loadstarts }
    override val messages by lazy { super.messages }
    override val mousedowns by lazy { super.mousedowns }
    override val mouseenters by lazy { super.mouseenters }
    override val mouseleaves by lazy { super.mouseleaves }
    override val mousemoves by lazy { super.mousemoves }
    override val mouseovers by lazy { super.mouseovers }
    override val mouseouts by lazy { super.mouseouts }
    override val mouseups by lazy { super.mouseups }
    override val offlines by lazy { super.offlines }
    override val onlines by lazy { super.onlines }
    override val opens by lazy { super.opens }
    override val pagehides by lazy { super.pagehides }
    override val pageshows by lazy { super.pageshows }
    override val pastes by lazy { super.pastes }
    override val pauses by lazy { super.pauses }
    override val plays by lazy { super.plays }
    override val playings by lazy { super.playings }
    override val popstates by lazy { super.popstates }
    override val progresss by lazy { super.progresss }
    override val ratechanges by lazy { super.ratechanges }
    override val resizes by lazy { super.resizes }
    override val resets by lazy { super.resets }
    override val scrolls by lazy { super.scrolls }
    override val searchs by lazy { super.searchs }
    override val seekeds by lazy { super.seekeds }
    override val seekings by lazy { super.seekings }
    override val selects by lazy { super.selects }
    override val shows by lazy { super.shows }
    override val stalleds by lazy { super.stalleds }
    override val storages by lazy { super.storages }
    override val submits by lazy { super.submits }
    override val suspends by lazy { super.suspends }
    override val timeupdates by lazy { super.timeupdates }
    override val toggles by lazy { super.toggles }
    override val touchcancels by lazy { super.touchcancels }
    override val touchends by lazy { super.touchends }
    override val touchmoves by lazy { super.touchmoves }
    override val touchstarts by lazy { super.touchstarts }
    override val transitionends by lazy { super.transitionends }
    override val unloads by lazy { super.unloads }
    override val volumechanges by lazy { super.volumechanges }
    override val waitings by lazy { super.waitings }
    override val wheels by lazy { super.wheels }

    override val abortsCaptured by lazy { super.abortsCaptured }
    override val afterprintsCaptured by lazy { super.afterprintsCaptured }
    override val animationendsCaptured by lazy { super.animationendsCaptured }
    override val animationiterationsCaptured by lazy { super.animationiterationsCaptured }
    override val animationstartsCaptured by lazy { super.animationstartsCaptured }
    override val beforeprintsCaptured by lazy { super.beforeprintsCaptured }
    override val beforeunloadsCaptured by lazy { super.beforeunloadsCaptured }
    override val blursCaptured by lazy { super.blursCaptured }
    override val canplaysCaptured by lazy { super.canplaysCaptured }
    override val canplaythroughsCaptured by lazy { super.canplaythroughsCaptured }
    override val changesCaptured by lazy { super.changesCaptured }
    override val clicksCaptured by lazy { super.clicksCaptured }
    override val contextmenusCaptured by lazy { super.contextmenusCaptured }
    override val copysCaptured by lazy { super.copysCaptured }
    override val cutsCaptured by lazy { super.cutsCaptured }
    override val dblclicksCaptured by lazy { super.dblclicksCaptured }
    override val dragsCaptured by lazy { super.dragsCaptured }
    override val dragendsCaptured by lazy { super.dragendsCaptured }
    override val dragentersCaptured by lazy { super.dragentersCaptured }
    override val dragleavesCaptured by lazy { super.dragleavesCaptured }
    override val dragoversCaptured by lazy { super.dragoversCaptured }
    override val dragstartsCaptured by lazy { super.dragstartsCaptured }
    override val dropsCaptured by lazy { super.dropsCaptured }
    override val durationchangesCaptured by lazy { super.durationchangesCaptured }
    override val endedsCaptured by lazy { super.endedsCaptured }
    override val errorsCaptured by lazy { super.errorsCaptured }
    override val focussCaptured by lazy { super.focussCaptured }
    override val focusinsCaptured by lazy { super.focusinsCaptured }
    override val focusoutsCaptured by lazy { super.focusoutsCaptured }
    override val fullscreenchangesCaptured by lazy { super.fullscreenchangesCaptured }
    override val fullscreenerrorsCaptured by lazy { super.fullscreenerrorsCaptured }
    override val hashchangesCaptured by lazy { super.hashchangesCaptured }
    override val inputsCaptured by lazy { super.inputsCaptured }
    override val invalidsCaptured by lazy { super.invalidsCaptured }
    override val keydownsCaptured by lazy { super.keydownsCaptured }
    override val keypresssCaptured by lazy { super.keypresssCaptured }
    override val keyupsCaptured by lazy { super.keyupsCaptured }
    override val loadsCaptured by lazy { super.loadsCaptured }
    override val loadeddatasCaptured by lazy { super.loadeddatasCaptured }
    override val loadedmetadatasCaptured by lazy { super.loadedmetadatasCaptured }
    override val loadstartsCaptured by lazy { super.loadstartsCaptured }
    override val messagesCaptured by lazy { super.messagesCaptured }
    override val mousedownsCaptured by lazy { super.mousedownsCaptured }
    override val mouseentersCaptured by lazy { super.mouseentersCaptured }
    override val mouseleavesCaptured by lazy { super.mouseleavesCaptured }
    override val mousemovesCaptured by lazy { super.mousemovesCaptured }
    override val mouseoversCaptured by lazy { super.mouseoversCaptured }
    override val mouseoutsCaptured by lazy { super.mouseoutsCaptured }
    override val mouseupsCaptured by lazy { super.mouseupsCaptured }
    override val offlinesCaptured by lazy { super.offlinesCaptured }
    override val onlinesCaptured by lazy { super.onlinesCaptured }
    override val opensCaptured by lazy { super.opensCaptured }
    override val pagehidesCaptured by lazy { super.pagehidesCaptured }
    override val pageshowsCaptured by lazy { super.pageshowsCaptured }
    override val pastesCaptured by lazy { super.pastesCaptured }
    override val pausesCaptured by lazy { super.pausesCaptured }
    override val playsCaptured by lazy { super.playsCaptured }
    override val playingsCaptured by lazy { super.playingsCaptured }
    override val popstatesCaptured by lazy { super.popstatesCaptured }
    override val progresssCaptured by lazy { super.progresssCaptured }
    override val ratechangesCaptured by lazy { super.ratechangesCaptured }
    override val resizesCaptured by lazy { super.resizesCaptured }
    override val resetsCaptured by lazy { super.resetsCaptured }
    override val scrollsCaptured by lazy { super.scrollsCaptured }
    override val searchsCaptured by lazy { super.searchsCaptured }
    override val seekedsCaptured by lazy { super.seekedsCaptured }
    override val seekingsCaptured by lazy { super.seekingsCaptured }
    override val selectsCaptured by lazy { super.selectsCaptured }
    override val showsCaptured by lazy { super.showsCaptured }
    override val stalledsCaptured by lazy { super.stalledsCaptured }
    override val storagesCaptured by lazy { super.storagesCaptured }
    override val submitsCaptured by lazy { super.submitsCaptured }
    override val suspendsCaptured by lazy { super.suspendsCaptured }
    override val timeupdatesCaptured by lazy { super.timeupdatesCaptured }
    override val togglesCaptured by lazy { super.togglesCaptured }
    override val touchcancelsCaptured by lazy { super.touchcancelsCaptured }
    override val touchendsCaptured by lazy { super.touchendsCaptured }
    override val touchmovesCaptured by lazy { super.touchmovesCaptured }
    override val touchstartsCaptured by lazy { super.touchstartsCaptured }
    override val transitionendsCaptured by lazy { super.transitionendsCaptured }
    override val unloadsCaptured by lazy { super.unloadsCaptured }
    override val volumechangesCaptured by lazy { super.volumechangesCaptured }
    override val waitingsCaptured by lazy { super.waitingsCaptured }
    override val wheelsCaptured by lazy { super.wheelsCaptured }
}