package dev.fritz2.headlessdemo

import dev.fritz2.core.*
import dev.fritz2.headless.components.DataCollection
import dev.fritz2.headless.components.dataCollection
import dev.fritz2.headless.components.inputField
import dev.fritz2.headless.components.tabGroup
import dev.fritz2.headless.foundation.SortDirection
import dev.fritz2.headless.foundation.utils.scrollintoview.ScrollPosition
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLTableRowElement

fun Tag<HTMLTableRowElement>.column(title: String, button: Tag<HTMLDivElement>.() -> Unit) {
    th(
        """drop-shadow-sm pl-4 py-3 z-10 text-left text-xs font-medium text-gray-500 uppercase  
        | tracking-wider sticky top-0 bg-gray-50""".trimMargin()
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
            "text-gray-500 h-3 w-3 mt-1 mr-2", content =
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

    tabGroup("w-full") {
        tabList("max-w-sm flex p-1 space-x-1 bg-blue-900/20 rounded-xl") {
            examples.forEach { (category, _, _) ->
                tab(
                    classes(
                        "w-full py-2.5 text-sm leading-5 font-medium text-blue-700 rounded-lg",
                        "focus:outline-none focus:ring-2 ring-offset-2 ring-offset-blue-400 ring-white ring-opacity-60"
                    )
                ) {
                    className(selected.map { sel ->
                        if (sel == index) "bg-white shadow"
                        else "text-blue-100 hover:bg-white/[0.12] hover:text-white"
                    })
                    +category
                }
            }
        }
        tabPanels("mt-2") {
            examples.forEach { (_, example, amount) ->
                panel {
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
    inputField("mt-2 mb-4") {
        value(filterStore)

        inputTextfield(
            """shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block sm:text-sm w-full max-w-sm
            | border-gray-300 px-4 rounded-full""".trimMargin()
        ) {
            placeholder("filter...")
        }
    }

    dataCollection<Person>(
        """shadow h-96 border border-gray-200 sm:rounded-lg overflow-y-auto  
        | overflow-x-auto relative""".trimMargin()
    ) {
        data(storedPersons.data, Person::id)

//        selection.single(selectionStore)
        selection.multi(selectionStore)

        filterStore.data handledBy filterByText()

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
                    }
                    column("Birthday") {}
                }
            }

            val padding = "px-4 py-2 whitespace-nowrap"

            dataCollectionItems(
                "text-sm font-medium text-gray-500 hover:bg-indigo-400",
                tag = RenderContext::tbody
            ) {
                scrollIntoView(vertical = ScrollPosition.center)
                items.map { it.count() } handledBy storedFilteredSize.update
                items.renderEach(Person::id, into = this, batch = true) { item ->
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

    div("bg-gray-300 mt-4 p-2 rounded-lg ring-2 ring-gray-50", id = "result") {
        em {
            selectionStore.data.map { it.count() }
                .combine(storedFilteredSize.data) { sel, count -> "Selected ($sel/$count):" }
                .renderText(into = this)
        }
        ul("") {
            selectionStore.data.map { it.map { it.fullName } }.renderEach {
                li { +it }
            }
        }
    }
}

fun RenderContext.gridListDemo(amount: Int) {
    val persons = FakePersons(amount)
    val storedPersons = storeOf(persons)
    val selectionStore = object : RootStore<List<Person>>(persons.take(2)) {}
    val filterStore = storeOf("")
    val storedFilteredSize = storeOf(0)

    dataCollection<Person> {
        data(storedPersons.data, Person::id)

//        selection.single(selectionStore)
        selection.multi(selectionStore)

        div("h-10 flex items-center") {
            dataCollectionSortButton(
                compareBy(Person::fullName),
                compareByDescending(Person::fullName),
                """w-8 flex justify-center my-2 bg-white rounded-lg 
                | shadow-md cursor-default focus:outline-none focus:ring-2 focus:ring-opacity-75 
                | focus:ring-white focus:ring-offset-orange-300 focus:ring-offset-2 
                | focus:border-indigo-500 sm:text-sm""".trimMargin()
            ) {
                direction.render(into = this) {
                    icon(
                        "text-gray-500 m-2", content =
                        when (it) {
                            SortDirection.NONE -> HeroIcons.selector
                            SortDirection.ASC -> HeroIcons.sort_ascending
                            SortDirection.DESC -> HeroIcons.sort_descending
                        }
                    )
                }
            }

            inputField("ml-4 my-2 grow") {
                value(filterStore)
                placeholder("filter...")
                inputTextfield(
                    """shadow-sm focus:ring-2 focus:ring-opacity-75 focus:ring-white focus:ring-offset-orange-300 
                    | focus:ring-offset-2 focus:outline-none block sm:text-sm w-full max-w-sm border-gray-300 px-4  
                    | rounded-full""".trimMargin()
                ) { }
            }
        }

        filterStore.data handledBy filterByText { "${it.fullName}|${it.email}" }

        div("h-96 pt-4 overflow-x-auto relative") {
            dataCollectionItems(
                "grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3 overflow-y-auto p-2",
                tag = RenderContext::ul
            ) {
                scrollIntoView()
                attr("role", "list")
                items.map { it.count() } handledBy storedFilteredSize.update
                items.renderEach(Person::id) { item ->
                    dataCollectionItem(
                        item,
                        "col-span-1 rounded-lg shadow divide-y divide-gray-300",
                        tag = RenderContext::li
                    ) {
                        className(selected.combine(active) { sel, act ->
                            classes(
                                if (act) "ring-2 ring-offset-2 ring-indigo-600" else "",
                                if (sel) "bg-indigo-100" else "bg-white"
                            )
                        })
                        div("w-full flex items-center justify-between p-6 space-x-6") {
                            div("flex-1 truncate") {
                                div("flex items-center space-x-3") {
                                    h3("text-gray-900 text-sm font-medium truncate") { +item.fullName }
                                    span(
                                        """flex-shrink-0 inline-block px-2 py-0.5 text-green-800 text-xs 
                                        | font-medium bg-green-100 rounded-full""".trimMargin()
                                    ) { +item.birthday }
                                }
                                p("mt-1 text-gray-500 text-sm truncate") {
                                    +"${item.address.postalCode} ${item.address.city}, ${item.address.street} ${item.address.houseNumber}"
                                }
                            }
                            img("w-10 h-10 bg-gray-300 rounded-full flex-shrink-0") {
                                src(item.portraitUrl)
                                alt("")
                            }
                        }
                        div {
                            div("-mt-px flex divide-x divide-gray-300") {
                                div("w-0 flex-1 flex") {
                                    a("""relative -mr-px w-0 flex-1 inline-flex items-center justify-center  
                                        | py-4 text-sm text-gray-700 font-medium border border-transparent  
                                        | rounded-bl-lg hover:text-gray-500""".trimMargin()
                                    ) {
                                        icon("w-5 h-5 text-gray-400", content = HeroIcons.mail)
                                        span("ml-3 truncate") { +item.email }
                                    }
                                }
                                div("-ml-px w-0 flex-1 flex") {
                                    a("""relative w-0 flex-1 inline-flex items-center justify-center py-4 
                                        | text-sm text-gray-700 font-medium border border-transparent rounded-br-lg 
                                        | hover:text-gray-500""".trimMargin()
                                    ) {
                                        icon("w-5 h-5 text-gray-400", content = HeroIcons.phone)
                                        span("ml-3") { +item.phone }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    div("bg-gray-300 mt-4 p-2 rounded-lg ring-2 ring-gray-50", id = "result") {
        em {
            selectionStore.data.map { it.count() }
                .combine(storedFilteredSize.data) { sel, count -> "Selected ($sel/$count):" }
                .renderText(into = this)
        }
        ul("") {
            selectionStore.data.map { it.map { it.fullName } }.renderEach {
                li { +it }
            }
        }
    }
}