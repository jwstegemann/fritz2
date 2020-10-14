package dev.fritz2.components

import dev.fritz2.binding.Store
import dev.fritz2.binding.handledBy
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.values
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.Style
import dev.fritz2.styling.params.plus
import dev.fritz2.styling.params.use
import dev.fritz2.styling.staticStyle
import kotlinx.coroutines.ExperimentalCoroutinesApi

class InputComponentContext private constructor(prefix: String) : BasicComponentContext(prefix) {
    companion object Foundation {

        // TODO: Check whether this is a good idea *AND* how to generalize it!
        fun build(prefix: String, build: Context<InputComponentContext> = {}): InputComponentContext =
            InputComponentContext(prefix)
                .apply(build)
                .apply {
                    InputComponentContext.basicInputStyles()
                    variant()
                    inputSize()
                }


        val cssClass = staticStyle(
            "f2Input",
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

            focus {
                border {
                    color { "#3182ce" } // TODO : Wehre to define? Or ability to provide?
                }
                boxShadow { outline }
            }

            disabled {
                background {
                    color { disabled }
                }
            }
        }

        object InputSizes {
            val normal: Style<BasicParams> = {
                height { "2.5rem" }
                minWidth { "2.5rem" }
                fontSize { normal }
                paddings {
                    horizontal { small }
                }
            }

            val large: Style<BasicParams> = {
                height { "3rem" }
                minWidth { "2.5rem" }
                fontSize { large }
                paddings {
                    horizontal { small }
                }
            }

            val small: Style<BasicParams> = {
                height { "2rem" }
                minWidth { "2.5rem" }
                fontSize { small }
                paddings {
                    horizontal { tiny }
                }
            }
        }

        object InputVariants {
            val outline: Style<BasicParams> = {
                // just leave the *foundation* CSS values untouched!
                // But we need a *name* for this variant, so we got to have this val!
            }

            val filled: Style<BasicParams> = {
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
    }

    var type: String = InputType.text

    fun type(value: InputType.() -> String) {
        type = InputType.value()
    }

    // TODO: Check for better name or how to avoid name collision with ``Layout.size``
    var inputSize: Style<BasicParams> = InputSizes.normal

    fun inputSize(value: InputSizes.() -> Style<BasicParams>) {
        inputSize = InputSizes.value()
    }

    var variant: Style<BasicParams> = InputVariants.outline

    fun variant(value: InputVariants.() -> Style<BasicParams>) {
        variant = InputVariants.value()
    }

    var store: Store<String>? = null

    fun store(value: () -> Store<String>) {
        store = value()
    }
}

fun HtmlElements.f2Input(build: Context<InputComponentContext> = {}): Component<Input> {
    val context = InputComponentContext.build("f2Input", build)

    return Component { init ->
        input("${InputComponentContext.cssClass} ${context.cssClass}") {
            attr("type", context.type)
            init()
            if (context.store != null) {
                // TODO: Check whether the non null assert is safe here!
                value = context.store!!.data
                changes.values() handledBy context.store!!.update
            }
        }
    }
}
