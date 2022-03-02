package dev.fritz2.headlessdemo

import dev.fritz2.core.*
import dev.fritz2.headless.components.*
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLTableRowElement

fun Tag<HTMLTableRowElement>.column(title: String, button: Tag<HTMLDivElement>.() -> Unit) {
    th("drop-shadow-sm pl-4 py-3 z-10 text-left text-xs font-medium text-gray-500 uppercase tracking-wider sticky top-0 bg-gray-50") {
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

val sortIcons: DataCollection<Person, HTMLDivElement>.DataCollectionSortButton<HTMLButtonElement>.() -> Unit = {
    direction.render {
        icon(
            "text-gray-500 h-3 w-3 mt-1 mr-2", content =
            when (it) {
                SortDirection.NONE -> HeroIcons.selector
                SortDirection.ASC -> HeroIcons.sort_ascending
                SortDirection.DESC -> HeroIcons.sort_descending
            }
        )
    }
}

fun RenderContext.collectionDemo() {
//    val selectionStore = object : RootStore<Person?>(null) {}
    val persons = FakePersons(100)
    val storedPersons = storeOf(persons)
    val selectionStore = object : RootStore<List<Person>>(persons.take(2)) {}

    val filterStore = storeOf("")
    inputField("mt-2 mb-4") {
        value(filterStore)

        inputTextfield("shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-1/2 sm:text-sm border-gray-300 px-4 rounded-full") {placeholder("filter...")
        }
    }

    dataCollection<Person>("shadow h-80 border border-gray-200 sm:rounded-lg overflow-y-auto overflow-x-auto relative") {
        data(storedPersons.data, Person::id)

//        selection.single(selectionStore)
        selection.multi(selectionStore)

        filterStore.data handledBy filterByText

        table("min-w-full divide-y divide-gray-200 bg-white") {
            thead {
                tr("divide-x divide-gray-100") {
                    column("Name") {
                        dataCollectionSortButton(
                            compareBy(Person::fullName),
                            compareByDescending(Person::fullName),
                            initialize = sortIcons
                        )
                    }
                    column("eMail") {
                        // TODO: Geht aktuell nicht, weil Sorting nicht public ist im Button
                        /*
                        dataCollectionSortButton(compareBy(Person::email), compareByDescending(Person::email)) {
                            keydowns.mapNotNull { if(shortcutOf(it) == Keys.Space) SORTING else null } handledBy sortBy
                        }

                         */
                    }
                    column("Birthday") {}
                }
            }

            val padding = "px-4 py-2 whitespace-nowrap"

            dataCollectionItems("text-sm font-medium text-gray-500 hover:bg-indigo-400", tag = RenderContext::tbody) {
                items.renderEach(Person::id) { item ->
                    dataCollectionItem(item, tag = RenderContext::tr) {
                        className(selected.combine(active) { sel, act ->
                            if (sel) {
                                if (act) "bg-indigo-200" else "bg-indigo-100"
                            } else {
                                if (act) "bg-indigo-50" else "odd:bg-white even:bg-gray-50"
                            }
                        })
                        td(padding) { +item.fullName }
                        td(padding) { +item.email }
                        td(padding) { +item.birthday }
                    }
                }
            }
        }

    }

    div("bg-gray-300 mt-4 p-2 rounded-lg ring-2 ring-gray-50") {
        em { +"Selected: " }
        ul("") {
            selectionStore.data.map { it.map { it.fullName } }.renderEach {
                li { +it }
            }
        }
    }
}
