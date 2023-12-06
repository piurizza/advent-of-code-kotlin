package aockt.y2023

import io.github.jadarma.aockt.core.Solution

object Y2023D03 : Solution {
    override fun partOne(input: String): Int {
        val parsedInput = parseInput(input)

        return parsedInput.indices
            .map { currentIndex ->
                val currentRow = parsedInput[currentIndex]
                val previousRow = parsedInput[(currentIndex - 1).coerceAtLeast(0)]
                val nextRow = parsedInput[(currentIndex + 1).coerceAtMost(parsedInput.size - 1)]

                val partNumberCandidates = findPartNumberCandidates(currentRow)

                if (partNumberCandidates.none()) return@map 0

                partNumberCandidates.map { partNumberCandidate ->
                    val adjacentRange = IntRange(
                        (partNumberCandidate.range.first - 1).coerceAtLeast(0),
                        (partNumberCandidate.range.last + 1).coerceAtMost(currentRow.length - 1)
                    )

                    val adjacentSymbolInPrevious =
                        if (currentRow != previousRow) adiacentSymbolAvailable(previousRow, adjacentRange)
                        else false

                    val adjacentSymbolInNext =
                        if (currentRow != nextRow) adiacentSymbolAvailable(nextRow, adjacentRange)
                        else false

                    if (
                        adiacentSymbolAvailable(currentRow, adjacentRange) ||
                        adjacentSymbolInPrevious ||
                        adjacentSymbolInNext
                    )
                        partNumberCandidate.value.toInt()
                    else 0
                }
                    .reduce { acc, partNumber -> acc + partNumber }
            }
            .reduce { acc, rowPartNumberSum -> acc + rowPartNumberSum }
    }

    override fun partTwo(input: String): Int {
        val parsedInput = parseInput(input)

        return parsedInput.indices
            .map { currentIndex ->
                val currentRow = parsedInput[currentIndex]

                if (!currentRow.contains("*")) return@map 0

                val previousRow = parsedInput[(currentIndex - 1).coerceAtLeast(0)]
                val nextRow = parsedInput[(currentIndex + 1).coerceAtMost(parsedInput.size - 1)]

                val partNumberCandidatesInPrevious =
                    if (currentRow != previousRow) findPartNumberCandidates(previousRow)
                    else emptySequence()

                val partNumberCandidatesInNext =
                    if (currentRow != nextRow) findPartNumberCandidates(nextRow)
                    else emptySequence()

                val partNumberCandidates = findPartNumberCandidates(currentRow)
                    .plus(partNumberCandidatesInPrevious)
                    .plus(partNumberCandidatesInNext)

                if (partNumberCandidates.none()) return@map 0

                findGearCandidates(currentRow)
                    .map { gearCandidate ->
                        val adjacentRange = IntRange(
                            (gearCandidate.range.first - 1).coerceAtLeast(0),
                            (gearCandidate.range.last + 1).coerceAtMost(currentRow.length - 1)
                        )

                        val adjacentPartNumbers = partNumberCandidates
                            .filter { partNumberCandidate ->
                                adjacentRange
                                    .intersect(
                                        partNumberCandidate.range
                                    )
                                    .isNotEmpty()
                            }

                        if (adjacentPartNumbers.count() == 2)
                            adjacentPartNumbers
                                .map { it.value.toInt() }
                                .reduce { acc, values ->
                                    acc * values
                                }
                        else 0
                    }
                    .reduce { acc, gearRatio -> acc + gearRatio }
            }
            .reduce { acc, rowGearRatio -> acc + rowGearRatio }

    }

    private fun parseInput(input: String): List<String> =
        input
            .splitToSequence('\n')
            .toList()

    private fun findPartNumberCandidates(row: String) =
        Regex("([0-9])+").findAll(row)

    private fun Char.isSymbol() =
        !(this.isLetterOrDigit() ||
            this == '.')

    private fun adiacentSymbolAvailable(row: String, range: IntRange) =
        row.substring(range).any { it.isSymbol() }

    private fun findGearCandidates(row: String) =
        Regex("\\*").findAll(row)
}