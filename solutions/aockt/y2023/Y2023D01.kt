package aockt.y2023

import io.github.jadarma.aockt.core.Solution


object Y2023D01 : Solution {
    override fun partOne(input: String): Int {
        return parseInput(input)
            .map { inputString ->
                val firstDigitIndex = inputString.indexOfFirst { char -> char.isDigit() }
                val lastDigitIndex = inputString.indexOfLast { char -> char.isDigit() }

                if (firstDigitIndex == lastDigitIndex)
                    "${inputString[firstDigitIndex]}${inputString[firstDigitIndex]}"
                        .toInt()
                else
                    "${inputString[firstDigitIndex]}${inputString[lastDigitIndex]}"
                        .toInt()
            }
            .reduce { acc, calibration -> acc + calibration }
    }

    override fun partTwo(input: String): Int {
        val spelledDigitsCandidates = arrayOf(
            "zero",
            "one",
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine"
        )

        return parseInput(input)
            .map { inputString ->
                val firstDigitIndex = inputString.indexOfFirst { char ->
                    char.isDigit()
                }.toNaturalNumberOrNull()

                val lastDigitIndex = inputString.indexOfLast { char ->
                    char.isDigit()
                }.toNaturalNumberOrNull()

                val firstSpelledDigit = extractSpelledDigit(
                    spelledDigitsCandidates,
                    inputString,
                    CalculationType.FIRST
                )

                val lastSpelledDigit = extractSpelledDigit(
                    spelledDigitsCandidates,
                    inputString,
                    CalculationType.LAST
                )

                val firstCalibrationPart = calculateCalibrationPart(
                    inputString,
                    firstDigitIndex,
                    firstSpelledDigit,
                    CalculationType.FIRST
                )

                val lastCalibrationPart = calculateCalibrationPart(
                    inputString,
                    lastDigitIndex,
                    lastSpelledDigit,
                    CalculationType.LAST
                )

                "$firstCalibrationPart$lastCalibrationPart".toInt()
            }
            .reduce { acc, calibration -> acc + calibration }
    }

    private fun parseInput(input: String): List<String> =
        input
            .splitToSequence('\n')
            .toList()

    private fun Int.toNaturalNumberOrNull() = if (this < 0) null else this

    private fun extractSpelledDigit(
        spelledDigitsCandidates: Array<String>,
        string: String,
        calculationType: CalculationType
    ): SpelledDigit {
        return when (calculationType) {
            CalculationType.FIRST ->
                spelledDigitsCandidates
                    .mapIndexed { index, candidate ->
                        index to string.indexOf(candidate)
                    }
                    .filter { it.second != -1 }
                    .reduceOrNull { acc, spelledDigit ->
                        if (spelledDigit.second < acc.second) spelledDigit
                        else acc
                    }
                    .let { SpelledDigit(it?.first, it?.second) }

            CalculationType.LAST ->
                spelledDigitsCandidates
                    .mapIndexed { index, candidate ->
                        index to string.lastIndexOf(candidate)
                    }
                    .filter { it.second != -1 }
                    .reduceOrNull { acc, spelledDigit ->
                        if (spelledDigit.second > acc.second) spelledDigit
                        else acc
                    }
                    .let { SpelledDigit(it?.first, it?.second) }
        }

    }

    private fun calculateCalibrationPart(
        inputString: String,
        digitIndex: Int?,
        spelledDigit: SpelledDigit,
        calculationType: CalculationType
    ): Int? {
        return when (calculationType) {
            CalculationType.FIRST ->
                digitIndex?.let {
                    spelledDigit.stringIndex?.let {
                        if (it < digitIndex) spelledDigit.value
                        else inputString[digitIndex].digitToInt()
                    } ?: inputString[digitIndex].digitToInt()
                } ?: spelledDigit.value

            CalculationType.LAST ->
                digitIndex?.let {
                    spelledDigit.stringIndex?.let {
                        if (it > digitIndex) spelledDigit.value
                        else inputString[digitIndex].digitToInt()
                    } ?: inputString[digitIndex].digitToInt()
                } ?: spelledDigit.value
        }

    }

    data class SpelledDigit(val value: Int?, val stringIndex: Int?)

    enum class CalculationType { FIRST, LAST }
}
