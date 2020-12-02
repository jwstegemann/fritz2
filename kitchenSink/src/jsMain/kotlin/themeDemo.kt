import dev.fritz2.components.lineUp
import dev.fritz2.components.playground
import dev.fritz2.components.switch
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
fun RenderContext.themeDemo(): Div {

    return contentFrame {

        showcaseHeader("Theme")

        paragraph {
            +"A"
            c("Theme")
            +"""
            groups predefined sets of values and scales to implement a consistent constraint-based design system.
            """.trimIndent()
        }

        paragraph {
            +"fritz2's components come with a predefined"
            c("DefaultTheme")
            +""" and can be used to start styling your app. Of course you can derive custom themes 
                from this one to change values, or even extend the theme by your own definitions, from simple property 
                values to complex predefined styles.""".trimIndent()
        }

        showcaseSection("Content")
        paragraph {
            +"A theme groups the following presets:"
            table("settings-table") {
                tr {
                    th { +"Name" }
                    th { +"Type" }
                    th { +"Description" }
                }
                tr {
                    td { +"reset" }
                    td { c("String") }
                    td { +"css to reset browser's defaults and set your own" }
                }
                tr {
                    td { +"name" }
                    td { c("String") }
                    td { +"an human readable name" }
                }
                tr {
                    td { +"breakPoints" }
                    td { c("ResponsiveValue") }
                    td { +"break points for different screen sizes that apply when working with ResponsiveValues" }
                }
            }
        }

        showcaseSection("Custom Themes")
        paragraph {
            +"To create a custom"
            c("Theme")
            +" you can easily create a new class inheriting from"
            c("DefaultTheme")
            +" and change just the definitions you want."

        }
        playground {
            source(
                """
                    class LargeFonts : DefaultTheme() {
                        override val name = "large Fonts"
                    
                        override val fontSizes = ScaledValue(
                            smaller = "1.125rem",
                            small = "1.25rem",
                            normal = "1.5rem",
                            large = "1.875rem",
                            larger = "2.25rem",
                            huge = "3rem"
                        )                    
                    }
                """
            )
        }

        showcaseSection("Changing the current Theme")
        paragraph {
            +"To access the current theme use"
            c("Theme()")
            +". Change the current theme by"
            c("Theme.use(someTheme)")
            "."
        }
        componentFrame {
            lineUp {
                items {
                    switch {
                        label("use large Fonts")
                        checked { ThemeStore.data.map { it == 1 } }
                        events {
                            changes.states().map { if (it) 1 else 0 } handledBy ThemeStore.selectTheme
                        }
                    }
                }
            }
        }
        playground {
            source(
                """
                    val themes = listOf<ExtendedTheme>(SmallFonts(), LargeFonts())

                    object ThemeStore : RootStore<Int>(0) {
                        val selectTheme = handle<Int> { _, index ->
                            Theme.use(themes[index])
                            index
                        }
                    }
                        
                    //set your default theme    
                    render(themes.first()) { theme ->
                        //...

                        switch {
                            label("use large Fonts")
                            checked { ThemeStore.data.map {it == 1} }
                            events {
                                changes.states().map { if (it) 1 else 0 } handledBy ThemeStore.update
                            }
                        }
                    }
                """
            )
        }


    }
}



