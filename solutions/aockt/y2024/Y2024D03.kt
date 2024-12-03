package aockt.y2024

import io.github.jadarma.aockt.core.Solution

object Y2024D03 : Solution {
    override fun partOne(input: String): Any {
        return calculateMulResultsSum(input)
    }

    override fun partTwo(input: String): Any {
        val splitRegex = Regex("don't\\(\\).*?(?:do\\(\\)|\\n)")
        val activeInput = input.replace('\n', ' ').plus('\n').split(splitRegex).reduce(String::plus)

        return calculateMulResultsSum(activeInput)
    }

    private fun calculateMulResultsSum(input: String): Int {
        val mulRegex = Regex("mul\\((\\d{1,3},\\d{1,3})\\)")

        return mulRegex.findAll(input)
            .map { match ->
                val groupValues = match.groupValues

                groupValues.subList(1, groupValues.size)
                    .map { numbers ->
                        numbers.split(',')
                            .map { it.toInt() }
                            .reduce(Int::times)
                    }
                    .reduce(Int::plus)
            }
            .reduce(Int::plus)
    }
}