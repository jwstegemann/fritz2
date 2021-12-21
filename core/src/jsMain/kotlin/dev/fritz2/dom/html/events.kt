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
 * This interface models the modifier keys, that enable combination of key shortcuts like "Strg + F" or alike.
 *
 * If offers default implementation for concatenating modifier keys with "real" keys or just Strings to offer a
 * beautiful readable shortcut combination:
 * ```
 * // Start with modifier key and append just a String
 * Keys.Control + "K"
 * // or other way round
 * Key("K") + Keys.Control
 * ```
 *
 * @see Key
 * @see Keys
 */
interface ModifierKey {
    val ctrl: Boolean
    val alt: Boolean
    val shift: Boolean
    val meta: Boolean

    /**
     * This operator function enables the concatenation with a key:
     * ```
     * Keys.Alt + Key("K")
     * ```
     *
     * @see ModifierKey
     */
    operator fun plus(other: Key): Key = Key(
        other.name,
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
     * By default, the provided key name will be converted to lower-case if it is exactly one character long.
     * This is a nice safety belt as most of the time one will compare a declared key to some key generated out of
     * a keyboard event. The latter will always emit events with lower-case single letters, which requires the
     * defined key also to hold the name of the key as lower-case:
     * ```
     * // some user hit "K" on keyboard -> event with key = "k" will be emitted
     * keydowns.map { Key("K") == Key(it) } // would emit `false` without automatic normalization
     * ```
     *
     * @see ModifierKey
     */
    operator fun plus(other: String): Key = Key(
        name = other,
        ctrl,
        alt,
        shift,
        meta
    )
}

/**
 * Enables combination of [ModifierKey]s like "Strg + Alt + F":
 * ```
 * Keys.Control + Keys.Alt + "F"
 * ```
 *
 * @param other the modifier key to concatenate
 */
operator fun ModifierKey.plus(other: ModifierKey): ModifierKey = object : ModifierKey {
    override val ctrl = this@plus.ctrl || other.ctrl
    override val alt = this@plus.alt || other.alt
    override val shift = this@plus.shift || other.shift
    override val meta = this@plus.meta || other.meta
}

/**
 * [Key] represents a key based upon the "key" property of a [KeyboardEvent].
 * More info [here](https://developer.mozilla.org/en-US/docs/Web/API/KeyboardEvent/key/Key_Values)
 *
 * A key can be easily constructed by a [KeyboardEvent] which makes this abstraction so feasible to use with the
 * keyboard event handling like:
 * ```
 * div {
 *     // raw usage (prefer next example!)
 *     keydowns.map { Key(it) } handledBy { /* use Key-object for further processing */ }
 *
 *     // stick to `keys` functions in most cases:
 *     keydowns.keys(Keys.Control + "K") handledBy { (key, event) ->
 *         // only if combination was pressed and with access to the original event too!
 *         // all other key events will be ignored
 *     }
 * }
 * ```
 *
 * This class enables by its implementation of [ModifierKey] the concatenation with other modifier keys, but it prevents
 * the meaningless combination of "real" keys:
 * ```
 * // this works:
 * Key("F") + Keys.Alt + Keys.Shift
 * // this won't work:
 * Key("F") + Key("P")
 *
 * // Ths first example could also be constructed by appropriate ctor call:
 * Key("F", alt = true, shift = true)
 * ```
 *
 * By default, the provided key name will be converted to lower-case if it is exactly one character long.
 * This is a nice safety belt as most of the time one will compare a declared key to some key generated out of
 * a keyboard event. The latter will always emit events with lower-case single letters, which requires the
 * defined key also to hold the name of the key as lower-case:
 * ```
 * // some user hit "K" on keyboard -> event with key = "k" will be emitted
 * keydowns.map { Key("K") == Key(it) } // would emit `false` without automatic normalization
 * // Key("K") -> Key(name = "k", ...)
 * ```
 *
 * It is known that the former behaviour can be bypassed by using the `copy` method of this data class.
 * As the automatic conversion can be considered rather as a benevolent tool to support the user than a strictly
 * enforced behaviour, this drawback is totally acceptable.
 *
 * @see ModifierKey
 * @see Keys
 */
data class Key private constructor(
    val name: String,
    override val ctrl: Boolean = false,
    override val alt: Boolean = false,
    override val shift: Boolean = false,
    override val meta: Boolean = false
) : ModifierKey {
    constructor(event: KeyboardEvent) : this(event.key, event.ctrlKey, event.altKey, event.shiftKey, event.metaKey)

    companion object {
        /**
         * Use this factory instead of the primary constructor (which is intentionally private!), as this method
         * will ensure that single letter key names will be converted automatically to lower-case.
         * This acts as a safety belt when comparing a manual constructed `Key` with some key object from a
         * keyboard event, which will always represent key names as lower-case in case of single letter ones.
         *
         * @see Key
         */
        operator fun invoke(
            name: String,
            ctrl: Boolean = false,
            alt: Boolean = false,
            shift: Boolean = false,
            meta: Boolean = false
        ) = Key(if (name.length > 1) name else name.lowercase(), ctrl, alt, shift, meta)
    }

    /**
     * This operator function enables the concatenation with additional modifier keys:
     * ```
     * Key("F") + Keys.Alt
     * // or even
     * Key("F") + Keys.Alt + Keys.Shift
     * ^^^^^^^^^^^^^^^^^^^
     * will already result in a `Key`
     * ```
     *
     * @see ModifierKey
     */
    operator fun plus(other: ModifierKey): Key = copy(
        ctrl = ctrl || other.ctrl,
        alt = alt || other.alt,
        shift = shift || other.shift,
        meta = meta || other.meta
    )
}

/**
 * This object offers expressive access to predefined [Key]s and [ModifierKey]s taken from the
 * [specification](https://www.w3.org/TR/uievents-key/#named-key-attribute-values) extended with `Space` as
 * symbol for simple space.
 *
 * This enables a beautiful definition of Keys and keyboard shortcut combinations:
 * ```
 * // define a commonly used combination
 * val searchKey = Keys.Shift + Keys.Alt + "F"
 *
 * // react only to a set of Keys e.g. to enable keyboard navigation of some component
 * div {
 *     keydowns.keys(Keys.Space, Keys.Enter).map { } handledBy selectItem
 * }
 * ```
 */
object Keys {
    val Alt = object : ModifierKey {
        override val ctrl = false
        override val alt = true
        override val shift = false
        override val meta = false
    }
    val Control = object : ModifierKey {
        override val ctrl = true
        override val alt = false
        override val shift = false
        override val meta = false
    }
    val Meta = object : ModifierKey {
        override val ctrl = false
        override val alt = false
        override val shift = false
        override val meta = true
    }
    val Shift = object : ModifierKey {
        override val ctrl = false
        override val alt = false
        override val shift = true
        override val meta = false
    }

    val AVRInput = Key("AVRInput")
    val AVRPower = Key("AVRPower")
    val Accept = Key("Accept")
    val Again = Key("Again")
    val AllCandidates = Key("AllCandidates")
    val Alphanumeric = Key("Alphanumeric")
    val AltGraph = Key("AltGraph")
    val AppSwitch = Key("AppSwitch")
    val ArrowDown = Key("ArrowDown")
    val ArrowLeft = Key("ArrowLeft")
    val ArrowRight = Key("ArrowRight")
    val ArrowUp = Key("ArrowUp")
    val Attn = Key("Attn")
    val AudioBalanceLeft = Key("AudioBalanceLeft")
    val AudioBalanceRight = Key("AudioBalanceRight")
    val AudioBassBoostDown = Key("AudioBassBoostDown")
    val AudioBassBoostToggle = Key("AudioBassBoostToggle")
    val AudioBassBoostUp = Key("AudioBassBoostUp")
    val AudioFaderFront = Key("AudioFaderFront")
    val AudioFaderRear = Key("AudioFaderRear")
    val AudioSurroundModeNext = Key("AudioSurroundModeNext")
    val AudioTrebleDown = Key("AudioTrebleDown")
    val AudioTrebleUp = Key("AudioTrebleUp")
    val AudioVolumeDown = Key("AudioVolumeDown")
    val AudioVolumeMute = Key("AudioVolumeMute")
    val AudioVolumeUp = Key("AudioVolumeUp")
    val Backspace = Key("Backspace")
    val BrightnessDown = Key("BrightnessDown")
    val BrightnessUp = Key("BrightnessUp")
    val BrowserBack = Key("BrowserBack")
    val BrowserFavorites = Key("BrowserFavorites")
    val BrowserForward = Key("BrowserForward")
    val BrowserHome = Key("BrowserHome")
    val BrowserRefresh = Key("BrowserRefresh")
    val BrowserSearch = Key("BrowserSearch")
    val BrowserStop = Key("BrowserStop")
    val Call = Key("Call")
    val Camera = Key("Camera")
    val CameraFocus = Key("CameraFocus")
    val Cancel = Key("Cancel")
    val CapsLock = Key("CapsLock")
    val ChannelDown = Key("ChannelDown")
    val ChannelUp = Key("ChannelUp")
    val Clear = Key("Clear")
    val Close = Key("Close")
    val ClosedCaptionToggle = Key("ClosedCaptionToggle")
    val CodeInput = Key("CodeInput")
    val ColorF0Red = Key("ColorF0Red")
    val ColorF1Green = Key("ColorF1Green")
    val ColorF2Yellow = Key("ColorF2Yellow")
    val ColorF3Blue = Key("ColorF3Blue")
    val ColorF4Grey = Key("ColorF4Grey")
    val ColorF5Brown = Key("ColorF5Brown")
    val Compose = Key("Compose")
    val ContextMenu = Key("ContextMenu")
    val Convert = Key("Convert")
    val Copy = Key("Copy")
    val CrSel = Key("CrSel")
    val Cut = Key("Cut")
    val DVR = Key("DVR")
    val Dead = Key("Dead")
    val Delete = Key("Delete")
    val Dimmer = Key("Dimmer")
    val DisplaySwap = Key("DisplaySwap")
    val Eisu = Key("Eisu")
    val Eject = Key("Eject")
    val End = Key("End")
    val EndCall = Key("EndCall")
    val Enter = Key("Enter")
    val EraseEof = Key("EraseEof")
    val Escape = Key("Escape")
    val ExSel = Key("ExSel")
    val Execute = Key("Execute")
    val Exit = Key("Exit")
    val F1 = Key("F1")
    val F10 = Key("F10")
    val F11 = Key("F11")
    val F12 = Key("F12")
    val F2 = Key("F2")
    val F3 = Key("F3")
    val F4 = Key("F4")
    val F5 = Key("F5")
    val F6 = Key("F6")
    val F7 = Key("F7")
    val F8 = Key("F8")
    val F9 = Key("F9")
    val FavoriteClear0 = Key("FavoriteClear0")
    val FavoriteClear1 = Key("FavoriteClear1")
    val FavoriteClear2 = Key("FavoriteClear2")
    val FavoriteClear3 = Key("FavoriteClear3")
    val FavoriteRecall0 = Key("FavoriteRecall0")
    val FavoriteRecall1 = Key("FavoriteRecall1")
    val FavoriteRecall2 = Key("FavoriteRecall2")
    val FavoriteRecall3 = Key("FavoriteRecall3")
    val FavoriteStore0 = Key("FavoriteStore0")
    val FavoriteStore1 = Key("FavoriteStore1")
    val FavoriteStore2 = Key("FavoriteStore2")
    val FavoriteStore3 = Key("FavoriteStore3")
    val FinalMode = Key("FinalMode")
    val Find = Key("Find")
    val Fn = Key("Fn")
    val FnLock = Key("FnLock")
    val GoBack = Key("GoBack")
    val GoHome = Key("GoHome")
    val GroupFirst = Key("GroupFirst")
    val GroupLast = Key("GroupLast")
    val GroupNext = Key("GroupNext")
    val GroupPrevious = Key("GroupPrevious")
    val Guide = Key("Guide")
    val GuideNextDay = Key("GuideNextDay")
    val GuidePreviousDay = Key("GuidePreviousDay")
    val HangulMode = Key("HangulMode")
    val HanjaMode = Key("HanjaMode")
    val Hankaku = Key("Hankaku")
    val HeadsetHook = Key("HeadsetHook")
    val Help = Key("Help")
    val Hibernate = Key("Hibernate")
    val Hiragana = Key("Hiragana")
    val HiraganaKatakana = Key("HiraganaKatakana")
    val Home = Key("Home")
    val Hyper = Key("Hyper")
    val Info = Key("Info")
    val Insert = Key("Insert")
    val InstantReplay = Key("InstantReplay")
    val JunjaMode = Key("JunjaMode")
    val KanaMode = Key("KanaMode")
    val KanjiMode = Key("KanjiMode")
    val Katakana = Key("Katakana")
    val Key11 = Key("Key11")
    val Key12 = Key("Key12")
    val LastNumberRedial = Key("LastNumberRedial")
    val LaunchApplication1 = Key("LaunchApplication1")
    val LaunchApplication2 = Key("LaunchApplication2")
    val LaunchCalendar = Key("LaunchCalendar")
    val LaunchContacts = Key("LaunchContacts")
    val LaunchMail = Key("LaunchMail")
    val LaunchMediaPlayer = Key("LaunchMediaPlayer")
    val LaunchMusicPlayer = Key("LaunchMusicPlayer")
    val LaunchPhone = Key("LaunchPhone")
    val LaunchScreenSaver = Key("LaunchScreenSaver")
    val LaunchSpreadsheet = Key("LaunchSpreadsheet")
    val LaunchWebBrowser = Key("LaunchWebBrowser")
    val LaunchWebCam = Key("LaunchWebCam")
    val LaunchWordProcessor = Key("LaunchWordProcessor")
    val Link = Key("Link")
    val ListProgram = Key("ListProgram")
    val LiveContent = Key("LiveContent")
    val Lock = Key("Lock")
    val LogOff = Key("LogOff")
    val MailForward = Key("MailForward")
    val MailReply = Key("MailReply")
    val MailSend = Key("MailSend")
    val MannerMode = Key("MannerMode")
    val MediaApps = Key("MediaApps")
    val MediaAudioTrack = Key("MediaAudioTrack")
    val MediaClose = Key("MediaClose")
    val MediaFastForward = Key("MediaFastForward")
    val MediaLast = Key("MediaLast")
    val MediaNextTrack = Key("MediaNextTrack")
    val MediaPause = Key("MediaPause")
    val MediaPlay = Key("MediaPlay")
    val MediaPlayPause = Key("MediaPlayPause")
    val MediaPreviousTrack = Key("MediaPreviousTrack")
    val MediaRecord = Key("MediaRecord")
    val MediaRewind = Key("MediaRewind")
    val MediaSkipBackward = Key("MediaSkipBackward")
    val MediaSkipForward = Key("MediaSkipForward")
    val MediaStepBackward = Key("MediaStepBackward")
    val MediaStepForward = Key("MediaStepForward")
    val MediaStop = Key("MediaStop")
    val MediaTopMenu = Key("MediaTopMenu")
    val MediaTrackNext = Key("MediaTrackNext")
    val MediaTrackPrevious = Key("MediaTrackPrevious")
    val MicrophoneToggle = Key("MicrophoneToggle")
    val MicrophoneVolumeDown = Key("MicrophoneVolumeDown")
    val MicrophoneVolumeMute = Key("MicrophoneVolumeMute")
    val MicrophoneVolumeUp = Key("MicrophoneVolumeUp")
    val ModeChange = Key("ModeChange")
    val NavigateIn = Key("NavigateIn")
    val NavigateNext = Key("NavigateNext")
    val NavigateOut = Key("NavigateOut")
    val NavigatePrevious = Key("NavigatePrevious")
    val New = Key("New")
    val NextCandidate = Key("NextCandidate")
    val NextFavoriteChannel = Key("NextFavoriteChannel")
    val NextUserProfile = Key("NextUserProfile")
    val NonConvert = Key("NonConvert")
    val Notification = Key("Notification")
    val NumLock = Key("NumLock")
    val OnDemand = Key("OnDemand")
    val Open = Key("Open")
    val PageDown = Key("PageDown")
    val PageUp = Key("PageUp")
    val Pairing = Key("Pairing")
    val Paste = Key("Paste")
    val Pause = Key("Pause")
    val PinPDown = Key("PinPDown")
    val PinPMove = Key("PinPMove")
    val PinPToggle = Key("PinPToggle")
    val PinPUp = Key("PinPUp")
    val PlaySpeedDown = Key("PlaySpeedDown")
    val PlaySpeedReset = Key("PlaySpeedReset")
    val PlaySpeedUp = Key("PlaySpeedUp")
    val Power = Key("Power")
    val PowerOff = Key("PowerOff")
    val PreviousCandidate = Key("PreviousCandidate")
    val Print = Key("Print")
    val PrintScreen = Key("PrintScreen")
    val Process = Key("Process")
    val Props = Key("Props")
    val RandomToggle = Key("RandomToggle")
    val RcLowBattery = Key("RcLowBattery")
    val RecordSpeedNext = Key("RecordSpeedNext")
    val Redo = Key("Redo")
    val RfBypass = Key("RfBypass")
    val Romaji = Key("Romaji")
    val STBInput = Key("STBInput")
    val STBPower = Key("STBPower")
    val Save = Key("Save")
    val ScanChannelsToggle = Key("ScanChannelsToggle")
    val ScreenModeNext = Key("ScreenModeNext")
    val ScrollLock = Key("ScrollLock")
    val Select = Key("Select")
    val Settings = Key("Settings")
    val SingleCandidate = Key("SingleCandidate")
    val Soft1 = Key("Soft1")
    val Soft2 = Key("Soft2")
    val Soft3 = Key("Soft3")
    val Soft4 = Key("Soft4")
    val Space = Key(" ")
    val SpeechCorrectionList = Key("SpeechCorrectionList")
    val SpeechInputToggle = Key("SpeechInputToggle")
    val SpellCheck = Key("SpellCheck")
    val SplitScreenToggle = Key("SplitScreenToggle")
    val Standby = Key("Standby")
    val Subtitle = Key("Subtitle")
    val Super = Key("Super")
    val Symbol = Key("Symbol")
    val SymbolLock = Key("SymbolLock")
    val TV = Key("TV")
    val TV3DMode = Key("TV3DMode")
    val TVAntennaCable = Key("TVAntennaCable")
    val TVAudioDescription = Key("TVAudioDescription")
    val TVAudioDescriptionMixDown = Key("TVAudioDescriptionMixDown")
    val TVAudioDescriptionMixUp = Key("TVAudioDescriptionMixUp")
    val TVContentsMenu = Key("TVContentsMenu")
    val TVDataService = Key("TVDataService")
    val TVInput = Key("TVInput")
    val TVInputComponent1 = Key("TVInputComponent1")
    val TVInputComponent2 = Key("TVInputComponent2")
    val TVInputComposite1 = Key("TVInputComposite1")
    val TVInputComposite2 = Key("TVInputComposite2")
    val TVInputHDMI1 = Key("TVInputHDMI1")
    val TVInputHDMI2 = Key("TVInputHDMI2")
    val TVInputHDMI3 = Key("TVInputHDMI3")
    val TVInputHDMI4 = Key("TVInputHDMI4")
    val TVInputVGA1 = Key("TVInputVGA1")
    val TVMediaContext = Key("TVMediaContext")
    val TVNetwork = Key("TVNetwork")
    val TVNumberEntry = Key("TVNumberEntry")
    val TVPower = Key("TVPower")
    val TVRadioService = Key("TVRadioService")
    val TVSatellite = Key("TVSatellite")
    val TVSatelliteBS = Key("TVSatelliteBS")
    val TVSatelliteCS = Key("TVSatelliteCS")
    val TVSatelliteToggle = Key("TVSatelliteToggle")
    val TVTerrestrialAnalog = Key("TVTerrestrialAnalog")
    val TVTerrestrialDigital = Key("TVTerrestrialDigital")
    val TVTimer = Key("TVTimer")
    val Tab = Key("Tab")
    val Teletext = Key("Teletext")
    val Undo = Key("Undo")
    val Unidentified = Key("Unidentified")
    val VideoModeNext = Key("VideoModeNext")
    val VoiceDial = Key("VoiceDial")
    val WakeUp = Key("WakeUp")
    val Wink = Key("Wink")
    val Zenkaku = Key("Zenkaku")
    val ZenkakuHankaku = Key("ZenkakuHankaku")
    val ZoomIn = Key("ZoomIn")
    val ZoomOut = Key("ZoomOut")
    val ZoomToggle = Key("ZoomToggle")
}