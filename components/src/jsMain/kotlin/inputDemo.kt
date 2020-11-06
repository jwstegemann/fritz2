import dev.fritz2.binding.Store
import dev.fritz2.binding.const
import dev.fritz2.binding.handledBy
import dev.fritz2.binding.storeOf
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.values
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun HtmlElements.inputDemo(): Div {

    val user = storeOf("Jon Snoe")

    return div {

        stackUp({
            alignItems { start }
            padding { "1rem" }
        }) {
            items {
                h1 { +"Inputs Showcase" }

                h3 { +"A basic Input needs no Store" }
                inputField {
                    placeholder = const("Placeholder")
                }

                h3 { +"A disabled component is skipped by the TAB key, but readonly isn't." }
                lineUp {
                    items {
                        inputField {
                            value = const("disabled")
                            disabled = const(true)
                        }
                        inputField({
                            focus {
                                border {
                                    color { dark }
                                }
                                boxShadow { none }
                            }
                        }) {
                            value = const("readonly")
                            readOnly = const(true)
                        }
                    }
                }

                h3 { +"Password" }
                inputField {
                    type = const("password")
                    placeholder = const("Password")
                }

                h3 { +"Inputs with store connect events automatically." }
                inputField(store = user) {
                    placeholder = const("Name")
                }

                h3 { +"Inputs without stores need manual event collection." }
                inputField {
                    placeholder = const("Name")
                    changes.values() handledBy user.update
                }

                (::p.styled {
                    background { color { light } }
                    paddings { horizontal { small } }
                    fontWeight { bold }
                }) {
                    +"Name in Store: "
                    user.data.bind()
                }

                h3 { +"Sizes" }
                lineUp {
                    items {
                        inputField({ theme().input.large() }) {
                            placeholder = const("large")
                        }
                        inputField({ theme().input.normal() }) {
                            placeholder = const("normal")
                        }
                        inputField({ theme().input.small() }) {
                            placeholder = const("small")
                        }
                    }
                }

                h3 { +"Variants" }
                lineUp {
                    items {
                        inputField({ theme().input.outline() }) {
                            placeholder = const("outline")
                        }
                        inputField({ theme().input.filled() }) {
                            placeholder = const("filled")
                        }
                    }
                }

                h2 { +"Input fields go to town" }

                val ourInputStyle: BasicParams.() -> Unit = {
                    theme().input.large()
                    theme().input.filled()
                    border {
                        color { warning }
                        width { "3px" }
                        style { double }
                    }
                    background {
                        color { dark }
                    }
                    radius { "1rem" }
                    color { light }

                    focus {
                        background {
                            color { light }
                        }
                        color { warning }
                    }
                }

                // Extend base component
                fun HtmlElements.ourInputField(
                    styling: BasicParams.() -> Unit = {},
                    store: Store<String>? = null,
                    baseClass: StyleClass? = null,
                    id: String? = null,
                    prefix: String = "ourInputField",
                    init: Input.() -> Unit
                ) {
                    inputField({
                        // always use corporate styling automatically!
                        ourInputStyle()
                        // still apply call-side defined styling!
                        styling()
                    }, store, baseClass, id, prefix, init)
                }

                lineUp {
                    spacing { tiny }
                    items {
                        // use our component instead of built-in one!
                        ourInputField {
                            type = const("text")
                            placeholder = const("user")
                        }
                        ourInputField({
                            // Passwords are dangerous -> so style ad hoc!!!
                            border {
                                color { danger }
                            }
                        }) {
                            type = const("password")
                            placeholder = const("password")
                        }
                    }
                }
                br {}
            }
        }
    }
}
