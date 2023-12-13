package aockt.common

import kotlin.math.abs

object Geo {
    data class Coordinates2D(
        var x: Int,
        var y: Int
    )

    data class BigCoordinates2D(
        var x: Long,
        var y: Long
    ) {
        fun orthogonalShortestPath(other: BigCoordinates2D) =
            abs(this.x - other.x) +
                abs(this.y - other.y)
    }
}