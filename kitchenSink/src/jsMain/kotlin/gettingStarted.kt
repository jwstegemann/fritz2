import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.gettingStarted(): Div {

    return contentFrame {

        showcaseHeader("Getting Started")

        paragraph {
            +"You can use fritz2 components in any Kotlin JS project or Kotlin multiplatform project."
        }

        showcaseSection("Installation")
        paragraph {
            +"Inside your Kotlin multiplatform project you must add the following to your "
            c("build.gradle.kts")
            +" or "
            c("build.gradle")
            +" file in the dependencies section:"
        }

        componentFrame {
            lineUp {
                items {

                }
            }
        }
        playground {
            source(
                """
                    clickButton { text("click me") } handledBy modal

                    pushButton {
                        icon { fromTheme { arrowLeft } }
                        text("previous")
                    }

                    pushButton {
                        icon { fromTheme { arrowRight } }
                        iconRight()
                        text("next")
                    }

                    pushButton { icon { fromTheme { check } } }
                """
            )
        }
    }
}



