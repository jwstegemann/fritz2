import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
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
            +"Inside your Kotlin multiplatform project add the following to your "
            c("build.gradle.kts")
            +" or "
            c("build.gradle")
            +" file in the dependencies section:"
        }
        playground {
            source(
                """
                 // build.gradle.kts
                 dependencies {
                    implementation("dev.fritz2:components:0.8")
                 }
                 // build.gradle
                 dependencies {
                    implementation 'dev.fritz2:components:0.8'
                 }
                """
            )
        }

        showcaseSection("Run in browser")
        paragraph {
            +"To get your JS loaded you need a static html file. "
            +"Here is a short example for a html-file in your JS resources folder (e.g. "
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
            +"Next step is to add your code into a Kotlin file (e.g. "
            c("app.kt")
            +") which contains a "
            c("fun main() {...}")
            +" function. In this function you call the "
            c("mount(\"target\")")
            +" function to append your dynamic html to the "
            c("<body>")
            +" element on your page."
        }
        playground { source(
            """fun main() {
                    val router = router(welcome)
                
                    render(themes.first()) { theme ->
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
//        infoBox {
//            +"To write a multi-page application like this one. It is a good practice to create your "
//            c("router")
//            +" and handle your routes here."
//        }

        showcaseSection("Pre-release versions")
        paragraph {
            +"For getting the latest pre-release version of fritz2 components take a look "
            externalLink("here", "https://docs.fritz2.dev/ProjectSetup.html#pre-release-builds")
        }
    }
}



