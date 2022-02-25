package dev.fritz2.headlessdemo

import dev.fritz2.core.RenderContext
import dev.fritz2.core.Tag
import dev.fritz2.headless.components.dataCollection
import org.w3c.dom.HTMLTableRowElement

fun Tag<HTMLTableRowElement>.column(title: String) {
    th("drop-shadow-sm pl-6 py-3 z-10 text-left text-xs font-medium text-gray-500 uppercase tracking-wider sticky top-0 bg-gray-50") {
        div("w-full flex flex-row items-center") {
            p("flex-auto") {
                +title
            }
        }
    }
}

fun RenderContext.collectionDemo() {
    dataCollection<Person>("shadow h-80 border border-gray-200 sm:rounded-lg overflow-y-auto overflow-x-auto relative") {
        data(TableStore.data)

        table("min-w-full divide-y divide-gray-200 bg-white") {
            thead {
                tr {
                    column("Name")
                    column("eMail")
                    column("Birthday")
                }
            }

            dataCollectionItems("text-sm font-medium text-gray-500", tag = RenderContext::tbody) {
                items.renderEach { item ->
                    tr {
                        td { +item.fullName }
                        td { +item.email }
                        td { +item.birthday }
                    }
                }
            }
        }

    }
}
