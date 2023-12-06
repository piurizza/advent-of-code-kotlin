package aockt.y2023

import io.github.jadarma.aockt.core.Solution

object Y2023D06 : Solution {
    override fun partOne(input: String): Int {
        val timeToDistance = parseMultipleRaces(input)

        return timeToDistance
            .keys
            .map { time ->
                (0..time)
                    .map { holdingButtonTime ->
                        val timeLeft = time - holdingButtonTime

                        if (holdingButtonTime * timeLeft > timeToDistance[time]!!) 1
                        else 0
                    }
                    .reduce { acc, singleRaceWon -> acc + singleRaceWon }
            }
            .reduce { acc, wonForEachRace -> acc * wonForEachRace }

    }

    override fun partTwo(input: String): Long {
        val timeToDistance = parseSingleRace(input)

        val winnerRange = searchMinWinner(
            0L..timeToDistance.first,
            timeToDistance.first,
            timeToDistance.second
        )
        val remainingRangeSize = winnerRange.first * 2 - 1

        return timeToDistance.first - remainingRangeSize
    }

    private fun parseMultipleRaces(input: String): Map<Int, Int> {
        val timeAndDistance = input
            .split("\n")
            .map { row ->
                val matches = Regex("\\d+").findAll(row)

                matches
                    .map { numberString ->
                        numberString.value.toInt()
                    }
                    .toList()
            }

        return timeAndDistance[0]
            .zip(timeAndDistance[1])
            .toMap()
    }

    private fun parseSingleRace(input: String): Pair<Long, Long> {
        val timeAndDistance = input
            .split("\n")
            .map { row ->
                val matches = Regex("\\d+").findAll(row)

                matches
                    .map { numberString ->
                        numberString.value
                    }
                    .reduce { acc, numberString -> acc + numberString }
                    .toLong()
            }

        return timeAndDistance[0] to timeAndDistance[1]
    }

    private tailrec fun searchMinWinner(range: LongRange, time: Long, distance: Long): LongRange {
        val timeLeft = time - range.first

        val minRangeWin = range.first * timeLeft > distance

        return if (minRangeWin) range
        else searchMinWinner(LongRange(range.first + 1, range.last - 1), time, distance)
    }
}

