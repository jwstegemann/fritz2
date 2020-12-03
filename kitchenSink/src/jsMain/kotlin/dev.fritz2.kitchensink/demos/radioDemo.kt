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
            c("Radios")
            +"and "
            c("RadioGroups")
            +"offer a smart option for single selections."
        }


        showcaseSection("Usage")
        paragraph {
            +"Define your radio(s) by adding a label, the selected state, and an event handler."
            +" A"
            c("radio")
            +"communicates the state of the component via the given events of boolean type."
            br {}
            +"The"
            c("radioGroup")
            +" uses a store to handle the selection and communication. You can display the group in a row or column."
        }
        val usageRadioStore = object : RootStore<Boolean>(true) {}
        val usageRadioGroupStore = object : RootStore<String>("item 2") {}
        componentFrame {
            stackUp {
                items {
                    lineUp {
                        items {
                            radio {
                                label("A single Radio")
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
            +"You can customize both the selected and unselected state of the component, and also the label."
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
                                label("danger font color, margined label")
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
                                label("danger font color, margined label")
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
            +"Choose from one of three predefined sizes ("
            c("small")
            +", "
            c("normal")
            +", or  "
            c("large")
            +"), or scale your radio(s) to your needs using the styling parameter."
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
}