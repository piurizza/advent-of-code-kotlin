package aockt.y2023

import io.github.jadarma.aockt.core.Solution
import kotlin.math.abs

object Y2023D08 : Solution {
    override fun partOne(input: String): Int {
        val parsedInput = parseInput(input)
        val instructions = parseInstructions(parsedInput)
        val nodes = parseNodes(parsedInput.drop(2))
        val currentNode = "AAA"
        val endNode = "ZZZ"

        return calculateSteps(
            instructions,
            nodes,
            currentNode,
            listOf(endNode)
        ).toInt()

    }

    override fun partTwo(input: String): Long {
        val parsedInput = parseInput(input)
        val instructions = parseInstructions(parsedInput)
        val nodes = parseNodes(parsedInput.drop(2))

        val currentNodes = nodes.keys
            .filter { key -> key.last() == 'A' }
            .toMutableList()

        val endNodes = nodes.keys
            .filter { key -> key.last() == 'Z' }

        return currentNodes
            .map { currentNode ->
                calculateSteps(
                    instructions,
                    nodes,
                    currentNode,
                    endNodes
                )
            }
            .reduce { acc, minSteps -> lcm(acc, minSteps) }
    }

    private fun parseInput(input: String) =
        input.split("\n")

    private fun parseInstructions(rows: List<String>) =
        rows[0]
            .replace("L", "0")
            .replace("R", "1")
            .split("")
            .drop(1)
            .dropLast(1)
            .map { it.toInt() }
            .toMutableList()

    private fun parseNodes(rows: List<String>) =
        rows
            .associate { row ->
                row.substring(0..2) to
                    listOf(
                        row.substring(7..9),
                        row.substring(12..14)
                    )
            }

    private fun calculateSteps(
        instructions: MutableList<Int>,
        nodes: Map<String, List<String>>,
        firstNode: String,
        endNodes: List<String>
    ): Long {
        var currentNode = firstNode
        var steps = 0L

        while (true) {
            instructions
                .forEach { instruction ->
                    currentNode = nodes[currentNode]?.get(instruction)
                        ?: throw IllegalStateException("Illegal node")

                    steps++

                    if (currentNode in endNodes) return steps
                }
        }
    }

    private fun gcd(a: Long, b: Long): Long {
        return if (b == 0L) a else gcd(b, a % b)
    }

    private fun lcm(a: Long, b: Long): Long {
        return if (a == 0L || b == 0L) 0
        else abs(a * b) / gcd(a, b)
    }
}