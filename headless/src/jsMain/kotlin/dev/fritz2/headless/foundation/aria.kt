package dev.fritz2.headless.foundation

import dev.fritz2.core.Id
import dev.fritz2.core.Tag

/**
 * Predefined constants for all [ARIA attributes](https://www.w3.org/TR/wai-aria-1.1/#state_prop_def)
 *
 * Use them for definition of an ARIA attribute within a [Tag]:
 * ```
 * input {
 *     attr(Aria.invalid, "true".whenever(someBooleanFlow)) // should only appear on `true`
 *     attr(Aria.labelledby, "id-of-some-label")
 *     attr(Aria.checked, someBooleanFlow.asString()) // should hold String values "true" or "false"
 * }
 * ```
 */
object Aria {
    const val grabbed = "aria-grabbed"
    const val autocomplete = "aria-autocomplete"
    const val valuemax = "aria-valuemax"
    const val flowto = "aria-flowto"
    const val disabled = "aria-disabled"
    const val roledescription = "aria-roledescription"
    const val describedby = "aria-describedby"
    const val relevant = "aria-relevant"
    const val rowcount = "aria-rowcount"
    const val controls = "aria-controls"
    const val readonly = "aria-readonly"
    const val selected = "aria-selected"
    const val hidden = "aria-hidden"
    const val live = "aria-live"
    const val atomic = "aria-atomic"
    const val rowindex = "aria-rowindex"
    const val level = "aria-level"
    const val posinset = "aria-posinset"
    const val haspopup = "aria-haspopup"
    const val valuetext = "aria-valuetext"
    const val sort = "aria-sort"
    const val colcount = "aria-colcount"
    const val current = "aria-current"
    const val pressed = "aria-pressed"
    const val placeholder = "aria-placeholder"
    const val modal = "aria-modal"
    const val required = "aria-required"
    const val valuenow = "aria-valuenow"
    const val colindex = "aria-colindex"
    const val keyshortcuts = "aria-keyshortcuts"
    const val label = "aria-label"
    const val dropeffect = "aria-dropeffect"
    const val rowspan = "aria-rowspan"
    const val activedescendant = "aria-activedescendant"
    const val orientation = "aria-orientation"
    const val colspan = "aria-colspan"
    const val multiline = "aria-multiline"
    const val labelledby = "aria-labelledby"
    const val multiselectable = "aria-multiselectable"
    const val busy = "aria-busy"
    const val description = "aria-description"
    const val checked = "aria-checked"
    const val valuemin = "aria-valuemin"
    const val owns = "aria-owns"
    const val details = "aria-details"
    const val invalid = "aria-invalid"
    const val expanded = "aria-expanded"
    const val setsize = "aria-setsize"
    const val errormessage = "aria-errormessage"
    const val dragged = "aria-dragged"

    /**
     * All aria [roles](https://www.w3.org/TR/wai-aria-1.1/#role_definitions)
     *
     * Use them within an attribute call:
     * ```
     * div {
     *     attr("role", Aria.Role.dialog)
     * }
     * ```
     */
    object Role {
        const val alert = "alert"
        const val contentinfo = "contentinfo"
        const val feed = "feed"
        const val row = "row"
        const val marquee = "marquee"
        const val slider = "slider"
        const val search = "search"
        const val dialog = "dialog"
        const val timer = "timer"
        const val textbox = "textbox"
        const val complementary = "complementary"
        const val tablist = "tablist"
        const val listbox = "listbox"
        const val menuitemradio = "menuitemradio"
        const val directory = "directory"
        const val gridcell = "gridcell"
        const val menuitemcheckbox = "menuitemcheckbox"
        const val switch = "switch"
        const val log = "log"
        const val cell = "cell"
        const val figure = "figure"
        const val table = "table"
        const val definition = "definition"
        const val toolbar = "toolbar"
        const val math = "math"
        const val main = "main"
        const val status = "status"
        const val tab = "tab"
        const val button = "button"
        const val menu = "menu"
        const val article = "article"
        const val treeitem = "treeitem"
        const val img = "img"
        const val form = "form"
        const val application = "application"
        const val spinbutton = "spinbutton"
        const val radio = "radio"
        const val searchbox = "searchbox"
        const val rowheader = "rowheader"
        const val banner = "banner"
        const val navigation = "navigation"
        const val menuitem = "menuitem"
        const val none = "none"
        const val note = "note"
        const val region = "region"
        const val treegrid = "treegrid"
        const val link = "link"
        const val option = "option"
        const val menubar = "menubar"
        const val tabpanel = "tabpanel"
        const val progressbar = "progressbar"
        const val grid = "grid"
        const val listitem = "listitem"
        const val combobox = "combobox"
        const val heading = "heading"
        const val presentation = "presentation"
        const val alertdialog = "alertdialog"
        const val document = "document"
        const val checkbox = "checkbox"
        const val columnheader = "columnheader"
        const val term = "term"
        const val rowgroup = "rowgroup"
        const val tooltip = "tooltip"
        const val tree = "tree"
        const val separator = "separator"
        const val radiogroup = "radiogroup"
        const val list = "list"
        const val scrollbar = "scrollbar"
        const val group = "group"
    }
}

/**
 * This hook encapsulates the generation of some ARIA-attribute, that deals with referencing some other tag by id.
 *
 * This is useful for situations where the client creates the content, that should be referenced by the underlying
 * (headless-)component. Both sections need to reference and declare the same id.
 *
 * This hook encapsulates the specific ARIA attribute setting, by letting the component define the specific ARIA
 * attribute, but enables the client to set a specific id or to create some random one and to use it. The component
 * simply needs to apply the hook, as the behaviour is to exactly add the initial ARIA attribute with the created id.
 *
 * The following strongly simplified example shows the use case:
 * ```kotlin
 * class SomeComponent {
 *      val ariaTitleId = AriaReferenceHook<Tag<HTMLDivElement>>(Aria.labelledby)
 *      val ariaDescriptionId = AriaReferenceHook<Tag<HTMLDivElement>>(Aria.describedby)
 *
 *      private var userContent: Tag<HTMLDivElement>.() -> Unit = {}
 *
 *      fun content(expr: Tag<HTMLDivElement>.() -> Unit) { userContent = expr }
 *
 *      fun render() {
 *          // surrounding structure by component itself
 *          div {
 *              // integrate user's content; reference hooks get invoked!
 *              userContent(this)
 *
 *              // apply effect of setting correct ARIA references
 *              hook(ariaTitleId, ariaDescriptionId)
 *          }
 *          // results in the following DOM-Subtree:
 *          // <div aria-labelledby="mySpecialTitleId" aria-describedby="AB12FD">
 *          //   <h1>My Title</h1>
 *          //   <p>lorem ipsum...</P>
 *          // </div>
 *      }
 * }
 *
 * // client usage
 * someCoponent {
 *      content {
 *          h1(id = ariaTitleId("mySpecialTitleId")) { +"My Title" }
 *          //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *          //      user set explicit ID
 *
 *          p(id = ariaDescriptionId()) {
 *          //     ^^^^^^^^^^^^^^^^^^^
 *          //     user relies on automatic created ID
 *
 *              +"lorem ipsum..."
 *          }
 *      }
 * }
 * ```
 */
class AriaReferenceHook<C : Tag<*>>(private val name: String) : Hook<C, Unit, Unit>() {
    operator fun invoke(id: String): String {
        value = id.let { v -> { attr(name, v) } }
        return id
    }

    operator fun invoke(): String {
        val id = Id.next()
        value = id.let { v -> { attr(name, v) } }
        return id
    }
}
