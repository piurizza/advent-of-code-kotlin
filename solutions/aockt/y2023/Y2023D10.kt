package aockt.y2023

import io.github.jadarma.aockt.core.Solution
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory

object Y2023D10 : Solution {
    override fun partOne(input: String): Int {
        val maze = parseInput(input)

        return createTilesLoop(maze)
            .size
            .div(2)
    }

    override fun partTwo(input: String): (Int) {
        val maze = parseInput(input)
        val tilesLoopCoordinates = createTilesLoop(maze)
            .map { tile ->
                Coordinate(
                    tile.coordinates.x.toDouble(),
                    tile.coordinates.y.toDouble()
                )
            }
            .toTypedArray()

        val geometryFactory = GeometryFactory()

        val polygon = geometryFactory.createPolygon(
            tilesLoopCoordinates
                + tilesLoopCoordinates.first()
        )

        val enclosedArea = (0..maze.maxY)
            .map { y ->
                (0..maze.maxX)
                    .filter { x ->
                        Coordinate(x.toDouble(), y.toDouble()) !in tilesLoopCoordinates
                    }
                    .count { x ->
                        polygon.contains(
                            GeometryFactory()
                                .createPoint(Coordinate(x.toDouble(), y.toDouble()))
                        )
                    }
            }
            .sumOf { it }

        return enclosedArea
    }

    private fun createTilesLoop(maze: Maze): MutableList<Tile> {

        var currentTile = maze.start.move(maze)
        val tilesLoop = mutableListOf(currentTile)

        while (!currentTile.isSameTile(maze.start)) {
            val nextTile = currentTile
                .move(maze)

            tilesLoop.add(nextTile)

            currentTile = nextTile
        }

        return tilesLoop
    }

    private fun parseInput(input: String): Maze {
        lateinit var startTile: Tile

        val grid = input
            .split("\n")
            .mapIndexed { index, row ->
                val splittedRow = row
                    .toCharArray()

                if (splittedRow.contains('S')) {
                    startTile = Tile(
                        coordinates = Coordinates(
                            x = splittedRow.indexOf('S'),
                            y = index
                        ),
                        type = TileType.START,
                        lastMove = MoveType.NONE
                    )
                }

                splittedRow
            }

        return Maze(
            grid = grid,
            start = startTile,
            maxY = grid.size - 1,
            maxX = grid.first().size - 1
        )
    }

    private fun Tile.move(maze: Maze): Tile {

        val leftX = if (this.coordinates.x != 0) this.coordinates.x - 1 else null
        val rightX = if (this.coordinates.x != maze.maxX) this.coordinates.x + 1 else null
        val upY = if (this.coordinates.y != 0) this.coordinates.y - 1 else null
        val downY = if (this.coordinates.y != maze.maxY) this.coordinates.y + 1 else null

        val leftTile = leftX?.let {
            this.copy(
                coordinates = this.coordinates.copy(x = it),
                type = lookupTileType(maze.grid[this.coordinates.y][it]),
                lastMove = MoveType.LEFT
            )
        }

        val rightTile = rightX?.let {
            this.copy(
                coordinates = this.coordinates.copy(x = it),
                type = lookupTileType(maze.grid[this.coordinates.y][it]),
                lastMove = MoveType.RIGHT

            )
        }

        val upTile = upY?.let {
            this.copy(
                coordinates = this.coordinates.copy(y = it),
                type = lookupTileType(maze.grid[it][this.coordinates.x]),
                lastMove = MoveType.UP
            )
        }

        val downTile = downY?.let {
            this.copy(
                coordinates = this.coordinates.copy(y = it),
                type = lookupTileType(maze.grid[it][this.coordinates.x]),
                lastMove = MoveType.DOWN
            )
        }

        val choice =
            when (this.lastMove) {
                MoveType.LEFT -> Choice(this, leftTile, upTile, null, downTile)
                MoveType.RIGHT -> Choice(this, null, upTile, rightTile, downTile)
                MoveType.UP -> Choice(this, leftTile, upTile, rightTile, null)
                MoveType.DOWN -> Choice(this, leftTile, null, rightTile, downTile)
                else -> Choice(this, leftTile, upTile, rightTile, downTile)
            }
                .filterOutInvalidMoves()

        return listOfNotNull(
            choice.left,
            choice.up,
            choice.right,
            choice.down
        )
            .first()

    }

    private fun lookupTileType(c: Char): TileType {
        return when (c) {
            '|' -> TileType.NS
            '-' -> TileType.EW
            'L' -> TileType.NE
            'J' -> TileType.NW
            '7' -> TileType.SW
            'F' -> TileType.SE
            'S' -> TileType.START
            else -> TileType.GROUND
        }
    }

    private fun Choice.filterOutInvalidMoves(): Choice {
        val validLeftTileTypes = setOf(
            TileType.START,
            TileType.EW,
            TileType.NE,
            TileType.SE
        )

        val validRightTileTypes = setOf(
            TileType.START,
            TileType.EW,
            TileType.NW,
            TileType.SW
        )

        val validUpTileTypes = setOf(
            TileType.START,
            TileType.NS,
            TileType.SW,
            TileType.SE
        )

        val validDownTileTypes = setOf(
            TileType.START,
            TileType.NS,
            TileType.NW,
            TileType.NE
        )

        if (origin == null)
            throw IllegalStateException("Origin cannot be null")

        return this.copy(
            left = this.left?.let {
                if (
                    it.type in validLeftTileTypes &&
                    (origin.type in validRightTileTypes)
                ) this.left else null
            },
            up = this.up?.let {
                if (
                    it.type in validUpTileTypes &&
                    (origin.type in validDownTileTypes)
                ) this.up else null
            },
            right = this.right?.let {
                if (
                    it.type in validRightTileTypes &&
                    (origin.type in validLeftTileTypes)
                ) this.right else null
            },
            down = this.down?.let {
                if (
                    it.type in validDownTileTypes &&
                    (origin.type in validUpTileTypes)
                ) this.down else null
            }
        )
    }

    data class Coordinates(
        val x: Int,
        val y: Int
    )

    data class Tile(
        val coordinates: Coordinates,
        val type: TileType,
        val lastMove: MoveType
    ) {
        fun isSameTile(other: Tile): Boolean =
            this.coordinates == other.coordinates
    }

    data class Choice(
        val origin: Tile?,
        val left: Tile?,
        val up: Tile?,
        val right: Tile?,
        val down: Tile?
    )

    data class Maze(
        val grid: List<CharArray>,
        val start: Tile,
        val maxY: Int,
        val maxX: Int
    )

    enum class TileType {
        NS, EW, NE, NW, SW, SE, START, GROUND
    }

    enum class MoveType {
        LEFT, RIGHT, UP, DOWN, NONE
    }

}