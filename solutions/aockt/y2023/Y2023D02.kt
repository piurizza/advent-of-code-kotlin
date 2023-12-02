package aockt.y2023

import io.github.jadarma.aockt.core.Solution
import kotlin.math.max

object Y2023D02 : Solution {

    private fun parseInput(input: String): List<String> =
        input
            .splitToSequence('\n')
            .toList()

    override fun partOne(input: String): Int {
        val maxDrawPossible = Draw(12, 13, 14)

        return parseInput(input)
            .map { gameString ->
                val gameId = extractGameId(gameString)
                val maxDrawInferred = inferMaxDraw(gameString)

                if (gameIsValid(maxDrawInferred, maxDrawPossible)) gameId.toInt()
                else 0
            }
            .reduce { acc, gameId -> acc + gameId }
    }

    override fun partTwo(input: String): Int {
        return parseInput(input)
            .map { gameString ->
                val maxDrawInferred = inferMaxDraw(gameString)
                maxDrawInferred.red * maxDrawInferred.green * maxDrawInferred.blue
            }
            .reduce { acc, gameId -> acc + gameId }
    }

    data class Draw(val red: Int, val green: Int, val blue: Int)

    private fun extractGameId(gameString: String) =
        gameString.substring(
            gameString.indexOf(" ") + 1 until gameString.indexOf(":")
        )

    private fun gameIsValid(maxDrawInferred: Draw, maxDrawPossible: Draw) =
        maxDrawInferred.red <= maxDrawPossible.red &&
            maxDrawInferred.blue <= maxDrawPossible.blue &&
            maxDrawInferred.green <= maxDrawPossible.green

    private fun inferMaxDraw(gameString: String): Draw {
        val startOfDrawsIndex = gameString.indexOf(":") + 1

        return gameString.substring(startOfDrawsIndex)
            .replace(" ", "")
            .split(";")
            .map { drawString ->
                val rgb = drawString.split(",")
                    .associate { colors ->
                        val indexOfFirstLetter = colors.indexOfFirst { it.isLetter() }
                        colors.substring(indexOfFirstLetter) to
                            colors.substring(0, indexOfFirstLetter).toInt()
                    }

                Draw(
                    rgb["red"] ?: 0,
                    rgb["green"] ?: 0,
                    rgb["blue"] ?: 0
                )
            }
            .reduce { acc, draw ->
                Draw(
                    max(acc.red, draw.red),
                    max(acc.green, draw.green),
                    max(acc.blue, draw.blue)
                )
            }
    }

}