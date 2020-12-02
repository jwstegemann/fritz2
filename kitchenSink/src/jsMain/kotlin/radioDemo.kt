import dev.fritz2.binding.RootStore
import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf


@ExperimentalCoroutinesApi
fun RenderContext.radiosDemo(): Div {
    val myItems = listOf("ffffff", "rrrrrr", "iiiiii", "tttttt", "zzzzzz", "222222")
    val myPairs =
        listOf((1 to "ffffff"), (2 to "rrrrrr"), (3 to "iiiiii"), (4 to "tttttt"), (5 to "zzzzzz"), (6 to "222222"))

    return contentFrame {
        showcaseHeader("SingleSelect")
        paragraph {
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
                        showcaseSection("SingleSelect large")
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
                        showcaseSection("SingleSelect normal, disabled")
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
                        showcaseSection("SingleSelect small, custom colors, horizontal")
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
    }
}