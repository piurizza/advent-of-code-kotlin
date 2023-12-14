package aockt.y2023

import io.github.jadarma.aockt.core.Solution
import java.util.*


object Y2023D12 : Solution {
    override fun partOne(input: String): Int {
        val conditionRecords = parseInput(input, 1)

        return conditionRecords
            .map { conditionRecord ->
                calculateArrangements(conditionRecord)
            }
            .sumOf { it }
    }

    override fun partTwo(input: String): Any {
        val conditionRecords = parseInput(input, 5)

        return conditionRecords
            .map { conditionRecord ->
                calculateArrangements(conditionRecord)
            }
            .sumOf { it }
    }

    private fun parseInput(input: String, extensionFactor: Int) =
        input
            .split("\n")
            .map { row ->
                val format = row
                    .split(" ")

                ConditionRecord(
                    "${format[0]}?"
                        .repeat(extensionFactor)
                        .dropLast(1),
                    List(extensionFactor) {
                        format[1]
                            .split(",")
                            .map { it.toInt() }
                    }.flatten()
                )
            }

    private fun calculateArrangements(unknownConditionRecord: ConditionRecord): Int {
        val unknownPositions =
            Regex("\\?")
                .findAll(unknownConditionRecord.record)
                .map { match ->
                    match.range.first
                }
                .toList()

        val knownDamagedCount = unknownConditionRecord.record
            .count { it == Status.DAMAGED.charValue }

        val expectedDamagedCount = unknownConditionRecord.contiguousGroupSizes
            .sumOf { it }

        val remainingDamagedToDiscover = expectedDamagedCount - knownDamagedCount

        val remainingOperationalToDiscover = unknownPositions.size - remainingDamagedToDiscover

        val stringToPermute = (
            Status.OPERATIONAL.charValue
                .toString()
                .repeat(remainingOperationalToDiscover) +
                Status.DAMAGED.charValue
                    .toString()
                    .repeat(remainingDamagedToDiscover)
            )

        val permutations = permute(stringToPermute.toMutableList())

        var validArrangements = 0

        permutations
            .forEach { permutation ->
                val arrangement = unknownConditionRecord.record
                    .toMutableList()

                val permutationQueue: Queue<Char> = LinkedList(permutation.toList())

                unknownPositions
                    .forEach { index ->
                        arrangement[index] = permutationQueue.poll()
                    }

                if (
                    ConditionRecord(
                        arrangement
                            .joinToString(""),
                        unknownConditionRecord.contiguousGroupSizes
                    )
                        .isCoherentWithSizes()
                )
                    validArrangements++
            }

        return validArrangements
    }

    private fun permute(chars: MutableList<Char>): Set<MutableList<Char>> {
        val permutations: Queue<MutableList<Char>> = LinkedList()
        val v: MutableSet<MutableList<Char>> = HashSet()
        permutations.add(ArrayList(chars))

        while (permutations.isNotEmpty()) {
            val charList = permutations.poll()

            if (!v.contains(charList)) {
                v.add(charList)
                for (i in charList.indices) {
                    val c = charList[i]
                    charList.add(c)
                    charList.removeAt(i)
                    permutations.add(ArrayList(charList))
                    charList.add(i, c)
                    charList.removeAt(charList.size - 1)
                }
            }
        }
        return v
    }

    private fun ConditionRecord.isCoherentWithSizes(): Boolean {
        val calculatedGroupSizes = this.record
            .split(".")
            .filter { it.isNotBlank() }
            .map { it.length }

        return calculatedGroupSizes == this.contiguousGroupSizes

    }

    data class ConditionRecord(
        val record: String,
        val contiguousGroupSizes: List<Int>
    )

    enum class Status(val charValue: Char) {
        UNKNOWN('?'),
        OPERATIONAL('.'),
        DAMAGED('#')
    }
}