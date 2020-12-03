package dev.fritz2.kitchensink.demos

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.kitchensink.base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.gettingStarted(): Div {

    return contentFrame {

        showcaseHeader("Getting Started")
        warningBox {
            +"This is a preview version of fritz2 components."
        }

        paragraph {
            +"You can use fritz2 components in any "
            externalLink("Kotlin JS", "https://kotlinlang.org/docs/reference/js-project-setup.html")
            +" project or "
            externalLink("Kotlin Multiplatform", "https://kotlinlang.org/docs/reference/mpp-intro.html")
            +" project."
        }

        showcaseSection("Installation")
        paragraph {
            +"To see how to create a new fritz2 Multiplatform project, take a look at the"
            externalLink("fritz2 project setup docs", "https://docs.fritz2.dev/ProjectSetup.html")
            +" ."
        }
        paragraph {
            +"Add the following to your "
            c("build.gradle.kts")
            +" file in the dependencies section:"
        }
        highlight {
            source(
                """
                 |val commonMain by getting {
                 |  dependencies {
                 |      implementation(kotlin("stdlib"))
                 |      implementation("dev.fritz2:components:0.8")
                 |  }
                 |}
                """.trimMargin()
            )
        }

        showcaseSection("Run in browser")
        paragraph {
            +"To get your JS loaded, you need a static html file."
            +" Here is a short example for an html-file in your JS resources folder (e.g."
            c("src/resources/index.html")
            +"):"
        }
        highlight {
            source("""
                |<html lang="en">
                |<head>
                |   <title>My app title</title>
                |   <!-- add styles, fonts etc. here -->
                |</head>
                |<!-- the id attribute is mandatory for mounting -->
                |<body id="target"> 
                |   Loading...
                |   <!-- add the compile js file here -->
                |   <script src="<project-name>.js"></script>
                |</body>
                |</html>""".trimMargin()
            )
        }

        paragraph {
            +"The next step is adding your code to a Kotlin file (e.g. "
            c("app.kt")
            +") which contains a "
            c("fun main() {...}")
            +" function. In this function, call the"
            c("mount(\"target\")")
            +" function to append your dynamic html to the "
            c("<body>")
            +" element of your page."
        }
        playground { source(
            """fun main() {
                    val router = router(welcome)
                
                    render(Themes().first()) { theme ->
                        div("header") {
                            ...
                        }
                        div("content") {
                            ...
                            router.data.render { site ->
                                when (site) {
                                    welcome -> welcome()
                                    pageA -> pageA()
                                    pageB -> pageB()
                                    else -> welcome()
                                }
                            }
                            ...
                        }
                        div("footer") {
                            ...
                        }
                    }.mount("target") // id for mounting
                }
            """.trimIndent()
        ) }

        showcaseSection("Pre-release versions")
        paragraph {
            +"To get the latest pre-release version of fritz2 components, take a look "
            externalLink("at the setup docs.", "https://docs.fritz2.dev/ProjectSetup.html#pre-release-builds")
        }
    }
}



