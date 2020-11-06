import dev.fritz2.binding.RootStore
import dev.fritz2.binding.const
import dev.fritz2.binding.each
import dev.fritz2.binding.handledBy
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import dev.fritz2.dom.html.render
import dev.fritz2.dom.states
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map

val myItems = listOf("ffffff", "rrrrrr", "iiiiii", "tttttt", "zzzzzz", "222222")

@ExperimentalCoroutinesApi
fun HtmlElements.singleSelectDemo(): Div {

    return div {

        stackUp({
            alignItems { start }
            padding { "1rem" }
        }) {
            items {

                h1 { +"SingleSelect Showcase" }

                p {
                    +"You can choose from 3 radio sizes. You may also chose custom colors for the radio background and "
                            +"border. However, any custom styles you apply to the component will be rendered for the "
                            +"internal container element only."
                }

                lineUp({
                    alignItems { baseline }
                }) {
                    items {
                        stackUp({
                            alignItems { baseline }
                            margins {
                                all { "1.5rem" }
                                left { "0" }
                            }
                        }) {
                            val mySelectedItem = "ffffff"
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
                                            items { myItems }
                                            selected { mySelectedItem }
                                            radioSize { large }
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
                            val mySelectedItem = "iiiiii"
                            val selectedItemStore = RootStore(mySelectedItem)

                            items {
                                h3 { +"SingleSelect normal, disabled" }
                                stackUp({
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
                            val mySelectedItem = "ffffff"
                            val selectedItemStore = RootStore(mySelectedItem)

                            items {
                                h3 { +"SingleSelect small, custom colors" }
                                stackUp({
                                    margins { bottom { "2.0rem" } }
                                    alignItems { baseline }
                                }) {
                                    items {
                                        radioGroup(
                                            {},
                                            id = "radioGroup3"
                                        ) {
                                            items { myItems }
                                            selected { mySelectedItem }
                                            radioSize { small }
                                            checkedBackgroundColor { theme().colors.warning }
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
    }
}

@ExperimentalCoroutinesApi
fun HtmlElements.multiSelectDemo(): Div {
    return div {

        stackUp({
            alignItems { start }
            padding { "1rem" }
        }) {
            items {

                h1 { +"MultiSelect Showcase" }

                p {
                    +"You can choose from 3 checkbox sizes. You may also chose custom colors for the box background and "
                    +"border. However, any custom styles you apply to the component will be rendered for the "
                    +"internal container element only."
                }

                lineUp({
                    alignItems { baseline }
                }) {
                    items {
                        stackUp({
                            alignItems { baseline }
                            margins {
                                all { "1.5rem" }
                                left { "0" }
                            }
                        }) {
                            items {
                                h3 { +"MultiSelect large" }
                                stackUp({
                                    margins { bottom { "2.0rem" } }
                                    alignItems { baseline }
                                    verticalAlign { top }
                                }) {
                                    val mySelectedItems = listOf("222222")

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
                                        } handledBy selectedItemsStore.update

                                        (::div.styled {
                                            background {
                                                color { theme().colors.light }
                                            }
                                            paddings {
                                                left { "0.5rem" }
                                                right { "0.5rem" }
                                            }
                                            radius { "5%" }
                                        }) {
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
                                    val mySelectedItems = listOf("ffffff", "222222")

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
                                        } handledBy selectedItemsStore.update

                                        (::div.styled {
                                            background {
                                                color { theme().colors.light }
                                            }
                                            paddings {
                                                left { "0.5rem" }
                                                right { "0.5rem" }
                                            }
                                            radius { "5%" }
                                        }) {
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
                            val checkedStore1 = RootStore(true)
                            val checkedStore2 = RootStore(true)
                            val checkedStore3 = RootStore(false)
                            items {
                                h3 { +"Single Checkboxes, custom colors" }
                                stackUp({
                                    margins { bottom { "2.0rem" } }
                                    alignItems { baseline }
                                }) {
                                    items {
                                        checkbox(
                                            {},
                                            id = "check1"
                                        ) {
                                            text = const("small")
                                            checkboxSize { small }
                                            checked { checkedStore1.data }
                                            borderColor { theme().colors.tertiary }
                                            checkedBackgroundColor { theme().colors.warning }
                                            events {
                                                changes.states() handledBy checkedStore1.update
                                            }
                                        }
                                        div {
                                            checkedStore1.data.map {
                                                (::p.styled {
                                                    background {
                                                        color { theme().colors.light }
                                                    }
                                                    paddings {
                                                        left { "0.5rem" }
                                                        right { "0.5rem" }
                                                    }
                                                    radius { "5%" }
                                                }) {
                                                    +"$it"
                                                }
                                            }.bind()
                                        }
                                        checkbox(
                                            {},
                                            id = "check2"
                                        ) {
                                            text = const("normal, disabled")
                                            checkboxSize { normal }
                                            checked { checkedStore2.data }
                                            disabled { const(true) }
                                            borderColor { theme().colors.info }
                                            checkedBackgroundColor { theme().colors.warning }
                                            events {
                                                changes.states() handledBy checkedStore2.update
                                            }
                                        }
                                        div {
                                            checkedStore2.data.map {
                                                (::p.styled {
                                                    background {
                                                        color { theme().colors.light }
                                                    }
                                                    paddings {
                                                        left { "0.5rem" }
                                                        right { "0.5rem" }
                                                    }
                                                    radius { "5%" }
                                                }) {
                                                    +"$it"
                                                }
                                            }.bind()
                                        }
                                        checkbox(
                                            {},
                                            id = "check3"
                                        ) {
                                            text = const("large, disabled")
                                            checkboxSize { large }
                                            checked { checkedStore3.data }
                                            disabled { const(true) }
                                            borderColor { "orange" }
                                            checkedBackgroundColor { "yellow" }
                                            events {
                                                changes.states() handledBy checkedStore3.update
                                            }
                                        }
                                        div {
                                            checkedStore3.data.map {
                                                (::p.styled {
                                                    background {
                                                        color { theme().colors.light }
                                                    }
                                                    paddings {
                                                        left { "0.5rem" }
                                                        right { "0.5rem" }
                                                    }
                                                    radius { "5%" }
                                                }) {
                                                    +"$it"
                                                }
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
    }
}