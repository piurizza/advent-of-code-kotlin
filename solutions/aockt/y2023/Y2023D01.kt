package aockt.y2023

import io.github.jadarma.aockt.core.Solution

/** A solution to a fictitious puzzle used for testing. */
object Y2023D01 : Solution {

    private fun parseInput(input: String): List<String> =
        input
            .splitToSequence('\n')
            .toList()

    override fun partOne(input: String): Int {
        return parseInput(input)
            .map { string ->
                val firstDigitIndex = string.indexOfFirst { char -> char.isDigit() }
                val lastDigitIndex = string.indexOfLast { char -> char.isDigit() }

                if (firstDigitIndex == lastDigitIndex)
                    "${string[firstDigitIndex]}${string[firstDigitIndex]}"
                        .toInt()
                else
                    "${string[firstDigitIndex]}${string[lastDigitIndex]}"
                        .toInt()
            }
            .reduce { acc, calibration -> acc + calibration  }
    }

    override fun partTwo(input: String) = TODO()
}
