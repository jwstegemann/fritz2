package dev.fritz2.headlessdemo.components

import dev.fritz2.core.*
import dev.fritz2.headless.components.DataCollection
import dev.fritz2.headless.components.dataCollection
import dev.fritz2.headless.components.inputField
import dev.fritz2.headless.components.tabGroup
import dev.fritz2.headless.foundation.SortDirection
import dev.fritz2.headless.foundation.utils.scrollintoview.ScrollPosition
import dev.fritz2.headlessdemo.FakePersons
import dev.fritz2.headlessdemo.Person
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLTableRowElement

fun RenderContext.filterInput(id: String, filterStore: Store<String>) {
    inputField("relative my-4 grow", id) {
        value(filterStore)
        div("absolute inset-y-0 left-0 pl-2.5 flex items-center pointer-events-none") {
            icon("w-5 h-5 text-primary-600", content = HeroIcons.search)
        }
        inputTextfield(
            """w-full max-w-sm py-2.5 pl-10 pr-2.5
                    | bg-white rounded-sm border border-primary-600 hover:border-primary-800
                    | font-sans text-sm text-primary-800 placeholder:text-slate-400
                    | disabled:opacity-50
                    | focus:outline-none focus:ring-4 focus:ring-primary-600 focus:border-primary-800""".trimMargin()
        ) {
            placeholder("Filter...")
        }
    }
}

fun Tag<HTMLTableRowElement>.column(title: String, button: Tag<HTMLDivElement>.() -> Unit) {
    th(
        """sticky top-0 pl-3 py-2.5 z-10
            | bg-white tracking-wider
            | text-left text-sm font-medium text-primary-700""".trimMargin()
    ) {
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
            "text-primary-800 h-4 w-4 mt-1 mr-2", content =
            when (it) {
                SortDirection.NONE -> HeroIcons.selector
                SortDirection.ASC -> HeroIcons.sort_ascending
                SortDirection.DESC -> HeroIcons.sort_descending
            }
        )
    }
}

const val AMOUNT_OF_PERSONS = 250

fun RenderContext.collectionDemo() {
    val examples = listOf(
        Triple("DataTable", RenderContext::dataTableDemo, AMOUNT_OF_PERSONS),
        Triple("GridList", RenderContext::gridListDemo, AMOUNT_OF_PERSONS),
    )

    tabGroup("w-full", id = "tabGroup") {
        tabList("max-w-sm flex p-1 space-x-1 bg-primary-900/20 rounded-md") {
            examples.forEach { (category, _, _) ->
                tab(
                    """w-full py-2.5 leading-5
                    | text-sm font-medium rounded
                    | focus:outline-none focus-visible:ring-4 focus-visible:ring-primary-600""".trimMargin()
                ) {
                    className(selected.map { sel ->
                        if (sel == index) "bg-primary-800 text-white shadow-md"
                        else "text-primary-100 hover:bg-primary-900/[0.12]"
                    })
                    +category
                }
            }
        }
        tabPanels("mt-2") {
            examples.forEach { (_, example, amount) ->
                panel("focus:outline-none") {
                    example(this, amount)
                }
            }
        }
    }

}

fun RenderContext.dataTableDemo(amount: Int) {
    val persons = FakePersons(amount)
    val storedPersons = storeOf(persons)
    val selectionStore = storeOf(persons.take(2))
    val storedFilteredSize = storeOf(0)

    val filterStore = storeOf("")
    filterInput("dataTable-filter", filterStore)

    dataCollection<Person>(
        """relative h-96 border border-primary-400
            | sm:rounded overflow-auto focus:outline-none""".trimMargin(),
        id = "dataTable"
    ) {
        data(storedPersons.data, Person::id)

//        selection.single(selectionStore)
        selection.multi(selectionStore)

        filterStore.data handledBy filterByText()

        table("min-w-full") {
            thead {
                tr("divide-x divide-primary-400") {
                    column("Name") {
                        dataCollectionSortButton(
                            compareBy(Person::fullName),
                            compareByDescending(Person::fullName),
                            initialize = sortIcons,
                            classes = "focus:outline-none",
                            id = "dataTable-sort-name"
                        )
                    }
                    column("eMail") {
                    }
                    column("Birthday") {}
                }
            }

            val padding = "px-3 py-2.5 whitespace-nowrap"

            dataCollectionItems(
                "text-sm font-base divide-y-2 divide-primary-100 focus:outline-none",
                tag = RenderContext::tbody
            ) {
                scrollIntoView(vertical = ScrollPosition.center)
                items.map { it.count() } handledBy storedFilteredSize.update
                items.renderEach(Person::id, into = this, batch = true) { item ->
                    dataCollectionItem(
                        item,
                        id = item.fullName,
                        classes = "divide-x divide-primary-400",
                        tag = RenderContext::tr
                    ) {
                        className(selected.combine(active) { sel, act ->
                            if (sel) {
                                if (act) "bg-primary-800 text-primary-100" else "bg-primary-700 text-primary-100"
                            } else {
                                if (act) "bg-primary-300 text-primary-900" else "bg-primary-200 text-primary-900"
                            }
                        })
                        td(padding) { +item.fullName }
                        td(padding) { +item.email }
                        td(padding) { +item.birthday }

                        // only needed for testing
                        attr("data-dataTable-selected", selected.asString())
                        attr("data-dataTable-active", active.asString())
                    }
                }
            }
        }
    }

    div(
        """mt-4 p-2.5
            | bg-primary-100 rounded shadow-sm
            | ring-2 ring-primary-500""".trimMargin(),
        id = "result"
    ) {
        attr("data-selected-count", selectionStore.data.map { it.count() })
        p("font-medium text-sm") {
            selectionStore.data.map { it.count() }
                .combine(storedFilteredSize.data) { sel, count -> "Selected ($sel/$count):" }
                .renderText(into = this)
        }
        ul("text-sm md:grid md:grid-flow-col md:grid-rows-2") {
            selectionStore.data.map { it.map { it.fullName } }.renderEach {
                li { +it }
            }
        }
    }
}

fun RenderContext.gridListDemo(amount: Int) {
    val persons = FakePersons(amount)
    val storedPersons = storeOf(persons)
    val selectionStore = storeOf(persons.take(2))
    val filterStore = storeOf("", id = "gridList-filter")
    val storedFilteredSize = storeOf(0)

    dataCollection<Person>(id = "gridList") {
        data(storedPersons.data, Person::id)

//        selection.single(selectionStore)
        selection.multi(selectionStore)

        div("flex items-center justify-end h-10 max-w-sm mt-4") {
            filterInput(id = "gridList-filter", filterStore)

            dataCollectionSortButton(
                compareBy(Person::fullName),
                compareByDescending(Person::fullName),
                """ml-3 flex justify-center items-center rounded border border-primary-700
                    | cursor-default sm:text-sm
                    | focus:outline-none focus:ring-2 focus:ring-primary-600 """.trimMargin(),
                id = "gridList-sort-name"
            ) {
                direction.render(into = this) {
                    icon(
                        "text-primary-700 h-5 w-5 m-2", content =
                        when (it) {
                            SortDirection.NONE -> HeroIcons.selector
                            SortDirection.ASC -> HeroIcons.sort_ascending
                            SortDirection.DESC -> HeroIcons.sort_descending
                        }
                    )
                }
            }
        }

        filterStore.data handledBy filterByText { "${it.fullName}|${it.email}" }

        div("h-96 pt-4 overflow-x-auto relative") {
            dataCollectionItems(
                "grid grid-cols-1 gap-6 sm:grid-cols-2 xl:grid-cols-3 overflow-y-auto p-2 focus:outline-none",
                tag = RenderContext::ul
            ) {
                scrollIntoView()
                attr("role", "list")
                items.map { it.count() } handledBy storedFilteredSize.update
                items.renderEach(Person::id) { item ->
                    dataCollectionItem(
                        item,
                        "col-span-1 rounded-lg border border-primary-400 divide-y divide-primary-400",
                        id = item.fullName,
                        tag = RenderContext::li
                    ) {
                        className(selected.combine(active) { sel, act ->
                            classes(
                                if (act) "ring-4 ring-primary-600" else "",
                                if (sel) "bg-primary-700 text-primary-100" else "bg-primary-200 text-primary-900"
                            )
                        })
                        div("w-full flex items-center justify-between p-4 space-x-6") {
                            div("flex-1 truncate") {
                                div("flex items-center space-x-3") {
                                    h3("text-sm font-medium truncate") { +item.fullName }
                                }
                                p("mt-2 text-xs truncate opacity-80") {
                                    +"${item.address.street} ${item.address.houseNumber} - ${item.address.postalCode} ${item.address.city}"
                                }
                                p("mt-1 text-xs truncate opacity-80") { +item.birthday }
                            }
                            img("w-16 h-16 bg-gray-300 rounded-md flex-shrink-0") {
                                src(item.portraitUrl)
                                alt("")
                            }
                        }
                        div {
                            div("-mt-px flex divide-x divide-primary-400") {
                                div("w-0 flex-1 flex") {
                                    a(
                                        """relative -mr-px w-0 flex-1 inline-flex items-center justify-center px-4 py-4
                                            | border border-transparent rounded-bl-lg
                                            | text-xs font-medium opacity-85""".trimMargin()
                                    ) {
                                        icon("w-5 h-5", content = HeroIcons.mail)
                                        span("ml-3 truncate flex-1") { +item.email }
                                    }
                                }
                                div("-ml-px w-0 flex-1 flex") {
                                    a(
                                        """relative w-0 flex-1 inline-flex items-center justify-center px-4 py-4
                                            | border border-transparent rounded-br-lg
                                            | text-xs font-medium opacity-85""".trimMargin()
                                    ) {
                                        icon("w-5 h-5", content = HeroIcons.phone)
                                        span("ml-3 truncate") { +item.phone }
                                    }
                                }
                            }
                        }
                        // only needed for testing
                        attr("data-dataTable-selected", selected.asString())
                        attr("data-dataTable-active", active.asString())
                    }
                }
            }
        }
    }

    div(
        """mt-4 p-2.5
            | bg-primary-100 rounded shadow-sm
            | ring-2 ring-primary-500""".trimMargin(),
        id = "result"
    ) {
        attr("data-selected-count", selectionStore.data.map { it.count() })
        p("font-medium text-sm") {
            selectionStore.data.map { it.count() }
                .combine(storedFilteredSize.data) { sel, count -> "Selected ($sel/$count):" }
                .renderText(into = this)
        }
        ul("text-sm md:grid md:grid-flow-col md:grid-rows-2") {
            selectionStore.data.map { it.map { it.fullName } }.renderEach {
                li { +it }
            }
        }
    }
}
