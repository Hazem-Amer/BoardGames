package org.example.game2048

import org.example.board.Cell
import org.example.board.Direction
import org.example.board.GameBoard
import org.example.board.createGameBoard
import org.example.game.Game


fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
    Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}


fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    val randomCellValue = initializer.nextValue(this)
    randomCellValue?.let { it ->
        this[it.first] = it.second
    }

}


fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
    val originalValues: List<Int?> = rowOrColumn.map { cell -> this.get(cell) }
    val valuesOfMovedCells = originalValues.moveAndMergeEqual { it * 2 }
    val valuesOfMovedCellsWithNulls = valuesOfMovedCells + List(rowOrColumn.size - valuesOfMovedCells.size, { null })
    val movedCellValuePairs = rowOrColumn.zip(valuesOfMovedCellsWithNulls)
    if (originalValues != valuesOfMovedCellsWithNulls) {
        for (cellValuePair in movedCellValuePairs) {
            this.set(cellValuePair.first, cellValuePair.second)
        }
        return true
    }
    return false
}


fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    when (direction) {
        Direction.UP -> {
            val cols = getRowsOrColumns(this, { index, range ->
                getColumn(range, index)
            })
            return moveValuesAndUpdate(this, cols)
        }

        Direction.DOWN -> {
            val cols = getRowsOrColumns(this, { index, range ->
                getColumn(range.reversed(), index)
            })
            return moveValuesAndUpdate(this, cols)
        }

        Direction.RIGHT -> {
            val rows = getRowsOrColumns(this, { index, range ->
                getRow(index, range.reversed())
            })
            return moveValuesAndUpdate(this, rows)
        }

        Direction.LEFT -> {
            val rows = getRowsOrColumns(this, { index, range ->
                getRow(index, range)
            })
            return moveValuesAndUpdate(this, rows)
        }

    }

}

private fun getRowsOrColumns(board: GameBoard<Int?>, getOperation: (Int, IntRange) -> List<Cell>): List<List<Cell>> {
    val result: MutableList<List<Cell>> = mutableListOf()
    for (i in 1..board.width) {
        result.add(getOperation(i, 1..board.width))
    }
    return result
}

private fun moveValuesAndUpdate(board: GameBoard<Int?>, rowsOrCols: List<List<Cell>>): Boolean {
    var movesCounter = 0
    for (rowOrCol in rowsOrCols) {
        if (board.moveValuesInRowOrColumn(rowOrCol))
            movesCounter++
    }
    return movesCounter > 0
}
