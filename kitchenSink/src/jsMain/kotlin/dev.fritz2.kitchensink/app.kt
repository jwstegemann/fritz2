package dev.fritz2.kitchensink

import dev.fritz2.binding.RootStore
import dev.fritz2.components.icon
import dev.fritz2.components.lineUp
import dev.fritz2.components.navBar
import dev.fritz2.components.stackUp
import dev.fritz2.dom.mount
import dev.fritz2.kitchensink.base.*
import dev.fritz2.kitchensink.demos.*
import dev.fritz2.routing.router
import dev.fritz2.styling.name
import dev.fritz2.styling.params.styled
import dev.fritz2.styling.staticStyle
import dev.fritz2.styling.theme.Theme
import dev.fritz2.styling.theme.render
import dev.fritz2.styling.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi

val themes = listOf<ExtendedTheme>(SmallFonts(), LargeFonts())

const val welcome_ = "Welcome"
const val gettingStarted_ = "Getting Started"
const val icons_ = "Icons"
const val spinner_ = "Spinner"
const val input_ = "Input"
const val buttons_ = "Buttons"
const val formcontrol_ = "Formcontrol"
const val flexbox_ = "Flexbox"
const val gridbox_ = "Gridbox"
const val checkboxes_ = "Checkboxes"
const val radios_ = "Radios"
const val switch_ = "Switch"
const val stack_ = "Stack"
const val modal_ = "Modal"
const val popover_ = "Popover"
const val styling_ = "Styling"
const val theme_ = "Theme"
const val tooltip_ = "Tooltip"
const val responsive_ = "Responsiveness"
const val textarea_ = "Textarea"

val router = router(welcome_)

object ThemeStore : RootStore<Int>(0) {
    val selectTheme = handle<Int> { _, index ->
        Theme.use(themes[index])
        index
    }
}

const val welcomeContentStaticCss = """
    background-image: url("https://unsplash.com/photos/LmyPLbbUWhA/download?force=true&w=1920");
    background-repeat: no-repeat;
    background-attachment: fixed;
"""

const val settingsTableStaticCss = """
        font-family: Inter, sans-serif;
        color: rgb(45, 55, 72);
        text-align: left;
        margin-top: 32px;
        width: 100%;
        border-collapse: collapse;
        
        & > tr > th {
            background: #F7FAFC;
            font-weight: 600;
            padding: 0.5rem;
            font-size: 0.875rem;
        }

        & > tr > td {
            padding: 0.5rem;
            border-top-width: 1px;
            border-color: lightgray;
            font-size: 0.875rem;
            white-space: normal;
        }
"""

@ExperimentalCoroutinesApi
fun main() {
    staticStyle("settings-table", settingsTableStaticCss)
    val welcomeContent = staticStyle("welcome-content", welcomeContentStaticCss)

    val router = router("")

    render(themes.first()) { theme ->
        navBar {
            brand {
                (::a.styled {
                    after {
                        textAlign { center }
                        background { color { primary } }
                        color { light }
                    }
                    alignItems { end }
                }) {
                    href("https://www.fritz2.dev/")
                    target("_new")

                    icon({
                        size { "2.5rem" }
                        color { primary }
                    }) { fromTheme { fritz2 } }

                    (::span.styled {
                        margins { left { normal } }
                        verticalAlign { sub }
                        fontSize { larger }
                        fontWeight { lighter }
                    }) { +"Components" }
                }
                //FIXME: convert to styles
                (::span.styled {
                    css("""
                    display: inline-flex;
                    vertical-align: top;
                    -moz-box-align: center;
                    align-items: center;
                    max-width: 100%;
                    font-weight: 500;
                    line-height: 1.2;
                    min-height: 1.5rem;
                    min-width: 1.5rem;
                    font-size: 0.875rem;
                    border-radius: 0.375rem;
                    padding-left: 0.5rem;
                    padding-right: 0.5rem;
                    background: none repeat scroll 0% 0%;
                    """.trimIndent())
                    background {
                        color { warning }
                    }
                    margins {
                        left { small }
                    }
                }) { +"Preview" }
            }

            actions {
                lineUp {
                    items {
                        navAnchor("Documentation", "https://docs.fritz2.dev/")
                        navAnchor("API", "https://api.fritz2.dev")
                        navAnchor("Examples", "https://www.fritz2.dev/examples.html")
                        navAnchor("Github", "https://github.com/jwstegemann/fritz2")
                    }
                }
            }
        }

        lineUp({
            alignItems { stretch }
            color { dark }
            minHeight { "100%" }
        }) {
            items {
                stackUp({
                    margins {
                        top { larger }
                    }
                    padding { "1rem" }
                    minWidth { "200px" }
                    minHeight { "100%" }
                    display { flex }
                    wrap { nowrap }
                    direction { column }
                    alignItems { flexStart }
                    background { color { base } }
                    color { dark }
                    paddings {
                        top { "50px" }
                    }
                }, id = "menu-left")
                {
                    spacing { tiny }
                    items {
                        (::p.styled {
                            width { "100%" }
                            margins { top { huge } }
                            paddings {
                                bottom { "1rem" }
                            }
                        }) {
                            menuAnchor(welcome_)
                            menuAnchor(gettingStarted_)
                        }
                        menuHeader { +"FEATURES" }
                        menuAnchor(styling_)
                        menuAnchor(theme_)
                        menuAnchor(responsive_)


                        menuHeader { +"LAYOUT" }
                        menuAnchor(flexbox_)
                        menuAnchor(gridbox_)
                        menuAnchor(stack_)

                        menuHeader { +"FORMS" }
                        menuAnchor(buttons_)
                        menuAnchor(checkboxes_)
                        menuAnchor(formcontrol_)
                        menuAnchor(input_)
                        menuAnchor(radios_)
                        menuAnchor(switch_)
                        menuAnchor(textarea_)

                        menuHeader { +"FEEDBACK" }
                        menuAnchor(spinner_)

                        menuHeader { +"OVERLAY" }
                        menuAnchor(modal_)
                        menuAnchor(popover_)
                        menuAnchor(tooltip_)

                        menuHeader { +"ICONS" }
                        menuAnchor(icons_)
                    }
                }
                (::div.styled(id = "content-right") {
                    paddings {
                        all { huge }
                    }
                    width {
                        "100%"
                    }
                }) {
                    className(welcomeContent.whenever(router.data) { it == welcome_ }.name)

                    // todo we might want a better flex demo
                    // todo we might want a dedicated theme demo (or use formcontrol (rename) --> all
                    //  together)
                    router.data.render { site ->
                        when (site) {
                            gettingStarted_ -> gettingStarted()
                            icons_ -> iconsDemo()
                            spinner_ -> spinnerDemo()
                            input_ -> inputDemo()
                            buttons_ -> buttonDemo()
                            formcontrol_ -> formControlDemo()
                            flexbox_ -> flexBoxDemo()
                            gridbox_ -> gridBoxDemo()
                            checkboxes_ -> checkboxesDemo()
                            radios_ -> radiosDemo()
                            switch_ -> switchDemo()
                            stack_ -> stackDemo()
                            modal_ -> modalDemo()
                            popover_ -> popoverDemo()
                            tooltip_ -> tooltipDemo()
                            welcome_ -> welcome()
                            styling_ -> stylingDemo()
                            theme_ -> themeDemo()
                            responsive_ -> responsiveDemo()
                            textarea_ -> textAreaDemo()
                            else -> welcome()
                        }
                    }
                }
            }
        }
    }.mount("target")
}
