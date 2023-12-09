package aockt.y2023

import io.github.jadarma.aockt.core.Solution

object Y2023D09 : Solution {
    override fun partOne(input: String): Long {
        val histories = parseInput(input)

        return histories
            .map { predictValue(it, PredictionType.NEXT) }
            .sumOf { it }
    }

    override fun partTwo(input: String): Long {
        val histories = parseInput(input)

        return histories
            .map { predictValue(it, PredictionType.PREVIOUS) }
            .sumOf { it }
    }

    private fun parseInput(input: String) =
        input
            .split("\n")
            .map { row ->
                row.split(" ")
            }
            .map { history -> history.map { it.toLong() } }

    private fun predictValue(
        history: List<Long>,
        predictionType: PredictionType
    ): Long {
        val totalSequences = mutableListOf(history.toMutableList())
        var step = 0

        while (true) {
            val sequenceOfDifferences = emptyList<Long>().toMutableList()
            val currentSequence = totalSequences[step]
            val lastIndex = currentSequence.size - 1

            for (index in 0 until lastIndex) {
                sequenceOfDifferences.add(
                    currentSequence[index + 1] - currentSequence[index]
                )
            }

            totalSequences.add(sequenceOfDifferences)
            step++

            if (sequenceOfDifferences.all { it == 0L })
                return when (predictionType) {
                    PredictionType.NEXT -> totalSequences
                        .reversed()
                        .map { it.last() }
                        .sumOf { it }

                    PredictionType.PREVIOUS -> totalSequences
                        .reversed()
                        .map { it.first() }
                        .reduce { acc, value -> -acc + value }
                }
        }

    }

    enum class PredictionType { PREVIOUS, NEXT }
}