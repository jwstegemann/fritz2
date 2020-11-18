package dev.fritz2.components

import dev.fritz2.binding.Store


import dev.fritz2.dom.html.Input
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.values
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.StyleClass.Companion.plus
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.InputFieldStyles

/**
 * This component object for inputFields just defines and holds basic styling information applied to every inputField.
 *
 * @see InputFieldStyles
 */
object InputFieldComponent {

    val staticCss = staticStyle(
        "inputBox",
        """
                display: inline-flex;
                position: relative;
                vertical-align: middle;
                height: 2.5rem;
                appearance: none;
                align-items : center;
                justify-content: center;
                transition: all 250ms;
                white-space: nowrap;
                outline: none;
                width: 100%
            """
    )

    val basicInputStyles: Style<BasicParams> = {
        lineHeight { normal }
        radius { normal }
        fontWeight { normal }
        paddings { horizontal { small } }
        border {
            width { thin }
            style { solid }
            color { light }
        }

        hover {
            border {
                color { dark }
            }
        }

        readOnly {
            background {
                color { disabled }
            }
        }

        disabled {
            background {
                color { disabled }
            }
        }

        focus {
            border {
                color { "#3182ce" } // TODO : Where to define? Or ability to provide?
            }
            boxShadow { outline }
        }
    }
}


/**
 * This component generates a text based input field.
 *
 * You can optionally pass in a store in order to set the value and react to updates _automatically_.
 *
 * To enable or disable it or to make it readOnly just use the well known attributes of the HTML
 * [input element](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/Input). To manually set the value or
 * react to a change refer also to its event's.
 *
 * There are some predefined stylings avaliable via the [dev.fritz2.styling.theme.Theme]. Have a look at
 * [dev.fritz2.styling.theme.Theme.input] or [InputFieldStyles]
 *
 * ```
 * inputField({ // just style it like any other component:
 *      theme().input.small() // render field rather small
 *      theme().input.filled() // render the field filled with a color
 * },
 * // pass in a store of type [String]
 * store = dataStore) {
 *      placeholder("Placeholder") // render a placeholder text for empty field
 * }
 * ```
 *
 * @see InputFieldComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param store optional [Store] that holds the data of the input
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param init a lambda expression for setting up the component itself. Details in [InputFieldComponent]
 */
fun RenderContext.inputField(
    styling: BasicParams.() -> Unit = {},
    store: Store<String>? = null,
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "inputField",
    init: Input.() -> Unit
) {
    (::input.styled(styling, baseClass + InputFieldComponent.staticCss, id, prefix) {
        InputFieldComponent.basicInputStyles()
    }) {
        init()
        store?.let {
            value(it.data)
            changes.values() handledBy it.update
        }
    }
}
