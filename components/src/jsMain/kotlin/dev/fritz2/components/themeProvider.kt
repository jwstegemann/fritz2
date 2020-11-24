package dev.fritz2.components

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.FlexParams
import dev.fritz2.styling.resetCss
import dev.fritz2.styling.theme.Theme

/**
 * This class offers the configuration of the [themeProvider] component.
 *
 * The component offers some configurable features:
 * - to enable or disable the resetting of the browser's default styling (it is highly recommended to stick with the
 *   default of resetting!) The reset procedure uses theme specific values already, so the basic look and feel of the app
 *   will comply to the theme.
 * - to pass in arbitrary content of course, as this component acts as the root of all UI
 *
 * The pattern to integrate a [themeProvider] into an app resembles always the following examples:
 * ```
 * // minimal integration: Stick to the default theme and reset the browser's CSS
 * render { theme: Theme -> // gain access to the specific (sub-)*type* of your theme and the initial theme
 *     themeProvider { // configure the provider itself -> nothing theme specific here, so the [DefaultTheme] will be used
 *          import dev.fritz2.styling.theme.Theme {
 *              // your UI goes here
 *          }
 *     }.mount("target")
 * ```
 *
 * Sometimes you want to set a theme that differs from the _default_ theme:
 * ```
 * render { theme: ExtendedTheme -> // gain access to the specific (sub-)*type* of your theme and the initial theme
 *     themeProvider {
 *          theme(myThemeInstance) // set the desired theme
 *          import dev.fritz2.styling.theme.Theme {
 *              // your UI goes here
 *          }
 *     }.mount("target")
 * ```
 */
class ThemeComponent {
    companion object {
        val dynamicResetCss: String
            get() =
                //modern-normalize v1.0.0 | MIT License | https://github.com/sindresorhus/modern-normalize
                """*,*::before,*::after{box-sizing:border-box}:root{-moz-tab-size:4;tab-size:4}html{line-height:1.15;-webkit-text-size-adjust:100%}body{margin:0}body{font-family:system-ui,-apple-system,'Segoe UI',Roboto,Helvetica,Arial,sans-serif,'Apple Color Emoji','Segoe UI Emoji'}hr{height:0;color:inherit}abbr[title]{-webkit-text-decoration:underline dotted;text-decoration:underline dotted}b,strong{font-weight:bolder}code,kbd,samp,pre{font-family:ui-monospace,SFMono-Regular,Consolas,'Liberation Mono',Menlo,monospace;font-size:1em}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-.25em}sup{top:-.5em}table{text-indent:0;border-color:inherit}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0}button,select{text-transform:none}button,[type='button'],[type='reset'],[type='submit']{-webkit-appearance:button}::-moz-focus-inner{border-style:none;padding:0}:-moz-focusring{outline:1px dotted ButtonText}:-moz-ui-invalid{box-shadow:none}legend{padding:0}progress{vertical-align:baseline}::-webkit-inner-spin-button,::-webkit-outer-spin-button{height:auto}[type='search']{-webkit-appearance:textfield;outline-offset:-2px}::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit}summary{display:list-item}blockquote,dl,dd,h1,h2,h3,h4,h5,h6,hr,figure,p,pre{margin:0}button{background-color:transparent;background-image:none}button:focus{outline:1px dotted;outline:5px auto -webkit-focus-ring-color}fieldset{margin:0;padding:0}ol,ul{list-style:none;margin:0;padding:0}html{font-family:ui-sans-serif,system-ui,-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,"Noto Sans",sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji";line-height:1.5}body{font-family:inherit;line-height:inherit}*,::before,::after{box-sizing:border-box;border-width:0;border-style:solid;border-color:#e5e7eb}hr{border-top-width:1px}img{border-style:solid}textarea{resize:vertical}input::placeholder,textarea::placeholder{color:#9ca3af}button,[role="button"]{cursor:pointer}table{border-collapse:collapse}h1,h2,h3,h4,h5,h6{font-size:inherit;font-weight:inherit}a{color:inherit;text-decoration:inherit}button,input,optgroup,select,textarea{padding:0;line-height:inherit;color:inherit}pre,code,kbd,samp{font-family:ui-monospace,SFMono-Regular,Menlo,Monaco,Consolas,"Liberation Mono","Courier New",monospace}img,svg,video,canvas,audio,iframe,embed,object{display:block;vertical-align:middle}img,video{max-width:100%;height:auto}""" + """
                h1 {
                  font-size: ${Theme().fontSizes.huge};
                  font-weight: bold;
                }
                h2 {
                  font-size: ${Theme().fontSizes.larger};
                  font-weight: bold;
                }
                h3 {
                  font-size: ${Theme().fontSizes.large};
                  font-weight: bold;
                }
                h4 {
                  font-size: ${Theme().fontSizes.normal};
                  font-weight: bold;
                }
                h5 {
                  font-size: ${Theme().fontSizes.small};
                  font-weight: bold;
                }
                h6 {
                  font-size: ${Theme().fontSizes.smaller};
                  font-weight: bold;
                }
            """.trimIndent()
    }

    var resetCss: Boolean = true

    fun resetCss(value: () -> Boolean) {
        resetCss = value()
    }

    var content: (RenderContext.() -> Unit)? = null

    fun content(value: RenderContext.() -> Unit) {
        content = value
    }

    fun theme(value: Theme) {
        Theme.use(value)
    }
}


/**
 * This component realizes an outer wrapper for the whole UI in order to initialize theming, reset browser css-defaults
 * (if you want) and rerender the content if the current theme changes in order to enable the _dynamic_ switching
 * between different [themes][Theme] at runtime.
 *
 * The component offers some configurable features:
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
        Theme.data.render {
            if (component.resetCss) {
                resetCss(ThemeComponent.dynamicResetCss)
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
                component.content?.let { it() }
            }
        }
    }
}
