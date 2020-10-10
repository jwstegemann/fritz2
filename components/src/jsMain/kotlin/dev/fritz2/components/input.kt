package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.binding.handledBy
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.values
import dev.fritz2.styling.params.BasicStyleParams
import dev.fritz2.styling.params.*
import dev.fritz2.styling.staticStyle
import kotlinx.coroutines.ExperimentalCoroutinesApi

val inputFoundations = staticStyle(
    "Input",
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
    """
)

val basicInputStyles: Style<BasicStyleParams> = {
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

    focus {
        border {
            color { "#3182ce" } // TODO : Wehre to define? Or ability to provide?
        }
        boxShadow { outline }
    }

    // TODO: Fix https://github.com/jwstegemann/fritz2/issues/171 to make this work!
    disabled {
        background {
            color { disabled }
        }
    }
}

object InputSizes {
    val normal: Style<BasicStyleParams> = {
        height { "2.5rem" }
        minWidth { "2.5rem" }
        fontSize { normal }
        paddings {
            horizontal { small }
        }
    }

    val large: Style<BasicStyleParams> = {
        height { "3rem" }
        minWidth { "2.5rem" }
        fontSize { large }
        paddings {
            horizontal { small }
        }
    }

    val small: Style<BasicStyleParams> = {
        height { "2rem" }
        minWidth { "2.5rem" }
        fontSize { small }
        paddings {
            horizontal { tiny }
        }
    }
}

object InputVariants {
    val outline: Style<BasicStyleParams> = {
        // just leave the *foundation* CSS values untouched!
        // But we need a *name* for this variant, so we got to have this val!
    }

    val filled: Style<BasicStyleParams> = {
        background {
            color { light }
        }

        hover {
            css("filter: brightness(90%);")
        }

        focus {
            zIndex { "1" }
            background {
                color { "transparent" }
            }
        }
    }
}

object InputType {
    const val text = "text"
    const val tel = "tel"
    const val password = "password"
    const val color = "color"
    const val date = "date"
    const val datetime = "datetime"
    const val datetimeLocal = "datetime-local"
    const val email = "email"
    const val month = "month"
    const val number = "number"
    const val range = "range"
    const val research = "research"
    const val search = "search"
    const val url = "url"
    const val week = "week"
}

@ExperimentalCoroutinesApi
inline fun HtmlElements.Input(
    crossinline styles: Style<BasicStyleParams> = {},
    crossinline type: InputType.() -> String = { text },
    variant: InputVariants.() -> Style<BasicStyleParams> = { outline },
    size: InputSizes.() -> Style<BasicStyleParams> = { normal },
    crossinline init: Input.() -> Unit
): Input {

    return input(
        "$inputFoundations ${
            use(
                basicInputStyles +
                        InputVariants.variant() +
                        InputSizes.size() +
                        styles,
                "Input"
            )
        }"
    ) {
        attr("type", InputType.type())
        init()
    }
}

@ExperimentalCoroutinesApi
inline fun HtmlElements.Input(
    store: Store<String>,
    crossinline styles: Style<BasicStyleParams> = {},
    crossinline type: InputType.() -> String = { text },
    variant: InputVariants.() -> Style<BasicStyleParams> = { outline },
    size: InputSizes.() -> Style<BasicStyleParams> = { normal },
    crossinline init: Input.() -> Unit
): Input {
    return Input(
        styles = styles,
        type = type,
        variant = variant,
        size = size,
        init = {
            init()
            value = store.data
            changes.values() handledBy store.update
        }
    )
}