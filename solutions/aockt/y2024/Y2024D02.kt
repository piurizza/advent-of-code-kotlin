package aockt.y2024

import io.github.jadarma.aockt.core.Solution
import kotlin.math.abs

object Y2024D02 : Solution {
    override fun partOne(input: String): Any {
        val reports = parseInput(input)
        return reports.count { isSafeReport(it, maxErrors = 1) }
    }

    override fun partTwo(input: String): Any {
        val reports = parseInput(input)
        return reports.count { isSafeReport(it, maxErrors = 2) }
    }

    fun parseInput(input: String): List<List<Int>> {
        return input.split('\n').map {
            it.split(' ').map { it.toInt() }
        }
    }

    private fun isSafeReport(levels: List<Int>, maxErrors: Int): Boolean {
        return checkLevels(levels, maxErrors) or checkLevels(levels.asReversed(), maxErrors)
    }

    private fun checkLevels(levels: List<Int>, maxErrors: Int): Boolean {
        var reportType = ReportType.NOT_INITIALIZED
        var errorsCount = 0

        levels.reduce { currLevel, nextLevel ->
            val previousBadLevelsCount = errorsCount
            if (!isDifferenceAllowed(currLevel, nextLevel)) errorsCount += 1
            else
                if (currLevel > nextLevel) {
                    when (reportType) {
                        ReportType.NOT_INITIALIZED -> reportType = ReportType.DESCENDING
                        ReportType.ASCENDING -> errorsCount += 1
                        else -> {}
                    }
                } else if (currLevel < nextLevel) {
                    when (reportType) {
                        ReportType.NOT_INITIALIZED -> reportType = ReportType.ASCENDING
                        ReportType.DESCENDING -> errorsCount += 1
                        else -> {}
                    }
                }

            if (errorsCount == maxErrors) return false

            if (errorsCount > previousBadLevelsCount) currLevel else nextLevel
        }

        return true
    }

    private fun isDifferenceAllowed(currLevel: Int, nextLevel: Int): Boolean {
        return (abs(currLevel - nextLevel) in 1..3)
    }

}

enum class ReportType { ASCENDING, DESCENDING, NOT_INITIALIZED }