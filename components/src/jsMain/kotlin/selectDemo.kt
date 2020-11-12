import dev.fritz2.binding.RootStore
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.styling.theme.theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf

val myItems = listOf("ffffff", "rrrrrr", "iiiiii", "tttttt", "zzzzzz", "222222")

@ExperimentalCoroutinesApi
fun RenderContext.singleSelectDemo(): Div {

    return stackUp({
        alignItems { start }
        padding { "1rem" }
    }) {
        items {

            h1 { +"SingleSelect Showcase" }

            p {
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
                                        size { large }
                                    } handledBy selectedItemStore.update
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
                                        disabled { flowOf(true) }
                                        items { myItems }
                                        selected { mySelectedItem }
                                    } handledBy selectedItemStore.update
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
                        margins { all { "1.5rem" } }
                    }) {
                        val mySelectedItem = "ffffff"
                        val selectedItemStore = RootStore(mySelectedItem)

                        items {
                            h3 { +"SingleSelect small, custom colors, horizontal" }
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
                                        direction { row }
                                        size { small }
                                        checkedBackgroundColor { theme().colors.warning }
                                        backgroundColor { theme().colors.tertiary }
                                    } handledBy selectedItemStore.update
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
        }
    }
}

@ExperimentalCoroutinesApi
fun RenderContext.multiSelectDemo(): Div {
    return stackUp({
        alignItems { start }
        padding { "1rem" }
    }) {
        items {

            h1 { +"MultiSelect Showcase" }

            p {
                +"You can choose from 3 checkbox sizes. You may also chose custom colors for the box background and"
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
                            all { "1.5rem" }
                            left { "0" }
                        }
                    }) {
                        items {
                            h3 { +"MultiSelect large, vertical" }
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
                                        disabled { flowOf(false) }
                                        items { myItems }
                                        size { large }
                                        initialSelection { mySelectedItems }
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
                                            selectedItemsStore.data.renderEach { selectedItem ->
                                                li { +selectedItem }
                                            }
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
                            h3 { +"MultiSelect normal, horizontal" }
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
                                        disabled { flowOf(false) }
                                        items { myItems }
                                        initialSelection { mySelectedItems }
                                        direction { row }
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
                                            selectedItemsStore.data.renderEach { selectedItem ->
                                                li { +selectedItem }
                                            }
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
                                        text = flowOf("small")
                                        size { small }
                                        checked { checkedStore1.data }
                                        borderColor { theme().colors.tertiary }
                                        checkedBackgroundColor { theme().colors.warning }
                                        backgroundColor { theme().colors.tertiary }
                                        events {
                                            changes.states() handledBy checkedStore1.update
                                        }
                                    }
                                    div {
                                        checkedStore1.data.render {
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
                                        }
                                    }
                                    checkbox(
                                        {},
                                        id = "check2"
                                    ) {
                                        text = flowOf("normal, disabled")
                                        checked { checkedStore2.data }
                                        disabled { flowOf(true) }
                                        borderColor { theme().colors.info }
                                        checkedBackgroundColor { theme().colors.warning }
                                        backgroundColor { theme().colors.tertiary }
                                        events {
                                            changes.states() handledBy checkedStore2.update
                                        }
                                    }
                                    div {
                                        checkedStore2.data.render {
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
                                        }
                                    }
                                    checkbox(
                                        {},
                                        id = "check3"
                                    ) {
                                        text = flowOf("large, disabled")
                                        checked { checkedStore3.data }
                                        disabled { flowOf(true) }
                                        borderColor { "orange" }
                                        size { large }
                                        checkedBackgroundColor { "yellow" }
                                        backgroundColor { theme().colors.tertiary }
                                        events {
                                            changes.states() handledBy checkedStore3.update
                                        }
                                    }
                                    div {
                                        checkedStore3.data.render {
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
