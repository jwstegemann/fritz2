@file:Suppress("unused")

package dev.fritz2.dom.html

import dev.fritz2.dom.html.Keys.NamedKeys
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
 * Sometimes it is necessary to use a more generic type (like [Event])
 * because the type that is offered to the listener is not always consistent
 * (on different browsers, different actions, etc.)
 */
object Events {

    // The event occurs when the loading of a media is aborted
    val abort = EventType<Event>("abort")

    // The event occurs when a page has started printing, or if the print dialogue box has been closed
    val afterprint = EventType<Event>("afterprint")

    // The event occurs when a page is about to be printed
    val beforeprint = EventType<Event>("beforeprint")

    // The event occurs before the document is about to be unloaded
    val beforeunload = EventType<Event>("beforeunload")

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
    val load = EventType<Event>("load")

    // The event occurs when media data is loaded
    val loadeddata = EventType<Event>("loadeddata")

    // The event occurs when metadata (like dimensions and duration) are loaded
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
    val resize = EventType<Event>("resize")

    // The event occurs when a form is reset
    val reset = EventType<Event>("reset")

    // The event occurs when an element's scrollbar is being scrolled
    val scroll = EventType<Event>("scroll")

    // The event occurs when the user writes something in a search field (for <input="search">)
    val search = EventType<Event>("search")

    // The event occurs when the user is finished moving/skipping to a new position in the media
    val seeked = EventType<Event>("seeked")

    // The event occurs when the user starts moving/skipping to a new position in the media
    val seeking = EventType<Event>("seeking")

    // The event occurs after the user selects some text (for <input> and <textarea>)
    val select = EventType<Event>("select")

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
    val unload = EventType<Event>("unload")

    // The event occurs when the volume of the media has changed (includes setting the volume to "mute")
    val volumechange = EventType<Event>("volumechange")

    // The event occurs when the media has paused but is expected to resume (like when the media pauses to buffer more data)
    val waiting = EventType<Event>("waiting")

    // The event occurs when the mouse wheel rolls up or down over an element
    val wheel = EventType<WheelEvent>("wheel")

}

/**
 * This interface models the modifier shortcuts, that enable combination of shortcuts like "STRG + F" or alike.
 *
 * If offers default implementation for concatenating modifiers with "real" shortcuts or just Strings to offer a
 * beautiful readable shortcut combination:
 * ```
 * // Start with modifier shortcut and append just a String
 * Keys.Control + "K"
 * // or other way round
 * Shortcut("K") + Keys.Control
 * ```
 *
 * @see Shortcut
 * @see Keys
 */
interface ModifierShortcut {
    val ctrl: Boolean
    val alt: Boolean
    val shift: Boolean
    val meta: Boolean

    /**
     * This operator function enables the concatenation with a [Shortcut]:
     * ```
     * Keys.Alt + Shortcut("K")
     * ```
     *
     * @see ModifierShortcut
     */
    operator fun plus(other: Shortcut): Shortcut = Shortcut(
        other.key,
        ctrl || other.ctrl,
        alt || other.alt,
        shift || other.shift,
        meta || other.meta,
    )

    /**
     * This operator function enables the concatenation with simply a [String] to enable a nice readable keyboard
     * combination:
     * ```
     * Keys.Shift + "F"
     * ```
     *
     * Be aware that the [Shortcut.key] property is *case sensitive* just likte the [KeyboardEvent.key] property.
     * So in order to match a shortcut with a capital key of an event, you must the [Shortcut.shift] flag to `true`.
     * ```
     * // A capital "K" should be matched, but would fail here:
     * keydowns.map { shortcutOf(it) == Shortcut("K") } // would emit `false`!
     * // Instead this will work:
     * keydowns.map { shortcutOf(it) == Shortcut("K", shift = true) }
     * // or with this operator an better readbility:
     * keydowns.map { shortcutOf(it) == Keys.Shift + "K" }
     * ```
     *
     * @see ModifierShortcut
     */
    operator fun plus(other: String): Shortcut = Shortcut(
        key = other,
        ctrl,
        alt,
        shift,
        meta
    )
}

/**
 * Enables combination of [ModifierShortcut]s like "STRG + ALT + F":
 * ```
 * Keys.Control + Keys.Alt + "F"
 * ```
 *
 * @param other the modifier shortcut to concatenate
 */
operator fun ModifierShortcut.plus(other: ModifierShortcut): ModifierShortcut = object : ModifierShortcut {
    override val ctrl = this@plus.ctrl || other.ctrl
    override val alt = this@plus.alt || other.alt
    override val shift = this@plus.shift || other.shift
    override val meta = this@plus.meta || other.meta
}

/**
 * [Shortcut] represents a grouping type upon the "key" and the modifier key properties of a [KeyboardEvent].
 * More info [here](https://developer.mozilla.org/en-US/docs/Web/API/KeyboardEvent/key/Key_Values)
 *
 * A shortcut can be easily constructed by a [KeyboardEvent] which makes this abstraction so feasible to use with the
 * keyboard event handling like:
 * ```
 * div {
 *     // raw usage (prefer next example!)
 *     keydowns.map { Shortcut(it) } handledBy { /* use object for further processing */ }
 *
 *     // use factory function to create a Shortcut object
 *     keydowns.map { shortcutOf(it) } handledBy { /* use object for further processing */ }
 *
 *     // combine with `filter` functions is a common pattern:
 *     keydowns.filter { shortcutOf(it) == Keys.Control + "k") handledBy {
 *         // only if combination was pressed and with access to the original event too!
 *         // all other key events will be ignored
 *     }
 * }
 * ```
 *
 * This class enables by its implementation of [ModifierShortcut] the concatenation with other modifiers, but it
 * prevents the meaningless combination of shortcuts:
 * ```
 * // this works:
 * Shortcut("F") + Keys.Alt + Keys.Shift
 * // this won't work:
 * Shortcut("F") + Shortcut("P")
 *
 * // Ths first example could also be constructed by an appropriate constructor call:
 * Shortcut("F", alt = true, shift = true)
 * ```
 *
 * Be aware that the [Shortcut.key] property is *case sensitive* just likte the [KeyboardEvent.key] property.
 * So in order to match a shortcut with a capital key of an event, you must the [Shortcut.shift] flag to `true`.
 * ```
 * // A capital "K" should be matched, but would fail here:
 * keydowns.map { shortcutOf(it) == Shortcut("K") } // would emit `false`!
 * // Instead this will work:
 * keydowns.map { shortcutOf(it) == Shortcut("K", shift = true) }
 * // or with this operator an better readbility:
 * keydowns.map { shortcutOf(it) == Keys.Shift + "K" }
 * ```
 * On the other hand there will be no matching for a lowercase key with `shift` property set to `true` either!
 *
 * @see ModifierShortcut
 * @see Keys
 */
data class Shortcut(
    val key: String,
    override val ctrl: Boolean = false,
    override val alt: Boolean = false,
    override val shift: Boolean = false,
    override val meta: Boolean = false
) : ModifierShortcut {
    constructor(event: KeyboardEvent) : this(event.key, event.ctrlKey, event.altKey, event.shiftKey, event.metaKey)

    /**
     * This operator function enables the concatenation with additional modifier shortcuts:
     * ```
     * Shortcut("F") + Keys.Alt
     * // or even
     * Shortcut("F") + Keys.Alt + Keys.Shift
     * // will already result in a `Shortcut`
     * ```
     *
     * @see ModifierShortcut
     */
    operator fun plus(other: ModifierShortcut): Shortcut = copy(
        ctrl = ctrl || other.ctrl,
        alt = alt || other.alt,
        shift = shift || other.shift,
        meta = meta || other.meta
    )
}

/**
 * The function generates a [Shortcut] object out of a [String].
 *
 * @param key the [String] that should be taken as [Shortcut.key] property
 */
fun shortcutOf(key: String) = Shortcut(key)

/**
 * The function generates a [Shortcut] object out of a [KeyboardEvent].
 *
 * @param event the event to convert into a [Shortcut]
 */
fun shortcutOf(event: KeyboardEvent) = Shortcut(event)

/**
 * This object offers expressive access to predefined [Shortcut]s and [ModifierShortcut]s taken from the
 * [specification](https://www.w3.org/TR/uievents-key/#named-key-attribute-values) extended with `Space` as
 * symbol for simple space.
 *
 * This enables a beautiful definition of shortcuts:
 * ```
 * // define a commonly used combination
 * val searchKey = Keys.Shift + Keys.Alt + "F"
 *
 * // react only to a set of Keys e.g. to enable keyboard navigation of some component
 * div {
 *     keydowns.filter { setOf(Keys.Space, Keys.Enter).contains(shortcutOf(it)) }.map { } handledBy selectItem
 * }
 * ```
 *
 * In some cases, you need to check, whether an event contains a named-key as key property. There is a [Set] with all
 * named-keys ready for this: [NamedKeys]
 */
object Keys {
    val Alt = object : ModifierShortcut {
        override val ctrl = false
        override val alt = true
        override val shift = false
        override val meta = false
    }
    val Control = object : ModifierShortcut {
        override val ctrl = true
        override val alt = false
        override val shift = false
        override val meta = false
    }
    val Meta = object : ModifierShortcut {
        override val ctrl = false
        override val alt = false
        override val shift = false
        override val meta = true
    }
    val Shift = object : ModifierShortcut {
        override val ctrl = false
        override val alt = false
        override val shift = true
        override val meta = false
    }

    val AVRInput = Shortcut("AVRInput")
    val AVRPower = Shortcut("AVRPower")
    val Accept = Shortcut("Accept")
    val Again = Shortcut("Again")
    val AllCandidates = Shortcut("AllCandidates")
    val Alphanumeric = Shortcut("Alphanumeric")
    val AltGraph = Shortcut("AltGraph")
    val AppSwitch = Shortcut("AppSwitch")
    val ArrowDown = Shortcut("ArrowDown")
    val ArrowLeft = Shortcut("ArrowLeft")
    val ArrowRight = Shortcut("ArrowRight")
    val ArrowUp = Shortcut("ArrowUp")
    val Attn = Shortcut("Attn")
    val AudioBalanceLeft = Shortcut("AudioBalanceLeft")
    val AudioBalanceRight = Shortcut("AudioBalanceRight")
    val AudioBassBoostDown = Shortcut("AudioBassBoostDown")
    val AudioBassBoostToggle = Shortcut("AudioBassBoostToggle")
    val AudioBassBoostUp = Shortcut("AudioBassBoostUp")
    val AudioFaderFront = Shortcut("AudioFaderFront")
    val AudioFaderRear = Shortcut("AudioFaderRear")
    val AudioSurroundModeNext = Shortcut("AudioSurroundModeNext")
    val AudioTrebleDown = Shortcut("AudioTrebleDown")
    val AudioTrebleUp = Shortcut("AudioTrebleUp")
    val AudioVolumeDown = Shortcut("AudioVolumeDown")
    val AudioVolumeMute = Shortcut("AudioVolumeMute")
    val AudioVolumeUp = Shortcut("AudioVolumeUp")
    val Backspace = Shortcut("Backspace")
    val BrightnessDown = Shortcut("BrightnessDown")
    val BrightnessUp = Shortcut("BrightnessUp")
    val BrowserBack = Shortcut("BrowserBack")
    val BrowserFavorites = Shortcut("BrowserFavorites")
    val BrowserForward = Shortcut("BrowserForward")
    val BrowserHome = Shortcut("BrowserHome")
    val BrowserRefresh = Shortcut("BrowserRefresh")
    val BrowserSearch = Shortcut("BrowserSearch")
    val BrowserStop = Shortcut("BrowserStop")
    val Call = Shortcut("Call")
    val Camera = Shortcut("Camera")
    val CameraFocus = Shortcut("CameraFocus")
    val Cancel = Shortcut("Cancel")
    val CapsLock = Shortcut("CapsLock")
    val ChannelDown = Shortcut("ChannelDown")
    val ChannelUp = Shortcut("ChannelUp")
    val Clear = Shortcut("Clear")
    val Close = Shortcut("Close")
    val ClosedCaptionToggle = Shortcut("ClosedCaptionToggle")
    val CodeInput = Shortcut("CodeInput")
    val ColorF0Red = Shortcut("ColorF0Red")
    val ColorF1Green = Shortcut("ColorF1Green")
    val ColorF2Yellow = Shortcut("ColorF2Yellow")
    val ColorF3Blue = Shortcut("ColorF3Blue")
    val ColorF4Grey = Shortcut("ColorF4Grey")
    val ColorF5Brown = Shortcut("ColorF5Brown")
    val Compose = Shortcut("Compose")
    val ContextMenu = Shortcut("ContextMenu")
    val Convert = Shortcut("Convert")
    val Copy = Shortcut("Copy")
    val CrSel = Shortcut("CrSel")
    val Cut = Shortcut("Cut")
    val DVR = Shortcut("DVR")
    val Dead = Shortcut("Dead")
    val Delete = Shortcut("Delete")
    val Dimmer = Shortcut("Dimmer")
    val DisplaySwap = Shortcut("DisplaySwap")
    val Eisu = Shortcut("Eisu")
    val Eject = Shortcut("Eject")
    val End = Shortcut("End")
    val EndCall = Shortcut("EndCall")
    val Enter = Shortcut("Enter")
    val EraseEof = Shortcut("EraseEof")
    val Escape = Shortcut("Escape")
    val ExSel = Shortcut("ExSel")
    val Execute = Shortcut("Execute")
    val Exit = Shortcut("Exit")
    val F1 = Shortcut("F1")
    val F10 = Shortcut("F10")
    val F11 = Shortcut("F11")
    val F12 = Shortcut("F12")
    val F2 = Shortcut("F2")
    val F3 = Shortcut("F3")
    val F4 = Shortcut("F4")
    val F5 = Shortcut("F5")
    val F6 = Shortcut("F6")
    val F7 = Shortcut("F7")
    val F8 = Shortcut("F8")
    val F9 = Shortcut("F9")
    val FavoriteClear0 = Shortcut("FavoriteClear0")
    val FavoriteClear1 = Shortcut("FavoriteClear1")
    val FavoriteClear2 = Shortcut("FavoriteClear2")
    val FavoriteClear3 = Shortcut("FavoriteClear3")
    val FavoriteRecall0 = Shortcut("FavoriteRecall0")
    val FavoriteRecall1 = Shortcut("FavoriteRecall1")
    val FavoriteRecall2 = Shortcut("FavoriteRecall2")
    val FavoriteRecall3 = Shortcut("FavoriteRecall3")
    val FavoriteStore0 = Shortcut("FavoriteStore0")
    val FavoriteStore1 = Shortcut("FavoriteStore1")
    val FavoriteStore2 = Shortcut("FavoriteStore2")
    val FavoriteStore3 = Shortcut("FavoriteStore3")
    val FinalMode = Shortcut("FinalMode")
    val Find = Shortcut("Find")
    val Fn = Shortcut("Fn")
    val FnLock = Shortcut("FnLock")
    val GoBack = Shortcut("GoBack")
    val GoHome = Shortcut("GoHome")
    val GroupFirst = Shortcut("GroupFirst")
    val GroupLast = Shortcut("GroupLast")
    val GroupNext = Shortcut("GroupNext")
    val GroupPrevious = Shortcut("GroupPrevious")
    val Guide = Shortcut("Guide")
    val GuideNextDay = Shortcut("GuideNextDay")
    val GuidePreviousDay = Shortcut("GuidePreviousDay")
    val HangulMode = Shortcut("HangulMode")
    val HanjaMode = Shortcut("HanjaMode")
    val Hankaku = Shortcut("Hankaku")
    val HeadsetHook = Shortcut("HeadsetHook")
    val Help = Shortcut("Help")
    val Hibernate = Shortcut("Hibernate")
    val Hiragana = Shortcut("Hiragana")
    val HiraganaKatakana = Shortcut("HiraganaKatakana")
    val Home = Shortcut("Home")
    val Hyper = Shortcut("Hyper")
    val Info = Shortcut("Info")
    val Insert = Shortcut("Insert")
    val InstantReplay = Shortcut("InstantReplay")
    val JunjaMode = Shortcut("JunjaMode")
    val KanaMode = Shortcut("KanaMode")
    val KanjiMode = Shortcut("KanjiMode")
    val Katakana = Shortcut("Katakana")
    val Key11 = Shortcut("Key11")
    val Key12 = Shortcut("Key12")
    val LastNumberRedial = Shortcut("LastNumberRedial")
    val LaunchApplication1 = Shortcut("LaunchApplication1")
    val LaunchApplication2 = Shortcut("LaunchApplication2")
    val LaunchCalendar = Shortcut("LaunchCalendar")
    val LaunchContacts = Shortcut("LaunchContacts")
    val LaunchMail = Shortcut("LaunchMail")
    val LaunchMediaPlayer = Shortcut("LaunchMediaPlayer")
    val LaunchMusicPlayer = Shortcut("LaunchMusicPlayer")
    val LaunchPhone = Shortcut("LaunchPhone")
    val LaunchScreenSaver = Shortcut("LaunchScreenSaver")
    val LaunchSpreadsheet = Shortcut("LaunchSpreadsheet")
    val LaunchWebBrowser = Shortcut("LaunchWebBrowser")
    val LaunchWebCam = Shortcut("LaunchWebCam")
    val LaunchWordProcessor = Shortcut("LaunchWordProcessor")
    val Link = Shortcut("Link")
    val ListProgram = Shortcut("ListProgram")
    val LiveContent = Shortcut("LiveContent")
    val Lock = Shortcut("Lock")
    val LogOff = Shortcut("LogOff")
    val MailForward = Shortcut("MailForward")
    val MailReply = Shortcut("MailReply")
    val MailSend = Shortcut("MailSend")
    val MannerMode = Shortcut("MannerMode")
    val MediaApps = Shortcut("MediaApps")
    val MediaAudioTrack = Shortcut("MediaAudioTrack")
    val MediaClose = Shortcut("MediaClose")
    val MediaFastForward = Shortcut("MediaFastForward")
    val MediaLast = Shortcut("MediaLast")
    val MediaNextTrack = Shortcut("MediaNextTrack")
    val MediaPause = Shortcut("MediaPause")
    val MediaPlay = Shortcut("MediaPlay")
    val MediaPlayPause = Shortcut("MediaPlayPause")
    val MediaPreviousTrack = Shortcut("MediaPreviousTrack")
    val MediaRecord = Shortcut("MediaRecord")
    val MediaRewind = Shortcut("MediaRewind")
    val MediaSkipBackward = Shortcut("MediaSkipBackward")
    val MediaSkipForward = Shortcut("MediaSkipForward")
    val MediaStepBackward = Shortcut("MediaStepBackward")
    val MediaStepForward = Shortcut("MediaStepForward")
    val MediaStop = Shortcut("MediaStop")
    val MediaTopMenu = Shortcut("MediaTopMenu")
    val MediaTrackNext = Shortcut("MediaTrackNext")
    val MediaTrackPrevious = Shortcut("MediaTrackPrevious")
    val MicrophoneToggle = Shortcut("MicrophoneToggle")
    val MicrophoneVolumeDown = Shortcut("MicrophoneVolumeDown")
    val MicrophoneVolumeMute = Shortcut("MicrophoneVolumeMute")
    val MicrophoneVolumeUp = Shortcut("MicrophoneVolumeUp")
    val ModeChange = Shortcut("ModeChange")
    val NavigateIn = Shortcut("NavigateIn")
    val NavigateNext = Shortcut("NavigateNext")
    val NavigateOut = Shortcut("NavigateOut")
    val NavigatePrevious = Shortcut("NavigatePrevious")
    val New = Shortcut("New")
    val NextCandidate = Shortcut("NextCandidate")
    val NextFavoriteChannel = Shortcut("NextFavoriteChannel")
    val NextUserProfile = Shortcut("NextUserProfile")
    val NonConvert = Shortcut("NonConvert")
    val Notification = Shortcut("Notification")
    val NumLock = Shortcut("NumLock")
    val OnDemand = Shortcut("OnDemand")
    val Open = Shortcut("Open")
    val PageDown = Shortcut("PageDown")
    val PageUp = Shortcut("PageUp")
    val Pairing = Shortcut("Pairing")
    val Paste = Shortcut("Paste")
    val Pause = Shortcut("Pause")
    val PinPDown = Shortcut("PinPDown")
    val PinPMove = Shortcut("PinPMove")
    val PinPToggle = Shortcut("PinPToggle")
    val PinPUp = Shortcut("PinPUp")
    val PlaySpeedDown = Shortcut("PlaySpeedDown")
    val PlaySpeedReset = Shortcut("PlaySpeedReset")
    val PlaySpeedUp = Shortcut("PlaySpeedUp")
    val Power = Shortcut("Power")
    val PowerOff = Shortcut("PowerOff")
    val PreviousCandidate = Shortcut("PreviousCandidate")
    val Print = Shortcut("Print")
    val PrintScreen = Shortcut("PrintScreen")
    val Process = Shortcut("Process")
    val Props = Shortcut("Props")
    val RandomToggle = Shortcut("RandomToggle")
    val RcLowBattery = Shortcut("RcLowBattery")
    val RecordSpeedNext = Shortcut("RecordSpeedNext")
    val Redo = Shortcut("Redo")
    val RfBypass = Shortcut("RfBypass")
    val Romaji = Shortcut("Romaji")
    val STBInput = Shortcut("STBInput")
    val STBPower = Shortcut("STBPower")
    val Save = Shortcut("Save")
    val ScanChannelsToggle = Shortcut("ScanChannelsToggle")
    val ScreenModeNext = Shortcut("ScreenModeNext")
    val ScrollLock = Shortcut("ScrollLock")
    val Select = Shortcut("Select")
    val Settings = Shortcut("Settings")
    val SingleCandidate = Shortcut("SingleCandidate")
    val Soft1 = Shortcut("Soft1")
    val Soft2 = Shortcut("Soft2")
    val Soft3 = Shortcut("Soft3")
    val Soft4 = Shortcut("Soft4")
    val Space = Shortcut(" ")
    val SpeechCorrectionList = Shortcut("SpeechCorrectionList")
    val SpeechInputToggle = Shortcut("SpeechInputToggle")
    val SpellCheck = Shortcut("SpellCheck")
    val SplitScreenToggle = Shortcut("SplitScreenToggle")
    val Standby = Shortcut("Standby")
    val Subtitle = Shortcut("Subtitle")
    val Super = Shortcut("Super")
    val Symbol = Shortcut("Symbol")
    val SymbolLock = Shortcut("SymbolLock")
    val TV = Shortcut("TV")
    val TV3DMode = Shortcut("TV3DMode")
    val TVAntennaCable = Shortcut("TVAntennaCable")
    val TVAudioDescription = Shortcut("TVAudioDescription")
    val TVAudioDescriptionMixDown = Shortcut("TVAudioDescriptionMixDown")
    val TVAudioDescriptionMixUp = Shortcut("TVAudioDescriptionMixUp")
    val TVContentsMenu = Shortcut("TVContentsMenu")
    val TVDataService = Shortcut("TVDataService")
    val TVInput = Shortcut("TVInput")
    val TVInputComponent1 = Shortcut("TVInputComponent1")
    val TVInputComponent2 = Shortcut("TVInputComponent2")
    val TVInputComposite1 = Shortcut("TVInputComposite1")
    val TVInputComposite2 = Shortcut("TVInputComposite2")
    val TVInputHDMI1 = Shortcut("TVInputHDMI1")
    val TVInputHDMI2 = Shortcut("TVInputHDMI2")
    val TVInputHDMI3 = Shortcut("TVInputHDMI3")
    val TVInputHDMI4 = Shortcut("TVInputHDMI4")
    val TVInputVGA1 = Shortcut("TVInputVGA1")
    val TVMediaContext = Shortcut("TVMediaContext")
    val TVNetwork = Shortcut("TVNetwork")
    val TVNumberEntry = Shortcut("TVNumberEntry")
    val TVPower = Shortcut("TVPower")
    val TVRadioService = Shortcut("TVRadioService")
    val TVSatellite = Shortcut("TVSatellite")
    val TVSatelliteBS = Shortcut("TVSatelliteBS")
    val TVSatelliteCS = Shortcut("TVSatelliteCS")
    val TVSatelliteToggle = Shortcut("TVSatelliteToggle")
    val TVTerrestrialAnalog = Shortcut("TVTerrestrialAnalog")
    val TVTerrestrialDigital = Shortcut("TVTerrestrialDigital")
    val TVTimer = Shortcut("TVTimer")
    val Tab = Shortcut("Tab")
    val Teletext = Shortcut("Teletext")
    val Undo = Shortcut("Undo")
    val Unidentified = Shortcut("Unidentified")
    val VideoModeNext = Shortcut("VideoModeNext")
    val VoiceDial = Shortcut("VoiceDial")
    val WakeUp = Shortcut("WakeUp")
    val Wink = Shortcut("Wink")
    val Zenkaku = Shortcut("Zenkaku")
    val ZenkakuHankaku = Shortcut("ZenkakuHankaku")
    val ZoomIn = Shortcut("ZoomIn")
    val ZoomOut = Shortcut("ZoomOut")
    val ZoomToggle = Shortcut("ZoomToggle")

    val NamedKeys = setOf(
        // modifiers
        "Alt",
        "Control",
        "Meta",
        "Shift",
        // rest
        "AVRInput",
        "AVRPower",
        "Accept",
        "Again",
        "AllCandidates",
        "Alphanumeric",
        "AltGraph",
        "AppSwitch",
        "ArrowDown",
        "ArrowLeft",
        "ArrowRight",
        "ArrowUp",
        "Attn",
        "AudioBalanceLeft",
        "AudioBalanceRight",
        "AudioBassBoostDown",
        "AudioBassBoostToggle",
        "AudioBassBoostUp",
        "AudioFaderFront",
        "AudioFaderRear",
        "AudioSurroundModeNext",
        "AudioTrebleDown",
        "AudioTrebleUp",
        "AudioVolumeDown",
        "AudioVolumeMute",
        "AudioVolumeUp",
        "Backspace",
        "BrightnessDown",
        "BrightnessUp",
        "BrowserBack",
        "BrowserFavorites",
        "BrowserForward",
        "BrowserHome",
        "BrowserRefresh",
        "BrowserSearch",
        "BrowserStop",
        "Call",
        "Camera",
        "CameraFocus",
        "Cancel",
        "CapsLock",
        "ChannelDown",
        "ChannelUp",
        "Clear",
        "Close",
        "ClosedCaptionToggle",
        "CodeInput",
        "ColorF0Red",
        "ColorF1Green",
        "ColorF2Yellow",
        "ColorF3Blue",
        "ColorF4Grey",
        "ColorF5Brown",
        "Compose",
        "ContextMenu",
        "Convert",
        "Copy",
        "CrSel",
        "Cut",
        "DVR",
        "Dead",
        "Delete",
        "Dimmer",
        "DisplaySwap",
        "Eisu",
        "Eject",
        "End",
        "EndCall",
        "Enter",
        "EraseEof",
        "Escape",
        "ExSel",
        "Execute",
        "Exit",
        "F1",
        "F10",
        "F11",
        "F12",
        "F2",
        "F3",
        "F4",
        "F5",
        "F6",
        "F7",
        "F8",
        "F9",
        "FavoriteClear0",
        "FavoriteClear1",
        "FavoriteClear2",
        "FavoriteClear3",
        "FavoriteRecall0",
        "FavoriteRecall1",
        "FavoriteRecall2",
        "FavoriteRecall3",
        "FavoriteStore0",
        "FavoriteStore1",
        "FavoriteStore2",
        "FavoriteStore3",
        "FinalMode",
        "Find",
        "Fn",
        "FnLock",
        "GoBack",
        "GoHome",
        "GroupFirst",
        "GroupLast",
        "GroupNext",
        "GroupPrevious",
        "Guide",
        "GuideNextDay",
        "GuidePreviousDay",
        "HangulMode",
        "HanjaMode",
        "Hankaku",
        "HeadsetHook",
        "Help",
        "Hibernate",
        "Hiragana",
        "HiraganaKatakana",
        "Home",
        "Hyper",
        "Info",
        "Insert",
        "InstantReplay",
        "JunjaMode",
        "KanaMode",
        "KanjiMode",
        "Katakana",
        "Key11",
        "Key12",
        "LastNumberRedial",
        "LaunchApplication1",
        "LaunchApplication2",
        "LaunchCalendar",
        "LaunchContacts",
        "LaunchMail",
        "LaunchMediaPlayer",
        "LaunchMusicPlayer",
        "LaunchPhone",
        "LaunchScreenSaver",
        "LaunchSpreadsheet",
        "LaunchWebBrowser",
        "LaunchWebCam",
        "LaunchWordProcessor",
        "Link",
        "ListProgram",
        "LiveContent",
        "Lock",
        "LogOff",
        "MailForward",
        "MailReply",
        "MailSend",
        "MannerMode",
        "MediaApps",
        "MediaAudioTrack",
        "MediaClose",
        "MediaFastForward",
        "MediaLast",
        "MediaNextTrack",
        "MediaPause",
        "MediaPlay",
        "MediaPlayPause",
        "MediaPreviousTrack",
        "MediaRecord",
        "MediaRewind",
        "MediaSkipBackward",
        "MediaSkipForward",
        "MediaStepBackward",
        "MediaStepForward",
        "MediaStop",
        "MediaTopMenu",
        "MediaTrackNext",
        "MediaTrackPrevious",
        "MicrophoneToggle",
        "MicrophoneVolumeDown",
        "MicrophoneVolumeMute",
        "MicrophoneVolumeUp",
        "ModeChange",
        "NavigateIn",
        "NavigateNext",
        "NavigateOut",
        "NavigatePrevious",
        "New",
        "NextCandidate",
        "NextFavoriteChannel",
        "NextUserProfile",
        "NonConvert",
        "Notification",
        "NumLock",
        "OnDemand",
        "Open",
        "PageDown",
        "PageUp",
        "Pairing",
        "Paste",
        "Pause",
        "PinPDown",
        "PinPMove",
        "PinPToggle",
        "PinPUp",
        "PlaySpeedDown",
        "PlaySpeedReset",
        "PlaySpeedUp",
        "Power",
        "PowerOff",
        "PreviousCandidate",
        "Print",
        "PrintScreen",
        "Process",
        "Props",
        "RandomToggle",
        "RcLowBattery",
        "RecordSpeedNext",
        "Redo",
        "RfBypass",
        "Romaji",
        "STBInput",
        "STBPower",
        "Save",
        "ScanChannelsToggle",
        "ScreenModeNext",
        "ScrollLock",
        "Select",
        "Settings",
        "SingleCandidate",
        "Soft1",
        "Soft2",
        "Soft3",
        "Soft4",
        " ",
        "SpeechCorrectionList",
        "SpeechInputToggle",
        "SpellCheck",
        "SplitScreenToggle",
        "Standby",
        "Subtitle",
        "Super",
        "Symbol",
        "SymbolLock",
        "TV",
        "TV3DMode",
        "TVAntennaCable",
        "TVAudioDescription",
        "TVAudioDescriptionMixDown",
        "TVAudioDescriptionMixUp",
        "TVContentsMenu",
        "TVDataService",
        "TVInput",
        "TVInputComponent1",
        "TVInputComponent2",
        "TVInputComposite1",
        "TVInputComposite2",
        "TVInputHDMI1",
        "TVInputHDMI2",
        "TVInputHDMI3",
        "TVInputHDMI4",
        "TVInputVGA1",
        "TVMediaContext",
        "TVNetwork",
        "TVNumberEntry",
        "TVPower",
        "TVRadioService",
        "TVSatellite",
        "TVSatelliteBS",
        "TVSatelliteCS",
        "TVSatelliteToggle",
        "TVTerrestrialAnalog",
        "TVTerrestrialDigital",
        "TVTimer",
        "Tab",
        "Teletext",
        "Undo",
        "Unidentified",
        "VideoModeNext",
        "VoiceDial",
        "WakeUp",
        "Wink",
        "Zenkaku",
        "ZenkakuHankaku",
        "ZoomIn",
        "ZoomOut",
        "ZoomToggle"
    )
}