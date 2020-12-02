package dev.fritz2.kitchensink.demos

import dev.fritz2.binding.RootStore
import dev.fritz2.components.lineUp
import dev.fritz2.components.radio
import dev.fritz2.components.radioGroup
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.kitchensink.base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf


@ExperimentalCoroutinesApi
fun RenderContext.radiosDemo(): Div {
    val demoItems = flowOf(listOf("item 1", "item 2", "item 3"))

    return contentFrame {
        showcaseHeader("Radios")

        paragraph {
            +"Using a "
            c("radio")
            +" or "
            c("radioGroup")
            +"for smarter single selection of different options. "

        }


        showcaseSection("Usage")
        paragraph {
            +"Define your radio(s) by adding label, the selected state and an eventHandler"
            +"A"
            c("radio")
            +"communicate the state of the component via the given events of boolean type."
            br {}
            +"The"
            c("radioGroup")
            +" using a store to handle the selection and communication. You can display the group in a row or column."
        }
        val usageRadioStore = object : RootStore<Boolean>(true) {}
        val usageRadioGroupStore = object : RootStore<String>("item 2") {}
        componentFrame {
            stackUp {
                items {
                    lineUp {
                        items {
                            radio {
                                label("my Radio")
                                selected { usageRadioStore.data }
                                events {
                                    changes.states() handledBy usageRadioStore.update
                                }
                            }
                        }
                    }
                    lineUp {
                        radioGroup(store = usageRadioGroupStore) {
                            items { demoItems }
                            direction { row }
                        }

                    }
                }
            }
        }
        playground {
            source(
                """
                     val myStore = object : RootStore<Boolean>(true) {}
                     radio {
                        label("my Radio")
                        selected { myStore.data }
                        events {
                            changes.states() handledBy usageRadioStore.update
                        }
                     }
                   
                     val demoItems = flowOf(listOf("item 1", "item 2", "item 3"))
                     val myStore = object : RootStore<String>("item 2") {}
                   
                     radioGroup(store = myStore) {
                        items { demoItems }
                        direction { row }
                     }
                """
            )
        }


        showcaseSection("Customizing")
        paragraph {
            +"You can customize the un(selected) state of the component, also the label"
        }

        componentFrame {
            stackUp {
                items {
                    lineUp {
                        items {

                           radio({
                                background {
                                    color { danger }
                                }
                            }) {
                                label("unselected style")
                                selected { flowOf(false) }
                            }

                            radio({
                                background {
                                    color { danger }
                                }
                            }) {
                                label("selected style")
                                selected { flowOf(true) }
                                selectedStyle {
                                    {
                                        background {
                                            color { success }
                                        }
                                    }
                                }
                            }

                            radio{
                                label("danger, margined label")
                                selected { flowOf(false) }
                                labelStyle {
                                    {
                                      margins { left { larger } }
                                      color { danger }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            playground {
                source(
                    """
                         radio({
                                background {
                                    color { danger }
                                }
                            }) {
                                label("unselected style")
                                selected { flowOf(false) }
                            }

                            radio({
                                background {
                                    color { danger }
                                }
                            }) {
                                label("selected style")
                                selected { flowOf(true) }
                                selectedStyle {
                                    {
                                        background {
                                            color { success }
                                        }
                                    }
                                }
                            }

                            radio{
                                label("danger, margined label")
                                selected { flowOf(false) }
                                labelStyle {
                                    {
                                      margins { left { larger } }
                                      color { danger }
                                    }
                                }
                            }
                    """
                )
            }
        }

        showcaseSection("Sizes")
        paragraph {
            +"choose from on three predefined sizes ("
            c("small")
            +", "
            c("normal")
            +" or  "
            c("large")
            +") or scale your radio(es) to your needs using the styling parameter."
        }
        val sizesRadioStore = object : RootStore<Boolean>(false){}
        val sizesRadio1Store = object : RootStore<Boolean>(false){}
        val sizesRadio2Store = object : RootStore<Boolean>(false){}
        componentFrame {
            stackUp {
                items {
                    lineUp {
                        items {
                            radio {
                                label("small")
                                size { small }
                                selected { sizesRadioStore.data }
                                events {
                                    changes.states() handledBy sizesRadioStore.update
                                }
                            }

                            radio {
                                label("normal")
                                size { normal }
                                selected { sizesRadio1Store.data }
                                events {
                                    changes.states() handledBy sizesRadio1Store.update
                                }
                            }

                            radio {
                                label("large")
                                size { large }
                                selected { sizesRadio2Store.data }
                                events {
                                    changes.states() handledBy sizesRadio2Store.update
                                }
                            }
                        }
                    }
                }
            }
            playground {
                source(
                    """
                         radio {
                                  label("small")
                                  size { small }
                                  selected { myStore.data }
                                  events {
                                      changes.states() handledBy myStore.update
                                  }
                              }
                              
                              radio {
                                  label("normal")
                                  size { normal } // default
                                  selected { myStore.data }
                                  events {
                                      changes.states() handledBy myStore.update
                                  }
                              }
                              
                              radio {
                                  label("large")
                                  size { large }
                                  selected { sizesRadio2Store.data }                                  
                                  events {
                                      changes.states() handledBy myStore.update
                                  }
                              }
                    """
                )
            }
        }

        showcaseSection("Disabled")
        val disabledRadioStore = object : RootStore<Boolean>(false){}
        val disabledRadio1Store = object : RootStore<Boolean>(true){}
        componentFrame {
            stackUp {
                items {
                    lineUp {
                        items {
                            radio {
                                label("disabled Radio")
                                disabled{ flowOf(true) }
                                selected { disabledRadioStore.data }
                                events {
                                    changes.states() handledBy disabledRadioStore.update
                                }
                            }

                            radio {
                                label("disabled Radio")
                                disabled { flowOf(true) }
                                selected { disabledRadio1Store.data }
                                events {
                                    changes.states() handledBy disabledRadio1Store.update
                                }
                            }
                        }
                    }
                }
            }
            playground {
                source(
                    """
                          radio {
                                  label("disabled Radio")
                                  disabled{ flowOf(true) }
                                  selected { myStore.data }
                                  events {
                                      changes.states() handledBy myStore.update
                                  }
                              }

                              radio {
                                  label("disabled Radio")
                                  disabled { flowOf(true) }
                                  selected { myStore.data }
                                  events {
                                      changes.states() handledBy myStore.update
                                  }
                              }
                    """
                )
            }
        }
    }
    /*val myItems = listOf("ffffff", "rrrrrr", "iiiiii", "tttttt", "zzzzzz", "222222")
    val myPairs =
        listOf((1 to "ffffff"), (2 to "rrrrrr"), (3 to "iiiiii"), (4 to "tttttt"), (5 to "zzzzzz"), (6 to "222222"))

    return dev.fritz2.kitchensink.contentFrame {
        dev.fritz2.kitchensink.showcaseHeader("SingleSelect")
        dev.fritz2.kitchensink.paragraph {
            +"You can choose from 3 radio sizes. You may also chose custom colors for the radio background and"
            +" border. However, any custom styles you apply to the component will be rendered for the"
            +" internal container element only. Also keep in mind that the default styling of our"
            +" components is not done yet."
        }

        lineUp({
            alignItems { baseline }
        }) {
            items {
                stackUp({
                    alignItems { baseline }
                    margins {
                        right { "1.5rem" }
                        bottom { "1.5rem" }
                    }
                }) {
                    val mySelectedItem = "ffffff"
                    val selectedItemStore = RootStore(mySelectedItem)

                    items {
                        dev.fritz2.kitchensink.showcaseSection("SingleSelect large")
                        lineUp({
                            margins { bottom { "2.0rem" } }
                            alignItems { baseline }
                        }) {
                            items {
                                radioGroup(
                                    store = selectedItemStore,
                                    id = "radioGroup1"
                                ) {
                                    items { flowOf(myItems) }
                                    size { large }
                                }
                            }
                        }
                        div {
                            selectedItemStore.data.render { selectedItem ->
                                p { +"Selected: $selectedItem" }
                            }
                        }
                    }
                }

                stackUp({
                    alignItems { baseline }
                    margins {
                        left { "1.5rem" }
                        right { "1.5rem" }
                        bottom { "1.5rem" }
                    }
                }) {
                    val mySelectedItem = "iiiiii"
                    val selectedItemStore = RootStore(mySelectedItem)

                    items {
                        dev.fritz2.kitchensink.showcaseSection("SingleSelect normal, disabled")
                        stackUp({
                            margins { bottom { "2.0rem" } }
                            alignItems { baseline }
                        }) {
                            items {
                                radioGroup(
                                    store = selectedItemStore,
                                    id = "radioGroup2"
                                ) {
                                    disabled(flowOf(true))
                                    items { flowOf(myItems) }
                                }
                            }
                        }
                        div {
                            selectedItemStore.data.render { selectedItem ->
                                p { +"Selected: $selectedItem" }
                            }
                        }
                    }
                }

                stackUp({
                    alignItems { baseline }
                    margins {
                        left { "1.5rem" }
                        right { "1.5rem" }
                        bottom { "1.5rem" }
                    }
                }) {
                    val mySelectedItem = "ffffff"
                    val selectedItemStore = RootStore(mySelectedItem)

                    items {
                        dev.fritz2.kitchensink.showcaseSection("SingleSelect small, custom colors, horizontal")
                        stackUp({
                            margins { bottom { "2.0rem" } }
                            alignItems { baseline }
                        }) {
                            items {
                                radioGroup(
                                    store = selectedItemStore,
                                    id = "radioGroup3"
                                ) {
                                    items { flowOf(myItems) }
                                    direction { row }
                                    size { small }
                                    selectedStyle {
                                        {
                                            background {
                                                color { warning }
                                            }
                                        }
                                    }
                                    itemStyle {
                                        {
                                            background {
                                                color { secondary }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        div {
                            selectedItemStore.data.render { selectedItem ->
                                p { +"Selected: $selectedItem" }
                            }
                        }
                    }
                }
            }
        }
    }*/
}