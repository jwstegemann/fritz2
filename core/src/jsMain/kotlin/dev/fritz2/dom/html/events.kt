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
    val input = EventType<InputEvent>("input")

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
 * [Key] represents a key press e.g. for keypress events
 */
data class Key(
    val code: Int,
    val name: String,
    val ctrl: Boolean,
    val alt: Boolean,
    val shift: Boolean,
    val meta: Boolean
) {
    companion object {
        fun from(e: KeyboardEvent) = Key(e.keyCode, e.key, e.ctrlKey, e.altKey, e.shiftKey, e.metaKey)
    }

    fun isKey(keys: Keys): Boolean = code == keys.code
}

/**
 * [Keys] contains most of the javascript related special key codes
 */
enum class Keys(val code: Int) {
    Backspace(8),
    Tab(9),
    Enter(13),
    Shift(16),
    Ctrl(17),
    Alt(18),
    Pause(19),
    CapsLock(20),
    Escape(27),
    Space(32),
    Pageup(33),
    Pagedown(34),
    End(35),
    Home(36),
    ArrowLeft(37),
    ArrowUp(38),
    ArrowRight(39),
    ArrowDown(40),
    Insert(45),
    Delete(46),
    ContextMenu(93),
    Numpad0(96),
    Numpad1(97),
    Numpad2(98),
    Numpad3(99),
    Numpad4(100),
    Numpad5(101),
    Numpad6(102),
    Numpad7(103),
    Numpad8(104),
    Numpad9(105),
    NumpadMultiply(106),
    NumpadAdd(107),
    NumpadSubtract(109),
    NumpadDecimal(110),
    NumpadDivide(111),
    F1(112),
    F2(113),
    F3(114),
    F4(115),
    F5(116),
    F6(117),
    F7(118),
    F8(119),
    F9(120),
    F10(121),
    F11(122),
    F12(123),
    NumLock(144),
    ScrollLock(145),
    Semicolon(186),
    Equalsign(187),
    Comma(188),
    Dash(189),
    Period(190),
    Backquote(220)
}