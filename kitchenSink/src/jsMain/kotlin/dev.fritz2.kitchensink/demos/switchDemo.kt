package dev.fritz2.kitchensink.demos

import dev.fritz2.binding.RootStore
import dev.fritz2.components.switch
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.kitchensink.base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.switchDemo(): Div {
    val checkedStore1 = RootStore(false)
    val checkedStore2 = RootStore(true)
    val checkedStore3 = RootStore(false)
    val checkedStore4 = RootStore(false)
    val checkedStore5 = RootStore(true)
    val checkedStore6 = RootStore(false)

    return contentFrame {
        showcaseHeader("Switch")
        paragraph {
            +"A switch button works like a checkbox. It can be used to set a single property in your application."
        }
        showcaseSection("Usage")
        paragraph {
            +"Create a switch with your own label and size. You can set the background color for the unchecked and"
            +" checked state, and even customize the color of the dot."
        }

        componentFrame {
            switch() {
                label("Default style")
                checked { checkedStore1.data }
                events {
                    changes.states() handledBy checkedStore1.update
                }
            }
        }
        componentFrame {
            switch(styling = {
                background {
                    color { danger }
                }
            }) {
                label("Custom background color")
                checked { checkedStore2.data }
                dotStyle {
                    {
                        background { color { base } }
                    }
                }
                events {
                    changes.states() handledBy checkedStore2.update
                }
            }
        }
        componentFrame {
            switch(styling = {
                background {
                    color { info }
                }
            }) {
                label("Different dot color")
                checked { checkedStore3.data }
                checkedStyle {
                    {
                        background {
                            color { "pink" }
                        }
                    }
                }
                dotStyle {
                    {
                        background {
                            color { danger }
                        }
                    }
                }
                events {
                    changes.states() handledBy checkedStore3.update
                }
            }
        }
        playground {
            source(
                """
                    switch(styling = {
                        background {
                            color { info } // change background color here
                        }
                    }) {
                        label("Different dot color")
                        checked { <your boolean> } // defines whether switch is checked or not 
                        checkedStyle {
                            {
                                background {
                                    color { "pink" } // change checked background color here
                                }
                            }
                        }
                        dotStyle {
                            {
                                background {
                                    color { danger } // change dot color here
                                }
                            }
                        }
                        events {
                            changes.states() handledBy <your boolean>.update
                            //changes the state of your check boolean
                        }
                    }
                """
            )
        }

        showcaseSection("Sizes")
        paragraph {
            +"Choose from the available sizes "
            c("small")
            +", "
            c("normal")
            +", and "
            c("large")
            +". "
        }

        componentFrame {
            switch() {
                label("Small size")
                size { small }
                checked { checkedStore4.data }
                events {
                    changes.states() handledBy checkedStore4.update
                }
            }
        }
        componentFrame {
            switch() {
                label("Normal size")
                size { normal }
                checked { checkedStore5.data }
                events {
                    changes.states() handledBy checkedStore5.update
                }
            }
        }
        componentFrame {
            switch() {
                label("Large size")
                size { large }
                checked { checkedStore6.data }
                events {
                    changes.states() handledBy checkedStore6.update
                }
            }
        }
        playground {
            source(
                """
                    switch() {
                        label("Small size")
                        size { small } // change size here
                    }
                """
            )
        }
    }
}