package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.binding.const
import dev.fritz2.binding.handledBy
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.values
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.StyleClass.Companion.plus
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.shadow
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.InputFieldStyles
import dev.fritz2.styling.theme.theme
import dev.fritz2.styling.whenever


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


fun HtmlElements.inputField(
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
            value = it.data
            changes.values() handledBy it.update
        }
    }
}
