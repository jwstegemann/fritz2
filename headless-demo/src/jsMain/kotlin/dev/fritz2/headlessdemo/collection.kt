package dev.fritz2.headlessdemo

import dev.fritz2.core.*
import dev.fritz2.headless.components.SortDirection
import dev.fritz2.headless.components.dataCollection
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLTableRowElement

fun Tag<HTMLTableRowElement>.column(title: String, button: Tag<HTMLDivElement>.() -> Unit) {
    th("drop-shadow-sm pl-6 py-3 z-10 text-left text-xs font-medium text-gray-500 uppercase tracking-wider sticky top-0 bg-gray-50") {
        div("w-full flex flex-row items-center") {
            p("flex-auto") {
                +title
            }
            div("flex-initial") {
                button()
            }
        }
    }
}

fun RenderContext.collectionDemo() {

//    val selectionStore = object : RootStore<Person?>(null) {}
    val selectionStore = object : RootStore<List<Person>>(fakeData[false]!!.take(2)) {}

    dataCollection<Person>("shadow h-80 border border-gray-200 sm:rounded-lg overflow-y-auto overflow-x-auto relative") {
        data(TableStore.data)
//        selection.single(selectionStore)
        selection.multi(selectionStore)

        table("min-w-full divide-y divide-gray-200 bg-white") {
            thead {
                tr {
                    column("Name") {
                        dataCollectionSortButton(compareBy(Person::fullName), compareByDescending(Person::fullName)) {
                            direction.render {
                                //TODO: auslagern für demos in icon-function
                                svg("text-gray-500 h-3 w-3") {
                                    content(
                                        when (it) {
                                            SortDirection.NONE -> HeroIcons.selector
                                            SortDirection.ASC -> HeroIcons.sort_ascending
                                            SortDirection.DESC -> HeroIcons.sort_descending
                                        }
                                    )
                                    viewBox("0 0 20 20")
                                    fill("currentColor")
                                    attr("aria-hidden", "true")
                                }
                            }
                        }
                    }
                    column("eMail") {

                    }
                    column("Birthday") {

                    }
                }
            }

            dataCollectionItems("text-sm font-medium text-gray-500", tag = RenderContext::tbody) {
                items.renderEach { item ->
                    dataCollectionItem(item, tag = RenderContext::tr) {
                        //TODO: add className for String?
                        className(selected.map {if (it) "bg-indigo-200" else ""})
                        td { +item.fullName }
                        td { +item.email }
                        td { +item.birthday }
                    }
                }
            }
        }

    }

    div("mt-10") {
        selectionStore.data.renderText()
    }
}
