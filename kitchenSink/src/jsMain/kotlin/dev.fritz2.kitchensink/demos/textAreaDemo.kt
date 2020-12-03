package dev.fritz2.kitchensink.demos

import dev.fritz2.binding.storeOf
import dev.fritz2.components.lineUp
import dev.fritz2.components.stackUp
import dev.fritz2.components.textArea
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.values
import dev.fritz2.kitchensink.base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf

@ExperimentalCoroutinesApi
fun RenderContext.textAreaDemo(): Div {

    return contentFrame {
        val dataStore = storeOf("Initial store value")

        showcaseHeader("Textarea")

        paragraph {
            +"""
            The Textarea component lets you easily create multi-line text inputs.
        """.trimIndent()
        }


        showcaseSection("Usage")

        paragraph {
            +"""
                Just define a textarea. 
                You can provide it with a
            """.trimIndent()

            +""
            c("placeholder")
            +" if you want."
            br { }
            +"A basic textarea has a"
            c("normal")
            +" size and is resizeable "
            c("vertically.")

        }

        componentFrame {
            lineUp {
                items {
                    textArea {

                    }
                    textArea {
                        placeholder("This is a placeholder.")
                    }
                }
            }
        }

        playground {
            source(
                """
                textArea { }
                textArea {
                        placeholder("This is a placeholder.")
                    }
                

            """.trimIndent()
            )
        }

        showcaseSection("Sizes")
        paragraph {
            +"fritz2 offers three different sizes of textareas:"
            c("small")
            +", "
            c("normal")
            +" (default) and "
            c("large")
        }

        componentFrame {
            stackUp {
                items {
                    textArea {
                        placeholder("A small textarea")
                        size { small }

                    }
                    textArea {
                        placeholder("A normal textarea")
                    }
                    textArea {
                        placeholder("A large textarea")
                        size { large }
                    }
                }
            }
        }

        playground {
            source(
                """
                 textArea {
                        placeholder("A small textarea")
                        size { small }

                    }
                    textArea {
                        placeholder("A normal textarea")
                    }
                    textArea {
                        placeholder("A large textarea")
                        size { large }
                    }
            """.trimIndent()
            )
        }

        showcaseSection("Resize behavior")
        paragraph {
            +"fritz2 offers the well known behavior of resizing a textarea by using one of these resizing options: "
            c("vertical")
            +", "
            c("horizontal")
            +" and "
            c("none")

        }

        componentFrame {
            stackUp {
                items {
                    textArea {
                        placeholder("resize : vertical")
                        resizeBehavior { vertical }

                    }
                    textArea {
                        placeholder("resize : horizontal")
                        resizeBehavior { horizontal }
                    }
                    textArea {
                        placeholder("resize : none")
                        resizeBehavior { none }
                    }
                }
            }
        }

        playground {
            source(
                """
                
                textArea {
                        placeholder("resize : vertical")
                        resizeBehavior { vertical }

                    }
                    textArea {
                        placeholder("resize : horizontal")
                        resizeBehavior { horizontal }
                    }
                    textArea {
                        placeholder("resize : none")
                        resizeBehavior { none }
                    }
                
            """.trimIndent()
            )
        }

        showcaseSection("Disable a textarea")
        paragraph { +"Of course it is possible to disable a textarea." }
        componentFrame {
            lineUp {
                items {
                    textArea {
                        placeholder("disabled")
                        disable(true)
                    }

                }
            }
        }

        playground {
            source(
                """
                 textArea {
                        placeholder("disabled")
                        disable(true)
                    }
            """.trimIndent()
            )
        }

        showcaseSection("Set an initial value")
        paragraph { +"You can optionally set an initial value for the textarea." }
        componentFrame {
            lineUp {
                items {
                    textArea {
                        placeholder("Textarea with initial value")
                        value { flowOf("My initial Value") }

                    }
                }
            }
        }

        playground {
            source(
                """
                 textArea {
                        placeholder("Textarea with initial value")
                        value { flowOf("My initial Value") }
                    }
            """.trimIndent()
            )
        }

        showcaseSection("Store")
        paragraph {
            +"You can handle events of a textarea automatically or manually."
            br {}
            +"Without a store, you have to connect your events manually, while it's done automatically when a"
            +"store is provided."
            br {}
            +"If you decide to handle events manually, you can do it by using the "
            c("base")
            +" tag of the textarea which provides the context for all events."
        }

        componentFrame {
            stackUp {
                p {
                    b { +"Current value in store: " }

                    dataStore.data.asText()
                }
                lineUp {
                    items {

                        textArea(store = dataStore) {
                            placeholder("")
                        }


                        textArea {
                            placeholder("Textarea without store")

                            base {
                                changes.values() handledBy dataStore.update

                            }

                        }

                    }
                }
            }

        }

        playground {
            source(
                """
                 val dataStore = storeOf("Initial store value")
                 
                   p {
                        b { +"Current value in store: " }
                        dataStore.data.asText()
                     }

              textArea(store = dataStore) {
                            placeholder("")
                        }


                        textArea {
                            placeholder("Textarea without store")

                            base {
                                changes.values() handledBy dataStore.update
                            }

                        }

            """.trimIndent()
            )
        }

    }

}