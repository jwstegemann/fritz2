package dev.fritz2.headlessdemo


import dev.fritz2.core.RenderContext
import tooltip


fun RenderContext.tooltipDemo() {

    button("""text-white group bg-blue-700 px-3 py-2 rounded-md inline-flex items-center text-base 
                | font-medium hover:text-opacity-100 focus:outline-none focus-visible:ring-2 hover:bg-blue-800 
                | focus-visible:ring-blue-600 focus-visible:ring-opacity-75""".trimMargin()
    ) {
        +"Some Button"
   }.tooltip("text-sm text-gray-50 bg-gray-800 px-2 py-1 rounded") {
        arrow()
        + "Some more Information"
    }

}
