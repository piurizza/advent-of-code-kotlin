package aockt.common

import kotlin.math.abs

object Geo {
    data class Coordinates2D(
        var x: Int,
        var y: Int
    ) {
        fun plus(other: Coordinates2D): Coordinates2D {
            return Coordinates2D(this.x + other.x, this.y + other.y)
        }

        companion object {
            fun getNeighbor(origin: Coordinates2D, direction: Direction, radius: Int): Neighbor {
                val neighborCoordinates = when (direction) {
                    Direction.UP -> origin.plus(Coordinates2D(0, -radius))
                    Direction.DOWN -> origin.plus(Coordinates2D(0, radius))
                    Direction.LEFT -> origin.plus(Coordinates2D(-radius, 0))
                    Direction.RIGHT -> origin.plus(Coordinates2D(radius, 0))
                    Direction.LEFT_UP -> origin.plus(Coordinates2D(-radius, -radius))
                    Direction.LEFT_DOWN -> origin.plus(Coordinates2D(-radius, radius))
                    Direction.RIGHT_UP -> origin.plus(Coordinates2D(radius, -radius))
                    Direction.RIGHT_DOWN -> origin.plus(Coordinates2D(radius, radius))
                }

                return Neighbor(
                    origin = origin,
                    direction = direction,
                    coordinates = neighborCoordinates,
                    radius = radius
                )
            }

            fun getNeighborhood(origin: Coordinates2D, radius: Int): List<Neighbor> {
                return Direction.entries.map {
                    getNeighbor(
                        origin = origin,
                        direction = it,
                        radius = radius
                    )
                }

            }
        }

        data class Neighbor(
            val origin: Coordinates2D,
            val coordinates: Coordinates2D,
            val direction: Direction,
            val radius: Int
        )
    }

    data class BigCoordinates2D(
        var x: Long,
        var y: Long
    ) {
        fun orthogonalShortestPath(other: BigCoordinates2D) =
            abs(this.x - other.x) +
                abs(this.y - other.y)
    }

    enum class Direction {
        UP, DOWN, LEFT, RIGHT, LEFT_DOWN, RIGHT_DOWN, LEFT_UP, RIGHT_UP
    }
}