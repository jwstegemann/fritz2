package dev.fritz2.components

import dev.fritz2.binding.RootStore
import dev.fritz2.dom.Window
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.styling.StyleClass
import dev.fritz2.styling.div
import dev.fritz2.styling.params.BasicParams
import dev.fritz2.styling.params.BoxParams
import dev.fritz2.styling.span
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.flow.map
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent

data class Progress<T>(val percent: Int, val value: T, val unscaled: Int = percent)

class SliderStore<T>(initialData: Progress<T>) : RootStore<Progress<T>>(initialData) {
    val registerMouseDown = handle<(Event) -> Unit> { v, action ->
        console.log("Listener 'move' registriert")
        window.addEventListener("mousemove", action)
        v
    }

    val unregisterMouseDown = handle<(Event) -> Unit> { v, action ->
        console.log("Listener 'move' abgemeldet")
        window.removeEventListener("mousemove", action)
        v
    }
}

enum class SliderDirection {
    UP, DOWN
}

interface SlidingStrategy<T> {
    fun nextValue(percent: Int, direction: SliderDirection): Progress<T>
}

/**
 * @param snap distance in percent to next scaled value.
 *             snap darf nur gesetzt werden, wenn scaleMax / step < 100 (oder gar 50?) sind
 */
class ScaledSlidingStrategy(
    private val scaleLimit: Int = 100,
    private val step: Int = 1,
    private val snap: Int = 0
) : SlidingStrategy<Int> {
    override fun nextValue(percent: Int, direction: SliderDirection): Progress<Int> {
        val realSnap = scaleLimit / 100 * snap
        val stepsDone = when (direction) {
            SliderDirection.UP -> ((scaleLimit * percent / 100) + realSnap) / step
            SliderDirection.DOWN -> ((scaleLimit * percent / 100) + (step - realSnap - 1)) / step
        }
        val scaledResult = maxOf(0, minOf(step * stepsDone, scaleLimit))
        val finalPercentage = minOf(scaledResult * 100 / scaleLimit, 100)
        return Progress(finalPercentage, scaledResult, percent)
    }
}

/**
 * TODO: snap weiterreichen
 */
class DiscreteSlidingStrategy<T>(private val items: List<T>) : SlidingStrategy<T> {
    override fun nextValue(percent: Int, direction: SliderDirection): Progress<T> {
        val scaledStrategy = ScaledSlidingStrategy(100 * (items.size - 1), 100, 5)
        val scaledResult = scaledStrategy.nextValue(percent, direction)
        return Progress(scaledResult.percent, items[scaledResult.value / 100], scaledResult.unscaled)
    }
}

open class SliderComponent<T>(
    protected val initialData: Progress<T>,
    protected val strategy: SlidingStrategy<T>
) :
    Component<Unit>,
    FormProperties by FormMixin() {

    fun move(store: SliderStore<T>): (Event) -> Unit = { event ->
        // TODO: ID muss übergeben werden!
        val containerRect = document.getElementById("dragcontainer")?.getBoundingClientRect()
        if (containerRect != null) {
            val offset = containerRect.left.toInt()
            val x = (event as MouseEvent).clientX
            val positionAbsolute =
                maxOf(0, minOf(x, containerRect.right.toInt()) - offset)
            val percentage = positionAbsolute * 101 / containerRect.width.toInt()

            val finalResult = if (percentage == store.current.unscaled) {
                store.current
            } else {
                strategy.nextValue(
                    percentage,
                    if (percentage > store.current.unscaled) SliderDirection.UP else SliderDirection.DOWN
                )
            }

            store.update(finalResult)
        }
    }

    override fun render(
        context: RenderContext,
        styling: BoxParams.() -> Unit,
        baseClass: StyleClass,
        id: String?,
        prefix: String
    ) {
        val store = SliderStore(initialData)
        val move = this@SliderComponent.move(store)

        context.apply {

            Window.mouseups.events.map { move } handledBy store.unregisterMouseDown

            div({
                border {
                    width { thin }
                    style { solid }
                    color { neutral.mainContrast }
                }
            }) {
                flexBox({
                    //width { full }
                    height { "2rem" }
                    background { color { gray100 } }
                    margins {
                        left { "1rem" }
                        right { "1rem" }
                    } // Radius
                    position { relative { } }
                }, id = "dragcontainer") {
                    store.data.render { x ->
                        div({
                            position {
                                relative {
                                    left { "calc(${x.percent}% - 1rem)" }
                                }
                            }
                            background { color { primary.main } }
                            zIndex { "100" }
                            width { "2rem" }
                            height { full }
                            radius { "1rem" }
                            display { flex }
                            justifyContent { center }
                            alignItems { center }
                        }, id = "drag") {
                            icon({
                                color { primary.mainContrast }
                                size { auto }
                            }) { fromTheme { clock } }
                            mousedowns.events.map { move } handledBy store.registerMouseDown
                        }
                        div({
                            height { "10px" }
                            radius { "5px" }
                            background { color { gray300 } }
                            position { absolute { top { "50%" } } }
                            css("transform: translateY(-50%)")
                            width { full }
                        }) {
                        }
                        div({
                            height { "10px" }
                            radius { "5px" }
                            background { color { "red" } }
                            position { absolute { top { "50%" } } }
                            css("transform: translateY(-50%)")
                            width { "calc(${x.percent}%)" }
                        }) {
                        }
                    }
                }
                lineUp {
                    items {
                        store.data.render {
                            span({
                                width { "6rem" }
                            }) { +"Value: $it" }
                        }
                    }
                }
            }
        }
    }
}

/**
 * This component generates a slider.
 *
 * @see SliderComponent
 *
 * @param styling a lambda expression for declaring the styling as fritz2's styling DSL
 * @param baseClass optional CSS class that should be applied to the element
 * @param id the ID of the element
 * @param prefix the prefix for the generated CSS class resulting in the form ``$prefix-$hash``
 * @param build a lambda expression for setting up the component itself. Details in [SliderComponent]
 */
fun RenderContext.slider(
    styling: BasicParams.() -> Unit = {},
    value: Int, // initialen Wert  setzen
    percent: Int = 0, // optional Ausgangswert setzen
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "slider",
    build: SliderComponent<Int>.() -> Unit = {}
) {
    // TODO params für ScaledSlidingStrategy in build verfügbar machen und dann ggf.
    //  lazy initialisieren / überschreiben
    //  -> bei Hauke abgucken, wie der dynamisch Sub-Kontexte ermöglicht
    // Hier ist der Typ T schon auf Int -> evtl. darüber gezielt Kontexte injecten?
    // + analog für andere Variante (diskrete Mengen)
    // + Lösung für custom Strategy finden!
    SliderComponent(Progress(percent, value), ScaledSlidingStrategy()).apply(build)
        .render(this, styling, baseClass, id, prefix)
}

fun <T> RenderContext.slider(
    styling: BasicParams.() -> Unit = {},
    value: List<T>, // initialen Wert  setzen
    percent: Int = 0, // optional Ausgangswert setzen
    baseClass: StyleClass = StyleClass.None,
    id: String? = null,
    prefix: String = "slider",
    build: SliderComponent<T>.() -> Unit = {}
) {
    SliderComponent(Progress(percent, value.first()), DiscreteSlidingStrategy(value)).apply(build)
        .render(this, styling, baseClass, id, prefix)
}
