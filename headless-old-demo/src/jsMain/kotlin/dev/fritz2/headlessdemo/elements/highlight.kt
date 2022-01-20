package dev.fritz2.headlessdemo.elements

import dev.fritz2.dom.afterMount
import dev.fritz2.dom.html.RenderContext

fun RenderContext.highlight(source: String, classes: String?, id: String?, language: String = "kotlin") {
    div("rounded-lg border border-solid border-gray-300") {
        pre("p-4 m-4 rounded-lg overflow-auto highlight lang-$language") {
            code {
                +source.trimIndent()
            }
        }
        afterMount { _, _ ->
            try {
                js(
                    """
                    document.querySelectorAll('.highlight').forEach(function(block) {
                        hljs.highlightBlock(block);
                    });
                    """
                )
            } catch (t: Throwable) {
                console.error(t)
            }
        }
    }
}
