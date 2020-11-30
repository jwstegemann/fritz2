
import dev.fritz2.binding.RootStore
import dev.fritz2.components.stackUp
import dev.fritz2.components.switch
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
fun RenderContext.switchDemo(): Div {
    val checkedStore1 = RootStore(false)
    val checkedStore2 = RootStore(false)
    val checkedStore3 = RootStore(true)
    val checkedStore4 = RootStore(false)

    return contentFrame {
        stackUp({
            alignItems { start }
            padding { "1rem" }
        }) {
            spacing { large }
            items {
                h1 { +"Switch Showcase" }
                p {
                    +"A checkbox like a switch Button"
                }
                switch() {
                    label("Small size")
                    size { small }
                    checked { checkedStore1.data }
                    events {
                        changes.states() handledBy checkedStore1.update
                    }
                }
                switch(styling = {
                    background {
                        color { dark }
                    }
                }) {
                    label("Normal size, custom default(background) color")
                    checked { checkedStore2.data }
                    events {
                        changes.states() handledBy checkedStore2.update
                    }
                }
                switch() {
                    label("Large size, custom checked(background) color")
                    checked { checkedStore3.data }
                    size { large }
                    checkedStyle {
                        {
                            background {
                                color { warning }
                            }
                        }
                    }
                    events {
                        changes.states() handledBy checkedStore3.update
                    }
                }
                switch(styling = {
                    background {
                        color { dark }
                    }
                }) {
                    label("more customizing")
                    checked { checkedStore4.data }
                    dotStyle {
                        {
                            background {
                                color { danger }
                            }
                        }
                    }
                    events {
                        changes.states() handledBy checkedStore4.update
                    }
                }
            }
        }
    }
}
