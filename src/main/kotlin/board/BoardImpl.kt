package org.example.board
import org.example.board.Direction

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

open class SquareBoardImpl(override val width: Int) : SquareBoard {
    val cells: List<List<Cell>>

    init {
        cells = List(width) { i ->
            List(width) { j ->
                Cell(i + 1, j + 1)
            }
        }
    }


    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return cells.flatten().firstOrNull { it.i == i && it.j == j }

    }

    override fun getCell(i: Int, j: Int): Cell {
        return getCellOrNull(i, j) ?: throw IllegalStateException("Cell doesn't exist.")
    }

    override fun getAllCells(): Collection<Cell> {
        return cells.flatten()
    }


    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val result = mutableListOf<Cell>()
        jRange.forEach { j ->
            val cell = getCellOrNull(i, j)
            cell?.let {
                result.add(it)
            }

        }
        return result
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val result = mutableListOf<Cell>()
        for (i in iRange) {
            val cell = getCellOrNull(i, j)
            if (cell != null) {
                result.add(cell)
            }
        }
        return result
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? =
        when (direction) {
            Direction.UP -> getCellOrNull(i - 1, j)
            Direction.DOWN -> getCellOrNull(i + 1, j)
            Direction.RIGHT -> getCellOrNull(i, j + 1)
            Direction.LEFT -> getCellOrNull(i, j - 1)
        }


}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {
    private val values: List<MutableList<T?>> = List(width) { MutableList(width) { null } }

    override fun get(cell: Cell): T? {
        return values[cell.i - 1][cell.j - 1]
    }

    override fun set(cell: Cell, value: T?) {
        if (cell.i !in 1..width || cell.j !in 1..width) {
            throw IllegalArgumentException("Cell ($cell) is out of bounds.")
        }
        values[cell.i - 1][cell.j - 1] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        val result = mutableListOf<Cell>()
        for (i in 1..width) {
            for (j in 1..width) {
                val value = values[i - 1][j - 1]
                if (predicate(value)) {
                    result.add(getCell(i, j))
                }
            }
        }
        return result
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        for (i in 1..width) {
            for (j in 1..width) {
                val value = values[i - 1][j - 1]
                if (predicate(value)) {
                    return getCell(i, j)
                }
            }
        }
        return null
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return values.flatten().any(predicate)
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return values.flatten().all(predicate)
    }
}

