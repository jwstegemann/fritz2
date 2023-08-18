package dev.fritz2.examples.tictactoe

import dev.fritz2.validation.Validation
import dev.fritz2.validation.ValidationMessage
import dev.fritz2.validation.validation

class GameEndMessage(override val path: String, val text: String, val type: String) : ValidationMessage {
    override val isError: Boolean = false
}

class Engine {

    companion object {
        private val endingValidator: Validation<Field, GameState, GameEndMessage> = validation { inspector, gameState ->
            if (inspector.data.any { it.isInWinningGroup }) {
                add(GameEndMessage(inspector.path, "Player ${gameState.player} has won!", "alert-success"))
            } else if (GameState.isFull(inspector.data)) {
                add(GameEndMessage(inspector.path, "This is a draw!", "alert-info"))
            }
        }

        private val winningGroups = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6),
        )

        private fun markWinningCells(state: GameState, field: Field): Field {
            val winningFields = winningGroups.filter { group ->
                group.map { field[it] }.all { it.symbol == state.player }
            }.flatten()
            return field.map { cell ->
                if (winningFields.contains(cell.id)) {
                    cell.copy(isInWinningGroup = true)
                } else {
                    cell
                }
            }
        }
    }

    fun next(state: GameState, move: Cell) =
        if (!state.hasEnded() && state.field[move.id].isBlank) {
            val newField = markWinningCells(state, state.field.map { if (it.id == move.id) move else it })
            val messages = endingValidator(newField, state)
            state.copy(field = newField, player = state.nextPlayer(), messages = messages)
        } else {
            state
        }
}
