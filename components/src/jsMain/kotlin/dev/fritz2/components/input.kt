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
import dev.fritz2.styling.theme.InputFieldSizes
import dev.fritz2.styling.theme.InputFieldStyles
import dev.fritz2.styling.theme.InputFieldVariants
import dev.fritz2.styling.theme.Theme

/**
 * This class deals with the _configuration_ of an input element.
 *
 * The inputField can be configured for the following aspects:
 *  - the size of the element
 *  - some predefined styling variants
 *  - the base options of the HTML input element can be set.
 *    [Attributes](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#Attributes)
 *
 *  * For a detailed explanation and examples of usage have a look at the [inputField] function!
 *
 * @see InputFieldStyles
 */
@ComponentMarker
open class InputFieldComponent {
    companion object {

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

    var variant: InputFieldVariants.() -> Style<BasicParams> = { Theme().input.variants.outline }

    fun variant(value: InputFieldVariants.() -> Style<BasicParams>) {
        variant = value
    }

    var size: InputFieldSizes.() -> Style<BasicParams> = { Theme().input.sizes.normal }

    fun size(value: InputFieldSizes.() -> Style<BasicParams>) {
        size = value
    }

    var base: (Input.() -> Unit)? = null

    fun base(value: Input.() -> Unit) {
        base = value
    }
}


/**
 * This component generates a text based input field.
 *
 * You can optionally pass in a store in order to set the value and react to updates _automatically_.
 *
 * There are options to choose from predefined sizes and some variants from the Theme.
 *
 * To enable or disable it or to make it readOnly just use the well known attributes of the HTML
 * [input element](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/Input). To manually set the value or
 * react to a change refer also to its event's. All that can be achieved via the [InputFieldComponent.base] property!
 *
 * ```
 * inputField(store = dataStore) {
 *      base {
 *          placeholder("Placeholder") // render a placeholder text for empty field
 *      }
 * }
 *
 * // apply predefined size and variant
 * inputField(store = dataStore) {
 *      size { small } // render a smaller input
 *      variant { filled } // fill the background with ``light`` color
 *      base {
 *          placeholder("Placeholder") // render a placeholder text for empty field
 *      }
 * }
 *
 * // Of course you can apply custom styling as well
 * inputField({ // just use the ``styling`` parameter!
 *      background {
 *          color { dark }
 *      }
 *      radius { "1rem" }
 * },
 * store = dataStore) {
 *      size { small } // render a smaller input
 *      base {
 *          placeholder("Placeholder") // render a placeholder text for empty field
 *      }
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
 * @param build a lambda expression for setting up the component itself. Details in [InputFieldComponent]
 */
fun RenderContext.inputField(
    styling: BasicParams.() -> Unit = {},
    store: Store<String>? = null,
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "inputField",
    build: InputFieldComponent.() -> Unit = {}
) {
    val component = InputFieldComponent().apply(build)

    (::input.styled(styling, baseClass + InputFieldComponent.staticCss, id, prefix) {
        component.size.invoke(Theme().input.sizes)()
        component.variant.invoke(Theme().input.variants)()
        InputFieldComponent.basicInputStyles()
    }) {
        component.base?.invoke(this)
        store?.let {
            value(it.data)
            changes.values() handledBy it.update
        }
    }
}
