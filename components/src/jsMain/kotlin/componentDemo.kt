import dev.fritz2.components.*
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.HtmlElements
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
fun HtmlElements.componentDemo(theme: ExtendedTheme): Div {
    return div {
        box {
            hugo {
                flexBox {
                    hugo {
                        gridBox {
                            hugo {
                                +"Bin da"
                            }
                        }
                    }
                }
            }
        }

        h1 { +"Stacks"}
        stackUp {
            border {
                width { thin }
                color { dark }
                style { solid }
            }
            spacing { normal }
            hugo {
                lineUp {
                    border {
                        width { thin }
                        color { secondary }
                        style { solid }
                    }
                    width { "100%" }
                    alignItems { start }
                    spacing { huge }
                    reverse { true }
                    hugo {
                        p { +"A" }
                        p { +"B" }
                        p { +"C" }
                    }
                }
                lineUp {
                    border {
                        width { thin }
                        color { secondary }
                        style { solid }
                    }
                    width { "100%" }
                    alignItems { start }
                    spacing { huge }
                    hugo {
                        p { +"D" }
                        p { +"E" }
                        p { +"F" }
                    }
                }
            }
        }
    }
}