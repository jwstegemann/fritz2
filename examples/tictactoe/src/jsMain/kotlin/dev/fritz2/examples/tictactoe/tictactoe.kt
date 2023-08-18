package dev.fritz2.examples.tictactoe

import dev.fritz2.core.RenderContext
import dev.fritz2.core.RootStore
import dev.fritz2.core.render
import dev.fritz2.core.viewBox
import kotlinx.coroutines.flow.map

class GameStore(private val engine: Engine, initialState: GameState) : RootStore<GameState>(initialState) {

    val field = data.map { it.field }

    val check = handle<Cell> { state, move ->
        engine.next(state, move)
    }
}

fun RenderContext.circleIcon() {
    svg("w-100") {
        viewBox("-3 -3 30 30")
        content(
            """<path fill-rule="evenodd" clip-rule="evenodd" fill="currentColor"
             d="M12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20C16.4183 20 20 
             16.4183 20 12C20 7.58172 16.4183 4 12 4ZM2 12C2 6.47715 6.47715 2 12 2C17.5228 
             2 22 6.47715 22 12C22 17.5228 17.5228 22 12 22C6.47715 22 2 17.5228 2 12Z"/>
            """.trimIndent(),
        )
    }
}

fun RenderContext.xIcon() {
    svg("w-100") {
        viewBox("0 0 24 24")
        content(
            """<path fill-rule="evenodd" clip-rule="evenodd" fill="currentColor"
             d="M5.29289 5.29289C5.68342 4.90237 6.31658 4.90237 6.70711 5.29289L12 10.5858L17.2929
             5.29289C17.6834 4.90237 18.3166 4.90237 18.7071 5.29289C19.0976 5.68342 19.0976 6.31658
             18.7071 6.70711L13.4142 12L18.7071 17.2929C19.0976 17.6834 19.0976 18.3166 18.7071 18.7071C18.3166
             19.0976 17.6834 19.0976 17.2929 18.7071L12 13.4142L6.70711 18.7071C6.31658 19.0976 5.68342 19.0976
             5.29289 18.7071C4.90237 18.3166 4.90237 17.6834 5.29289 17.2929L10.5858 12L5.29289 6.70711C4.90237
             6.31658 4.90237 5.68342 5.29289 5.29289Z"/>
            """.trimIndent(),
        )
    }
}

fun main() {
    val gameStore = GameStore(Engine(), GameState())

    render("#target") {
        div("d-flex justify-content-center w-100") {
            div("d-flex flex-column") {
                inlineStyle("width: 30.5rem;")
                gameStore.data.render {
                    if (it.hasEnded()) {
                        val message = it.messages.first()
                        div("alert") {
                            +message.text
                            className(message.type)
                            attr("role", "alert")
                        }
                    }
                }
                div("row") {
                    gameStore.field.renderEach { cell ->
                        div("col-4 border d-flex justify-content-center align-items-center") {
                            inlineStyle("height: 10rem; background-color: ${if (cell.isInWinningGroup) "green" else "white"};")
                            if (cell.symbol.isNotBlank()) {
                                if (cell.symbol == "X") xIcon() else circleIcon()
                            }
                            clicks.map {
                                cell.copy(symbol = gameStore.current.player)
                            } handledBy gameStore.check
                        }
                    }
                }
                button("btn btn-secondary mt-4") {
                    +"Reset"
                    clicks.map { GameState(player = gameStore.current.player) } handledBy gameStore.update
                }
            }
        }
    }
}
