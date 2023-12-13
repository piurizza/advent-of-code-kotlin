package aockt.y2023

import aockt.common.Geo.BigCoordinates2D
import io.github.jadarma.aockt.core.Solution
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

object Y2023D11 : Solution {
    override fun partOne(input: String): Long {
        val universe = parseUniverse(input)
        val galaxyCoordinates = universe.expand(2)

        return calculateShortestPathSum(galaxyCoordinates)
    }

    override fun partTwo(input: String): Long {
        val universe = parseUniverse(input)
        val galaxyCoordinates = universe.expand(1_000_000)

        return calculateShortestPathSum(galaxyCoordinates)
    }

    private fun parseUniverse(input: String): Universe {
        val grid: MutableList<MutableList<Char>> = input
            .split("\n")
            .map { string ->
                string
                    .trim()
                    .toList()
                    .toMutableList()
            }
            .toMutableList()

        return Universe(grid = grid)
    }

    private fun Universe.expand(expansionFactor: Int): MutableList<BigCoordinates2D> {
        val galaxyCoordinates =
            (0..this.maxY).flatMap { y ->
                (0..this.maxX).map { x ->
                    GridCell(
                        BigCoordinates2D(x.toLong(), y.toLong()),
                        this.grid[y][x]
                    )
                }
            }
                .filter { it.type == GALAXY }
                .map { it.coordinates }
                .toMutableList()

        var yOffset = 0L
        (0..this.maxY).forEach { y ->
            if (this.grid[y].all { it == SPACE }) {
                galaxyCoordinates
                    .filter { it.y > y + yOffset }
                    .forEach { it.y += expansionFactor - 1L }
                yOffset += expansionFactor - 1L
            }
        }

        var xOffset = 0L

        (0..this.maxX).forEach { x ->
            if ((0..this.maxY).all { y -> this.grid[y][x] == SPACE }) {
                galaxyCoordinates
                    .filter { it.x > x + xOffset }
                    .forEach { it.x += expansionFactor - 1L }
                xOffset += expansionFactor - 1L
            }
        }

        return galaxyCoordinates
    }

    private fun calculateShortestPathSum(galaxyCoordinates: MutableList<BigCoordinates2D>) =
        runBlocking {
            galaxyCoordinates
                .flatMapIndexed { currentIndex, currentGalaxyCoordinates ->
                    galaxyCoordinates
                        .filterIndexed { index, _ -> index > currentIndex }
                        .map { otherGalaxyCoordinates ->
                            async {
                                otherGalaxyCoordinates
                                    .orthogonalShortestPath(
                                        currentGalaxyCoordinates
                                    )
                            }
                        }
                }
                .awaitAll()
                .sumOf { it }
        }

    data class Universe(
        val grid: MutableList<MutableList<Char>> = emptyList<MutableList<Char>>().toMutableList()
    ) {
        val maxX = if (grid.isEmpty()) 0 else grid.first().size - 1
        val maxY = if (grid.isEmpty()) 0 else grid.size - 1
    }

    data class GridCell(
        val coordinates: BigCoordinates2D,
        val type: Char
    )

    private const val GALAXY = '#'
    private const val SPACE = '.'

}
