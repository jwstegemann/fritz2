import dev.fritz2.binding.Store
import dev.fritz2.binding.storeOf
import dev.fritz2.components.inputField
import dev.fritz2.components.lineUp
import dev.fritz2.components.stackUp
import dev.fritz2.components.styled
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.values
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.inputDemo(): Div {

    val user = storeOf("Jon Snoe")

    return stackUp({
        alignItems { start }
        padding { "1rem" }
    }) {
        items {
            h1 { +"Inputs Showcase" }

            h3 { +"A basic Input needs no Store" }
            inputField {
                placeholder("Placeholder")
            }

            h3 { +"A disabled component is skipped by the TAB key, but readonly isn't." }
            lineUp {
                items {
                    inputField {
                        value("disabled")
                        disabled(true)
                    }
                    inputField({
                        focus {
                            border {
                                color { dark }
                            }
                            boxShadow { none }
                        }
                    }) {
                        value("readonly")
                        readOnly(true)
                    }
                }
            }

            h3 { +"Password" }
            inputField {
                type("password")
                placeholder("Password")
            }

            h3 { +"Inputs with store connect events automatically." }
            inputField(store = user) {
                placeholder("Name")
            }

            h3 { +"Inputs without stores need manual event collection." }
            inputField {
                placeholder("Name")
                changes.values() handledBy user.update
            }

            (::p.styled {
                background { color { light } }
                fontWeight { bold }
                radius { "5%" }
                paddings {
                    left { "0.3rem" }
                    right { "0.3rem" }
                }
            }) {
                +"Name in Store: "
                user.data.asText()
            }

            h3 { +"Sizes" }
            lineUp {
                items {
                    inputField({ theme().input.large() }) {
                        placeholder("large")
                    }
                    inputField({ theme().input.normal() }) {
                        placeholder("normal")
                    }
                    inputField({ theme().input.small() }) {
                        placeholder("small")
                    }
                }
            }

            h3 { +"Variants" }
            lineUp {
                items {
                    inputField({ theme().input.outline() }) {
                        placeholder("outline")
                    }
                    inputField({ theme().input.filled() }) {
                        placeholder("filled")
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
            fun RenderContext.ourInputField(
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
                        type("text")
                        placeholder("user")
                    }
                    ourInputField({
                        // Passwords are dangerous -> so style ad hoc!!!
                        border {
                            color { danger }
                        }
                    }) {
                        type("password")
                        placeholder("password")
                    }
                }
            }
            br {}
        }
    }
}
