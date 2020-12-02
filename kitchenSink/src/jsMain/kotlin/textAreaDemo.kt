import dev.fritz2.binding.storeOf
import dev.fritz2.components.lineUp
import dev.fritz2.components.playground
import dev.fritz2.components.stackUp
import dev.fritz2.components.textArea
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.values
import dev.fritz2.styling.params.AlignItemsValues
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf

@ExperimentalCoroutinesApi
fun RenderContext.textAreaDemo(): Div {

    return contentFrame {
        val dataStore = storeOf("Initial store value")

        showcaseHeader("Textarea")

        paragraph {
            +"""
            The Textarea component offers you the possibility to easily create multi-line text inputs
        """.trimIndent()
        }


        showcaseSection("Usage")

        paragraph {
            +"""
                Just define a textarea. 
                You can give it a
            """.trimIndent()

            +""
            c("placeholder")
            +"if you want. "
            br { }
            +"A basic textarea has a custom size of"
            c("normal")
            +"and is resizeable "
            c("vertical.")

        }

        componentFrame {
            lineUp {
                items {
                    textArea {

                    }
                    textArea {
                        placeholder("Here we have our placeholder")
                    }
                }
            }
        }

        playground {
            source(
                """
                textArea { }
                textArea {
                        placeholder("Here we have our placeholder")
                    }
                

            """.trimIndent()
            )
        }

        showcaseSection("Sizes")
        paragraph {
            +"fritz2 offers three different sizes of textareas :"
            c("small")
            +"|"
            c("normal")
            +"|"
            c("large.")

            br { }
            +"As mentioned earlier,"
            c("normal")
            +"is the default value."
        }

        componentFrame {
            lineUp {
                items {
                    textArea {
                        placeholder("I am a small textarea")
                        size { small }

                    }
                    textArea {
                        placeholder("I am a normal textarea")
                        size { normal }
                    }
                    textArea {
                        placeholder("I am a large textarea")
                        size { large }
                    }
                }
            }
        }

        playground {
            source(
                """
                 textArea {
                        placeholder("i am a small textarea")
                        size { small }

                    }
                    textArea {
                        placeholder("i am a normal textarea")
                        size { normal }
                    }
                    textArea {
                        placeholder("i am a large textarea")
                        size { large }
                    }
            """.trimIndent()
            )
        }

        showcaseSection("Resize behavior")
        paragraph {
            +"fritz2 offers you the  well known behavior of resizing a textarea : "
            c("vertical")
            +"|"
            c("horizontal")
            +"|"
            c("none")

        }

        componentFrame {
            lineUp {
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
        paragraph { +"Of course it is possible to disable a textarea" }
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

        showcaseSection("Set an initial value ")
        paragraph { +"if you need you can set an initial value on the textarea" }
        componentFrame {
            lineUp {
                items {
                    textArea {
                        placeholder("textarea with initial value")
                        value { flowOf("My initial Value") }

                    }
                }
            }
        }

        playground {
            source(
                """
                 textArea {
                        placeholder("textarea with initial value")
                        value { flowOf("My initial Value") }
                    }
            """.trimIndent()
            )
        }

        showcaseSection("Store")
        paragraph {
            +"You have the possibility to handle events of an textarea automatically or manually."
            br {}
            +"A Textarea with a store connects events automatically while without a store you have to  do it manually."
            br {}
            +"If you decide to handle events manually you can do it by using the "
            c("base")
            +"tag of the textarea."
        }

        componentFrame {
            stackUp {
                p {
                    b { +"Current value in store : " }

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
                        b { +"Current value in store : " }
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