package dev.fritz2.kitchensink.base

import dev.fritz2.components.box
import dev.fritz2.components.stackUp
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.staticStyle
import kotlinx.browser.window

const val backgroundColor = "#2B2B2B"
const val playgroundMarker = "pg-code"

/**
 * Class for configuring the appearance of a PopoverComponent.
 */
class PlaygroundComponent {
    companion object {
        init {
            staticStyle(
                """
                .CodeMirror {
                  font-size: 0.85em !important;
                }
                
                .CodeMirror-gutters {
                    visibility: hidden !important
                }
                
                .executable-fragment-wrapper {
                    margin-bottom: 0 !important;
                }
                
                .executable-fragment {
                    border-width: 0 !important;
                }
                
                .js-code-output-executor {
                    display: none;
                }
                
                .icon {
                    background: none !important;
                }
                
            """.trimIndent()
            )
        }

        suspend fun update() {
            window.setTimeout({
                try {
                    js("KotlinPlayground(\".$playgroundMarker\")")
                } catch (t: Throwable) {
                    console.error(t)
                }
            }, 300)
        }
    }


    var source: String = "// your code goes here"
    fun source(value: String) {
        source = value
    }

    var size: Int = 200
    fun size(value: Int) {
        size = value
    }

}

fun RenderContext.playground(
    styling: BasicParams.() -> Unit = {},
    baseClass: StyleClass? = null,
    id: String? = null,
    prefix: String = "playground",
    build: PlaygroundComponent.() -> Unit = {}
) {
    val component = PlaygroundComponent().apply(build)

    stackUp ({
        margins { top { large } }
    }){
        items {
            box({
                background { color { backgroundColor } }
                radius { "12px" }
                width { full }
                padding { small }
            }) {
                code(playgroundMarker) {
                    attr("data-target-platform", "js")
                    attr("data-highlight-only", true)
                    attr("theme", "darcula")
                    attr("auto-indent", "true")
                    attr("lines", "false")
                    attr("output-size", component.size)

                    +component.source
                }
            }
        }
    }
}