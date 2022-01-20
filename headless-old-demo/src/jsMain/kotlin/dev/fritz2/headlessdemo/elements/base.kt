package dev.fritz2.headlessdemo.elements

import dev.fritz2.dom.html.RenderContext


fun RenderContext.demoContainer(content: RenderContext.() -> Unit) =
    div("flex flex-col gap-8 px-8 pt-6 pb-8 max-w-auto bg-white md:m-8 md:shadow md:rounded-lg md:max-w-4xl") {
        content()
    }


fun RenderContext.demoSection(title: String?, classes: String?, id: String?, content: RenderContext.() -> Unit) {
    div {
        if (title != null) {
            div("mb-4 text-lg text-primary font-semibold") {
                +title
            }
        }
        div("flex flex-col gap-4 text-gray-500") {
            content()
        }
    }
}


fun RenderContext.componentFrame(content: RenderContext.() -> Unit) =
    div("p-4 rounded-lg border border-solid border-gray-300") {
        content()
    }
