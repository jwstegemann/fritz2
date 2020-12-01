import dev.fritz2.binding.RootStore
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.theme.Theme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf


@ExperimentalCoroutinesApi
fun RenderContext.checkboxesDemo(): Div {
    val myItems = listOf("ffffff", "rrrrrr", "iiiiii", "tttttt", "zzzzzz", "222222")
    val myPairs =
        listOf((1 to "ffffff"), (2 to "rrrrrr"), (3 to "iiiiii"), (4 to "tttttt"), (5 to "zzzzzz"), (6 to "222222"))

    return contentFrame {
        showcaseHeader("Checkbox")

        paragraph {
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
                        showcaseSection("MultiSelect large, vertical")
                        stackUp({
                            margins { bottom { "2.0rem" } }
                            alignItems { baseline }
                            verticalAlign { top }
                        }) {
                            val mySelectedItems = flowOf(listOf(1 to "222222"))

                            val selectedItemsStore = object : RootStore<List<Pair<Int, String>>>(emptyList()) {
                                val toggle = handle<Pair<Int, String>> { list, item ->
                                    if (list.contains(item)) {
                                        list.filter { it != item }
                                    } else {
                                        list + item
                                    }

                                }
                            }

                            items {
                                checkboxGroup<Pair<Int, String>>(
                                    {},
                                    store = selectedItemsStore,
                                    id = "checkGroup1"
                                ) {
                                    disabled(flowOf(false))
                                    label {
                                        it.second
                                    }
                                    items { flowOf(myPairs) }
                                    size { large }
                                }

                                (::div.styled {
                                    background {
                                        color { light }
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
                                            li { +selectedItem.second }
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
                        showcaseSection("MultiSelect normal, horizontal")
                        stackUp({
                            margins { bottom { "2.0rem" } }
                            alignItems { baseline }
                        }) {
                            val mySelectedItems = listOf("ffffff", "222222")

                            val selectedItemsStore = object : RootStore<List<String>>(mySelectedItems) {
                                val toggle = handle<String> { list, item ->
                                    if (list.contains(item)) {
                                        list.filter { it != item }
                                    } else {
                                        list + item
                                    }

                                }
                            }

                            items {
                                checkboxGroup(
                                    store = selectedItemsStore,
                                    id = "checkGroup2"
                                ) {
                                    items { flowOf(myItems) }
                                    //TODO: @Transpi like Button?
                                    icon { Theme().icons.call }
                                    direction { row }
                                }

                                (::div.styled {
                                    background {
                                        color { light }
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
                        showcaseSection("Single Checkboxes, custom colors")
                        stackUp({
                            margins { bottom { "2.0rem" } }
                            alignItems { baseline }
                        }) {
                            items {
                                checkbox(
                                    {
                                        background { color { "red" } }
                                    },
                                    id = "check1"
                                ) {
                                    label {
                                        +"small text incl. custom labelcontent"
                                        icon { fromTheme { arrowLeft } }
                                    }
                                    size { small }
                                    checkedStyle {
                                        {
                                            background { color { "green" } }
                                        }
                                    }
                                    checked { checkedStore1.data }
                                    events {
                                        changes.states() handledBy checkedStore1.update
                                    }
                                }
                                div {
                                    checkedStore1.data.render {
                                        (::p.styled {
                                            background {
                                                color { light }
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
                                    label(flowOf("normal, disabled"))
                                    checked { checkedStore2.data }
                                    disabled { flowOf(true) }
                                    events {
                                        changes.states() handledBy checkedStore2.update
                                    }
                                }
                                div {
                                    checkedStore2.data.render {
                                        (::p.styled {
                                            background {
                                                color { light }
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
                                    {

                                    },
                                    id = "check3"
                                ) {
                                    label(flowOf("large, disabled"))
                                    checked { checkedStore3.data }
                                    disabled { flowOf(true) }
                                    size { large }
                                    events {
                                        changes.states() handledBy checkedStore3.update
                                    }
                                }
                                div {
                                    checkedStore3.data.render {
                                        (::p.styled {
                                            background {
                                                color { light }
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

