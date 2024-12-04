package aockt.y2024

import aockt.common.Geo.Coordinates2D
import aockt.common.Geo.Coordinates2D.Companion.getNeighbor
import aockt.common.Geo.Coordinates2D.Companion.getNeighborhood
import aockt.common.Geo.Direction
import io.github.jadarma.aockt.core.Solution

object Y2024D04 : Solution {
    override fun partOne(input: String): Any {
        val matrix = parseInput(input)

        return matrix.mapIndexed { colIndex, col ->
            col.mapIndexed { rowIndex, row ->
                if (row == 'X') countNeighborhoodXmas(matrix, rowIndex, colIndex)
                else 0
            }
                .reduce(Int::plus)
        }
            .reduce(Int::plus)
    }

    override fun partTwo(input: String): Any {
        val matrix = parseInput(input)

        return matrix.mapIndexed { colIndex, col ->
            col.mapIndexed { rowIndex, row ->
                if (row == 'A' && isXmasCrossValid(matrix, rowIndex, colIndex)) 1
                else 0
            }
                .reduce(Int::plus)
        }
            .reduce(Int::plus)
    }

    private fun parseInput(input: String): List<CharArray> {
        return input.split('\n').map { it.toCharArray() }
    }

    private fun countNeighborhoodXmas(matrix: List<CharArray>, rowIndex: Int, colIndex: Int): Int {
        val xCoordinates = Coordinates2D(rowIndex, colIndex)
        val neighborhood = getNeighborhood(xCoordinates, radius = 1)

        val candidates: List<Candidate> = neighborhood.mapNotNull { neighbor ->
            if (!checkMatrix(matrix, neighbor.coordinates, 'M')) null
            else Candidate(neighbor.coordinates, neighbor.direction)
        }

        return candidates.count { candidate ->
            listOf('A', 'S')
                .forEachIndexed { index, char ->
                    val coordinatesToCheck = getNeighbor(candidate.coordinate, candidate.direction, index + 1)
                        .coordinates

                    if (!checkMatrix(matrix, coordinatesToCheck, char))
                        return@count false
                }
            true
        }
    }

    private fun isXmasCrossValid(
        matrix: List<CharArray>,
        rowIndex: Int,
        colIndex: Int
    ): Boolean {
        val origin = Coordinates2D(rowIndex, colIndex)
        val diagonalNeighborhood = listOf(
            listOf(
                getNeighbor(origin, Direction.LEFT_DOWN, radius = 1).coordinates,
                getNeighbor(origin, Direction.RIGHT_UP, radius = 1).coordinates
            ),
            listOf(
                getNeighbor(origin, Direction.LEFT_UP, radius = 1).coordinates,
                getNeighbor(origin, Direction.RIGHT_DOWN, radius = 1).coordinates
            )
        )

        return diagonalNeighborhood.all { checkMatrix(matrix, it, listOf('M', 'S')) }
    }

    private fun checkMatrix(matrix: List<CharArray>, coordinates: Coordinates2D, char: Char): Boolean {
        if (coordinates.y !in matrix.indices) return false
        if (coordinates.x !in (0 until matrix.first().size)) return false
        return matrix[coordinates.y][coordinates.x] == char
    }

    private fun checkMatrix(matrix: List<CharArray>, coordinates: List<Coordinates2D>, chars: List<Char>): Boolean {
        val charsToFind = chars.toMutableList()

        coordinates.forEach {
            if (it.y !in matrix.indices) return false
            if (it.x !in (0 until matrix.first().size)) return false
            val matrixChar = matrix[it.y][it.x]
            if (matrixChar !in charsToFind) return false
            charsToFind.remove(matrixChar)
        }

        return true
    }

    data class Candidate(
        val coordinate: Coordinates2D,
        val direction: Direction
    )
}