package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.SimpleHandler
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.FlexParams
import dev.fritz2.styling.resetCss
import dev.fritz2.styling.theme.DefaultTheme
import dev.fritz2.styling.theme.Theme
import dev.fritz2.styling.theme.currentTheme
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.flow.Flow

interface ThemeStore {
    val data: Flow<Int>
    val selectTheme: SimpleHandler<Int>
}


/**
 * This class offers the configuration of the [themeProvider] component.
 *
 * The component offers some configurable features:
 * - to set one or a [list][List] of themes; if given a list, the first theme of the list will be taken as current theme
 *   automatically.
 * - to enable or disable the resetting of the browser's default styling (it is highly recommended to stick with the
 *   default of resetting!) The reset procedure uses theme specific values already, so the basic look and feel of the app
 *   will comply to the theme.
 * - to pass in arbitrary content of course, as this component acts as the root of all UI
 * - it offers access to the [themeStore] in order to enable the _dynamic_ switching between different [themes][Theme]
 *   at runtime.
 *
 * The pattern to integrate a [themeProvider] into an app resembles always the following examples:
 * ```
 * // minimal integration: Stick to the default theme and reset the browser's CSS
 * render { theme: Theme -> // gain access to the specific (sub-)*type* of your theme and the initial theme
 *     themeProvider { // configure the provider itself -> nothing theme specific here, so the [DefaultTheme] will be used
 *          items {
 *              // your UI goes here
 *          }
 *     }.mount("target")
 * ```
 *
 * Sometimes you want to set a theme that differs from the _default_ theme:
 * ```
 * render { theme: ExtendedTheme -> // gain access to the specific (sub-)*type* of your theme and the initial theme
 *     themeProvider {
 *          theme { myThemeInstance } // set the desired theme
 *          items {
 *              // your UI goes here
 *          }
 *     }.mount("target")
 * ```
 * If you want to enable active switching between two themes, you have to _grab_ the theme store in order to pass a
 * fitting flow (of a selection component probably) into it
 * ```
 * // prepare some collection of themes:
 * val themes = listOf<ExtendedTheme>(
 *      Light(),
 *      Dark()
 * )
 *
 * // set the themes!
 * render { theme: ExtendedTheme -> // gain access to the specific (sub-)*type* of your theme and the initial theme
 *     themeProvider {
 *          themes { themes } // set the desired themes
 *          items {
 *              // use the exposed ``themeStore`` to dynamically select the current theme
 *              themeStore.data.map { currentThemeIndex -> // grab the current index to deduce the name later on
 *                  radioGroup {
 *                      items { themes.map { it.name } } // provide a list of names
 *                      selected { themes[currentThemeIndex].name } // set the selected name
 *                  }.map { selected -> // derive the index of the selected theme via its name
 *                      themes.indexOf(
 *                          themes.find {
 *                              selected == it.name
 *                          }
 *                      )
 *                  } handledBy themeStore.selectTheme // use the exposed ``themeStore`` as handler
 *              }
 *          }.watch() // must be watched, as there is nothing bound!
 *     }.mount("target")
 * ```
 */
class ThemeComponent {
    companion object {
        val staticResetCss: String
            get() = """
/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */

html {
  line-height: 1.15; /* 1 */
  -webkit-text-size-adjust: 100%; /* 2 */
  font-rendering: optimizeLegibilty;
  color: ${theme().colors.dark}
}
body {
  margin: 0;
  line-height: ${theme().lineHeights.large};
  font-family: Inter , sans-serif;
  font-feature-settings: "kern";
}
main {
  display: block;
}
h1 {
  font-size: ${theme().fontSizes.huge};
  margin: 2rem .25rem;
}
hr {
  box-sizing: content-box; /* 1 */
  height: 0; /* 1 */
  overflow: visible; /* 2 */
}
pre {
  font-size: ${theme().fontSizes.normal}; /* 2 */
}
a {
  font-size: ${theme().fontSizes.small}
  font-weight: 600;
  background-color: transparent;
}
abbr[title] {
  border-bottom: none; /* 1 */
  text-decoration: underline; /* 2 */
  -webkit-text-decoration: underline dotted;
          text-decoration: underline dotted; /* 2 */
}
b,
strong {
  font-weight: bolder;
}
code,
kbd,
samp {
  font-size: ${theme().fontSizes.huge};; /* 2 */
}
small {
  font-size: 80%;
}
sub,
sup {
  font-size: 75%;
  line-height: 0;
  position: relative;
  vertical-align: baseline;
}
sub {
  bottom: -0.25em;
}
sup {
  top: -0.5em;
}
img {
  border-style: none;
}
button,
input,
optgroup,
select,
textarea {
  font-family: inherit; /* 1 */
  font-size: ${theme().fontSizes.normal};; /* 1 */
  line-height: ${theme().lineHeights.tiny}; /* 1 */
  margin: 0; /* 2 */
}
button,
input { /* 1 */
  overflow: visible;
}
button,
select { /* 1 */
  text-transform: none;
}
button,
[type="button"],
[type="reset"],
[type="submit"] {
  -webkit-appearance: button;
}
fieldset {
  padding: 0.35em 0.75em 0.625em;
}
legend {
  box-sizing: border-box; /* 1 */
  color: inherit; /* 2 */
  display: table; /* 1 */
  max-width: 100%; /* 1 */
  padding: 0; /* 3 */
  white-space: normal; /* 1 */
}
progress {
  vertical-align: baseline;
}
textarea {
  overflow: auto;
}
[type="checkbox"],
[type="radio"] {
  box-sizing: border-box; /* 1 */
  padding: 0; /* 2 */
}
[type="number"]::-webkit-inner-spin-button,
[type="number"]::-webkit-outer-spin-button {
  height: auto;
}
[type="search"] {
  -webkit-appearance: textfield; /* 1 */
  outline-offset: -2px; /* 2 */
}
[type="search"]::-webkit-search-decoration {
  -webkit-appearance: none;
}
::-webkit-file-upload-button {
  -webkit-appearance: button; /* 1 */
  font: inherit; /* 2 */
}
details {
  display: block;
}
summary {
  display: list-item;
}
template {
  display: none;
}
[hidden] {
  display: none;
}

*,
*::before,
*::after {
  box-sizing: inherit;
}
blockquote,
dl,
dd,
h1,
h2,
h3,
h4,
h5,
h6,
hr,
figure,
p,
pre {
  margin: 0;
}
p {
  font-size: ${theme().fontSizes.normal};
  margin-top: 1.25rem;
  line-height: ${theme().lineHeights.larger};
}
button {
  background: transparent;
  padding: 0;
}
button:focus {
  outline: 1px dotted;
}
fieldset {
  margin: 0;
  padding: 0;
}
ol,
ul {
  list-style: none;
  margin: 0;
  padding: 0;
  padding-left: ${theme().space.normal};
}
ul {
  list-style-type: disc
}
ol {
  list-style-type: decimal
}

*,
*::before,
*::after {
  border-width: 0;
  border-style: solid;
  border-color: #e2e8f0;
  border-sizing: border-box;
  overflow-wrap: break-word;
}
hr {
  border-top-width: 1px;
}
img {
  border-style: solid;
}
textarea {
  resize: vertical;
}
input::placeholder,
textarea::placeholder {
  color: #a0aec0;
}
button,
[role="button"] {
  cursor: pointer;
}
table {
  border-collapse: collapse;
}
h1 {
  margin-top: 2rem;
  margin-bottom: .25rem;
  line-height: ${theme().lineHeights.tiny};
  font-weight: 700;
  font-size: ${theme().fontSizes.huge};
  letter-spacing: ${theme().letterSpacings.small};
  outline: 0;
}
h2 {
  margin-top: 4rem;
  margin-bottom: 0.5rem;
  line-height: ${theme().lineHeights.small};
  font-weight: 600;
  font-size: ${theme().fontSizes.larger};
  letter-spacing: ${theme().letterSpacings.small};
}
h3 {
  margin-top: 3rem;
  line-height: smaller;
  font-weight: 600;
  font-size: ${theme().fontSizes.large};
  letter-spacing: -.025em;
}
h4 {
  font-size: ${theme().fontSizes.normal};
  font-weight: bold;
}
h5 {
  font-size: ${theme().fontSizes.small};
  font-weight: bold;
}
h6 {
  font-size: ${theme().fontSizes.smaller};
  font-weight: bold;
}
a {
  font-size: ${theme().fontSizes.small};
  color: inherit;
  text-decoration: inherit;
}
button,
input,
optgroup,
select,
textarea {
  padding: 0;
  line-height: inherit;
  color: inherit;
}
pre,
code,
kbd,
samp {
  font-family: Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
}
img,
svg,
video,
canvas,
audio,
iframe,
embed,
object {
  display: block;
  vertical-align: middle;
}
img,
video {
  max-width: 100%;
  height: auto;
}
/*# sourceMappingURL=base.css.map */""".trimIndent()

    }

    var resetCss: Boolean = true

    fun resetCss(value: () -> Boolean) {
        resetCss = value()
    }

    var items: (RenderContext.() -> Unit)? = null

    fun items(value: RenderContext.() -> Unit) {
        items = value
    }

    var themes = listOf<Theme>(DefaultTheme())

    fun theme(value: () -> Theme) {
        themes = listOf(value())
    }

    fun themes(values: () -> List<Theme>) {
        themes = values()
    }

    // Expose ``ThemeStore`` via build-Block and bind it to a local val for further usage!
    val themeStore: ThemeStore = object : RootStore<Int>(0), ThemeStore {
        override val selectTheme = handle<Int> { _, index ->
            currentTheme = themes[index]
            index
        }
    }

    init {
        currentTheme = themes.first()
    }
}


/**
 * This component realizes an outer wrapper for the whole UI in order to set and initialize the actual theme
 * and to expose the [ThemeStore] in order to enable the _dynamic_ switching between different [themes][Theme]
 * at runtime.
 *
 * The component offers some configurable features:
 * - to set one or a [list][List] of themes
 * - to enable or disable the resetting of the browser's default styling (it is highly recommended to stick with the
 *   default of resetting!) The reset procedure uses theme specific values already, so the basic look and feel of the app
 *   will comply to the theme.
 * - to pass in arbitrary content of course, as this component acts as the root of all UI
 *
 * The pattern to integrate a [themeProvider] into an app resembles always the following examples:
 * ```
 * // minimal integration: Stick to the default theme and reset the browser's CSS
 * render { theme: ExtendedTheme -> // gain access to the specific (sub-)*type* of your theme and the initial theme
 *     themeProvider { // configure the provider itself -> nothing theme specific here, so the [DefaultTheme] will be used
 *          items {
 *              // your UI goes here
 *          }
 *     }.mount("target")
 * ```
 *
 * For a detailed overview of the configuration options have a look at [ThemeComponent]
 *
 * @see ThemeComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [ThemeComponent]
 */
fun RenderContext.themeProvider(
    styling: FlexParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "box",
    build: ThemeComponent.() -> Unit = {}
): Div {
    val component = ThemeComponent().apply(build)

    return div {
        component.themeStore.data.render {
            if (component.resetCss) {
                resetCss(ThemeComponent.staticResetCss)
            }
            box(
                {
                    styling()
                    position {
                        fixed {
                            vertical { "0" }
                            horizontal { "0" }
                        }
                    }
                    overflow { auto }
                }, baseClass, id, prefix
            ) {
                component.items?.let { it() }
            }
        }
    }
}
