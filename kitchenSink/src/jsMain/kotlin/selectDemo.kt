import dev.fritz2.binding.storeOf
import dev.fritz2.components.lineUp
import dev.fritz2.components.playground
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf

@ExperimentalCoroutinesApi
fun RenderContext.selectDemo(): Div {
    val myOptions = listOf("black", "red", "yellow")
    val selectedItem = storeOf("")

    return contentFrame {

        showcaseHeader("Select")
        paragraph { +"The Select Component represents a control that allows users to pick a value from predefined options." }

        showcaseSection("Usage")
        paragraph {
            +"To use this component just follow these steps:"
            br {}
            +" - define a List<String>  and pass it to the options function"
            br { }
            +"- create a Store<String> which holds your current selected item and pass it to the component"
            br {}
            +"- if you want, you can define a placeholder"

        }

        componentFrame {
            lineUp {
                items {
                    select(store = selectedItem) {
                        placeholder("My Placeholder")
                        options(myOptions)

                    }

                    p {
                        selectedItem.data.render { it ->
                            p { +"Selected: $it" }
                        }
                    }
                }
            }
        }

        playground {
            source(
                """
                 val myOptions = listOf("black", "red", "yellow")

                 val selectedItem = storeOf("")   
                    
                 select( store = selectedItem) {
                        placeholder("My Placeholder")
                        options(myOptions)
                    }

                    p {
                        selectedItem.data.render { it ->
                            p { +"Selected: (it)" }
                        }
                    }
            """.trimIndent()
            )
        }

        showcaseSection("Sizes")
        paragraph {
            +"Our Select component offers you three sizes: "
            c("small")
            +"|"
            c("normal")
            +"|"
            c("large")
        }

        componentFrame {
            stackUp {
                items {
                    select(store = selectedItem) {
                        placeholder("small")
                        size { small }

                    }
                    select(store = selectedItem) {
                        placeholder("normal - the default")
                        size { normal }
                    }
                    select(store = selectedItem) {
                        placeholder("large")
                        size { large }
                    }
                }

            }
        }

        playground {
            source(
                """
                 select(store = selectedItem) {
                        placeholder("small")
                        size { small }

                    }
                    select(store = selectedItem) {
                        placeholder("normal - the default")
                        size { normal }
                    }
                    select(store = selectedItem) {
                        placeholder("large")
                        size { large }
                    }
            """.trimIndent()
            )
        }

        showcaseSection("Select variants")
        paragraph {
            +"You can choose between four variants for the select component: "
            c("outline")
            +"|"
            c("filled")
            +"|"
            c("flushed")
            +"|"
            c("unstyled")
        }
        componentFrame {
            stackUp {
                items {
                    select(store = selectedItem) {
                        placeholder("outline - default")
                        variant { outline }

                    }
                    select(store = selectedItem) {
                        placeholder("filled")
                        variant { filled }
                    }
                    select(store = selectedItem) {
                        placeholder("flushed")
                        variant { flushed }
                    }
                    select(store = selectedItem) {
                        placeholder("unstyled")
                        variant { unstyled }
                    }

                }
            }
        }

        playground {
            source(
                """
                  
                    select(store = selectedItem) {
                        placeholder("outline - default")
                        variant { outline }

                    }
                    select(store = selectedItem) {
                        placeholder("filled")
                        variant { filled }
                    }
                    select(store = selectedItem) {
                        placeholder("flushed")
                        variant { flushed }
                    }
                    select(store = selectedItem) {
                        placeholder("unstyled")
                        variant { unstyled }
                    }
            """.trimIndent()
            )
        }

        showcaseSection("Change the Icon")
        paragraph {
            +"You can change the default "
            c("icon")
            +"from your select component by "
            +"defining an "
            c("icon")
            +"tag and pass one of our provided icons as follows: "
            c("Theme().icons.icon_name")
        }
        componentFrame {
            lineUp {
                items {
                    select(store = selectedItem) {
                        placeholder("close icon")
                        icon { Theme().icons.close }
                    }
                    select(store = selectedItem) {
                        placeholder("circle-add icon")
                        icon { Theme().icons.circleAdd }

                    }
                }
            }
        }
        playground {
            source(
                """
             select(store = selectedItem) {
                        placeholder("close icon")
                        icon { Theme().icons.close } // icon_name = close
                    }
                    select(store = selectedItem) {
                        placeholder("circle-add icon")
                        icon { Theme().icons.circleAdd } // icon_name = circleAdd

                    }
        """.trimIndent()
            )
        }
    }


}
