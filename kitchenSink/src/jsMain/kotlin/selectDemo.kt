import dev.fritz2.binding.RootStore
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

val myItems = listOf("ffffff", "rrrrrr", "iiiiii", "tttttt", "zzzzzz", "222222")
val myPairs = listOf((1 to "ffffff"), (2 to "rrrrrr" ), (3 to "iiiiii"), (4 to "tttttt"), ( 5 to "zzzzzz"), (6 to "222222"))

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
                                        checkedBackgroundColor { warning }
                                        backgroundColor { tertiary }
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
                                val mySelectedItems = flowOf(listOf(1 to "222222"))

                                val selectedItemsStore = object: RootStore<List<Pair<Int, String>>>(emptyList()) {
                                    val toggle = handle<Pair<Int, String>> { list, item ->
                                        if( list.contains(item) ) {
                                            list.filter { it != item }
                                        } else {
                                            list + item
                                        }

                                    }
                                }

                                items {
                                    checkboxGroup<Pair<Int,String>>(
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
                            h3 { +"MultiSelect normal, horizontal" }
                            stackUp({
                                margins { bottom { "2.0rem" } }
                                alignItems { baseline }
                            }) {
                                val mySelectedItems = listOf("ffffff", "222222")

                                val selectedItemsStore = object: RootStore<List<String>>(mySelectedItems) {
                                    val toggle = handle<String>{ list, item ->
                                        if( list.contains(item) ) {
                                            list.filter { it != item }
                                        } else {
                                            list + item
                                        }

                                    }
                                }

                                items {
                                    checkboxGroup<String>(
                                        store = selectedItemsStore,
                                        id = "checkGroup2"
                                    ) {
                                        items { flowOf(myItems) }
                                        icon {  theme().icons.bell  }
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
                            h3 { +"Single Checkboxes, custom colors" }
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
                                               background { color {"green"}}
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
                                        label( flowOf("normal, disabled") )
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
}
