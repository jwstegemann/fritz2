import dev.fritz2.components.box
import dev.fritz2.components.flexBox
import dev.fritz2.components.gridBox
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
    }
}