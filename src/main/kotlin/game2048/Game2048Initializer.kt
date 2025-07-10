package org.example.game2048

import org.example.board.Cell
import org.example.board.GameBoard
import kotlin.random.Random

interface Game2048Initializer<T> {

    fun nextValue(board: GameBoard<T?>): Pair<Cell, T>?
}

object RandomGame2048Initializer: Game2048Initializer<Int> {
    private fun generateRandomStartValue(): Int =
        if (Random.nextInt(10) == 9) 4 else 2


    override fun nextValue(board: GameBoard<Int?>): Pair<Cell, Int>? {
        val emptyCells = board.filter { element-> element == null }
        if(emptyCells.isEmpty()) return null
        val randomCell = emptyCells.random()
        return Pair(randomCell, generateRandomStartValue())
    }
}