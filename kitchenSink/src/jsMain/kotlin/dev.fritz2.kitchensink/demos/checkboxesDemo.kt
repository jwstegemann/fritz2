package dev.fritz2.kitchensink.demos

import dev.fritz2.binding.RootStore
import dev.fritz2.binding.storeOf
import dev.fritz2.components.checkbox
import dev.fritz2.components.checkboxGroup
import dev.fritz2.components.lineUp
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.kitchensink.base.*
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf


@ExperimentalCoroutinesApi
fun RenderContext.checkboxesDemo(): Div {

    return contentFrame {
        showcaseHeader("Checkboxes")

        paragraph {
            c("Checkboxes")
            +" and"
            c("CheckboxGroups")
            +" provide a solution for multi selections. They come with their own options, and"
            +" of course you can customize their appearance with the use of the styling parameter."
        }

        paragraph {
            +"Please note that the creation of stores was omitted in some of the examples to keep the source fragments short."
        }

        val demoItems = listOf("item 1", "item 2", "item 3")
        val usageCheckboxStore = storeOf(true)
        val usageCheckboxGroupStore = RootStore(listOf("item 2", "item 3"))

        showcaseSection("Usage")
        paragraph {
            +"Single "
            c("Checkboxes")
            +" simply need a Flow of Boolean representing their state, passed via the"
            c("checked")
            +" function. If you want to connect a handler to the state changes, use the event context."
        }
        componentFrame {
            checkbox {
                label("A single Checkbox")
                checked { usageCheckboxStore.data }
                events {
                    changes.states() handledBy usageCheckboxStore.update
                }
            }
        }
        playground {
            source(
                """
                checkbox {
                    label("A single Checkbox")
                    checked { store.data }
                    events {
                        changes.states() handledBy store.update
                    }
                }
                """
            )
        }

        paragraph {
            +" If you need more than one checkbox, try using a checkbox group component. It accepts a Flow of"
            c("List<T>")
            +" as group items, and its selection event returns all currently checked entries."
            +" The example below uses Strings, but any type can be displayed. Since the store is a non-optional"
            +" argument anyway, the component always connects the checked-handler automatically. Using the"
            c("direction")
            +" parameter, you can display the group in a row or as a column."
        }
        componentFrame {
            checkboxGroup(store = usageCheckboxGroupStore) {
                items { flowOf(demoItems) }
                direction { row }
            }
        }
        playground {
            source(
                """
                val allItems = listOf("item 1", "item 2", "item 3")
                val checkedItems = storeOf(listOf("item 2", "item 3"))
                checkboxGroup(store = storeOf(checkedItems)) {
                    items { flowOf(allItems) }
                    direction { row }
                }
                """
            )
        }

        showcaseSection("Customizing")
        paragraph {
            +"You can customize the checked styles, unchecked styles, and the component label. The unchecked"
            +" styles go directly into the styling parameter, while you need to use the component functions "
            c("checkedStyle")
            +" and "
            c("labelStyle")
            +" for their respective changes in appearance."
            +" Additionally, you can change your checkboxes' check mark with any "
            c("icon")
            +" from the fritz2 icon-Set."
        }

        componentFrame {
            stackUp {
                items {
                    checkbox({
                        background { color { "tomato" } }
                    }) {
                        label("changed unchecked background color")
                        checked { usageCheckboxStore.data }
                        events {
                            changes.states() handledBy usageCheckboxStore.update
                        }
                    }

                    checkbox {
                        label("changed checkmark to fritz2 icon")
                        icon { Theme().icons.fritz2 }
                        checked { usageCheckboxStore.data }
                        events {
                            changes.states() handledBy usageCheckboxStore.update
                        }
                    }

                    checkbox {
                        label("custom label style: larger margin")
                        labelStyle { { margins { left { larger } } } }
                        checked { usageCheckboxStore.data }
                        events {
                            changes.states() handledBy usageCheckboxStore.update
                        }
                    }
                }
            }
        }
        playground {
            source(
                """
                    checkbox({
                        background { color { "tomato" } }
                    }) {
                        label("changed unchecked background color")
                    }

                    checkbox {
                        label("changed checkmark to fritz2 icon")
                        checked { flowOf(true) }
                        icon { Theme().icons.fritz2 }
                    }

                    checkbox {
                        label("custom label style: larger margin")
                        labelStyle { { margins { left { larger } } } }
                    }
                    """
            )
        }

        showcaseSection("Sizes")
        paragraph {
            +"Choose from the three predefined sizes "
            c("small")
            +", "
            c("normal")
            +", or  "
            c("large")
            +", or scale your checkboxes to your needs using the styling parameter."
        }

        componentFrame {
            lineUp(
                {
                    alignItems { center }
                }
            ){
                items {
                    checkbox {
                        label("small")
                        size { small }
                        checked { usageCheckboxStore.data }
                        events {
                            changes.states() handledBy usageCheckboxStore.update
                        }
                    }
                    checkbox {
                        label("normal")
                        checked { usageCheckboxStore.data }
                        events {
                            changes.states() handledBy usageCheckboxStore.update
                        }
                    }
                    checkbox {
                        label("large")
                        checked { usageCheckboxStore.data }
                        size { large }
                        events {
                            changes.states() handledBy usageCheckboxStore.update
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
                    }
                    checkbox {
                        label("normal")
                    }
                    checkbox {
                        label("large")
                        size { large }
                    }
                    """
            )
        }

        showcaseSection("Disabled")
        componentFrame {
            stackUp {
                items {
                    checkbox {
                        checked { usageCheckboxStore.data }
                        label("A disabled checkbox or checkboxGroup can not be selected.")
                        disabled { flowOf(true) }
                        events {
                            changes.states() handledBy usageCheckboxStore.update
                        }
                    }
                    checkboxGroup(store = usageCheckboxGroupStore) {
                        items { flowOf(demoItems) }
                        direction { column }
                        disabled { flowOf(true) }
                    }
                }

            }
        }
        playground {
            source(
                """
                    checkbox {
                        label("A disabled Checkbox or CheckboxGroup can not be selected.")
                        disabled { flowOf(true) }
                    }
                    
                    checkboxGroup(store = selectedItemsStore) {
                        items { flowOf(allItems) }
                        direction { column }
                        disabled { flowOf(true) }
                    }
                    """
            )
        }
    }
}

