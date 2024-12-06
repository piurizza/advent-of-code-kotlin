package aockt.y2024

import io.github.jadarma.aockt.core.Solution

object Y2024D05 : Solution {
    override fun partOne(input: String): Any {
        val (rules, updates) = parseInput(input)
        val validUpdates = updates.zip(isValidUpdate(updates, rules)).filter { it.second }
        return validUpdates.sumOf { (update, _) -> update[update.size.div(2)] }
    }

    override fun partTwo(input: String): Any {
        val (rules, updates) = parseInput(input)
        val erroneousUpdates = updates.zip(isValidUpdate(updates, rules)).filterNot { it.second }
        val sortedUpdates = sortErroneousUpdates(erroneousUpdates.map { it.first }, rules)
        return sortedUpdates.sumOf { update -> update[update.size.div(2)] }
    }

    private fun parseInput(input: String): Pair<MutableMap<Int, MutableSet<Int>>, List<List<Int>>> {
        val (rawRules, rawUpdates) = input.split("\n\n")
        val rules = emptyMap<Int, MutableSet<Int>>().toMutableMap()

        rawRules.split('\n').map { rule ->
            val (x, y) = rule.split('|').map { it.toInt() }
            if (x in rules) rules[x]!!.add(y)
            else rules[x] = mutableSetOf(y)
        }

        val updates = rawUpdates.split('\n')
            .map { update -> update.split(',') }
            .map { update -> update.map { value -> value.toInt() } }

        return Pair(rules, updates)
    }

    private fun isValidUpdate(
        updates: List<List<Int>>,
        rules: MutableMap<Int, MutableSet<Int>>
    ): List<Boolean> {
        return updates.map { update ->
            update.forEachIndexed { index, page ->
                if (index != update.size - 1) {
                    val subsequentPages = update.subList((index + 1), update.size)
                    val isRuleBroken = subsequentPages.any { subsequentPage ->
                        if (subsequentPage !in rules) false
                        else page in rules[subsequentPage]!!
                    }
                    if (isRuleBroken) return@map false
                }
            }
            true
        }
    }

    private fun sortErroneousUpdates(
        updates: List<List<Int>>,
        rules: MutableMap<Int, MutableSet<Int>>
    ): List<List<Int>> {
        val rulesWithPre = rules.map { rule ->
            val preRequisites =
                rules.mapNotNull { otherRule -> if (rule.key in otherRule.value) setOf(otherRule.key) else null }
                    .reduceOrNull { acc, preRequisites -> acc.union(preRequisites) }

            rule.key to preRequisites
        }.toMap()

        return updates.map { update ->
            val wrongUpdate = update.toMutableList()
            val correctUpdate = emptyList<Int>().toMutableList()

            while (wrongUpdate.size != 1) {
                val correctPage = wrongUpdate.first { page ->
                    page in rulesWithPre && rulesWithPre[page]?.all { it !in wrongUpdate } ?: true
                }

                wrongUpdate.remove(correctPage)
                correctUpdate.add(correctPage)
            }

            correctUpdate.add(wrongUpdate.first())

            correctUpdate.toList()
        }
    }


}
