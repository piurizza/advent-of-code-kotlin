package aockt.y2024

import io.github.jadarma.aockt.core.Solution
import kotlin.math.min
import kotlin.math.max

object Y2024D01 : Solution {
    override fun partOne(input: String): Any {
        val (firstList, secondList) = parseInput(input)

        val result = firstList.sorted()
            .zip(
                secondList.sorted()
            )
            .map {
                max(it.first, it.second) - min(it.first, it.second)
            }
            .reduce(Int::plus)

        return result
    }

    override fun partTwo(input: String): Any {
        val (firstList, secondList) = parseInput(input)
        val appearanceFrequencies = mutableMapOf<Int, Int>()

        secondList.forEach { num ->
            if (num in appearanceFrequencies) appearanceFrequencies[num] = appearanceFrequencies[num]!!.plus(1)
            else appearanceFrequencies[num] = 1
        }

        val result = firstList
            .map { num ->
                if (num in appearanceFrequencies) num * appearanceFrequencies[num]!!
                else 0
            }
            .reduce(Int::plus)

        return result
    }

    fun parseInput(input: String): Pair<List<Int>, List<Int>> {
        val listsRegex = Regex("(\\d+) +(\\d+)")

        val listsPair = listsRegex.findAll(input)
            .map { row ->
                val (firstListValue, secondListValue) = row.destructured
                firstListValue to secondListValue
            }
            .toList()

        return Pair(listsPair.map { it.first.toInt() }, listsPair.map { it.second.toInt() })

    }
}