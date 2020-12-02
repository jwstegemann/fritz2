import dev.fritz2.binding.Store
import dev.fritz2.binding.storeOf
import dev.fritz2.components.InputFieldComponent
import dev.fritz2.components.inputField
import dev.fritz2.components.lineUp
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.values
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.inputDemo(): Div {

    val user = storeOf("Jon Snoe")

    return contentFrame {
        showcaseHeader("Inputs")

        showcaseSection("A basic Input needs no Store")
        componentFrame {
            inputField {
                base {
                    placeholder("Placeholder")
                }
            }
        }
        showcaseSection("A disabled component is skipped by the TAB key, but readonly isn't.")
        componentFrame {
            lineUp {
                items {
                    inputField {
                        base {
                            value("disabled")
                            disabled(true)
                        }
                    }
                    inputField({
                        focus {
                            border {
                                color { dark }
                            }
                            boxShadow { none }
                        }
                    }) {
                        base {
                            value("readonly")
                            readOnly(true)
                        }
                    }
                }
            }
        }
        showcaseSection("Password")
        componentFrame {
            inputField {
                base {
                    type("password")
                    placeholder("Password")
                }
            }
        }
        showcaseSection("Inputs with store connect events automatically.")
        componentFrame {
            inputField(store = user) {
                base {
                    placeholder("Name")
                }
            }
        }
        showcaseSection("Inputs without stores need manual event collection.")
        componentFrame {
            inputField {
                base {
                    placeholder("Name")
                    changes.values() handledBy user.update
                }
            }
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

        showcaseSection("Sizes")
        componentFrame {
            lineUp {
                items {
                    inputField {
                        size { large }
                        base {
                            placeholder("large")
                        }
                    }
                    inputField {
                        size { normal }
                        base {
                            placeholder("normal")
                        }
                    }
                    inputField {
                        size { small }
                        base {
                            placeholder("small")
                        }
                    }
                }
            }
        }
        showcaseSection("Variants")
        componentFrame {
            lineUp {
                items {
                    inputField {
                        variant { outline }
                        base {
                            placeholder("outline")
                        }
                    }
                    inputField {
                        variant { filled }
                        base {
                            placeholder("filled")
                        }
                    }
                }
            }
        }
        showcaseSubHeader("Input fields go to town")
        componentFrame {

            val ourInputStyle: BasicParams.() -> Unit = {
                Theme().input.sizes.large()
                Theme().input.variants.filled()
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
                build: InputFieldComponent.() -> Unit
            ) {
                inputField({
                    // always use corporate styling automatically!
                    ourInputStyle()
                    // still apply call-side defined styling!
                    styling()
                }, store, baseClass, id, prefix, build)
            }

            lineUp {
                spacing { tiny }
                items {
                    // use our component instead of built-in one!
                    ourInputField {
                        base {
                            type("text")
                            placeholder("user")
                        }
                    }
                    ourInputField({
                        // Passwords are dangerous -> so style ad hoc!!!
                        border {
                            color { danger }
                        }
                    }) {
                        base {
                            type("password")
                            placeholder("password")
                        }
                    }
                }
            }
            br {}
        }
    }
}