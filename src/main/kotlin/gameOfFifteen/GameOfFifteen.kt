package org.example.gameOfFifteen

import org.example.board.Cell
import org.example.board.Direction
import org.example.board.GameBoard
import org.example.board.GameBoardImpl
import org.example.game.Game


fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game {
    return GameOfFifteen(initializer)
}

class GameOfFifteen(val initializer: GameOfFifteenInitializer) : Game {
    val board: GameBoard<Int?> = GameBoardImpl(4)
    override fun initialize() {
        val allCells = board.getAllCells()
        allCells.forEachIndexed { index, cell ->
            var currentElement: Int? = null
            if (index <= 14)
                currentElement = this.initializer.initialPermutation.get(index)

            board.set(cell, currentElement)
        }
    }

    override fun canMove(): Boolean {
        return !permutationIsOrdered()
    }

    override fun hasWon(): Boolean {
        return permutationIsOrdered()
    }

    override fun processMove(direction: Direction) {
        moveValues(direction)
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }


    private fun permutationIsOrdered(): Boolean {
        return this.initializer.initialPermutation == (1..15).toList()

    }

    private fun moveValues(direction: Direction) {
        when (direction) {
            Direction.UP -> {
                return swapValueWIthNullCell(direction)
            }

            Direction.DOWN -> {
                return swapValueWIthNullCell(direction)
            }

            Direction.RIGHT -> {
                return swapValueWIthNullCell(direction)
            }

            Direction.LEFT -> {
                return swapValueWIthNullCell(direction)
            }
        }

    }

    private fun swapValueWIthNullCell(direction: Direction) {
        val nullCell = getNullCell()
        val movableCell = board.getMovableCell(direction, nullCell)
        if (movableCell == null) return
        else {

            board.set(nullCell, board.get(movableCell))
            board.set(movableCell, null)
        }
    }

    private fun getNullCell(): Cell {
        val nullCell = board.find { cellValue -> cellValue == null }
        return if (nullCell != null) nullCell else throw IllegalStateException("There is no empty cell")
    }


}


fun GameBoard<Int?>.getMovableCell(direction: Direction, nullCell: Cell): Cell? {
    return nullCell.getNeighbour(direction.reversed())
}

