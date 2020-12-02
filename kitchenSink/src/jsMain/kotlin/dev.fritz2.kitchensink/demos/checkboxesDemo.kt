package dev.fritz2.kitchensink.demos

import dev.fritz2.binding.RootStore
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.kitchensink.*
import dev.fritz2.kitchensink.base.playground
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf


@ExperimentalCoroutinesApi
fun RenderContext.checkboxesDemo(): Div {

    val demoItems = flowOf(listOf("item 1", "item 2", "item 3"))

      return contentFrame {
          showcaseHeader("Checkboxes")

          paragraph {
              +"Using a "
              c("checkbox")
              +" or "
              c("checkboxGroup")
              +"for smarter multiple selection of different options. "

          }


          showcaseSection("Usage")
          paragraph {
              +"Define your checkbox(es) by adding label, the checked state and an eventHandler"
              +"A"
              c("checkbox")
              +"communicate the state of the component via the given events of boolean type."
              br {}
              +"The"
              c("checkboxGroup")
              +" using a store to handle the selections and communication. You can display the group in a row or column."
          }
          val usageCheckboxStore = object : RootStore<Boolean>(true) {}
          val usageCheckboxGroupStore = object : RootStore<List<String>>(emptyList()) {}
          componentFrame {
              stackUp {
                  items {
                      lineUp {
                          items {
                              checkbox {
                                  label("my Checkbox")
                                  checked { usageCheckboxStore.data }
                                  events {
                                      changes.states() handledBy usageCheckboxStore.update
                                  }
                              }
                          }
                      }
                      lineUp {
                          checkboxGroup(store = usageCheckboxGroupStore) {
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
                     val usageCheckboxStore = object : RootStore<Boolean>(true) {}
                     checkbox {
                        label("my Checkbox")
                        checked { usageCheckboxStore.data }
                        events {
                            changes.states() handledBy usageCheckboxStore.update
                        }
                     }
                   
                     val demoItems = flowOf(listOf("item 1", "item 2", "item 3"))
                     val usageCheckboxGroupStore = object : RootStore<List<String>>(emptyList()) {}
                   
                     checkboxGroup(store = usageCheckboxGroupStore) {
                        items { demoItems }
                        direction { row }
                     }
                """
              )
          }


          showcaseSection("Customizing")
          paragraph {
              +"You've a great scope to customize the box(es)."
              +"Setting up the checked "
              c("icon")
              +" with the fritz2 icon-Set. But you can also set the "
              c("labelStyle")
              +","
              c("checkedStyle")
              +"and of course you can overwrite the default style of the"
              c("checkbox")
              +"The "
              c("checkboxGroup")
              +" uses the same invocations."
          }
          val customizingCheckboxStore = object : RootStore<Boolean>(false){}
          val customizingCheckbox1Store = object : RootStore<Boolean>(false){}
          val customizingCheckbox2Store = object : RootStore<Boolean>(false){}
          componentFrame {
              stackUp {
                  items {
                      lineUp {
                          items {
                              checkbox {
                                  label("fritz2 icon")
                                  checked { customizingCheckboxStore.data }
                                  icon { Theme().icons.fritz2 }
                                  events {
                                      changes.states() handledBy customizingCheckboxStore.update
                                  }
                              }
                              checkbox {
                                  label("checked style")
                                  checked { customizingCheckbox1Store.data }
                                  checkedStyle {
                                      {
                                          background {
                                              color { warning }
                                          }
                                      }
                                  }
                                  events {
                                      changes.states() handledBy customizingCheckbox1Store.update
                                  }
                              }
                              checkbox({
                                  background {
                                      color { danger }
                                  }
                              }) {
                                  label("some different colors")
                                  checked { customizingCheckbox2Store.data }
                                  checkedStyle {
                                      {
                                          background {
                                              color { success }
                                          }
                                      }
                                  }
                                  events {
                                      changes.states() handledBy customizingCheckbox2Store.update
                                  }
                              }
                          }
                      }
                  }
              }

              playground {
                  source(
                      """
                         checkbox {
                                label("fritz2 icon")
                                checked { myStore.data }
                                icon { Theme().icons.fritz2 }
                                events {
                                    changes.states() handledBy myStore.update
                                }
                            }
                            
                            checkbox {
                                label("checked style")
                                checked { myStore.data }
                                checkedStyle {
                                    {
                                        background {
                                            color { warning }
                                        }
                                    }
                                }    
                                events {
                                    changes.states() handledBy myStore.update
                                }
                            }
                            
                            checkbox( {
                                background {
                                    color { danger }
                                }
                            }) {
                                label("some different colors")
                                checked { myStore.data }
                                checkedStyle {
                                    {
                                        background {
                                            color { success }
                                        }
                                    }
                                }
                                events {
                                    changes.states() handledBy myStore.update
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
              +") or scale your checkbox(es) to your needs using the styling parameter."
          }
          val sizesCheckboxStore = object : RootStore<Boolean>(false){}
          val sizesCheckbox1Store = object : RootStore<Boolean>(false){}
          val sizesCheckbox2Store = object : RootStore<Boolean>(false){}
          componentFrame {
              stackUp {
                  items {
                      lineUp {
                          items {
                              checkbox {
                                  label("small")
                                  size { small }
                                  checked { sizesCheckboxStore.data }
                                  events {
                                      changes.states() handledBy sizesCheckboxStore.update
                                  }
                              }

                              checkbox {
                                  label("normal")
                                  size { normal }
                                  checked { sizesCheckbox1Store.data }
                                  events {
                                      changes.states() handledBy sizesCheckbox1Store.update
                                  }
                              }

                              checkbox {
                                  label("large")
                                  size { large }
                                  checked { sizesCheckbox2Store.data }
                                  events {
                                      changes.states() handledBy sizesCheckbox2Store.update
                                  }
                              }
                          }
                      }
                  }
              }
              playground {
                  source(
                      """
                         checkbox {
                                  label("small")
                                  size { small }
                                  checked { myStore.data }
                                  events {
                                      changes.states() handledBy myStore.update
                                  }
                              }
                              
                              checkbox {
                                  label("normal")
                                  size { normal } // default
                                  checked { myStore.data }
                                  events {
                                      changes.states() handledBy myStore.update
                                  }
                              }
                              
                              checkbox {
                                  label("large")
                                  size { large }
                                  checked { sizesCheckbox2Store.data }                                  
                                  events {
                                      changes.states() handledBy myStore.update
                                  }
                              }
                    """
                  )
              }
          }

          showcaseSection("Disabled")
          val disabledCheckboxStore = object : RootStore<Boolean>(false){}
          val disabledCheckbox1Store = object : RootStore<Boolean>(true){}
          componentFrame {
              stackUp {
                  items {
                      lineUp {
                          items {
                              checkbox {
                                  label("disabled Checkbox")
                                  disabled{ flowOf(true) }
                                  checked { disabledCheckboxStore.data }
                                  events {
                                      changes.states() handledBy disabledCheckboxStore.update
                                  }
                              }

                              checkbox {
                                  label("disabled Checkbox")
                                  disabled { flowOf(true) }
                                  checked { disabledCheckbox1Store.data }
                                  events {
                                      changes.states() handledBy disabledCheckbox1Store.update
                                  }
                              }
                          }
                      }
                  }
              }
              playground {
                  source(
                      """
                          checkbox {
                                  label("disabled Checkbox")
                                  disabled{ flowOf(true) }
                                  checked { myStore.data }
                                  events {
                                      changes.states() handledBy myStore.update
                                  }
                              }

                              checkbox {
                                  label("disabled Checkbox")
                                  disabled { flowOf(true) }
                                  checked { myStore.data }
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

