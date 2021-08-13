package dev.fritz2.dom.html

import org.w3c.dom.*
import org.w3c.dom.clipboard.ClipboardEvent
import org.w3c.dom.events.*
import org.w3c.xhr.ProgressEvent

/**
 * Contains the javascript related name and actual type of the [Event].
 */
class EventType<T : Event>(val name: String)

/**
 * Contains all javascript event types.
 * Take a look [here](https://www.w3schools.com/jsref/dom_obj_event.asp).
 *
 * Sometimes it is necessary to use a more generic type (like UIEvent or even Event)
 * because the type that is offered to the listener is not always consistent
 * (on different browsers, different actions, etc.)
 */
object Events {

    // The event occurs when the loading of a media is aborted
    val abort = EventType<UIEvent>("abort")

    // The event occurs when a page has started printing, or if the print dialogue box has been closed
    val afterprint = EventType<Event>("afterprint")

    // The event occurs when a page is about to be printed
    val beforeprint = EventType<Event>("beforeprint")

    // The event occurs before the document is about to be unloaded
    val beforeunload = EventType<UIEvent>("beforeunload")

    // The event occurs when an element loses focus
    val blur = EventType<FocusEvent>("blur")

    // The event occurs when the browser can start playing the media (when it has buffered enough to begin)
    val canplay = EventType<Event>("canplay")

    // The event occurs when the browser can play through the media without stopping for buffering
    val canplaythrough = EventType<Event>("canplaythrough")

    // The event occurs when the content of a form element, the selection, or the checked state have changed (for <input>, <select>, and <textarea>)
    val change = EventType<Event>("change")

    // The event occurs when the user clicks on an element
    val click = EventType<MouseEvent>("click")

    // The event occurs when the user right-clicks on an element to open a context menu
    val contextmenu = EventType<MouseEvent>("contextmenu")

    // The event occurs when the user copies the content of an element
    val copy = EventType<ClipboardEvent>("copy")

    // The event occurs when the user cuts the content of an element
    val cut = EventType<ClipboardEvent>("cut")

    // The event occurs when the user double-clicks on an element
    val dblclick = EventType<MouseEvent>("dblclick")

    // The event occurs when an element is being dragged
    val drag = EventType<DragEvent>("drag")

    // The event occurs when the user has finished dragging an element
    val dragend = EventType<DragEvent>("dragend")

    // The event occurs when the dragged element enters the drop target
    val dragenter = EventType<DragEvent>("dragenter")

    // The event occurs when the dragged element leaves the drop target
    val dragleave = EventType<DragEvent>("dragleave")

    // The event occurs when the dragged element is over the drop target
    val dragover = EventType<DragEvent>("dragover")

    // The event occurs when the user starts to drag an element
    val dragstart = EventType<DragEvent>("dragstart")

    // The event occurs when the dragged element is dropped on the drop target
    val drop = EventType<DragEvent>("drop")

    // The event occurs when the duration of the media is changed
    val durationchange = EventType<Event>("durationchange")

    // The event occurs when the media has reach the end (useful for messages like "thanks for listening")
    val ended = EventType<Event>("ended")

    // The event occurs when an element gets focus
    val focus = EventType<FocusEvent>("focus")

    // The event occurs when an element is about to get focus
    val focusin = EventType<FocusEvent>("focusin")

    // The event occurs when an element is about to lose focus
    val focusout = EventType<FocusEvent>("focusout")

    // The event occurs when an element is displayed in fullscreen mode
    val fullscreenchange = EventType<Event>("fullscreenchange")

    // The event occurs when an element can not be displayed in fullscreen mode
    val fullscreenerror = EventType<Event>("fullscreenerror")

    // The event occurs when there has been changes to the anchor part of a URL
    val hashchange = EventType<HashChangeEvent>("hashchange")

    // The event occurs when an element gets user input
    // has to use Event as type because Chrome and Safari offer Events instead of InputEvents when selecting from a datalist
    val input = EventType<Event>("input")

    // The event occurs when an element is invalid
    val invalid = EventType<Event>("invalid")

    // The event occurs when the user is pressing a key
    val keydown = EventType<KeyboardEvent>("keydown")

    // The event occurs when the user presses a key
    val keypress = EventType<KeyboardEvent>("keypress")

    // The event occurs when the user releases a key
    val keyup = EventType<KeyboardEvent>("keyup")

    // The event occurs when an object has loaded
    val load = EventType<UIEvent>("load")

    // The event occurs when media data is loaded
    val loadeddata = EventType<Event>("loadeddata")

    // The event occurs when meta data (like dimensions and duration) are loaded
    val loadedmetadata = EventType<Event>("loadedmetadata")

    // The event occurs when the browser starts looking for the specified media
    val loadstart = EventType<ProgressEvent>("loadstart")

    // The event occurs when a message is received through the event source
    val message = EventType<Event>("message")

    // The event occurs when the user presses a mouse button over an element
    val mousedown = EventType<MouseEvent>("mousedown")

    // The event occurs when the pointer is moved onto an element
    val mouseenter = EventType<MouseEvent>("mouseenter")

    // The event occurs when the pointer is moved out of an element
    val mouseleave = EventType<MouseEvent>("mouseleave")

    // The event occurs when the pointer is moving while it is over an element
    val mousemove = EventType<MouseEvent>("mousemove")

    // The event occurs when the pointer is moved onto an element, or onto one of its children
    val mouseover = EventType<MouseEvent>("mouseover")

    // The event occurs when a user moves the mouse pointer out of an element, or out of one of its children
    val mouseout = EventType<MouseEvent>("mouseout")

    // The event occurs when a user releases a mouse button over an element
    val mouseup = EventType<MouseEvent>("mouseup")

    // The event occurs when the browser starts to work offline
    val offline = EventType<Event>("offline")

    // The event occurs when the browser starts to work online
    val online = EventType<Event>("online")

    // The event occurs when a connection with the event source is opened
    val open = EventType<Event>("open")

    // The event occurs when the user navigates away from a webpage
    val pagehide = EventType<PageTransitionEvent>("pagehide")

    // The event occurs when the user navigates to a webpage
    val pageshow = EventType<PageTransitionEvent>("pageshow")

    // The event occurs when the user pastes some content in an element
    val paste = EventType<ClipboardEvent>("paste")

    // The event occurs when the media is paused either by the user or programmatically
    val pause = EventType<Event>("pause")

    // The event occurs when the media has been started or is no longer paused
    val play = EventType<Event>("play")

    // The event occurs when the media is playing after having been paused or stopped for buffering
    val playing = EventType<Event>("playing")

    // The event occurs when the window's history changes
    val popstate = EventType<PopStateEvent>("popstate")

    // The event occurs when the browser is in the process of getting the media data (downloading the media)
    val progress = EventType<Event>("progress")

    // The event occurs when the playing speed of the media is changed
    val ratechange = EventType<Event>("ratechange")

    // The event occurs when the document view is resized
    val resize = EventType<UIEvent>("resize")

    // The event occurs when a form is reset
    val reset = EventType<Event>("reset")

    // The event occurs when an element's scrollbar is being scrolled
    val scroll = EventType<UIEvent>("scroll")

    // The event occurs when the user writes something in a search field (for <input="search">)
    val search = EventType<Event>("search")

    // The event occurs when the user is finished moving/skipping to a new position in the media
    val seeked = EventType<Event>("seeked")

    // The event occurs when the user starts moving/skipping to a new position in the media
    val seeking = EventType<Event>("seeking")

    // The event occurs after the user selects some text (for <input> and <textarea>)
    val select = EventType<UIEvent>("select")

    // The event occurs when a <menu> element is shown as a context menu
    val show = EventType<Event>("show")

    // The event occurs when the browser is trying to get media data, but data is not available
    val stalled = EventType<Event>("stalled")

    // The event occurs when a Web Storage area is updated
    val storage = EventType<StorageEvent>("storage")

    // The event occurs when a form is submitted
    val submit = EventType<Event>("submit")

    // The event occurs when the browser is intentionally not getting media data
    val suspend = EventType<Event>("suspend")

    // The event occurs when the playing position has changed (like when the user fast forwards to a different point in the media)
    val timeupdate = EventType<Event>("timeupdate")

    // The event occurs when the user opens or closes the <details> element
    val toggle = EventType<Event>("toggle")

    // The event occurs when the touch is interrupted
    val touchcancel = EventType<TouchEvent>("touchcancel")

    // The event occurs when a finger is removed from a touch screen
    val touchend = EventType<TouchEvent>("touchend")

    // The event occurs when a finger is dragged across the screen
    val touchmove = EventType<TouchEvent>("touchmove")

    // The event occurs when a finger is placed on a touch screen
    val touchstart = EventType<TouchEvent>("touchstart")

    // The event occurs once a page has unloaded (for <body>)
    val unload = EventType<UIEvent>("unload")

    // The event occurs when the volume of the media has changed (includes setting the volume to "mute")
    val volumechange = EventType<Event>("volumechange")

    // The event occurs when the media has paused but is expected to resume (like when the media pauses to buffer more data)
    val waiting = EventType<Event>("waiting")

    // The event occurs when the mouse wheel rolls up or down over an element
    val wheel = EventType<WheelEvent>("wheel")

}

/**
 * [Key] represents a key from [KeyboardEvent]
 *
 * More info [here](https://developer.mozilla.org/en-US/docs/Web/API/KeyboardEvent/key/Key_Values)
 */
class Key(
    val code: String,
    val key: String,
    val ctrl: Boolean = false,
    val alt: Boolean = false,
    val shift: Boolean = false,
    val meta: Boolean = false
) {
    constructor(key: String) : this(key, key)
    constructor(e: KeyboardEvent) : this(e.code, e.key, e.ctrlKey, e.altKey, e.shiftKey, e.metaKey)

    override fun equals(other: Any?): Boolean =
        if (other is Key) key == other.key else super.equals(other)

    override fun hashCode(): Int = key.hashCode()

    override fun toString() = key
}

object Keys {
    val Unidentified = Key("Unidentified")
    val Alt = Key("Alt")
    val AltGraph = Key("AltGraph")
    val CapsLock = Key("CapsLock")
    val Control = Key("Control")
    val Fn = Key("Fn")
    val FnLock = Key("FnLock")
    val Hyper = Key("Hyper")
    val Meta = Key("Meta")
    val NumLock = Key("NumLock")
    val ScrollLock = Key("ScrollLock")
    val Shift = Key("Shift")
    val Super = Key("Super")
    val Symbol = Key("Symbol")
    val SymbolLock = Key("SymbolLock")
    val Enter = Key("Enter")
    val Tab = Key("Tab")
    val Space = Key(" ")
    val ArrowDown = Key("ArrowDown")
    val ArrowLeft = Key("ArrowLeft")
    val ArrowRight = Key("ArrowRight")
    val ArrowUp = Key("ArrowUp")
    val End = Key("End")
    val Home = Key("Home")
    val PageDown = Key("PageDown")
    val PageUp = Key("PageUp")
    val Backspace = Key("Backspace")
    val Clear = Key("Clear")
    val Copy = Key("Copy")
    val CrSel = Key("CrSel")
    val Cut = Key("Cut")
    val Delete = Key("Delete")
    val EraseEof = Key("EraseEof")
    val ExSel = Key("ExSel")
    val Insert = Key("Insert")
    val Paste = Key("Paste")
    val Redo = Key("Redo")
    val Undo = Key("Undo")
    val Accept = Key("Accept")
    val Again = Key("Again")
    val Attn = Key("Attn")
    val Cancel = Key("Cancel")
    val ContextMenu = Key("ContextMenu")
    val Escape = Key("Escape")
    val Execute = Key("Execute")
    val Find = Key("Find")
    val Help = Key("Help")
    val Pause = Key("Pause")
    val Play = Key("Play")
    val Props = Key("Props")
    val Select = Key("Select")
    val ZoomIn = Key("ZoomIn")
    val ZoomOut = Key("ZoomOut")
    val F1 = Key("F1")
    val F2 = Key("F2")
    val F3 = Key("F3")
    val F4 = Key("F4")
    val F5 = Key("F5")
    val F6 = Key("F6")
    val F7 = Key("F7")
    val F8 = Key("F8")
    val F9 = Key("F9")
    val F10 = Key("F10")
    val F11 = Key("F11")
    val F12 = Key("F12")
    val Num0 = Key("0")
    val Num1 = Key("1")
    val Num2 = Key("2")
    val Num3 = Key("3")
    val Num4 = Key("4")
    val Num5 = Key("5")
    val Num6 = Key("6")
    val Num7 = Key("7")
    val Num8 = Key("8")
    val Num9 = Key("9")
    val Separator = Key("Separator")
}