import dev.fritz2.binding.RootStore
import dev.fritz2.binding.const
import dev.fritz2.binding.each
import dev.fritz2.binding.handledBy
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.render
import dev.fritz2.dom.states
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map


@ExperimentalCoroutinesApi
fun HtmlElements.singleSelectDemo(): Div {
    return div {

        lineUp({
            alignItems { baseline }
        }) {
            items {
                stackUp({
                    alignItems { baseline }
                    margins { all { "1.5rem" } }
                }) {
                    val myItems = listOf("Arthur", "Tricia", "Zaphod", "Ford", "Marvin")
                    val mySelectedItem = "Zaphod"
                    val selectedItemStore = RootStore(mySelectedItem)

                    items {
                        h3 { +"SingleSelect large" }
                        lineUp({
                            margins { bottom { "2.0rem" } }
                            alignItems { baseline }
                        }) {
                            items {
                                radioGroup(
                                    {},
                                    id = "radioGroup1"
                                ) {
                                    disabled { const(false) }
                                    items { myItems }
                                    selected { mySelectedItem }
                                    radioSize { large }
                                    borderColor { "black" }
                                    backgroundColor { "white" }
                                    checkedBackgroundColor { "gray" }
                                } handledBy selectedItemStore.update
                            }
                        }
                        div {
                            selectedItemStore.data.render { selectedItem ->
                                p { +"Selected: $selectedItem" }
                            }.bind()
                        }
                    }
                }

                stackUp({
                    alignItems { baseline }
                    margins { all { "1.5rem" } }
                }) {
                    val myItems = listOf("Arthur", "Tricia", "Zaphod", "Ford", "Marvin")
                    val mySelectedItem = "Marvin"
                    val selectedItemStore = RootStore(mySelectedItem)

                    items {
                        h3 { +"SingleSelect normal, disabled" }
                        lineUp({
                            margins { bottom { "2.0rem" } }
                            alignItems { baseline }
                        }) {
                            items {
                                radioGroup(
                                    {},
                                    id = "radioGroup2"
                                ) {
                                    disabled { const(true) }
                                    items { myItems }
                                    selected { mySelectedItem }
                                    radioSize { normal }
                                    borderColor { "black" }
                                    backgroundColor { "white" }
                                    checkedBackgroundColor { "gray" }
                                } handledBy selectedItemStore.update
                            }
                        }
                        div {
                            selectedItemStore.data.render { selectedItem ->
                                p { +"Selected: $selectedItem" }
                            }.bind()
                        }
                    }
                }

                stackUp({
                    alignItems { baseline }
                    margins { all { "1.5rem" } }
                }) {
                    val myItems = listOf("Arthur", "Tricia", "Zaphod", "Ford", "Marvin")
                    val mySelectedItem = "Tricia"
                    val selectedItemStore = RootStore(mySelectedItem)

                    items {
                        h3 { +"SingleSelect small, custom colors" }
                        lineUp({
                            margins { bottom { "2.0rem" } }
                            alignItems { baseline }
                        }) {
                            items {
                                radioGroup(
                                    {},
                                    id = "radioGroup3"
                                ) {
                                    disabled { const(false) }
                                    items { myItems }
                                    selected { mySelectedItem }
                                    radioSize { small }
                                    borderColor { "black" }
                                    backgroundColor { "white" }
                                    checkedBackgroundColor { "gray" }
                                } handledBy selectedItemStore.update
                            }
                        }
                        div {
                            selectedItemStore.data.render { selectedItem ->
                                p { +"Selected: $selectedItem" }
                            }.bind()
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalCoroutinesApi
fun HtmlElements.multiSelectDemo(): Div {
    return div {
        lineUp({
            alignItems { baseline }
        }) {
            items {
                stackUp({
                    alignItems { baseline }
                    margins { all { "1.5rem" } }
                }) {
                    items {
                        h3 { +"MultiSelect large" }
                        stackUp({
                            margins { bottom { "2.0rem" } }
                            alignItems { baseline }
                            verticalAlign { top }
                        }) {
                            val myItems = listOf("Arthur", "Tricia", "Zaphod", "Ford", "Marvin")
                            val mySelectedItems = listOf("Zaphod", "Ford")

                            val selectedItemsStore = RootStore(mySelectedItems)

                            items {
                                checkboxGroup(
                                    {},
                                    id = "checkGroup1"
                                ) {
                                    disabled { const(false) }
                                    items { myItems }
                                    initialSelection { mySelectedItems }
                                    checkboxSize { large }
                                    borderColor { "black" }
                                    backgroundColor { "white" }
                                    checkedBackgroundColor { "gray" }
                                } handledBy selectedItemsStore.update

                                div {
                                    h4 { +"Selected:" }
                                    ul {
                                        selectedItemsStore.data.each().render { selectedItem ->
                                            li { +selectedItem }
                                        }.bind()
                                    }
                                }
                            }
                        }
                    }
                }

                stackUp({
                    alignItems { baseline }
                    margins { all { "1.5rem" } }
                }) {
                    items {
                        h3 { +"MultiSelect normal" }
                        stackUp({
                            margins { bottom { "2.0rem" } }
                            alignItems { baseline }
                        }) {
                            val myItems = listOf("Arthur", "Tricia", "Zaphod", "Ford", "Marvin")
                            val mySelectedItems = listOf("Zaphod", "Ford")

                            val selectedItemsStore = RootStore(mySelectedItems)

                            items {
                                checkboxGroup(
                                    {},
                                    id = "checkGroup2"
                                ) {
                                    disabled { const(false) }
                                    items { myItems }
                                    initialSelection { mySelectedItems }
                                    checkboxSize { normal }
                                    borderColor { "black" }
                                    backgroundColor { "white" }
                                    checkedBackgroundColor { "gray" }
                                } handledBy selectedItemsStore.update

                                div {
                                    h4 { +"Selected:" }
                                    ul {
                                        selectedItemsStore.data.each().render { selectedItem ->
                                            li { +selectedItem }
                                        }.bind()
                                    }
                                }
                            }
                        }
                    }
                }

                stackUp({
                    alignItems { baseline }
                    margins { all { "1.5rem" } }
                }) {
                    items {
                        h3 { +"Single Checkboxes, custom colors" }
                        stackUp({
                            margins { bottom { "2.0rem" } }
                            alignItems { baseline }
                        }) {
                            val checkedStore1 = RootStore(true)
                            val checkedStore2 = RootStore(true)
                            val checkedStore3 = RootStore(false)

                            items {
                                checkbox(
                                    {},
                                    id = "check1"
                                ) {
                                    text = const("small")
                                    checkboxSize { small }
                                    checked { checkedStore1.data }
                                    disabled { const(false) }
                                    borderColor { "black" }
                                    backgroundColor { "white" }
                                    checkedBackgroundColor { "gray" }
                                    events {
                                        changes.states() handledBy checkedStore1.update
                                    }
                                }
                                checkbox(
                                    {},
                                    id = "check2"
                                ) {
                                    text = const("normal, disabled")
                                    checkboxSize { normal }
                                    checked { checkedStore2.data }
                                    disabled { const(true) }
                                    borderColor { "black" }
                                    backgroundColor { "white" }
                                    checkedBackgroundColor { "gray" }
                                    events {
                                        changes.states() handledBy checkedStore2.update
                                    }
                                }
                                checkbox(
                                    {},
                                    id = "check3"
                                ) {
                                    text = const("large, disabled")
                                    checkboxSize { large }
                                    checked { checkedStore3.data }
                                    disabled { const(true) }
                                    borderColor { "black" }
                                    backgroundColor { "white" }
                                    checkedBackgroundColor { "gray" }
                                    events {
                                        changes.states() handledBy checkedStore3.update
                                    }
                                }

                                div {
                                    checkedStore1.data.map {
                                        p { +"checked: $it" }
                                    }.bind()
                                }
                                div {
                                    checkedStore2.data.map {
                                        p { +"checked2: $it" }
                                    }.bind()
                                }
                                div {
                                    checkedStore3.data.map {
                                        p { +"checked3: $it" }
                                    }.bind()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}